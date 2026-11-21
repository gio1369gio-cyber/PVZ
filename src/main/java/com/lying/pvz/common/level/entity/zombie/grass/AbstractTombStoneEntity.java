package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.card.CardContent;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.assist.GraveBusterEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.item.card.CardPlacementService;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.utils.ZombieUtil;
import com.lying.pvz.utils.others.WeightList;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 墓碑类僵尸基类。
 *
 * <p>墓碑不是战斗单位，而是“格子占用 + 召唤物投放点”：
 * <ul>
 *   <li>无 AI goal、无移动、无攻击、不可被选为目标、不可被魅惑；</li>
 *   <li>每 tick 把自身对齐到方块中心，位置锁定在 {@code blockPos + 0.5}；</li>
 *   <li>玩家手持 {@link PlantCardItem} 且目标类型为 {@link PVZPlants#GRAVE_BUSTER}
 *       时交互，令墓碑吞噬该植物并在植物侧回填 {@code target} 为自身；</li>
 *   <li>掉落池继承自 {@link ZombieEntity} 的 {@code NORMAL_DROP_LIST}，
 *       左侧铜币置为 {@code COPPER}，但 {} 走统一枚举。</li>
 * </ul>
 *
 * <p>子类只需决定血量、可否被魅惑、召唤行为；{@link TombStoneEntity} 与
 * {@link com.lying.pvz.common.level.entity.zombie.custom.GigaTombStoneEntity}
 * 分别承担普通与巨型墓碑。
 */
public abstract class AbstractTombStoneEntity extends ZombieEntity {

	protected static final WeightList<DropType> TOMBSTONE_DROP_LIST = new WeightList<>();

	public AbstractTombStoneEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setImmuneAllEffects();
		this.setIsWholeBody();
		this.canBeMini = false;
		this.canBeStealByBungee = false;
		this.canCollideWithZombie = false;
		this.canHelpAttack = false;
	}

	@Override
	protected void registerGoals() {
	}

	@Override
	protected VariantType rollVariantType() {
		return VariantType.NORMAL;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			BlockPos pos = this.blockPosition();
			this.setPos(pos.getX() + 0.5, this.getY(), pos.getZ() + 0.5);
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.level().isClientSide()) return InteractionResult.PASS;

		final ItemStack stack = player.getItemInHand(hand);
		if (!(stack.getItem() instanceof PlantCardItem card)) return InteractionResult.PASS;

		final CardContent content = card.content(stack);
		if (content.isEmpty()) return InteractionResult.PASS;

		if (!CardPlacementService.checkCommon(player, stack, content)) {
			return InteractionResult.PASS;
		}
		return card.trySpawnAndApply(player, this, stack, content,
				type -> type == PVZPlants.GRAVE_BUSTER.get(),
				plant -> {
					if (plant instanceof GraveBusterEntity g) g.setTarget(this);
				})
				? InteractionResult.SUCCESS_SERVER
				: InteractionResult.PASS;
	}
	@Override
	public float getWalkSpeed() {
		return 0;
	}

	@Override
	public float getEatDamage() {
		return 0;
	}

	@Override
	public float getFollowRange() {
		return ZombieUtil.CLOSE_TARGET_RANGE;
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return false;
	}

	@Override
	protected WeightList<DropType> getDropSpecialList() {
		if (TOMBSTONE_DROP_LIST.isEmpty()) {
			TOMBSTONE_DROP_LIST.addAll(super.getDropSpecialList());
			TOMBSTONE_DROP_LIST.setLeftItem(DropType.COPPER);
		}
		return TOMBSTONE_DROP_LIST;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return EntityDimensions.scalable(0.8F, 1.6F);
	}

	@Override
	public float getLife() {
		return 70;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}
}