package com.lying.pvz.api.raid;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * 挑战（raid）子系统的对外 API。
 *
 * <p><b>集合返回值语义</b>：{@link #definitions()} 返回<b>不可变快照</b>——每次调用
 * 返回一份新 Map，调用方修改返回值不影响内部状态。与 {@code IPVZLookup} 保持一致。
 */
public interface IRaidAPI {

    /* 组件类型注册（mod bus 阶段一次） */

    void registerAmount(String name, MapCodec<? extends IAmountDefinition> codec);

    void registerPlacement(String name, MapCodec<? extends IPlacementDefinition> codec);

    void registerChallenge(String name, MapCodec<? extends IChallengeDefinition> codec);

    void registerWave(String name, MapCodec<? extends IWaveDefinition> codec);

    void registerSpawn(String name, MapCodec<? extends ISpawnDefinition> codec);

    void registerReward(String name, MapCodec<? extends IRewardDefinition> codec);

    /* 运行时 */

    boolean create(ServerLevel level, Identifier raidID, BlockPos pos, @Nullable ServerPlayer owner);


    boolean isRaider(ServerLevel level, Entity entity);

    Optional<IChallenge> getChallenge(ServerLevel level, Entity entity);

    Optional<IChallenge> getNearByRaid(ServerLevel level, BlockPos pos);

    /**
     * 已加载的全部挑战定义，<b>不可变快照</b>。
     */
    Map<Identifier, IChallengeDefinition> definitions();

    /**
     * 按 ID 查挑战定义。
     */
    Optional<IChallengeDefinition> getDefinition(Identifier id);
}