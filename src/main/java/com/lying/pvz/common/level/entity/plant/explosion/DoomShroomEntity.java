package com.lying.pvz.common.level.entity.plant.explosion;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.misc.DoomFixerEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantBomberEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.WorldUtil;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import java.util.ArrayList;
import java.util.List;

/**
 * 毁灭菇。范围 10.5 格、高伤 {@link SkillTypes#HIGH_EXPLODE_DAMAGE}；
 * 视 {@code EventHooks.canEntityGrief} 决定是否清除 5×5×2 加球体半径 10 内的方块。
 * 倒计时 {@link #getReadyTime()} 前 2 tick 生成 {@link DoomFixerEntity} 修复地形。
 */
public class DoomShroomEntity extends PlantBomberEntity {

	public static final float MAX_EXPLOSION_LEVEL = 500;

	public DoomShroomEntity(EntityType<? extends PlantBomberEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (this.getAttackTime() == this.getReadyTime() - 2) {
			final DoomFixerEntity fixer = PvZEntityRegister.DOOM_FIXER.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (fixer == null) {
				return;
			}
			EntityUtil.onEntitySpawn(this.level(), fixer, this.blockPosition());
		}
	}

	@Override
	protected void startBomb(boolean server) {
		if (server) {
			final float range = this.getExplodeRange();
			final AABB aabb = EntityUtil.getEntityAABB(this, range, range);
			EntityUtil.getWholeTargetableEntitiesIgnoreFog(this, aabb).forEach(target -> {
				if (target instanceof EnderDragon dragon) {
					dragon.hurt(this.damageSources().explosion(this, this),
							this.getExplodeDamage() * 2);
				} else {
					target.hurt(PvZEntityDamageSource.explode(this), this.getExplodeDamage());
				}
			});
			PlantEntity.clearLadders(this, aabb);
			EntityUtil.playSound(this, PvZSoundRegister.DOOM_SHROOM.get());
			if (this.level() instanceof ServerLevel serverLevel) {
				if (EventHooks.canEntityGrief(serverLevel, this)) {
					this.destroyBlocks();
				}
			}
		} else {
			for (int i = 0; i < 300; ++i) {
				WorldUtil.spawnRandomSpeedParticle(this.level(), PvZParticleRegister.SPORE.get(),
						this.position().add(0, 1, 0), 0.8F);
			}
		}
	}

	@SuppressWarnings("deprecation")
	protected void destroyBlocks() {
		final ObjectArrayList<Pair<ItemStack, BlockPos>> list = new ObjectArrayList<>();
		final List<BlockPos> posList = new ArrayList<>();
		final int len = 2;
		for (int i = -len; i <= len; ++i) {
			for (int j = -len; j <= len; ++j) {
				for (int k = -2; k < 0; ++k) {
					posList.add(this.blockPosition().offset(i, k, j));
				}
			}
		}
		final int range = 10;
		for (int h = 0; h <= range + 8; ++h) {
			for (int i = -range; i <= range; ++i) {
				for (int j = -range; j <= range; ++j) {
					if (new Vec3(i, h - 5, j).lengthSqr() <= range * range) {
						posList.add(this.blockPosition().offset(i, h, j));
					}
				}
			}
		}
		posList.forEach(pos -> {
			final BlockState state = this.level().getBlockState(pos);
			if (state.isAir() || state.getBlock().getExplosionResistance() > MAX_EXPLOSION_LEVEL) {
				return;
			}
			final BlockEntity blockEntity = state.hasBlockEntity() ? this.level().getBlockEntity(pos) : null;
			final LootParams.Builder loot = new LootParams.Builder((ServerLevel) this.level())
					.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
					.withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
					.withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
					.withOptionalParameter(LootContextParams.THIS_ENTITY, this)
					.withParameter(LootContextParams.EXPLOSION_RADIUS, (float) len);
			state.getDrops(loot).forEach(stack -> {
				for (int l = 0; l < list.size(); ++l) {
					final Pair<ItemStack, BlockPos> pair = list.get(l);
					final ItemStack existing = pair.getFirst();
					if (ItemEntity.areMergable(existing, stack)) {
						final ItemStack merged = ItemEntity.merge(existing, stack, 16);
						list.set(l, Pair.of(merged, pair.getSecond()));
					}
				}
				list.add(Pair.of(stack, pos));
			});
			this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		});
		for (Pair<ItemStack, BlockPos> pair : list) {
			Block.popResource(this.level(), pair.getSecond(), pair.getFirst());
		}
	}

	@Override
	public float getExplodeDamage() {
		return this.getSkillValue(SkillTypes.HIGH_EXPLODE_DAMAGE.get());
	}

	@Override
	public float getExplodeRange() {
		return 10.5F;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1.5F);
	}

	@Override
	public int getReadyTime() {
		return 50;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.DOOM_SHROOM.get();
	}
}