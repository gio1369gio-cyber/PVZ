package com.lying.pvz.common.level.entity;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.api.entity.PAZState;
import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.init.PVZEntityDataSerializers;
import com.lying.pvz.common.init.PvZAttributes;
import com.lying.pvz.common.level.entity.bowling.AbstractBowlingProjectile;
import com.lying.pvz.common.level.entity.zombie.roof.BungeeZombieEntity;
import com.lying.pvz.common.projectile.mark.MarkComponent;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.lying.pvz.utils.others.WeightList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * 植物与僵尸的共同实现基类。
 *
 * <p><b>技能</b>：{@link SkillLevels} 直接存于 {@link #SKILLS} synched data，
 * 经 {@link PVZEntityDataSerializers#SKILL_LEVELS} 编码，API 层永远读 {@link SkillLevels}。
 *
 * <p><b>状态</b>：位掩码存在 {@link #STATES}，但语义全部由 {@link PAZState} 统一分配。
 * 子类不再定义 bit 常量，统一走 {@link #hasState(PAZState)} / {@link #setState(PAZState, boolean)}。
 * 这解决了旧版“植物 bit0 = 梯子、僵尸 bit0 = 魅惑”的冲突。
 *
 * <p><b>可命中性</b>：{@link #canBeHitBy(Entity)} 默认返回 {@code true}。
 * 植物覆写“有外层 / 有梯子时不可命中”，僵尸覆写“升起时不可命中”。
 * 弹药侧两步判定：先比 {@code ownerGroup}，再问本方法。
 *
 * <p><b>护甲</b>：{@link #getArmor()} / {@link #getArmorToughness()} 返回 {@code float}，
 * 与 {@code getSkillValue(...)} 的浮点语义一致，不再静默截断。
 */
public abstract class AbstractPAZEntity extends PathfinderMob implements IPAZEntity, Mark.Holder {

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(AbstractPAZEntity.class,
                    PVZEntityDataSerializers.OPTIONAL_UUID.get());
    private static final EntityDataAccessor<SkillLevels> SKILLS =
            SynchedEntityData.defineId(AbstractPAZEntity.class,
                    PVZEntityDataSerializers.SKILL_LEVELS.get());
    private static final EntityDataAccessor<Integer> STATES =
            SynchedEntityData.defineId(AbstractPAZEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EXIST_TICK =
            SynchedEntityData.defineId(AbstractPAZEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<List<Mark.View>> MARK_VIEWS =
            SynchedEntityData.defineId(AbstractPAZEntity.class,
                    PVZEntityDataSerializers.MARK_VIEWS.get());

    protected static volatile WeightList<DropType> NORMAL_DROP_LIST = new WeightList<>();

    private static volatile boolean normalDropInitialized = false;

    protected Player ownerPlayer;
    /** 标记组件：层数按 (mark id, 来源 UUID) 分开，按 duration 到期。 */
    private final MarkComponent markComponent = new MarkComponent(this);
    /* states */
    protected boolean canBeCold = true;
    protected boolean canBeFrozen = true;
    protected boolean canBeCharm = true;
    protected boolean canBeButtered = true;
    protected boolean canBeMini = true;
    protected boolean canBeInvisible = true;
    protected boolean canBeRemove = true;
    protected boolean canDespawn = true;
    protected boolean canBeStealByBungee = true;
    /* misc */
    protected boolean canSpawnDrop = true;
    protected boolean canHelpAttack = true;
    public boolean canGiveXP = false;

    static synchronized void ensureNormalDropListInitialized() {
        if (normalDropInitialized) {
            return;
        }
        final int p = PvZConfig.LOCAL_CONFIG.EntitySettings.DropChanceMultiper.get();
        final int pp = p * p;
        final WeightList<DropType> list = new WeightList<>();
        list.addItem(DropType.COPPER, pp * p);
        list.addItem(DropType.SILVER, pp);
        list.addItem(DropType.GOLD, p);
        list.addItem(DropType.JEWEL, 1);
        list.addItem(DropType.CHOCOLATE, 1);
        list.setTotal(pp * pp);
        NORMAL_DROP_LIST = list;
        normalDropInitialized = true;
    }

    static synchronized void resetAndEnsureNormalDropListInitialized() {
        normalDropInitialized = false;
        ensureNormalDropListInitialized();
    }

    public static synchronized void refreshDropTable() {
        resetAndEnsureNormalDropListInitialized();
    }

    public AbstractPAZEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(SKILLS, SkillLevels.EMPTY);
        builder.define(EXIST_TICK, 0);
        builder.define(STATES, 0);
        MarkComponent.define(builder);    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        this.finalizeSpawn(null);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    public static AttributeSupplier.Builder createPAZAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.FLYING_SPEED)
                .add(PvZAttributes.INNER_DEFENCE_HP)
                .add(PvZAttributes.OUTER_DEFENCE_HP);
    }

    /**
     * 随机分配技能等级。
     *
     * <p>从 {@code getPAZType().getSkills()} 中随机挑选，按点数约束逐级升。
     * 最终以 {@link SkillLevels} 形式写入。
     */
    public static void randomInitSkills(AbstractPAZEntity pazEntity, int maxLevel) {
        final Set<ISkillType> skillSet = pazEntity.getPAZType().getSkills();
        if (skillSet.isEmpty()) {
            pazEntity.setSkillsRaw(SkillLevels.EMPTY);
            pazEntity.initAttributes();
            return;
        }
        final List<ISkillType> skills = new ArrayList<>(skillSet);
        final Map<Identifier, Integer> skillMap = new HashMap<>();
        int point = MathUtil.getRandomMinMax(pazEntity.getRandom(), maxLevel / 2, maxLevel);
        for (int i = 0; i < 10; ++i) {
            final ISkillType skillType = skills.get(pazEntity.getRandom().nextInt(skills.size()));
            int lvl = skillMap.getOrDefault(skillType.getID(), 0);
            while (lvl < skillType.getMaxLevel()
                    && skillType.getCostAt(lvl) <= point
                    && pazEntity.getRandom().nextFloat() < 0.8F) {
                point -= skillType.getCostAt(lvl);
                ++lvl;
                skillMap.put(skillType.getID(), lvl);
            }
        }
        pazEntity.setSkillsRaw(new SkillLevels(skillMap));
        pazEntity.initAttributes();
    }

    public void finalizeSpawn(@Nullable CompoundTag tag) {
        if (!this.level().isClientSide()) {
            if (tag != null && tag.contains(SkillTypes.SKILL_TAG)) {
                this.setSkillsRaw(tag.read(SkillTypes.SKILL_TAG, SkillLevels.CODEC)
                        .orElse(SkillLevels.EMPTY));
            }
            this.getSpawnSound().ifPresent(s -> EntityUtil.playSound(this, s));
            this.initAttributes();
            this.updatePAZStates();
        }
    }

    public void onSpawnedByPlayer(@Nullable Player player, int sunCost) {
        if (player != null) {
            this.setOwnerUUID(player.getUUID());
        }
        this.heal(this.getMaxHealth());
    }

    @Override
    public void checkDespawn() {
        if (!this.canDespawn) {
            return;
        }
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            if (EntityGroupHander.isMonsterGroup(this.getEntityGroupType())) {
                this.discard();
            }
            return;
        }
        super.checkDespawn();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Profiler.get().push("PAZ Tick");
        this.pazTick();
        Profiler.get().pop();
    }

    public void pazTick() {
        if (!this.level().isClientSide()) {
            this.setExistTick(this.getExistTick() + 1);
            this.markComponent.tick();
        }
    }

    /* ================================================================== */
    /* 标记                                                                */
    /* ================================================================== */

    @Override
    public int marks(Mark.Key key) {
        return this.markComponent.marks(key);
    }

    @Override
    public int marks(Identifier mark) {
        return this.markComponent.marks(mark);
    }

    @Override
    public int addMark(Mark.Key key, int amount) {
        return this.markComponent.addMark(key, amount);
    }

    @Override
    public int removeMark(Mark.Key key, int amount) {
        return this.markComponent.removeMark(key, amount);
    }

    @Override
    public boolean hasMark(Mark.Key key) {
        return this.markComponent.hasMark(key);
    }

    @Override
    public List<Mark.View> views() {
        return this.markComponent.views();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (dataParameter.equals(STATES)) {
            this.updatePAZStates();
        }
    }

    @Override
    public void onCharmedBy(@Nullable LivingEntity entity) {
        if (this.canBeCharmed()) {
            final Player player = EntityUtil.getEntityOwner(this.level(), entity);
            if (player instanceof ServerPlayer serverPlayer) {
                //PvZTriggers.CHARM_ZOMBIE.get().trigger(serverPlayer, this);
            }
        }
    }

    /* ================================================================== */
    /* 防御伤害管线                                                        */
    /* ================================================================== */

    public static void damageOuterDefence(final LivingDamageEvent.Pre ev) {
        float amount = ev.getNewDamage();
        if (amount <= 0) {
            return;
        }
        if (ev.getEntity() instanceof AbstractPAZEntity pazEntity && pazEntity.canOuterDefend(ev.getSource())) {
            final double life = pazEntity.getOuterDefenceLife();
            if (life > 0) {
                if (life > amount) {
                    pazEntity.setOuterDefenceLife(life - amount);
                    amount = 0;
                    pazEntity.onOuterDefenceHurt();
                } else {
                    amount = (float) (amount - life);
                    pazEntity.setOuterDefenceLife(0);
                    pazEntity.onOuterDefenceBroken();
                }
            }
        }
        ev.setNewDamage(amount);
    }

    public static void damageInnerDefence(final LivingDamageEvent.Pre ev) {
        float amount = ev.getNewDamage();
        if (amount <= 0) {
            return;
        }
        if (ev.getEntity() instanceof AbstractPAZEntity pazEntity) {
            final double life = pazEntity.getInnerDefenceLife();
            if (life > 0) {
                if (life > amount) {
                    pazEntity.setInnerDefenceLife(life - amount);
                    amount = 0;
                    pazEntity.onInnerDefenceHurt();
                } else {
                    amount -= life;
                    pazEntity.setInnerDefenceLife(0);
                    pazEntity.onInnerDefenceBroken();
                }
            }
        }
        ev.setNewDamage(amount);
    }

    public void onOuterDefenceHurt() {
    }

    public void onOuterDefenceBroken() {
    }

    public void onInnerDefenceHurt() {
    }

    public void onInnerDefenceBroken() {
    }

    public boolean canOuterDefend(DamageSource source) {
        return true;
    }

    /* ================================================================== */
    /* 目标 / 可命中性                                                     */
    /* ================================================================== */

    public boolean checkCanPAZTarget(Entity target) {
        return EntityUtil.checkCanEntityBeTarget(this, target) && this.canPAZTarget(target);
    }

    public boolean checkCanPAZAttack(Entity target) {
        return EntityUtil.checkCanEntityBeAttack(this, target) && this.canPAZTarget(target);
    }

    public boolean canBeTargetBy(LivingEntity living) {
        return true;
    }

    public boolean canPAZTarget(Entity target) {
        if (target instanceof AbstractPAZEntity pazEntity) {
            return pazEntity.canBeTargetBy(this);
        }
        return true;
    }

    /**
     * 弹药可命中性。默认 {@code true}；植物覆写“外层 / 梯子不可命中”，
     * 僵尸覆写“升起不可命中”。
     *
     * <p>弹药侧 {@code shouldHit} 先比 {@code ownerGroup}，再调本方法。
     */
    public boolean canBeHitBy(Entity source) {
        return true;
    }

    /* ================================================================== */
    /* 受击 / 死亡                                                         */
    /* ================================================================== */

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source instanceof PvZEntityDamageSource) {
            this.setInvulnerableTime(0);
        }
        if (!this.level().isClientSide()
                && source.getEntity() instanceof LivingEntity living
                && EntityUtil.checkCanEntityBeAttack(this, living)) {
            if (EntityUtil.isEntityValid(this.getTarget()) && this.getRandom().nextFloat() < 0.4F) {
                if (this.distanceTo(living) < this.distanceTo(this.getTarget())) {
                    this.setTarget(living);
                }
            }
        }
        return super.hurtServer(level, source, damage);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (source instanceof PvZEntityDamageSource pds
                && pds.getDirectEntity() instanceof AbstractBowlingProjectile
                && pds.getDamageCount() > 0) {
            this.canSpawnDrop = false;
        }
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.canRemoveWhenDeath()) {
            for (int i = 0; i < 5; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(),
                        this.getRandomZ(1.0D), d0, d1, d2);
            }
            this.onRemoveWhenDeath();
            this.discard();
        }
    }

    protected void onRemoveWhenDeath() {
    }

    protected boolean canRemoveWhenDeath() {
        return this.deathTime >= this.getDeathTime();
    }

    protected int getDeathTime() {
        return 20;
    }

    /* ================================================================== */
    /* 属性刷新                                                            */
    /* ================================================================== */

    protected void initAttributes() {
        this.refreshBaseAttributes();
        AttributeInstance innerDefence = this.getAttribute(PvZAttributes.INNER_DEFENCE_HP);
        if (innerDefence != null) {
            innerDefence.setBaseValue(this.getInnerLife());
        }
        AttributeInstance outerDefence = this.getAttribute(PvZAttributes.OUTER_DEFENCE_HP);
        if (outerDefence != null) {
            outerDefence.setBaseValue(this.getOuterLife());
        }
        this.heal(this.getMaxHealth());
    }

    protected void refreshBaseAttributes() {
        AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(this.getLife());
        }
        AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.setBaseValue(this.getArmor());
        }
        AttributeInstance armorToughness = this.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (armorToughness != null) {
            armorToughness.setBaseValue(this.getArmorToughness());
        }
    }

    protected void refreshAttributesForVersionUpgrade() {
        final float oldMaxHealth = this.getMaxHealth();
        final boolean wasFullHealth = oldMaxHealth > 0 && this.getHealth() >= oldMaxHealth - 0.01F;
        this.refreshBaseAttributes();
        if (wasFullHealth) {
            this.setHealth(this.getMaxHealth());
        }
    }

    protected void updatePAZStates() {
    }

    protected abstract float getLife();

    protected float getInnerLife() {
        return 0;
    }

    protected float getOuterLife() {
        return 0;
    }

    public float getArmor() {
        return 0;
    }

    public float getArmorToughness() {
        return 0;
    }

    @Override
    public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
        list.addAll(Arrays.asList(
                Pair.of(PAZAlmanacs.SUN_COST, this.getPAZType().getSunCost()),
                Pair.of(PAZAlmanacs.COOL_DOWN, this.getPAZType().getCoolDown().getCD(0))
        ));
    }

    /* ================================================================== */
    /* 技能                                                                */
    /* ================================================================== */

    @Override
    public SkillLevels getSkillLevels() {
        return this.entityData.get(SKILLS);
    }

    @Override
    public void applySkillLevels(SkillLevels skills) {
        this.setSkillsRaw(skills);
        this.initAttributes();
    }

    protected void setSkillsRaw(SkillLevels skills) {
        this.entityData.set(SKILLS, skills == null ? SkillLevels.EMPTY : skills);
    }

    public float getSkillValue(ISkillType type) {
        return type.getValueAt(this.getSkillLevels().getLevel(type));
    }

    /* ================================================================== */
    /* 状态                                                                */
    /* ================================================================== */

    /** 读取 {@code state} 是否置位。 */
    public boolean hasState(PAZState state) {
        return state.isSet(this.getPAZState());
    }

    /** 设置或清除 {@code state}，写回 {@code STATES}。 */
    public void setState(PAZState state, boolean value) {
        this.setPAZState(state.apply(this.getPAZState(), value));
    }

    public int getPAZState() {
        return this.entityData.get(STATES);
    }

    public void setPAZState(int state) {
        this.entityData.set(STATES, state);
    }

    /* ================================================================== */
    /* 状态查询                                                            */
    /* ================================================================== */

    public boolean canNormalUpdate() {
        return !(this.getVehicle() instanceof BungeeZombieEntity)
                && !EntityUtil.isEntityFrozen(this)
                && !EntityUtil.isEntityButter(this);
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return super.removeWhenFarAway(distance);
    }

    public IRankType getRank() {
        return this.getPAZType().getRank();
    }

    @Override
    public boolean canBeCold() {
        return this.canBeCold;
    }

    @Override
    public boolean canBeButtered() {
        return this.canBeButtered;
    }

    @Override
    public boolean canBeFrozen() {
        return this.canBeFrozen && !this.isInLiquid() && !this.isInLava();
    }

    @Override
    public boolean canBeCharmed() {
        return this.canBeCharm;
    }

    @Override
    public boolean canBeMini() {
        return this.canBeMini;
    }

    @Override
    public boolean canBeInvisible() {
        return this.canBeInvisible;
    }

    @Override
    public boolean canBeStealByBungee() {
        return this.canBeStealByBungee;
    }

    public int getZombieXp() {
        return this.getPAZType().getXpPoint();
    }

    protected WeightList<DropType> getDropSpecialList() {
        ensureNormalDropListInitialized();
        return NORMAL_DROP_LIST;
    }

    public Optional<SoundEvent> getSpawnSound() {
        return Optional.empty();
    }

    /* ================================================================== */
    /* NBT                                                                 */
    /* ================================================================== */

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.getOwnerUUID().ifPresent(uuid -> output.store("OwnerUUID", UUIDUtil.CODEC, uuid));
        output.putInt("paz_states", this.getPAZState());
        output.putBoolean("can_despawn", this.canDespawn);
        output.putInt("paz_exist_tick", this.getExistTick());
        output.putDouble("inner_defence_life", this.getInnerDefenceLife());
        output.putDouble("outer_defence_life", this.getOuterDefenceLife());
        output.store(SkillTypes.SKILL_TAG, SkillLevels.CODEC, this.getSkillLevels());
        this.markComponent.save(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("OwnerUUID", UUIDUtil.CODEC).ifPresent(uuid -> {
            try {
                this.setOwnerUUID(uuid);
            } catch (Throwable ignored) {
            }
        });
        this.setPAZState(input.getIntOr("paz_states", 0));
        this.canDespawn = input.getBooleanOr("can_despawn", false);
        this.setSkillsRaw(input.read(SkillTypes.SKILL_TAG, SkillLevels.CODEC)
                .orElse(SkillLevels.EMPTY));
        this.setExistTick(input.getIntOr("paz_exist_tick", 0));
        this.refreshAttributesForVersionUpgrade();
        this.updatePAZStates();
        this.setInnerDefenceLife(input.getDoubleOr("inner_defence_life", 0));
        this.setOuterDefenceLife(input.getDoubleOr("outer_defence_life", 0));
        this.markComponent.load(input);
    }

    /* ================================================================== */
    /* 生命 / 防御                                                         */
    /* ================================================================== */

    public double getCurrentHealth() {
        return this.getInnerDefenceLife() + this.getHealth();
    }

    public double getCurrentMaxHealth() {
        return this.getInnerLife() + this.getMaxHealth();
    }

    public double getInnerDefenceLife() {
        return this.getAttributeValue(PvZAttributes.INNER_DEFENCE_HP);
    }

    public void setInnerDefenceLife(double life) {
        AttributeInstance instance = this.getAttribute(PvZAttributes.INNER_DEFENCE_HP);
        if (instance != null) {
            instance.setBaseValue(life);
        }
    }

    public double getOuterDefenceLife() {
        return this.getAttributeValue(PvZAttributes.OUTER_DEFENCE_HP);
    }

    public void setOuterDefenceLife(double life) {
        AttributeInstance instance = this.getAttribute(PvZAttributes.OUTER_DEFENCE_HP);
        if (instance != null) {
            instance.setBaseValue(life);
        }
    }

    /* ================================================================== */
    /* Owner / ExistTick                                                   */
    /* ================================================================== */

    @Override
    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public int getExistTick() {
        return this.entityData.get(EXIST_TICK);
    }

    public void setExistTick(int tick) {
        this.entityData.set(EXIST_TICK, tick);
    }

    /* ================================================================== */
    /* 掉落                                                                */
    /* ================================================================== */

    protected enum DropType {
        COPPER, SILVER, GOLD, JEWEL, CHOCOLATE,
    }
}