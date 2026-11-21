package com.lying.pvz.common.level.entity.creature;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.lying.pvz.utils.AlgorithmUtil;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SnailEntity extends Animal {

	protected Optional<Entity> targetEntity = Optional.empty();

	public SnailEntity(EntityType<? extends Animal> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(3, new SnailNearestTargetGoal(this, 15));
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
		return null;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	public final class SnailNearestTargetGoal extends Goal {

		private final SnailEntity owner;
		private final Level world;
		private final float range;
		private final int targetChance = 10;
		private final AlgorithmUtil.EntitySorter sorter;

		public SnailNearestTargetGoal(SnailEntity snail, float range) {
			this.owner = snail;
			this.range = range;
			this.world = this.owner.level();
			this.sorter = new AlgorithmUtil.EntitySorter(this.owner);
		}

		@Override
		public boolean canUse() {
			if (this.owner.getRandom().nextInt(this.targetChance) != 0) {
				return false;
			}
			List<Entity> list = this.world.getEntitiesOfClass(Entity.class,
					EntityUtil.getEntityAABB(this.owner, this.range, this.range),
					(entity) -> true);
			if (list.isEmpty()) {
				return false;
			}
			Collections.sort(list, this.sorter);
			this.owner.targetEntity = Optional.of(list.get(0));
			return true;
		}
	}
}