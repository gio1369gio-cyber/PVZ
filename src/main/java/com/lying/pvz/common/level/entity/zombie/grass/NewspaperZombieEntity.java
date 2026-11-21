package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.zombie.base.DefenceZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.level.entity.zombie.part.PVZHealthPartEntity;
import com.lying.pvz.utils.EffectUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 读报僵尸。
 *
 * <p>继承 {@link DefenceZombieEntity}，外层防御为一张报纸
 * （{@link PVZHealthPartEntity}，1×1 碰撞箱，{@code getOuterLife = 10}）。
 *
 * <p>报纸未破时本体行动迟缓（{@link ZombieUtil#WALK_LITTLE_SLOW}）；
 * 报纸被打碎后进入“暴怒”状态：
 * <ul>
 *   <li>{@link #onOuterDefenceBroken()} 施加永久加速（等级 1）与
 *       力量药水（等级随机 1~{@link #getAngryLevel()}），并播放
 *       {@link PvZSoundRegister#ZOMBIE_ANGRY}；</li>
 *   <li>{@link #canLostHand()} 仅在暴怒后才允许断手；</li>
 *   <li>{@link #setBodyStates} 在掉落部件时把 {@code handDefence}
 *       设为“是否仍未暴怒”，让掉落物的外观跟随报纸状态。</li>
 * </ul>
 *
 * <p>部件死亡音效为 {@link PvZSoundRegister#PAPER_BROKEN}。
 * 本体血量 22，{@link #isAngry()} 即 {@code !canPartsExist()}。
 */
public class NewspaperZombieEntity extends DefenceZombieEntity {

	public NewspaperZombieEntity(EntityType<? extends DefenceZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected PVZHealthPartEntity createPart() {
		final PVZHealthPartEntity created = new PVZHealthPartEntity(this, 1f, 1f);
		created.setOwner(this);
		return created;
	}

	@Override
	protected float getPartHeightOffset() {
		return this.isMiniZombie() ? 0.2F : 0.7F;
	}

	@Override
	public void onOuterDefenceBroken() {
		super.onOuterDefenceBroken();
		if (!this.level().isClientSide()) {
			this.addEffect(EffectUtil.effect(MobEffects.SPEED, 120000, 1));
			this.addEffect(EffectUtil.effect(MobEffects.STRENGTH, 120000,
					MathUtil.getRandomMinMax(this.random, 1, this.getAngryLevel())));
			EntityUtil.playSound(this, PvZSoundRegister.ZOMBIE_ANGRY.get());
		}
	}

	@Override
	public boolean canLostHand() {
		return super.canLostHand() && this.isAngry();
	}

	@Override
	protected void setBodyStates(ZombieDropBodyEntity body) {
		super.setBodyStates(body);
		body.setHandDefence(!this.isAngry());
	}

	@Override
	public SoundEvent getPartDeathSound() {
		return PvZSoundRegister.PAPER_BROKEN.get();
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_LITTLE_SLOW;
	}

	public int getAngryLevel() {
		return 3;
	}

	@Override
	public float getLife() {
		return 22;
	}

	@Override
	public float getOuterLife() {
		return 10;
	}

	public boolean isAngry() {
		return !this.canPartsExist();
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.NEWSPAPER_ZOMBIE.get();
	}
}