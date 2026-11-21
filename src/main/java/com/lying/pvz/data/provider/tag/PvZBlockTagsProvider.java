package com.lying.pvz.data.provider.tag;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.tag.PvZBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class PvZBlockTagsProvider extends BlockTagsProvider {

    public PvZBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, PvZMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // ---- 精华相关：矿石总标签 + 辐射转化标签 ----
        TagAppender<Block> essenceOres = this.tag(PvZBlockTags.ESSENCE_ORES);
        for (IEssenceType essence : PVZAPI.lookup().essences()) {
            essenceOres.add(keyOf(essence.getEssenceOre()));

            essence.getRadiationBlockTag().ifPresent(tag -> {
                TagAppender<Block> appender = this.tag(tag);
                essence.getRadiationBlock()
                        .ifPresent(b -> appender.add(keyOf(b)));
            });
        }

        this.tag(PvZBlockTags.ZOMBIE_BREAKABLE)
                .add(PvZBlockRegister.FLOWER_POT.key())
                .add(PvZBlockRegister.LILY_PAD.key())
                .add(PvZBlockRegister.WATER_POT.key());

        // ---- 通用 / 兼容标签 ----
        this.tag(PvZBlockTags.AMETHYST_ORES).add(PvZBlockRegister.AMETHYST_ORE.key());
        this.tag(BlockTags.CLIMBABLE).add(PvZBlockRegister.STEEL_LADDER.key());

        this.tag(PvZBlockTags.GOLD_TILES)
                .add(PvZBlockRegister.GOLD_TILE1.key())
                .add(PvZBlockRegister.GOLD_TILE2.key())
                .add(PvZBlockRegister.GOLD_TILE3.key());


        this.tag(PvZBlockTags.PLANT_SUIT_BLOCKS)
                .addTag(PvZBlockTags.GOLD_TILES)
                .add(keyOf(Blocks.GRASS_BLOCK))
                .add(PvZBlockRegister.FLOWER_POT.key())
                .add(PvZBlockRegister.LILY_PAD.key())
                .add(PvZBlockRegister.WATER_POT.key());

        // ---- 工具等级 ----
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(PvZBlockRegister.ORIGIN_ORE.key())
                .add(PvZBlockRegister.APPEASE_ORE.key())
                .add(PvZBlockRegister.LIGHT_ORE.key())
                .add(PvZBlockRegister.EXPLOSION_ORE.key())
                .add(PvZBlockRegister.DEFENCE_ORE.key())
                .add(PvZBlockRegister.ICE_ORE.key())
                .add(PvZBlockRegister.ENFORCE_ORE.key())
                .add(PvZBlockRegister.TOXIC_ORE.key())
                .add(PvZBlockRegister.ASSIST_ORE.key())
                .add(PvZBlockRegister.MAGIC_ORE.key())
                .add(PvZBlockRegister.FLAME_ORE.key())
                .add(PvZBlockRegister.SPEAR_ORE.key())
                .add(PvZBlockRegister.ARMA_ORE.key())
                .add(PvZBlockRegister.ELECTRIC_ORE.key())
                .add(PvZBlockRegister.SHADOW_ORE.key())
                .add(PvZBlockRegister.AMETHYST_BLOCK.key())
                .add(PvZBlockRegister.LUNAR_STONE.key())
                .add(PvZBlockRegister.GOLD_TILE1.key())
                .add(PvZBlockRegister.GOLD_TILE2.key())
                .add(PvZBlockRegister.GOLD_TILE3.key())
                .add(PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.key())
                .add(PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY.key())
                .add(PvZBlockRegister.SLOT_MACHINE.key());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(PvZBlockRegister.ESSENCE_ALTAR.key())
                .add(PvZBlockRegister.ORIGIN_BLOCK.key())
                .add(PvZBlockRegister.AMETHYST_ORE.key());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.key())
                .add(PvZBlockRegister.SUN_CONVERTER.key())
                .add(PvZBlockRegister.FRAGMENT_SPLICE.key())
                .add(PvZBlockRegister.CARD_FUSION_TABLE.key());

        // ---- 挖掘工具 ----
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(PvZBlockRegister.ESSENCE_ALTAR.key())
                .add(PvZBlockRegister.ORIGIN_ORE.key())
                .add(PvZBlockRegister.APPEASE_ORE.key())
                .add(PvZBlockRegister.LIGHT_ORE.key())
                .add(PvZBlockRegister.EXPLOSION_ORE.key())
                .add(PvZBlockRegister.DEFENCE_ORE.key())
                .add(PvZBlockRegister.ICE_ORE.key())
                .add(PvZBlockRegister.ENFORCE_ORE.key())
                .add(PvZBlockRegister.TOXIC_ORE.key())
                .add(PvZBlockRegister.ASSIST_ORE.key())
                .add(PvZBlockRegister.MAGIC_ORE.key())
                .add(PvZBlockRegister.FLAME_ORE.key())
                .add(PvZBlockRegister.SPEAR_ORE.key())
                .add(PvZBlockRegister.ARMA_ORE.key())
                .add(PvZBlockRegister.ELECTRIC_ORE.key())
                .add(PvZBlockRegister.SHADOW_ORE.key())
                .add(PvZBlockRegister.AMETHYST_ORE.key())
                .add(PvZBlockRegister.AMETHYST_BLOCK.key())
                .add(PvZBlockRegister.ORIGIN_BLOCK.key())
                .add(PvZBlockRegister.LUNAR_STONE.key())
                .add(PvZBlockRegister.STEEL_LADDER.key())
                .add(PvZBlockRegister.LANTERN.key())
                .add(PvZBlockRegister.GOLD_TILE1.key())
                .add(PvZBlockRegister.GOLD_TILE2.key())
                .add(PvZBlockRegister.GOLD_TILE3.key())
                .add(PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.key())
                .add(PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.key())
                .add(PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY.key())
                .add(PvZBlockRegister.SUN_CONVERTER.key())
                .add(PvZBlockRegister.FRAGMENT_SPLICE.key())
                .add(PvZBlockRegister.SLOT_MACHINE.key())
                .add(PvZBlockRegister.CARD_FUSION_TABLE.key());


        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(PvZBlockRegister.CHOMPER.key())
                .add(PvZBlockRegister.FROZEN_MELON.key())
                .add(PvZBlockRegister.NUT_LOG.key())
                .add(PvZBlockRegister.NUT_WOOD.key())
                .add(PvZBlockRegister.STRIPPED_NUT_WOOD.key());

        this.tag(BlockTags.LEAVES).add(PvZBlockRegister.NUT_LEAVES.key());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(PvZBlockRegister.NUT_LEAVES.key());


        this.tag(BlockTags.LOGS)
                .add(PvZBlockRegister.NUT_LOG.key())
                .add(PvZBlockRegister.NUT_WOOD.key())
                .add(PvZBlockRegister.STRIPPED_NUT_WOOD.key());

        this.tag(BlockTags.SAPLINGS).add(PvZBlockRegister.NUT_SAPLING.key());

        this.tag(BlockTags.CROPS)
                .add(PvZBlockRegister.PEA_PLANT.key())
                .add(PvZBlockRegister.CABBAGE.key())
                .add(PvZBlockRegister.CORN.key())
                .add(PvZBlockRegister.CHILI.key())
                .add(PvZBlockRegister.GARLIC.key());

        this.tag(BlockTags.FLOWER_POTS).add(PvZBlockRegister.FLOWER_POT.key());

        this.tag(BlockTags.DOORS).add(PvZBlockRegister.SCREEN_DOOR.key());
    }

    private static ResourceKey<Block> keyOf(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block)
                .orElseThrow(() -> new IllegalStateException("Unregistered block: " + block));
    }


    @Override
    public String getName() {
        return "Plants vs Zombies block tags";
    }
}