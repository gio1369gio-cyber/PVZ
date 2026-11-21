package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantDefenderEntity;
import com.lying.pvz.common.level.entity.zombie.base.DefenceZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.level.entity.zombie.part.PVZHealthPartEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 梯子僵尸。
 *
 * <p>继承 {@link DefenceZombieEntity}，携带 1 个 {@link PVZHealthPartEntity} 部件
 * （1×1.7 碰撞箱，{@code getOuterLife = 250}）。本体 65 血。
 *
 * <p>核心机制为“搭梯”：
 * <ul>
 *   <li>{@link #doHurtTarget} 命中目标前，若持有梯子且目标是可搭梯的植物
 *       （{@link #canTargetPutLadder}），则把梯子“移植”到植物上：</li>
 *   <li>植物侧 {@code increaseMetal()} 记录梯子，僵尸自身 {@code decreaseMetal()}
 *       并降速到 {@link ZombieUtil#WALK_NORMAL}；</li>
 *   <li>僵尸爬上带梯植物后不再触发碾压伤害，可跨越继续前进。</li>
 * </ul>
 *
 * <p>{@code LADDER} 是新 {@link MetalTypes} 里唯一保留的植物侧金属类型，
 * {@link #getMetalType()} 返回 {@link MetalTypes#LADDER}。
 * 与读报/铁门一致：持有梯子时不允许断手，掉落部件时同步 {@code handDefence}。
 */
public class LadderZombieEntity extends DefenceZombieEntity implements IHasMetal {

	public LadderZombieEntity(EntityType<? extends DefenceZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected PVZHealthPartEntity createPart() {
		final PVZHealthPartEntity created = new PVZHealthPartEntity(this, 1f, 1.7f);
		created.setOwner(this);
		return created;
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entityIn) {
		if (this.hasMetal() && canTargetPutLadder(entityIn)) {
			this.putLadderOn(entityIn);
		}
		return super.doHurtTarget(level, entityIn);
	}

	public void putLadderOn(Entity entity) {
		if (entity instanceof PlantEntity plant) {
			plant.increaseMetal();
		}
		this.decreaseMetal();
	}

	public static boolean canTargetPutLadder(Entity target) {
		if (!(target instanceof PlantEntity plant) || hasLadderOnEntity(target)) {
			return false;
		}
		if (target instanceof PlantDefenderEntity) {
			return true;
		}
		return plant.getOuterPlantInfo().isPresent();
	}

	private static boolean hasLadderOnEntity(Entity target) {
		if (!(target instanceof PlantEntity plant)) {
			return false;
		}
		return plant.hasMetal();
	}

	@Override
	public boolean hasMetal() {
		return this.getOuterDefenceLife() > 0;
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
	public void decreaseMetal() {
		this.setOuterDefenceLife(0);
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_NORMAL);
		}
	}

	@Override
	public void increaseMetal() {
		this.setOuterDefenceLife(this.getOuterLife());
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_FAST);
		}
		this.resetParts();
	}

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.LADDER;
	}

	@Override
	public SoundEvent getPartHurtSound() {
		return PvZSoundRegister.METAL_HIT.get();
	}

	@Override
	public float getLife() {
		return 65;
	}

	@Override
	public float getOuterLife() {
		return 250;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_FAST;
	}

	@Override
	public IZombieType getZombieType() {
		return RoofZombies.LADDER_ZOMBIE.get();
	}
}