package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.OtherZombies;
import com.lying.pvz.common.level.entity.drop.DropEntity;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.misc.PvZLoot;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

/**
 * 太阳神僵尸（RA 僵尸）。
 *
 * <p>本体 36 血，行走 {@link ZombieUtil#WALK_LITTLE_SLOW}。核心机制为“吸阳光”：
 * <ul>
 *   <li>{@code searchTick} 在 100~400 tick 间循环；归零时把 20 格内所有
 *       {@link SunEntity} 标记为 ，并进入
 *       {@link #ABSORB_TICK} 的吸收动画窗口；</li>
 *   <li>{@link #tickSunSet()} 每 tick 把被吸阳光拉向本体，距离 ≤2 时累加
 *       {@code sunAmount} 并移除阳光实体；</li>
 *   <li>若当前不在吸收窗口（{@link #checkCanWorkNow()} 为 false），
 *       释放所有阳光回 { DropStates#NORMAL}；</li>
 *   <li>死亡时 {@link #onRemoveWhenDeath()} 把累计阳光一次性
 *       {@link SunEntity#spawnSunsByAmount} 抛回。</li>
 * </ul>
 *
 * <p>掉落表沿用 {@link PvZLoot#RA_ZOMBIE}。
 */
public class RaZombieEntity extends PVZZombieEntity {

	public static final int ABSORB_TICK = 40;
	private final Set<SunEntity> sunSet = new HashSet<>();
	private final int MaxSearchTick = 400;
	private final int MinSearchTick = 100;
	private int searchTick = 0;
	private int sunAmount = 0;
	private final double MaxSearchRange = 20;

	public RaZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_LITTLE_SLOW);
		}
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			this.tickSunSet();
			if (this.getAttackTime() > 0) {
				this.setAttackTime(this.getAttackTime() - 1);
			}
			if (this.searchTick > 0) {
				--this.searchTick;
			}
			if (this.searchTick <= 0) {
				this.searchTick = this.getRandom().nextInt(this.MaxSearchTick - this.MinSearchTick + 1)
						+ this.MinSearchTick;
				this.setAttackTime(ABSORB_TICK);
				EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
				this.level().getEntitiesOfClass(SunEntity.class,
						MathUtil.getAABBWithPos(this.blockPosition(), MaxSearchRange),
						sun -> sun.getDropState() != DropEntity.DropStates.STEAL && !this.sunSet.contains(sun)
				).forEach(sun -> {
					sun.setDropState(DropEntity.DropStates.STEAL);
					this.sunSet.add(sun);
				});
			}
		}
	}

	private void tickSunSet() {
		Set<SunEntity> tmp = new HashSet<>();
		this.sunSet.forEach(sun -> {
			if (sun != null && !sun.isRemoved() && sun.getDropState() == DropEntity.DropStates.STEAL) {
				tmp.add(sun);
			}
		});
		this.sunSet.clear();
		this.sunSet.addAll(tmp);
		tmp.clear();
		if (!this.checkCanWorkNow()) {
			this.sunSet.forEach(sun -> sun.setDropState(DropEntity.DropStates.NORMAL));
			this.sunSet.clear();
			return;
		}
		this.sunSet.forEach(sun -> {
			final double speed = 0.3D;
			Vec3 now = this.position().add(0, this.getEyeHeight(), 0);
			Vec3 vec = now.subtract(sun.position());
			if (vec.length() <= 2) {
				this.sunAmount += sun.getAmount();
				sun.discard();
			} else {
				sun.setDeltaMovement(vec.normalize().scale(speed));
			}
		});
	}

	@Override
	protected void onRemoveWhenDeath() {
		super.onRemoveWhenDeath();
		if (!this.level().isClientSide()) {
			this.sunSet.forEach(sun -> sun.setDropState(DropEntity.DropStates.NORMAL));
			SunEntity.spawnSunsByAmount(this.level(), this.blockPosition(), this.sunAmount);
			this.sunAmount = 0;
		}
	}

	public boolean checkCanWorkNow() {
		return this.getAttackTime() > 0;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.searchTick = input.getIntOr("zombie_search_tick", 0);
		this.sunAmount = input.getIntOr("zombie_sun_amount", 0);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("zombie_search_tick", this.searchTick);
		output.putInt("zombie_sun_amount", this.sunAmount);
	}

	@Override
	public float getLife() {
		return 36;
	}

	@Override
	public IZombieType getZombieType() {
		return OtherZombies.RA_ZOMBIE.get();
	}
}