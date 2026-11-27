package com.lying.pvz.data.bootstrap.worldgen.structures;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

public class PvZStructureSets {

    /**
     * 合并为单 set 后 spacing 必须 ≥ 最大结构的世界跨度。
     * DaveVilla 是 2×2 × PIECE_SPAN(32) = 64，所以下限 64。
     * 这里取 6 个原配置的最大值，避免比原来更稀疏；再与 64 取大。
     */
    private static final int PVZ_SPACING = 100;

    private static final int PVZ_SEPARATION = PVZ_SPACING / 2;
    private static final int PVZ_SALT       = 998244353;

    /**
     * 岛心建筑 set 的 spacing：取 1、separation 取 0 使每个区块都成为候选
     * （{@code floorDiv(x,1)=x}，偏移 {@code nextInt(1)=0} 恒 0），
     * 再靠 {@code PvZFixedPositionStructure} 在目标坐标区块精确生成。
     */
    private static final int ISLAND_SPACING = 1;
    private static final int ISLAND_SEPARATION = 0;
    private static final int ISLAND_SALT = 0;

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(
                ResourceKey.create(Registries.STRUCTURE_SET, StringUtil.id("pvz_structures")),
                new StructureSet(
                        List.of(
                                StructureSet.entry(structures.getOrThrow(PvZStructures.BUCKET_HOUSE), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.DOLPHIN_HOUSE), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.GRAVE_HOUSE), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.YETI_HOUSE), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.DAVE_VILLA), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.SUN_TEMPLE), 1)
                        ),
                        new RandomSpreadStructurePlacement(PVZ_SPACING, PVZ_SEPARATION, RandomSpreadType.LINEAR,PVZ_SALT)
                )
        );

        // 四主题岛心建筑：spacing=1 使每个区块被轮询，固定坐标结构在各岛心精确生成一座。
        context.register(
                ResourceKey.create(Registries.STRUCTURE_SET, StringUtil.id("pvz_island_structures")),
                new StructureSet(
                        List.of(
                                StructureSet.entry(structures.getOrThrow(PvZStructures.EGYPT), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.HARBOR), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.WILD_WEST), 1),
                                StructureSet.entry(structures.getOrThrow(PvZStructures.FUTURE_WORLD), 1)
                        ),
                        new RandomSpreadStructurePlacement(ISLAND_SPACING, ISLAND_SEPARATION, RandomSpreadType.LINEAR, ISLAND_SALT)
                )
        );
    }
}