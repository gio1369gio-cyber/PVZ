package com.lying.pvz.common.level.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 水生/两栖生物的诱惑 AI。
 *
 * 与通用 TemptGoal 的核心差异，全部由“水生场景”推出，而非对齐某个版本：
 * 1. 停止/惊吓用水平距离：玩家站在岸上时垂直差不应阻止生物在下方水面停下。
 * 2. 忽略视线：跨水层/岸沿的 hasLineOfSight 判定不可靠，会吞掉最常见场景。
 * 3. 直接找“拿着食物的最近玩家”：避免近处空手玩家挡住远处拿食物的人。
 * 4. 排除创造/观察：创造玩家拿食物通常在建造/测试，不该被追。
 */
public class WaterTemptGoal extends Goal {

	private static final double DEFAULT_TEMPT_RANGE    = 10.0D;
	private static final double DEFAULT_STOP_DISTANCE  = 2.5D;
	private static final double SCARE_RADIUS           = 6.0D;
	private static final int    COOLDOWN_TICKS         = 100;
	private static final double MOVE_EPSILON_SQR       = 0.01D;
	private static final double ROT_EPSILON_DEG        = 5.0D;

	protected final PathfinderMob creature;
	private final double speed;
	private final Ingredient temptItem;
	private final boolean scaredByPlayerMovement;
	private final double stopDistance;

	/** 实例级条件：selector 需闭包访问 temptItem，不能是静态常量。 */
	private final TargetingConditions targetingConditions;

	protected Player closestPlayer;

	// 记录玩家上一 tick 的位置/视角，用于 scare 判定
	private double targetX, targetY, targetZ;
	private double lastPitch, lastYaw;

	private int delayTemptCounter;
	private boolean isRunning;

	public WaterTemptGoal(PathfinderMob creature, double speed, Ingredient temptItem, boolean scaredByPlayerMovement) {
		this(creature, speed, temptItem, scaredByPlayerMovement, DEFAULT_TEMPT_RANGE, DEFAULT_STOP_DISTANCE);
	}

	public WaterTemptGoal(PathfinderMob creature, double speed, Ingredient temptItem, boolean scaredByPlayerMovement,
						  double temptRange, double stopDistance) {
		this.creature = creature;
		this.speed = speed;
		this.temptItem = temptItem;
		this.scaredByPlayerMovement = scaredByPlayerMovement;
		this.stopDistance = stopDistance;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

		// 保留原类的存在理由：只服务水生/两栖导航器
		if (!(creature.getNavigation() instanceof WaterBoundPathNavigation)
				&& !(creature.getNavigation() instanceof AmphibiousPathNavigation)) {
			throw new IllegalArgumentException("Unsupported mob type for WaterTemptGoal: " + creature.getType());
		}

		// 需求 2：忽略视线；需求 3：物品检查进 selector，直接找“拿食物的最近玩家”
		this.targetingConditions = TargetingConditions.forNonCombat()
				.range(temptRange)                 // 构造时设置一次，避免 canUse 里反复 mutate
				.ignoreInvisibilityTesting()
				.ignoreLineOfSight()
				.selector((target, level) -> this.isValidTarget(target));
	}

	/** 需求 3 + 需求 4：只接受“非创造/观察 + 手持食物”的玩家。 */
	private boolean isValidTarget(LivingEntity target) {
		if (!(target instanceof Player player)) return false;
		if (player.isCreative() || player.isSpectator()) return false;
		return this.isTempting(player.getMainHandItem())
				|| this.isTempting(player.getOffhandItem());
	}

	protected boolean isTempting(ItemStack stack) {
		return this.temptItem.test(stack);
	}

	@Override
	public boolean canUse() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		}
		// getNearestPlayer(TargetingConditions, LivingEntity) 在 ServerLevel 上
		if (!(this.creature.level() instanceof ServerLevel serverLevel)) {
			return false;
		}
		this.closestPlayer = serverLevel.getNearestPlayer(this.targetingConditions, this.creature);
		return this.closestPlayer != null;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.closestPlayer == null) return false;

		if (this.scaredByPlayerMovement) {
			// scare 也用水平距离：玩家在岸上水平靠近时应当触发，而不是被垂直差“豁免”
			double horizontalDistSqr = horizontalDistSqr(this.closestPlayer);
			if (horizontalDistSqr < SCARE_RADIUS * SCARE_RADIUS) {
				// 玩家自身相对上次记录位置移动超过阈值 -> 惊吓
				if (this.closestPlayer.distanceToSqr(this.targetX, this.targetY, this.targetZ) > MOVE_EPSILON_SQR) {
					return false;
				}
				if (Math.abs(this.closestPlayer.getXRot() - this.lastPitch) > ROT_EPSILON_DEG
						|| Math.abs(this.closestPlayer.getYRot() - this.lastYaw) > ROT_EPSILON_DEG) {
					return false;
				}
			} else {
				this.targetX = this.closestPlayer.getX();
				this.targetY = this.closestPlayer.getY();
				this.targetZ = this.closestPlayer.getZ();
			}
			this.lastPitch = this.closestPlayer.getXRot();
			this.lastYaw   = this.closestPlayer.getYRot();
		}

		return this.canUse();
	}

	@Override
	public void start() {
		this.targetX   = this.closestPlayer.getX();
		this.targetY   = this.closestPlayer.getY();
		this.targetZ   = this.closestPlayer.getZ();
		this.lastPitch = this.closestPlayer.getXRot();
		this.lastYaw   = this.closestPlayer.getYRot();
		this.isRunning = true;
	}

	@Override
	public void stop() {
		this.closestPlayer = null;
		this.creature.getNavigation().stop();
		this.delayTemptCounter = COOLDOWN_TICKS;
		this.isRunning = false;
	}

	@Override
	public void tick() {
		this.creature.getLookControl().setLookAt(this.closestPlayer,
				(float) (this.creature.getMaxHeadYRot() + 20),
				(float) this.creature.getMaxHeadXRot());

		// 需求 1：水平距离停止——玩家在岸上、生物在水面下方时，水平接近即停
		if (this.horizontalDistSqr(this.closestPlayer) < this.stopDistance * this.stopDistance) {
			this.creature.getNavigation().stop();
		} else {
			this.creature.getNavigation().moveTo(this.closestPlayer, this.speed);
		}
	}

	/** 水平距离平方：y 用生物自身 y，抵消玩家站岸上的高度差。 */
	private double horizontalDistSqr(Player player) {
		return this.creature.distanceToSqr(player.getX(), this.creature.getY(), player.getZ());
	}

	public boolean isRunning() {
		return this.isRunning;
	}
}