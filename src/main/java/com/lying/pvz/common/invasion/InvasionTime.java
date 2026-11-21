package com.lying.pvz.common.invasion;

import com.lying.pvz.PvZMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.clock.ClockInstance;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.timeline.Timelines;

/**
 * 入侵子系统的时间抽象。所有时间判定唯一入口。
 *
 * <p>不依赖 periodTicks（{@link ClockInstance} 未暴露），
 * 早晨判定走 timeline 的 {@link ClockTimeMarkers#DAY} 周期 marker。
 */
public final class InvasionTime {
    private static final int PERIOD_FALLBACK = 24000;

    private InvasionTime() {}

    public static Holder<WorldClock> clock(MinecraftServer server) {
        return server.registryAccess()
                .lookupOrThrow(Registries.WORLD_CLOCK)
                .getOrThrow(WorldClocks.OVERWORLD);
    }

    public static ClockInstance instance(ServerLevel level) {
        MinecraftServer server = level.getServer();
        return server.clockManager().getInstance(clock(server));
    }

    /** 单调 totalTicks，跨天不清零。 */
    public static long totalTicks(ServerLevel level) {
        return instance(level).totalTicks();
    }

    /** 当前 tick 是否为"早晨"（timeline 的 DAY marker）。 */
    public static boolean isMorning(ServerLevel level) {
        return isAtMarker(level, ClockTimeMarkers.DAY);
    }

    public static boolean isAtMarker(ServerLevel level, ResourceKey<ClockTimeMarker> marker) {
        MinecraftServer server = level.getServer();
        return server.clockManager().isAtTimeMarker(clock(server), marker);
    }
    /** 一天的 tick 数。优先 timeline 注册值；timeline 缺失或 period 为空时兜底。 */
    public static int periodTicks(ServerLevel level) {
        try {
            final int p = level.getServer().registryAccess()
                    .lookupOrThrow(Registries.TIMELINE)
                    .getOrThrow(Timelines.OVERWORLD_DAY)
                    .value()
                    .periodTicks()
                    .orElseGet(() -> {
                        PvZMod.LOGGER.warn("[InvasionTime] OVERWORLD_DAY period empty, fallback 24000");
                        return PERIOD_FALLBACK;
                    });
            if (p <= 0) {
                PvZMod.LOGGER.warn("[InvasionTime] OVERWORLD_DAY period <= 0 ({}), fallback 24000", p);
                return PERIOD_FALLBACK;
            }
            return p;
        } catch (Throwable t) {
            PvZMod.LOGGER.warn("[InvasionTime] timeline lookup failed, fallback 24000", t);
            return PERIOD_FALLBACK;
        }
    }
}