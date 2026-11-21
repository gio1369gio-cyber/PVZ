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
 * 卷心菜投手。
 *
 * 发射流程由父类 {@link PlantPultEntity#performPult} 统一处理（{@code shootFrom} + {@code aimAt}）。
 */
public class CabbagePultEntity extends PlantPultEntity {

	public CabbagePultEntity(EntityType<? extends PlantPultEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.CABBAGE;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.CABBAGE.get();
	}

	@Override
	protected void configureShot(ShotSpec spec) {
		spec.baseDamage(this.isPlantInSuperMode() ? this.getSuperDamage() : this.getAttackDamage())
				.variant(this.getCabbageVariant());
	}

	/** 卷心菜变体：普通卷心菜 / 冰卷心菜。冰卷心菜投手覆写返回 {@link BulletKinds#CABBAGE_ICE}。 */
	protected int getCabbageVariant() {
		return BulletKinds.CABBAGE_NORMAL;
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_CABBAGE_DAMAGE.get());
	}

	@Override
	public float getSuperDamage() {
		return this.getAttackDamage() + 20;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.CABBAGE_PULT.get();
	}
}