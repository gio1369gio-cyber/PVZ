package com.lying.pvz.common.level.entity.zombie.custom;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.level.entity.zombie.grass.AbstractTombStoneEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 巨型墓碑僵尸。
 *
 * <p>继承 {@link AbstractTombStoneEntity}，仅做两处差异：
 * <ul>
 *   <li>禁用魅惑：{@code canBeCharm = false}。巨型墓碑是场景装饰类僵尸，
 *       翻转阵营没有意义，且其碰撞/遮挡行为依赖阵营判定。</li>
 *   <li>绑定类型 {@link CustomZombies#GIGA_TOMBSTONE}。</li>
 * </ul>
 *
 * <p>其余行为（不可推动、无目标、无技能、无 loot）继承自墓碑基类。
 */
public class GigaTombStoneEntity extends AbstractTombStoneEntity {

	public GigaTombStoneEntity(EntityType<? extends AbstractTombStoneEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canBeCharm = false;
	}

	@Override
	public IZombieType getZombieType() {
		return CustomZombies.GIGA_TOMBSTONE.get();
	}
}