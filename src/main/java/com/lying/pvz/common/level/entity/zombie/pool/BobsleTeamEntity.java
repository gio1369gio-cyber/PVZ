package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMultiPart;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.level.entity.zombie.part.PVZZombiePartEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * 雪橇车僵尸（两人组）。
 *
 * <p>本体 60 血，行走 {@link ZombieUtil#WALK_VERY_FAST}、攻击
 * {@link ZombieUtil#NORMAL_DAMAGE}。携带 2 个独立碰撞箱部件
 * （{@link PVZZombiePartEntity}），由 {@link IHasMultiPart} 统一 tick 同步位置。
 *
 * <p>核心机制为“离开雪/冰后暴毙”：
 * <ul>
 *   <li>{@link #zombieTick()} 检测落地且不在雪/冰上，
 *       累计 {@code outSnowTick}；超过 {@link #MAX_OUT_SNOW_TICK} 后
 *       以 {@code dryOut} 伤害源掉落部件并 {@code discard()}；</li>
 *   <li>任何时刻回到雪/冰上会把计时清零。</li>
 * </ul>
 *
 * <p>死亡时（{@link #onRemoveWhenDeath()}）生成 4 只 {@link BobsleZombieEntity}。
 * 掉落部件使用高摩擦系数 {@code 0.95}，避免部件滑出视野。迷你化被禁用。
 */
public class BobsleTeamEntity extends ZombieEntity implements IHasMultiPart {

	public static final int PART_NUM = 2;
	private static final int MAX_OUT_SNOW_TICK = 100;
	private final PVZZombiePartEntity[] parts = new PVZZombiePartEntity[PART_NUM];
	private int outSnowTick;

	public BobsleTeamEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setIsWholeBody();
		this.resetParts();
		this.setImmuneAllEffects();
		this.canBeMini = false;
	}

	@Override
	public VariantType getVariantType() {
		return VariantType.NORMAL;
	}

	@Override
	public void resetParts() {
		this.removeParts();
		for (int i = 0; i < PART_NUM; i++) {
			this.parts[i] = new PVZZombiePartEntity(this, 1f, 1.5f);
			this.parts[i].setOwner(this);
		}
	}

	@Override
	public void removeParts() {
		for (int i = 0; i < PART_NUM; i++) {
			if (this.parts[i] == null) {
				continue;
			}
			this.parts[i].discard();
			this.parts[i] = null;
		}
	}

	@Override
	public void updateParts() {
		for (int i = 0; i < PART_NUM; i++) {
			if (this.parts[i] == null) {
				continue;
			}
			// 先定 OWNER_ID，再让实体入 level
			this.parts[i].setOwner(this);
			if (!this.parts[i].isAddedToLevel()) {
				this.level().addFreshEntity(this.parts[i]);
			}
			final float j = 2 * 3.14159f * this.getYHeadRot() / 360;
			final float dis = this.getPartOffset(i);
			final Vec3 pos = this.position();
			this.parts[i].yRotO = this.getYRot();
			this.parts[i].xRotO = this.getXRot();
			this.parts[i].snapTo(pos.x() - Math.sin(j) * dis, pos.y() + 0.05f,
					pos.z() + Math.cos(j) * dis, this.getYRot(), this.getXRot());
		}
	}

	public float getPartOffset(int num) {
		if (num == 0) {
			return -1.5f;
		} else if (num == 1) {
			return -2.5f;
		}
		return 0;
	}
	@Override
	public void setId(int id) {
		super.setId(id);
		for (PVZZombiePartEntity part : this.parts) {
			if (part != null) {
				part.setOwnerId(id);
			}
		}
	}
	@Override
	public void tick() {
		super.tick();
		this.updateParts();
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (!this.level().isClientSide()) {
			if (this.isInWater()
					|| (this.onGround() && !EntityUtil.isOnSnow(this) && !EntityUtil.isOnIce(this))) {
				++this.outSnowTick;
				if (this.outSnowTick > MAX_OUT_SNOW_TICK) {
					this.onRemoveWhenDeath();
					this.discard();
				}
			} else {
				this.outSnowTick = 0;
			}
		}
	}

	@Override
	protected void onRemoveWhenDeath() {
		if (!this.level().isClientSide()) {
			for (int i = 0; i < 4; ++i) {
				BobsleZombieEntity zombie = PvZEntityRegister.BOBSLE_ZOMBIE.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
				ZombieUtil.copySummonZombieData(this, zombie);
				EntityUtil.onEntityRandomPosSpawn(this.level(), zombie, this.blockPosition(), 2);
			}
		}
	}

	@Override
	protected void setBodyStates(ZombieDropBodyEntity body) {
		super.setBodyStates(body);
		body.setFriction(0.95F);
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(1.25f, 1.4f);
	}

	@Override
	public float getLife() {
		return 60;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_VERY_FAST;
	}

	@Override
	public float getEatDamage() {
		return ZombieUtil.NORMAL_DAMAGE;
	}

	@Override
	protected int getDeathTime() {
		return 2;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.outSnowTick = input.getIntOr("out_snow_tick", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("out_snow_tick", this.outSnowTick);
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.BOBSLE_TEAM.get();
	}
}