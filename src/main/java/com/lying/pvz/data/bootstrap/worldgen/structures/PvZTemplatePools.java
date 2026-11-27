package com.lying.pvz.data.bootstrap.worldgen.structures;

import com.lying.pvz.common.worldgen.structure.PvZSinglePoolElement;
import com.lying.pvz.utils.StringUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

public class PvZTemplatePools {

    public static final ResourceKey<StructureTemplatePool> BUCKET_HOUSE = register("bucket_house");
    public static final ResourceKey<StructureTemplatePool> DOLPHIN_HOUSE = register("dolphin_house");
    public static final ResourceKey<StructureTemplatePool> GRAVE_HOUSE = register("grave_house");
    public static final ResourceKey<StructureTemplatePool> YETI_HOUSE = register("yeti_house");
    public static final ResourceKey<StructureTemplatePool> SUN_TEMPLE = register("sun_temple");

    public static final ResourceKey<StructureTemplatePool> DAVE_VILLA_1 = register("dave_villa/part1");
    public static final ResourceKey<StructureTemplatePool> DAVE_VILLA_2 = register("dave_villa/part2");
    public static final ResourceKey<StructureTemplatePool> DAVE_VILLA_3 = register("dave_villa/part3");
    public static final ResourceKey<StructureTemplatePool> DAVE_VILLA_4 = register("dave_villa/part4");

    // 四主题岛心建筑模板池
    public static final ResourceKey<StructureTemplatePool> EGYPT = register("egypt");
    public static final ResourceKey<StructureTemplatePool> FUTURE = register("future");
    public static final ResourceKey<StructureTemplatePool> HARBOR = register("harbor");
    public static final ResourceKey<StructureTemplatePool> WILD = register("wild");

    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSORS =
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.withDefaultNamespace("empty"));

    private static ResourceKey<StructureTemplatePool> register(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, StringUtil.id(name));
    }

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> emptyProcessors = processors.getOrThrow(EMPTY_PROCESSORS);

        createPool(context, BUCKET_HOUSE, "pvz:zombie_house/bucket_house", emptyProcessors, "bucket_house");
        createPool(context, DOLPHIN_HOUSE, "pvz:zombie_house/dolphin_house", emptyProcessors, "dolphin_house");
        createPool(context, GRAVE_HOUSE, "pvz:zombie_house/graveyard", emptyProcessors, "grave_house");
        createPool(context, YETI_HOUSE, "pvz:zombie_house/yeti_house", emptyProcessors, "yeti_house");
        createPool(context, SUN_TEMPLE, "pvz:shop/sun_temple", emptyProcessors, "sun_temple");

        createPool(context, DAVE_VILLA_1, "pvz:dave_villa/davevilla1", emptyProcessors, "dave_villa");
        createPool(context, DAVE_VILLA_2, "pvz:dave_villa/davevilla2", emptyProcessors, "dave_villa");
        createPool(context, DAVE_VILLA_3, "pvz:dave_villa/davevilla3", emptyProcessors, "dave_villa");
        createPool(context, DAVE_VILLA_4, "pvz:dave_villa/davevilla4", emptyProcessors, "dave_villa");

        // 四主题岛心建筑：structure_name 用于战利品表分派
        createPool(context, EGYPT, "pvz:pvz2/egypt", emptyProcessors, "egypt");
        createPool(context, FUTURE, "pvz:pvz2/future", emptyProcessors, "future");
        createPool(context, HARBOR, "pvz:pvz2/harbor", emptyProcessors, "harbor");
        createPool(context, WILD, "pvz:pvz2/wild", emptyProcessors, "wild");
    }

    private static void createPool(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key,
                                   String nbtPath, Holder<StructureProcessorList> processors, String structureName) {

        Holder<StructureTemplatePool> fallback = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);

        context.register(key, new StructureTemplatePool(fallback,
                List.of(Pair.of(new PvZSinglePoolElement(nbtPath, processors,
                                        StructureTemplatePool.Projection.RIGID, structureName), 1))
        ));
    }
}