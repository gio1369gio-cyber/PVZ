package com.lying.pvz.common.level.entity.zombie.pool;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMultiPart;
import com.lying.pvz.api.entity.ability.IHasWheel;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.zombie.base.CarZombieEntity;
import com.lying.pvz.common.level.entity.zombie.part.PVZZombiePartEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Optional;

/**
 * 冰车僵尸（ZOMBONI）。
 *
 * <p>继承 {@link CarZombieEntity}，本体 130 血、护甲韧性 12、行走
 * {@link ZombieUtil#WALK_SLOW}。核心机制为“碾冰造雪”：
 * <ul>
 *   <li>每 tick 在脚下 3 格半径内把水源转换为浮冰（内联 1.21.1 缺失的
 *       {@code FrostWalkerEnchantment.onEntityMoved} 逻辑）；</li>
 *   <li>在自身位置生成一层雪（{@link SnowLayerBlock#LAYERS} = 1），
 *       仅当目标位置空气或被雪覆盖时生效。</li>
 * </ul>
 *
 * <p>{@link #spikeWheelBy(LivingEntity)} 让尖刺类植物（地刺）对冰车造成
 * 2 倍最大生命伤害。携带 1 个 {@link PVZZombiePartEntity} 部件；
 * 死亡时通过 {@link #removeParts()} 清理。{@link #getHurtSound} 恒为
 * {@link PvZSoundRegister#METAL_HIT}（冰车外壳），生成音效
 * {@link PvZSoundRegister#CAR}。
 */
public class ZomboniEntity extends CarZombieEntity implements IHasMultiPart, IHasWheel {

	private PVZZombiePartEntity part;

	public ZomboniEntity(EntityType<? extends CarZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setImmuneAllEffects();
		this.resetParts();
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (!this.level().isClientSide()) {
			if (this.onGround()) {
				final BlockState frostedIce = Blocks.FROSTED_ICE.defaultBlockState();
				final int radius = 3;
				final BlockPos origin = this.blockPosition();
				final BlockPos.MutableBlockPos above = new BlockPos.MutableBlockPos();
				for (BlockPos pos : BlockPos.betweenClosed(
						origin.offset(-radius, -1, -radius),
						origin.offset(radius, -1, radius))) {
					if (pos.closerToCenterThan(this.position(), radius)) {
						above.set(pos.getX(), pos.getY() + 1, pos.getZ());
						if (this.level().getBlockState(above).isAir()) {
							BlockState below = this.level().getBlockState(pos);
							if (below.getFluidState().is(FluidTags.WATER)
									&& below.getFluidState().isSource()
									&& frostedIce.canSurvive(this.level(), pos)
									&& this.level().isUnobstructed(frostedIce, pos, CollisionContext.empty())) {
								this.level().setBlockAndUpdate(pos, frostedIce);
							}
						}
					}
				}
			}
			final BlockPos blockpos = this.blockPosition();
			final BlockState state = Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 1);
			if ((this.level().isEmptyBlock(blockpos)
					|| this.level().getBlockState(blockpos).getBlock() == Blocks.SNOW)
					&& state.canSurvive(this.level(), blockpos)) {
				this.level().setBlockAndUpdate(blockpos, state);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.updateParts();
	}

	@Override
	public void spikeWheelBy(LivingEntity entity) {
		if (this.level() instanceof ServerLevel serverLevel) {
			this.hurtServer(serverLevel, this.damageSources().thorns(entity),
					EntityUtil.getMaxHealthDamage(this, 2));
		}
	}

	@Override
	public void resetParts() {
		this.removeParts();
		this.part = new PVZZombiePartEntity(this, 1.2f, 1.5f);
		this.part.setOwner(this);
	}

	@Override
	public void removeParts() {
		if (this.part != null) {
			this.part.discard();
			this.part = null;
		}
	}

	@Override
	public void updateParts() {
		if (this.part != null) {
			if (!this.part.isAddedToLevel()) {
				this.level().addFreshEntity(this.part);
			}
			final float j = 2 * 3.14159f * this.getYRot() / 360;
			final float dis = this.getPartOffset();
			final Vec3 pos = this.position();
			this.part.yRotO = this.getYRot();
			this.part.xRotO = this.getXRot();
			this.part.snapTo(pos.x() - Math.sin(j) * dis, pos.y() + 0.2f,
					pos.z() + Math.cos(j) * dis, this.getYRot(), this.getXRot());
			this.part.setOwner(this);
		}
	}


	@Override
	public void remove(RemovalReason reason) {
		this.removeParts();
		super.remove(reason);
	}

	public float getPartOffset() {
		return 1.2f;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_SLOW;
	}

	@Override
	public float getLife() {
		return 130;
	}

	@Override
	public float getArmorToughness() {
		return 12f;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return PvZSoundRegister.METAL_HIT.get();
	}

	@Override
	public Optional<SoundEvent> getSpawnSound() {
		return Optional.ofNullable(PvZSoundRegister.CAR.get());
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8f, 2.3f);
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.ZOMBONI.get();
	}
}