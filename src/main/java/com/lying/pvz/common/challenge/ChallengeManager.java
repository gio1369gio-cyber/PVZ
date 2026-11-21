package com.lying.pvz.common.challenge;

import com.lying.pvz.api.event.raid.RaidEvent;
import com.lying.pvz.api.raid.*;
import com.lying.pvz.common.impl.challenge.DefaultChallengeDefinition;
import com.lying.pvz.common.impl.challenge.DefaultSpawnDefinition;
import com.lying.pvz.common.impl.challenge.DefaultWaveDefinition;
import com.lying.pvz.common.impl.challenge.RaidCodecs;
import com.lying.pvz.common.impl.challenge.amount.ConstantAmount;
import com.lying.pvz.common.impl.challenge.amount.RandomAmount;
import com.lying.pvz.common.impl.challenge.placement.CenterPlacement;
import com.lying.pvz.common.impl.challenge.placement.OffsetPlacement;
import com.lying.pvz.common.impl.challenge.placement.OuterPlacement;
import com.lying.pvz.common.impl.challenge.reward.AdvancementReward;
import com.lying.pvz.common.impl.challenge.reward.ItemReward;
import com.lying.pvz.common.impl.challenge.reward.UnlockReward;
import com.lying.pvz.utils.ConfigUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.Nullable;
import java.util.*;

public class ChallengeManager implements IRaidAPI {

    /* 组件类型注册表 */
    private static final Map<String, MapCodec<? extends IAmountDefinition>> AMOUNT_CODECS = new HashMap<>();
    private static final Map<String, MapCodec<? extends IPlacementDefinition>> PLACEMENT_CODECS = new HashMap<>();
    private static final Map<String, MapCodec<? extends IChallengeDefinition>> CHALLENGE_CODECS = new HashMap<>();
    private static final Map<String, MapCodec<? extends IWaveDefinition>> WAVE_CODECS = new HashMap<>();
    private static final Map<String, MapCodec<? extends ISpawnDefinition>> SPAWN_CODECS = new HashMap<>();
    private static final Map<String, MapCodec<? extends IRewardDefinition>> REWARD_CODECS = new HashMap<>();

    /* 已加载的定义 */
    private static final Map<Identifier, IChallengeDefinition> DEFINITIONS = new HashMap<>();

    /**
     * 主模组在 mod bus 阶段调用一次（实例方法，走 IRaidAPI 注册路径）。
     */
    public void initRegistry() {
        registerAmount(ConstantAmount.TYPE, ConstantAmount.CODEC);
        registerAmount(RandomAmount.TYPE, RandomAmount.CODEC);

        registerPlacement(CenterPlacement.TYPE, CenterPlacement.CODEC);
        registerPlacement(OffsetPlacement.TYPE, OffsetPlacement.CODEC);
        registerPlacement(OuterPlacement.TYPE, OuterPlacement.CODEC);

        registerChallenge(DefaultChallengeDefinition.TYPE, DefaultChallengeDefinition.CODEC);
        registerWave(DefaultWaveDefinition.TYPE, DefaultWaveDefinition.CODEC);
        registerSpawn(DefaultSpawnDefinition.TYPE, DefaultSpawnDefinition.CODEC);

        registerReward(ItemReward.TYPE, ItemReward.CODEC);
        registerReward(AdvancementReward.TYPE, AdvancementReward.CODEC);
        registerReward(UnlockReward.TYPE, UnlockReward.CODEC);
    }

    /* ---------------- dispatch Codec 生成（静态，供 loader / 组件引用） ---------------- */

    public static Codec<IAmountDefinition> amountCodec() {
        return RaidCodecs.dispatch("type", AMOUNT_CODECS, IAmountDefinition::type);
    }

    public static Codec<IPlacementDefinition> placementCodec() {
        return RaidCodecs.dispatch("type", PLACEMENT_CODECS, IPlacementDefinition::type);
    }

    public static Codec<IRewardDefinition> rewardCodec() {
        return RaidCodecs.dispatch("type", REWARD_CODECS, IRewardDefinition::type);
    }

    public static Codec<IWaveDefinition> waveCodec() {
        return RaidCodecs.dispatch("type", WAVE_CODECS, IWaveDefinition::type);
    }

    public static Codec<ISpawnDefinition> spawnCodec() {
        return RaidCodecs.dispatch("type", SPAWN_CODECS, ISpawnDefinition::type);
    }

    public static Codec<IChallengeDefinition> challengeCodec() {
        return RaidCodecs.dispatch("type", CHALLENGE_CODECS, IChallengeDefinition::type);
    }

    /* ---------------- loader 回调 ---------------- */

    public static void clearLoaded() {
        DEFINITIONS.clear();
    }

    public static void registerLoaded(Identifier id, IChallengeDefinition definition) {
        DEFINITIONS.put(id, definition);
    }

    public static IChallengeDefinition getDefinitionStatic(Identifier id) {
        return DEFINITIONS.get(id);
    }

    public static int loadedCount() {
        return DEFINITIONS.size();
    }

    /* ---------------- IRaidAPI: 注册 ---------------- */

    @Override
    public void registerAmount(String n, MapCodec<? extends IAmountDefinition> c) {
        AMOUNT_CODECS.put(n, c);
    }

    @Override
    public void registerPlacement(String n, MapCodec<? extends IPlacementDefinition> c) {
        PLACEMENT_CODECS.put(n, c);
    }

    @Override
    public void registerChallenge(String n, MapCodec<? extends IChallengeDefinition> c) {
        CHALLENGE_CODECS.put(n, c);
    }

    @Override
    public void registerWave(String n, MapCodec<? extends IWaveDefinition> c) {
        WAVE_CODECS.put(n, c);
    }

    @Override
    public void registerSpawn(String n, MapCodec<? extends ISpawnDefinition> c) {
        SPAWN_CODECS.put(n, c);
    }

    @Override
    public void registerReward(String n, MapCodec<? extends IRewardDefinition> c) {
        REWARD_CODECS.put(n, c);
    }

    /* ---------------- IRaidAPI: 运行时 ---------------- */

    @Override
    public boolean create(ServerLevel level, Identifier raidID, BlockPos pos, @Nullable ServerPlayer owner) {
        final IChallengeDefinition definition = DEFINITIONS.get(raidID);
        if (definition == null) return false;
        if (!definition.isSuitableDimension(level.dimension())) return false;
        if (getNearByRaid(level, pos).isPresent()) return false;

        final ChallengeData data = ChallengeData.get(level);
        final int id = data.nextId();
        final UUID ownerUUID = owner != null ? owner.getUUID() : null;
        final ChallengeInstance instance = new ChallengeInstance(id, level, definition, raidID, pos, ownerUUID);
        data.addChallenge(instance);
        NeoForge.EVENT_BUS.post(new RaidEvent.RaidStartEvent(instance));
        return true;
    }

    @Override
    public boolean isRaider(ServerLevel level, Entity entity) {
        return getChallenge(level, entity).isPresent();
    }

    @Override
    public Optional<IChallenge> getChallenge(ServerLevel level, Entity entity) {
        for (ChallengeInstance c : ChallengeData.get(level).getChallenges()) {
            if (c.getRaiders().contains(entity)) return Optional.of(c);
        }
        return Optional.empty();
    }

    @Override
    public Optional<IChallenge> getNearByRaid(ServerLevel level, BlockPos pos) {
        for (ChallengeInstance c : ChallengeData.get(level).getChallenges()) {
            final BlockPos center = c.getCenter();
            if (Math.abs(center.getX() - pos.getX()) < ChallengeInstance.raidRange()
                    && Math.abs(center.getY() - pos.getY()) < ChallengeInstance.raidRange()
                    && Math.abs(center.getZ() - pos.getZ()) < ChallengeInstance.raidRange()) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public Map<Identifier, IChallengeDefinition> definitions() {
        return Collections.unmodifiableMap(DEFINITIONS);
    }

    @Override
    public Optional<IChallengeDefinition> getDefinition(Identifier id) {
        return Optional.ofNullable(DEFINITIONS.get(id));
    }
    /**
     * 每 tick 驱动全部挑战。
     *
     * <p>由主模组在 {@code LevelTickEvent.Post}（或 {@code ServerTickEvent.Post}）
     * 里对 {@link ServerLevel} 调用一次。
     */
    public static void tickChallenges(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        final ChallengeData data = ChallengeData.get(serverLevel);
        if (!ConfigUtil.isRaidEnable()) {
            for (ChallengeInstance c : data.getChallenges()) c.forceRemove();
            data.tick(serverLevel);
            return;
        }
        data.tick(serverLevel);
    }
    public static void replaceLoaded(Map<Identifier, IChallengeDefinition> defs) {
        DEFINITIONS.clear();
        DEFINITIONS.putAll(defs);
    }
}