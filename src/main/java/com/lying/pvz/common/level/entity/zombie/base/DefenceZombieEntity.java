package com.lying.pvz.common.level.entity.zombie.base;

import com.lying.pvz.api.entity.ability.IHasMultiPart;
import com.lying.pvz.common.level.entity.MultiPartEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.part.PVZHealthPartEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 带独立防御部件（如铁门、铁桶、橄榄球头盔）的僵尸基类。
 *
 * <p>防御部件是独立 {@link MultiPartEntity}，由本类在服务端每 tick 同步位置；
 * 部件的存活由 {@link #getOuterDefenceLife()} 决定，归零即 {@link #removeParts()}。
 *
 * <p>伤害分流：
 * <ul>
 *   <li>{@link #canOuterDefend(DamageSource)} 仅在 {@link #hitDefence} 为 true 时
 *       允许外层防御吸收伤害；{@link #onOuterDefenceHurt()} / {@link #onOuterDefenceBroken()}
 *       负责播放部件音效并复位 {@link #hitDefence}。</li>
 *   <li>外层血量走 {@code PvZAttributes.OUTER_DEFENCE_HP}（见 {@code AbstractPAZEntity#damageOuterDefence}），
 *       与本体血量相互独立。</li>
 * </ul>
 *
 * <p>迷你化：覆写 {@link #onBeMini()} 同步部件尺寸，并按 0.6 比例缩放外层血量。
 *
 * <p>部件位置由 {@link #getPartWidthOffset()} / {@link #getPartHeightOffset()} 决定，
 * 迷你僵尸使用更小的偏移。
 */
public abstract class DefenceZombieEntity extends PVZZombieEntity implements IHasMultiPart  {

	protected PVZHealthPartEntity part;
	public boolean hitDefence = false;

	public DefenceZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.resetParts();
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			this.updateParts();
		}
	}

	@Override
	public void resetParts() {
		this.removeParts();
		this.part = this.createPart();
	}

	protected abstract PVZHealthPartEntity createPart();

	@Override
	public void removeParts() {
		if (EntityUtil.isEntityValid(this.part)) {
			this.part.discard();
			this.part = null;
		}
	}

	@Override
	public void updateParts() {
		if (this.canPartsExist()) {
			if (!EntityUtil.isEntityValid(this.part)) {
				this.resetParts();
			}
			// 先定 OWNER_ID，再让实体入 level，保证包里带的是正确值
			this.part.setOwner(this);
			if (!this.part.isAddedToLevel()) {
				this.level().addFreshEntity(this.part);
			}
			final float j = 2 * 3.14159f * this.getYRot() / 360;
			final float dis = this.getPartWidthOffset();
			final Vec3 pos = this.position();
			this.part.yRotO = this.getYRot();
			this.part.xRotO = this.getXRot();
			this.part.snapTo(pos.x() - Math.sin(j) * dis,
					pos.y() + this.getPartHeightOffset(),
					pos.z() + Math.cos(j) * dis,
					this.getYRot(), this.getXRot());
		} else {
			this.removeParts();
		}
	}
	@Override
	public void setId(int id) {
		super.setId(id);
		if (this.part != null) {
			this.part.setOwnerId(id);
		}
	}
	@Override
	public void onBeMini() {
		super.onBeMini();
		if (EntityUtil.isEntityValid(this.part)) {
			this.part.onOwnerBeMini(this);
		}
		this.setOuterDefenceLife(this.getOuterLife() * 0.6F);
	}


	@Override
	public boolean canPartsExist() {
		return this.getOuterDefenceLife() > 0;
	}

	@Override
	public void onOuterDefenceBroken() {
		super.onOuterDefenceBroken();
		if (!this.level().isClientSide()) {
			EntityUtil.playSound(this, this.getPartDeathSound());
		}
		this.hitDefence = false;
	}

	@Override
	public void onOuterDefenceHurt() {
		super.onOuterDefenceHurt();
		if (!this.level().isClientSide()) {
			EntityUtil.playSound(this, this.getPartHurtSound());
		}
		this.hitDefence = false;
	}

	@Override
	public boolean canOuterDefend(DamageSource source) {
		return super.canOuterDefend(source) && this.hitDefence;
	}

	protected float getPartHeightOffset() {
		return this.isMiniZombie() ? 0.1F : 0.2F;
	}

	public float getPartWidthOffset() {
		return this.isMiniZombie() ? 0.3F : 0.55F;
	}

	public SoundEvent getPartHurtSound() {
		return null;
	}

	public SoundEvent getPartDeathSound() {
		return null;
	}
}