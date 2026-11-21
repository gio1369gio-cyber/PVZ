package com.lying.pvz.common.level.entity.plant.base;

import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

/**
 * 一次性炸弹类植物基类。
 *
 * <p>生命周期由 {@link #normalPlantTick()} 驱动：{@code attackTime} 从 0 递增，
 * 到达 {@link #getReadyTime()} 时服务端 {@link #startBomb(boolean)} 并 {@code discard}，
 * 客户端在 {@code readyTime - 1} 帧触发一次本地表现（粒子/音效）。
 *
 * <p>没有超级模式：不覆写 {@code getSuperTimeLength()}，由 {@link PlantEntity}
 * 默认 0，{@code ICanSuperMode#canStartSuperMode()} 自然返回 false。
 */
public abstract class PlantBomberEntity extends PlantEntity {

	protected boolean hasBombAlamancs = true;

	public PlantBomberEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		final int time = this.getAttackTime();
		if (this.level().isClientSide()) {
			if (time == this.getReadyTime() - 1) {
				this.startBomb(false);
			}
		} else {
			if (time > this.getReadyTime()) {
				this.startBomb(true);
				this.discard();
			}
			this.setAttackTime(time + 1);
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		if (this.hasBombAlamancs) {
			list.addAll(Arrays.asList(
					Pair.of(PAZAlmanacs.EXPLODE_DAMAGE, this.getExplodeDamage()),
					Pair.of(PAZAlmanacs.EXPLODE_RANGE, this.getExplodeRange())
			));
		}
	}

	public float getExplodeDamage() {
		return 0;
	}

	public float getExplodeRange() {
		return 0;
	}

	/**
	 * @param server {@code true} 只在服务端执行真实爆炸；{@code false} 仅客户端特效
	 */
	protected abstract void startBomb(boolean server);

	public abstract int getReadyTime();

	@Override
	public boolean isPlantImmuneTo(DamageSource source) {
		return super.isPlantImmuneTo(source) || this.canNormalUpdate();
	}

	@Override
	public int getSuperTimeLength() {
		return 0;
	}

}