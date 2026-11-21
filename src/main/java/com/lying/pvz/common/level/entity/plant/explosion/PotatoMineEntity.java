package com.lying.pvz.common.level.entity.plant.explosion;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantCloserEntity;
import com.lying.pvz.common.level.entity.zombie.pool.DiggerZombieEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 * 土豆雷。
 *
 * <p>前 {@link #getPrepareCD()} tick 埋在地下（{@code isRisingFromDirt} 区间：{@code prepareCD - 20} ~ {@code prepareCD}），
 * 之后变成可触发的 {@link #isMineReady()}。super 模式发射 {@link #getShootNum()} 个
 * {@link com.lying.pvz.common.projectile.BulletKinds#POTATO}（新弹药）并重置自身为埋地状态。
 */
public class PotatoMineEntity extends PlantCloserEntity {

	public static final int RISING_ANIM_CD = 20;

	public PotatoMineEntity(EntityType<? extends PlantCloserEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			if (this.isRisingFromDirt()) {
				final Vec3 offset = new Vec3(
						MathUtil.getRandomFloat(this.getRandom()), 0,
						MathUtil.getRandomFloat(this.getRandom())).normalize();
				WorldUtil.spawnRandomSpeedParticle(this.level(),
						PvZParticleRegister.DIRT_BURST_OUT.get(),
						this.position().add(offset),
						MathUtil.getRandomFloat(this.getRandom()) / 8, 0.06F);
			}
		} else if (this.getExistTick() == this.getPrepareCD() - RISING_ANIM_CD + 1) {
			EntityUtil.playSound(this, PvZSoundRegister.DIRT_RISE.get());
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.EXPLODE_DAMAGE, this.getExplodeDamage()),
				Pair.of(PAZAlmanacs.EXPLODE_RANGE, this.getExplodeRange()),
				Pair.of(PAZAlmanacs.PREPARE_CD, this.getPrepareCD())
		));
	}

	@Override
	public void performAttack(LivingEntity target1) {
		if (this.level().isClientSide()) {
			return;
		}
		final float range = 1.6F;
		final AABB aabb = EntityUtil.getEntityAABB(this, range, range);
		EntityUtil.getWholeTargetableEntitiesIgnoreFog(this, aabb).forEach(target ->
				target.hurt(PvZEntityDamageSource.explode(this), this.getExplodeDamage()));
		PlantEntity.clearLadders(this, aabb);
		EntityUtil.playSound(this, PvZSoundRegister.POTATO_MINE.get());
		for (int i = 1; i <= 10; ++i) {
			EntityUtil.spawnParticle(this, 3);
			EntityUtil.spawnParticle(this, 4);
		}
		this.discard();
	}

	@Override
	public boolean canPAZTarget(Entity target) {
		if (target instanceof DiggerZombieEntity) {
			return true;
		}
		return super.canPAZTarget(target);
	}

	@Override
	public boolean canStartSuperMode() {
		return super.canStartSuperMode() && this.getAttackTime() <= 0;
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.shootPotatos();
		if (!this.isRisingFromDirt() && !this.isMineReady()) {
			this.setRisingFromDirt();
		}
	}

	/**
	 * 发射 {@link #getShootNum()} 发 {@link}。
	 * 旧 {@code PotatoEntity(level(), this)} → 新 {@code new PotatoProjectile(PvZEntityRegister.POTATO.get(), level())}，
	 * 用 {@code shootFrom(this)} 快照 owner/技能/附魔（落地会复制这些数据到新土豆雷）。
	 */
	protected void shootPotatos() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final int num = this.getShootNum();
		for (int i = 1; i <= num; ++i) {
			final float dx = MathUtil.getRandomFloat(this.getRandom()) * i / 2;
			final float dy = 0.5F;
			final float dz = MathUtil.getRandomFloat(this.getRandom()) * i / 2;
			BulletLauncher.launchRaw(serverLevel, PvZEntityRegister.POTATO.get(), this,
					this.getEntityGroupType(), BulletKinds.POTATO, new ShotSpec(),
					new Vec3(this.getX(), this.getY() + 1, this.getZ()),
					new Vec3(dx, dy, dz));
		}
	}

	public int getShootNum() {
		return 3;
	}

	public float getExplodeRange() {
		return 1.8F;
	}

	public float getExplodeDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_BOMB_DAMAGE.get());
	}

	public int getPrepareCD() {
		return (int) this.getSkillValue(SkillTypes.MINE_FAST_PREPARE.get());
	}

	public boolean isMineReady() {
		return this.getExistTick() > this.getPrepareCD();
	}

	public boolean isRisingFromDirt() {
		return this.getExistTick() >= this.getPrepareCD() - RISING_ANIM_CD
				&& this.getExistTick() <= this.getPrepareCD();
	}

	public void setRisingFromDirt() {
		this.setExistTick(this.getPrepareCD() - RISING_ANIM_CD - 2);
	}

	@Override
	protected boolean canBeImmuneToEnforce(Entity entity) {
		return super.canBeImmuneToEnforce(entity)
				&& (this.isMineReady() || this.isRisingFromDirt());
	}

	@Override
	public boolean canCheckDistance() {
		return this.isMineReady();
	}

	public int getSignChangeCD() {
		return this.isMineReady() ? 10 : 20;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.6F, 0.4F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.POTATO_MINE.get();
	}
}