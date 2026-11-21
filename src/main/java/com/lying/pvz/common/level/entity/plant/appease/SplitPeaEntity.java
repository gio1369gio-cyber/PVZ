package com.lying.pvz.common.level.entity.plant.appease;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.zombie.pool.DiggerZombieEntity;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 分裂豌豆。
 *
 * <p>{@link #ROUND_TICK} 控制朝向：0 表示朝前，{@code MAX_ROUND_TIME/2} 表示朝后。
 * 前后发射数量按 {@link #isFacingFront()} 分配；攻击结束按 {@link #getDoubleChance()}
 * 概率翻转朝向。
 */
public class SplitPeaEntity extends PeaShooterEntity {

	private static final EntityDataAccessor<Integer> ROUND_TICK =
			SynchedEntityData.defineId(SplitPeaEntity.class, EntityDataSerializers.INT);
	public static final int MAX_ROUND_TIME = 20;

	public SplitPeaEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ROUND_TICK, 0);
	}

	@Override
	protected void plantTick() {
		super.plantTick();
		if (!this.level().isClientSide()) {
			if (this.getRoundTick() != 0 && this.getRoundTick() != MAX_ROUND_TIME / 2) {
				this.rotateFacing();
			}
		}
	}

	@Override
	public void shootBullet() {
		if (this.isPlantInSuperMode()) {
			final int cnt = this.getSuperShootCount();
			for (int i = 0; i < cnt; ++i) {
				final float offset = MathUtil.getRandomFloat(this.getRandom()) / 3;
				final float offsetH = MathUtil.getRandomFloat(this.getRandom()) / 3;
				this.performShoot(SHOOT_OFFSET, offset, offsetH,
						this.getExistTick() % 10 == 0, FORWARD_SHOOT_ANGLE);
				this.performShoot(SHOOT_OFFSET, offset, offsetH,
						false, BACK_SHOOT_ANGLE);
			}
		} else {
			final int frontNum = this.isFacingFront() ? 1 : 2;
			final int backNum = this.isFacingFront() ? 2 : 1;
			if (this.getAttackTime() <= frontNum) {
				this.performShoot(SHOOT_OFFSET, 0, 0,
						this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
			}
			if (this.getAttackTime() <= backNum) {
				this.performShoot(SHOOT_OFFSET, 0, 0, false, BACK_SHOOT_ANGLE);
			}
			this.checkAndChangeFacing();
		}
	}

	private void checkAndChangeFacing() {
		if (this.getAttackTime() == 1 && !this.isPlantInSuperMode()) {
			final float chance = this.isFacingFront()
					? this.getDoubleChance() : 1 - this.getDoubleChance();
			if (this.getRandom().nextFloat() < chance) {
				this.rotateFacing();
			}
		}
	}

	private void rotateFacing() {
		this.setRoundTick((this.getRoundTick() + 1) % MAX_ROUND_TIME);
	}

	public float getDoubleChance() {
		return this.getSkillValue(SkillTypes.SPLIT_DOUBLE_CHANCE.get());
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(2);
	}

	public boolean isFacingFront() {
		return this.getRoundTick() == 0;
	}

	@Override
	public double getMaxShootAngle() {
		return 5;
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		if (entity instanceof DiggerZombieEntity) {
			return this.checkY(entity);
		}
		return super.canPAZTarget(entity);
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setRoundTick(input.getIntOr("round_tick", 0));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("round_tick", this.getRoundTick());
	}

	public int getRoundTick() {
		return this.entityData.get(ROUND_TICK);
	}

	public void setRoundTick(int tick) {
		this.entityData.set(ROUND_TICK, tick);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SPLIT_PEA.get();
	}
}