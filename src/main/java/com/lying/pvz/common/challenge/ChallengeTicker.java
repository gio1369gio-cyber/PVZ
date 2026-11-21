package com.lying.pvz.common.challenge;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.event.raid.RaidEvent;
import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.api.raid.IPlacementDefinition;
import com.lying.pvz.api.raid.ISpawnDefinition;
import com.lying.pvz.common.impl.challenge.placement.CenterPlacement;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.common.level.entity.ai.goal.ChallengeMoveGoal;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

public final class ChallengeTicker {

    private static final int NO_PLANT_CHECK_INTERVAL = 40;   // 检测频率
    private static final int NO_PLANT_GRACE_TICKS = 100;     // 5 秒宽限


    private ChallengeTicker() { }

    public static void tick(ChallengeInstance instance) {
        if (instance.isRemoving()) return;

        // ① 本维度全员离线 → 冻结（首次进入时清怪 + 回退到当前波开始）
        if (instance.getWorld().players().isEmpty()) {
            if (!instance.isFrozen()) {
                freeze(instance);
                instance.setFrozen(true);
            }
            return;
        }
        instance.setFrozen(false);

        // ② 和平模式 → 直接销毁（不走 LOSS）
        if (instance.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            beginRemoving(instance);
            return;
        }

        // ③ definition 丢失 → 直接销毁
        final IChallengeDefinition definition = instance.getDefinition();
        if (definition == null) {
            beginRemoving(instance);
            return;
        }

        // ④ 离场倒计时（单点累加，只在 RUNNING + activated + bossBar 空时）
        if (instance.isActivated()
                && instance.getState() == ChallengeState.RUNNING
                && instance.getBossBar().getPlayers().isEmpty()) {
            if (instance.getStopTick() == 0) {
                sendTooFarAway(instance);
            }
            instance.incrementStopTick();
            if (instance.getStopTick() >= ChallengeInstance.waitTime()) {
                instance.markLoss();
                fireLoss(instance, definition);
                return;
            }
        }
        // 注：归零在 updatePlayers 里（bossBar 非空时）

        tickBossBar(instance);

        switch (instance.getState()) {
            case PREPARE -> tickPreparing(instance, definition);
            case RUNNING -> tickRunning(instance, definition);
            case VICTORY -> tickVictory(instance, definition);
            case LOSS -> tickLoss(instance, definition);
            case REMOVING -> { return; }
        }

        if (!instance.isFirstTick()) {
            instance.setFirstTick(true);
            final SoundEvent sound = definition.getPrepareSound();
            for (ServerPlayer p : instance.getPlayers()) ChallengeInstance.playSound(p, sound);
        }

        instance.incrementTick();
    }

    private static void tickPreparing(ChallengeInstance instance, IChallengeDefinition definition) {
        if (instance.getTick() >= definition.getPrepareCD(instance.getCurrentWave())) {
            waveStart(instance, definition);
            instance.setWaitTick(0);
        }
    }

    private static void tickRunning(ChallengeInstance instance, IChallengeDefinition definition) {
        if (tickNoPlant(instance)) {
            instance.markLoss();
            fireLoss(instance, definition);
            return;
        }
        if (instance.getTick() >= definition.getLastDuration(instance.getCurrentWave())
                || canNextWave(instance, definition)) {
            if (instance.getWaitTick() > 60 || !canNextWave(instance, definition)) {
                advanceWave(instance, definition);
                if (instance.isLoss()) { fireLoss(instance, definition); return; }
                if (instance.isVictory()) { fireWin(instance, definition); return; }
                return;
            }
            instance.incrementWaitTick();
        }
        tickSpawns(instance, definition);
    }

    private static void tickVictory(ChallengeInstance instance, IChallengeDefinition definition) {
        if (instance.getTick() >= definition.getWinTick()) {
            beginRemoving(instance);
        }
    }

    private static void tickLoss(ChallengeInstance instance, IChallengeDefinition definition) {
        if (instance.getTick() >= definition.getLossTick()) {
            beginRemoving(instance);
        }
    }

    private static void tickSpawns(ChallengeInstance instance, IChallengeDefinition definition) {
        if (instance.getWorld().getDifficulty() == Difficulty.HARD) {
            if (instance.getTick() % 10 == 2) {
                int total = 0;
                for (ServerPlayer p : instance.getPlayers()) {
                    total += PlayerUtil.getResource(p, Resources.TREE_LVL);
                }
                instance.setCurrentMaxLevel(total);
            }
        } else {
            instance.setCurrentMaxLevel(0);
        }

        final List<ISpawnDefinition> spawns = definition.getSpawns(instance.getCurrentWave());
        while (instance.getCurrentSpawn() < spawns.size()
                && instance.getTick() >= spawns.get(instance.getCurrentSpawn()).getSpawnTick()) {
            spawnEntity(instance, definition, spawns.get(instance.getCurrentSpawn()));
            instance.incrementCurrentSpawn();
        }
        instance.getRaiders().removeIf(e -> !e.isAlive());
    }

    private static void spawnEntity(ChallengeInstance instance, IChallengeDefinition definition,
                                    ISpawnDefinition spawn) {
        final int count = spawn.rollAmount(instance.getWorld().getRandom());
        for (int i = 0; i < count; i++) {
            IPlacementDefinition placement = spawn.getPlacement();
            if (placement == null) placement = definition.getPlacement(instance.getCurrentWave());
            if (placement == null) placement = new CenterPlacement(1, true);

            final BlockPos pos = placement.getPlacePosition(instance.getWorld(), instance.getCenter());

            final CompoundTag tag = spawn.getNBT().copy();
            final ListTag posTag = new ListTag();
            posTag.add(DoubleTag.valueOf(pos.getX() + 0.5D));
            posTag.add(DoubleTag.valueOf(pos.getY()));
            posTag.add(DoubleTag.valueOf(pos.getZ() + 0.5D));
            tag.put("Pos", posTag);

            final Entity entity = EntityType.loadEntityRecursive(
                    spawn.getSpawnType(), tag, instance.getWorld(),
                    EntitySpawnReason.EVENT, e -> e);
            if (entity == null) continue;
            instance.getWorld().addFreshEntity(entity);
            instance.getRaiders().add(entity);

            entity.getData(PvZAttachments.CHALLENGE_DATA).setChallengeID(instance.getInstanceId());

            if (entity instanceof Mob mob) {
                mob.setPersistenceRequired();
                if (definition.shouldCloseToCenter()) {
                    mob.goalSelector.addGoal(0, new ChallengeMoveGoal(mob, instance));
                }
            }
            if (entity instanceof AbstractPAZEntity paz) {
                AbstractPAZEntity.randomInitSkills(paz,
                        Math.max(0, instance.getCurrentMaxLevel() - definition.getRecommendLevel()));
            }
        }
    }

    private static void advanceWave(ChallengeInstance instance, IChallengeDefinition definition) {
        instance.setTick(0);
        if (canNextWave(instance, definition)) {
            instance.setCurrentSpawn(0);
            instance.incrementCurrentWave();
            if (instance.getCurrentWave() >= definition.getTotalWaveCount()) {
                instance.setState(ChallengeState.VICTORY);
            } else {
                instance.setState(ChallengeState.PREPARE);
            }
        } else {
            instance.markLoss();
        }
    }

    private static boolean canNextWave(ChallengeInstance instance, IChallengeDefinition definition) {
        instance.getRaiders().removeIf(e -> !e.isAlive());
        return instance.getRaiders().isEmpty()
                && definition.isWaveFinish(instance.getCurrentWave(), instance.getCurrentSpawn());
    }

    private static void tickBossBar(ChallengeInstance instance) {
        if (instance.getTick() % 10 == 0 && !instance.getWorld().players().isEmpty()) {
            updatePlayers(instance);
        }

        final IChallengeDefinition c = instance.getDefinition();
        if (c == null) return;

        instance.getBossBar().setColor(c.getBarColor());
        if (instance.isPreparing()) {
            instance.getBossBar().setName(c.getTitle());
            final int cd = c.getPrepareCD(instance.getCurrentWave());
            instance.getBossBar().setProgress(cd > 0
                    ? Math.min(1F, (float) instance.getTick() / cd) : 0F);
        } else if (instance.isRunning()) {
            instance.getBossBar().setName(c.getTitle().copy()
                    .append(" - ")
                    .append(Component.translatable("event.minecraft.raid.raiders_remaining",
                            instance.getRaiders().size())));
            final int dur = c.getLastDuration(instance.getCurrentWave());
            instance.getBossBar().setProgress(dur > 0
                    ? Math.max(0F, 1F - (float) instance.getTick() / dur) : 0F);
        } else if (instance.isVictory()) {
            instance.getBossBar().setName(c.getTitle().copy().append(" - ").append(c.getWinTitle()));
            instance.getBossBar().setProgress(1F);
        } else if (instance.isLoss()) {
            instance.getBossBar().setName(c.getTitle().copy().append(" - ").append(c.getLossTitle()));
            instance.getBossBar().setProgress(1F);
        }
    }

    /**
     * bossBar 增删与参与者状态维护。
     *
     * <ul>
     *   <li>范围判定统一 {@code < raidRange()}，与 {@code ChallengeManager.getNearByRaid} 一致。</li>
     *   <li>用 UUID 比较，避免玩家重登产生的新 {@link ServerPlayer} 实例造成重复。</li>
     *   <li>进入 bossBar：加 {@code participants}，从 {@code deadParticipants} 移除（复活重进）。</li>
     *   <li>离开 bossBar：不改 {@code participants} / {@code deadParticipants}（历史保留）。</li>
     *   <li>不再设置 stopTick：{@code stopTick} 只由 {@link #tick} 顶部累加、本方法清零。</li>
     * </ul>
     */
    private static void updatePlayers(ChallengeInstance instance) {
        final ServerLevel level = instance.getWorld();
        final BlockPos center = instance.getCenter();
        final int range = ChallengeInstance.raidRange();

        final Map<UUID, ServerPlayer> inRange = new HashMap<>();
        for (ServerPlayer p : level.getPlayers(p -> p.isAlive()
                && Math.abs(p.getX() - center.getX()) < range
                && Math.abs(p.getY() - center.getY()) < range
                && Math.abs(p.getZ() - center.getZ()) < range)) {
            inRange.put(p.getUUID(), p);
        }

        // 1. 新进入者
        final Set<UUID> currentUUIDs = new HashSet<>();
        for (ServerPlayer p : instance.getBossBar().getPlayers()) {
            currentUUIDs.add(p.getUUID());
        }
        for (Map.Entry<UUID, ServerPlayer> e : inRange.entrySet()) {
            if (!currentUUIDs.contains(e.getKey())) {
                instance.getBossBar().addPlayer(e.getValue());
                instance.getParticipants().add(e.getKey());
                instance.getDeadParticipants().remove(e.getKey());
            }
        }

        // 2. 离开者（不改 participants / deadParticipants）
        for (ServerPlayer p : new ArrayList<>(instance.getBossBar().getPlayers())) {
            if (!inRange.containsKey(p.getUUID())) {
                instance.getBossBar().removePlayer(p);
            }
        }

        // 3. activated + stopTick 归零
        if (!instance.getBossBar().getPlayers().isEmpty()) {
            if (!instance.isActivated()) {
                instance.setActivated(true);
            }
            instance.setStopTick(0);
        }
    }

    private static void waveStart(ChallengeInstance instance, IChallengeDefinition definition) {
        instance.setTick(0);
        instance.setState(ChallengeState.RUNNING);
        instance.setNoPlantTicks(0);
        instance.setPlantNearby(true);
        for (ServerPlayer p : instance.getPlayers()) {
            if (definition.showRoundTitle()) {
                p.sendSystemMessage(Component.translatable(
                                "challenge.pvz.round", instance.getCurrentWave() + 1)
                        .withStyle(ChatFormatting.DARK_RED));
            }
            ChallengeInstance.playSound(p, definition.getStartWaveSound());
        }
    }

    private static void fireWin(ChallengeInstance instance, IChallengeDefinition definition) {
        instance.setTick(0);
        for (ServerPlayer p : instance.getPlayers()) {
            ChallengeInstance.playSound(p, definition.getWinSound());
            PvZTriggers.CHALLENGE.get().trigger(p, instance.getID().toString());
        }

        final RaidEvent.RaidWinEvent event = new RaidEvent.RaidWinEvent(instance);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;

        definition.getRewards().forEach(instance::broadcastReward);
    }

    private static void fireLoss(ChallengeInstance instance, IChallengeDefinition definition) {
        instance.setTick(0);
        for (ServerPlayer p : instance.getPlayers()) {
            ChallengeInstance.playSound(p, definition.getLossSound());
        }
        NeoForge.EVENT_BUS.post(new RaidEvent.RaidLossEvent(instance));
    }
    /**
     * 全员离线冻结：清怪 + bossBar 清空 + 回退到当前波开始。
     *
     * <p>只在 PREPARE / RUNNING 时回退；VICTORY / LOSS 保持状态（倒计时期间 tick 已冻结，恢复后继续）。
     * {@code currentWave} 保留不动，{@code tick} / {@code currentSpawn} / {@code waitTick} 归零，
     * 状态退回 {@code PREPARE}，恢复后走当前波的 {@code prepareCD} 重新开始。
     */
    private static void freeze(ChallengeInstance instance) {
        for (Entity e : instance.getRaiders()) {
            if (e != null && e.isAlive()) e.discard();
        }
        instance.getRaiders().clear();
        instance.getBossBar().removeAllPlayers();
        instance.setStopTick(0);

        if (instance.isRunning()) {
            instance.setTick(0);
            instance.setCurrentSpawn(0);
            instance.setWaitTick(0);
            instance.setState(ChallengeState.PREPARE);
        }
    }

    /**
     * 向所有历史参与者（{@code participants}）发送"离开范围"提示。
     * 仅在 stopTick 从 0 变为 1 的那一 tick 调用一次。
     */
    private static void sendTooFarAway(ChallengeInstance instance) {
        final ServerLevel level = instance.getWorld();
        for (UUID uuid : instance.getParticipants()) {
            final Player p = level.getPlayerByUUID(uuid);
            if (p != null) {
                p.sendSystemMessage(Component.translatable("challenge.pvz.too_far_away")
                        .withStyle(ChatFormatting.RED));
            }
        }
    }
    private static void beginRemoving(ChallengeInstance instance) {
        instance.forceRemove();
    }

    /**
     * 玩家死亡处理。由 {@code ChallengeEventHandler.onLivingDeath} 调用。
     *
     * <ul>
     *   <li>把玩家加入其所属挑战的 {@code deadParticipants}，从 bossBar 移除。</li>
     *   <li>若所有 participants 都已死亡（且 participants 非空），立即 {@link ChallengeInstance#markLoss()}。</li>
     *   <li>玩家可能同时参与多个挑战（跨维度），逐个处理。</li>
     * </ul>
     */
    public static void onPlayerDeath(ServerPlayer player) {
        final ServerLevel level = player.level();
        final ChallengeData data = ChallengeData.get(level);
        for (ChallengeInstance c : data.getChallenges()) {
            if (!c.getParticipants().contains(player.getUUID())) continue;

            c.getDeadParticipants().add(player.getUUID());
            c.getBossBar().removePlayer(player);

            if (!c.getParticipants().isEmpty()
                    && c.getDeadParticipants().containsAll(c.getParticipants())) {
                c.markLoss();
            }
        }
    }

    private static boolean tickNoPlant(ChallengeInstance instance) {
        if (instance.getTick() % NO_PLANT_CHECK_INTERVAL == 0) {
            instance.setPlantNearby(hasPlantNearby(instance));
        }
        if (instance.isPlantNearby()) {
            instance.setNoPlantTicks(0);
            return false;
        }
        instance.setNoPlantTicks(instance.getNoPlantTicks() + 1);
        return instance.getNoPlantTicks() >= NO_PLANT_GRACE_TICKS;
    }

    private static boolean hasPlantNearby(ChallengeInstance instance) {
        final int range = ChallengeInstance.raidRange();
        final BlockPos c = instance.getCenter();
        final AABB area = AABB.ofSize(
                new Vec3(c.getX() + 0.5D, c.getY() + 0.5D, c.getZ() + 0.5D),
                range * 2D, range * 2D, range * 2D);
        return !instance.getWorld()
                .getEntitiesOfClass(AbstractPAZEntity.class, area, ChallengeTicker::isPlant)
                .isEmpty();
    }

    private static boolean isPlant(AbstractPAZEntity paz) {
        return paz.getPAZType() instanceof IPlantType
                || paz.getEntityGroupType() == GroupType.PLANTS;
    }
}