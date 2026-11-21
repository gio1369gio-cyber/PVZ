package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.enforce.SquashEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 窝瓜僵尸。
 *
 * <p>本体 25 血，行走 {@link ZombieUtil#WALK_VERY_FAST}，禁用断头。
 *
 * <p>接近目标 10 格内时原地“变身”为一只 {@link SquashEntity} 并立即
 * {@code discard()} 自身：
 * <ul>
 *   <li>辣椒、窝瓜这类“一次性植物僵尸”通过 {@code EntityUtil.onEntitySpawn}
 *       生成对应植物实体，由植物完成实际效果；</li>
 *   <li>阵营通过 {@code setCharmed(!this.isCharmed())} 同步；</li>
 *   <li>目标同步到植物实体，由植物自行处理跃击。</li>
 * </ul>
 */
public class SquashZombieEntity extends AbstractZombotanyEntity {

	public SquashZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canLostHead = false;
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_VERY_FAST);
		}
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			final LivingEntity target = this.getTarget();
			if (target != null && this.distanceToSqr(target) <= 10) {
				final SquashEntity squash = PvZEntityRegister.SQUASH.get()
						.create(this.level(), EntitySpawnReason.TRIGGERED);
				if (squash == null) {
					return;
				}
				squash.setCharmed(!this.isCharmed());
				squash.setTarget(target);
				EntityUtil.onEntitySpawn(this.level(), squash, this.blockPosition().above(2));
				this.discard();
			}
		}
	}

	@Override
	public float getLife() {
		return 25;
	}

	@Override
	public IZombieType getZombieType() {
		return Zombotanies.SQUASH_ZOMBIE.get();
	}
}