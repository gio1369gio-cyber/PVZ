package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.plant.IPlantEntity;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.PlantInfo;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 南瓜。
 *
 * <p>外层容器型植物。本体无攻击，实际作用是作为 {@link PumpkinInfo} 被其他植物
 * {@code placeOuter} 时提供外层防御生命。super 模式把外层生命提升到
 * {@code SUPER_PUMPKIN_LIFE}；治疗按当前阶段上限 clamp。
 */
public class PumpkinEntity extends PlantEntity {

	public PumpkinEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.PUMPKIN.get();
	}

	@Override
	public int getSuperTimeLength() {
		return 0;
	}

	public static class PumpkinInfo extends PlantInfo {

		private static final float NORMAL_PUMPKIN_LIFE = 400;
		private static final float SUPER_PUMPKIN_LIFE = 800;

		@Override
		public void onSuper(IPlantEntity plantEntity) {
			super.onSuper(plantEntity);
			// 只把外层南瓜生命从 NORMAL 提升到 SUPER，不改变南瓜类型（不升级为钢南瓜）。
			plantEntity.setOuterDefenceLife(SUPER_PUMPKIN_LIFE);
		}

		@Override
		public void placeOn(IPlantEntity plantEntity, int sunCost) {
			super.placeOn(plantEntity, sunCost);
			plantEntity.setPumpkin(true);
			plantEntity.setOuterDefenceLife(NORMAL_PUMPKIN_LIFE);
		}

		@Override
		public void onHeal(IPlantEntity plantEntity, float percent) {
			final float max = plantEntity.getOuterDefenceLife() > NORMAL_PUMPKIN_LIFE
					? SUPER_PUMPKIN_LIFE : NORMAL_PUMPKIN_LIFE;
			plantEntity.setOuterDefenceLife(Mth.clamp(
					plantEntity.getOuterDefenceLife() * (1 + percent), 0, max));
		}
	}
}