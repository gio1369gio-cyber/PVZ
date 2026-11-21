package com.lying.pvz.common.level.entity.plant.magic;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantBomberEntity;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlantUtil;
import com.lying.pvz.utils.WorldUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

/**
 * 模仿者。倒计时结束后按内部卡类型执行“召唤 / 放方块 / 外层叠加”三选一。
 *
 * <p>三种执行模式由 {@link ImitateType} 决定：
 * <ul>
 *   <li>{@link ImitateType#SUMMON}：走 {@code handlePlantEntity / handlePlantBlock}，
 *       正常召唤植物；</li>
 *   <li>{@link ImitateType#OUTER}：把 {@code consumer} 作用到 {@code targetEntity} 上，
 *       用于南瓜壳等外层叠加。</li>
 * </ul>
 *
 * <p>内部卡（被模仿的卡）读自 synched {@link #IMITATE_CARD}，
 * 类型判定走 {@code PlantCardItem#getType()} 后 {@code instanceof IPlantType}。
 */
public class ImitaterEntity extends PlantBomberEntity {

	private static final EntityDataAccessor<ItemStack> IMITATE_CARD =
			SynchedEntityData.defineId(ImitaterEntity.class, EntityDataSerializers.ITEM_STACK);

	private Entity targetEntity;
	private Direction placeDirection = Direction.NORTH;
	private ImitateType imitateType = ImitateType.SUMMON;
	private Consumer<PlantEntity> consumer = (e) -> {};

	public ImitaterEntity(EntityType<? extends PlantBomberEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithPlant = false;
		this.setImmuneToWeak(true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(IMITATE_CARD, ItemStack.EMPTY);
	}

	@Override
	protected void registerGoals() {
	}

	@Override
	protected void startBomb(boolean server) {
		if (server) {
			final ItemStack card = this.getImitateCard();
			if (!(card.getItem() instanceof PlantCardItem plantCard)) {
				PvZMod.LOGGER.warn("Imitate Error : Wrong Card !");
				return;
			}
			final IPAZType rawType = plantCard.getType();
			if (!(rawType instanceof IPlantType plantType)) {
				PvZMod.LOGGER.warn("Imitate Error : Card type is not a plant type !");
				return;
			}
			EntityUtil.playSound(this, PvZSoundRegister.WAKE_UP.get());
			if (plantType == PVZPlants.IMITATER.get()) {
				this.imitateRandomly();
			} else {
				this.imitate(plantType);
			}
		} else {
			for (int i = 0; i < 3; ++i) {
				WorldUtil.spawnRandomSpeedParticle(this.level(), ParticleTypes.EXPLOSION,
						this.position(), 0.01F);
			}
		}
	}

	public void imitate(IPlantType plantType) {
		if (this.imitateType == ImitateType.OUTER) {
			if (this.consumer != null && this.targetEntity instanceof PlantEntity plant) {
				this.consumer.accept(plant);
			}
			return;
		}

		if (plantType.getPlantBlock().isPresent()) {
			if (this.blockPosition().getY() >= 2) {
				final BlockState state = PlantCardItem.getBlockState(this.placeDirection, plantType);
				PlantCardItem.handlePlantBlock(this.level(), plantType, state,
						this.blockPosition());
			}
			return;
		}

		this.getOwnerPlayer().ifPresent(player ->
				PlantCardItem.handlePlantEntity(player, plantType, this.getImitateCard(),
						this.blockPosition(), plantEntity -> {
							PlantUtil.copyPlantData(this, plantEntity);
							PlantCardItem.applyCardSkillsToPlant(plantEntity, this.getImitateCard());
							PlantCardItem.enchantPlantEntityByCard(plantEntity, this.getImitateCard(),
									this.level().registryAccess());
							if (this.getVehicle() != null) {
								this.stopRiding();
								plantEntity.startRiding(this.getVehicle());
							}
							if (this.getTarget() != null) {
								plantEntity.setTarget(this.getTarget());
							}
							this.consumer.accept(plantEntity);
						}));
	}

	public void imitateRandomly() {
	}

	@Override
	public void tick() {
		this.noPhysics = true;
		super.tick();
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return false;
	}

	public void setImitateAction(Consumer<PlantEntity> c) {
		this.consumer = c == null ? (e) -> {} : c;
	}

	public void setImitateType(ImitateType type) {
		this.imitateType = type;
	}

	public void setDirection(Direction dir) {
		this.placeDirection = dir;
	}

	public void setTargetEntity(Entity entity) {
		this.targetEntity = entity;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public int getReadyTime() {
		return 30;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.7F, 1.25F);
	}

	public void setImitateCard(ItemStack stack) {
		this.entityData.set(IMITATE_CARD, stack);
	}

	public ItemStack getImitateCard() {
		return this.entityData.get(IMITATE_CARD);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.IMITATER.get();
	}

	public enum ImitateType {
		SUMMON,
		OUTER
	}
}