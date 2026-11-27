package com.lying.pvz.common.level.entity.zombie;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.PAZState;
import com.lying.pvz.api.entity.ability.*;
import com.lying.pvz.api.entity.projectile.IPAZProjectile;
import com.lying.pvz.api.entity.zombie.IZombieEntity;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.common.level.entity.ai.goal.PVZLookRandomlyGoal;
import com.lying.pvz.common.level.entity.ai.goal.PVZSwimGoal;
import com.lying.pvz.common.level.entity.ai.goal.ZombieBreakPlantBlockGoal;
import com.lying.pvz.common.level.entity.ai.goal.attack.PVZZombieAttackGoal;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.drop.CoinEntity;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.tag.PvZBlockTags;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ParticleUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * 僵尸实体基类。
 *
 * <p><b>动画字段</b>：{@link #ATTACK_ANIM_TIME} 与 {@link #RISING_ANIM_TIME}
 * 独立存放。前者由 {@code doHurtTarget} 置为正值、每 tick 递减到 0；
 * 后者由 {@code finalizeSpawn} 置为负值、每 tick 递增到 0。
 * 两者互不干扰，解决了旧版单字段下升起被攻击递减压到 0 的问题。
 * 渲染层需同时读取两者，按各自语义偏移。
 */
public abstract class PVZZombieEntity extends AbstractPAZEntity
        implements IZombieEntity, ICanDropBodyParts, ICanClimbWalls, ICanBeMini {

    private static final EntityDataAccessor<Integer> ZOMBIE_TYPE =
            SynchedEntityData.defineId(PVZZombieEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_TIME =
            SynchedEntityData.defineId(PVZZombieEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_ANIM_TIME =
            SynchedEntityData.defineId(PVZZombieEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RISING_ANIM_TIME =
            SynchedEntityData.defineId(PVZZombieEntity.class, EntityDataSerializers.INT);

    public static final int PERFORM_ATTACK_CD = 10;
    public static final int RISING_CD = 30;

    protected boolean needRising = false;
    public boolean canCollideWithZombie = true;
    protected boolean canLostHand = true;
    protected boolean canLostHead = true;
    protected int climbUpTick = 0;
    protected int maxClimbUpTick = 5;

    public PVZZombieEntity(EntityType<? extends AbstractPAZEntity> type, Level level) {
        super(type, level);
        this.xpReward = this.getZombieXp() / 2;
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 6.0F);
        this.setPathfindingMalus(PathType.DAMAGING_IN_NEIGHBOR, 6.0F);
        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 6.0F);
        this.setPathfindingMalus(PathType.LEAVES, 4F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ZOMBIE_TYPE, VariantType.NORMAL.ordinal());
        builder.define(ATTACK_TIME, 0);
        builder.define(ATTACK_ANIM_TIME, 0);
        builder.define(RISING_ANIM_TIME, 0);
    }

    public static AttributeSupplier createZombieAttributes() {
        return AbstractPAZEntity.createPAZAttributes()
                .add(Attributes.ATTACK_DAMAGE, ZombieUtil.VERY_LOW)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, ZombieUtil.CLOSE_TARGET_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9)
                .add(Attributes.MOVEMENT_SPEED, ZombieUtil.WALK_NORMAL)
                .add(Attributes.FLYING_SPEED, 0)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new PVZLookRandomlyGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new PVZSwimGoal(this));
        this.registerAttackGoals();
        this.registerTargetGoals();
    }

    protected void registerAttackGoals() {
        this.goalSelector.addGoal(3, new PVZZombieAttackGoal(this, true));
        this.goalSelector.addGoal(6, new ZombieBreakPlantBlockGoal(PvZBlockTags.ZOMBIE_BREAKABLE, this, 1F, 10));
    }

    protected void registerTargetGoals() {
        this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, true, true,
                ZombieUtil.NORMAL_TARGET_RANGE, ZombieUtil.NORMAL_TARGET_HEIGHT));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return super.createNavigation(level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        if (!this.level().isClientSide()) {
            this.setZombieType(this.rollVariantType());
            if (this.needRising) {
                this.setAttackAnimTime(0);
                this.setRisingAnimTime(-RISING_CD);
                this.setState(PAZState.RISING, true);
                this.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, RISING_CD + 10, 20, false, false));
            }
        }
        return data;
    }

    protected VariantType rollVariantType() {
        final int t = this.getRandom().nextInt(100);
        final int a = PvZConfig.LOCAL_CONFIG.EntitySettings.ZombieSetting.ZombieSuperChance.get();
        final int b = PvZConfig.LOCAL_CONFIG.EntitySettings.ZombieSetting.ZombieSunChance.get();
        return (t < a) ? VariantType.SUPER : (t < a + b) ? VariantType.SUN : VariantType.NORMAL;
    }

    @Override
    protected void updatePAZStates() {
        super.updatePAZStates();
        if (!this.level().isClientSide() && this.canBeMini() && this.isMiniZombie()) {
            this.onBeMini();
        }
    }

    @Override
    protected void refreshBaseAttributes() {
        super.refreshBaseAttributes();
        AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.setBaseValue(this.getEatDamage());
        }
        AttributeInstance followRange = this.getAttribute(Attributes.FOLLOW_RANGE);
        if (followRange != null) {
            followRange.setBaseValue(this.getFollowRange());
        }
        AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            movementSpeed.setBaseValue(this.getWalkSpeed());
        }
        AttributeInstance knockbackResistance = this.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (knockbackResistance != null) {
            knockbackResistance.setBaseValue(this.getKBValue());
        }
    }

    @Override
    public float getArmor() {
        return this.getSkillValue(SkillTypes.TOUGH_BODY.get());
    }

    @Override
    public void pazTick() {
        super.pazTick();
        Profiler.get().push("PVZ Zombie Tick");
        this.zombieTick();
        Profiler.get().pop();

        if (this.canNormalUpdate()) {
            Profiler.get().push("PVZ Normal Zombie Tick");
            this.normalZombieTick();
            Profiler.get().pop();
        }
    }

    public void zombieTick() {
        if (this.tickCount <= 2) {
            this.refreshDimensions();
        }
        if (this.isZombieRising()) {
            this.setRisingAnimTime(this.getRisingAnimTime() + 1);
            if (this.getRisingAnimTime() >= 0) {
                this.setRisingAnimTime(0);
                this.setState(PAZState.RISING, false);
            }
            if (level().isClientSide()) {
                ParticleUtil.spawnSplash(this.level(), this.position(), 1);
            }
        }
        if (!this.level().isClientSide()) {
            if (this.isInLava() && this.getExistTick() % 10 == 0 && this.getOwnerUUID().isEmpty()) {
                this.heal(20);
            }
        }
    }

    public void normalZombieTick() {
        if (!this.level().isClientSide()) {
            this.setAttackAnimTime(Math.max(0, this.getAttackAnimTime() - 1));
            if (this.isClimbing()) {
                if (++this.climbUpTick <= this.maxClimbUpTick) {
                    final Vec3 vec = this.getDeltaMovement();
                    this.setDeltaMovement(vec.x, this.getClimbSpeed(), vec.z);
                }
            } else {
                this.climbUpTick = 0;
            }
        }
    }

    @Override
    public boolean isClimbing() {
        return this.horizontalCollision;
    }

    @Override
    public double getClimbSpeed() {
        return 0.3D;
    }

    @Override
    public void onBeMini() {
        final float targetMaxHealth = this.getLife() * this.getMiniLifeRatio();
        if (Math.abs(this.getMaxHealth() - targetMaxHealth) > 0.01F) {
            AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(targetMaxHealth);
            }
        }
        this.addEffect(new MobEffectInstance(MobEffects.SPEED, 1000000, 0, false, false));
        this.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 1000000, 0, false, false));
    }

    @Override
    public boolean canLostHand() {
        return this.canLostHand;
    }

    @Override
    public boolean canLostHead() {
        return this.canLostHead;
    }

    @Override
    public boolean hasLostHand() {
        return this.hasState(PAZState.LOST_HAND);
    }

    @Override
    public boolean hasLostHead() {
        return this.hasState(PAZState.LOST_HEAD);
    }

    @Override
    public void onLostPart(BodyType type, DamageSource source) {
        switch (type) {
            case HAND -> {
                this.setState(PAZState.LOST_HAND, true);
                this.spawnDropBody(source, BodyType.HAND);
            }
            case HEAD -> {
                this.setState(PAZState.LOST_HEAD, true);
                this.spawnDropBody(source, BodyType.HEAD);
            }
            case BODY -> {
                ZombieDropBodyEntity body = this.spawnDropBody(source, BodyType.BODY);
                if (body != null) {
                    body.setMaxLiveTick(40);
                }
            }
            default -> {
            }
        }
    }

    /**
     * 创建并加入掉落肢体实体。返回创建的实体（可能为 null），
     * 供调用方追加部位特有配置（如 BODY 的寿命）。
     *
     * <p>统一在此处应用 {@link #setBodyStates}，保证手、头、身三部位的
     * 迷你状态一致。旧版仅 BODY 走 {@code setBodyStates} 是遗漏。
     */
    @Nullable
    protected ZombieDropBodyEntity spawnDropBody(DamageSource source, BodyType type) {
        ZombieDropBodyEntity body = PvZEntityRegister.ZOMBIE_DROP_BODY.get()
                .create(level(), EntitySpawnReason.TRIGGERED);
        if (body == null) {
            return null;
        }
        body.droppedByOwner(this, source, type);
        this.setBodyStates(body);
        level().addFreshEntity(body);
        return body;
    }

    protected void setBodyStates(ZombieDropBodyEntity body) {
        body.setMini(this.isMiniZombie());
    }

    public boolean checkCanLostHand() {
        return this.getHealth() < Math.min(40, this.getMaxHealth() * 0.5F);
    }

    public boolean checkCanLostHead() {
        return this.getHealth() < 10 && this.getHealth() / this.getMaxHealth() < 0.1F;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (!level.isClientSide()) {
            boolean flag = super.hurtServer(level, source, damage);
            if (ConfigUtil.enableZombieDropParts()) {
                if (this.hasHand() && this.canLostHand() && this.checkCanLostHand()) {
                    this.onLostPart(BodyType.HAND, source);
                }
                if (this.hasHead() && this.canLostHead() && this.checkCanLostHead()) {
                    this.onLostPart(BodyType.HEAD, source);
                }
            }
            return flag;
        }
        return false;
    }

    protected void dealDamageEffectToZombie(PvZEntityDamageSource source) {
        if (source.isDefended()) {
            return;
        }
        for (MobEffectInstance effect : source.getEffects()) {
            EntityUtil.addPotionEffect(this, effect);
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        target.setInvulnerableTime(0);
        this.setAttackAnimTime(PERFORM_ATTACK_CD);

        final PvZEntityDamageSource source = this.getZombieAttackDamageSource();
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        damage = EnchantmentHelper.modifyDamage(level, this.getWeaponItem(), target, source, damage);

        final float finalDamage = this.getModifyAttackDamage(target, damage);
        boolean flag = target.hurtServer(level, source, finalDamage);
        if (flag) {
            this.causeExtraKnockback(target, this.getKnockback(target, source),
                    Vec3.ZERO, source, finalDamage, false);

            if (target instanceof Player player) {
                this.maybeDisableShield(player, this.getMainHandItem(),
                        player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
            }
            EnchantmentHelper.doPostAttackEffects(level, target, source);
            this.setLastHurtMob(target);
        }
        return flag;
    }

    private void maybeDisableShield(Player player, ItemStack mainHand, ItemStack shieldInUse) {
        if (mainHand.isEmpty() || shieldInUse.isEmpty()) {
            return;
        }
        if (!mainHand.is(ItemTags.AXES)) {
            return;
        }
        if (shieldInUse.getItem() != Items.SHIELD) {
            return;
        }
        if (this.random.nextFloat() < 0.25F) {
            player.getCooldowns().addCooldown(shieldInUse, 100);
            player.level().broadcastEntityEvent(player, (byte) 30);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return 9 - level.getBrightness(LightLayer.BLOCK, pos);
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        this.fallDistance = 0.0F;
        this.stuckSpeedMultiplier = Vec3.ZERO;
    }

    @Override
    protected float getBlockSpeedFactor() {
        Block block = this.level().getBlockState(this.blockPosition()).getBlock();
        float factor = block.getSpeedFactor();
        if (block == Blocks.WATER || block == Blocks.BUBBLE_COLUMN) {
            return factor;
        }
        return 1F;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.85f;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public float getEatDamage() {
        return ZombieUtil.VERY_LOW;
    }

    public float getWalkSpeed() {
        return ZombieUtil.WALK_NORMAL;
    }

    public float getKBValue() {
        return 0.92F;
    }

    public float getFollowRange() {
        return ZombieUtil.CLOSE_TARGET_RANGE;
    }

    protected PvZEntityDamageSource getZombieAttackDamageSource() {
        return PvZEntityDamageSource.eat(this);
    }

    protected float getModifyAttackDamage(Entity entity, float f) {
        return f;
    }

    @Override
    public void push(Entity entityIn) {
        if (this.isSleeping()) {
            return;
        }
        if (this.isPassengerOfSameVehicle(entityIn)) {
            return;
        }
        if (entityIn.noPhysics || this.noPhysics) {
            return;
        }
        double d0 = entityIn.getX() - this.getX();
        double d1 = entityIn.getZ() - this.getZ();
        double d2 = Mth.absMax(d0, d1);
        if (d2 >= 0.009999999776482582D) {
            d2 = Mth.sqrt((float) d2);
            d0 = d0 / d2;
            d1 = d1 / d2;
            double d3 = 1.0D / d2;
            if (d3 > 1.0D) {
                d3 = 1.0D;
            }
            d0 = d0 * d3;
            d1 = d1 * d3;
            d0 = d0 * 0.05000000074505806D;
            d1 = d1 * 0.05000000074505806D;
            if (!this.isVehicle() && this.isPushable()) {
                this.push(-d0, 0.0D, -d1);
            }
            if (!entityIn.isVehicle() && checkCanPushEntity(entityIn)) {
                entityIn.push(d0, 0.0D, d1);
            }
        }
    }

    @Override
    protected void pushEntities() {
        double dd = this.getCollideWidthOffset();
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        List<LivingEntity> list = serverLevel.getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(dd, 0, dd));
        if (list.isEmpty()) {
            return;
        }
        int i = serverLevel.getServer().getGameRules().get(GameRules.MAX_ENTITY_CRAMMING);
        if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
            int j = 0;
            for (LivingEntity living : list) {
                if (!living.isPassenger()) {
                    ++j;
                }
            }
            if (j > i - 1) {
                this.hurtServer(serverLevel, this.damageSources().cramming(), 6.0F);
            }
        }
        for (LivingEntity target : list) {
            if (!this.is(target) && this.shouldCollideWithEntity(target)) {
                this.doPush(target);
            }
        }
    }

    protected double getCollideWidthOffset() {
        return -0.25D;
    }

    protected boolean shouldCollideWithEntity(LivingEntity target) {
        if (this.getTarget() == target) {
            return true;
        }
        if (target instanceof PVZZombieEntity zombie) {
            return this.canCollideWithZombie && zombie.canCollideWithZombie;
        }
        return false;
    }

    protected boolean checkCanPushEntity(Entity target) {
        return !(target instanceof PlantEntity);
    }

    @Override
    public boolean canBeAttractedBy(ICanAttract defender) {
        return true;
    }

    @Override
    public void attractBy(ICanAttract defender) {
    }

    public void checkAndAddPotionEffect(MobEffectInstance effect) {
        if (effect.is(PvZEffectRegister.COLD_EFFECT) && !this.canBeCold()) {
            return;
        }
        if (effect.is(PvZEffectRegister.FROZEN_EFFECT) && !this.canBeFrozen()) {
            return;
        }
        if (effect.is(PvZEffectRegister.BUTTER_EFFECT) && !this.canBeButtered()) {
            return;
        }
        this.addEffect(effect);
    }

    @Override
    public void onCharmedBy(@Nullable LivingEntity entity) {
        super.onCharmedBy(entity);
        if (this.canBeCharmed()) {
            this.setCharmed(!this.isCharmed());
            if (this.getVariantType() == VariantType.SUPER) {
                this.setZombieType(VariantType.NORMAL);
                this.dropEnergy();
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
        if (source instanceof PvZEntityDamageSource pds && pds.isMustHurt()) {
            return false;
        }
        return !source.is(DamageTypes.FELL_OUT_OF_WORLD)
                && !source.isCreativePlayer()
                && this.isZombieInvulnerableTo(source);
    }

    protected boolean isZombieInvulnerableTo(DamageSource source) {
        return this.isZombieRising()
                || (!EntityUtil.isEntityValid(source.getEntity()) && !isMagicDamage(source));
    }

    private static boolean isMagicDamage(DamageSource source) {
        return source.is(DamageTypes.MAGIC)
                || source.is(DamageTypes.INDIRECT_MAGIC)
                || source.is(DamageTypes.THORNS);
    }

    @Override
    public boolean fireImmune() {
        return !ConfigUtil.immuineToDamage();
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return !ConfigUtil.immuineToDamage();
    }

    public boolean isZombieRising() {
        return this.hasState(PAZState.RISING);
    }

    public void setZombieRising() {
        this.needRising = true;
    }

    public void setImmuneAllEffects() {
        this.canBeButtered = false;
        this.canBeCold = false;
        this.canBeFrozen = false;
    }

    public void setIsWholeBody() {
        this.canLostHand = false;
        this.canLostHead = false;
    }

    public boolean isZombieColdOrForzen() {
        return EntityUtil.isEntityCold(this) || EntityUtil.isEntityFrozen(this);
    }

    @Override
    public boolean hasMetal() {
        return false;
    }

    @Override
    public void decreaseMetal() {
    }

    @Override
    public void increaseMetal() {
    }

    @Override
    public MetalTypes getMetalType() {
        return MetalTypes.EMPTY;
    }

    @Override
    public GroupType getEntityGroupType() {
        return this.isCharmed() ? GroupType.PLANTS : GroupType.ZOMBIES;
    }

    @Override
    public IPAZType getPAZType() {
        return this.getZombieType();
    }

    public abstract IZombieType getZombieType();

    @Override
    public boolean canBeHitBy(Entity source) {
        return !this.isZombieRising() && super.canBeHitBy(source);
    }

    @Override
    public boolean canPAZTarget(Entity target) {
        if (target.isInWater()
                && target.getFluidTypeHeight(NeoForgeMod.WATER_TYPE.value()) + 0.05 > target.getBbHeight()) {
            return false;
        }
        return super.canPAZTarget(target);
    }

    public int getAttackCD() {
        if (!this.canNormalUpdate()) {
            return 10000000;
        }
        int cd = 20;
        MobEffectInstance coldEffect = this.getEffect(PvZEffectRegister.COLD_EFFECT);
        if (coldEffect != null) {
            cd += 3 * coldEffect.getAmplifier();
        }
        return cd;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isMiniZombie()
                ? EntityDimensions.scalable(0.3F, 0.6F)
                : EntityDimensions.scalable(0.8f, 1.98f);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (ConfigUtil.enableZombieDropParts() && !this.level().isClientSide()) {
            this.onLostPart(BodyType.BODY, source);
        }
    }

    @Override
    protected boolean canRemoveWhenDeath() {
        return (ConfigUtil.enableZombieDropParts() && !level().isClientSide()) || super.canRemoveWhenDeath();
    }

    @Override
    protected void onRemoveWhenDeath() {
        super.onRemoveWhenDeath();
        if (level().isClientSide()) {
            return;
        }
        switch (this.getVariantType()) {
            case SUPER -> this.dropEnergy();
            case SUN -> this.dropSun();
            case BEARD -> {
            }
            default -> {
            }
        }
        if (this.canSpawnDrop) {
            this.spawnSpecialDrops();
        }
    }

    protected void dropEnergy() {
        EntityUtil.createEntityAndSpawn(level(), PvZEntityRegister.ENERGY.get(), this.blockPosition().above());
    }

    protected void dropSun() {
        int num = this.getRandom().nextInt(8) + 3;
        for (int i = 0; i < num; ++i) {
            SunEntity.dropSunRandomly(level(), blockPosition().above(), 25, 2);
        }
    }

    protected void spawnSpecialDrops() {
        getDropSpecialList().getRandomItem(this.random).ifPresent(this::doZombieDrop);
    }

    private void doZombieDrop(DropType type) {
        switch (type) {
            case COPPER -> CoinEntity.spawnCoin(level(), blockPosition(), CoinEntity.CoinType.COPPER);
            case SILVER -> CoinEntity.spawnCoin(level(), blockPosition(), CoinEntity.CoinType.SILVER);
            case GOLD -> CoinEntity.spawnCoin(level(), blockPosition(), CoinEntity.CoinType.GOLD);
            case JEWEL -> EntityUtil.createEntityAndSpawn(level(), PvZEntityRegister.JEWEL.get(), blockPosition());
            case CHOCOLATE -> {
                ItemEntity chocolate = new ItemEntity(level(), getX(), getY(), getZ(),
                        new ItemStack(PvZItemRegister.CHOCOLATE.get()));
                EntityUtil.playSound(chocolate, PvZSoundRegister.JEWEL_DROP.get());
                level().addFreshEntity(chocolate);
            }
        }
    }

    public boolean canBreakPlantBlock() {
        return !this.isCharmed();
    }

    public boolean canZombieBeRemoved() {
        return this.canBeRemove;
    }

    public boolean canHelpAttack() {
        return this.canHelpAttack;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return PvZSoundRegister.ZOMBIE_GROAN.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        if (source.getDirectEntity() instanceof IPAZProjectile) {
            return PvZSoundRegister.SPLAT.get();
        }
        return super.getHurtSound(source);
    }

    @Override
    public Optional<SoundEvent> getSpawnSound() {
        if (this.needRising) {
            return Optional.ofNullable(PvZSoundRegister.DIRT_RISE.get());
        }
        return Optional.empty();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("zombie_type", this.getVariantType().ordinal());
        output.putInt("zombie_attack_time", this.getAttackTime());
        output.putInt("zombie_anim_time", this.getAttackAnimTime());
        output.putInt("zombie_rising_anim_time", this.getRisingAnimTime());
        output.putBoolean("need_rising", this.needRising);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setZombieType(VariantType.fromOrdinal(input.getIntOr("zombie_type", 0)));
        this.setAttackTime(input.getIntOr("zombie_attack_time", 0));
        this.needRising = input.getBooleanOr("need_rising", false);

        final int savedAnimTime = input.getIntOr("zombie_anim_time", 0);
        final int savedRisingAnim = input.getIntOr("zombie_rising_anim_time", Integer.MIN_VALUE);
        if (savedRisingAnim == Integer.MIN_VALUE) {
            // 旧存档：单一 anim_time，负值表示升起中
            if (savedAnimTime < 0) {
                this.setAttackAnimTime(0);
                this.setRisingAnimTime(savedAnimTime);
            } else {
                this.setAttackAnimTime(savedAnimTime);
                this.setRisingAnimTime(0);
            }
        } else {
            this.setAttackAnimTime(Math.max(0, savedAnimTime));
            this.setRisingAnimTime(savedRisingAnim);
        }
    }

    public int getAttackTime() {
        return this.entityData.get(ATTACK_TIME);
    }

    public void setAttackTime(int cd) {
        this.entityData.set(ATTACK_TIME, cd);
    }

    /**
     * 攻击动画时间。正值表示正在播放攻击动画，每 tick 递减到 0。
     *
     * <p>兼容方法：等价于 {@link #getAttackAnimTime()}。旧调用点可继续使用。
     */
    public int getAnimTime() {
        return this.getAttackAnimTime();
    }

    /**
     * 设置攻击动画时间。兼容方法：等价于 {@link #setAttackAnimTime(int)}。
     */
    public void setAnimTime(int cd) {
        this.setAttackAnimTime(cd);
    }

    /**
     * 攻击动画时间。{@code doHurtTarget} 置为 {@link #PERFORM_ATTACK_CD}，
     * 由 {@link #normalZombieTick()} 每 tick 递减到 0。
     */
    public int getAttackAnimTime() {
        return this.entityData.get(ATTACK_ANIM_TIME);
    }

    public void setAttackAnimTime(int cd) {
        this.entityData.set(ATTACK_ANIM_TIME, Math.max(0, cd));
    }

    /**
     * 升起动画时间。{@code finalizeSpawn} 置为 {@code -RISING_CD}，
     * 由 {@link #zombieTick()} 每 tick 递增到 0，到 0 时清 RISING 位。
     *
     * <p>渲染层据此偏移身体高度：{@code -RISING_CD} 时身体完全在土里，
     * 0 时完全露出。
     */
    public int getRisingAnimTime() {
        return this.entityData.get(RISING_ANIM_TIME);
    }

    public void setRisingAnimTime(int cd) {
        this.entityData.set(RISING_ANIM_TIME, Math.min(0, cd));
    }

    public VariantType getVariantType() {
        return VariantType.fromOrdinal(this.entityData.get(ZOMBIE_TYPE));
    }

    public void setZombieType(VariantType type) {
        this.entityData.set(ZOMBIE_TYPE, type.ordinal());
    }

    @Override
    public boolean isCharmed() {
        return this.hasState(PAZState.CHARMED);
    }

    public void setCharmed(boolean flag) {
        this.setState(PAZState.CHARMED, flag);
    }

    public boolean isMiniZombie() {
        return this.hasState(PAZState.MINI);
    }

    public void setMiniZombie(boolean flag) {
        this.setState(PAZState.MINI, flag);
    }

    public boolean hasHand() {
        return !this.hasState(PAZState.LOST_HAND);
    }

    public void lostHand(boolean lost) {
        this.setState(PAZState.LOST_HAND, lost);
    }

    public boolean hasHead() {
        return !this.hasState(PAZState.LOST_HEAD);
    }

    public void lostHead(boolean lost) {
        this.setState(PAZState.LOST_HEAD, lost);
    }

    public enum VariantType {
        NORMAL,
        SUPER,
        BEARD,
        SUN;

        public static VariantType fromOrdinal(int ordinal) {
            final VariantType[] values = values();
            if (ordinal < 0 || ordinal >= values.length) {
                return NORMAL;
            }
            return values[ordinal];
        }
    }
}