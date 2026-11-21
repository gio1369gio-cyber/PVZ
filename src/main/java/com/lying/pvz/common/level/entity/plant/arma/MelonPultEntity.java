package com.lying.pvz.common.level.entity.plant.arma;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantPultEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 西瓜投手。
 *
 * <p>变体：{@link BulletKinds#MELON_NORMAL} / {@link BulletKinds#MELON_ICE}。
 * 寒冰西瓜覆写 {@link #getMelonVariant()} 并在 {@link #configureShot} 追加寒冷。
 */
public class MelonPultEntity extends PlantPultEntity {

	public MelonPultEntity(EntityType<? extends PlantPultEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.MELON;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.MELON.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		spec.baseDamage(this.isPlantInSuperMode() ? this.getSuperDamage() : this.getAttackDamage())
				.variant(this.getMelonVariant());
	}

	protected int getMelonVariant() {
		return BulletKinds.MELON_NORMAL;
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_MELON_DAMAGE.get());
	}

	@Override
	public float getSuperDamage() {
		return this.getAttackDamage() + 15;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 1F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.MELON_PULT.get();
	}
}