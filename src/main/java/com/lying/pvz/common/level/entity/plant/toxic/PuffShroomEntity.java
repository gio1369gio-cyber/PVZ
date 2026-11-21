package com.lying.pvz.common.level.entity.plant.toxic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 小喷菇。
 *
 * <p>发射短生命周期的 {@link com.lying.pvz.common.projectile.BulletKinds#SPORE}（10 tick）。super 模式下：
 * <ul>
 *   <li>自身进入 super；</li>
 *   <li>若 {@code first == true}，联动附近同 owner 的 PuffShroom 一起进入 super
 *       （最多 {@link #getMaxSuperCnt()} 只，含自身）；</li>
 *   <li>触发 {@code ENTITY_EFFECT_AMOUNT} 成就计数。</li>
 * </ul>
 *
 * <p>递归控制：{@link #onSuperStart(boolean)} 中 {@code first == false} 分支不再联动其他 PuffShroom。
 */
public class PuffShroomEntity extends PlantShooterEntity {

	protected static final double SHOOT_OFFSET = 0.1D;

	public PuffShroomEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void shootBullet() {
		if (this.isPlantInSuperMode()) {
			this.performShoot(SHOOT_OFFSET, 0, -0.2F,
					this.getExistTick() % 5 == 0, FORWARD_SHOOT_ANGLE);
		} else {
			this.performShoot(SHOOT_OFFSET, 0, -0.2F,
					this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
		}
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.SPORE_DAMAGE.get());
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.SPORE;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.SPORE.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		spec.baseDamage(this.getAttackDamage()).lifeTicks(10);
	}

	@Override
	protected SoundEvent getShootSound() {
		return PvZSoundRegister.PUFF.get();
	}

	/**
	 * 新架构：覆写 {@link #onSuperStart(boolean)}。
	 *
	 * <p>父类 {@link com.lying.pvz.common.level.entity.plant.PlantEntity#startSuperMode(boolean)}
	 * 委托到本方法。{@code first == false} 表示被其他 PuffShroom 联动触发，不再继续扩散。
	 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		if (!first) {
			return;
		}
		int cnt = 1;
		final int range = 20;
		for (PuffShroomEntity shroom : this.level().getEntitiesOfClass(PuffShroomEntity.class,
				EntityUtil.getEntityAABB(this, range, range), this::canSuperTogether)) {
			if (shroom.canStartSuperMode()) {
				shroom.startSuperMode(false);
				++cnt;
				if (cnt >= this.getMaxSuperCnt()) {
					break;
				}
			}
		}
		final Player player = EntityUtil.getEntityOwner(this.level(), this);
		if (player instanceof ServerPlayer sp) {
			PvZTriggers.ENTITY_EFFECT_AMOUNT.get().trigger(sp, this, cnt);
		}
	}

	protected boolean canSuperTogether(PuffShroomEntity entity) {
		if (EntityUtil.canTargetEntity(this, entity)
				|| entity.getPlantType() != this.getPlantType()) {
			return false;
		}
		return this.getOwnerUUID().isPresent()
				&& entity.getOwnerUUID().isPresent()
				&& entity.getOwnerUUID().get().equals(this.getOwnerUUID().get());
	}

	public int getMaxSuperCnt() {
		return 10;
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(1);
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.5F, 0.5F);
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	@Override
	protected float getLife() {
		return 10;
	}

	@Override
	public float getShootRange() {
		return 10;
	}

	@Override
	public float getShootHeight() {
		return 2;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.PUFF_SHROOM.get();
	}
}