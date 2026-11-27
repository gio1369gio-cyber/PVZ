package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.assist.GraveBusterEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.other.NobleZombieEntity;
import com.lying.pvz.common.level.entity.zombie.roof.Edgar090505Entity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

/**
 * 普通墓碑（波次召唤型）。
 *
 * <p>继承 {@link AbstractTombStoneEntity}，在无 AI、不可被选中的基础上
 * 追加两类“召唤点”行为：
 * <ul>
 *   <li><b>波次激活</b>：{@link #activateByWave()} 置 {@code waveSummonTick = 40}，
 *       到 1 时调用 {@link #summonZombie()} 从
 *       随机召唤一只僵尸（带 rising 标记与宿主数据复制）；</li>
 *   <li><b>常驻召唤</b>：{@code TombStoneSummonZombieGoal} 在可召唤（有目标）时
 *       按 1/10 概率启动，{@code currentSummonCD} 在 360~1200 tick 间随机，
 *       计时到后同样调用 {@link #summonZombie()}。</li>
 * </ul>
 *
 * <p>击杀方式受限：{@link #isZombieInvulnerableTo} 仅在伤害直接来源为
 * {@link GraveBusterEntity} 时才允许受伤，这是墓碑的“专属解法”。
 *
 * <p>目标选择指向 {@link NobleZombieEntity} 与 {@link Edgar090505Entity}，
 * 使墓碑可作为“BOSS 小怪”的生成媒介（这两类僵尸靠近时触发召唤）。
 */
public class TombStoneEntity extends AbstractTombStoneEntity {

	protected int waveSummonTick = 0;
	protected int currentSummonCD;
	private final int MinSummonCD = 360;
	private final int MaxSummonCD = 1200;

	public TombStoneEntity(EntityType<? extends AbstractTombStoneEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new TombStoneSummonZombieGoal(this));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, NobleZombieEntity.class, true));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Edgar090505Entity.class, true));
	}

	public void activateByWave() {
		this.waveSummonTick = 40;
	}

	public static void spawnTombStone(Level world, BlockPos pos) {
		TombStoneEntity tomb = PvZEntityRegister.TOMB_STONE.get().create(world,EntitySpawnReason.TRIGGERED);
		if (tomb == null) {
			return;
		}
		tomb.setZombieRising();
		EntityUtil.onEntitySpawn(world, tomb, pos);
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			if (this.waveSummonTick > 0) {
				--this.waveSummonTick;
				if (this.waveSummonTick == 1) {
					this.summonZombie();
				}
			}
		}
	}

	public void summonZombie() {
		final List<IZombieType> list = ZombieUtil.getDefaultZombies();
		final IZombieType type = list.get(this.random.nextInt(list.size()));
		type.getEntityType().ifPresent(t -> {
			Mob zombie = t.create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (zombie == null) {
				return;
			}
			if (zombie instanceof PVZZombieEntity zombieEntity) {
				zombieEntity.setZombieRising();
				ZombieUtil.copySummonZombieData(this, zombieEntity);
			}
			EntityUtil.onEntitySpawn(this.level(), zombie, this.blockPosition());
		});
	}

	protected boolean canSummonZombie() {
		return EntityUtil.isEntityValid(this.getTarget());
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source)
				|| !(source.getDirectEntity() instanceof GraveBusterEntity);
	}

	protected int genSummonCD() {
		return MathUtil.getRandomMinMax(this.getRandom(), MinSummonCD, MaxSummonCD);
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.TOMB_STONE.get();
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.waveSummonTick = input.getIntOr("wave_summon_tick", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("wave_summon_tick", this.waveSummonTick);
	}

	static class TombStoneSummonZombieGoal extends Goal {

		protected final TombStoneEntity tomb;

		public TombStoneSummonZombieGoal(TombStoneEntity tomb) {
			this.tomb = tomb;
		}

		@Override
		public boolean canUse() {
			if (this.tomb.getAttackTime() > 0) {
				return true;
			}
			if (this.tomb.random.nextInt(10) == 0 && this.tomb.canSummonZombie()) {
				this.tomb.setAttackTime(1);
				this.tomb.currentSummonCD = this.tomb.genSummonCD();
				return true;
			}
			return false;
		}

		@Override
		public void start() {
		}

		@Override
		public boolean canContinueToUse() {
			return this.tomb.getAttackTime() > 0 && this.tomb.canSummonZombie();
		}

		@Override
		public void tick() {
			int time = this.tomb.getAttackTime();
			if (time >= this.tomb.currentSummonCD) {
				this.tomb.summonZombie();
				this.tomb.setAttackTime(0);
			} else {
				this.tomb.setAttackTime(time + 1);
			}
		}

		@Override
		public void stop() {
			this.tomb.setAttackTime(0);
		}
	}
}