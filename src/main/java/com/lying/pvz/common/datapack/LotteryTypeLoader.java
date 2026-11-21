package com.lying.pvz.common.datapack;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.lottery.Lottery;
import com.lying.pvz.common.lottery.LotteryImpl;
import com.lying.pvz.common.lottery.LotteryJson;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 奖池数据包加载器。资源目录 {@code data/<ns>/lotterys/*.json}。
 * <p>基类已用 {@code ConditionalOps} 处理 {@code neoforge:conditions}，解析交给 {@link LotteryJson#CODEC}。
 *
 * <p>采用「先构建、后整体替换」策略：{@link #LOTTERIES} 始终指向一个不可变 map，
 * 避免重载过程中外部读到半空状态。
 */
public class LotteryTypeLoader extends SimpleJsonResourceReloadListener<LotteryJson> {

    /** 不可变 map，每次 {@link #apply} 原子替换，外部永远读到完整一致的数据。 */
    private static volatile Map<Identifier, Lottery> LOTTERIES = Map.of();

    public static final String NAME = "lottery";
    private static final FileToIdConverter LISTER = FileToIdConverter.json(NAME + "s");

    public LotteryTypeLoader() {
        super(LotteryJson.CODEC, LISTER);
    }

    @Override
    protected void apply(Map<Identifier, LotteryJson> map, ResourceManager manager, ProfilerFiller profiler) {
        final Map<Identifier, Lottery> fresh = new HashMap<>(map.size());
        map.forEach((id, json) -> {
            final Lottery lottery = LotteryImpl.from(id, json);
            if (lottery != null) {
                fresh.put(id, lottery);
            }
        });
        LOTTERIES = Map.copyOf(fresh);
        PvZMod.LOGGER.info("Loaded {} custom lottery type", fresh.size());
    }

    public static Optional<Lottery> getLotteryType(Identifier id) {
        return Optional.ofNullable(LOTTERIES.get(id));
    }

    public static Map<Identifier, Lottery> getLotteries() {
        return LOTTERIES;
    }
}