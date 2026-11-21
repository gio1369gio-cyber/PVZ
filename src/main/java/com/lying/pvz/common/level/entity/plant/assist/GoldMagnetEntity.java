package com.lying.pvz.common.level.entity.plant.assist;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.drop.CoinEntity;
import com.lying.pvz.common.level.entity.drop.DropEntity;
import com.lying.pvz.common.level.entity.drop.JewelEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.lying.pvz.utils.enums.Resources;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 金磁石（Gold Magnet）。
 *
 * <p>周期搜索附近 {@link CoinEntity} / {@link JewelEntity}，将其状态置为
 * {@link #onCollectCoin(DropEntity)} 结算。
 */
public class GoldMagnetEntity extends PlantEntity {

	private final Set<DropEntity> coinSet = new HashSet<>();
	private static final int SEARCH_CD = 60;

	public GoldMagnetEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide()) {
			this.tickCoinSet();
			this.setAttackTime(this.coinSet.size());
		}
	}

	private void tickCoinSet() {
		final Set<DropEntity> tmp = new HashSet<>();
		this.coinSet.forEach(coin -> {
			if (EntityUtil.isEntityValid(coin) && coin.getDropState() == DropEntity.DropStates.ABSORB) {
				tmp.add(coin);
			}
		});
		this.coinSet.clear();
		this.coinSet.addAll(tmp);
		tmp.clear();

		if (!this.checkCanWorkNow()) {
			this.coinSet.forEach(coin -> coin.setDropState(DropEntity.DropStates.NORMAL));
			this.coinSet.clear();
			return;
		}

		if (this.getExistTick() % SEARCH_CD == 0) {
			final float range = this.getSearchRange();
			this.level().getEntitiesOfClass(DropEntity.class,
					EntityUtil.getEntityAABB(this, range, range),
					drop -> (drop instanceof CoinEntity || drop instanceof JewelEntity)
							&& drop.getDropState() == DropEntity.DropStates.NORMAL
							&& !this.coinSet.contains(drop)
			).forEach(coin -> {
				coin.setDropState(DropEntity.DropStates.ABSORB);
				this.coinSet.add(coin);
			});
			if (!this.coinSet.isEmpty()) {
				EntityUtil.playSound(this, PvZSoundRegister.MAGNET.get());
			}
		}

		this.coinSet.forEach(coin -> {
			final double speed = 0.35D;
			final Vec3 now = new Vec3(this.getX(), this.getY() + this.getBbHeight(), this.getZ());
			final Vec3 vec = now.subtract(coin.position());
			if (vec.length() <= 1) {
				this.onCollectCoin(coin);
			} else {
				coin.setDeltaMovement(vec.normalize().scale(speed));
			}
		});
	}

	protected void onCollectCoin(DropEntity drop) {
		this.getOwnerPlayer().ifPresent(player -> {
			if (drop instanceof CoinEntity) {
				PlayerUtil.addResource(player, Resources.MONEY, drop.getAmount());
				EntityUtil.playSound(this, PvZSoundRegister.COIN_PICK.get());
			} else if (drop instanceof JewelEntity) {
				PlayerUtil.addResource(player, Resources.GEM_NUM, drop.getAmount());
				EntityUtil.playSound(this, PvZSoundRegister.JEWEL_PICK.get());
			}
		});
		drop.discard();
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.WORK_RANGE, this.getSearchRange()));
	}

	public int getSearchRange() {
		return 8;
	}

	protected boolean canWorkNow() {
		return this.getOwnerPlayer().isPresent();
	}

	/** 兼容旧调用点。 */
	protected boolean checkCanWorkNow() {
		return this.canWorkNow();
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.5F, 1.3F);
	}

	@Override
	public int getSuperTimeLength() {
		return 0;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.GOLD_MAGNET.get();
	}
}