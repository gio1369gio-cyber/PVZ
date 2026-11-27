package com.lying.pvz.common.level.entity.zombie.boss;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.CoinEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.projectile.TargetArrowEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.common.level.entity.zombie.roof.ImpEntity;
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
import net.minecraft.world.entity.player.Player;
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
 * 四足机甲（Zombot）BOSS 基类。
 *
 * <p>植物大战僵尸 2 的四个时空 BOSS——狮身终结者（Sphinx-inator）、甲板漫步者（Plank Walker）、
 * 牛车格斗者（War Wagon）、明日破译者（Tomorrow-tron）——共用同一套四足机甲躯体，
 * 仅「头部的装饰部件」与「召唤的僵尸」不同（见 {@link ZombotVariant}）。
 *
 * <p><b>通用技能</b>（各子类行为基本一致，仅在召唤池等细节上有差别）：
 * <ul>
 *   <li><b>召唤</b>：随血量阶段升级召唤的僵尸种类，第一阶段召唤基础/主题僵尸，后续阶段加入更强单位。</li>
 *   <li><b>导弹</b>：瞄准任意植物发射导弹，命中后在目标位置制造地形障碍（墓碑等）。
 *       给被瞄准的植物使用能量豆大招可使其失去目标。</li>
 *   <li><b>毁灭冲撞</b>：向所在两行发起冲锋，碾压路径上的所有生物（植物、僵尸、玩家等），可被植物大招打断。</li>
 *   <li><b>跳跃</b>：跳到其他行，落点压死植物、僵尸及其它生物。</li>
 * </ul>
 *
 * <p>总血量由「外防御 + 内防御」组成，具体数值由子类通过 {@link #getLife()} / {@link #getInnerLife()} 决定。
 */
public abstract class AbstractZombotEntity extends AbstractBossZombieEntity {

    private static final EntityDataAccessor<Integer> STATES =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> DASH_ORIGIN =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.BLOCK_POS);
    /** 冲刺朝向（度，与 getYRot 同约定），同步客户端供模型冲刺转向动画读取。 */
    private static final EntityDataAccessor<Float> DASH_YAW =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.FLOAT);
    /** ANGRY 动画播放中（同步客户端供渲染器读取；等其它动作结束后播放，播放期间阻止新技能抢占）。 */
    private static final EntityDataAccessor<Boolean> ANGRY_PLAYING =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.BOOLEAN);
    /**
     * 模型动画时钟（tick，服务端维护并同步）：未暂停时每 tick 递增，暂停时冻结。
     * 客户端用它代替本地 ageInTicks 播放 Blockbench 动画，暂停期间画面定格、恢复后接着播。
     */
    private static final EntityDataAccessor<Integer> ANIM_CLOCK =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.INT);
    /** 模型动画暂停开关（由服务端状态设置并同步客户端）。 */
    private static final EntityDataAccessor<Boolean> ANIM_PAUSED =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.BOOLEAN);
    /**
     * 召唤动画相位：0 无 / 1 张嘴（0→0.75s）/ 2 冻结（jaw 停 0.75s，显示漩涡）/ 3 闭嘴（0.75→1.25s）。
     * 同步客户端，驱动漩涡显隐与专用动画时钟。
     */
    private static final EntityDataAccessor<Integer> SUMMON_PHASE =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.INT);
    /** 召唤动画专用时钟（tick，冻结时停止推进），同步客户端。 */
    private static final EntityDataAccessor<Integer> SUMMON_TICK =
            SynchedEntityData.defineId(AbstractZombotEntity.class, EntityDataSerializers.INT);

    /** ANGRY 单次播放时长（tick，约 2 秒）。 */
    private static final int ANGRY_TICKS = 40;
    /** 血量下降沿阈值：跨过 70% / 50% / 30% 时各排队播放一次 ANGRY。 */
    private static final float[] ANGRY_THRESHOLDS = {0.7F, 0.5F, 0.3F};

    /* ---- SUMMON 动画分段时序（与 Blockbench SUMMON 动画 1.25s 对齐）---- */
    /** 张嘴段 tick：0→0.75s（15 tick），jaw 张到最大时冻结。 */
    private static final int SUMMON_FREEZE_TICK = 15;
    /** 冻结段等待 tick：约 1.25s，等召唤僵尸接近破土完成（RISING_CD=30 tick）。 */
    private static final int SUMMON_VORTEX_TICKS = 25;
    /** 闭嘴段结束 tick：0.75→1.25s（总 25 tick），之后回 NORMAL。 */
    private static final int SUMMON_END_TICK = 25;

    /** 冲刺方向（服务端内部状态，无需同步客户端）。 */
    private Vec3 dashDir = Vec3.ZERO;
    /** 冲刺水平速度（格/tick）。 */
    private static final double DASH_SPEED = 0.55D;
    /** 冲刺持续 tick（≈10 格）。 */
    private static final int DASH_TICKS = 18;
    /** 毁灭冲撞冷却（tick）：从起手开始计时，防止冲撞被打断或杀死玩家后立即重入。 */
    private static final int DASH_COOLDOWN = 320;
    /** 与最近植物的最大水平距离（格）：超出则拉回，避免越打越远。 */
    private static final double MAX_PLANT_DIST = 10.0D;
    /** 跳跃蓄力时长（tick）：先明显下蹲弯腿，再真正起跳。 */
    private static final int JUMP_CHARGE_TICKS = 20;

    /** 已被 TargetArrow 标记、等待 3 秒后发射导弹的植物（服务端内部状态）。 */
    private final List<LivingEntity> markedPlants = new ArrayList<>();
    /** 已丢出的标记箭，导弹发射时一并清除（服务端内部状态）。 */
    private final List<TargetArrowEntity> markedArrows = new ArrayList<>();

    /** 技能冷却（tick）。 */
    protected int summonTick;
    protected int missileTick;
    protected int dashTick;
    protected int jumpTick;
    /** 小鬼炮冷却（甲板漫步者专用，其余 BOSS 不使用）。 */
    protected int impCannonTick;

    /** 已跨过的 ANGRY 血量阈值索引（-1 表示 70% 已过、0 表示 50% 已过、1 表示 30% 已过、2 表示全过）。 */
    private int angryThresholdIndex = -1;
    /** 待播放的 ANGRY 次数队列（下降沿各入队一次）。 */
    private int angryQueue;
    /** 当前 ANGRY 播放剩余 tick（服务端内部状态）。 */
    private int angryPlayTick;
    /** SUMMON 冻结段已等待 tick（服务端内部状态，无需同步客户端）。 */
    private int summonFreezeWait;

    public AbstractZombotEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setIsWholeBody();
        this.refreshCountCD = 10;
        this.summonTick = 40;
        this.missileTick = 160;
        this.dashTick = 220;
        this.jumpTick = 40;
        this.impCannonTick = 120;
    }

    /** BOSS 死亡掉落的 PvZ 金币数量，每枚 GOLD 金币 = 100 钱。 */
    private static final int BOSS_GOLD_COIN_COUNT = 20;

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.level().isClientSide()) {
            return;
        }
        for (int i = 0; i < BOSS_GOLD_COIN_COUNT; i++) {
            CoinEntity.spawnCoin(this.level(), this.blockPosition(), CoinEntity.CoinType.GOLD);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATES, States.NORMAL.ordinal());
        builder.define(DASH_ORIGIN, BlockPos.ZERO);
        builder.define(DASH_YAW, 0F);
        builder.define(ANGRY_PLAYING, false);
        builder.define(ANIM_CLOCK, 0);
        builder.define(ANIM_PAUSED, false);
        builder.define(SUMMON_PHASE, 0);
        builder.define(SUMMON_TICK, 0);
    }

    @Override
    protected void registerGoals() {
        // 不注册近战/行走 AI：本 BOSS 只靠技能与跳跃移动
        this.goalSelector.addGoal(8, new ZombotJumpGoal(this));
        this.goalSelector.addGoal(5, new ZombotSummonGoal(this));
        this.goalSelector.addGoal(4, new ZombotMissileGoal(this));
        this.goalSelector.addGoal(4, new ZombotImpCannonGoal(this));
        this.goalSelector.addGoal(3, new ZombotDashGoal(this));
        registerTargetGoals();
    }

    @Override
    protected void registerTargetGoals() {
        this.targetSelector.addGoal(0, new com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal(
                this, false, true, ZombieUtil.LITTLE_CLOSE_TARGET_RANGE, ZombieUtil.LITTLE_HIGH_TARGET_HEIGHT));
    }

    /* ================================================================ */
    /* 子类差异化钩子                                                     */
    /* ================================================================ */

    /** 该 BOSS 对应的僵尸类型（决定图鉴/卡牌等元数据）。 */
    @Override
    public abstract IZombieType getZombieType();

    /** 该 BOSS 的外观变体（决定共享四足机甲模型显示哪些装饰部件）。 */
    public abstract ZombotVariant getZombotVariant();

    /** 召唤的僵尸种类：子类按各自时空主题返回对应僵尸。 */
    @Nullable
    protected abstract Mob getSummonZombie();

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
    /** 导弹：阶段2起（blood ≤ 75%）。甲板漫步者覆盖为 false（改用小鬼炮）。 */
    public boolean canUseMissile() { return this.getBossStage() <= 3; }
    /** 冲刺：阶段3起（blood ≤ 50%）。 */
    public boolean canUseDash() { return this.getBossStage() <= 2; }
    /** 小鬼炮：仅甲板漫步者可用（替代导弹技能）。 */
    public boolean canUseImpCannon() { return false; }

    /* ================================================================ */
    /* 状态                                                              */
    /* ================================================================ */

    public States getState() { return States.fromOrdinal(this.entityData.get(STATES)); }
    public void setState(States state) { this.entityData.set(STATES, state.ordinal()); }

    /** ANGRY 动画是否正在播放（供渲染器映射为「愤怒」动画）。 */
    public boolean isAngryPlaying() { return this.entityData.get(ANGRY_PLAYING); }
    private void setAngryPlaying(boolean playing) { this.entityData.set(ANGRY_PLAYING, playing); }

    /**
     * 暂停/恢复模型动画（服务端调用；客户端只读）。暂停时动画时钟冻结，画面定格当前帧；
     * 恢复后从冻结点继续，不会跳帧。注意：暂停只影响动画表现，不影响实体 AI/技能/位移。
     */
    public void setAnimPaused(boolean paused) { this.entityData.set(ANIM_PAUSED, paused); }
    public boolean isAnimPaused() { return this.entityData.get(ANIM_PAUSED); }
    /** 当前动画时钟（毫秒），供渲染层直接传给 Blockbench 动画。 */
    public long getAnimClockMillis() { return this.entityData.get(ANIM_CLOCK) * 50L; }

    /** SUMMON 相位：0 无 / 1 张嘴 / 2 冻结显漩涡 / 3 闭嘴。 */
    public int getSummonPhase() { return this.entityData.get(SUMMON_PHASE); }
    private void setSummonPhase(int phase) { this.entityData.set(SUMMON_PHASE, phase); }
    /** SUMMON 专用动画时钟（毫秒），冻结期间保持不变以定格 jaw。 */
    public long getSummonAnimMillis() { return this.entityData.get(SUMMON_TICK) * 50L; }
    private int getSummonTick() { return this.entityData.get(SUMMON_TICK); }
    private void setSummonTick(int tick) { this.entityData.set(SUMMON_TICK, tick); }

    /**
     * 推进 ANGRY 动画：血量跨过 70/50/30 下降沿时各入队一次；
     * 仅当实体处于 NORMAL（无其它动作）时才依次播放，每次约 2 秒。
     */
    private void tickAngryAnimation() {
        // BOSS 血量是「内防御 + 外血」复合：伤害先扣内防御（占约 80%），
        // 期间原版 getHealth()/getMaxHealth() 恒为 1.0，70/50/30 阈值永远跨不过。
        // 必须用与 BossBar 一致的真实总血量比例 getCurrentHealth()/getCurrentMaxHealth()。
        final float healthPercent = (float) (this.getCurrentHealth() / Math.max(1D, this.getCurrentMaxHealth()));
        // 下降沿：血量降到阈值以下且该阈值尚未跨过，则入队并推进索引
        while (this.angryThresholdIndex + 1 < ANGRY_THRESHOLDS.length
                && healthPercent <= ANGRY_THRESHOLDS[this.angryThresholdIndex + 1]) {
            this.angryThresholdIndex++;
            this.angryQueue++;
        }
        // 计时推进必须在 state 判断之外：ANGRY 播放中一旦被技能抢占导致 state 非 NORMAL，
        // 若把递减放进 NORMAL 分支，计时器会卡住、ANGRY_PLAYING 永远为 true。
        if (this.angryPlayTick > 0) {
            if (--this.angryPlayTick <= 0) this.setAngryPlaying(false);
            return;
        }
        // 仅在空闲状态启动新的 ANGRY：其它动作优先，ANGRY 排队等待。
        // 播放期间 isAngryPlaying()=true，各技能 Goal 的 canUse() 会被挡住，
        // 保证这 40 tick 内 state 保持 NORMAL、ANGRY 能完整播完。
        if (this.angryQueue > 0 && this.getState() == States.NORMAL) {
            this.angryQueue--;
            this.angryPlayTick = ANGRY_TICKS;
            this.setAngryPlaying(true);
        }
    }

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

    public boolean isJumpImmune() { return this.getState() == States.JUMP || this.getState() == States.JUMP_CHARGE; }
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

    /** 跳跃、冲刺全程免疫伤害（掉出世界与创造模式伤害除外，见 PVZZombieEntity 调用处）。 */
    @Override
    protected boolean isZombieInvulnerableTo(DamageSource source) {
        return super.isZombieInvulnerableTo(source) || this.isJumpImmune() || this.isDashing();
    }

    /* ================================================================ */
    /* 技能行为                                                          */
    /* ================================================================ */

    /** 技能1：从下颌下方召唤僵尸（立即生成并结束 SUMMON，供外部直接触发）。 */
    public void doSummon() {
        this.spawnSummonZombies();
        this.finishSummon();
    }

    /** 真正生成僵尸并重置召唤冷却：在 jaw 张到最大（冻结起点）时调用。 */
    private void spawnSummonZombies() {
        final int cnt = Math.max(1, this.bossInfo.getPlayers().size() + 1);
        BlockPos jawPos = this.blockPosition().below();
        for (int i = 0; i < cnt; ++i) {
            Mob zombie = getSummonZombie();
            if (zombie == null) continue;
            ZombieUtil.copySummonZombieData(this, (PVZZombieEntity) zombie);
            BlockPos pos = WorldUtil.getSuitableHeightRandomPos(this.level(), jawPos, 1, 4);
            EntityUtil.onEntitySpawn(this.level(), zombie, pos);
            ((PVZZombieEntity) zombie).setZombieRising();
        }
        // 频繁召唤：1.5~2.5 秒一次（从冻结起点开始计时）
        this.summonTick = 30 + this.getRandom().nextInt(20);
    }

    /** 闭嘴段结束：回 NORMAL 并复位 SUMMON 相位/专用时钟。 */
    private void finishSummon() {
        this.setState(States.NORMAL);
        this.setAttackTime(0);
        this.setSummonPhase(0);
        this.summonFreezeWait = 0;
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
                ZombotMissileEntity missile = new ZombotMissileEntity(serverLevel, this, plant);
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

    /* ---- 小鬼炮（甲板漫步者） ---------------------------------------- */

    /** 当前小鬼炮已发射的波数（Goal 内部使用，服务端状态）。 */
    private int impVolleyFired;

    /**
     * 技能2（甲板漫步者）：从「眼睛」处露出大炮，连续发射 1~3 波，每波 6~12 只海盗小鬼，
     * 以抛物线越过前排防御落到场地后方。每波由 {@link ZombotImpCannonGoal} 按间隔驱动。
     */
    public void fireImpCannonVolley() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        final int count = 6 + this.getRandom().nextInt(7); // 6~12
        Vec3 dir = new Vec3(this.getLookAngle().x, 0, this.getLookAngle().z);
        if (dir.lengthSqr() < 1.0E-4) dir = new Vec3(0, 0, -1);
        dir = dir.normalize();

        for (int i = 0; i < count; ++i) {
            ImpEntity imp = PvZEntityRegister.IMP.get().create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
            if (imp == null) continue;
            // 从眼睛正前方生成，横向随机散开，避免全叠在一起
            Vec3 side = new Vec3(-dir.z, 0, dir.x);
            double spread = (this.getRandom().nextFloat() - 0.5F) * 6.0D;
            double mouthY = this.getY() + this.getEyeHeight() * 0.9D;
            imp.setPos(this.getX() + dir.x * 1.2D + side.x * spread,
                    mouthY,
                    this.getZ() + dir.z * 1.2D + side.z * spread);
            ZombieUtil.copySummonZombieData(this, imp);
            // 抛物线：水平速度与竖直上抛，越过前排后落地（落地前无敌由 ImpEntity 处理）
            double hSpeed = 0.55D + this.getRandom().nextDouble() * 0.35D;
            double vSpeed = 0.45D + this.getRandom().nextDouble() * 0.20D;
            imp.launchByCannon(hSpeed * dir.x + side.x * spread * 0.05D,
                    vSpeed,
                    hSpeed * dir.z + side.z * spread * 0.05D);
            serverLevel.addFreshEntity(imp);
        }
        ++this.impVolleyFired;
        serverLevel.sendParticles(ParticleTypes.SMOKE,
                this.getX() + dir.x * 1.2D, this.getY() + this.getEyeHeight() * 0.9D, this.getZ() + dir.z * 1.2D,
                8, 0.4, 0.4, 0.4, 0.02);
    }

    public int getImpVolleyFired() { return this.impVolleyFired; }
    public void setImpVolleyFired(int value) { this.impVolleyFired = value; }

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
        // 起手即锁定满冷却：即使冲刺中途被打断（如击杀玩家触发关卡失败），
        // 也不会因冷却还停在 0 而在下一 tick 立即重新冲撞。
        this.dashTick = DASH_COOLDOWN;
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
     * 冲刺推进（每 tick 调用）：以 DASH_SPEED 匀速移动 dir，沿途碾压所有生物并喷粒子。
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
        // 毁灭冲撞：碾压路径上的所有生物（植物、僵尸、玩家及其它 mob），
        // 不做阵营/敌对过滤（自身除外）；但排除创造/旁观模式的玩家，避免误伤正在观察/调试的玩家。
        // 同一生物在覆盖区内只会被命中一次（kill 后立即移除）。
        for (LivingEntity victim : serverLevel.getEntitiesOfClass(LivingEntity.class, column,
                e -> e != this && e.isAlive()
                        && !(e instanceof Player pl && (pl.isCreative() || pl.isSpectator())))) {
            victim.kill(serverLevel);
        }
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
        this.dashTick = DASH_COOLDOWN;
    }

    /** 计算跳跃方向（落点钳制在植物 10 格圈内）；距离不合适时返回 null。 */
    @Nullable
    private Vec3 computeJumpDelta() {
        BlockPos target = WorldUtil.getSuitableHeightRandomPos(this.level(), this.blockPosition(), 2, 10);
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
        return (dist > 10 || dist < 1) ? null : delta.normalize();
    }

    /**
     * 跳跃：先进入 {@link States#JUMP_CHARGE} 蓄力（下蹲弯腿），蓄力结束才起跳。
     * 落点被钳制在植物 10 格圈内，竖直初速度按 5 格高度计算（全程免疫）。
     */
    public void doJump() {
        if (this.computeJumpDelta() == null) { this.jumpTick = 30; return; }
        this.setState(States.JUMP_CHARGE);
        this.setAttackTime(0);
        this.setDeltaMovement(Vec3.ZERO);
        this.jumpTick = 60 + this.getRandom().nextInt(60);
    }

    /** 蓄力结束，真正起跳。 */
    private void launchJump() {
        Vec3 dir = this.computeJumpDelta();
        if (dir == null) {
            this.setState(States.NORMAL);
            this.setAttackTime(0);
            this.jumpTick = 30;
            return;
        }
        // 5 格高度对应竖直初速度：v = sqrt(2*g*h)，重力按 0.08/tick²、h=5 估算
        final double jumpVy = Math.sqrt(2 * 0.08 * 5.0);
        this.setDeltaMovement(dir.scale(0.55).add(0, jumpVy, 0));
        this.setState(States.JUMP);
        this.setAttackTime(0);
    }

    @Override
    public void zombieTick() {
        super.zombieTick();
        if (this.level().isClientSide()) return;

        // 动画时钟：未暂停时推进，暂停时冻结（客户端据此定格当前帧，恢复后接着播）
        if (!this.isAnimPaused()) {
            this.entityData.set(ANIM_CLOCK, this.entityData.get(ANIM_CLOCK) + 1);
        }

        // 冲撞冷却每 tick 递减且有下界 0。绝不能放进 Goal.canUse() 递减：
        // 那样在无目标期间会被反复调用减成巨大负数，目标一恢复就立刻冲撞（杀死玩家后无限连撞）。
        if (this.dashTick > 0) this.dashTick--;

        this.tickAngryAnimation();

        States state = this.getState();
        PlantEntity nearest = this.findNearestPlant();
        // 视线始终盯着最近植物（蓄力/冲刺时方向由 dashYaw 锁定，不在此覆盖）
        if (state != States.DASH_CHARGE && state != States.DASH) {
            this.faceNearestPlant(nearest);
        }

        // 跳跃蓄力推进：期间锁定不动，蓄力满后起跳
        if (state == States.JUMP_CHARGE) {
            this.setAttackTime(this.getAttackTime() + 1);
            if (this.getAttackTime() >= JUMP_CHARGE_TICKS) this.launchJump();
        }

        // 跳跃落地检测：只在下落触地时回到 NORMAL，避免刚起跳（vy>0）被误判落地
        if (state == States.JUMP && this.onGround() && this.getDeltaMovement().y <= 0D && this.tickCount > 4) {
            this.setState(States.NORMAL);
            this.setDeltaMovement(Vec3.ZERO);
        }
        // 冲刺推进：每 tick 匀速移动并摧毁沿途植物（由 Goal 计时，上限 DASH_TICKS）
        if (state == States.DASH) {
            this.doDashStep();
        } else if (state != States.DASH_CHARGE && state != States.JUMP_CHARGE) {
            // 与植物距离超过 10 格时匀速靠近，避免越打越远（蓄力期锁定不动）
            this.enforcePlantLeash(nearest);
        }
    }

    /* ================================================================ */
    /* BOSS 基础属性                                                      */
    /* ================================================================ */

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
        this.setState(States.fromOrdinal(input.getIntOr("zombot_state", States.NORMAL.ordinal())));
        this.summonTick = input.getIntOr("zombot_summon_tick", 40);
        this.missileTick = input.getIntOr("zombot_missile_tick", 160);
        // 钳制下界：兼容旧存档里被 canUse 减成负数的 dashTick，避免读档后立即连撞
        this.dashTick = Math.max(0, input.getIntOr("zombot_dash_tick", 220));
        this.jumpTick = input.getIntOr("zombot_jump_tick", 40);
        this.impCannonTick = input.getIntOr("zombot_imp_cannon_tick", 120);
        this.setImpVolleyFired(input.getIntOr("zombot_imp_volley", 0));
        this.angryThresholdIndex = input.getIntOr("zombot_angry_threshold", -1);
        this.angryQueue = input.getIntOr("zombot_angry_queue", 0);
        this.angryPlayTick = input.getIntOr("zombot_angry_tick", 0);
        this.entityData.set(ANIM_CLOCK, input.getIntOr("zombot_anim_clock", 0));
        this.setAnimPaused(input.getBooleanOr("zombot_anim_paused", false));
        this.setSummonPhase(input.getIntOr("zombot_summon_phase", 0));
        this.setSummonTick(input.getIntOr("zombot_summon_anim_tick", 0));
        this.summonFreezeWait = input.getIntOr("zombot_summon_freeze", 0);
        this.setDashOrigin(new BlockPos(
                input.getIntOr("zombot_origin_x", 0),
                input.getIntOr("zombot_origin_y", 0),
                input.getIntOr("zombot_origin_z", 0)));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("zombot_state", this.getState().ordinal());
        output.putInt("zombot_summon_tick", this.summonTick);
        output.putInt("zombot_missile_tick", this.missileTick);
        output.putInt("zombot_dash_tick", this.dashTick);
        output.putInt("zombot_jump_tick", this.jumpTick);
        output.putInt("zombot_imp_cannon_tick", this.impCannonTick);
        output.putInt("zombot_imp_volley", this.getImpVolleyFired());
        output.putInt("zombot_angry_threshold", this.angryThresholdIndex);
        output.putInt("zombot_angry_queue", this.angryQueue);
        output.putInt("zombot_angry_tick", this.angryPlayTick);
        output.putInt("zombot_anim_clock", this.entityData.get(ANIM_CLOCK));
        output.putBoolean("zombot_anim_paused", this.isAnimPaused());
        output.putInt("zombot_summon_phase", this.getSummonPhase());
        output.putInt("zombot_summon_anim_tick", this.getSummonTick());
        output.putInt("zombot_summon_freeze", this.summonFreezeWait);
        output.putInt("zombot_origin_x", this.getDashOrigin().getX());
        output.putInt("zombot_origin_y", this.getDashOrigin().getY());
        output.putInt("zombot_origin_z", this.getDashOrigin().getZ());
    }

    /** 状态枚举。顺序即同步 ordinal，末尾追加，勿改序。 */
    public enum States {
        NORMAL, SUMMON, MISSILE, DASH_CHARGE, DASH, JUMP, IMP_CANNON, JUMP_CHARGE;

        public static States fromOrdinal(int ordinal) {
            States[] v = values();
            return ordinal >= 0 && ordinal < v.length ? v[ordinal] : NORMAL;
        }
    }

    /**
     * 四足机甲的外观变体：决定共享模型显示哪些头部装饰部件。
     *
     * <ul>
     *   <li>{@link #SPHINX_INATOR}：法老头巾（nemes）；</li>
     *   <li>{@link #PLANK_WALKER}：桅杆（trick_rod）+ 海盗旗（flag）；</li>
     *   <li>{@link #WAR_WAGON}：牛（cow）；</li>
     *   <li>{@link #TOMORROW_TRON}：无（纯底座）。</li>
     * </ul>
     */
    public enum ZombotVariant {
        SPHINX_INATOR(true, false, false, false),
        PLANK_WALKER(false, true, true, false),
        WAR_WAGON(false, false, false, true),
        TOMORROW_TRON(false, false, false, false);

        public final boolean nemes;
        public final boolean trickRod;
        public final boolean flag;
        public final boolean cow;

        ZombotVariant(boolean nemes, boolean trickRod, boolean flag, boolean cow) {
            this.nemes = nemes;
            this.trickRod = trickRod;
            this.flag = flag;
            this.cow = cow;
        }
    }

    /* ================================================================ */
    /* 技能 Goals                                                        */
    /* ================================================================ */

    /** 蓄力冲刺：DASH_CHARGE 蓄力 60 tick → DASH 匀速冲刺 DASH_TICKS tick → 回原位。 */
    protected static class ZombotDashGoal extends Goal {
        private final AbstractZombotEntity boss;
        private int dashElapsed;

        ZombotDashGoal(AbstractZombotEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (boss.isAngryPlaying()) return false; // ANGRY 播放期间不启动技能，保证其完整播完
            // 冷却在 zombieTick 里每 tick 递减且有下界；这里只读取判断，绝不在 canUse 里递减。
            if (boss.dashTick > 0) return false;
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

    /**
     * 召唤（分三段，与 Blockbench SUMMON 动画 1.25s 对齐）：
     * phase1 张嘴 0.75s → phase2 冻结（漩涡显现 + 生成僵尸，等破土）→ phase3 闭嘴 0.5s → NORMAL。
     */
    protected static class ZombotSummonGoal extends Goal {
        private final AbstractZombotEntity boss;

        ZombotSummonGoal(AbstractZombotEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (boss.isAngryPlaying()) return false; // ANGRY 播放期间不启动技能，保证其完整播完
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
            boss.setSummonTick(0);
            boss.summonFreezeWait = 0;
            boss.setSummonPhase(1);
        }

        @Override
        public void tick() {
            final int phase = boss.getSummonPhase();
            if (phase == 2) {
                // 冻结段：jaw 时钟停住（漩涡显示在口中），等生成僵尸破土接近完成再解冻
                if (++boss.summonFreezeWait >= SUMMON_VORTEX_TICKS) boss.setSummonPhase(3);
                return;
            }
            // 张嘴 / 闭嘴段：推进专用动画时钟
            final int t = boss.getSummonTick() + 1;
            boss.setSummonTick(t);
            boss.setAttackTime(t);
            if (phase == 1 && t >= SUMMON_FREEZE_TICK) {
                // 到达 0.75s：冻结，此刻生成僵尸（破土会在冻结等待期内进行）
                boss.setSummonPhase(2);
                boss.spawnSummonZombies();
            } else if (phase == 3 && t >= SUMMON_END_TICK) {
                boss.finishSummon();
            }
        }

        @Override
        public void stop() { boss.setAttackTime(0); }
    }

    /** 导弹：MISSILE 状态蓄力 30 tick 后丢标记箭，再等 3 秒（60 tick）发射导弹。 */
    protected static class ZombotMissileGoal extends Goal {
        private static final int MARK_TICK = 30;
        private static final int LAUNCH_DELAY = 60; // 3 秒
        private final AbstractZombotEntity boss;

        ZombotMissileGoal(AbstractZombotEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (boss.isAngryPlaying()) return false; // ANGRY 播放期间不启动技能，保证其完整播完
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

    /**
     * 小鬼炮（甲板漫步者）：IMP_CANNON 状态蓄力 20 tick 后开始，
     * 每隔 30 tick 发射一波，共 1~3 波（每波由 {@link #fireImpCannonVolley()} 发射 6~12 只小鬼）。
     */
    protected static class ZombotImpCannonGoal extends Goal {
        private static final int WARMUP_TICK = 20;
        private static final int VOLLEY_INTERVAL = 30;
        private static final int MAX_VOLLEYS = 3;
        private final AbstractZombotEntity boss;

        ZombotImpCannonGoal(AbstractZombotEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (boss.isAngryPlaying()) return false; // ANGRY 播放期间不启动技能，保证其完整播完
            if (--boss.impCannonTick > 0) return false;
            return boss.getTarget() != null && boss.canUseImpCannon() && boss.getState() == States.NORMAL;
        }

        @Override
        public boolean canContinueToUse() { return boss.getState() == States.IMP_CANNON; }

        @Override
        public void start() {
            boss.setState(States.IMP_CANNON);
            boss.setAttackTime(0);
            boss.setImpVolleyFired(0);
        }

        @Override
        public void tick() {
            final int t = boss.getAttackTime() + 1;
            boss.setAttackTime(t);
            if (t < WARMUP_TICK) return;
            if (t == WARMUP_TICK || (t - WARMUP_TICK) % VOLLEY_INTERVAL == 0) {
                if (boss.getImpVolleyFired() < MAX_VOLLEYS) boss.fireImpCannonVolley();
            }
            if (boss.getImpVolleyFired() >= MAX_VOLLEYS) {
                boss.setState(States.NORMAL);
                boss.setAttackTime(0);
                boss.impCannonTick = 240 + boss.getRandom().nextInt(120);
            }
        }

        @Override
        public void stop() { boss.setAttackTime(0); }
    }

    /** 跳跃移动：10 格内跳格，落地后冷却。 */
    protected static class ZombotJumpGoal extends Goal {
        private final AbstractZombotEntity boss;

        ZombotJumpGoal(AbstractZombotEntity boss) { this.boss = boss; }

        @Override
        public boolean canUse() {
            if (boss.isAngryPlaying()) return false; // ANGRY 播放期间不启动技能，保证其完整播完
            if (--boss.jumpTick > 0) return false;
            return boss.getState() == States.NORMAL && boss.onGround();
        }

        @Override
        public boolean canContinueToUse() {
            States s = boss.getState();
            return s == States.JUMP_CHARGE || s == States.JUMP;
        }

        @Override
        public void start() { boss.doJump(); }

        @Override
        public void stop() { }
    }
}
