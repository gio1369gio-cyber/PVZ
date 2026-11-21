package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.zombie.base.DefenceZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.level.entity.zombie.part.PVZHealthPartEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 铁门僵尸。
 *
 * <p>继承 {@link DefenceZombieEntity}，外层防御为铁门
 * （{@link PVZHealthPartEntity}，1×1.7 碰撞箱，{@code getOuterLife = 200}）。
 * 本体 24 血。
 *
 * <p>铁门属于外层金属覆层，{@link #getMetalType()} 返回
 * {@link MetalTypes#SCREEN_DOOR}，参与 {@code MetalTypes} 的外层伤害分流。
 * 与读报僵尸相同，铁门未破时不允许断手（{@link #canLostHand()}）；
 * 掉落部件时把 {@code handDefence} 同步为“是否仍有铁门”。
 *
 * <p>部件受击音效 {@link PvZSoundRegister#METAL_HIT}。
 */
public class ScreenDoorZombieEntity extends DefenceZombieEntity implements IHasMetal {

	public ScreenDoorZombieEntity(EntityType<? extends DefenceZombieEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected PVZHealthPartEntity createPart() {
		final PVZHealthPartEntity created = new PVZHealthPartEntity(this, 1f, 1.7f);
		created.setOwner(this);
		return created;
	}

	@Override
	public boolean hasMetal() {
		return this.getOuterDefenceLife() > 0;
	}

	@Override
	public void decreaseMetal() {
		this.setOuterDefenceLife(0);
	}

	@Override
	public void increaseMetal() {
		this.setOuterDefenceLife(this.getOuterLife());
		this.resetParts();
	}

	@Override
	public boolean canLostHand() {
		return super.canLostHand() && !this.hasMetal();
	}

	@Override
	protected void setBodyStates(ZombieDropBodyEntity body) {
		super.setBodyStates(body);
		body.setHandDefence(this.hasMetal());
	}

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.SCREEN_DOOR;
	}

	@Override
	public SoundEvent getPartHurtSound() {
		return PvZSoundRegister.METAL_HIT.get();
	}

	@Override
	public float getLife() {
		return 24;
	}

	@Override
	public float getOuterLife() {
		return 200;
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.SCREENDOOR_ZOMBIE.get();
	}
}