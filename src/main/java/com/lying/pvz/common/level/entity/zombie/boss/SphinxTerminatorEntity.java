package com.lying.pvz.common.level.entity.zombie.boss;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.projectile.TargetArrowEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 狮身终结者（Sphinx Terminator）— 埃及群系 BOSS。
 *
 * <p>三技能按血量阶段解锁（4 段，血越少技能越多）：
 * <ul>
 *   <li><b>召唤</b>（阶段1起）：jaw 张合动画，从下颌下方召唤僵尸；</li>
 *   <li><b>导弹</b>（阶段2起）：TargetArrow 标记植物 → 发射追踪导弹 → 命中摧毁植物并在旁侧生成 2~3 墓碑；</li>
 *   <li><b>冲刺</b>（阶段3起）：蓄力（期间被植物伤害可打断）→ 冲刺摧毁路径植物 → 返回原位。</li>
 * </ul>
 *
 * <p><b>移动</b>：不吃常规行走 AI，靠 {@link SphinxJumpGoal} 在 10 格内跳跃换格，
 * 跳跃全程免疫伤害（{@link #isZombieInvulnerableTo}）。
 *
 * <p>状态、冷却、冲刺起点、阶段均持久化 + 同步客户端供模型动画读取。
 */
public class SphinxTerminatorEntity extends AbstractBossZombieEntity {

    private static final EntityDataAccessor<Integer> STATES =
            SynchedEntityData.defineId(SphinxTerminatorEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> DASH_ORIGIN =
            SynchedEntityData.defineId(SphinxTerminatorEntity.class, EntityDataSerializers.BLOCK_POS);
    /** 冲刺朝向（度，与 getYRot 同约定），同步客户端供模型冲刺转向动画读取。 */
    private static final EntityDataAccessor<Float> DASH_YAW =
            SynchedEntityData.defineId(SphinxTerminatorEntity.class, EntityDataSerializers.FLOAT);

    /** 冲刺方向（服务端内部状态，无需同步客户端）。 */
    private Vec3 dashDir = Vec3.ZERO;
    /** 冲刺水平速度（格/tick）。 */
    private static final double DASH_SPEED = 0.55D;
    /** 冲刺持续 tick（≈10 格）。 */
    private static final int DASH_TICKS = 18;
    /** 与最近植物的最大水平距离（格）：超出则拉回，避免越打越远。 */
    private static final double MAX_PLANT_DIST = 10.0D;

    /** 已被 TargetArrow 标记、等待 3 秒后发射导弹的植物（服务端内部状态）。 */
    private final List<LivingEntity> markedPlants = new ArrayList<>();
    /** 已丢出的标记箭，导弹发射时一并清除（服务端内部状态）。 */
    private final List<TargetArrowEntity> markedArrows = new ArrayList<>();

    /** 技能冷却（tick）。 */
    protected int summonTick;
    protected int missileTick;
    protected int dashTick;
    protected int jumpTick;

    public SphinxTerminatorEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setIsWholeBody();
        this.refreshCountCD = 10;
        this.summonTick = 40;
        this.missileTick = 160;
        this.dashTick = 220;
        this.jumpTick = 40;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATES, States.NORMAL.ordinal());
        builder.define(DASH_ORIGIN, BlockPos.ZERO);
        builder.define(DASH_YAW, 0F);
    }

    @Override
    protected void registerGoals() {
        // 不注册近战/行走 AI：本 BOSS 只靠技能与跳跃移动
        this.goalSelector.addGoal(8, new SphinxJumpGoal(this));
        this.goalSelector.addGoal(5, new SphinxSummonGoal(this));
        this.goalSelector.addGoal(4, new SphinxMissileGoal(this));
        this.goalSelector.addGoal(3, new SphinxDashGoal(this));
        registerTargetGoals();
    }

    @Override
    protected void registerTargetGoals() {
        this.targetSelector.addGoal(0, new com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal(
                this, false, true, ZombieUtil.LITTLE_CLOSE_TARGET_RANGE, ZombieUtil.LITTLE_HIGH_TARGET_HEIGHT));
    }

    /* ================================================================ */
    /* 阶段 / 技能门槛                                                    */
    /* ================================================================ */

    /** 血量阶段：4→1（满血4，濒死1）。阶段越低技能越多。 */
    public int getBossStage() {
        final float percent = this.bossInfo.getProgress();
        return percent > 3F / 4 ? 4 : percent > 2F / 4 ? 3 : percent > 1F / 4 ? 2 : 1;
    }

    /** 召唤：阶段1起可用。 */
    public boolean canUseSummon() { return true; }
    /** 导弹：阶段2起（blood ≤ 75%）。 */
    public boolean canUseMissile() { return this.getBossStage() <= 3; }
    /** 冲刺：阶段3起（blood ≤ 50%）。 */
    public boolean canUseDash() { return this.getBossStage() <= 2; }

    /* ================================================================ */
    /* 状态                                                              */
    /* ================================================================ */

    public States getState() { return States.fromOrdinal(this.entityData.get(STATES)); }
    public void setState(States state) { this.entityData.set(STATES, state.ordinal()); }

    public BlockPos getDashOrigin() { return this.entityData.get(DASH_ORIGIN); }
    public void setDashOrigin(BlockPos pos) { this.entityData.set(DASH_ORIGIN, pos); }

    public Vec3 getDashDir() { return this.dashDir; }
    public void setDashDir(Vec3 dir) { this.dashDir = dir; }

    /** 冲刺朝向（度），供模型渲染冲刺转向。 */
    public float getDashYaw() { return this.entityData.get(DASH_YAW); }
    public void setDashYaw(float yaw) { this.entityData.set(DASH_YAW, yaw); }

    /** 把水平方向向量转成实体 yaw（度），与 getYRot 约定一致：+(0,0,1)=0°。 */
    private static float yawOf(Vec3 dir) {
        return (float) (Mth.atan2(-dir.x, dir.z) * Mth.RAD_TO_DEG);
    }

    public boolean isJumpImmune() { return this.getState() == States.JUMP; }
    public boolean isDashing() { return this.getState() == States.DASH; }
    public boolean isChargingDash() { return this.getState() == States.DASH_CHARGE; }

    /** 蓄力期间免疫普通攻击伤害（植物普通攻击不打断冲刺）。 */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isChargingDash() && source.getEntity() instanceof PlantEntity) {
            // 普通攻击不打断：蓄力阶段免疫植物伤害
            return false;
        }
        return super.hurtServer(level, source, damage);
    }

    /** 跳跃、冲刺全程免疫伤害（掉出世界与创造模式伤害除外，见 ZombieEntity 调用处）。 */
    @Override
    protected boolean isZombieInvulnerableTo(DamageSource source) {
        return super.isZombieInvulnerableTo(source) || this.isJumpImmune() || this.isDashing();
    }

    /* ================================================================ */
    /* 技能行为                                                          */
    /* ================================================================ */

    /** 技能1：从下颌下方召唤僵尸。 */
    public void doSummon() {
        this.setState(States.NORMAL);
        final int cnt = Math.max(1, this.bossInfo.getPlayers().size() + 1);
        BlockPos jawPos = this.blockPosition().below();
        for (int i = 0; i < cnt; ++i) {
            Mob zombie = getSummonZombie();
            if (zombie == null) continue;
            ZombieUtil.copySummonZombieData(this, (ZombieEntity) zombie);
            BlockPos pos = WorldUtil.getSuitableHeightRandomPos(this.level(), jawPos, 1, 4);
            EntityUtil.onEntitySpawn(this.level(), zombie, pos);
            ((ZombieEntity) zombie).setZombieRising();
        }
        // 频繁召唤：1.5~2.5 秒一次
        this.summonTick = 30 + this.getRandom().nextInt(20);
    }

    @Nullable
    private Mob getSummonZombie() {
        // 埃及主题：Ra 僵尸优先，混入基础僵尸
        EntityType<?> type = this.getRandom().nextInt(3) == 0
                ? PvZEntityRegister.NORMAL_ZOMBIE.get()
                : PvZEntityRegister.RA_ZOMBIE.get();
        return (Mob) type.create(this.level(), EntitySpawnReason.MOB_SUMMONED);
    }

    /** 技能2 阶段1：统计附近植物，先丢 TargetArrow 标记，3 秒后由 {@link #launchMissiles()} 发射导弹。 */
    public void doMissile() {
        this.setState(States.MISSILE);
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        this.markedPlants.clear();
        this.markedArrows.clear();
        AABB box = EntityUtil.getEntityAABB(this, 40, 40);
        List<LivingEntity> plants = EntityUtil.getTargetableLivings(this, box).stream()
                .filter(e -> e instanceof PlantEntity)
                .toList();

        // 导弹数量 = 附近植物数 / 4，向上取整且至少 1 枚
        final int count = Math.max(1, (plants.size() + 3) / 4);
        for (int i = 0; i < count && i < plants.size(); ++i) {
            LivingEntity plant = plants.get(i);
            this.markedPlants.add(plant);
            // 标记箭直接丢到目标植物头顶，精准标出 3 秒后将被攻击的位置
            TargetArrowEntity arrow =
                    PvZEntityRegister.TARGET_ARROW.get().create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
            if (arrow != null) {
                arrow.shootFrom(this);
                arrow.aimAt(plant);
                arrow.setPos(plant.getX(), plant.getY() + plant.getBbHeight() + 0.5D, plant.getZ());
                arrow.setDeltaMovement(Vec3.ZERO);
                serverLevel.addFreshEntity(arrow);
                this.markedArrows.add(arrow);
            }
        }
    }

    /** 技能2 阶段2：标记 3 秒后向已标记植物发射追踪导弹。 */
    public void launchMissiles() {
        this.setState(States.NORMAL);
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        for (LivingEntity plant : this.markedPlants) {
            if (EntityUtil.isEntityValid(plant)) {
                SphinxMissileEntity missile = new SphinxMissileEntity(serverLevel, this, plant);
                serverLevel.addFreshEntity(missile);
            }
        }
        // 导弹已发射，清除头顶的标记箭
        for (TargetArrowEntity arrow : this.markedArrows) {
            if (EntityUtil.isEntityValid(arrow)) arrow.discard();
        }
        this.markedPlants.clear();
        this.markedArrows.clear();
        this.missileTick = 200 + this.getRandom().nextInt(100);
    }

    /** 找到 40 格内最近的可攻击植物（视线/冲刺方向都以此为准）。 */
    @Nullable
    public PlantEntity findNearestPlant() {
        AABB box = EntityUtil.getEntityAABB(this, 40, 40);
        PlantEntity nearest = null;
        double bestSq = Double.MAX_VALUE;
        for (LivingEntity e : EntityUtil.getTargetableLivings(this, box)) {
            if (!(e instanceof PlantEntity plant) || !EntityUtil.canAttackEntity(this, plant)) continue;
            double sq = plant.distanceToSqr(this);
            if (sq < bestSq) { bestSq = sq; nearest = plant; }
        }
        return nearest;
    }

    /** 立即把头/身体朝向设为 yaw（同步 O 值供渲染插值，模型才会真正转向）。 */
    private void applyFacing(float yaw) {
        this.setYHeadRot(yaw);
        this.setYBodyRot(yaw);
    }

    /**
     * 让视线（头朝向）朝向最近的植物；无植物则朝向当前 target。
     * 每 tick 调用，保证平时都盯着植物。
     */
    private void faceNearestPlant(@Nullable PlantEntity plant) {
        LivingEntity target = plant != null ? plant : this.getTarget();
        if (target == null) return;
        Vec3 to = new Vec3(target.getX() - this.getX(), 0, target.getZ() - this.getZ());
        if (to.lengthSqr() < 1.0E-4) return;
        this.applyFacing(yawOf(to));
        this.setXRot(0);
    }

    /**
     * 距离约束：与最近植物的水平距离超过 10 格时，每 tick 朝植物匀速靠近，
     * 保证 BOSS 不会被跳跃/冲刺带得越来越远。
     */
    private void enforcePlantLeash(@Nullable PlantEntity plant) {
        if (plant == null) return;
        double dx = plant.getX() - this.getX();
        double dz = plant.getZ() - this.getZ();
        double distSq = dx * dx + dz * dz;
        if (distSq <= MAX_PLANT_DIST * MAX_PLANT_DIST || distSq < 1.0E-4) return;
        double dist = Math.sqrt(distSq);
        final double pullSpeed = 0.35D;
        this.setDeltaMovement(dx / dist * pullSpeed, this.getDeltaMovement().y, dz / dist * pullSpeed);
    }

    /** 技能3 起手：记录起点与冲刺方向（朝最近植物）。 */
    public void beginDashCharge() {
        this.setDashOrigin(this.blockPosition());
        // 起手瞬间锁定最近植物方向；之后冲刺全程按此固定方向推进
        PlantEntity plant = this.findNearestPlant();
        Vec3 dir;
        if (plant != null) {
            dir = new Vec3(plant.getX() - this.getX(), 0, plant.getZ() - this.getZ());
            if (dir.lengthSqr() < 1.0E-4) dir = this.getLookAngle();
            dir = dir.normalize();
        } else {
            dir = new Vec3(this.getLookAngle().x, 0, this.getLookAngle().z).normalize();
        }
        this.setDashDir(dir);
        // 同步冲刺朝向并让实体整体转向该方向（模型、位移、粒子一致）
        final float yaw = yawOf(dir);
        this.setDashYaw(yaw);
        this.setYRot(yaw);
        this.applyFacing(yaw);
        this.setState(States.DASH_CHARGE);
        this.setAttackTime(0);
    }

    /**
     * 冲刺推进（每 tick 调用）：以 DASH_SPEED 匀速移动 dir，沿途摧毁植物与喷粒子。
     * 不用一次性 setDeltaMovement，因为地面摩擦每 tick 会把水平速度衰减掉。
     */
    public void doDashStep() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        Vec3 dir = this.getDashDir();
        // 冲刺全程头/身体锁定冲刺方向，模型才不会朝错
        this.applyFacing(this.getDashYaw());
        // 恒定水平速度 + 保留竖直分量（贴地）
        this.setDeltaMovement(dir.x * DASH_SPEED, this.getDeltaMovement().y, dir.z * DASH_SPEED);
        BlockPos p = this.blockPosition();
        AABB column = new AABB(
                new Vec3(p.getX() - 1, p.getY() - 2, p.getZ() - 1),
                new Vec3(p.getX() + 2, p.getY() + 5, p.getZ() + 2));
        serverLevel.getEntitiesOfClass(PlantEntity.class, column, e -> EntityUtil.canAttackEntity(this, e))
                .forEach(plant -> plant.kill(serverLevel));
        serverLevel.sendParticles(ParticleTypes.EXPLOSION, p.getX(), p.getY() + 1, p.getZ(),
                2, 0.5, 0.5, 0.5, 0.01);
    }

    /** 冲刺结束：回原位（若原位离植物超过 10 格，则改落在植物 10 格圈内）。 */
    public void finishDash() {
        BlockPos origin = this.getDashOrigin();
        if (!origin.equals(BlockPos.ZERO) && this.level() instanceof ServerLevel sl) {
            BlockPos safe = WorldUtil.getSuitableHeightPos(sl, origin);
            PlantEntity plant = this.findNearestPlant();
            if (plant != null) {
                double dx = safe.getX() + 0.5D - plant.getX();
                double dz = safe.getZ() + 0.5D - plant.getZ();
                double d = Math.sqrt(dx * dx + dz * dz);
                if (d > MAX_PLANT_DIST && d > 1.0E-4) {
                    double k = MAX_PLANT_DIST / d;
                    BlockPos clamped = new BlockPos(
                            (int) Math.floor(plant.getX() + dx * k),
                            safe.getY(),
                            (int) Math.floor(plant.getZ() + dz * k));
                    safe = WorldUtil.getSuitableHeightPos(sl, clamped);
                }
            }
            this.setPos(safe.getX() + 0.5, safe.getY() + 1, safe.getZ() + 0.5);
            sl.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + 1, this.getZ(),
                    16, 0.6, 1.0, 0.6, 0.02);
        }
        this.setDeltaMovement(Vec3.ZERO);
        this.setState(States.NORMAL);
        this.setAttackTime(0);
        this.dashTick = 320;
    }

    /** 跳跃：10 格内跳到旁边格子（落点被钳制在植物 10 格内），竖直初速度按 5 格高度计算（全程免疫）。 */
    public void doJump() {
        BlockPos target = WorldUtil.getSuitableHeightRandomPos(this.level(), this.blockPosition(), 2, 10);
        // 落点若离最近植物超过 10 格，钳制到植物 10 格圈内，避免越跳越远
        PlantEntity plant = this.findNearestPlant();
        if (plant != null) {
            double dx = target.getX() - plant.getX();
            double dz = target.getZ() - plant.getZ();
            double d = Math.sqrt(dx * dx + dz * dz);
            if (d > MAX_PLANT_DIST && d > 1.0E-4) {
                double k = MAX_PLANT_DIST / d;
                target = new BlockPos(
                        (int) Math.floor(plant.getX() + dx * k),
                        target.getY(),
                        (int) Math.floor(plant.getZ() + dz * k));
            }
        }
        Vec3 delta = new Vec3(target.getX() - this.getX(), 0, target.getZ() - this.getZ());
        double dist = delta.length();
        if (dist > 10 || dist < 1) { this.jumpTick = 30; return; }
        // 5 格高度对应竖直初速度：v = sqrt(2*g*h)，重力按 0.08/tick²、h=5 估算
        final double jumpVy = Math.sqrt(2 * 0.08 * 5.0);
        this.setDeltaMovement(delta.normalize().scale(0.55).add(0, jumpVy, 0));
        this.setState(States.JUMP);
        this.jumpTick = 60 + this.getRandom().nextInt(60);
    }

    @Override
    public void zombieTick() {
        super.zombieTick();
        if (this.level().isClientSide()) return;

        States state = this.getState();
        PlantEntity nearest = this.findNearestPlant();
        // 视线始终盯着最近植物（蓄力/冲刺时方向由 dashYaw 锁定，不在此覆盖）
        if (state != States.DASH_CHARGE && state != States.DASH) {
            this.faceNearestPlant(nearest);
        }

        // 跳跃落地检测
        if (state == States.JUMP && this.onGround() && this.tickCount > 4) {
            this.setState(States.NORMAL);
            this.setDeltaMovement(Vec3.ZERO);
        }
        // 冲刺推进：每 tick 匀速移动并摧毁沿途植物（由 Goal 计时，上限 DASH_TICKS）
        if (state == States.DASH) {
            this.doDashStep();
        } else if (state != States.DASH_CHARGE) {
            // 与植物距离超过 10 格时匀速靠近，避免越打越远（蓄力期锁定不动）
            this.enforcePlantLeash(nearest);
        }
    }

    /* ================================================================ */
    /* BOSS 基础属性                                                      */
    /* ================================================================ */

    @Override
    public IZombieType getZombieType() { return RoofZombies.SPHINX_TERMINATOR.get(); }

    @Override
    protected float getLife() { return 1500; }

    @Override
    protected float getInnerLife() { return 6000; }

    @Override
    public float getWalkSpeed() { return 0; }

    @Override
    public float getEatDamage() { return ZombieUtil.LITTLE_HIGH; }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(2.5F, 3.0F); }

    @Override
    public boolean canPAZTarget(Entity target) { return true; }

    /* ================================================================ */
    /* 存档                                                              */
    /* ================================================================ */

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setState(States.fromOrdinal(input.getIntOr("sphinx_state", States.NORMAL.ordinal())));
        this.summonTick = input.getIntOr("sphinx_summon_tick", 40);
        this.missileTick = input.getIntOr("sphinx_missile_tick", 160);
        this.dashTick = input.getIntOr("sphinx_dash_tick", 220);
        this.jumpTick = input.getIntOr("sphinx_jump_tick", 40);
        this.setDashOrigin(new BlockPos(
                input.getIntOr("sphinx_origin_x", 0),
                input.getIntOr("sphinx_origin_y", 0),
                input.getIntOr("sphinx_origin_z", 0)));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("sphinx_state", this.getState().ordinal());
        output.putInt("sphinx_summon_tick", this.summonTick);
        output.putInt("sphinx_missile_tick", this.missileTick);
        output.putInt("sphinx_dash_tick", this.dashTick);
        output.putInt("sphinx_jump_tick", this.jumpTick);
        output.putInt("sphinx_origin_x", this.getDashOrigin().getX());
        output.putInt("sphinx_origin_y", this.getDashOrigin().getY());
        output.putInt("sphinx_origin_z", this.getDashOrigin().getZ());
    }

    /** 状态枚举。顺序即同步 ordinal，末尾追加，勿改序。 */
    public enum States {
        NORMAL, SUMMON, MISSILE, DASH_CHARGE, DASH, JUMP;

        public static States fromOrdinal(int ordinal) {
            States[] v = values();
            return ordinal >= 0 && ordinal < v.length ? v[ordinal] : NORMAL;
        }
    }

    /* ================================================================ */
    /* 技能 Goals                                                        */
    /* ================================================================ */

    /** 蓄力冲刺：DASH_CHARGE 蓄力 60 tick → DASH 匀速冲刺 DASH_TICKS tick → 回原位。 */
    protected static class SphinxDashGoal extends Goal {
        private final SphinxTerminatorEntity boss;
        private int dashElapsed;

        SphinxDashGoal(SphinxTerminatorEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (--boss.dashTick > 0) return false;
            return boss.getTarget() != null && boss.canUseDash() && boss.getState() == States.NORMAL;
        }

        @Override
        public boolean canContinueToUse() {
            States s = boss.getState();
            return s == States.DASH_CHARGE || s == States.DASH;
        }

        @Override
        public void start() {
            boss.beginDashCharge();
            dashElapsed = 0;
        }

        @Override
        public void tick() {
            States s = boss.getState();
            if (s == States.DASH_CHARGE) {
                boss.setAttackTime(boss.getAttackTime() + 1);
                // 蓄力全程锁定冲刺方向（最近植物）
                boss.applyFacing(boss.getDashYaw());
                if (boss.getAttackTime() > 60) {
                    boss.setState(States.DASH);
                    boss.setAttackTime(0);
                    dashElapsed = 0;
                }
            } else if (s == States.DASH) {
                // 推进由 zombieTick 的 doDashStep 每 tick 执行，这里只计时
                if (++dashElapsed > DASH_TICKS) {
                    boss.finishDash();
                }
            }
        }

        @Override
        public void stop() {
            dashElapsed = 0;
        }
    }

    /** 召唤：SUMMON 状态 jaw 张合，attackTime 到阈值后从下颌召唤。 */
    protected static class SphinxSummonGoal extends Goal {
        private final SphinxTerminatorEntity boss;

        SphinxSummonGoal(SphinxTerminatorEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (--boss.summonTick > 0) return false;
            // 召唤不设血量/僵尸数量门槛：任何阶段都生效，仅由冷却驱动
            return boss.getState() == States.NORMAL && boss.canUseSummon();
        }

        @Override
        public boolean canContinueToUse() { return boss.getState() == States.SUMMON; }

        @Override
        public void start() {
            boss.setState(States.SUMMON);
            boss.setAttackTime(0);
        }

        @Override
        public void tick() {
            boss.setAttackTime(boss.getAttackTime() + 1);
            if (boss.getAttackTime() > 24) boss.doSummon();
        }

        @Override
        public void stop() { boss.setAttackTime(0); }
    }

    /** 导弹：MISSILE 状态蓄力 30 tick 后丢标记箭，再等 3 秒（60 tick）发射导弹。 */
    protected static class SphinxMissileGoal extends Goal {
        private static final int MARK_TICK = 30;
        private static final int LAUNCH_DELAY = 60; // 3 秒
        private final SphinxTerminatorEntity boss;

        SphinxMissileGoal(SphinxTerminatorEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (--boss.missileTick > 0) return false;
            return boss.getTarget() != null && boss.canUseMissile() && boss.getState() == States.NORMAL;
        }

        @Override
        public boolean canContinueToUse() { return boss.getState() == States.MISSILE; }

        @Override
        public void start() {
            boss.setState(States.MISSILE);
            boss.setAttackTime(0);
        }

        @Override
        public void tick() {
            final int t = boss.getAttackTime() + 1;
            boss.setAttackTime(t);
            if (t == MARK_TICK) boss.doMissile();
            if (t >= MARK_TICK + LAUNCH_DELAY) boss.launchMissiles();
        }

        @Override
        public void stop() { boss.setAttackTime(0); }
    }

    /** 跳跃移动：10 格内跳格，落地后冷却。 */
    protected static class SphinxJumpGoal extends Goal {
        private final SphinxTerminatorEntity boss;

        SphinxJumpGoal(SphinxTerminatorEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (--boss.jumpTick > 0) return false;
            return boss.getState() == States.NORMAL && boss.onGround();
        }

        @Override
        public boolean canContinueToUse() { return boss.getState() == States.JUMP; }

        @Override
        public void start() { boss.doJump(); }

        @Override
        public void stop() { }
    }
}
