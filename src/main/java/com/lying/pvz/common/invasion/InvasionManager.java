package com.lying.pvz.common.invasion;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.PvZMod;
import com.lying.pvz.api.event.invasion.InvasionEndEvent;
import com.lying.pvz.api.invasion.*;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.invasion.type.MissionTypes;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

import java.util.*;
import java.util.function.Supplier;

public class InvasionManager implements IInvasionAPI {

    private static final Map<UUID, Invasion> ACTIVE = new HashMap<>();

    /** 命令预定的延迟入侵。键为玩家 UUID，值为待触发的入侵。 */
    private static final Map<UUID, PendingInvasion> PENDING = new HashMap<>();

    /** 通过 API 注册的类型。跨数据包 reload 存活。 */
    private static final Map<Identifier, IInvasionType> REGISTERED = new HashMap<>();

    /** 数据包加载的类型。每次 reload 重建。 */
    private static final Map<Identifier, IInvasionType> DATAPACK = new HashMap<>();

    /* ---------- 注册 ---------- */

    @Override
    public void registerInvasionType(Identifier id, Supplier<IInvasionType> f) {
        final IInvasionType prev = REGISTERED.put(id, f.get());
        if (prev != null) {
            PvZMod.LOGGER.warn("[Invasion] duplicate API registration for {}, overwritten", id);
        }
    }

    /** 数据包 reload 前调用：只清数据包部分，API 注册保留。 */
    public static void clearDatapack() {
        DATAPACK.clear();
    }

    /** 数据包加载后调用。 */
    public static void registerDatapack(Identifier id, IInvasionType t) {
        DATAPACK.put(id, t);
    }

    /** 合并视图：数据包优先于 API。调用方持有返回值，不随内部变化。 */
    private static Map<Identifier, IInvasionType> merged() {
        if (REGISTERED.isEmpty()) return new HashMap<>(DATAPACK);
        if (DATAPACK.isEmpty()) return new HashMap<>(REGISTERED);
        final Map<Identifier, IInvasionType> result = new HashMap<>(REGISTERED);
        for (var e : DATAPACK.entrySet()) {
            if (result.containsKey(e.getKey())) {
                PvZMod.LOGGER.warn("[Invasion] datapack {} overrides API registration", e.getKey());
            }
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }

    public static int loadedCount() {
        if (REGISTERED.isEmpty()) return DATAPACK.size();
        if (DATAPACK.isEmpty()) return REGISTERED.size();
        final Set<Identifier> keys = new HashSet<>(REGISTERED.keySet());
        keys.addAll(DATAPACK.keySet());
        return keys.size();
    }

    public static int apiCount() { return REGISTERED.size(); }
    public static int datapackCount() { return DATAPACK.size(); }

    @Override
    public void registerSpawnType(Identifier id, Supplier<ISpawnType> f) {
        // no-op；保留 API 稳定
    }

    @Override
    public void registerMissionType(Identifier id, Supplier<IMissionType> f) {
        MissionTypes.register(f.get());
    }

    /* ---------- 查询 ---------- */

    @Override
    public Optional<IInvasion> getInvasion(ServerPlayer player) {
        return Optional.ofNullable(ACTIVE.get(player.getUUID()));
    }

    @Override
    public Optional<IInvasion> getInvasion(ServerLevel level, UUID owner) {
        return Optional.ofNullable(ACTIVE.get(owner));
    }

    @Override
    public boolean isInInvasion(ServerPlayer player) { return ACTIVE.containsKey(player.getUUID()); }

    @Override
    public Map<Identifier, IInvasionType> invasionTypes() {
        // 契约要求"不可变快照"——merged() 已返回新 Map，再包一层 unmodifiable
        return Collections.unmodifiableMap(merged());
    }

    @Override
    public Map<Identifier, IMissionType> missionTypes() {
        return Collections.unmodifiableMap(new HashMap<>(MissionTypes.all()));
    }

    /* ---------- 手动控制 ---------- */

    @Override
    public void forceEnable(ServerPlayer player, Identifier invasionID) {
        IInvasionType type = merged().get(invasionID);
        if (type == null) return;
        startInvasion(player, type);
    }

    /** 是否已有进行中的入侵。 */
    public static boolean hasActive(ServerPlayer player) {
        Invasion inv = ACTIVE.get(player.getUUID());
        return inv != null && inv.isRunning();
    }

    /**
     * 立即触发入侵。已有运行中的入侵（自然或命令）时失败返回 {@code false}，
     * 不覆盖——同一玩家同一时刻只允许一个入侵。
     */
    private static boolean startInvasion(ServerPlayer player, IInvasionType type) {
        if (hasActive(player)) return false;

        Invasion inv = new Invasion(player);
        inv.forceEnableWith(type);

        if (!inv.isRunning()) {
            ACTIVE.remove(player.getUUID());
            return false;
        }
        return true;
    }

    /** 预定一个入侵，在 {@code delayTicks} tick 后触发。 */
    public static void scheduleInvasion(ServerPlayer player, IInvasionType type, int delayTicks) {
        if (type == null) return;
        PENDING.put(player.getUUID(), new PendingInvasion(player, type, Math.max(0, delayTicks)));
    }

    /** 每 tick 推进预定队列；到点后触发。 */
    public static void tickScheduled() {
        if (PENDING.isEmpty()) return;
        for (Iterator<Map.Entry<UUID, PendingInvasion>> it = PENDING.entrySet().iterator(); it.hasNext();) {
            Map.Entry<UUID, PendingInvasion> entry = it.next();
            PendingInvasion pending = entry.getValue();
            ServerPlayer player = pending.player();
            if (player.isRemoved() || player.level().getServer() == null) {
                it.remove();
                continue;
            }
            if (pending.delay() <= 0) {
                startInvasion(player, pending.type());
                it.remove();
            } else {
                entry.setValue(new PendingInvasion(player, pending.type(), pending.delay() - 1));
            }
        }
    }

    @Override
    public void forceDisable(ServerPlayer player) {
        Invasion inv = ACTIVE.remove(player.getUUID());
        if (inv != null && inv.isRunning()) {
            inv.disableWith(InvasionEndEvent.Reason.COMMAND);
        }
        PlayerUtil.getOptManager(player).ifPresent(m -> m.setInvasionSafeDays(configSafeDays()));
    }

    @Override
    public int getSafeDaysRemaining(ServerPlayer player) {
        return player.getData(PvZAttachments.PLAYER_META).safeDays;
    }

    public static Invasion findOwning(Entity e) {
        for (Invasion inv : ACTIVE.values()) {
            if (inv.ownsEntity(e)) return inv;
        }
        return null;
    }

    /* ---------- 抽取 ---------- */

    public static IInvasionType pickPrimary(ServerPlayer player) {
        return pickWeighted(player, false);
    }

    public static List<IInvasionType> pickAssists(ServerPlayer player) {
        List<IInvasionType> result = new ArrayList<>();
        RandomSource rand = player.getRandom();
        final int level = PlayerUtil.getResource(player, Resources.TREE_LVL);
        final Map<Identifier, IInvasionType> pool = merged();   // 循环外取一次
        for (IInvasionType t : pool.values()) {
            if (!t.isAssist()) continue;
            if (t.getRequireLevel() > level) continue;
            if (!fogAllowed(t, level)) continue;
            if (rand.nextInt(Math.max(1, t.getTriggerChance())) == 0) result.add(t);
        }
        return result;
    }

    private static IInvasionType pickWeighted(ServerPlayer player, boolean assist) {
        List<IInvasionType> candidates = new ArrayList<>();
        int total = 0;
        final int level = PlayerUtil.getResource(player, Resources.TREE_LVL);
        final Map<Identifier, IInvasionType> pool = merged();   // 循环外取一次
        for (IInvasionType t : pool.values()) {
            if (t.isAssist() != assist) continue;
            if (t.getRequireLevel() > level) continue;
            if (!fogAllowed(t, level)) continue;
            candidates.add(t);
            total += Math.max(1, t.getTriggerChance());
        }
        if (candidates.isEmpty()) return null;
        int pick = player.getRandom().nextInt(total);
        for (IInvasionType t : candidates) {
            pick -= Math.max(1, t.getTriggerChance());
            if (pick < 0) return t;
        }
        return candidates.get(0);
    }

    /** 迷雾入侵需要玩家树等级 > 5（路灯花已解锁）。 */
    private static boolean fogAllowed(IInvasionType t, int level) {
        return t.getFog() == null || level > 5;
    }

    /* ---------- 生命周期 ---------- */

    public static void addActive(ServerPlayer player, Invasion inv) {
        ACTIVE.put(player.getUUID(), inv);
    }

    public static Invasion removeActive(ServerPlayer player) {
        return ACTIVE.remove(player.getUUID());
    }

    public static Invasion getActive(ServerPlayer player) {
        return ACTIVE.get(player.getUUID());
    }

    public static Invasion getActive(UUID uuid) {
        return ACTIVE.get(uuid);
    }

    public static void handlePlayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        Invasion inv = ACTIVE.get(newPlayer.getUUID());
        if (inv != null) {
            inv.refreshPlayer(newPlayer);
        }
    }

    public static void handlePlayerLogout(ServerPlayer player) {
        Invasion inv = ACTIVE.remove(player.getUUID());
        if (inv != null && inv.isRunning()) {
            inv.disableWith(InvasionEndEvent.Reason.LOGOUT);
        }
        PlayerUtil.getOptManager(player).ifPresent(m -> m.setInvasionSafeDays(configSafeDays()));
    }

    public static void handlePlayerDimensionChange(ServerPlayer player) {
        Invasion inv = ACTIVE.remove(player.getUUID());
        if (inv != null && inv.isRunning()) {
            inv.disableWith(InvasionEndEvent.Reason.DIMENSION);
        }
        PlayerUtil.getOptManager(player).ifPresent(m -> m.setInvasionSafeDays(configSafeDays()));
    }

    public static void clearAll() {
        for (Invasion inv : new ArrayList<>(ACTIVE.values())) {
            if (inv.isRunning()) {
                inv.disableWith(InvasionEndEvent.Reason.NATURAL);
            }
        }
        ACTIVE.clear();
    }

    /* ---------- 配置 ---------- */

    public static int configSafeDays() {
        try {
            return Math.max(0, PvZConfig.LOCAL_CONFIG.InvasionSettings.SafeDayLength.get());
        } catch (Throwable t) {
            return 3;
        }
    }

    /** 波次刷完后的超时宽限（单位：游戏天数）。0 表示禁用兜底。 */
    public static double configOvertimeGraceDays() {
        try {
            return Math.max(0D, PvZConfig.LOCAL_CONFIG.InvasionSettings.OvertimeGraceDays.get());
        } catch (Throwable t) {
            return 0.5D;
        }
    }

    public static void tickActive() {
        if (ACTIVE.isEmpty()) return;
        for (Invasion inv : new ArrayList<>(ACTIVE.values())) {
            if (inv.isRunning()) {
                inv.tick();
            }
        }
    }

    /** 待触发的入侵：目标玩家 + 入侵类型 + 剩余 tick。 */
    private record PendingInvasion(ServerPlayer player, IInvasionType type, int delay) {}
}