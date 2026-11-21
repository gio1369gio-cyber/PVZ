package com.lying.pvz.common.projectile;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.projectile.BulletImpact;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.BulletMotion;
import com.lying.pvz.api.projectile.BulletTransformer;
import com.lying.pvz.api.projectile.IBullet;
import com.lying.pvz.common.init.PVZEntityDataSerializers;
import com.lying.pvz.common.level.entity.MultiPartEntity;
import com.lying.pvz.utils.WorldUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * 新弹药体系的唯一实体类。
 *
 * <p>职责只有四件事：
 * <ol>
 *   <li>每 tick 交给 {@link BulletMotion} 推进；</li>
 *   <li>用 {@link SweptHitResolver} 求命中；</li>
 *   <li>按顺序执行 {@link BulletImpact.Step}；</li>
 *   <li>维护同步数据（变体 / 目标）。</li>
 * </ol>
 *
 * <p>伤害、效果、爆炸、落地生成、标记都不在类里写死——它们由
 * {@code BulletKind} 的 impact 列表组合。新增一种子弹不需要新实体类。
 */
public class BulletEntity extends Entity implements IBullet {

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> TARGET_UUID =
            SynchedEntityData.defineId(BulletEntity.class, PVZEntityDataSerializers.OPTIONAL_UUID.get());
    private static final EntityDataAccessor<Integer> STATE =
            SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.INT);

    /* 发射快照（服务端） */
    @Nullable
    private UUID ownerUuid;
    private GroupType ownerGroup = GroupType.NEUTRALS;
    private float baseDamage;
    private float damageAdd;
    private float damageMultiplier = 1.0F;
    private float speed;
    private int maxLifeTicks = 40;
    private int pierceLeft;
    private double maxFlyDistance;
    @Nullable
    private Vec3 shootOrigin;
    private List<BulletImpact.Step> impactSteps = List.of();

    /* 飞行状态（服务端） */
    private final IntOpenHashSet hitEntities = new IntOpenHashSet();
    private final Set<TriggerKey> triggered = new HashSet<>();

    public BulletEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    /* ================================================================== */
    /* 同步数据                                                            */
    /* ================================================================== */

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(VARIANT, 0);
        builder.define(TARGET_UUID, Optional.empty());
        builder.define(STATE, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key.equals(VARIANT)) {
            this.refreshDimensions();
        }
    }

    /* ================================================================== */
    /* IBullet                                                             */
    /* ================================================================== */

    @Override
    public Entity self() {
        return this;
    }

    @Override
    public BulletKind kind() {
        final BulletKind kind = BulletKinds.get(this.getType());
        if (kind == null) {
            throw new IllegalStateException("No BulletKind registered for " + this.getType());
        }
        return kind;
    }

    @Override
    public int variant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    public void setVariant(int variant) {
        final int safe = this.kind().variants().has(variant) ? variant : 0;
        if (this.entityData.get(VARIANT) == safe) {
            return;
        }
        this.entityData.set(VARIANT, safe);
        this.refreshDimensions();
    }

    @Override
    public int state() {
        return this.entityData.get(STATE);
    }

    @Override
    public void setState(int state) {
        this.entityData.set(STATE, state);
    }

    @Override
    public Vec3 velocity() {
        return this.getDeltaMovement();
    }

    @Override
    public void setVelocity(Vec3 velocity) {
        this.setDeltaMovement(velocity);
    }

    @Override
    public @Nullable Entity ownerEntity() {
        return this.ownerUuid == null ? null : this.level().getEntity(this.ownerUuid);
    }

    @Override
    public @Nullable UUID ownerUuid() {
        return this.ownerUuid;
    }

    @Override
    public GroupType ownerGroup() {
        return this.ownerGroup;
    }

    @Override
    public @Nullable LivingEntity target() {
        final Optional<UUID> id = this.entityData.get(TARGET_UUID);
        if (id.isEmpty()) {
            return null;
        }
        return this.level().getEntity(id.get()) instanceof LivingEntity living ? living : null;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        this.entityData.set(TARGET_UUID,
                Optional.ofNullable(target == null ? null : target.getUUID()));
    }

    @Override
    public int tickCount() {
        return this.tickCount;
    }

    @Override
    public float baseDamage() {
        return this.baseDamage;
    }

    @Override
    public float damageAdd() {
        return this.damageAdd;
    }

    @Override
    public float damageMultiplier() {
        return this.damageMultiplier;
    }

    @Override
    public int pierceLeft() {
        return this.pierceLeft;
    }

    @Override
    public void setPierceLeft(int pierce) {
        this.pierceLeft = pierce;
    }

    @Override
    public boolean hasHit(Entity target) {
        final Entity host = target instanceof MultiPartEntity part ? part.getOwner() : target;
        return host != null && this.hitEntities.contains(host.getId());
    }

    @Override
    public float speed() {
        return this.speed;
    }

    @Override
    public boolean hasTriggered(Identifier transformer, int providerId) {
        return this.triggered.contains(new TriggerKey(transformer, providerId));
    }

    @Override
    public void markTriggered(Identifier transformer, int providerId) {
        this.triggered.add(new TriggerKey(transformer, providerId));
    }

    @Override
    public void applyTransform(BulletTransformer.Result result) {
        if (result.variant() != BulletTransformer.Result.KEEP_VARIANT) {
            this.setVariant(result.variant());
        }
        this.damageAdd += result.damageAdd();
        this.damageMultiplier *= result.damageMultiplier();
    }

    /* ================================================================== */
    /* 发射快照                                                            */
    /* ================================================================== */

    /** 由 {@code BulletLauncher} 在加入世界前调用一次。 */
    public void applyShot(BulletShot shot) {
        this.ownerUuid = shot.owner();
        this.ownerGroup = shot.group();
        this.impactSteps = shot.impact();
        this.baseDamage = shot.baseDamage();
        this.damageAdd = 0.0F;
        this.damageMultiplier = shot.damageMultiplier();
        this.speed = shot.speed();
        this.maxLifeTicks = shot.lifeTicks();
        this.pierceLeft = shot.pierce();
        this.maxFlyDistance = shot.maxFlyDistance();
        this.setVariant(shot.variant());
        this.shootOrigin = this.position();
    }

    /* ================================================================== */
    /* tick                                                                */
    /* ================================================================== */

    @Override
    public void tick() {
        super.tick();
        final BulletKind kind = this.kind();
        final BulletMotion motion = kind.motion();

        if (this.level().isClientSide()) {
            this.spawnTrailParticle(kind);
            if (motion.syncMode() == BulletMotion.SyncMode.PREDICTED) {
                motion.tick(this);
                this.setPos(this.position().add(this.getDeltaMovement()));
                motion.postTick(this);
                this.updateRotation();
            }
            return;
        }

        if (this.maxLifeTicks > 0 && this.tickCount >= this.maxLifeTicks) {
            this.discard();
            return;
        }
        if (this.maxFlyDistance > 0.0D && this.shootOrigin != null
                && this.position().distanceToSqr(this.shootOrigin) > this.maxFlyDistance * this.maxFlyDistance) {
            this.discard();
            return;
        }

        motion.tick(this);
        final Vec3 from = this.position();
        final Vec3 to = from.add(this.getDeltaMovement());

        if (kind.passThrough()) {
            PassThroughScanner.scan(this, from, to);
        }

        final BulletImpact.Hit hit = SweptHitResolver.resolve(this, from, to);
        if (hit == null) {
            this.setPos(to);
            motion.postTick(this);
            this.updateRotation();
            return;
        }

        this.setPos(hit.pos());
        boolean consumed = false;
        for (BulletImpact.Step step : this.impactSteps) {
            if (step.apply(this, hit) == BulletImpact.Result.CONSUME) {
                consumed = true;
                break;
            }
        }

        if (!consumed) {
            if (hit.target() != null) {
                this.addHitEntity(hit.target());
                if (this.tryPierce()) {
                    consumed = true;
                }
            } else if (hit.blockState() != null) {
                consumed = true;
            }
        }

        if (consumed) {
            this.discard();
        } else {
            this.setPos(to);
            motion.postTick(this);
            this.updateRotation();
        }
    }

    /** 客户端每 tick 生成飞行尾迹粒子（孢子 / 烟雾等）。 */
    private void spawnTrailParticle(BulletKind kind) {
        final BulletKind.TrailSpec trail = kind.trail();
        if (trail == null) {
            return;
        }
        for (int i = 0; i < trail.count(); i++) {
            WorldUtil.spawnRandomSpeedParticle(this.level(),
                    trail.particle().get(), this.position(), trail.speed());
        }
    }

    private void updateRotation() {
        final Vec3 velocity = this.getDeltaMovement();
        final double horizontal = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        if (horizontal < 1.0E-5D && Math.abs(velocity.y) < 1.0E-5D) {
            return;
        }
        final float yaw = (float) (Math.atan2(velocity.x, velocity.z) * (180.0D / Math.PI));
        final float pitch = (float) (Math.atan2(velocity.y, horizontal) * (180.0D / Math.PI));
        this.setYRot(net.minecraft.util.Mth.lerp(0.2F, this.yRotO, yaw));
        this.setXRot(net.minecraft.util.Mth.lerp(0.2F, this.xRotO, pitch));
    }

    @Override
    public void addHitEntity(Entity target) {
        final Entity host = target instanceof MultiPartEntity part ? part.getOwner() : target;
        if (host != null) {
            this.hitEntities.add(host.getId());
        }
    }

    /** 返回 true 表示穿透余额耗尽，应销毁。 */
    private boolean tryPierce() {
        if (this.pierceLeft == -1) {
            return false;
        }
        if (this.pierceLeft > 0) {
            --this.pierceLeft;
            return false;
        }
        return true;
    }

    /* ================================================================== */
    /* Entity                                                              */
    /* ================================================================== */

    @Override
    public net.minecraft.world.entity.EntityDimensions getDimensions(Pose pose) {
        return this.kind().hitbox(this.variant());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

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
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }

    /* ================================================================== */
    /* NBT                                                                 */
    /* ================================================================== */

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("variant", this.variant());
        output.putInt("owner_group", this.ownerGroup.ordinal());
        output.putFloat("base_damage", this.baseDamage);
        output.putFloat("damage_multiplier", this.damageMultiplier);
        output.putFloat("speed", this.speed);
        output.putInt("life_ticks", this.maxLifeTicks);
        output.putInt("pierce_left", this.pierceLeft);
        output.putDouble("max_fly_distance", this.maxFlyDistance);
        if (this.ownerUuid != null) {
            output.store("owner", UUIDUtil.CODEC, this.ownerUuid);
        }
        this.entityData.get(TARGET_UUID).ifPresent(uuid -> output.store("target", UUIDUtil.CODEC, uuid));
        if (this.shootOrigin != null) {
            output.store("shoot_origin", Vec3.CODEC, this.shootOrigin);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.setVariant(input.getIntOr("variant", 0));
        this.ownerUuid = input.read("owner", UUIDUtil.CODEC).orElse(null);

        final GroupType[] groups = GroupType.values();
        final int groupOrdinal = input.getIntOr("owner_group", GroupType.NEUTRALS.ordinal());
        this.ownerGroup = (groupOrdinal >= 0 && groupOrdinal < groups.length)
                ? groups[groupOrdinal] : GroupType.NEUTRALS;

        this.baseDamage = input.getFloatOr("base_damage", 0.0F);
        this.damageAdd = 0.0F;
        this.damageMultiplier = input.getFloatOr("damage_multiplier", 1.0F);
        this.speed = input.getFloatOr("speed", 0.0F);
        this.maxLifeTicks = input.getIntOr("life_ticks", 40);
        this.pierceLeft = input.getIntOr("pierce_left", 0);
        this.maxFlyDistance = input.getDoubleOr("max_fly_distance", 0.0D);
        this.entityData.set(TARGET_UUID, input.read("target", UUIDUtil.CODEC));
        this.shootOrigin = input.read("shoot_origin", Vec3.CODEC).orElse(null);

        // 飞行中存档：恢复 Kind 可推导的步骤；本发额外效果不持久化。
        this.impactSteps = BulletImpacts.rebuild(this.kind(), this.variant(), this.ownerUuid);
    }

    private record TriggerKey(Identifier transformer, int providerId) {
    }
}
