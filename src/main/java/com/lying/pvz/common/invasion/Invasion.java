package com.lying.pvz.common.invasion;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.entity.ability.IHasOwner;
import com.lying.pvz.api.event.invasion.InvasionEndEvent;
import com.lying.pvz.api.event.invasion.InvasionStartEvent;
import com.lying.pvz.api.event.invasion.InvasionWaveEvent;
import com.lying.pvz.api.event.mission.MissionStageCompleteEvent;
import com.lying.pvz.api.invasion.*;
import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.invasion.type.InvasionFog;
import com.lying.pvz.common.invasion.type.MissionTypes;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.light.PlanternEntity;
import com.lying.pvz.common.payload.InvasionStatePayload;
import com.lying.pvz.common.payload.InvasionWavePayload;
import com.lying.pvz.common.payload.MissionStagePayload;
import com.lying.pvz.utils.EffectUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 单玩家入侵运行时。
 *
 * <p>权威状态在此；{@link InvasionSnapshot} 是派生展示态，通过 attachment sync 到客户端。
 * 玩家侧持久化只保留 {@link Resources#MISSION_FINISH_TIME}；本类的 mission 进度由
 * {@link #missionValue} 本地维护，不再写 {@code Resources}。
 */
public class Invasion implements IInvasion {

    private enum SpawnResult { OK, NO_POS, BAD_PLACE, NO_ENTITY }

    private ServerPlayer player;
    private ServerLevel level;
    private PlayerDataManager pdm;

    private boolean running = false;
    /** 是否为命令触发的入侵（命令入侵不重置安全天数）。 */
    private boolean command = false;
    private IInvasionType primary;
    private final Set<IInvasionType> assists = new HashSet<>();

    private int tick = 0;
    private int waveIndex = 0;
    private int warningIndex = 0;
    private int nextSpawnTick = 0;
    private int victoryGraceTick = 0;
    /** 波次刷完后仍有怪未清的累计 tick，达到宽限阈值后兜底结束。 */
    private int overtimeTick = 0;
    /** 普通波预告提前量：3 秒。硬编码，按需求固定。 */
    private static final int WARNING_LEAD_NORMAL = 60;
    /** 大波预告提前量：8 秒。硬编码。 */
    private static final int WARNING_LEAD_HUGE   = 160;
    private int[] primaryWaveTimes = new int[0];
    private int[] primaryWaveSpawnCounts = new int[0];

    private IMissionType missionType;
    private int missionStage = 0;
    private int missionValue = 0;
    private int missionTick = 0;

    private final Set<UUID> spawnedEntities = new HashSet<>();
    private int killInSecond = 0;
    private int maxKillInSecond = 0;
    private int killResetTick = 0;

    public Invasion(ServerPlayer player) {
        this.player = player;
        this.level = player.level();
        this.pdm = new PlayerDataManager(player);
        publishSnapshot();
    }

    public void refreshPlayer(ServerPlayer newPlayer) {
        this.player = newPlayer;
        this.level = newPlayer.level();
        this.pdm = new PlayerDataManager(newPlayer);
        publishSnapshot();
    }

    /* ---------------- IInvasion ---------------- */

    @Override public UUID getOwner() { return player.getUUID(); }
    @Override public boolean isRunning() { return running; }
    @Override public IInvasionType getPrimary() { return primary; }
    @Override public Collection<IInvasionType> getAssists() { return assists; }
    @Override public int getWaveIndex() { return waveIndex; }
    @Override public IMissionType getMissionType() { return missionType; }
    @Override public int getMissionStage() { return missionStage; }
    @Override public int getMissionValue() { return missionValue; }

    @Override
    public void enable() {
        if (running) return;

        IInvasionType main = InvasionManager.pickPrimary(player);
        if (main == null) {
            PvZMod.LOGGER.debug("[Invasion] no primary candidate for {} (expLevel={}, loaded={})",
                    player.getGameProfile().name(), pdm.getResource(Resources.TREE_LVL),
                    InvasionManager.loadedCount());
            return;
        }
        InvasionStartEvent ev = new InvasionStartEvent(player, main);
        NeoForge.EVENT_BUS.post(ev);

        if (ev.isCanceled()) {
            PvZMod.LOGGER.debug("[Invasion] start canceled by event: type={}", main.getID());
            return;
        }

        PvZMod.LOGGER.info("[Invasion] enable: player={}, type={}", player.getGameProfile().name(), main.getID());

        resetState(main);
        pdm.setInvasionSafeDays(0);
        player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("invasion.pvz.start"));
        PacketDistributor.sendToPlayer(player, InvasionStatePayload.start(main.getID()));
    }

    @Override
    public void disable() {
        disableWith(InvasionEndEvent.Reason.NATURAL);
    }

    public void disableWith(InvasionEndEvent.Reason reason) {
        if (!running) return;
        this.running = false;
        // 清除迷雾入侵施加在僵尸身上的隐身/雾隐效果，避免取消入侵后僵尸永久隐身。
        for (UUID uuid : spawnedEntities) {
            Entity e = level.getEntity(uuid);
            if (e instanceof LivingEntity living) {
                living.removeEffect(PvZEffectRegister.FOG_HIDDEN);
                living.removeEffect(MobEffects.INVISIBILITY);
            }
        }
        this.spawnedEntities.clear();
        InvasionManager.removeActive(player);
        publishSnapshot();
        PacketDistributor.sendToPlayer(player, InvasionStatePayload.end(reason));
        NeoForge.EVENT_BUS.post(new InvasionEndEvent(player, reason));

        if (!command && !player.isRemoved() && player.connection != null) {
            pdm.setInvasionSafeDays(InvasionManager.configSafeDays());
        }
    }

    public void forceEnableWith(IInvasionType type) {
        if (running) return;
        InvasionStartEvent ev = new InvasionStartEvent(player, type);
        NeoForge.EVENT_BUS.post(ev);
        if (ev.isCanceled()) return;
        this.command = true;
        resetState(type);
        PacketDistributor.sendToPlayer(player, InvasionStatePayload.start(type.getID()));
    }

    private void resetState(IInvasionType type) {
        this.primary = type;
        this.assists.clear();
        this.assists.addAll(InvasionManager.pickAssists(player));
        this.running = true;
        this.tick = 0;
        this.waveIndex = 0;
        this.warningIndex = 0;
        this.nextSpawnTick = 0;
        this.victoryGraceTick = 0;
        this.overtimeTick = 0;
        this.missionType = pickMission();
        this.missionStage = 0;
        this.missionValue = 0;
        this.missionTick = 0;
        this.killInSecond = 0;
        this.maxKillInSecond = 0;
        this.killResetTick = 0;

        final int[] jsonTimes = type.getWaveTimes();
        if (jsonTimes.length > 0) {
            this.primaryWaveTimes = jsonTimes;
            this.primaryWaveSpawnCounts = type.getWaveSpawnCounts();
        } else {
            final InvasionConstants.RolledWaves rolled =
                    InvasionConstants.rollDefaultWaves(level.getRandom(), pdm.getResource(Resources.TREE_LVL));
            this.primaryWaveTimes = rolled.times();
            this.primaryWaveSpawnCounts = rolled.counts();
        }

        InvasionManager.addActive(player, this);
        PvZMod.LOGGER.info("[Invasion] started {} for {}: waves={}, times={}, counts={}",
                type.getID(), player.getGameProfile().name(),
                primaryWaveTimes.length,
                java.util.Arrays.toString(primaryWaveTimes),
                java.util.Arrays.toString(primaryWaveSpawnCounts));
        publishSnapshot();
    }

    /* ---------------- tick ---------------- */

    public void tick() {
        if (!running) return;
        if (level.getDifficulty() == Difficulty.PEACEFUL) { disable(); return; }

        tick++;
        if (tick % 20 == 0) {
            pruneSpawned();
            applyFogHiding();
            publishSnapshot();
        }

        final int[] waveTimes = this.primaryWaveTimes;

        // ① 预告先行：不取消、不发事件，纯通知
        while (warningIndex < waveTimes.length && tick >= warnTick(warningIndex)) {

            PvZMod.LOGGER.debug("[Invasion] warning wave {} at tick {} (huge={})",
                    warningIndex, tick, isHugeWave(warningIndex));

            fireWaveWarning(warningIndex);
            warningIndex++;
        }

        // ② 触发：保持原逻辑
        while (waveIndex < waveTimes.length && tick >= waveTimes[waveIndex]) {
            final int triggered = waveIndex;

            PvZMod.LOGGER.debug("[Invasion] trigger wave {} at tick {}, count={}",
                    triggered, tick,
                    triggered < primaryWaveSpawnCounts.length ? primaryWaveSpawnCounts[triggered] : 0);


            InvasionWaveEvent ev = new InvasionWaveEvent(player, triggered);
            NeoForge.EVENT_BUS.post(ev);
            if (!ev.isCanceled()) {
                spawnWave(triggered);
                PacketDistributor.sendToPlayer(player,
                        new InvasionWavePayload(triggered, waveTimes.length,
                                InvasionWavePayload.Phase.TRIGGERED));
            }
            waveIndex++;
            publishSnapshot();
        }

        boolean wavesOngoing = waveTimes.length == 0 || waveIndex < waveTimes.length;
        if (wavesOngoing && tick >= nextSpawnTick) {
            spawnBatch();
            nextSpawnTick = tick + spawnInterval();
        }

        if (waveTimes.length > 0 && waveIndex >= waveTimes.length) {
            if (spawnedEntities.isEmpty()) {
                overtimeTick = 0;
                if (++victoryGraceTick >= 100) {

                    PvZMod.LOGGER.info("[Invasion] VICTORY after {} ticks", tick);

                    disableWith(InvasionEndEvent.Reason.VICTORY);
                    return;
                }
                if (victoryGraceTick == 1) {
                    PvZMod.LOGGER.debug("[Invasion] victory grace started at tick {}", tick);
                }


            } else {

                if (victoryGraceTick != 0) {
                    PvZMod.LOGGER.debug("[Invasion] grace reset: spawned={} (tick {})",
                            spawnedEntities.size(), tick);
                }

                victoryGraceTick = 0;

                final int grace = overtimeGraceTicks();
                if (grace > 0 && ++overtimeTick >= grace) {
                    PvZMod.LOGGER.info("[Invasion] TIMEOUT at tick {}: remaining={}, grace={}",
                            tick, spawnedEntities.size(), grace);
                    clearRemainingEntities();
                    player.sendSystemMessage(Component.translatable("invasion.pvz.timeout",
                            InvasionManager.configSafeDays()).withStyle(ChatFormatting.YELLOW));
                    disableWith(InvasionEndEvent.Reason.TIMEOUT);
                    return;
                }
            }
        }

        if (missionType != null && "instant_kill".equals(missionType.getID().getPath())) {
            killResetTick++;
            if (killResetTick >= 20) {
                killResetTick = 0;
                if (killInSecond > maxKillInSecond) {
                    maxKillInSecond = killInSecond;
                    this.missionValue = maxKillInSecond;
                    publishSnapshot();
                }
                killInSecond = 0;
            }
        }

        tickMission();
    }

    private int warnTick(int i) {
        final int lead = isHugeWave(i) ? WARNING_LEAD_HUGE : WARNING_LEAD_NORMAL;
        final int ideal = primaryWaveTimes[i] - lead;
        if (i == 0) {
            return Math.max(0, ideal);
        }
        return Math.max(primaryWaveTimes[i - 1] + 1, ideal);
    }

    /** 最后一波必为大波。 */
    private boolean isHugeWave(int i) {
        return i == primaryWaveTimes.length - 1;
    }

    private void fireWaveWarning(int i) {
        final InvasionWavePayload.Phase phase = isHugeWave(i)
                ? InvasionWavePayload.Phase.HUGE_WARNING
                : InvasionWavePayload.Phase.WARNING;
        PacketDistributor.sendToPlayer(player,
                new InvasionWavePayload(i, primaryWaveTimes.length, phase));
    }

    /** 清理已不在 level 中的 UUID（区块卸载、非死亡移除等）。 */
    private void pruneSpawned() {
        if (spawnedEntities.isEmpty()) return;

        final int before = spawnedEntities.size();

        spawnedEntities.removeIf(uuid -> level.getEntity(uuid) == null);

        final int removed = before - spawnedEntities.size();
        if (removed > 0) {
            PvZMod.LOGGER.debug("[Invasion] pruned {} entities, remaining={}", removed, spawnedEntities.size());
        }
    }

    /* ---------------- 超时兜底 ---------------- */

    /** 波次刷完后仍不清场的兜底宽限（tick）；0 或负数表示禁用。 */
    private int overtimeGraceTicks() {
        final double days = InvasionManager.configOvertimeGraceDays();
        if (days <= 0D) return -1;
        return Math.max(20, (int) Math.round(InvasionTime.periodTicks(level) * days));
    }

    /** 超时结束：清除本次入侵刷出的残留实体（不触发死亡与掉落）。 */
    private void clearRemainingEntities() {
        int cleared = 0;
        for (UUID uuid : spawnedEntities) {
            Entity e = level.getEntity(uuid);
            if (e != null) {
                e.discard();
                cleared++;
            }
        }
        spawnedEntities.clear();
        PvZMod.LOGGER.info("[Invasion] cleared {} remaining entities on timeout", cleared);
    }

    /* ---------------- 迷雾隐怪 ---------------- */

    /** 有效迷雾：优先主入侵的雾，若为空则取首个带雾的辅助入侵（迷雾支援）。 */
    @Nullable
    private IInvasionFog effectiveFog() {
        IInvasionFog fog = this.primary != null ? this.primary.getFog() : null;
        if (fog != null) return fog;
        for (IInvasionType assist : assists) {
            fog = assist.getFog();
            if (fog != null) return fog;
        }
        return null;
    }

    /** 僵尸靠近植物该距离（格）内即被植物“看见”，解除迷雾隐身。 */
    private static final int PLANT_REVEAL_RANGE = 5;

    /** 每 20 tick 依据迷雾策略对入侵刷出的怪物施加/解除雾隐标记（纯客户端渲染用，不影响 AI）。 */
    private void applyFogHiding() {
        IInvasionFog fog = effectiveFog();
        if (fog == null || !fog.hideRaiders()) return;

        for (UUID uuid : spawnedEntities) {
            Entity e = level.getEntity(uuid);
            if (!(e instanceof LivingEntity living)) continue;

            // 被路灯花照亮，或已贴近友方植物（5 格内），都视为“可见”，解除隐身；
            // 否则保持隐身，5 格外想攻击就必须依靠路灯花的光眼效果。
            final boolean hidden = !isLitByPlantern(living) && !isNearFriendlyPlant(living);
            if (hidden) {
                living.addEffect(EffectUtil.effect(PvZEffectRegister.FOG_HIDDEN, 40, 0));
                // 迷雾中的僵尸默认带真正的隐身，只有被路灯花照射或贴近植物才解除。
                if (!living.hasEffect(MobEffects.INVISIBILITY)) {
                    living.addEffect(EffectUtil.effect(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0));
                }
            } else if (living.hasEffect(PvZEffectRegister.FOG_HIDDEN)) {
                living.removeEffect(PvZEffectRegister.FOG_HIDDEN);
                living.removeEffect(MobEffects.INVISIBILITY);
            }
        }
    }

    /** 是否有路灯花在其光照范围内照亮该实体（驱雾）。 */
    private boolean isLitByPlantern(LivingEntity target) {
        for (PlanternEntity p : level.getEntitiesOfClass(PlanternEntity.class,
                EntityUtil.getEntityAABB(target, 64, 64))) {
            final float range = p.getEffectRange();
            if (target.distanceToSqr(p) <= range * range) {
                return true;
            }
        }
        return false;
    }

    /** 僵尸 5 格内是否存在友方植物（植物看到近身僵尸即可攻击）。 */
    private boolean isNearFriendlyPlant(LivingEntity target) {
        for (PlantEntity plant : level.getEntitiesOfClass(PlantEntity.class,
                EntityUtil.getEntityAABB(target, PLANT_REVEAL_RANGE, PLANT_REVEAL_RANGE))) {
            if (EntityUtil.isFriendly(target, plant)
                    && target.distanceToSqr(plant) <= (double) PLANT_REVEAL_RANGE * PLANT_REVEAL_RANGE) {
                return true;
            }
        }
        return false;
    }

    /* ---------------- 战斗接入 ---------------- */

    public boolean ownsEntity(Entity e) {
        return spawnedEntities.contains(e.getUUID());
    }

    public void onEntityDeath(Entity dead, @Nullable Entity killer) {
        if (!spawnedEntities.remove(dead.getUUID())) return;
        if (missionType == null) return;
        if (!creditKill(killer)) return;
        switch (missionType.getID().getPath()) {
            case "kill" -> addMissionValue(1);
            case "instant_kill" -> killInSecond++;
        }
    }

    private boolean creditKill(@Nullable Entity killer) {
        if (killer == null) return false;
        if (killer == player) return true;
        if (killer instanceof IHasOwner ho) {
            return ho.getOwnerUUID().map(u -> u.equals(player.getUUID())).orElse(false);
        }
        return false;
    }

    public void onSunCollected(int amount) {
        if (!running || missionType == null) return;
        if (!"collect_sun".equals(missionType.getID().getPath())) return;
        addMissionValue(amount);
    }

    /** 直接推进本地 missionValue，本地为权威。 */
    private void addMissionValue(int amount) {
        if (amount == 0) return;
        this.missionValue += amount;
        publishSnapshot();
    }

    /* ---------------- 刷怪 ---------------- */

    private void spawnWave(int wave) {
        final int count = (wave < primaryWaveSpawnCounts.length) ? primaryWaveSpawnCounts[wave] : 0;
        spawnN(count);
    }

    private void spawnBatch() {
        spawnN(1 + level.getRandom().nextInt(2));
    }

    private int spawnInterval() {
        return switch (level.getDifficulty()) {
            case HARD -> 80;
            case NORMAL -> 140;
            default -> 200;
        };
    }

    private void spawnN(int count) {

        if (primary.getSpawns().isEmpty()) {
            PvZMod.LOGGER.info("[Invasion] spawnN({}) skipped: primary has no spawns", count);
            return;
        }

        RandomSource rand = level.getRandom();
        int ok = 0, noPos = 0, badPlace = 0, noEntity = 0;
        for (int i = 0; i < count; i++) {
            ISpawnType st = pickSpawn(primary, rand);
            if (st == null) continue;
            switch (spawnOne(st)) {
                case OK -> ok++;
                case NO_POS -> noPos++;
                case BAD_PLACE -> badPlace++;
                case NO_ENTITY -> noEntity++;
            }
        }
        if (ok == 0) {
            PvZMod.LOGGER.debug("[Invasion] spawnN({}) all failed: noPos={}, badPlace={}, noEntity={}",
                    count, noPos, badPlace, noEntity);
        } else if (ok < count) {
            PvZMod.LOGGER.debug("[Invasion] spawnN({}) partial: ok={}, noPos={}, badPlace={}, noEntity={}",
                    count, ok, noPos, badPlace, noEntity);
        }



    }

    private ISpawnType pickSpawn(IInvasionType type, RandomSource rand) {
        List<ISpawnType> list = new ArrayList<>();
        int total = 0;
        int playerLevel = pdm.getResource(Resources.TREE_LVL);
        for (ISpawnType s : type.getSpawns()) {
            if (s.getInvasionLevel() <= playerLevel + 30) {
                list.add(s);
                total += s.getWeight();
            }
        }
        if (list.isEmpty()) return null;
        int pick = rand.nextInt(total);
        for (ISpawnType s : list) {
            pick -= s.getWeight();
            if (pick < 0) return s;
        }
        return list.get(0);
    }

    private SpawnResult  spawnOne(ISpawnType st) {
        RandomSource rand = level.getRandom();
        BlockPos base = player.blockPosition();
        for (int attempt = 0; attempt < 8; attempt++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double dist = 16 + rand.nextDouble() * 8;
            int dx = (int) Math.round(Math.cos(angle) * dist);
            int dz = (int) Math.round(Math.sin(angle) * dist);
            BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos(
                    base.getX() + dx, base.getY(), base.getZ() + dz);

            BlockPos pos = findGroundPos(m);
            if (pos == null) continue;
            if (!st.checkPos(level, pos)) continue;

            Entity entity = createFromSpawnType(st);
            if (entity == null) return SpawnResult.NO_ENTITY;   // 单条失败不放弃整批

            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            if (entity instanceof Mob mob) {
                mob.setPersistenceRequired();
                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos),
                        EntitySpawnReason.EVENT, null);
            }
            level.addFreshEntity(entity);
            spawnedEntities.add(entity.getUUID());

            PvZMod.LOGGER.debug("[Invasion] spawned {} at {} (place={})",
                    entity.getType(), pos, st.getPlaceType());

            return SpawnResult.OK;
        }
        return SpawnResult.NO_POS;
    }

    @Nullable
    private Entity createFromSpawnType(ISpawnType st) {
        final EntityType<?> type = st.getEntityType();
        final CompoundTag nbt = st.getNbt();

        final CompoundTag tag;
        if (nbt == null || nbt.isEmpty()) {
            tag = new CompoundTag();
        } else {
            tag = nbt.copy();
            tag.remove("UUID");
            tag.remove("Motion");
            tag.remove("Pos");
            tag.remove("id");
        }
        final ListTag posTag = new ListTag();
        posTag.add(DoubleTag.valueOf(0.5D));
        posTag.add(DoubleTag.valueOf(0.0D));
        posTag.add(DoubleTag.valueOf(0.5D));
        tag.put("Pos", posTag);

        return EntityType.loadEntityRecursive(
                type, tag, level, EntitySpawnReason.EVENT, e -> e);
    }

    @Nullable
    private BlockPos findGroundPos(BlockPos.MutableBlockPos base) {
        for (int dy = 6; dy >= -8; dy--) {
            BlockPos p = base.offset(0, dy, 0);
            if (!level.getBlockState(p).isAir()) continue;          // 本格空气
            if (!level.getBlockState(p.above()).isAir()) continue;  // 上方空气（容纳实体）
            if (!level.getBlockState(p.below()).isSolid()) continue; // 下方实心（可站）
            if (!level.canSeeSky(p)) continue;
            return p.immutable();
        }
        return null;
    }

    /* ---------------- 任务 ---------------- */

    private IMissionType pickMission() {
        RandomSource rand = level.getRandom();
        List<IMissionType> pool = new ArrayList<>(MissionTypes.all().values());
        if (pool.isEmpty()) return null;
        int total = 0;
        for (IMissionType m : pool) total += Math.max(1, m.getWeight());
        int pick = rand.nextInt(total);
        for (IMissionType m : pool) {
            pick -= Math.max(1, m.getWeight());
            if (pick < 0) return m;
        }
        return pool.get(0);
    }

    private void tickMission() {
        if (missionType == null) return;
        if (++missionTick % 20 != 0) return;

        boolean stageChanged = false;
        while (missionStage < missionType.getMaxStage()) {
            if (missionValue < missionType.getRequiredValue(missionStage)) break;
            final int completedStage = missionStage;
            final IMissionType completedType = missionType;
            missionType.onStageComplete(player, completedStage);
            MissionTypes.defaultReward(player, completedStage);
            NeoForge.EVENT_BUS.post(new MissionStageCompleteEvent(player, completedType, completedStage));
            PacketDistributor.sendToPlayer(player,
                    new MissionStagePayload(completedType.getID(), completedStage));
            missionStage++;
            stageChanged = true;
        }

        boolean missionReset = false;
        if (missionStage >= missionType.getMaxStage()) {
            pdm.addResource(Resources.MISSION_FINISH_TIME, 1);
            this.missionType = pickMission();
            this.missionStage = 0;
            this.missionValue = 0;
            this.maxKillInSecond = 0;
            this.killInSecond = 0;
            this.killResetTick = 0;
            missionReset = true;
        }

        if (stageChanged || missionReset) {
            publishSnapshot();
        }
    }

    /* ---------------- 快照发布 ---------------- */

    private void publishSnapshot() {
        InvasionSnapshot snapshot = buildSnapshot();
        player.setData(PvZAttachments.INVASION_SNAPSHOT, snapshot);
        if (!player.isRemoved() && player.connection != null) {
            player.syncData(PvZAttachments.INVASION_SNAPSHOT);
        }
    }

    private InvasionSnapshot buildSnapshot() {
        return new InvasionSnapshot(
                this.running,
                Optional.ofNullable(this.primary).map(IInvasionType::getID),
                this.assists.stream().map(IInvasionType::getID).toList(),
                this.waveIndex,
                toIntList(this.primaryWaveTimes),
                toIntList(this.primaryWaveSpawnCounts),
                Optional.ofNullable(this.missionType).map(IMissionType::getID),
                this.missionStage,
                this.missionValue,
                this.tick,
                Optional.ofNullable(effectiveFog())
                        .filter(InvasionFog.class::isInstance)
                        .map(InvasionFog.class::cast)
        );
    }

    private static List<Integer> toIntList(int[] array) {
        List<Integer> list = new ArrayList<>(array.length);
        for (int v : array) list.add(v);
        return list;
    }

    public void forgetEntity(UUID uuid) { spawnedEntities.remove(uuid); }
}