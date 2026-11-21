package com.lying.pvz.common.level.entity;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.entity.ability.IHasMultiPart;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 多部件实体的部件基类。
 *
 * <p>部件通过 {@link #OWNER_ID} 指向宿主，宿主的 {@code IHasMultiPart}
 * 每 tick 调用 {@link #setOwner(LivingEntity)} 同步位置与尺寸。
 *
 * <p><b>不参与存档</b>：部件是宿主运行时衍生物，宿主读档后重新生成部件；
 * 因此 NBT 实现为空。
 *
 * <p><b>伤害</b>：部件不接受伤害，{@link #hurtServer} 返回 {@code false}，
 * 伤害由宿主在 {@code hurtServer} 里自行处理。
 */
public abstract class MultiPartEntity extends Entity {

    private static final EntityDataAccessor<Integer> OWNER_ID =
            SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> WIDTH =
            SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEIGHT =
            SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);

    private IHasMultiPart parent;
    protected final float MaxWidth;
    protected final float MaxHeight;

    public MultiPartEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.MaxWidth = 0.5F;
        this.MaxHeight = 0.5F;
    }

    public MultiPartEntity(EntityType<?> entityTypeIn, LivingEntity owner, float sizeX, float sizeY) {
        super(entityTypeIn, owner.level());
        if (owner instanceof IHasMultiPart part) {
            this.parent = part;
        } else {
            PvZMod.LOGGER.warn("MultiPartEntity owner {} does not implement IHasMultiPart", owner);
        }
        this.setOwner(owner);
        this.MaxWidth = sizeX;
        this.MaxHeight = sizeY;
        this.setPartWidth(sizeX);
        this.setPartHeight(sizeY);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_ID, 0);
        builder.define(WIDTH, 0.5F);
        builder.define(HEIGHT, 0.5F);
    }

    @Override
    public void tick() {

        if (this.tickCount <= 5) {
            this.refreshDimensions();
        }
        // 客户端从包里重建的 part 走 (EntityType, Level) 构造，
        // parent == null，但 OWNER_ID 会从包里同步。
        // 服务端构造期建的 part 走带 owner 的构造，parent != null，
        // 这里补一次 OWNER_ID。
        if (this.level().isClientSide() && this.getOwnerId() == 0) {
            this.refreshOwnerIdFromParent();
        }
        if (!this.level().isClientSide()) {
            if (this.canExist()) {
                this.markHurt();
                this.collideWithNearbyEntities();
            } else {
                this.discard();
            }
        }
        super.tick();
    }

    private void refreshOwnerIdFromParent() {
        if (!(this.parent instanceof LivingEntity living)) {
            return;
        }
        try {
            int id = living.getId();
            if (id != 0) {
                this.setOwnerId(id);
            }
        } catch (IllegalStateException ignored) {
            // 下一 tick 再试
        }
    }

    public boolean canExist() {
        return EntityUtil.isEntityValid(this.getOwner());
    }

    @Nullable
    public LivingEntity getOwner() {
        final Entity entity = this.level().getEntity(this.getOwnerId());
        return entity instanceof LivingEntity living ? living : null;
    }

    public IHasMultiPart getParent() {
        return this.parent;
    }

    public void setOwner(LivingEntity entity) {
        if (entity == null) {
            this.setOwnerId(0);
            return;
        }
        try {
            this.setOwnerId(entity.getId());
        } catch (IllegalStateException e) {
            // 客户端 createEntityFromPacket / recreateFromPacket 构造期，
            // owner 还没有 ID。0 视为未绑定，tick 里会补一次。
            this.setOwnerId(0);
        }
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getOwner() == entity;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    /** 子类可覆写以在每 tick 推挤附近实体。 */
    public void collideWithNearbyEntities() {
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 vec3) {
        return InteractionResult.FAIL;
    }

    /**
     * 部件不接受伤害。1.21.1 伤害入口是 {@link #hurtServer(ServerLevel, DamageSource, float)}，
     * {@code hurt} 不再可重写。
     */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getPartWidth(), this.getPartHeight());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    private int getOwnerId() {
        return this.entityData.get(OWNER_ID);
    }

    public void setOwnerId(int ownerId) {
        this.entityData.set(OWNER_ID, ownerId);
    }

    public float getPartWidth() {
        return this.entityData.get(WIDTH);
    }

    public void setPartWidth(float scale) {
        this.entityData.set(WIDTH, scale);
    }

    public float getPartHeight() {
        return this.entityData.get(HEIGHT);
    }

    public void setPartHeight(float scale) {
        this.entityData.set(HEIGHT, scale);
    }
}