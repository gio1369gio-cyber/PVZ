package com.lying.pvz.common.level.entity.plant.arma;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.common.impl.plant.CustomPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 黄油投手。
 *
 * <p>继承 {@link KernelPultEntity}，强制使用 {@link BulletKinds#BUTTER}。
 */
public class ButterPultEntity extends KernelPultEntity {

	public ButterPultEntity(EntityType<? extends KernelPultEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setCurrentBullet(CornTypes.BUTTER);
	}

	@Override
	protected @Nullable BulletKind getBulletKind() {
		return BulletKinds.BUTTER;
	}

	@Override
	protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
		return PvZEntityRegister.BUTTER.get();
	}

	@Override
	protected void changeBullet() {
		this.setCurrentBullet(CornTypes.BUTTER);
	}

	@Override
	public float getAttackDamage() {
		return 0.1F;
	}

	@Override
	public int getButterDuration() {
		return 90;
	}

	@Override
	public IPlantType getPlantType() {
		return CustomPlants.BUTTER_PULT.get();
	}
}