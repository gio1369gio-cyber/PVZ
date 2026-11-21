package com.lying.pvz.common.level.entity.zombie.pool;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 撑杆僵尸。
 *
 * <p>本体 48 血。{@code HAS_POGO} 控制两种形态：
 * <ul>
 *   <li><b>有撑杆</b>：站在地面或水中时每 {@code JUMP_CD} tick 向上弹跳，
 *       寒冷/黄油无效（{@link #canBeFrozen} / {@link #canBeButtered}），
 *       不可破植物方块；</li>
 *   <li><b>无撑杆</b>：恢复常规僵尸行为。</li>
 * </ul>
 *
 * <p>{@link #canBeAttractedBy} 对 {@link PVZPlants#TALL_NUT} 返回 true，
 * 触发 {@link #attractBy} 时立即丢杆。{@code POGO} 在新 {@link MetalTypes}
 * 不存在，{@link #getMetalType()} 返回 {@link MetalTypes#EMPTY}。
 */
public class PogoZombieEntity extends ZombieEntity implements IHasMetal {

	private static final EntityDataAccessor<Boolean> HAS_POGO =
			SynchedEntityData.defineId(PogoZombieEntity.class, EntityDataSerializers.BOOLEAN);
	private static final int JUMP_CD = 10;

	public PogoZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HAS_POGO, true);
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			this.setAttackTime((this.getAttackTime() + 1) % JUMP_CD);
			if (this.hasPogo() && (this.onGround() || this.isInWater())
					&& this.getAttackTime() % JUMP_CD == 0) {
				final double motionY = 0.85D + MathUtil.getRandomFloat(this.getRandom()) * 0.1D;
				this.setDeltaMovement(this.getDeltaMovement().x(), motionY, this.getDeltaMovement().z());
				EntityUtil.playSound(this, PvZSoundRegister.POGO.get());
			}
		}
	}

	@Override
	public boolean canBreakPlantBlock() {
		return super.canBreakPlantBlock() && !this.hasMetal();
	}

	@Override
	public boolean canBeAttractedBy(ICanAttract defender) {
		if (defender instanceof PlantEntity plant) {
			final IPlantType type = plant.getPlantType();
			return type == PVZPlants.TALL_NUT.get();
		}
		return true;
	}

	@Override
	public void attractBy(ICanAttract defender) {
		super.attractBy(defender);
		if (this.hasPogo()) {
			this.setPogo(false);
			EntityUtil.playSound(this, PvZSoundRegister.HAMMER_BONK.get());
		}
	}

	@Override
	public boolean hasMetal() {
		return this.hasPogo();
	}

	@Override
	public void decreaseMetal() {
		this.setPogo(false);
	}

	@Override
	public void increaseMetal() {
		this.setPogo(true);
	}

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.POGO;
	}

	@Override
	public boolean canBeFrozen() {
		return !this.hasPogo();
	}

	@Override
	public boolean canBeButtered() {
		return !this.hasPogo();
	}

	@Override
	public float getLife() {
		return 48;
	}

	public boolean hasPogo() {
		return this.entityData.get(HAS_POGO);
	}

	public void setPogo(boolean has) {
		this.entityData.set(HAS_POGO, has);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setPogo(input.getBooleanOr("has_pogo", true));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("has_pogo", this.hasPogo());
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.POGO_ZOMBIE.get();
	}
}