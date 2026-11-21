package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantDefenderEntity;
import com.lying.pvz.utils.AlgorithmUtil;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

/**
 * 大蒜。
 *
 * <p>被僵尸啃咬（{@link PvZEntityDamageSource#isEatDamage()}）时，把啃咬者重定向到附近
 * 另一颗大蒜（{@link #updateGarlic()} 搜索范围内最近的、无仇恨关系的大蒜）。
 * 触发时播放 {@code YUCK} 音效。
 */
public class GarlicEntity extends PlantDefenderEntity {

	protected final AlgorithmUtil.EntitySorter sorter;
	private GarlicEntity garlic;

	public GarlicEntity(EntityType<? extends PlantDefenderEntity> type, Level worldIn) {
		super(type, worldIn);
		this.sorter = new AlgorithmUtil.EntitySorter(this);
	}

	/**
	 * 1.21.1 伤害入口：覆写 {@link #hurtServer}（{@code hurt} 不再是覆写点）。
	 */
	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		if (source instanceof PvZEntityDamageSource pvzSource
				&& pvzSource.isEatDamage()
				&& source.getEntity() instanceof Mob mob) {
			this.updateGarlic();
			if (this.garlic != null) {
				EntityUtil.playSound(mob, PvZSoundRegister.YUCK.get());
				mob.setTarget(this.garlic);
			}
		}
		return super.hurtServer(level, source, damage);
	}

	private void updateGarlic() {
		if (!EntityUtil.isEntityValid(this.garlic)
				|| !this.getSensing().hasLineOfSight(this.garlic)) {
			this.garlic = null;
			final float range = this.getChangeRange();
			final List<GarlicEntity> list = this.level().getEntitiesOfClass(
					GarlicEntity.class,
					EntityUtil.getEntityAABB(this, range, range),
					target -> !target.is(this)
							&& EntityUtil.isEntityValid(target)
							&& this.getSensing().hasLineOfSight(target)
							&& !EntityUtil.canTargetEntity(this, target));
			if (list.isEmpty()) {
				return;
			}
			Collections.sort(list, this.sorter);
			this.garlic = list.get(0);
		}
	}

	public float getChangeRange() {
		return 10;
	}

	@Override
	public float getLife() {
		return this.getSkillValue(SkillTypes.MORE_GARLIC_LIFE.get());
	}

	@Override
	public float getSuperLife() {
		return 0;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1.2F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.GARLIC.get();
	}
}