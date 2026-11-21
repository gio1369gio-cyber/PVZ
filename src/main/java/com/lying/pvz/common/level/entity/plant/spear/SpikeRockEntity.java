package com.lying.pvz.common.level.entity.plant.spear;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 地刺王。继承 {@link SpikeWeedEntity}：{@link #getSpikesCount()} = {@code MORE_SPIKE} 技能值
 * （消耗完 {@code discard} 由父类处理），攻击间隔 {@link #getAttackCD()} = 20。
 */
public class SpikeRockEntity extends SpikeWeedEntity {

	public SpikeRockEntity(EntityType<? extends SpikeWeedEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setSpikeNum(this.getSpikesCount());
	}

	public int getSpikesCount() {
		return (int) this.getSkillValue(SkillTypes.MORE_SPIKE.get());
	}

	@Override
	public int getAttackCD() {
		return 20;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SPIKE_ROCK.get();
	}
}