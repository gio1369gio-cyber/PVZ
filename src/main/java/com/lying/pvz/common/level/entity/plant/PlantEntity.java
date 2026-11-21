package com.lying.pvz.common.level.entity.plant;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.card.CardContent;
import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.PlantCombatStyle;
import com.lying.pvz.api.content.paz.PlantTrait;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.PAZState;
import com.lying.pvz.api.entity.ability.*;
import com.lying.pvz.api.entity.plant.IPlantEntity;
import com.lying.pvz.api.entity.plant.IPlantInfo;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.*;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.common.level.entity.ai.goal.PVZLookRandomlyGoal;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.plant.light.GoldLeafEntity;
import com.lying.pvz.common.level.entity.plant.magic.CoffeeBeanEntity;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.common.level.item.card.CardPlacementService;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.common.tag.PvZEntityTypeTags;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EffectUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 植物实体基类。
 */
public abstract class PlantEntity extends AbstractPAZEntity
        implements IPlantEntity, ICanWilt, ICanSleep, ICanSuperMode,
        ICanPlaceOuter, ICanBeUpgraded, ICanBeCollidedBy {

    private static final EntityDataAccessor<Integer> SUPER_TIME =
            SynchedEntityData.defineId(PlantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_TIME =
            SynchedEntityData.defineId(PlantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> GOLD_TIME =
            SynchedEntityData.defineId(PlantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BOOST_TIME =
            SynchedEntityData.defineId(PlantEntity.class, EntityDataSerializers.INT);

    private static final int PLANT_WEAK_CD = 10;

    protected IPlantInfo innerPlant;
    protected IPlantInfo outerPlant;
    protected ItemEnchantments cardEnchantments = ItemEnchantments.EMPTY;
    protected boolean isImmuneToWeak = false;
    protected int weakTime = 0;
    public int sleepTime = 0;

    public boolean canCollideWithPlant = true;

    public PlantEntity(EntityType<? extends AbstractPAZEntity> type, Level level) {
        super(type, level);
        this.innerPlant = new PlantInfo(this.getPlantType());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SUPER_TIME, 0);
        builder.define(ATTACK_TIME, 0);
        builder.define(GOLD_TIME, 0);
        builder.define(BOOST_TIME, 0);
    }

    public static AttributeSupplier createPlantAttributes() {
        return AbstractPAZEntity.createPAZAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new PVZLookRandomlyGoal(this));
    }

    @Override
    public void onSpawnedByPlayer(@Nullable Player player, int sunCost) {
        super.onSpawnedByPlayer(player, sunCost);
        this.getPlantInfo().ifPresent(info -> info.setSunCost(sunCost));
    }

    @Override
    public void applyCardData(SkillLevels skills, ItemEnchantments enchantments) {
        this.applySkillLevels(skills);
        this.cardEnchantments = enchantments == null ? ItemEnchantments.EMPTY : enchantments;
    }

    public ItemEnchantments getCardEnchantments() {
        return this.cardEnchantments;
    }

    @Override
    public void pazTick() {
        super.pazTick();
        Profiler.get().push("PVZ Plant Tick");
        this.plantTick();
        Profiler.get().pop();

        if (this.canNormalUpdate()) {
            Profiler.get().push("PVZ Normal Plant Tick");
            this.normalPlantTick();
            Profiler.get().pop();
        }

        if (!this.level().isClientSide()) {
            this.getOwnerPlayer().ifPresent(p -> this.canDespawn = this.shouldDespawnByAnchor(p));
        }
    }

    protected @Nullable BlockPos getDespawnAnchor(Player owner) {
        if (owner instanceof ServerPlayer serverPlayer) {
            final ServerPlayer.RespawnConfig config = serverPlayer.getRespawnConfig();
            return config == null ? null : config.respawnData().pos();
        }
        return null;
    }

    protected boolean shouldDespawnByAnchor(Player owner) {
        final BlockPos anchor = this.getDespawnAnchor(owner);
        if (anchor == null) {
            return false;
        }
        final int range = ConfigUtil.despawnOwnedEntityRange();
        if (range < 0) {
            return false;
        }
        return this.tickCount > ConfigUtil.getPlantMinimumTick()
                && anchor.distToCenterSqr(this.position()) > (double) range * range;
    }

    protected void plantTick() {
        if (!this.level().isClientSide()) {
            if (this.shouldWilt() && this.weakTime <= 0) {
                this.weakTime = PLANT_WEAK_CD;
                this.hurt(PvZEntityDamageSource.plantWilt(), EntityUtil.getMaxHealthDamage(this, 0.35F));
            }
            this.weakTime = Math.max(0, this.weakTime - 1);
            this.setSuperTime(Math.max(0, this.getSuperTime() - 1));
            if (this.getSuperTime() <= 0 && this.hasState(PAZState.SUPER)) {
                this.setState(PAZState.SUPER, false);
            }
            this.setBoostTime(Math.max(0, this.getBoostTime() - 1));
            this.tickSleep();
        }

        if (this.level().isClientSide() && this.isPlantSleeping() && this.tickCount % 20 == 0) {
            EntityUtil.spawnSpeedParticle(this, PvZParticleRegister.SLEEP.get(), 0.05F);
        }

        if (this.shouldLockXZ() && this.getVehicle() == null) {
            BlockPos pos = this.blockPosition();
            this.setPos(pos.getX() + 0.5, this.getY(), pos.getZ() + 0.5);
        }

        if (!this.level().isClientSide() && this.getPlantType().hasTrait(PlantTrait.WATER) && this.isInWater()) {
            Vec3 vec = this.getDeltaMovement();
            double speedY = Math.min(vec.y, 0.05D);
            this.setDeltaMovement(vec.x, speedY, vec.z);
        }
    }

    private void tickSleep() {
        if (!(this instanceof ICanSleep sleep)) {
            return;
        }
        if (sleep.shouldSleepNow()) {
            this.sleepTime = this.sleepTime <= 1 ? this.sleepTime + 1 : this.sleepTime - 1;
        } else {
            this.sleepTime = this.sleepTime <= -1 ? this.sleepTime + 1 : this.sleepTime - 1;
        }
        if (!this.isPlantSleeping() && this.sleepTime > 0) {
            this.setState(PAZState.SLEEPING, true);
        }
        if (this.isPlantSleeping() && this.sleepTime <= 0) {
            this.setState(PAZState.SLEEPING, false);
        }
    }

    protected void normalPlantTick() {
        if (!this.level().isClientSide() && this.getGoldTime() < GoldLeafEntity.GOLD_GEN_CD) {
            Block block = this.level().getBlockState(this.blockPosition().below()).getBlock();
            int lvl = GoldLeafEntity.getBlockGoldLevel(block);
            if (lvl <= 0) {
                this.setGoldTime(0);
                return;
            }
            this.setGoldTime(this.getGoldTime() + 1);
            if (this.getGoldTime() >= GoldLeafEntity.GOLD_GEN_CD) {
                this.setGoldTime(0);
                SunEntity sun = PvZEntityRegister.SUN.get().create(level(), EntitySpawnReason.MOB_SUMMONED);
                if (sun == null) {
                    return;
                }
                sun.setAmount(GoldLeafEntity.getGoldGenAmount(lvl));
                EntityUtil.onEntityRandomPosSpawn(level(), sun, blockPosition(), 2);
                EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
            }
        }
    }

    @Override
    public boolean shouldWilt() {
        if (this.isImmuneToWeak() || this.getVehicle() != null) {
            return false;
        }
        if (this.getPlantType().hasTrait(PlantTrait.WATER)) {
            if (this.isInWater()) {
                return false;
            }
            // 水生植物：所在格或脚下格（含半格高水盆边界、浮在水面的情形）任一有
            // 水体支撑就不枯萎。单独取 getSupportBlockPos() 会因实体中心横跨两格
            // 而漏判脚下（below）的水，导致浮在水面的猫尾草等被误判枯萎掉血。
            final BlockPos at = this.blockPosition();
            return !isWaterSupportBlock(level(), at)
                    && !isWaterSupportBlock(level(), at.below());
        }
        if (this.isInWater()) {
            return true;
        }
        final BlockPos pos = this.getSupportBlockPos();
        return !this.getPlantType().getPlacement().canPlaceOnBlockState(level().getBlockState(pos));
    }

    /**
     * 植物脚下的支撑方块位置。
     *
     * <p>与实体中心 y 和 {@link #blockPosition()} y 的差值判断：若几乎一致
     * （站在方块顶部整格），支撑为 {@code blockPosition().below()}；否则
     * （中心横跨两格，如半格高水盆上），支撑为 {@code blockPosition()}。
     */
    private BlockPos getSupportBlockPos() {
        return Math.abs(this.getY() - this.blockPosition().getY()) <= 0.01D
                ? this.blockPosition().below() : this.blockPosition();
    }

    /**
     * 该位置是否能为水生植物提供水体支撑：水方块、水盆或睡莲。
     */
    private static boolean isWaterSupportBlock(Level level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER)
                || level.getBlockState(pos).is(PvZBlockRegister.WATER_POT)
                || level.getBlockState(pos).is(PvZBlockRegister.LILY_PAD);
    }

    public void setImmuneToWeak(boolean value) {
        this.isImmuneToWeak = value;
    }

    public boolean isImmuneToWeak() {
        return this.isImmuneToWeak || this.getPlantType().hasTrait(PlantTrait.IMMUNE_TO_WILT);
    }

    @Override
    public boolean shouldSleepNow() {
        return this.getPlantType().hasTrait(PlantTrait.SHROOM) && this.level().isBrightOutside();
    }

    @Override
    public boolean isSleeping() {
        return this.isPlantSleeping();
    }

    /** 睡觉的植物不工作（参考磁力菇）：白天蘑菇不攻击 / 不吸附。 */
    @Override
    public boolean canNormalUpdate() {
        return super.canNormalUpdate() && !this.isPlantSleeping();
    }

    @Override
    public int getSuperTimeLength() {
        return 0;
    }

    @Override
    public boolean canStartSuperMode() {
        return ICanSuperMode.super.canStartSuperMode()
                && this.canNormalUpdate()
                && !this.isPlantInSuperMode();
    }

    @Override
    public void onSuperStart(boolean first) {
        if (first) {
            Player player = EntityUtil.getEntityOwner(level(), this);
            if (player instanceof ServerPlayer sp) {
                PvZTriggers.PLANT_SUPER.get().trigger(sp, this);
            }
        }
        this.setSuperTime(this.getSuperTimeLength());
        this.heal(this.getMaxHealth());
        this.setState(PAZState.SUPER, true);
    }

    /**
     * 进入超级模式的统一入口（由 {@code makeSuperMode} 调用）。
     *
     * <p>外层优先语义：若本植物带有外层植物（南瓜壳），则<b>只强化外层</b>
     * （外层 {@code NORMAL} 生命 → {@code SUPER} 生命），内层植物不进入超级模式、
     * 不设置 {@link PAZState#SUPER}、不触发 super layer；仅当没有外层时才进入
     * 内层超级模式。
     */
    public void makeSuper() {
        final Optional<IPlantInfo> outer = this.getOuterPlantInfo();
        // 有外层植物且外层尚未超级化 → 只强化外层（NORMAL → SUPER 生命），内层不进入超级。
        // 外层已超级化（或没有外层）→ 才让内层植物进入超级模式。
        if (outer.isPresent() && !outer.get().isSuper()) {
            outer.get().onSuper(this);
        } else {
            this.startSuperMode(true);
        }
    }

    public void startSuperMode(boolean first) {
        this.onSuperStart(first);
    }

    public boolean isPlantInSuperMode() {
        return this.hasState(PAZState.SUPER);
    }

    public boolean canStartSuperModeLegacy() {
        return this.canStartSuperMode();
    }

    public int getSuperTime() {
        return this.entityData.get(SUPER_TIME);
    }

    public void setSuperTime(int time) {
        this.entityData.set(SUPER_TIME, time);
    }

    public int getBoostTime() {
        return this.entityData.get(BOOST_TIME);
    }

    public void setBoostTime(int time) {
        this.entityData.set(BOOST_TIME, time);
    }

    public boolean isPlantInBoost() {
        return this.getBoostTime() > 0;
    }

    @Override
    public boolean canPlaceOuter() {
        return this.getOuterPlantInfo().isEmpty();
    }

    @Override
    public void placeOuter(IPlantType type, int sunCost) {
        if (!type.hasTrait(PlantTrait.OUTER)) {
            PvZMod.LOGGER.error("Place Outer Plant Error : it's not outer plant type !");
            return;
        }
        final IPlantInfo info;
        try {
            info = type.getOuterPlantInfo().get();
        } catch (IllegalStateException ex) {
            PvZMod.LOGGER.error("Place Outer Plant Error : outer plant info is empty for type {}!",
                    type.getID(), ex);
            return;
        }
        if (info == null) {
            PvZMod.LOGGER.error("Place Outer Plant Error : outer plant supplier returned null for type {}!",
                    type.getID());
            return;
        }
        this.outerPlant = info;
        this.outerPlant.setType(type);
        this.outerPlant.placeOn(this, sunCost);
        this.setState(PAZState.PUMPKIN, true);
    }

    @Override
    public void removeOuter() {
        if (this.isRemoved()) {
            return;
        }
        this.outerPlant = null;
        this.setState(PAZState.PUMPKIN, false);
        if (this.hasMetal()) {
            this.decreaseMetal();
        }
    }

    public void onPlaceOuterPlant(IPlantType type, int sunCost) {
        this.placeOuter(type, sunCost);
    }

    public void removeOuterPlant() {
        this.removeOuter();
    }

    @Override
    public void onOuterDefenceBroken() {
        super.onOuterDefenceBroken();
        if (this.isRemoved()) {
            return;
        }
        this.removeOuter();
    }

    @Override
    public void onUpgrade(IPlantEntity newPlant) {
        if (!(newPlant instanceof PlantEntity plant) || plant.isRemoved()) {
            return;
        }
        plant.mergeUpgradeDataFrom(this);
        this.discard();
    }

    private void mergeUpgradeDataFrom(PlantEntity oldPlant) {
        SkillLevels merged = mergeSkills(
                this.getPlantType().getSkills(),
                oldPlant.getSkillLevels(),
                this.getSkillLevels());
        this.applySkillLevels(merged);

        this.cardEnchantments = mergeEnchantments(
                oldPlant.getCardEnchantments(), this.cardEnchantments);

        if (oldPlant.getOuterPlantInfo().isPresent()) {
            final IPlantInfo outer = oldPlant.outerPlant;
            final int outerSunCost = outer.getSunCost();
            final double outerDefence = oldPlant.getOuterDefenceLife();
            this.outerPlant = outer;
            // 重新绑定到新宿主：外层 info 的 placeOn 会重建宿主侧初始化
            // （如 PumpkinInfo 设置外层防御基准），随后再恢复实际剩余防御生命，
            // 避免升级时外层植物退化 / 防御值被 initAttributes 重置为 0。
            outer.placeOn(this, outerSunCost);
            this.setOuterDefenceLife(outerDefence);
            this.setState(PAZState.PUMPKIN, oldPlant.hasState(PAZState.PUMPKIN));
        }
        this.sleepTime = oldPlant.sleepTime;
    }

    private static SkillLevels mergeSkills(Set<ISkillType> allowed,
                                           SkillLevels a, SkillLevels b) {
        final boolean allowAll = allowed == null || allowed.isEmpty();
        final Set<Identifier> ids = new HashSet<>();
        ids.addAll(a.levels().keySet());
        ids.addAll(b.levels().keySet());

        final Map<Identifier, Integer> result = new HashMap<>();
        for (Identifier id : ids) {
            final ISkillType skill = PVZAPI.lookup().skill(id).orElse(null);
            if (skill == null) {
                continue;
            }
            if (!allowAll && !allowed.contains(skill)) {
                continue;
            }
            final int lvl = Math.max(a.getLevel(skill), b.getLevel(skill));
            if (lvl > 0) {
                result.put(id, lvl);
            }
        }
        return result.isEmpty() ? SkillLevels.EMPTY : new SkillLevels(result);
    }

    private static ItemEnchantments mergeEnchantments(ItemEnchantments a, ItemEnchantments b) {
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        final ItemEnchantments.Mutable result = new ItemEnchantments.Mutable(a);
        for (var entry : b.entrySet()) {
            final Holder<Enchantment> holder = entry.getKey();
            final int level = entry.getIntValue();
            if (level > result.getLevel(holder)) {
                result.set(holder, level);
            }
        }
        return result.toImmutable();
    }

    public void onPlantUpgrade(PlantEntity newPlant) {
        this.onUpgrade(newPlant);
    }

    @Override
    public boolean canBeUpgraded() {
        return ICanBeUpgraded.super.canBeUpgraded();
    }

    @Override
    public boolean canBeHitBy(Entity source) {
        return !this.hasMetal() && super.canBeHitBy(source);
    }

    @Override
    public boolean canBeTargetBy(LivingEntity living) {
        return !this.hasMetal();
    }

    @Override
    public boolean canBeAttractedBy(ICanAttract defender) {
        return true;
    }

    @Override
    public void attractBy(ICanAttract defender) {
        if (defender instanceof LivingEntity living) {
            this.setTarget(living);
        }
    }

    @Override
    public boolean canOuterDefend(DamageSource source) {
        if (source instanceof PvZEntityDamageSource pds && pds.isParabola()) {
            return false;
        }
        return super.canOuterDefend(source);
    }

    @Override
    public GroupType getEntityGroupType() {
        return this.isCharmed() ? GroupType.ZOMBIES : GroupType.PLANTS;
    }

    @Override
    public boolean canBeCollidedBy(Entity other) {
        return true;
    }

    @Override
    public void push(Entity entityIn) {
        super.push(entityIn);
        if (this.isPassengerOfSameVehicle(entityIn) || entityIn.noPhysics || this.noPhysics) {
            return;
        }
        double dx = entityIn.getX() - this.getX();
        double dz = entityIn.getZ() - this.getZ();
        if (Mth.absMax(dx, dz) < 0.01
                && entityIn instanceof PlantEntity
                && !EntityUtil.canTargetEntity(this, entityIn)) {
            if (this.tickCount >= entityIn.tickCount) {
                if (entityIn.level() instanceof ServerLevel serverLevel) {
                    this.hurtServer(serverLevel, PvZEntityDamageSource.plantWilt(),
                            EntityUtil.getMaxHealthDamage(this, 0.5F));
                }
            }
        }
    }

    @Override
    protected void pushEntities() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        List<LivingEntity> list = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
        if (list.isEmpty()) {
            return;
        }
        int i = serverLevel.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING);
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
            if (!this.is(target) && shouldCollideWithEntity(target)) {
                this.doPush(target);
            }
        }
    }

    protected boolean shouldCollideWithEntity(LivingEntity target) {
        if (target instanceof PlantEntity other) {
            if (!this.canCollideWithPlant || !other.canCollideWithPlant) {
                return false;
            }
            final PlantCombatStyle style = other.getPlantType().getCombatStyle();
            if (style == PlantCombatStyle.MELEE_DEFENSE && !other.canBeCollidedBy(this)) {
                return !EntityUtil.canTargetEntity(this, target);
            }
            return true;
        }
        if (target instanceof Mob mob) {
            if (mob.getTarget() == this) {
                return true;
            }
            if (target.is(PvZEntityTypeTags.TOMB_STONE)) {
                return true;
            }
        }
        return false;
    }

    protected boolean shouldLockXZ() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
        return this.isPlantImmuneTo(source)
                && !source.is(DamageTypes.FELL_OUT_OF_WORLD)
                && !source.isCreativePlayer()
                && !source.equals(PvZEntityDamageSource.plantWilt());
    }

    public boolean isPlantImmuneTo(DamageSource source) {
        return this.isPlantInSuperMode();
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        target.setInvulnerableTime(0);
        return super.doHurtTarget(level, target);
    }

    public boolean onHealBy(IPlantType plantType, float percent) {
        boolean healed;
        if (plantType.hasTrait(PlantTrait.OUTER)) {
            final float before = (float) this.getOuterDefenceLife();
            this.getOuterPlantInfo().ifPresent(l -> l.onHeal(this, percent));
            healed = this.getOuterDefenceLife() > before;
        } else {
            if (this.getHealth() >= this.getMaxHealth()) {
                healed = false;
            } else {
                this.heal(this.getLife() * percent);
                healed = true;
            }
        }
        if (healed) {
            this.addEffect(EffectUtil.viewEffect(MobEffects.REGENERATION, 60, 0));
            this.getSpawnSound().ifPresent(s -> EntityUtil.playSound(this, s));
        }
        return healed;
    }

    public void onPlantUpgrade(PlantEntity oldPlant, PlantEntity newPlant) {
        oldPlant.onUpgrade(newPlant);
    }

    public boolean canBeUpgrade(Player player) {
        return !this.getPlantType().getUpgradeTo().isEmpty();
    }


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof AbstractCardItem card)) return InteractionResult.PASS;
        final CardContent content = card.content(stack);
        if (content.isEmpty()) return InteractionResult.PASS;

        if (player.level().isClientSide()) {
            return InteractionResult.CONSUME;
        }

        if (!CardPlacementService.checkCommon(player, stack, content)) {
            return InteractionResult.PASS;
        }
        return card.tryInteractWithPlant(player, this, stack, content)
                ? InteractionResult.SUCCESS_SERVER
                : InteractionResult.PASS;
    }
    @Override
    protected float getLife() {
        return this.getSkillValue(SkillTypes.PLANT_MORE_LIFE.get());
    }

    @Override
    protected float getInnerLife() {
        return 0;
    }

    @Override
    protected float getOuterLife() {
        return 0;
    }

    @Override
    public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
        super.addAlmanacEntries(list);
        list.addAll(Arrays.asList(
                Pair.of(PAZAlmanacs.HEALTH, this.getSkillValue(SkillTypes.PLANT_MORE_LIFE.get()))
        ));
    }

    @Override
    public void onCharmedBy(@Nullable LivingEntity entity) {
        if (this.canBeCharmed()) {
            this.setState(PAZState.CHARMED, !this.isCharmed());
        }
    }

    public boolean hasMetal() {
        return this.hasState(PAZState.METAL);
    }

    public void setMetal(boolean flag) {
        this.setState(PAZState.METAL, flag);
    }

    @Override
    public void increaseMetal() {
        this.setMetal(true);
    }

    @Override
    public void decreaseMetal() {
        this.setMetal(false);
    }

    @Override
    public MetalTypes getMetalType() {
        return MetalTypes.LADDER;
    }

    @Override
    public boolean isCharmed() {
        return this.hasState(PAZState.CHARMED);
    }

    public void setCharmed(boolean flag) {
        this.setState(PAZState.CHARMED, flag);
    }

    public boolean isPlantSleeping() {
        return this.hasState(PAZState.SLEEPING);
    }

    public void setPlantSleeping(boolean flag) {
        this.setState(PAZState.SLEEPING, flag);
    }

    public boolean hasPumpkin() {
        return this.hasState(PAZState.PUMPKIN);
    }

    @Override
    public void setPumpkin(boolean flag) {
        this.setState(PAZState.PUMPKIN, flag);
    }

    public int getAttackTime() {
        return this.entityData.get(ATTACK_TIME);
    }

    public void setAttackTime(int cd) {
        this.entityData.set(ATTACK_TIME, cd);
    }

    public int getGoldTime() {
        return this.entityData.get(GOLD_TIME);
    }

    public void setGoldTime(int cd) {
        this.entityData.set(GOLD_TIME, cd);
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

    public static void clearLadders(LivingEntity entity, AABB aabb) {
        entity.level().getEntitiesOfClass(PlantEntity.class, aabb, target ->
                target.hasMetal() && !EntityUtil.checkCanEntityBeAttack(entity, target)
        ).forEach(plant -> plant.setMetal(false));
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        if (this.getPlantType().hasTrait(PlantTrait.WATER)) {
            return false;
        }
        return super.canDrownInFluidType(type);
    }

    public boolean canHelpAttack() {
        return this.canHelpAttack;
    }

    public Optional<Player> getOwnerPlayer() {
        if (!this.hasOwner()) {
            return Optional.empty();
        }
        if (!EntityUtil.isEntityValid(this.ownerPlayer)) {
            this.ownerPlayer = this.level().getPlayerByUUID(this.getOwnerUUID().get());
        }
        return Optional.ofNullable(this.ownerPlayer);
    }

    public boolean hasOwner() {
        return this.getOwnerUUID().isPresent();
    }

    @Override
    public Optional<SoundEvent> getSpawnSound() {
        return Optional.ofNullable(this.getPlantType().hasTrait(PlantTrait.WATER)
                ? PvZSoundRegister.PLACE_PLANT_WATER.get() : PvZSoundRegister.PLACE_PLANT_GROUND.get());
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("plant_super_time", this.getSuperTime());
        output.putInt("plant_attack_time", this.getAttackTime());
        output.putInt("plant_gold_time", this.getGoldTime());
        output.putInt("plant_boost_time", this.getBoostTime());
        output.putInt("plant_sleep_time", this.sleepTime);
        output.putBoolean("immune_to_weak", this.isImmuneToWeak);
        output.store("card_enchantments", ItemEnchantments.CODEC, this.cardEnchantments);
        if (this.innerPlant != null) {
            final CompoundTag inner = new CompoundTag();
            this.innerPlant.write(inner);
            output.store("inner_plant_info", CompoundTag.CODEC, inner);
        }
        if (this.outerPlant != null) {
            final CompoundTag outer = new CompoundTag();
            this.outerPlant.write(outer);
            output.store("outer_plant_info", CompoundTag.CODEC, outer);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSuperTime(input.getIntOr("plant_super_time", 0));
        this.setAttackTime(input.getIntOr("plant_attack_time", 0));
        this.setGoldTime(input.getIntOr("plant_gold_time", 0));
        this.setBoostTime(input.getIntOr("plant_boost_time", 0));
        this.sleepTime = input.getIntOr("plant_sleep_time", 0);
        this.isImmuneToWeak = input.getBooleanOr("immune_to_weak", false);
        this.cardEnchantments = input.read("card_enchantments", ItemEnchantments.CODEC)
                .orElse(ItemEnchantments.EMPTY);

        input.read("inner_plant_info", CompoundTag.CODEC).ifPresent(tag -> {
            this.innerPlant = PlantInfo.read(this.innerPlant, wrap(tag, "inner_plant_info"), "inner_plant_info");
        });
        input.read("outer_plant_info", CompoundTag.CODEC).ifPresent(tag -> {
            this.outerPlant = PlantInfo.read(this.outerPlant, wrap(tag, "outer_plant_info"), "outer_plant_info");
        });
    }

    private static CompoundTag wrap(CompoundTag tag, String key) {
        final CompoundTag wrapper = new CompoundTag();
        wrapper.put(key, tag);
        return wrapper;
    }

    public Optional<IPlantInfo> getOuterPlantInfo() {
        return Optional.ofNullable(this.outerPlant);
    }

    public Optional<IPlantInfo> getPlantInfo() {
        return Optional.ofNullable(this.innerPlant);
    }

    public IEssenceType getPlantEssenceType() {
        return this.getPlantType().getEssence();
    }

    @Override
    public IPAZType getPAZType() {
        return this.getPlantType();
    }

    public abstract IPlantType getPlantType();
}