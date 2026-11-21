package com.lying.pvz.common.level.entity;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.entity.ability.IHasGroup;
import com.lying.pvz.api.entity.ability.IHasOwner;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * 带 owner 的实体基类（旧弹药、掉落物、身体部件等共用）。
 *
 * <p><b>owner 语义</b>：
 * <ul>
 *   <li>{@link #ownerId} 是权威数据，存 NBT、跨维度稳定；</li>
 *   <li>{@link #owner} 是运行时缓存，服务端 {@link #getOwner()} 按 {@link #ownerId} 回填；</li>
 *   <li>客户端 {@link #getOwner()} 只能拿到同维度已加载实体，跨维度/未加载返回 {@code null}，
 *       调用方判归属应优先用 {@link #getOwnerUUID()}。</li>
 * </ul>
 *
 * <p><b>group 语义</b>：{@link #groupType} 在 {@link #summonByOwner(Entity)} 时从 owner 快照，
 * 之后不随 owner 变化。读档按 {@link GroupType#ordinal()} 恢复，越界回退
 * {@link #getInitialEntityGroup()}——不走 {@code EntityGroupHander.getGroup}，
 * 那是玩家资源 {@code Resources.GROUP_TYPE} 的语义（带 -2 偏移），与这里存 ordinal 不一致。
 */
public abstract class AbstractOwnerEntity extends PVZBaseEntity implements IHasGroup, IHasOwner {

    /** 运行时 owner 缓存。服务端每次 {@link #getOwner()} 按 ownerId 回填；客户端可能为 null。 */
    protected Entity owner;
    /** 权威 owner UUID。 */
    protected UUID ownerId;
    /** 发射时快照的阵营，不随 owner 变化。 */
    protected GroupType groupType;

    public AbstractOwnerEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.groupType = this.getInitialEntityGroup();
    }

    public AbstractOwnerEntity(EntityType<?> type, Level worldIn, Entity livingEntityIn) {
        super(type, worldIn);
        this.summonByOwner(livingEntityIn);
    }

    /** 从 owner 快照 ownerId 与 groupType。 */
    public void summonByOwner(Entity owner) {
        this.owner = owner;
        this.ownerId = owner.getUUID();
        this.groupType = EntityUtil.getEntityGroup(owner);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    /** 设置 owner 运行时缓存；同时同步 ownerId，保证 NBT 写出的 owner 与缓存一致。 */
    public void setOwner(LivingEntity player) {
        this.owner = player;
        if (player != null) {
            this.ownerId = player.getUUID();
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.level() instanceof ServerLevel serverLevel && this.ownerId != null) {
            final Entity cached = this.owner;
            if (EntityUtil.isEntityValid(cached) && this.ownerId.equals(cached.getUUID())) {
                return cached;
            }
            this.owner = serverLevel.getEntity(this.ownerId);
        }
        return this.owner;
    }

    public Entity getOwnerOrSelf() {
        final Entity resolved = this.getOwner();
        return resolved == null ? this : resolved;
    }

    @Override
    public Optional<UUID> getOwnerUUID() {
        return Optional.ofNullable(this.ownerId);
    }

    public GroupType getInitialEntityGroup() {
        return GroupType.NEUTRALS;
    }

    @Override
    public GroupType getEntityGroupType() {
        return this.groupType;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.ownerId != null) {
            output.store("owner", UUIDUtil.CODEC, this.ownerId);
        }
        output.putInt("entity_tick_exist", this.tickCount);
        output.putInt("group_owner_type", this.groupType.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.owner = null;
        input.read("owner", UUIDUtil.CODEC).ifPresent(uuid -> this.ownerId = uuid);
        this.tickCount = input.getIntOr("entity_tick_exist", 0);

        final int ord = input.getIntOr("group_owner_type", this.getInitialEntityGroup().ordinal());
        final GroupType[] values = GroupType.values();
        this.groupType = (ord >= 0 && ord < values.length)
                ? values[ord]
                : this.getInitialEntityGroup();
    }
}
