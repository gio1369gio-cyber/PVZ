package com.lying.pvz.api.invasion;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * 入侵子系统的对外 API。
 *
 * <p><b>集合返回值语义</b>：{@link #invasionTypes()} / {@link #missionTypes()}
 * 返回<b>不可变快照</b>——每次调用返回一份新 Map，调用方修改返回值不影响内部状态。
 * 与 {@code IPVZLookup} 保持一致。
 */
public interface IInvasionAPI {

    void registerInvasionType(Identifier id, Supplier<IInvasionType> factory);

    void registerSpawnType(Identifier id, Supplier<ISpawnType> factory);

    void registerMissionType(Identifier id, Supplier<IMissionType> factory);

    Optional<IInvasion> getInvasion(ServerPlayer player);

    Optional<IInvasion> getInvasion(ServerLevel level, UUID owner);

    boolean isInInvasion(ServerPlayer player);

    /**
     * 全部入侵类型，<b>不可变快照</b>。
     */
    Map<Identifier, IInvasionType> invasionTypes();

    /**
     * 全部任务类型，<b>不可变快照</b>。
     */
    Map<Identifier, IMissionType> missionTypes();

    void forceEnable(ServerPlayer player, Identifier invasionID);

    void forceDisable(ServerPlayer player);
    /**
     * 剩余安全天数。0 表示即将/已经进入入侵风险。
     */
    int getSafeDaysRemaining(ServerPlayer player);
}