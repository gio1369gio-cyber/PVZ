package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 路障僵尸。
 *
 * <p>本体 20 血 + 内层路障 40 血。{@link #hasConeHead(int)} 按内层剩余比例
 * 分三档（>2/3、>1/3、>0）供渲染选择模型破损层，命中音效在仍有路障时
 * 使用 {@link PvZSoundRegister#PLASTIC_HIT}。
 *
 * <p>路障属于内部防御，走 {@code INNER_DEFENCE_HP} 属性，不映射到
 * {MetalTypes}（后者只用于外层金属，如铁门/梯子）。
 */
public class ConeHeadZombieEntity extends NormalZombieEntity {

	public ConeHeadZombieEntity(EntityType<? extends NormalZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getLife() {
		return 20;
	}

	@Override
	public float getInnerLife() {
		return 40;
	}

	public boolean hasConeHead(int stage) {
		final double percent = this.getInnerDefenceLife() / this.getInnerLife();
		if (stage == 3) {
			return percent > 2.0F / 3;
		} else if (stage == 2) {
			return percent > 1.0F / 3;
		} else if (stage == 1) {
			return percent > 0;
		}
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return this.getInnerDefenceLife() > 0 ? PvZSoundRegister.PLASTIC_HIT.get() : super.getHurtSound(damageSourceIn);
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.CONEHEAD_ZOMBIE.get();
	}
}