package com.lying.pvz.common.level.entity.zombie.base;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.BodyType;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.level.entity.misc.DestroyCarEntity;
import com.lying.pvz.common.level.entity.misc.ElementBallEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.level.entity.zombie.roof.BungeeZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.ZombieUtil;
import com.lying.pvz.utils.others.WeightList;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * EDGAR 机器人僵尸基类。
 *
 * <p>继承 {@link AbstractBossZombieEntity}，在 BOSS 通用能力（BossBar、脱战自愈、
 * 范围清场、碾压推挤）之上增加三种专属行为：
 * <ul>
 *   <li><b>发射元素球</b>：随机切换 {@code FLAME} / {@code ICE} 状态，动画结束后
 *       {@link #shootElementBall()} 生成 {@link ElementBallEntity}。</li>
 *   <li><b>偷取植物</b>：{@code STEAL} 状态，动画结束后 {@link #stealRandomTargets()}
 *       在 50 格范围内随机选目标，各召唤一只 {@link BungeeZombieEntity} 抓取。</li>
 *   <li><b>投掷拆迁车</b>：{@code CAR} 状态，动画结束后 {@link #throwDestroyCar()}
 *       按附近植物数量生成多辆 {@link DestroyCarEntity}。</li>
 * </ul>
 *
 * <p>三种状态由内部 {@link Goal} 驱动，共用 {@link #getAttackTime()} 作为动画计时，
 * 在 {@link #registerAttackGoals()} 注册。三者都以 {@code NORMAL} 状态作为门槛，
 * 且各自的冷却 tick（{@code shootBallTick / stealPlantTick / throwCarTick}）在
 * 每次动作结束按 {@link #getShootBallCD()} / {@link #getStealPlantCD()} / {@link #getThrowCarCD()}
 * 加上随机偏移回填。
 *
 * <p><b>召唤僵尸</b>：{@link #summonZombieByBungee()} 每 100 tick 根据
 * {@link #getBossStage()} 从 {@code ZOMBIES_1}~{@code ZOMBIES_5} 抽类型，
 * 通过 {@link #onBossSummon} 生成并附带 Bungee 投放。
 *
 * <p><b>死亡演出</b>：{@link #tickDeath()} 每 20 tick 在客户端生成两次
 * {@code EXPLOSION_EMITTER}； 一次性抛出全部 6 种部件。
 *
 * <p><b>NBT</b>：状态与三种冷却 tick 均持久化；写入方法改为
 * {@link ValueOutput} / {@link ValueInput}。
 */
public abstract class EdgarRobotEntity extends AbstractBossZombieEntity {

    private static final EntityDataAccessor<Integer> STATES =
            SynchedEntityData.defineId(EdgarRobotEntity.class, EntityDataSerializers.INT);

    private static final WeightList<Supplier<IZombieType>> ZOMBIES_1 = new WeightList<>();
    private static final WeightList<Supplier<IZombieType>> ZOMBIES_2 = new WeightList<>();
    private static final WeightList<Supplier<IZombieType>> ZOMBIES_3 = new WeightList<>();
    private static final WeightList<Supplier<IZombieType>> ZOMBIES_4 = new WeightList<>();
    private static final WeightList<Supplier<IZombieType>> ZOMBIES_5 = new WeightList<>();
    protected int throwCarTick;
    protected int shootBallTick;
    protected int stealPlantTick;

    static {
        {
            ZOMBIES_1.addItem(GrassZombies.NORMAL_ZOMBIE, 100);
            ZOMBIES_1.addItem(GrassZombies.NEWSPAPER_ZOMBIE, 90);
            ZOMBIES_1.addItem(GrassZombies.SCREENDOOR_ZOMBIE, 70);
            ZOMBIES_1.addItem(PoolZombies.SNORKEL_ZOMBIE, 100);
            ZOMBIES_1.addItem(PoolZombies.BALLOON_ZOMBIE, 80);
            ZOMBIES_1.addItem(RoofZombies.IMP, 80);
        }
        {
            ZOMBIES_2.addItem(GrassZombies.CONEHEAD_ZOMBIE, 100);
            ZOMBIES_2.addItem(GrassZombies.POLE_ZOMBIE, 90);
            ZOMBIES_2.addItem(GrassZombies.DANCING_ZOMBIE, 70);
            ZOMBIES_2.addItem(GrassZombies.OLD_ZOMBIE, 80);
            ZOMBIES_2.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE, 85);
            ZOMBIES_2.addItem(PoolZombies.DIGGER_ZOMBIE, 70);
            ZOMBIES_2.addItem(PoolZombies.POGO_ZOMBIE, 85);
            ZOMBIES_2.addItem(PoolZombies.BALLOON_ZOMBIE, 80);
        }
        {
            ZOMBIES_3.addItem(GrassZombies.BUCKETHEAD_ZOMBIE, 100);
            ZOMBIES_3.addItem(GrassZombies.FOOTBALL_ZOMBIE, 90);
            ZOMBIES_3.addItem(PoolZombies.ZOMBONI, 85);
            ZOMBIES_3.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE, 85);
            ZOMBIES_3.addItem(PoolZombies.DIGGER_ZOMBIE, 75);
            ZOMBIES_3.addItem(RoofZombies.LADDER_ZOMBIE, 85);
            ZOMBIES_3.addItem(RoofZombies.CATAPULT_ZOMBIE, 80);
        }
        {
            ZOMBIES_4.addItem(GrassZombies.FOOTBALL_ZOMBIE, 100);
            ZOMBIES_4.addItem(GrassZombies.GIGA_FOOTBALL_ZOMBIE, 80);
            ZOMBIES_4.addItem(PoolZombies.ZOMBONI, 90);
            ZOMBIES_4.addItem(CustomZombies.LAVA_ZOMBIE, 85);
            ZOMBIES_4.addItem(PoolZombies.DIGGER_ZOMBIE, 75);
            ZOMBIES_4.addItem(RoofZombies.CATAPULT_ZOMBIE, 90);
            ZOMBIES_4.addItem(RoofZombies.GARGANTUAR, 85);
        }
        {
            ZOMBIES_5.addItem(GrassZombies.GIGA_FOOTBALL_ZOMBIE, 100);
            ZOMBIES_5.addItem(GrassZombies.SUNDAY_EDITION_ZOMBIE, 85);
            ZOMBIES_5.addItem(CustomZombies.LAVA_ZOMBIE, 95);
            ZOMBIES_5.addItem(RoofZombies.CATAPULT_ZOMBIE, 110);
            ZOMBIES_5.addItem(RoofZombies.GARGANTUAR, 100);
            ZOMBIES_5.addItem(RoofZombies.GIGA_GARGANTUAR, 70);
        }
    }

    public EdgarRobotEntity(EntityType<? extends AbstractBossZombieEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setIsWholeBody();
        this.shootBallTick = this.getShootBallCD();
        this.stealPlantTick = this.getStealPlantCD();
        this.throwCarTick = this.getThrowCarCD();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATES, EdgarStates.NORMAL.ordinal());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 40.0F));
        registerTargetGoals();
        registerAttackGoals();
    }

    @Override
    protected void registerAttackGoals() {
        this.goalSelector.addGoal(4, new EdgarShootBallGoal(this));
        this.goalSelector.addGoal(5, new EdgarStealPlantGoal(this));
        this.goalSelector.addGoal(3, new EdgarThrowCarGoal(this));
    }

    @Override
    protected void registerTargetGoals() {
        this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, false, true,
                ZombieUtil.LITTLE_CLOSE_TARGET_RANGE, ZombieUtil.LITTLE_HIGH_TARGET_HEIGHT));
    }

    @Override
    public void normalZombieTick() {
        super.normalZombieTick();
        if (!this.level().isClientSide()) {
            if (this.getExistTick() % 100 == 0) {
                this.summonZombieByBungee();
            }
        }
    }

    @Override
    public boolean canPAZTarget(Entity target) {
        return true;
    }

    public void summonZombieByBungee() {
        if (this.nearbyZombieCount < this.maxZombieSurround) {
            final int count = this.getSpawnCount();
            for (int i = 0; i < count; ++i) {
                this.getSummonZombie().ifPresent(entity -> {
                    if (entity instanceof ZombieEntity zombie) {
                        this.onBossSummon(zombie, this.blockPosition().above(10));

                        BungeeZombieEntity bungee = PvZEntityRegister.BUNGEE_ZOMBIE.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
                        if (bungee == null) {
                            return;
                        }
                        bungee.setBungeeType(BungeeZombieEntity.BungeeTypes.SUMMON);
                        bungee.setBungeeState(BungeeZombieEntity.BungeeStates.DOWN);
                        bungee.setStealTarget(entity);
                        BlockPos pos = this.getRandom().nextInt(5) == 0
                                ? WorldUtil.getSuitableHeightRandomPos(this.level(), this.blockPosition(), 20, 40)
                                : WorldUtil.getSuitableHeightRandomPos(this.level(), this.blockPosition(), 3, 8);
                        this.onBossSummon(bungee, pos.above(20));

                        EntityUtil.playSound(bungee, PvZSoundRegister.BUNGEE_SCREAM.get());
                    }
                });
            }
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime % 20 == 1) {
            if (this.level().isClientSide()) {
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                        this.getX(), this.getY() + 5, this.getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected int getDeathTime() {
        return 60;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!this.level().isClientSide()) {
            this.bossInfo.getPlayers().forEach(player ->
                    CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(player, this, source));
        }
    }

    public void shootElementBall() {
        final ElementBallEntity ball = PvZEntityRegister.ELEMENT_BALL.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
        if (ball == null) {
            return;
        }
        ball.summonByOwner(this);
        ball.setSpeed(this.getElementBallSpeed());
        ball.setAuto(this.shootAutoBall());
        ball.setPos(this.getX(), this.getY() + this.getEyeHeight(), this.getZ());
        ball.shoot(this.getTarget());
        ball.setElementBallType(this.getRobotState() == EdgarStates.ICE
                ? ElementBallEntity.ElementTypes.ICE
                : ElementBallEntity.ElementTypes.FLAME);
        this.level().addFreshEntity(ball);
        this.setRobotState(EdgarStates.NORMAL);

        this.shootBallTick = this.getShootBallCD() + MathUtil.getRandomInRange(this.getRandom(), 80);
    }

    public void stealRandomTargets() {
        this.setRobotState(EdgarStates.NORMAL);
        final int cnt = this.getStealCount();
        final float range = 50;
        List<LivingEntity> list = EntityUtil.getTargetableLivings(this,
                EntityUtil.getEntityAABB(this, range, range));
        if (!list.isEmpty()) {
            for (int i = 0; i < cnt; ++i) {
                LivingEntity target = list.get(this.getRandom().nextInt(list.size()));
                BungeeZombieEntity zombie = PvZEntityRegister.BUNGEE_ZOMBIE.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
                if (zombie == null) {
                    return;
                }
                zombie.setBungeeType(BungeeZombieEntity.BungeeTypes.STEAL);
                zombie.setStealTarget(target);
                zombie.setBungeeState(BungeeZombieEntity.BungeeStates.DOWN);
                ZombieUtil.copySummonZombieData(this, zombie);
                EntityUtil.onEntitySpawn(this.level(), zombie, this.blockPosition().above(18));
            }
        }

        this.stealPlantTick = this.getStealPlantCD() + MathUtil.getRandomInRange(this.getRandom(), 120);
    }

    public void throwDestroyCar() {
        this.setRobotState(EdgarStates.NORMAL);
        final int max = Math.max(1, (this.nearbyPlantCount - this.maxPlantSurround) / 10);
        final int throwNum = MathUtil.getRandomMinMax(this.getRandom(), max / 2 + 1, max + 1);
        final float range = 50;
        final List<LivingEntity> list = EntityUtil.getTargetableLivings(this,
                EntityUtil.getEntityAABB(this, range, range));
        if (!list.isEmpty()) {
            for (int i = 0; i < throwNum; ++i) {
                final int pos = this.getRandom().nextInt(list.size());
                DestroyCarEntity car = PvZEntityRegister.DESTROY_CAR.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
                if (car == null) {
                    return;
                }
                car.summonByOwner(this);
                car.setPos(this.getX(), this.getY() + this.getEyeHeight(), this.getZ());
                car.shootPultBullet(list.get(pos));
                this.level().addFreshEntity(car);
            }
        }

        this.throwCarTick = this.getThrowCarCD() + MathUtil.getRandomInRange(this.getRandom(), 160);
    }

    protected Optional<Mob> getSummonZombie() {
        final int stage = getBossStage();
        final WeightList<Supplier<IZombieType>> pool;
        switch (stage) {
            case 1 -> pool = ZOMBIES_1;
            case 2 -> pool = ZOMBIES_2;
            case 3 -> pool = ZOMBIES_3;
            case 4 -> pool = ZOMBIES_4;
            case 5 -> pool = ZOMBIES_5;
            default -> {
                return Optional.empty();
            }
        }
        final IZombieType zombieType = pool.getRandomItem(this.getRandom())
                .map(Supplier::get)
                .orElse(null);
        if (zombieType == null) {
            return Optional.empty();
        }
        if (zombieType.getEntityType().isPresent()) {
            return Optional.ofNullable(zombieType.getEntityType().get().create(this.level(),EntitySpawnReason.MOB_SUMMONED));
        }
        return Optional.empty();
    }

    public abstract int getBossStage();

    public float getElementBallSpeed() { return 0.18F; }
    public int getStealCount() { return 3; }
    public int getSpawnCount() { return 2; }
    public boolean shootAutoBall() { return false; }
    public int getShootBallCD() { return 600; }
    public int getThrowCarCD() { return 800; }
    public int getStealPlantCD() { return 400; }
    public int getAnimShootCD() { return 40; }
    public int getAnimThrowCD() { return 30; }
    public int getAnimStealCD() { return 30; }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setRobotState(EdgarStates.fromOrdinal(input.getIntOr("zomboss_state", EdgarStates.NORMAL.ordinal())));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.throwCarTick = input.getIntOr("zomboss_throw_car_tick", 0);
        this.shootBallTick = input.getIntOr("zomboss_shoot_ball_cd", 0);
        this.stealPlantTick = input.getIntOr("zomboss_steal_cd", 0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("zomboss_state", this.getRobotState().ordinal());
        output.putInt("zomboss_throw_car_tick", this.throwCarTick);
        output.putInt("zomboss_shoot_ball_cd", this.shootBallTick);
        output.putInt("zomboss_steal_cd", this.stealPlantTick);
    }

    public void setRobotState(EdgarStates state) {
        this.entityData.set(STATES, state.ordinal());
    }

    public EdgarStates getRobotState() {
        return EdgarStates.fromOrdinal(this.entityData.get(STATES));
    }

    @Override
    public Optional<SoundEvent> getSpawnSound() {
        return Optional.ofNullable(PvZSoundRegister.EDGAR_LAUGH.get());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PvZSoundRegister.EDGAR_EXPLOSION.get();
    }

    public enum EdgarStates {
        NORMAL, FLAME, ICE, STEAL, CAR;

        public static EdgarStates fromOrdinal(int ordinal) {
            final EdgarStates[] values = values();
            return (ordinal >= 0 && ordinal < values.length) ? values[ordinal] : NORMAL;
        }
    }

    protected static class EdgarShootBallGoal extends Goal {

        private final EdgarRobotEntity edgarRobot;

        public EdgarShootBallGoal(EdgarRobotEntity edgarRobot) {
            this.edgarRobot = edgarRobot;
        }

        @Override
        public boolean canUse() {
            if (--this.edgarRobot.shootBallTick > 0) {
                return false;
            }
            return this.edgarRobot.getTarget() != null
                    && this.edgarRobot.getRobotState() == EdgarStates.NORMAL;
        }

        @Override
        public boolean canContinueToUse() {
            return this.edgarRobot.getRobotState() == EdgarStates.FLAME
                    || this.edgarRobot.getRobotState() == EdgarStates.ICE;
        }

        @Override
        public void start() {
            this.edgarRobot.setRobotState(this.edgarRobot.getRandom().nextInt(2) == 0
                    ? EdgarStates.FLAME : EdgarStates.ICE);
            this.edgarRobot.bossInfo.getPlayers().forEach(p ->
                    PlayerUtil.playClientSound(p, PvZSoundRegister.EDGAR_SHOOT.get()));
            this.edgarRobot.setAttackTime(0);
        }

        @Override
        public void tick() {
            this.edgarRobot.setAttackTime(this.edgarRobot.getAttackTime() + 1);
            if (this.edgarRobot.getAttackTime() > this.edgarRobot.getAnimShootCD()) {
                this.edgarRobot.shootElementBall();
            }
        }

        @Override
        public void stop() {
            this.edgarRobot.setAttackTime(0);
        }
    }

    protected static class EdgarStealPlantGoal extends Goal {

        private final EdgarRobotEntity edgarRobot;

        public EdgarStealPlantGoal(EdgarRobotEntity edgarRobot) {
            this.edgarRobot = edgarRobot;
        }

        @Override
        public boolean canUse() {
            if (--this.edgarRobot.stealPlantTick > 0) {
                return false;
            }
            return this.edgarRobot.getTarget() != null
                    && this.edgarRobot.getRobotState() == EdgarStates.NORMAL
                    && this.edgarRobot.nearbyPlantCount > 10;
        }

        @Override
        public boolean canContinueToUse() {
            return this.edgarRobot.getRobotState() == EdgarStates.STEAL;
        }

        @Override
        public void start() {
            this.edgarRobot.setRobotState(EdgarStates.STEAL);
            this.edgarRobot.setAttackTime(0);
        }

        @Override
        public void tick() {
            this.edgarRobot.setAttackTime(this.edgarRobot.getAttackTime() + 1);
            if (this.edgarRobot.getAttackTime() > this.edgarRobot.getAnimStealCD()) {
                this.edgarRobot.stealRandomTargets();
            }
        }

        @Override
        public void stop() {
            this.edgarRobot.setAttackTime(0);
        }
    }

    protected static class EdgarThrowCarGoal extends Goal {

        private final EdgarRobotEntity edgarRobot;

        public EdgarThrowCarGoal(EdgarRobotEntity edgarRobot) {
            this.edgarRobot = edgarRobot;
        }

        @Override
        public boolean canUse() {
            if (--this.edgarRobot.throwCarTick > 0) {
                return false;
            }
            return this.edgarRobot.getTarget() != null
                    && this.edgarRobot.getRobotState() == EdgarStates.NORMAL
                    && this.edgarRobot.nearbyPlantCount > 40;
        }

        @Override
        public boolean canContinueToUse() {
            return this.edgarRobot.getRobotState() == EdgarStates.CAR;
        }

        @Override
        public void start() {
            this.edgarRobot.setRobotState(EdgarStates.CAR);
            this.edgarRobot.setAttackTime(0);
        }

        @Override
        public void tick() {
            this.edgarRobot.setAttackTime(this.edgarRobot.getAttackTime() + 1);
            if (this.edgarRobot.getAttackTime() > this.edgarRobot.getAnimThrowCD()) {
                this.edgarRobot.throwDestroyCar();
            }
        }

        @Override
        public void stop() {
            this.edgarRobot.setAttackTime(0);
        }
    }
}