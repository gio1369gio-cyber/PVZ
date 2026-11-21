package com.lying.pvz.common.level.entity.zombie.part;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.MultiPartEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * 僵尸部件基类。
 *
 * <p>继承 {@link MultiPartEntity}，把伤害转发给宿主僵尸：
 * {@link #hurtServer(ServerLevel, DamageSource, float)} 在宿主有效时调用
 * {@code owner.hurtServer(...)}，因此部件本身不持有生命值，命中判定与减伤
 * 全部走宿主的伤害管线。
 *
 * <p>{@link #onOwnerBeMini(ZombieEntity)} 按迷你比例缩放部件尺寸；
 * 迷你僵尸部件为原尺寸的 0.4 倍。
 *
 * <p>{@link #getZombie()} 是 {@link #getOwner()} 的窄化视图：宿主不是
 * {@link ZombieEntity} 时记录 warn 并返回 {@link Optional#empty()}，
 * 避免调用方在 {@code instanceof} 之前就产生 NPE。
 */
public class PVZZombiePartEntity extends MultiPartEntity {

	public PVZZombiePartEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	public PVZZombiePartEntity(ZombieEntity owner, float sizeX, float sizeY) {
		super(PvZEntityRegister.ZOMBIE_PART.get(), owner, sizeX, sizeY);
	}

	public void onOwnerBeMini(ZombieEntity zombie) {
		final float scale = zombie.isMiniZombie() ? 0.4F : 1F;
		this.setPartWidth(this.MaxWidth * scale);
		this.setPartHeight(this.MaxHeight * scale);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		final LivingEntity owner = this.getOwner();
		if (EntityUtil.isEntityValid(owner)) {
			return owner.hurtServer(level, source, damage);
		}
		return false;
	}

	public Optional<ZombieEntity> getZombie() {
		final LivingEntity owner = this.getOwner();
		if (!(owner instanceof ZombieEntity zombie)) {
			PvZMod.LOGGER.warn("Wrong Owner Entity for Zombie's Part !");
			return Optional.empty();
		}
		return Optional.of(zombie);
	}
}