package com.lying.pvz.common.level.entity.bowling;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.entity.projectile.ProjectileTrajectory;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.projectile.AbstractPAZProjectile;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * 保龄球类弹药基类。
 *
 * <p>与普通 {@link AbstractPAZProjectile} 的差异：
 * <ul>
 *   <li>移动方向由 {@link #getDirection()} + {@link #getBowlingFacing()} 决定，
 *       不靠 {@code deltaMovement} 的自然物理；</li>
 *   <li>撞实体：造成伤害后换向，不消失；</li>
 *   <li>撞方块：第一次换向并给 15 tick 冷却，冷却内再撞墙才 {@code discard}；</li>
 *   <li>生命周期、owner、group、技能/附魔快照仍走 {@link AbstractPAZProjectile}。</li>
 * </ul>
 *
 * <p>具体伤害与特殊行为由子类实现 {@link #dealDamageTo(Entity)}，
 * 需要爆炸等特殊换向时覆写 {@link #changeDirection()}。
 *
 * <p><b>移动与命中检测的分离</b>：
 * <ul>
 *   <li>{@link #applyBowlingMotion()}：按朝向算 {@code deltaMovement}，不移动；</li>
 *   <li>{@link #tickBowlingRayTrace()}：<b>射线</b>检测前方 1 帧距离内的实体，命中就伤害 + 换向；</li>
 *   <li>{@link #tickBowlingMove()}：<b>预判</b>本帧 {@code pos → pos+delta} 是否穿墙，
 *       命中就不移动、让 {@link #handleBlockHit()} 处理；否则 {@code setPos}；</li>
 *   <li>{@link #tickCollision()}：AABB 兜底，拾取与已占据包围盒重叠的实体。</li>
 * </ul>
 *
 * <p><b>客户端</b>：只跑 {@link #tickBowlingMove()} 的移动部分，不跑射线、不跑伤害。
 * 撞墙时客户端也停下，等服务端同步新朝向——比"客户端穿墙、服务端事后拉回"更平滑。
 */
public abstract class AbstractBowlingProjectile extends AbstractPAZProjectile {

    private static final EntityDataAccessor<Integer> FACING =
            SynchedEntityData.defineId(AbstractBowlingProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DIRECTION =
            SynchedEntityData.defineId(AbstractBowlingProjectile.class, EntityDataSerializers.INT);

    /** 换向后 10 tick 内不再换向，避免同帧反复换。 */
    protected int bowlingTick = 0;
    /** 撞墙后 15 tick 内再撞墙则销毁。 */
    protected int wallTick = 0;
    protected int hitCount = 0;
    protected boolean playSpawnSound = false;
    /** 垂直下落速度累积，用于从高处/悬崖滚落时平滑贴地。 */
    protected double fallVelocity = 0D;

    protected AbstractBowlingProjectile(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    @Override
    public net.minecraft.world.entity.EntityDimensions getDimensions(net.minecraft.world.entity.Pose pose) {
        // 保龄球是贴地滚动的"球"，用约 1 格的方形尺寸（默认 Entity 尺寸 0.6×1.8 过高，
        // 导致球悬空/碰撞盒与视觉不匹配）。GiantNut 等大球子类可覆写为更大尺寸。
        return net.minecraft.world.entity.EntityDimensions.scalable(1.0F, 1.0F);
    }

    /* ================================================================== */
    /* 同步数据                                                            */
    /* ================================================================== */

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FACING, BowlingFacing.MID.ordinal());
        builder.define(DIRECTION, Direction.NORTH.ordinal());
    }

    /* ================================================================== */
    /* 运动学                                                              */
    /* ================================================================== */

    @Override
    public ProjectileTrajectory getTrajectory() {
        return ProjectileTrajectory.DIRECT;
    }

    @Override
    public boolean isHoming() {
        return false;
    }

    @Override
    public boolean isBallistic() {
        return false;
    }

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public float getBulletSpeed() {
        return 0.3F;
    }

    /**
     * 保龄球覆写整个 {@code tick}：不走 {@link AbstractPAZProjectile#tickImpact} 的
     * "命中即消失"流程，改为"命中换向"。
     *
     * <p>执行顺序刻意设计为：
     * <ol>
     *   <li>{@code applyBowlingMotion}：只更新朝向与 {@code deltaMovement}，不移动</li>
     *   <li>{@code tickBowlingRayTrace}：从当前位置向前探查实体，命中即伤害换向</li>
     *   <li>{@code tickBowlingMove}：预判墙并移动</li>
     *   <li>{@code tickCollision}：AABB 兜底</li>
     * </ol>
     */
    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            this.tickBowlingMove();
            return;
        }
        if (this.tickCount >= this.getMaxLiveTick()) {
            this.onExpire();
            this.discard();
            return;
        }
        if (this.tickCount <= 10 && !this.playSpawnSound) {
            EntityUtil.playSound(this, PvZSoundRegister.BOWLING.get());
            this.playSpawnSound = true;
        }

        this.applyBowlingMotion();
        this.tickBowlingRayTrace();
        this.tickBowlingMove();
        this.tickCollision();

        if (this.bowlingTick > 0) --this.bowlingTick;
        if (this.wallTick > 0) --this.wallTick;
    }

    /** 根据当前朝向设置 {@code deltaMovement}。不移动实体本身。 */
    private void applyBowlingMotion() {
        this.yRotO = this.getYRot();
        this.setYRot(this.getDirection().toYRot() + this.getBowlingFacing().offset);
        final double angle = this.getYRot() * Math.PI / 180.0D;
        final double dx = -Math.sin(angle);
        final double dz = Math.cos(angle);
        final double speed = this.getBulletSpeed();
        // Y 分量恒为 0：垂直位移完全由 tickBowlingMove 末尾的贴地逻辑控制，
        // 避免球在地形高低差处悬空滚动。
        this.setDeltaMovement(dx * speed, 0D, dz * speed);
    }

    /**
     * 射线检测前方实体。射线长度取 {@code speed*2 + 0.2}（约 1 帧距离 + 余量），
     * 避免"提前几格就开始伤害"。
     *
     * <p><b>关键</b>：方向与 {@link #applyBowlingMotion()} 一致，用 {@code (-sin, cos)}。
     * 之前这里误用了 {@code (sin, -cos)}，射线朝身后飞，命中判定全反。
     */
    private void tickBowlingRayTrace() {
        final double rayLen = this.getBulletSpeed() * 2.0D + 0.2D;
        final double angle = this.getYRot() * Math.PI / 180.0D;
        final Vec3 start = this.position();
        final Vec3 end = start.add(-Math.sin(angle) * rayLen, 0D, Math.cos(angle) * rayLen);

        // 方块 clip 只用来截短实体射线终点，防止穿墙打墙后敌人
        HitResult blockHit = this.level().clip(new ClipContext(start, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        Vec3 clippedEnd = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation() : end;

        EntityHitResult entityHit = this.rayTraceEntities(start, clippedEnd);
        if (entityHit == null) {
            return;
        }
        Entity target = entityHit.getEntity();
        if (!this.shouldHit(target)) {
            return;
        }

        target.setInvulnerableTime(0);
        this.dealDamageTo(target);
        this.addHitEntity(target);
        ++this.hitCount;
        this.changeDirection();
    }

    /**
     * 预判本帧是否穿墙。两端都执行：
     * <ul>
     *   <li>服务端：命中就调 {@link #handleBlockHit()}（换向 / 销毁）；</li>
     *   <li>客户端：命中就停下不移动，等服务端同步 {@code FACING}。</li>
     * </ul>
     *
     * <p>原实现直接 {@code setPos} 并依赖 {@code horizontalCollision}——但 {@code setPos}
     * 不更新该字段，撞墙冷却逻辑从未生效。现在改用 {@code clip} 主动判定。
     */
    protected void tickBowlingMove() {
        final Vec3 motion = this.getDeltaMovement();
        final Vec3 from = this.position();
        final Vec3 to = from.add(motion);

        BlockHitResult blockHit = this.level().clip(new ClipContext(
                from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHit.getType() != HitResult.Type.MISS) {
            if (!this.level().isClientSide()) {
                this.handleBlockHit();
            }
            this.snapToGround();
            return;
        }

        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE,
                        to.x - motion.x * 0.25D, to.y - motion.y * 0.25D, to.z - motion.z * 0.25D,
                        motion.x, motion.y, motion.z);
            }
        }
        this.setPos(to.x, to.y, to.z);
        // 水平移动后把球对齐到下方支撑面，处理高低差（下坡下落 / 上坡抬升）。
        this.snapToGround();
    }

    /**
     * 把球体贴合到下方地形表面，避免高低差处悬空。
     * <ul>
     *   <li>下方无支撑（虚空）：受重力持续下落；</li>
     *   <li>球底高于支撑面（悬崖 / 下坡）：受重力渐进下落，不瞬移；</li>
     *   <li>球底低于或等于支撑面（上坡 / 台阶）：直接抬升贴地。</li>
     * </ul>
     */
    protected void snapToGround() {
        final double halfHeight = this.getBbHeight() / 2.0D;
        final double groundY = this.findGroundY();
        final double currentBottom = this.getY() - halfHeight;

        if (groundY == Double.NEGATIVE_INFINITY) {
            // 下方无方块（虚空 / 高空），自由下落。
            this.fallVelocity -= this.getGravityVelocity();
            this.setPos(this.getX(), this.getY() + this.fallVelocity, this.getZ());
            return;
        }

        if (currentBottom > groundY + 1.0E-3D) {
            // 悬空：受重力下落，但不超过支撑面（clamp 到贴地）。
            this.fallVelocity -= this.getGravityVelocity();
            double newBottom = currentBottom + this.fallVelocity;
            if (newBottom <= groundY) {
                newBottom = groundY;
                this.fallVelocity = 0D;
            }
            this.setPos(this.getX(), newBottom + halfHeight, this.getZ());
        } else {
            // 贴地 / 上坡抬升。
            this.setPos(this.getX(), groundY + halfHeight, this.getZ());
            this.fallVelocity = 0D;
        }
    }

    /**
     * 查找球体水平投影下方最近的固体支撑面顶面 Y（世界坐标，即球底应贴合的高度）。
     * 从球底向下逐层扫描，找不到（虚空 / 高空）返回 {@link Double#NEGATIVE_INFINITY}。
     */
    private double findGroundY() {
        final double halfHeight = this.getBbHeight() / 2.0D;
        final AABB box = this.getBoundingBox();
        final int minX = Mth.floor(box.minX);
        final int maxX = Mth.floor(box.maxX);
        final int minZ = Mth.floor(box.minZ);
        final int maxZ = Mth.floor(box.maxZ);
        final BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

        // 从球底所在层向下扫描（最多 32 层），找第一层有碰撞的水平投影。
        final int startY = Mth.floor(this.getY() - halfHeight);
        for (int y = startY; y >= startY - 32; --y) {
            boolean solid = false;
            for (int x = minX; x <= maxX && !solid; ++x) {
                for (int z = minZ; z <= maxZ && !solid; ++z) {
                    mpos.set(x, y, z);
                    if (!this.level().getBlockState(mpos)
                            .getCollisionShape(this.level(), mpos).isEmpty()) {
                        solid = true;
                    }
                }
            }
            if (solid) {
                return y + 1.0D;
            }
        }
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * 撞墙处理。
     * <ul>
     *   <li>换向冷却中（{@code bowlingTick > 0}）：原地不动，等冷却结束；</li>
     *   <li>15 tick 内二次撞墙（{@code wallTick > 0}）：销毁；</li>
     *   <li>否则：进入 15 tick 冷却并换向。</li>
     * </ul>
     */
    private void handleBlockHit() {
        if (this.bowlingTick > 0) {
            return;
        }
        if (this.wallTick > 0) {
            this.discard();
            return;
        }
        this.wallTick = 15;
        this.changeDirection();
    }

    /** AABB 兜底：拾取与本包围盒重叠的目标，处理射线漏掉的极近实体。 */
    protected void tickCollision() {
        if (this.level().isClientSide()) {
            return;
        }
        if (this.bowlingTick > 0) {
            return;
        }
        var targets = this.level().getEntities(this, this.getBoundingBox(), this::canHitEntity);
        if (!targets.isEmpty()) {
            Entity target = targets.get(0);
            target.setInvulnerableTime(0);
            this.dealDamageTo(target);
            this.addHitEntity(target);
            ++this.hitCount;
            this.changeDirection();
        }
    }

    /* ================================================================== */
    /* 命中 / 换向                                                         */
    /* ================================================================== */

    /**
     * 基类 {@code tickImpact} 的钩子——保龄球不复用其流程。
     * 实体命中在 {@link #tickBowlingRayTrace} 中处理，方块命中在
     * {@link #tickBowlingMove} 中处理，这里是空实现。
     */
    @Override
    protected void onImpact(HitResult result) {
        // no-op
    }

    /**
     * 换向：MID 随机左右，LEFT/RIGHT 互翻，BOMB 保持。换向后 10 tick 内不再换向，
     * 避免同一帧反复换。
     */
    protected void changeDirection() {
        switch (this.getBowlingFacing()) {
            case MID -> this.setBowlingFacing(this.random.nextBoolean() ? BowlingFacing.LEFT : BowlingFacing.RIGHT);
            case LEFT -> this.setBowlingFacing(BowlingFacing.RIGHT);
            case RIGHT -> this.setBowlingFacing(BowlingFacing.LEFT);
            case BOMB -> {
            }
        }
        this.bowlingTick = 10;
    }

    /** 具体伤害，由子类实现。 */
    protected abstract void dealDamageTo(Entity entity);

    /* ================================================================== */
    /* 发射                                                                */
    /* ================================================================== */

    /**
     * 玩家投掷：方向来自玩家朝向，阵营视作植物。
     *
     * <p>与 {@link AbstractPAZProjectile#shootFrom} 对齐地初始化快照：
     * {@link #shootOrigin}、{@link #pierceLeft}、{@link #bulletSpeed}、{@link #attackDamage}、
     * owner UUID、owner group——否则 {@code shouldHit} 的阵营过滤会出错、
     * {@code createDamageSource} 丢失发射者。
     */
    public void shoot(Player player) {
        this.setDirection(player.getDirection());
        this.setYRot(this.getDirection().toYRot());
        this.initShot();
        this.setOwnerUUID(player.getUUID());
        // ⚠️ 按项目里 GroupType 的实际枚举名调整；此值决定"能打到谁"。
        this.ownerGroup = GroupType.PLANTS;
    }

    /** 从指定方向发射（非玩家来源，如机关）。 */
    public void shoot(Direction direction) {
        this.setDirection(direction);
        this.setYRot(direction.toYRot());
        this.initShot();
    }

    /** 与 {@code shootFrom} 对齐的通用快照初始化（不含阵营 / owner，由调用方决定）。 */
    private void initShot() {
        this.shootOrigin = this.position();
        this.pierceLeft = this.computePierceCount();
        this.bulletSpeed = this.computeBulletSpeed();
        this.attackDamage = this.computeAttackDamage(null);
    }

    /* ================================================================== */
    /* 状态 getter / setter                                                */
    /* ================================================================== */

    public Direction getDirection() {
        final Direction[] values = Direction.values();
        final int ord = this.entityData.get(DIRECTION);
        return (ord >= 0 && ord < values.length) ? values[ord] : Direction.NORTH;
    }

    public void setDirection(Direction direction) {
        this.entityData.set(DIRECTION, direction.ordinal());
    }

    public BowlingFacing getBowlingFacing() {
        final BowlingFacing[] values = BowlingFacing.values();
        final int ord = this.entityData.get(FACING);
        return (ord >= 0 && ord < values.length) ? values[ord] : BowlingFacing.MID;
    }

    public void setBowlingFacing(BowlingFacing facing) {
        this.entityData.set(FACING, facing.ordinal());
    }

    public int getHitCount() {
        return this.hitCount;
    }

    /* ================================================================== */
    /* NBT                                                                 */
    /* ================================================================== */

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("bowling_facing", this.getBowlingFacing().ordinal());
        output.putInt("bowling_direction", this.getDirection().ordinal());
        output.putInt("bowling_tick", this.bowlingTick);
        output.putInt("wall_tick", this.wallTick);
        output.putInt("hit_count", this.hitCount);
        output.putBoolean("play_spawn_sound", this.playSpawnSound);
        output.putDouble("fall_velocity", this.fallVelocity);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        final BowlingFacing[] facings = BowlingFacing.values();
        final int facingOrd = input.getIntOr("bowling_facing", BowlingFacing.MID.ordinal());
        this.setBowlingFacing((facingOrd >= 0 && facingOrd < facings.length)
                ? facings[facingOrd] : BowlingFacing.MID);

        final Direction[] dirs = Direction.values();
        final int dirOrd = input.getIntOr("bowling_direction", Direction.NORTH.ordinal());
        this.setDirection((dirOrd >= 0 && dirOrd < dirs.length) ? dirs[dirOrd] : Direction.NORTH);

        this.bowlingTick = input.getIntOr("bowling_tick", 0);
        this.wallTick = input.getIntOr("wall_tick", 0);
        this.hitCount = input.getIntOr("hit_count", 0);
        this.playSpawnSound = input.getBooleanOr("play_spawn_sound", false);
        this.fallVelocity = input.getDoubleOr("fall_velocity", 0D);
    }

    /* ================================================================== */
    /* 朝向枚举                                                            */
    /* ================================================================== */

    /**
     * 保龄球横向偏角。
     * <ul>
     *   <li>{@link #LEFT} / {@link #RIGHT}：相对基础方向偏 ±45°；</li>
     *   <li>{@link #MID}：正前方，换向时随机左右；</li>
     *   <li>{@link #BOMB}：爆炸保龄球专用，换向不生效。</li>
     * </ul>
     */
    public enum BowlingFacing {
        LEFT(-45F),
        MID(0F),
        RIGHT(45F),
        BOMB(0F);

        public final float offset;

        BowlingFacing(float offset) {
            this.offset = offset;
        }
    }
}