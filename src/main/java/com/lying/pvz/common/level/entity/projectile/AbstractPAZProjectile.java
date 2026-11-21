package com.lying.pvz.common.level.entity.projectile;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.content.skill.SkillScope;
import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.entity.projectile.IPAZProjectile;
import com.lying.pvz.api.entity.projectile.ProjectileTrajectory;
import com.lying.pvz.common.init.PVZEntityDataSerializers;
import com.lying.pvz.common.level.entity.MultiPartEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.base.DefenceZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * 保龄球弹药基类。
 */
public abstract class AbstractPAZProjectile extends Entity implements IPAZProjectile {

    /** 无限穿透。 */
    public static final int INFINITE_PIERCE = -1;
    /** 默认生命周期。 */
    public static final int DEFAULT_MAX_LIVE_TICK = 100;
    /** 默认弹速。 */
    public static final float DEFAULT_BULLET_SPEED = 1.5F;
    /** 默认重力加速度。 */
    public static final float DEFAULT_GRAVITY = 0.03F;
    /** 默认空中判定 y 差阈值（格）。 */
    public static final double DEFAULT_AIRBORNE_Y_THRESHOLD = 1.5D;

    /**
     * 空中目标 tag。玩家可在 {@code data/pvz/tags/entity_type/airborne.json} 维护。
     * 命中空中判定时与 y 差阈值双判定，任一为真即视为空中。
     */
    public static final TagKey<EntityType<?>> AIRBORNE = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("pvz", "airborne"));

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(AbstractPAZProjectile.class,
                    PVZEntityDataSerializers.OPTIONAL_UUID.get());

    /* ===== 发射快照（shootFrom 时定死） ===== */
    protected GroupType ownerGroup = GroupType.NEUTRALS;
    protected SkillLevels projectileSkills = SkillLevels.EMPTY;
    protected ItemEnchantments cardEnchantments = ItemEnchantments.EMPTY;
    protected int maxLiveTick = DEFAULT_MAX_LIVE_TICK;
    protected float bulletSpeed = DEFAULT_BULLET_SPEED;
    protected float attackDamage = 0F;
    protected double shootAngle = 0D;
    /** 发射者提供的瞬时效果（冰豌豆的寒冷、黄油投手的黄油等）。只在服务端使用，不存 NBT。 */
    protected MobEffectInstance cachedEffect = null;
    /** 发射原点，用于距离衰减与飞出销毁。shootFrom 时记录。 */
    protected @Nullable Vec3 shootOrigin = null;

    /* ===== 飞行状态 ===== */
    /** 剩余可穿透次数。0 = 普通弹；-1 = 无限；N = 可多穿 N 个。 */
    protected int pierceLeft = 0;
    protected final it.unimi.dsi.fastutil.ints.IntOpenHashSet hitEntities =
            new it.unimi.dsi.fastutil.ints.IntOpenHashSet();
    protected Predicate<BlockState> ignoreBlocks = state -> false;
    protected Optional<UUID> targetId = Optional.empty();

    /* ===== 运动参数 ===== */
    /**
     * 有效距离内的空气阻力。默认 1.0（不减速），
     * 与 PvZ 原作子弹匀速飞行的手感一致。
     */
    protected float airSlowDownNear = 1.0F;
    /**
     * 超出 {@link #slowDownStartRatio} 阈值后的快速衰减系数。
     * 默认 0.85，让子弹在接近射程终点时明显减速、视觉上"泄气"。
     */
    protected float airSlowDownFar = 0.85F;
    /**
     * 快速衰减的起始距离比例（相对 {@link #maxFlyDistance}）。
     * 默认 0.7，即射出 70% 射程后开始减速。
     */
    protected float slowDownStartRatio = 0.7F;
    /**
     * 最大飞行距离（格）。0 表示不限，永不衰减也永不消失。
     * 超过时触发 {@link #onExpire} 并 discard。
     */
    protected double maxFlyDistance = 0D;
    /**
     * 是否能命中空中目标。默认 false，符合 PvZ 原作"普通植物打不到气球僵尸"。
     * 仙人掌、猫尾草、杨桃等覆写返回 true。
     */
    protected boolean canHitAirborne = false;
    /**
     * 是否开启穿过检测。默认 false，零开销。
     * 火焰树桩这种"子弹穿过时触发转换"的机制覆写返回 true。
     */
    protected boolean wantsPassThroughCheck = false;

    protected AbstractPAZProjectile(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }




    /* ================================================================== */
    /* 同步数据                                                            */
    /* ================================================================== */

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    /* ================================================================== */
    /* IPAZProjectile 契约                                                 */
    /* ================================================================== */

    @Override
    public void shootFrom(IPAZEntity shooter) {
        this.ownerGroup = shooter.getEntityGroupType();
        this.projectileSkills = shooter.getSkillLevels().filter(id ->
                PVZAPI.lookup().skill(id)
                        .map(skill -> skill.getScope() == SkillScope.PROJECTILE)
                        .orElse(false));
        if (shooter instanceof PlantEntity plant) {
            this.cardEnchantments = plant.getCardEnchantments();
        }
        if (shooter instanceof Entity entity) {
            this.setOwnerUUID(entity.getUUID());
        }
        this.shootOrigin = this.position();
        this.pierceLeft = this.computePierceCount();
        this.bulletSpeed = this.computeBulletSpeed();
        this.attackDamage = this.computeAttackDamage(null);
        this.onShootFrom(shooter);
    }

    @Override
    public void aimAt(LivingEntity target) {
        Vec3 aim = target.position().add(0, target.getBbHeight() * 0.5, 0);
        this.aimAt(aim.subtract(this.position()).normalize());
    }

    @Override
    public void aimAt(Vec3 direction) {
        this.setDeltaMovement(direction.scale(this.getBulletSpeed()));
    }

    @Override
    public GroupType getOwnerGroup() {
        return this.ownerGroup;
    }

    @Override
    public int getMaxLiveTick() {
        return this.maxLiveTick;
    }

    @Override
    public void setMaxLiveTick(int ticks) {
        this.maxLiveTick = ticks;
    }

    /* ================================================================== */
    /* 子类扩展点：数据                                                        */
    /* ================================================================== */

    /** {@link #shootFrom} 末尾回调，子类可在此设置变体、覆盖生命周期等。 */
    protected void onShootFrom(IPAZEntity shooter) {
    }

    /** 弹药基础伤害（不含技能、附魔）。子类覆写。 */
    protected float getBaseAttackDamage() {
        return 0F;
    }

    /** 弹药基础弹速。子类覆写。 */
    protected float getBaseBulletSpeed() {
        return DEFAULT_BULLET_SPEED;
    }

    /** 弹药基础穿透次数。0 = 普通弹，-1 = 无限。子类覆写。 */
    protected int getBasePierceCount() {
        return 0;
    }

    /**
     * 消费伤害技能得到最终伤害。基类默认用 {@link #getBaseAttackDamage()} 或 {@link #attackDamage}。
     *
     * <p>子类覆写时读 {@link #getProjectileSkillValue(ISkillType)} 按需乘算或加算。
     */
    protected float computeAttackDamage(@Nullable Entity target) {
        if (this.attackDamage > 0) {
            return this.attackDamage;
        }
        return this.getBaseAttackDamage();
    }

    /**
     * 消费速度技能得到最终弹速。
     */
    protected float computeBulletSpeed() {
        return this.getBaseBulletSpeed();
    }

    /**
     * 消费穿透技能得到最终穿透次数。
     */
    protected int computePierceCount() {
        return this.getBasePierceCount();
    }

    /** 轨迹类型。子类按需覆写。 */
    public ProjectileTrajectory getTrajectory() {
        return ProjectileTrajectory.DIRECT;
    }

    /** 是否追踪目标。追踪弹覆写返回 true，并调用 {@link #setTargetId(UUID)}。 */
    public boolean isHoming() {
        return false;
    }

    /** 是否走弹道方程。{@link ProjectileTrajectory#LOB} 的弹药覆写返回 true。 */
    public boolean isBallistic() {
        return this.getTrajectory() == ProjectileTrajectory.LOB;
    }

    /** 是否受重力。默认 false；{@link ProjectileTrajectory#LOB} 的弹药覆写返回 true。 */
    public boolean hasGravity() {
        return false;
    }

    /** 重力加速度。 */
    protected float getGravityVelocity() {
        return DEFAULT_GRAVITY;
    }

    /** 弹速。 */
    public float getBulletSpeed() {
        return this.bulletSpeed;
    }

    public void setBulletSpeed(float speed) {
        this.bulletSpeed = speed;
    }

    /** 俯仰角限制，供 {@link #shootDirection(Vec3)} 使用。 */
    public double getShootAngle() {
        return this.shootAngle;
    }

    public void setShootAngle(double angle) {
        this.shootAngle = angle;
    }

    /** 命中实体。子类必须实现。 */
    protected abstract void onImpact(HitResult result);

    /** 过期回调（生命周期耗尽或飞出最大距离）。默认什么都不做。 */
    protected void onExpire() {
    }

    /**
     * 伤害修正。基类直接返回 {@code base}；
     * 子类可读 {@link #getProjectileSkillValue(ISkillType)} 覆写。
     */
    protected float modifyDamage(float base, Entity target) {
        return base;
    }

    /**
     * 附魔效果触发落点。<b>基类在命中实体时自动调用</b>，子类覆写实现具体效果。
     * 只读 {@link #cardEnchantments}（已过滤 CARD tag）。
     */
    protected void applyEnchantEffects(Entity target) {
        // TODO: cardEnchantments.entrySet().forEach(entry -> ...);
    }

    /* ================================================================== */
    /* 子类扩展点：回调                                                        */
    /* ================================================================== */

    /** 穿过实体的回调。默认空，火焰树桩转换等场景覆写。 */
    protected void onPassThroughEntity(Entity entity) {
    }

    /** 穿过方块的回调。默认空。 */
    protected void onPassThroughBlock(BlockState state, BlockPos pos) {
    }

    /** 追踪目标丢失时的回调。猫尾草等需要重新搜索的弹药覆写。 */
    protected void onTargetLost() {
    }

    /** 命中实体后的回调。猫尾草等需要在命中后切换目标的弹药覆写。 */
    protected void onTargetHit(Entity target) {
    }

    /* ================================================================== */
    /* 自动接线：药水与附魔                                                    */
    /* ================================================================== */

    /**
     * 命中实体时自动施加 {@link #cachedEffect}。子类可覆写关闭。
     *
     * <p>调用 {@code addEffect(effect, source)} 而非 {@code addEffect(effect)}，
     * 让药水来源可追溯，并能被 {@code MobEffectEvent.Added} 拦截。
     */
    protected void applyCachedEffect(Entity target) {
        if (this.cachedEffect == null) {
            return;
        }
        if (!(target instanceof LivingEntity living)) {
            return;
        }
        living.addEffect(new MobEffectInstance(this.cachedEffect), this.getOwnerEntity(this.level()).orElse(null));
    }

    /**
     * 通用药水施加工具。子类在溅射、多段等场景手动施加时用。
     */
    protected void applyEffectToTarget(Entity target, @Nullable MobEffectInstance effect) {
        if (effect == null || !(target instanceof LivingEntity living)) {
            return;
        }
        living.addEffect(new MobEffectInstance(effect), this.getOwnerEntity(this.level()).orElse(null));
    }

    /* ================================================================== */
    /* tick 主循环                                                         */
    /* ================================================================== */

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.tickMove();
            return;
        }
        if (this.tickCount >= this.getMaxLiveTick()) {
            this.onExpire();
            this.discard();
            return;
        }
        if (this.maxFlyDistance > 0 && this.shootOrigin != null
                && this.position().distanceToSqr(this.shootOrigin) > this.maxFlyDistance * this.maxFlyDistance) {
            this.onExpire();
            this.discard();
            return;
        }
        this.tickHoming();
        this.tickImpact();
        if (this.isRemoved()) {
            return;
        }
        this.tickMove();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    /** 追踪：校验目标有效性，失效则降级为直线，不 discard。 */
    protected void tickHoming() {
        if (!this.isHoming()) {
            return;
        }
        LivingEntity target = this.resolveTarget();
        if (target == null) {
            return;
        }
        this.aimAt(target);
    }

    /**
     * 解析追踪目标。目标失效（移除、死亡、阵营翻转、不可命中、跨维度）时
     * 清空 {@link #targetId}，调 {@link #onTargetLost}，返回 null。
     */
    @Nullable
    protected LivingEntity resolveTarget() {
        if (this.targetId.isEmpty()) {
            return null;
        }
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        Entity entity = serverLevel.getEntity(this.targetId.get());
        if (!(entity instanceof LivingEntity living) || !living.isAlive() || living.isRemoved()) {
            this.clearTargetAndNotify();
            return null;
        }
        if (!(living instanceof IPAZEntity paz) || !paz.canBeHitBy(this)) {
            this.clearTargetAndNotify();
            return null;
        }
        if (paz.getEntityGroupType() == this.ownerGroup) {
            this.clearTargetAndNotify();
            return null;
        }
        return living;
    }

    private void clearTargetAndNotify() {
        this.targetId = Optional.empty();
        this.onTargetLost();
    }

    /** 设置追踪目标 UUID。 */
    public void setTargetId(@Nullable UUID uuid) {
        this.targetId = Optional.ofNullable(uuid);
    }

    /**
     * 射线检测：块 clip + 实体 ray trace，取近者，命中就调 onImpact。
     *
     * <p>命中实体时先经 {@link #resolveOwnerEntity} 映射到宿主，
     * 阵营判定、药水、附魔、伤害都作用在宿主上；传给 {@link #onImpact}
     * 的 {@link EntityHitResult} 也替换为宿主版本，避免子类直接读
     * {@code getEntity()} 时打到 {@link MultiPartEntity}（{@code hurtServer}
     * 返回 false，伤害静默丢失）。
     */
    protected void tickImpact() {
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());

        HitResult blockHit = this.level().clip(new ClipContext(start, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        EntityHitResult entityHit = this.rayTraceEntities(start, end);
        HitResult result = entityHit != null ? entityHit : blockHit;

        if (result.getType() == HitResult.Type.MISS) {
            return;
        }
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockState state = this.level().getBlockState(((BlockHitResult) result).getBlockPos());
            if (this.ignoreBlocks.test(state)) {
                return;
            }
        }

        if (result instanceof EntityHitResult entityResult) {
            final Entity raw = entityResult.getEntity();
            Entity target = this.resolveOwnerEntity(raw);
            if (!this.shouldHit(target)) {
                return;
            }
            if (raw != target && raw instanceof MultiPartEntity
                    && target instanceof DefenceZombieEntity defence) {
                defence.hitDefence = true;
            }

            this.onTargetHit(target);
            this.applyCachedEffect(target);
            this.applyEnchantEffects(target);
            result = new EntityHitResult(target, entityResult.getLocation());
        }
        this.setPos(result.getLocation());
        this.onImpact(result);
    }

    @Nullable
    protected EntityHitResult rayTraceEntities(Vec3 start, Vec3 end) {
        AABB box = this.getBoundingBox()
                .expandTowards(this.getDeltaMovement())
                .inflate(1.0D);
        return ProjectileUtil.getEntityHitResult(
                this.level(), this, start, end,
                box, this::canHitEntity, ProjectileUtil.computeMargin(this));
    }

    /** 单发弹药是否可以把 {@code target} 纳入射线候选。 */
    protected boolean canHitEntity(Entity target) {
        if (target == this || !target.isPickable()) {
            return false;
        }
        Entity owner = this.resolveOwnerEntity(target);
        if (this.hitEntities.contains(owner.getId())) {
            return false;
        }
        return this.shouldHit(owner);
    }

    /**
     * 阵营 + 目标侧判定。三步：比阵营、问目标 {@link IPAZEntity#canBeHitBy}、
     * 若不能打空中则排除空中目标。
     */
    protected boolean shouldHit(Entity target) {
        if (!(target instanceof LivingEntity living)) {
            return false;
        }
        if (EntityUtil.getEntityGroup(living) == this.ownerGroup) {
            return false;
        }
        if (!this.canHitAirborne && this.isTargetAirborne(target)) {
            return false;
        }
        return !(living instanceof IPAZEntity paz) || paz.canBeHitBy(this);
    }

    /**
     * 目标是否在空中。tag 与 y 差双判定，任一为真即视为空中。
     * 子类可覆写加更多判定。
     */
    protected boolean isTargetAirborne(Entity target) {
        if (target.is(AIRBORNE)) {
            return true;
        }
        double yDiff = target.getY() - this.getY();
        return !target.onGround() && yDiff > DEFAULT_AIRBORNE_Y_THRESHOLD;
    }

    /**
     * 多部件映射。命中 {@link MultiPartEntity} 部件时返回其宿主；
     * 否则返回 {@code target} 自身。
     *
     * <p>部件 {@code hurtServer} 返回 false，伤害必须作用在宿主上。
     * 同时 {@code hitEntities} 记的是宿主 ID，同一僵尸多部件不会被重复命中。
     */
    protected Entity resolveOwnerEntity(Entity target) {
        if (target instanceof MultiPartEntity part) {
            LivingEntity owner = part.getOwner();
            if (owner != null) {
                return owner;
            }
        }
        return target;
    }

    /** 把目标加入穿透集（含多部件映射）。 */
    public void addHitEntity(Entity entity) {
        Entity owner = this.resolveOwnerEntity(entity);
        this.hitEntities.add(owner.getId());
    }

    /**
     * 消耗一次穿透。返回 true 表示穿透耗尽，调用方应 {@code discard}。
     */
    protected boolean tryPierce() {
        if (this.pierceLeft == INFINITE_PIERCE) {
            return false;
        }
        if (this.pierceLeft > 0) {
            --this.pierceLeft;
            return false;
        }
        return true;
    }

    public int getPierceLeft() {
        return this.pierceLeft;
    }

    public void setPierceLeft(int value) {
        this.pierceLeft = value;
    }

    public void setIgnoreBlocks(Predicate<BlockState> predicate) {
        this.ignoreBlocks = predicate == null ? state -> false : predicate;
    }

    /* ================================================================== */
    /* 移动                                                                */
    /* ================================================================== */

    protected void tickMove() {
        Vec3 motion = this.getDeltaMovement();
        double nextX = this.getX() + motion.x;
        double nextY = this.getY() + motion.y;
        double nextZ = this.getZ() + motion.z;

        float horizontal = (float) Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        float targetYRot = (float) (Math.atan2(motion.x, motion.z) * (180F / (float) Math.PI));
        float targetXRot = (float) (Math.atan2(motion.y, horizontal) * (180F / (float) Math.PI));
        this.setYRot(net.minecraft.util.Mth.lerp(0.2F, this.yRotO, targetYRot));
        this.setXRot(net.minecraft.util.Mth.lerp(0.2F, this.xRotO, targetXRot));

        Vec3 from = this.position();
        float slowdown = this.computeAirSlowDown(from);
        this.setDeltaMovement(motion.scale(slowdown));

        if (this.hasGravity() && !this.isNoGravity()) {
            Vec3 current = this.getDeltaMovement();
            this.setDeltaMovement(current.x, current.y - this.getGravityVelocity(), current.z);
        }
        this.setPos(nextX, nextY, nextZ);

        if (this.wantsPassThroughCheck) {
            this.checkPassThrough(from, this.position());
        }
    }

    /**
     * 根据当前位置与发射原点计算空气阻力。有效距离内匀速，
     * 超出阈值后快速衰减。
     */
    protected float computeAirSlowDown(Vec3 from) {
        if (this.maxFlyDistance <= 0 || this.shootOrigin == null) {
            return this.airSlowDownNear;
        }
        double ratio = Math.sqrt(from.distanceToSqr(this.shootOrigin)) / this.maxFlyDistance;
        return ratio < this.slowDownStartRatio ? this.airSlowDownNear : this.airSlowDownFar;
    }

    /**
     * 检测从 from 到 to 之间穿过的实体与方块，触发对应回调。
     *
     * <p>基类 {@code Entity#onInsideBlock} 对弹药无效——它由
     * {@code LivingEntity.aiStep} 的 {@code applyEffectsFromBlocks} 触发，
     * 而弹药继承 Entity 不经过那条链。因此必须在此手动检测。
     */
    protected void checkPassThrough(Vec3 from, Vec3 to) {
        BlockPos fromPos = BlockPos.containing(from);
        BlockPos toPos = BlockPos.containing(to);
        if (!fromPos.equals(toPos)) {
            BlockPos min = new BlockPos(
                    Math.min(fromPos.getX(), toPos.getX()),
                    Math.min(fromPos.getY(), toPos.getY()),
                    Math.min(fromPos.getZ(), toPos.getZ()));
            BlockPos max = new BlockPos(
                    Math.max(fromPos.getX(), toPos.getX()),
                    Math.max(fromPos.getY(), toPos.getY()),
                    Math.max(fromPos.getZ(), toPos.getZ()));
            for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
                BlockState state = this.level().getBlockState(pos);
                if (!state.isAir()) {
                    this.onPassThroughBlock(state, pos.immutable());
                }
            }
        }

        AABB sweep = new AABB(
                Math.min(from.x, to.x) - 0.3, Math.min(from.y, to.y) - 0.3, Math.min(from.z, to.z) - 0.3,
                Math.max(from.x, to.x) + 0.3, Math.max(from.y, to.y) + 0.3, Math.max(from.z, to.z) + 0.3);
        for (Entity entity : this.level().getEntities(this, sweep)) {
            if (entity != this) {
                this.onPassThroughEntity(entity);
            }
        }
    }

    /**
     * 朝指定方向发射，带俯仰角限制。
     * {@code shootAngle} 越大，俯仰限制越紧。
     */
    public void shootDirection(Vec3 direction) {
        double dx = direction.x;
        double dy = direction.y;
        double dz = direction.z;
        double limit = this.getShootAngle();
        double dxz = Math.sqrt(dx * dx + dz * dz);
        if (limit > 0 && dxz > 0) {
            dy = net.minecraft.util.Mth.clamp(dy, -dxz / limit, dxz / limit);
        }
        double total = Math.sqrt(dxz * dxz + dy * dy);
        if (total <= 1.0E-7) {
            return;
        }
        this.setDeltaMovement(new Vec3(dx, dy, dz).scale(this.getBulletSpeed() / total));
    }

    /* ================================================================== */
    /* 抛物线解算                                                          */
    /* ================================================================== */

    /**
     * 抛物线解算器。给定起点、终点、弹速、重力，返回初速度向量。
     *
     * <p>解二次方程：{@code y = x·tanθ - (g·x²) / (2·v²·cos²θ)}。
     * 判别式为负（弹速不够）时返回 null。
     *
     * <p>不考虑空气阻力，因此长距离弹道会有所偏移，属于可接受的近似。
     */
    @Nullable
    public static Vec3 solveBallistic(Vec3 from, Vec3 to, float speed, float gravity) {
        Vec3 delta = to.subtract(from);
        double horizontal = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
        if (horizontal < 1.0E-4) {
            return null;
        }
        double v2 = (double) speed * speed;
        double g = gravity;
        double discriminant = v2 * v2 - g * (g * horizontal * horizontal + 2 * delta.y * v2);
        if (discriminant < 0) {
            return null;
        }
        double sqrtDisc = Math.sqrt(discriminant);
        double angle = Math.atan2(v2 - sqrtDisc, g * horizontal);
        double cos = Math.cos(angle);
        double vx = cos * speed * (delta.x / horizontal);
        double vz = cos * speed * (delta.z / horizontal);
        double vy = Math.sin(angle) * speed;
        return new Vec3(vx, vy, vz);
    }

    /** 朝目标点做抛物线瞄准。解算失败时保持当前速度。 */
    public void aimAtBallistic(Vec3 target) {
        Vec3 velocity = solveBallistic(this.position(), target, this.getBulletSpeed(), this.getGravityVelocity());
        if (velocity != null) {
            this.setDeltaMovement(velocity);
        }
    }

    /* ================================================================== */
    /* 伤害 / 穿透                                                         */
    /* ================================================================== */

    /**
     * 对目标造成弹药伤害。一站式工具：
     * <ol>
     *   <li>读 {@link #computeAttackDamage} 得最终伤害</li>
     *   <li>清零目标的 {@code invulnerableTime} 与 {@code damageCooldownTime}，
     *       保证连发、溅射、穿透不会被无敌帧吞掉</li>
     *   <li>调 {@code hurtServer} 打上去</li>
     * </ol>
     *
     * <p><b>为何要清 {@code damageCooldownTime}</b>：LivingEntity 的 {@code hurtServer}
     * 里真正的冷却判定是 {@code damageCooldownTime > 10 && damage <= lastHurt}，
     * 仅 {@code setInvulnerableTime(0)} 不足以让后续命中生效。
     * {@code damageCooldownTime} 是 LivingEntity 的 public 字段，直接清零即可。
     *
     * <p><b>玩家例外</b>：{@code damageCooldownTime} 对 ServerPlayer 不自动递减
     * （LivingEntity.baseTick 有 {@code !(this instanceof ServerPlayer)} 条件），
     * 弹药对玩家连发时第二发会等 20 tick。PvZ 僵尸植物暂无卡片，本次不处理。
     *
     * @return 目标是否真的受到伤害
     */
    protected boolean dealProjectileDamage(@Nullable Entity target) {
        if (target == null || !(this.level() instanceof ServerLevel serverLevel)) {
            return false;
        }
        float damage = this.computeAttackDamage(target);
        if (damage <= 0) {
            return false;
        }
        damage = this.modifyDamage(damage, target);

        if (target instanceof LivingEntity living) {
            living.setInvulnerableTime(0);
            living.damageCooldownTime = 0;
        }
        DamageSource source = this.createDamageSource();
        return target.hurtServer(serverLevel, source, damage);
    }

    /** 弹药伤害源。默认用 thrown，子类可覆写为 PVZ 专属伤害源。 */
    protected DamageSource createDamageSource() {
        Entity owner = this.getOwnerEntity(this.level()).orElse(null);
        return this.damageSources().thrown(this, owner);
    }

    /* ================================================================== */
    /* 溅射                                                                */
    /* ================================================================== */

    /**
     * 收集以 {@code center} 为中心、半径 {@code radius} 内的目标。
     *
     * <p>默认过滤：命中白名单为空、阵营不同、{@link #shouldHit} 为真。
     * 子类可通过 {@code filter} 追加谓词。
     */
    protected List<LivingEntity> collectSplashTargets(Vec3 center, double radius, @Nullable Predicate<LivingEntity> filter) {
        AABB box = new AABB(center, center).inflate(radius);
        List<LivingEntity> result = new ArrayList<>();
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (!this.shouldHit(living)) {
                continue;
            }
            if (filter != null && !filter.test(living)) {
                continue;
            }
            result.add(living);
        }
        return result;
    }

    /** 溅射伤害：收集并逐个造成弹药伤害。 */
    protected void splashDamage(Vec3 center, double radius, @Nullable Predicate<LivingEntity> filter) {
        for (LivingEntity target : this.collectSplashTargets(center, radius, filter)) {
            this.dealProjectileDamage(target);
        }
    }

    /** 溅射药水：收集并逐个施加药水。 */
    protected void splashEffect(Vec3 center, double radius, @Nullable MobEffectInstance effect, @Nullable Predicate<LivingEntity> filter) {
        if (effect == null) {
            return;
        }
        for (LivingEntity target : this.collectSplashTargets(center, radius, filter)) {
            this.applyEffectToTarget(target, effect);
        }
    }

    /* ================================================================== */
    /* 技能 / 附魔工具                                                     */
    /* ================================================================== */

    /** 读快照的 PROJECTILE 面技能值。 */
    protected float getProjectileSkillValue(ISkillType type) {
        return type.getValueAt(this.projectileSkills.getLevel(type));
    }

    public SkillLevels getProjectileSkills() {
        return this.projectileSkills;
    }

    public ItemEnchantments getCardEnchantments() {
        return this.cardEnchantments;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public void setAttackDamage(float damage) {
        this.attackDamage = damage;
    }

    @Nullable
    public MobEffectInstance getCachedEffect() {
        return this.cachedEffect;
    }

    protected void setCachedEffect(@Nullable MobEffectInstance effect) {
        this.cachedEffect = effect;
    }

    /* ================================================================== */
    /* 渲染 / 网络                                                         */
    /* ================================================================== */

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(size)) {
            size = 4.0D;
        }
        size *= 64.0D;
        return distance < size * size;
    }

    @Override
    public void lerpMotion(Vec3 movement) {
        super.lerpMotion(movement);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double x = movement.x;
            double y = movement.y;
            double z = movement.z;
            float horizontal = (float) Math.sqrt(x * x + z * z);
            this.setYRot((float) (Math.atan2(x, z) * (180F / (float) Math.PI)));
            this.setXRot((float) (Math.atan2(y, horizontal) * (180F / (float) Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
            this.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }

    /* ================================================================== */
    /* NBT                                                                 */
    /* ================================================================== */

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        this.getOwnerUUID().ifPresent(uuid -> output.store("owner_uuid", UUIDUtil.CODEC, uuid));
        output.putInt("owner_group", this.ownerGroup.ordinal());
        output.putInt("max_live_tick", this.maxLiveTick);
        output.putFloat("bullet_speed", this.bulletSpeed);
        output.putFloat("attack_damage", this.attackDamage);
        output.putDouble("shoot_angle", this.shootAngle);
        output.putInt("pierce_left", this.pierceLeft);
        output.putBoolean("can_hit_airborne", this.canHitAirborne);
        output.putBoolean("wants_pass_through", this.wantsPassThroughCheck);
        output.putDouble("max_fly_distance", this.maxFlyDistance);
        output.store("projectile_skills", SkillLevels.CODEC, this.projectileSkills);
        output.store("card_enchantments", ItemEnchantments.CODEC, this.cardEnchantments);
        this.targetId.ifPresent(uuid -> output.store("target_uuid", UUIDUtil.CODEC, uuid));
        if (this.shootOrigin != null) {
            output.store("shoot_origin", Vec3.CODEC, this.shootOrigin);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        input.read("owner_uuid", UUIDUtil.CODEC).ifPresent(this::setOwnerUUID);
        int groupOrdinal = input.getIntOr("owner_group", GroupType.NEUTRALS.ordinal());
        GroupType[] groups = GroupType.values();
        this.ownerGroup = (groupOrdinal < 0 || groupOrdinal >= groups.length)
                ? GroupType.NEUTRALS
                : groups[groupOrdinal];
        this.maxLiveTick = input.getIntOr("max_live_tick", DEFAULT_MAX_LIVE_TICK);
        this.bulletSpeed = input.getFloatOr("bullet_speed", DEFAULT_BULLET_SPEED);
        this.attackDamage = input.getFloatOr("attack_damage", 0F);
        this.shootAngle = input.getDoubleOr("shoot_angle", 0D);
        this.pierceLeft = input.getIntOr("pierce_left", 0);
        this.canHitAirborne = input.getBooleanOr("can_hit_airborne", false);
        this.wantsPassThroughCheck = input.getBooleanOr("wants_pass_through", false);
        this.maxFlyDistance = input.getDoubleOr("max_fly_distance", 0D);
        this.projectileSkills = input.read("projectile_skills", SkillLevels.CODEC)
                .orElse(SkillLevels.EMPTY);
        this.cardEnchantments = input.read("card_enchantments", ItemEnchantments.CODEC)
                .orElse(ItemEnchantments.EMPTY);
        input.read("target_uuid", UUIDUtil.CODEC).ifPresentOrElse(
                uuid -> this.targetId = Optional.of(uuid),
                () -> this.targetId = Optional.empty());
        this.shootOrigin = input.read("shoot_origin", Vec3.CODEC).orElse(null);
    }
}