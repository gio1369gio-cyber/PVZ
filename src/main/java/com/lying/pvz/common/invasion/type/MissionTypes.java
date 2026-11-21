package com.lying.pvz.common.invasion.type;

import com.lying.pvz.api.invasion.IMissionType;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 内置 3 类任务 + 开放注册表。 */
public final class MissionTypes {

    private static final Map<Identifier, IMissionType> REGISTRY = new ConcurrentHashMap<>();

    public static final IMissionType KILL = simple("kill", 5, 50, 100, 200, 300, 500);
    public static final IMissionType INSTANT_KILL = simple("instant_kill", 5, 10, 20, 40, 60, 80);
    public static final IMissionType COLLECT_SUN = simple("collect_sun", 5, 5000, 10000, 15000, 20000, 25000);

    private MissionTypes() { }

    public static void bootstrap() {
        REGISTRY.clear();
        register(KILL);
        register(INSTANT_KILL);
        register(COLLECT_SUN);
    }

    /**
     * 默认奖励（旧 {@code MissionManager#rewardPlayer} 的等价实现）。
     *
     * <p>每个阶段：发升级消息 + 按阶段发钱/抽奖/宝石。
     * <b>音效不在此处播放</b>——由 {@code MissionStagePayload.Handler} 客户端统一处理，
     * 避免服务端 {@code playClientSound} 与客户端 handler 双发。
     */
    public static void defaultReward(ServerPlayer player, int stage) {
        PlayerUtil.sendMsgTo(player,
                Component.translatable("invasion.pvz.mission.finish", stage));
        switch (stage) {
            case 0 -> PlayerUtil.addResource(player, Resources.MONEY, 250);
            case 1 -> PlayerUtil.addResource(player, Resources.LOTTERY_CHANCE, 5);
            case 2 -> PlayerUtil.addResource(player, Resources.MONEY, 500);
            case 3 -> PlayerUtil.addResource(player, Resources.LOTTERY_CHANCE, 5);
            default -> PlayerUtil.addResource(player, Resources.GEM_NUM,
                    1 + player.getRandom().nextInt(2));
        }
    }

    public static void register(IMissionType t) {
        REGISTRY.put(t.getID(), t);
    }

    public static IMissionType get(Identifier id) {
        return REGISTRY.get(id);
    }

    public static Map<Identifier, IMissionType> all() {
        return REGISTRY;
    }

    private static IMissionType simple(String id, int maxStage, int... values) {
        return new IMissionType() {
            private final Identifier rid = Identifier.fromNamespaceAndPath("pvz", id);

            @Override public Identifier getID() { return rid; }

            @Override
            public MutableComponent getName() {
                return Component.translatable("invasion.pvz.mission." + id + ".name");
            }

            @Override public int getMaxStage() { return maxStage; }

            @Override
            public int getRequiredValue(int stage) {
                return (stage >= 0 && stage < values.length) ? values[stage] : Integer.MAX_VALUE;
            }

            @Override public int getWeight() { return 10; }
        };
    }
}