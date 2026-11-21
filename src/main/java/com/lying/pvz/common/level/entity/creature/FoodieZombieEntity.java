package com.lying.pvz.common.level.entity.creature;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.WaterTemptGoal;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.zombie.pool.SnorkelZombieEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;

public class FoodieZombieEntity extends Animal {

	private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(PvZItemRegister.FAKE_BRAIN.get(),
			PvZItemRegister.REAL_BRAIN.get());
	private static final EntityDataAccessor<Integer> GEN_TICK =
			SynchedEntityData.defineId(FoodieZombieEntity.class, EntityDataSerializers.INT);
	protected int lvl;
	protected static final int MAX_LVL = 10;

	public FoodieZombieEntity(EntityType<? extends Animal> type, Level worldIn) {
		super(type, worldIn);
		this.moveControl = new MoveHelperController(this);
		this.setPathfindingMalus(PathType.WATER, 0.0F);
		this.refreshDimensions();
		this.lvl = 1;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(GEN_TICK, -1);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new BreathAirGoal(this));
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new WaterTemptGoal(this, 1.0D, TEMPTATION_ITEMS,false));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide() && this.getGenTick() >= 0) {
			this.setGenTick(this.getGenTick() - 1);
			if (this.getGenTick() == 0) {
				this.produceSun();
			}
		}
	}

	protected void produceSun() {
		SunEntity sun = PvZEntityRegister.SUN.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
		if (sun == null) {
			return;
		}
		sun.setPos(this.getX(), this.getY() + 1, this.getZ());
		sun.setAmount(this.getSunAmount());
		this.level().addFreshEntity(sun);
	}

	protected int getSunAmount() {
		return 20 + this.lvl * 5;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (this.isFood(itemstack)) {
			if (!this.level().isClientSide() && this.getAge() == 0 && this.getGenTick() == -1 && this.canFallInLove()) {
				this.usePlayerItem(player, hand, itemstack);
				this.playSound(PvZSoundRegister.SLURP.get(), 1f, 1f);
				this.setInLove(player);
				this.setGenTick(this.getGenCD());
				player.swing(hand, SwingAnimation.DEFAULT,true);
				return InteractionResult.CONSUME;
			}

			if (this.isBaby()) {
				this.usePlayerItem(player, hand, itemstack);
				this.ageUp((int) ((float) (-this.getAge() / 20) * 0.1F), true);
				return InteractionResult.CONSUME;
			} else {
				if (itemstack.getItem() == PvZItemRegister.REAL_BRAIN.get() && this.lvl <= MAX_LVL) {
					++this.lvl;
					return InteractionResult.CONSUME;
				}
			}
			return InteractionResult.FAIL;
		}
		return super.mobInteract(player, hand);
	}

	private int getGenCD() {
		return 600 - this.lvl * 20;
	}


	@Override
	protected EntityDimensions getDefaultDimensions(Pose pose) {
		if (this.isBaby()) {
			return EntityDimensions.scalable(0.3f, 0.3f);
		}
		return EntityDimensions.scalable(0.7f, 0.5f);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageable) {
		return PvZEntityRegister.FOODIE_ZOMBIE.get().create(level, EntitySpawnReason.MOB_SUMMONED);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return TEMPTATION_ITEMS.test(stack);
	}


	@Override
	public boolean checkSpawnObstruction(LevelReader worldIn) {
		return worldIn.isUnobstructed(this);
	}

	@Override
	protected PathNavigation createNavigation(Level worldIn) {
		return new AmphibiousPathNavigation(this, worldIn);
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 20) {
			if (!this.level().isClientSide()) {
				this.level().broadcastEntityEvent(this, (byte) 60);
				this.remove(RemovalReason.KILLED);
			}
			for (int i = 0; i < 20; ++i) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level().addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(),
						this.getRandomZ(1.0D), d0, d1, d2);
			}
			this.doDeathSpawn();
		}
	}

	private void doDeathSpawn() {
		if (!this.level().isClientSide()) {
			SnorkelZombieEntity snorkel = PvZEntityRegister.SNORKEL_ZOMBIE.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
			if (snorkel == null) {
				return;
			}
			snorkel.setPos(this.getX(), this.getY(), this.getZ());
			this.level().addFreshEntity(snorkel);
		}
	}

	@Deprecated
	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public boolean canBeLeashed() {
		return true;
	}

	public int getGenTick() {
		return this.entityData.get(GEN_TICK);
	}

	public void setGenTick(int tick) {
		this.entityData.set(GEN_TICK, tick);
	}

	static class MoveHelperController extends MoveControl {
		private final FoodieZombieEntity zombie;

		public MoveHelperController(FoodieZombieEntity zombie) {
			super(zombie);
			this.zombie = zombie;
		}

		@Override
		public void tick() {
			if (this.zombie.isInWater()) {
				this.zombie.setDeltaMovement(this.zombie.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
			}

			if (this.operation == Operation.MOVE_TO && !this.zombie.getNavigation().isDone()) {
				double d0 = this.wantedX - this.zombie.getX();
				double d1 = this.wantedY - this.zombie.getY();
				double d2 = this.wantedZ - this.zombie.getZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				if (d3 < 2.5000003E-7F) {
					this.mob.setZza(0.0F);
				} else {
					float f = (float) (Mth.atan2(d2, d0) * (180F / Math.PI)) - 90.0F;
					this.zombie.setYRot(this.rotlerp(this.zombie.getYRot(), f, 10.0F));
					this.zombie.yBodyRot = this.zombie.getYRot();
					this.zombie.yHeadRot = this.zombie.getYRot();
					float f1 = (float) (this.speedModifier * this.zombie.getAttributeValue(Attributes.MOVEMENT_SPEED));
					if (this.zombie.isInWater()) {
						this.zombie.setSpeed(f1 * 0.02F);
						float f2 = -((float) (Mth.atan2(d1, Mth.sqrt((float) (d0 * d0 + d2 * d2))) * (180F / Math.PI)));
						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
						this.zombie.setXRot(this.rotlerp(this.zombie.getXRot(), f2, 5.0F));
						float f3 = Mth.cos(this.zombie.getXRot() * ((float) Math.PI / 180F));
						float f4 = Mth.sin(this.zombie.getXRot() * ((float) Math.PI / 180F));
						this.zombie.setZza(f3 * f1);
						this.zombie.setYya(-f4 * f1);
					} else {
						this.zombie.setSpeed(f1 * 0.1F);
					}
				}
			} else {
				this.zombie.setSpeed(0.0F);
				this.zombie.setXxa(0.0F);
				this.zombie.setYya(0.0F);
				this.zombie.setZza(0.0F);
			}
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.lvl = input.getIntOr("zombie_lvl", this.lvl);
		this.setGenTick(input.getIntOr("gen_tick", -1));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("zombie_lvl", this.lvl);
		output.putInt("gen_tick", this.getGenTick());
	}
}