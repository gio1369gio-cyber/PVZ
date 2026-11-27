package com.lying.pvz.common.level.entity.zombie.custom;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.level.entity.ai.navigator.LavaZombiePathNavigator;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.level.entity.zombie.base.SwimmerZombieEntity;
import com.lying.pvz.common.misc.PvZLoot;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;

/**
 * 熔岩僵尸。
 *
 * <p>继承 {@link SwimmerZombieEntity}，在“水中姿态切换”的基础上扩展为“岩浆中站立/漂浮”：
 * <ul>
 *   <li>{@link PathType#LAVA} 路径成本置 0，寻路优先走岩浆；</li>
 *   <li>{@link #canStandOnFluid(FluidState)} 对岩浆返回 {@code true}，
 *       配合 {@link #floatInLava()} 在岩浆表面托起本体；</li>
 *   <li>{@link #getWalkTargetValue(BlockPos, LevelReader)} 在岩浆块上返回 10，
 *       在岩浆中返回 {@code -∞} 避免自陷。</li>
 * </ul>
 *
 * <p>攻击命中目标点燃 5 秒；移动速度 {@link ZombieUtil#WALK_FAST}，攻击
 * {@link ZombieUtil#LITTLE_LOW}；水中减速 0.935。
 *
 * <p>寻路使用 {@link LavaZombiePathNavigator}，用于评估“能否穿越岩浆”。
 * 掉落表沿用 {@link PvZLoot#LAVA_ZOMBIE}。
 */
public class LavaZombieEntity extends SwimmerZombieEntity {

	public LavaZombieEntity(EntityType<? extends SwimmerZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setPathfindingMalus(PathType.LAVA, 0.0F);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_FAST);
		}
		AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (attackDamage != null) {
			attackDamage.setBaseValue(ZombieUtil.LITTLE_LOW);
		}
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		this.floatInLava();
	}

	private void floatInLava() {
		if (this.isInLava() && !this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.06D, 0.0D));
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity target) {
		target.igniteForSeconds(5);
		return super.doHurtTarget(level, target);
	}

	@Override
	public float getLife() {
		return 80;
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.935f;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
		if (worldIn.getBlockState(pos).getFluidState().is(FluidTags.LAVA)) {
			return 10.0F;
		}
		return this.isInLava() ? Float.NEGATIVE_INFINITY : 0.0F;
	}

	@Override
	public boolean canStandOnFluid(FluidState fluidState) {
		return fluidState.is(FluidTags.LAVA);
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new LavaZombiePathNavigator(this, level);
	}



	@Override
	public IZombieType getZombieType() {
		return CustomZombies.LAVA_ZOMBIE.get();
	}
}