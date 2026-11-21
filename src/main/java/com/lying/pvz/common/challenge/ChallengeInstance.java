package com.lying.pvz.common.challenge;

import com.lying.pvz.api.raid.IChallenge;
import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.api.raid.IRewardDefinition;
import com.lying.pvz.utils.ConfigUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ChallengeInstance implements IChallenge {

    private final int instanceId;
    private final ServerLevel level;
    private final IChallengeDefinition definition;
    private final Identifier definitionId;
    private final BlockPos center;
    private final ServerBossEvent bossBar;
    @Nullable private final UUID ownerUUID;

    private ChallengeState state = ChallengeState.PREPARE;
    private int tick = 0;
    private int stopTick = 0;
    private int waitTick = 0;
    private int currentWave = 0;
    private int currentSpawn = 0;
    private int currentMaxLevel = 0;
    private boolean firstTick = false;
    private boolean activated = false;
    private boolean frozen = false;

    private final Set<Entity> raiders = new HashSet<>();
    private final Set<UUID> participants = new HashSet<>();
    private final Set<UUID> deadParticipants = new HashSet<>();
    private int noPlantTicks = 0;
    private boolean plantNearby = true;

    public ChallengeInstance(int instanceId, ServerLevel level,
                             IChallengeDefinition definition, Identifier definitionId,
                             BlockPos center, @Nullable UUID ownerUUID) {
        this.instanceId = instanceId;
        this.level = level;
        this.definition = definition;
        this.definitionId = definitionId;
        this.center = center;
        this.ownerUUID = ownerUUID;
        this.bossBar = createBossBar();
    }

    public ChallengeInstance(ServerLevel level, ChallengeSnapshot snapshot, IChallengeDefinition definition) {
        this.instanceId = snapshot.instanceId();
        this.level = level;
        this.definition = definition;
        this.definitionId = snapshot.definitionId();
        this.center = snapshot.center();
        this.ownerUUID = snapshot.ownerUUID();
        this.activated = snapshot.activated();
        this.state = ChallengeState.values()[snapshot.stateOrdinal()];
        this.tick = snapshot.tick();
        this.stopTick = snapshot.stopTick();
        this.waitTick = snapshot.waitTick();
        this.currentWave = snapshot.currentWave();
        this.currentSpawn = snapshot.currentSpawn();
        this.firstTick = snapshot.firstTick();
        this.participants.addAll(snapshot.participants());
        this.deadParticipants.addAll(snapshot.deadParticipants());
        this.bossBar = createBossBar();
        for (UUID uuid : snapshot.raiders()) {
            Entity e = level.getEntities().get(uuid);
            if (e != null) this.raiders.add(e);
        }
    }

    public static int raidRange() { return ConfigUtil.getRaidRange(); }
    public static int waitTime() { return ConfigUtil.getRaidWaitTime(); }

    private ServerBossEvent createBossBar() {
        final UUID barId = UUID.nameUUIDFromBytes(
                ("challenge:" + this.instanceId).getBytes(StandardCharsets.UTF_8));
        return new ServerBossEvent(barId, Component.empty(),
                BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    }

    @Override public Identifier getID() { return this.definitionId; }
    @Override public IChallengeDefinition getDefinition() { return this.definition; }
    @Override public BlockPos getCenter() { return this.center; }
    @Override public ServerLevel getWorld() { return this.level; }
    @Override public MutableComponent getName() { return this.definition.getTitle().copy(); }

    public int getInstanceId() { return this.instanceId; }
    public Identifier getDefinitionId() { return this.definitionId; }
    public ChallengeState getState() { return this.state; }
    public Set<Entity> getRaiders() { return this.raiders; }
    public int getCurrentWave() { return this.currentWave; }
    public int getCurrentMaxLevel() { return this.currentMaxLevel; }
    public ServerBossEvent getBossBar() { return this.bossBar; }

    public List<ServerPlayer> getPlayers() {
        return new ArrayList<>(this.bossBar.getPlayers());
    }

    public boolean isStopping() { return this.stopTick > 0; }
    public boolean isPreparing() { return this.state == ChallengeState.PREPARE; }
    public boolean isRunning() { return this.state == ChallengeState.RUNNING; }
    public boolean isVictory() { return this.state == ChallengeState.VICTORY; }
    public boolean isLoss() { return this.state == ChallengeState.LOSS; }
    public boolean isRemoving() { return this.state == ChallengeState.REMOVING; }

    void setState(ChallengeState s) { this.state = s; }
    void setTick(int t) { this.tick = t; }
    void setStopTick(int t) { this.stopTick = t; }
    void setWaitTick(int t) { this.waitTick = t; }
    void setCurrentWave(int w) { this.currentWave = w; }
    void setCurrentSpawn(int s) { this.currentSpawn = s; }
    void setCurrentMaxLevel(int l) { this.currentMaxLevel = l; }
    void setFirstTick(boolean b) { this.firstTick = b; }
    void incrementTick() { ++this.tick; }
    void incrementStopTick() { ++this.stopTick; }
    void incrementWaitTick() { ++this.waitTick; }
    void incrementCurrentSpawn() { ++this.currentSpawn; }
    void incrementCurrentWave() { ++this.currentWave; }
    int getTick() { return this.tick; }
    int getStopTick() { return this.stopTick; }
    int getWaitTick() { return this.waitTick; }
    int getCurrentSpawn() { return this.currentSpawn; }
    boolean isFirstTick() { return this.firstTick; }
    ServerLevel getLevel() { return this.level; }
    public @Nullable UUID getOwnerUUID() { return this.ownerUUID; }
    public boolean isActivated() { return this.activated; }
    void setActivated(boolean b) { this.activated = b; }
    public Set<UUID> getParticipants() { return this.participants; }
    public Set<UUID> getDeadParticipants() { return this.deadParticipants; }
    public boolean isFrozen() { return this.frozen; }
    void setFrozen(boolean b) { this.frozen = b; }
    int getNoPlantTicks() { return this.noPlantTicks; }
    void setNoPlantTicks(int t) { this.noPlantTicks = t; }
    boolean isPlantNearby() { return this.plantNearby; }
    void setPlantNearby(boolean b) { this.plantNearby = b; }

    public ChallengeSnapshot toSnapshot() {
        final List<UUID> raiderUUIDs = new ArrayList<>();
        for (Entity e : this.raiders) raiderUUIDs.add(e.getUUID());
        return new ChallengeSnapshot(
                this.instanceId, this.definitionId, this.center, this.state.ordinal(),
                this.tick, this.stopTick, this.waitTick, this.currentWave, this.currentSpawn,
                this.firstTick, this.activated, this.ownerUUID,
                raiderUUIDs, new ArrayList<>(this.participants), new ArrayList<>(this.deadParticipants));
    }
    static void playSound(ServerPlayer player, SoundEvent sound) {
        if (sound == null || player == null) return;
        player.playSound(sound, 1F, 1F);
    }

    public void broadcastReward(IRewardDefinition reward) {
        for (ServerPlayer p : getPlayers()) {
            reward.reward(p);
        }
        reward.rewardGlobally(this);
    }

    void cleanup() {
        this.bossBar.removeAllPlayers();
        for (Entity e : this.raiders) if (e != null && e.isAlive()) e.discard();
        this.raiders.clear();
    }

    public void forceRemove() {
        this.state = ChallengeState.REMOVING;
        this.cleanup();
    }
    /**
     * 标记为失败。幂等：已处于 LOSS / VICTORY / REMOVING 时不做任何改变。
     * 供 {@code ChallengeTicker.onPlayerDeath} 与 S3 各失败路径调用。
     */
    public void markLoss() {
        if (this.state == ChallengeState.LOSS
                || this.state == ChallengeState.VICTORY
                || this.state == ChallengeState.REMOVING) return;
        this.state = ChallengeState.LOSS;
        this.tick = 0;
        this.stopTick = 0;
    }
    void clearBossBarPlayers() {
        this.bossBar.removeAllPlayers();
    }
}