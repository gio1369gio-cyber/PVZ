package com.lying.pvz.common.level.entity.plant.explosion;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.misc.FireCrackersEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantCloserEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * 竹笋（Bamboo Lord）。
 *
 * <p>普通攻击：3 格范围爆炸，弹起目标，并分裂 {@link #getSplitCount()} 发
 * {@link com.lying.pvz.common.projectile.BulletKinds#FIRE_CRACKER}（新弹药）追向随机可见目标。
 * super 模式在附近随机高度生成 {@link #getCrackersNum()} 个 {@link FireCrackersEntity}。
 */
public class BambooLordEntity extends PlantCloserEntity {

	public static final int UP_CD = 20;
	public static final float UP_SPEED = 2F;

	public BambooLordEntity(EntityType<? extends PlantCloserEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void focusOnTarget(LivingEntity target1) {
		super.focusOnTarget(target1);
		if (this.getAttackTime() == 1) {
			final float range = 2F;
			EntityUtil.getTargetableEntities(this,
					EntityUtil.getEntityAABB(this, range, range)).forEach(target ->
					target.setDeltaMovement(target.getDeltaMovement().add(0, UP_SPEED, 0)));
			this.setDeltaMovement(this.getDeltaMovement().add(0, UP_SPEED, 0));
			for (int i = 0; i < 3; ++i) {
				EntityUtil.spawnParticle(this, 5);
			}
			EntityUtil.playSound(this, PvZSoundRegister.POTATO_MINE.get());
		}
	}

	@Override
	public void performAttack(LivingEntity target1) {
		for (int i = 0; i < 3; ++i) {
			EntityUtil.spawnParticle(this, 5);
		}
		final float range = 3F;
		EntityUtil.getTargetableEntities(this,
				EntityUtil.getEntityAABB(this, range, range)).forEach(target -> {
			target.hurt(PvZEntityDamageSource.explode(this), this.getAttackDamage());
			target.setDeltaMovement(target.getDeltaMovement().add(0, UP_SPEED, 0));
		});
		this.split();
		EntityUtil.playSound(this, PvZSoundRegister.POTATO_MINE.get());
	}

	/**
	 * 分裂：向视野内最多 30 格随机目标发射 {@link #getSplitCount()} 发
	 * {@link com.lying.pvz.common.projectile.BulletKinds#FIRE_CRACKER}（伤害 2× 自身）。
	 */
	protected void split() {
		final float range = 30F;
		final List<LivingEntity> list = new ArrayList<>();
		EntityUtil.getTargetableLivings(this,
						EntityUtil.getEntityAABB(this, range, range))
				.forEach(target -> {
					if (this.getSensing().hasLineOfSight(target)) {
						list.add(target);
					}
				});
		if (!list.isEmpty() && this.level() instanceof ServerLevel serverLevel) {
			for (int i = 0; i < this.getSplitCount(); ++i) {
				final int pos = this.getRandom().nextInt(list.size());
				final LivingEntity target = list.get(pos);
				final ShotSpec spec = new ShotSpec().baseDamage(this.getAttackDamage() * 2);
				BulletLauncher.launch(serverLevel, PvZEntityRegister.FIRE_CRACKER.get(), this,
						this.getEntityGroupType(), BulletKinds.FIRE_CRACKER, spec,
						new Vec3(this.getX(), this.getY(), this.getZ()), target, null);
			}
		}
		for (int i = 0; i < 3; ++i) {
			EntityUtil.spawnParticle(this, 5);
		}
		this.discard();
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.EXPLODE_DAMAGE, this.getAttackDamage()));
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		for (int i = 0; i < this.getCrackersNum(); ++i) {
			this.generateCracker();
		}
	}

	protected void generateCracker() {
		final int range = 10;
		final BlockPos pos = WorldUtil.getSuitableHeightRandomPos(
				this.level(), this.blockPosition(), range);
		final FireCrackersEntity entity = new FireCrackersEntity(this.level(), this);
		entity.setPos(pos.getX(), pos.getY(), pos.getZ());
		entity.setFuse(10);
		entity.summonByOwner(this);
		this.level().addFreshEntity(entity);
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.SMALL_BOMB_DAMAGE.get());
	}

	public int getSplitCount() {
		return 1;
	}

	public int getCrackersNum() {
		return 10;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.6F, 1F);
	}

	@Override
	public IPlantType getPlantType() {
		return OtherPlants.BAMBOO_LORD.get();
	}
}