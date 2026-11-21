package com.lying.pvz.common.level.entity.plant.enforce;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.pool.BalloonZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Bonk Choy。
 *
 * <p>近距离连击型近战植物。普通攻击使用负/正攻击时间两条分支实现"咬/收"的动画节拍；
 * super 模式每 5 tick 对周围 5 格目标造成 {@code 5 × 基础伤害} 的 AOE。
 */
public class BonkChoyEntity extends PlantEntity {

	public BonkChoyEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new BonkChoyAttackGoal(this));
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, true, false, 3, 3));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (this.isPlantInSuperMode() && this.getSuperTime() % 5 == 0) {
			final float range = 5F;
			EntityUtil.getTargetableEntities(this,
					EntityUtil.getEntityAABB(this, range, range)).forEach(target -> {
				if (target instanceof LivingEntity living) {
					living.damageCooldownTime = 0;   // 保证 5 tick 满频率
				}
				target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage() * 5);
				EntityUtil.spawnParticle(target, 7);
				EntityUtil.playSound(this, PvZSoundRegister.SWING.get());
			});
		}
	}

	public void attackTarget(LivingEntity target) {
		EntityUtil.playSound(this, PvZSoundRegister.SWING.get());
		EntityUtil.spawnParticle(target, 7);
		if (target instanceof LivingEntity living) {
			living.damageCooldownTime = 0;   // 保证 10 tick 边界稳定
		}
		target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage());
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		if (entity instanceof BalloonZombieEntity) {
			return true;
		}
		return super.canPAZTarget(entity);
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.ATTACK_CD, this.getAttackCD())
		));
	}

	public int getAttackCD() {
		return 10;
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_SWING_DAMAGE.get());
	}

	@Override
	public int getSuperTimeLength() {
		return 120;
	}

	@Override
	public IPlantType getPlantType() {
		return OtherPlants.BONK_CHOY.get();
	}

	private final class BonkChoyAttackGoal extends Goal {

		private final BonkChoyEntity attacker;

		public BonkChoyAttackGoal(BonkChoyEntity attacker) {
			this.attacker = attacker;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			final LivingEntity living = this.attacker.getTarget();
			if (!EntityUtil.isEntityValid(living)) {
				return false;
			}
			return this.attacker.hasLineOfSight(living)
					&& EntityUtil.getAttackRange(this.attacker, living, 3F)
					>= EntityUtil.getNearestDistance(this.attacker, living);
		}

		@Override
		public boolean canContinueToUse() {
			return this.canUse();
		}

		@Override
		public void stop() {
			this.attacker.setTarget(null);
			this.attacker.setAttackTime(0);
		}

		@Override
		public void tick() {
			final LivingEntity target = this.attacker.getTarget();
			this.attacker.getLookControl().setLookAt(target, 30F, 30F);
			if (this.attacker.getAttackTime() == 0) {
				if (this.attacker.getAttackDamage() >= EntityUtil.getCurrentHealth(target)) {
					this.attacker.setAttackTime(1);
				} else {
					this.attacker.setAttackTime(-1);
				}
			} else if (this.attacker.getAttackTime() > 0) {
				this.attacker.setAttackTime(this.attacker.getAttackTime() + 1);
				if (this.attacker.getAttackTime() == this.attacker.getAttackCD() * 4 / 5) {
					this.attacker.attackTarget(target);
				} else if (this.attacker.getAttackTime() >= this.attacker.getAttackCD()) {
					this.attacker.setAttackTime(0);
				}
			} else {
				this.attacker.setAttackTime(this.attacker.getAttackTime() - 1);
				if (-this.attacker.getAttackTime() == this.attacker.getAttackCD() * 4 / 5) {
					this.attacker.attackTarget(target);
				} else if (-this.attacker.getAttackTime() >= this.attacker.getAttackCD()) {
					this.attacker.setAttackTime(0);
				}
			}
		}
	}
}