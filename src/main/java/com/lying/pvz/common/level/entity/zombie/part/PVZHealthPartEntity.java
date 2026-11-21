package com.lying.pvz.common.level.entity.zombie.part;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.level.entity.zombie.base.DefenceZombieEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 带外层防御血量的僵尸部件（铁门、报纸、桶等）。
 *
 * <p>与 {@link PVZZombiePartEntity} 的差异：
 * <ul>
 *   <li>宿主固定为 {@link DefenceZombieEntity}，部件存活条件叠加
 *       {@code owner.canPartsExist()}；</li>
 *   <li>{@link #hurtServer} 在命中 {@link PvZEntityDamageSource} 且
 *       {@code isThroughDamage()} 为 false 时标记 {@code setDefended(true)}，
 *       让伤害管线走“外层防御吸收”分支；随后设置
 *       {@code zombie.hitDefence = true}，由宿主的
 *       {@code onOuterDefenceHurt / onOuterDefenceBroken} 播放部件音效；</li>
 *   <li>实际伤害仍通过 {@code super.hurtServer} 转发给宿主，部件不独立结算血量。</li>
 * </ul>
 */
public class PVZHealthPartEntity extends PVZZombiePartEntity {

	protected DefenceZombieEntity zombie;

	public PVZHealthPartEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	public PVZHealthPartEntity(DefenceZombieEntity owner, float sizeX, float sizeY) {
		super(owner, sizeX, sizeY);
		this.zombie = owner;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		DefenceZombieEntity owner = this.zombie;
		if (owner == null && this.getOwner() instanceof DefenceZombieEntity def) {
			owner = def;
			this.zombie = def;
		}
		if (owner == null) {
			return false;
		}
		if (source instanceof PvZEntityDamageSource pds) {
			if (pds.isThroughDamage()) {
				return super.hurtServer(level, source, damage);
			}
			pds.setDefended(true);
		}
		owner.hitDefence = true;
		return super.hurtServer(level, source, damage);
	}

	@Override
	public boolean canExist() {
		if (this.zombie == null && this.getOwner() instanceof DefenceZombieEntity def) {
			this.zombie = def;
		}
		return super.canExist() && this.zombie != null && this.zombie.canPartsExist();
	}
}