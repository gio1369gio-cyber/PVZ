package com.lying.pvz.common.level.entity.plant.base;

import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.api.entity.ability.ICanBeAttracted;
import com.lying.pvz.common.level.entity.ai.goal.misc.PlantAttractGoal;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

/**
 * 防御型植物基类。实现 {@link ICanAttract} 标记接口，
 * 吸引逻辑通过自身 public 方法 {@link #canAttract} / {@link #attract} 暴露给 {@code PlantAttractGoal}。
 */
public abstract class PlantDefenderEntity extends PlantEntity implements ICanAttract {
	public PlantDefenderEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PlantAttractGoal(this, this, 20));
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.ARMOR, this.getArmor()),
				Pair.of(PAZAlmanacs.ARMOR_TOUGHNESS, this.getArmorToughness())
		));
	}

	/**
	 * 新架构：覆写 {@link #onSuperStart(boolean)}（{@code startSuperMode} 已委托到此处）。
	 * 进入 super 时把内层防御生命提升到 {@link #getSuperLife()}。
	 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.setInnerDefenceLife(this.getSuperLife());
	}

	@Override
	public boolean canAttract(LivingEntity entity) {
		if (entity instanceof ICanBeAttracted attracted && !attracted.canBeAttractedBy(this)) {
			return false;
		}
		return this.getSensing().hasLineOfSight(entity);
	}

	@Override
	public void attract(LivingEntity target) {
		if (target instanceof Mob mob && !(mob.getTarget() instanceof ICanBeAttracted)) {
			mob.setTarget(this);
		}
		if (target instanceof ICanBeAttracted attracted) {
			attracted.attractBy(this);
		}
	}

	@Override
	public float getAttractRange() {
		return 2.5F;
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	public abstract float getSuperLife();
}