package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 橄榄球僵尸。
 *
 * <p>本体 20 血 + 内层头盔 140 血，行走 {@link ZombieUtil#WALK_FAST}。
 * 头盔属于内层防御（{@code INNER_DEFENCE_HP}），不是外层金属覆层，
 * 因此 {@link #getMetalType()} 返回 {@link MetalTypes#EMPTY}。
 *
 * <p>禁用断手（{@code canLostHand = false}）：橄榄球服是整体护具，
 * 断手会破坏渲染一致性，也与其“碾压型”定位不符。
 *
 * <p>碰撞箱：常规 0.8×2.4；迷你僵尸 0.5×0.75。
 */
public class FootballZombieEntity extends PVZZombieEntity implements IHasMetal {

	public FootballZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canLostHand = false;
		this.increaseMetal();
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.5F, 0.75F);
		}
		return EntityDimensions.scalable(0.8F, 2.4F);
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_FAST;
	}

	@Override
	public float getLife() {
		return 20;
	}

	@Override
	public float getInnerLife() {
		return 140;
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.FOOTBALL_ZOMBIE.get();
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

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.FOOTBALL_HELMET;
	}
}