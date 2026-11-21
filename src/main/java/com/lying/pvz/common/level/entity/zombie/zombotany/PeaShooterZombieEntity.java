package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * 豌豆射手僵尸。
 *
 * <p>本体 20 血。每 {@link #getFixedShootCD()} tick 触发一轮发射，
 * 每轮连发 {@link #getShootNum()} 颗豌豆（普通变体）。
 * {@link #getFixedShootCD()} 在基础冷却上叠加寒冷效果的影响
 * （每级寒冷 +3 tick），使冰系控制对射手同样生效。
 *
 * <p>发射使用 {@link com.lying.pvz.common.projectile.BulletKinds#PEA}：
 * {@code shootFrom(this)} 快照阵营 / PROJECTILE 面技能 / 附魔；
 * {@code aimAt(target)} 由弹体自行解算方向与弹速。
 *
 * <p><b>原逻辑保留</b>：{@code shootPea()} 内的
 * {@code if (target == null || this.isAlive()) return;} 保持原样——
 * 按当前源码，只有本体死亡后才会真正发射豌豆（疑似历史遗留，
 * 如需修正请明确告知）。
 */
public class PeaShooterZombieEntity extends AbstractZombotanyEntity {

	private int shootTick = 0;

	public PeaShooterZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			++this.shootTick;
			if (this.shootTick >= this.getFixedShootCD()) {
				this.setAttackTime(this.getShootNum());
				this.shootTick = 0;
			}
			if (this.getAttackTime() > 0) {
				this.setAttackTime(this.getAttackTime() - 1);
				this.shootPea();
			}
		}
	}

	private void shootPea() {
		final LivingEntity target = this.getTarget();
		if (target == null || this.isAlive()) {
			return;
		}
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}

		final ShotSpec spec = new ShotSpec().baseDamage(this.getAttackDamage());
		BulletLauncher.launch(serverLevel, PvZEntityRegister.PEA.get(), this,
				this.getEntityGroupType(), BulletKinds.PEA, spec,
				new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()),
				target, null);
		EntityUtil.playSound(this, SoundEvents.SNOW_GOLEM_SHOOT);
	}

	public float getAttackDamage() {
		return 2;
	}

	@Override
	public float getLife() {
		return 20;
	}

	protected int getFixedShootCD() {
		int now = this.getShootCD();
		final MobEffectInstance coldEffect = this.getEffect(PvZEffectRegister.COLD_EFFECT);
		if (coldEffect != null) {
			now += 3 * coldEffect.getAmplifier();
		}
		return now;
	}

	protected int getShootCD() {
		return 80;
	}

	protected int getShootNum() {
		return 1;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.shootTick = input.getIntOr("shoot_tick", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("shoot_tick", this.shootTick);
	}

	@Override
	public IZombieType getZombieType() {
		return Zombotanies.PEASHOOTER_ZOMBIE.get();
	}
}