package com.lying.pvz.common.level.entity.plant.base;

import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.projectile.IShooter;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.appease.StarFruitEntity;
import com.lying.pvz.common.level.entity.plant.spear.CatTailEntity;
import com.lying.pvz.common.level.entity.projectile.AbstractPAZProjectile;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * 直线射手型植物基类。攻击节奏由 {@code shootTick} 驱动，到达 {@code SHOOT_POINT_OFFSET}
 * 触发一次 {@link #startShootAttack()}，实际放出由 {@link #normalPlantTick()} 的 {@link #canAttackNow()} 决定。
 */
public abstract class PlantShooterEntity extends PlantEntity implements IShooter {
	private static final EntityDataAccessor<Integer> SHOOT_TICK =
			SynchedEntityData.defineId(PlantShooterEntity.class, EntityDataSerializers.INT);

	public static final float FORWARD_SHOOT_ANGLE = 0;
	public static final float BACK_SHOOT_ANGLE = 180;
	public static final float FORWARD_LEFT_SHOOT_ANGLE = -7.5F;
	public static final float FORWARD_RIGHT_SHOOT_ANGLE = 7.5F;
	public static final int SHOOT_ANIM_CD = 10;
	public static final int SHOOT_POINT = SHOOT_ANIM_CD * 3 / 4;
	public static final int SHOOT_POINT_OFFSET = SHOOT_ANIM_CD - SHOOT_POINT;

	public PlantShooterEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SHOOT_TICK, 0);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		final var followRange = this.getAttribute(Attributes.FOLLOW_RANGE);
		if (followRange != null) {
			followRange.setBaseValue(this.getShootRange());
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new ShooterAttackGoal(this));
		this.addTargetGoals();
	}

	protected void addTargetGoals() {
		this.targetSelector.addGoal(0, new ShooterNearestTargetGoal(
				this, true, false, this.getShootRange(), this.getShootHeight()));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide() && this.canAttackNow()) {
			this.shootBullet();
		}
		if (this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.BULLET_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.SHOOT_CD, this.getShootCD()),
				Pair.of(PAZAlmanacs.SHOOT_RANGE, this.getShootRange())
		));
	}

	public void performShoot(double forwardOffset, double rightOffset, double heightOffset,
							 boolean needSound, double angleOffset) {
		Optional.ofNullable(this.getTarget()).ifPresent(target -> {
			final Vec3 vec = EntityUtil.getNormalisedVector2d(this, target);
			final double deltaY = this.getDimensions(this.getPose()).height() * 0.7F + heightOffset;
			final double deltaX = forwardOffset * vec.x - rightOffset * vec.z;
			final double deltaZ = forwardOffset * vec.z + rightOffset * vec.x;
			final Vec3 pos = new Vec3(
					this.getX() + deltaX, this.getY() + deltaY, this.getZ() + deltaZ);

			final BulletKind kind = this.getBulletKind();
			if (kind != null && this.level() instanceof ServerLevel serverLevel) {
				final ShotSpec spec = new ShotSpec();
				this.configureShot(spec);

				Vec3 dir = new Vec3(
						target.getX() - pos.x,
						target.getY() + target.getBbHeight() * 0.5 - pos.y,
						target.getZ() - pos.z);

				// 贴脸：子弹起点已落入目标碰撞盒内时，朝「目标中心」的方向会因水平分量趋近 0
				// 而退化为垂直（子弹朝天飞）。此时改为水平朝目标射击，保证扫掠命中。
				if (target.getBoundingBox().contains(pos)) {
					dir = new Vec3(vec.x, 0.0D, vec.z);
				}
				dir = this.applyAngleOffset(dir.normalize(), angleOffset);

				final BulletEntity bullet = BulletLauncher.launch(serverLevel, this.getBulletType(), this,
						this.getEntityGroupType(), kind, spec, pos, target, dir);
				if (bullet != null) {
					this.onBulletLaunched(bullet);
				}
				if (needSound) {
					EntityUtil.playSound(this, this.getShootSound());
				}
				return;
			}

			final AbstractPAZProjectile bullet = this.createProjectile();
			bullet.setPos(pos.x, pos.y, pos.z);
			// 旧路径：shootFrom 快照 owner/group/技能/附魔
			bullet.shootFrom(this);
			bullet.setAttackDamage(this.getAttackDamage());

			// 等效于旧 shootPea(dx, dy, dz, speed, angleOffset)
			final double dx = target.getX() - bullet.getX();
			final double dy = target.getY() + target.getBbHeight() * 0.5 - bullet.getY();
			final double dz = target.getZ() - bullet.getZ();
			final Vec3 dir = this.applyAngleOffset(
					new Vec3(dx, dy, dz).normalize(), angleOffset);
			bullet.setDeltaMovement(dir.scale(this.getBulletSpeed()));

			if (needSound) {
				EntityUtil.playSound(this, this.getShootSound());
			}
			this.level().addFreshEntity(bullet);
		});
	}

	public void shootByAngle(float angle, float height) {
		angle *= (float) Math.PI / 180F;
		final double vx = -Mth.sin(angle);
		final double vz = Mth.cos(angle);

		final BulletKind kind = this.getBulletKind();
		if (kind != null && this.level() instanceof ServerLevel serverLevel) {
			final ShotSpec spec = new ShotSpec();
			this.configureShot(spec);
			final BulletEntity bullet = BulletLauncher.launch(serverLevel, this.getBulletType(), this,
					this.getEntityGroupType(), kind, spec,
					new Vec3(this.getX(), this.getY() + height, this.getZ()),
					null, new Vec3(vx, 0.0D, vz));
			if (bullet != null) {
				this.onBulletLaunched(bullet);
			}
			return;
		}

		final AbstractPAZProjectile bullet = this.createProjectile();
		bullet.setPos(this.getX(), this.getY() + height, this.getZ());
		bullet.shootFrom(this);
		bullet.setAttackDamage(this.getAttackDamage());
		bullet.setDeltaMovement(vx * this.getBulletSpeed(), 0, vz * this.getBulletSpeed());
		this.level().addFreshEntity(bullet);
	}

	/** 绕 Y 轴应用角度偏移（等效旧 shootPea 的 angleOffset）。 */
	private Vec3 applyAngleOffset(Vec3 dir, double angleOffset) {
		if (angleOffset == 0) {
			return dir;
		}
		final double rad = angleOffset * Math.PI / 180.0;
		final double cos = Math.cos(rad);
		final double sin = Math.sin(rad);
		return new Vec3(dir.x * cos - dir.z * sin, dir.y, dir.x * sin + dir.z * cos);
	}

	/* ================================================================== */
	/* ICanShoot（新架构引入）                                              */
	/* ================================================================== */

	@Override
	public AbstractPAZProjectile createProjectile() {
		final AbstractPAZProjectile bullet = this.createBullet();
		if (bullet == null) {
			throw new IllegalStateException(
					"Plant " + this.getPlantType().getID() + " uses BulletKind, not AbstractPAZProjectile");
		}
		return bullet;
	}

	/**
	 * 旧弹药工厂。已迁移到 {@link BulletKind} 的植物保持返回 {@code null}；
	 * 未迁移的植物继续覆写本方法。
	 */
	protected @Nullable AbstractPAZProjectile createBullet() {
		return null;
	}

	/**
	 * 新弹药 Kind。返回非 null 时 {@link #performShoot} 走新发射路径，
	 * {@link #createBullet()} 不再被调用。
	 */
	protected @Nullable BulletKind getBulletKind() {
		return null;
	}

	/** 新弹药 EntityType；{@link #getBulletKind()} 非 null 时必须返回非 null。 */
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return null;
	}

	/** 每发配置：变体、伤害覆盖、效果、角度等。 */
	protected void configureShot(ShotSpec spec) {
	}

	/** 新弹药发射成功后的回调（例如猫尾草缓存制导弹）。 */
	protected void onBulletLaunched(BulletEntity bullet) {
	}

	@Override
	public float getAttackRange() {
		return this.getShootRange();
	}

	@Override
	public int getAttackInterval() {
		return this.getShootCD();
	}

	protected SoundEvent getShootSound() {
		return SoundEvents.SNOW_GOLEM_SHOOT;
	}

	protected boolean canAttackNow() {
		return this.getAttackTime() > 0;
	}

	public abstract float getAttackDamage();

	@Override
	public boolean canPAZTarget(Entity entity) {
		return this.checkY(entity) && super.canPAZTarget(entity);
	}

	@Override
	public boolean checkY(Entity target) {
		final double dx = target.getX() - this.getX();
		final double dz = target.getZ() - this.getZ();
		final double minY = target.getY() - this.getY() - this.getEyeHeight();
		final double maxY = minY + target.getBbHeight();
		final double dis = Math.sqrt(dx * dx + dz * dz);
		final double y = dis / this.getMaxShootAngle();
		return minY < y && maxY > -y;
	}

	public double getMaxShootAngle() {
		return 60;
	}

	public float getShootRange() {
		return 30;
	}

	public float getShootHeight() {
		return 2;
	}

	public boolean canShoot() {
		return this.canNormalUpdate();
	}

	@Override
	public int getShootCD() {
		return 30;
	}

	@Override
	public float getBulletSpeed() {
		return 1.5F;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setShootTick(input.getIntOr("plant_shoot_tick", 0));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("plant_shoot_tick", this.getShootTick());
	}

	public int getShootTick() {
		return this.entityData.get(SHOOT_TICK);
	}

	public void setShootTick(int tick) {
		this.entityData.set(SHOOT_TICK, tick);
	}

	/* ================================================================== */
	/* AI                                                                  */
	/* ================================================================== */

	static class ShooterAttackGoal extends Goal {

		protected final PlantShooterEntity shooter;
		protected LivingEntity target;

		public ShooterAttackGoal(PlantShooterEntity shooter) {
			this.shooter = shooter;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			if (!this.shooter.canShoot()) {
				this.shooter.setShootTick(0);
				return false;
			}
			this.target = this.shooter.getTarget();
			if (!this.checkTarget()) {
				this.target = null;
				this.shooter.setTarget(null);
				return false;
			}
			return true;
		}

		@Override
		public boolean canContinueToUse() {
			return this.canUse();
		}

		@Override
		public void stop() {
			this.shooter.setShootTick(0);
		}

		@Override
		public void tick() {
			if (!(this.shooter instanceof StarFruitEntity)) {
				this.shooter.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
			}
			if (this.shooter.isPlantInSuperMode()) {
				this.shooter.startShootAttack();
				this.shooter.setShootTick(0);
				return;
			}
			final int time = this.shooter.getShootTick();
			final int total = this.shooter.getShootCD();
			if (time >= total) {
				this.shooter.setShootTick(0);
			} else {
				if (time == total - SHOOT_POINT_OFFSET) {
					this.shooter.startShootAttack();
				}
				this.shooter.setShootTick(time + 1);
			}
		}

		private boolean checkTarget() {
			if (EntityUtil.canAttackEntity(this.shooter, this.target)) {
				if (this.shooter instanceof CatTailEntity) {
					return EntityUtil.canSeeEntity(this.shooter, this.target);
				}
				return this.shooter.getSensing().hasLineOfSight(this.target);
			}
			return false;
		}
	}

	protected static class ShooterNearestTargetGoal extends PVZNearestTargetGoal {

		private final PlantShooterEntity shooter;

		public ShooterNearestTargetGoal(PlantShooterEntity mobIn, boolean checkSight,
										boolean memory, float w, float h) {
			super(mobIn, checkSight, memory, w, h);
			this.shooter = mobIn;
		}

		@Override
		protected boolean checkOther(LivingEntity entity) {
			return super.checkOther(entity) && this.shooter.checkY(entity);
		}
	}
}