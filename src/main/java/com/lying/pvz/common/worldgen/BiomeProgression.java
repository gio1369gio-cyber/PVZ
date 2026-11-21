package com.lying.pvz.common.worldgen;

import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

/**
 * PVZ 世界群系的解锁顺序链：远古埃及 → 海盗港湾 → 狂野西部 → 功夫世界。
 *
 * <p>纯静态定义，无状态。供 BOSS 解锁事件、召唤物品、TimeKeyItem 传送共用。
 * 起始群系（埃及）永远解锁；击败 BOSS 后按此链解锁下一个。
 */
public final class BiomeProgression {

    /** 解锁顺序，索引即阶段号。 */
    public static final List<ResourceKey<Biome>> ORDER = List.of(
            PvZBiome.ANCIENT_EGYPT,
            PvZBiome.PIRATE_HARBOR,
            PvZBiome.WILD_WEST,
            PvZBiome.KUNG_FU_WORLD
    );

    private BiomeProgression() {}

    /** 起始群系（索引 0）。 */
    public static ResourceKey<Biome> start() {
        return ORDER.get(0);
    }

    /** 群系在链中的阶段号（0 起）；不属于链则返回 -1。 */
    public static int indexOf(ResourceKey<Biome> biome) {
        return ORDER.indexOf(biome);
    }

    public static int indexOf(Holder<Biome> biome) {
        if (biome == null) return -1;
        for (int i = 0; i < ORDER.size(); i++) {
            if (biome.is(ORDER.get(i))) return i;
        }
        return -1;
    }

    /** 是否属于 PVZ 解锁链中的群系。 */
    public static boolean isProgressionBiome(ResourceKey<Biome> biome) {
        return indexOf(biome) >= 0;
    }

    public static boolean isProgressionBiome(Holder<Biome> biome) {
        return indexOf(biome) >= 0;
    }

    /**
     * 给定当前群系，返回应解锁的下一个群系；已是最后一个或无匹配则返回 null。
     * 用于 BOSS 击杀后推进解锁。
     */
    public static ResourceKey<Biome> nextAfter(ResourceKey<Biome> current) {
        int i = indexOf(current);
        return i >= 0 && i + 1 < ORDER.size() ? ORDER.get(i + 1) : null;
    }

    /** 是否是链的最后一个群系（功夫世界）。 */
    public static boolean isLast(ResourceKey<Biome> biome) {
        return indexOf(biome) == ORDER.size() - 1;
    }
}
