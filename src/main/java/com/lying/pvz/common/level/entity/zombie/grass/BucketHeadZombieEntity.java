package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 铁桶僵尸。
 *
 * <p>本体 20 血 + 内层铁桶 110 血，分三档破损层（见 {@link #hasBucketHead(int)}）。
 * 命中音效在仍有铁桶时使用 {@link PvZSoundRegister#METAL_HIT}。
 *
 * <p>{@code getMetalType()} 返回 {@link MetalTypes#EMPTY}：新架构的 {@code MetalTypes}
 * 只覆盖外层金属（梯子/铁门），铁桶属于内层防御（{@code INNER_DEFENCE_HP}），
 * 不在该枚举范围内。若未来需要按“桶/头盔”区分音效或模型，应扩展
 * {@code MetalTypes} 而非复用 {@code SCREEN_DOOR}。
 */
public class BucketHeadZombieEntity extends NormalZombieEntity implements IHasMetal {

	public BucketHeadZombieEntity(EntityType<? extends NormalZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.increaseMetal();
	}

	@Override
	public float getLife() {
		return 20;
	}

	@Override
	public float getInnerLife() {
		return 110;
	}

	@Override
	public boolean hasMetal() {
		return this.getInnerDefenceLife() > 0;
	}

	@Override
	public void decreaseMetal() {
		this.setInnerDefenceLife(0);
	}

	@Override
	public void increaseMetal() {
		this.setInnerDefenceLife(this.getInnerLife());
	}

	public boolean hasBucketHead(int stage) {
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
	public MetalTypes getMetalType() {
		return MetalTypes.BUCKET_HEAD;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return this.hasMetal() ? PvZSoundRegister.METAL_HIT.get() : super.getHurtSound(damageSourceIn);
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.BUCKETHEAD_ZOMBIE.get();
	}
}