package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.impl.EssenceTypes;
import com.lying.pvz.common.level.block.cubes.ButterBlock;
import com.lying.pvz.common.level.block.cubes.OriginBlock;
import com.lying.pvz.common.level.block.ore.EssenceOreBlock;
import com.lying.pvz.common.level.block.ore.PVZOreBlock;
import com.lying.pvz.common.level.block.others.PlantPotBlock;
import com.lying.pvz.common.level.block.others.QuestionMarkPotBlock;
import com.lying.pvz.common.level.block.others.ScreenDoorBlock;
import com.lying.pvz.common.level.block.others.SteelLadderBlock;
import com.lying.pvz.common.level.block.plants.*;
import com.lying.pvz.common.level.block.special.*;
import com.lying.pvz.common.level.item.blockitem.LilyPadItem;
import com.lying.pvz.common.level.item.blockitem.SlotMachineItem;
import com.lying.pvz.common.worldgen.feature.grower.PvZTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZBlockRegister {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PvZMod.MOD_ID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(PvZMod.MOD_ID);

    //plant
    public static final DeferredBlock<PvZLilyPadBlock> LILY_PAD = BLOCKS.registerBlock("lily_pad", PvZLilyPadBlock::new,
            prop -> BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD));

    public static final DeferredBlock<Block> POT_GRASS = BLOCKS.registerBlock("pot_grass", PotGrassBlock::new,
           prop ->  BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                   .sound(SoundType.STONE).strength(6.0f, 6.0f));

    public static final DeferredBlock<Block> WATER_POT = BLOCKS.registerBlock("water_pot",WaterPotBlock::new,
           prop -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT)
                   .strength(1.0F).noOcclusion().mapColor(MapColor.STONE).sound(SoundType.STONE));





    public static final DeferredBlock<Block> NUT_LEAVES = BLOCKS.register("nut_leaves",id ->
            new LeavesBlock(AmbientLeavesBlockSoundPlayer.noAmbientSound(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, id))){
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final DeferredBlock<Block> NUT_WOOD = BLOCKS.register("nut_wood", id ->
            new RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, id))){
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final DeferredBlock<Block> STRIPPED_NUT_WOOD = BLOCKS.register("stripped_nut_wood", id ->
            new  RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, id))){
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final DeferredBlock<Block> STRIPPED_NUT_LOG = BLOCKS.register("stripped_nut_log", id ->
            new  RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, id))){
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });


    public static final DeferredBlock<Block> NUT_LOG = BLOCKS.register("nut_log", id ->
            new RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, id))){
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final DeferredBlock<Block> NUT_SAPLING = BLOCKS.register("nut_sapling", id ->
            new SaplingBlock(PvZTreeGrowers.NUT_TREE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)
                    .setId(ResourceKey.create(Registries.BLOCK, id))));

    public static final DeferredBlock<Block> CHOMPER = BLOCKS.registerBlock("chomper", ChomperBlock::new,
            pop -> BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN).noCollision().noOcclusion()
                    .requiresCorrectToolForDrops());

    public static final DeferredBlock<Block> PEA_BLOCK = BLOCKS.registerBlock("pea_block",Block::new,
           prop -> Block.Properties.of().mapColor(MapColor.PLANT)
                   .strength(5.0f, 5.0f).sound(SoundType.BAMBOO_SAPLING));
    //block
    public static final DeferredBlock<Block> ORIGIN_BLOCK = BLOCKS.registerBlock("origin_block", OriginBlock::new,
            prop-> prop.mapColor(MapColor.COLOR_GREEN).strength(15, 50)
                    .requiresCorrectToolForDrops().lightLevel(i -> 15).randomTicks()
                    .sound(SoundType.ANCIENT_DEBRIS).noOcclusion());

    public static final DeferredBlock<Block> BUTTER_BLOCK = BLOCKS.registerBlock("butter_block", ButterBlock::new,
            prop-> BlockBehaviour.Properties.ofFullCopy(Blocks.HONEY_BLOCK));

    public static final DeferredBlock<Block> STEEL_LADDER = BLOCKS.registerBlock("steel_ladder", SteelLadderBlock::new,
            prop -> BlockBehaviour.Properties.ofFullCopy(Blocks.LADDER).strength(6F).requiresCorrectToolForDrops());

    public static final DeferredBlock<Block> AMETHYST_BLOCK = BLOCKS.register("amethyst_block", id ->
            new Block(Block.Properties.ofFullCopy(Blocks.EMERALD_BLOCK).strength(9, 9)
                    .setId(ResourceKey.create(Registries.BLOCK, id))));

    public static final DeferredBlock<Block> LUNAR_STONE = BLOCKS.register("lunar_stone", id ->
            new Block(Block.Properties.ofFullCopy(Blocks.EMERALD_BLOCK).strength(9, 9)
                    .lightLevel(i -> 6).setId(ResourceKey.create(Registries.BLOCK, id))));

    public static final DeferredBlock<Block> FROZEN_MELON = BLOCKS.register("frozen_melon", id ->
            new Block(Block.Properties.ofFullCopy(Blocks.MELON).setId(ResourceKey.create(Registries.BLOCK, id))));

    // ores.
    public static final DeferredBlock<EssenceOreBlock> ORIGIN_ORE = BLOCKS.register("origin_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.ORIGIN, 12));

    public static final DeferredBlock<EssenceOreBlock> APPEASE_ORE = BLOCKS.register("appease_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.APPEASE, 0));

    public static final DeferredBlock<EssenceOreBlock> LIGHT_ORE = BLOCKS.register("light_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.LIGHT, 12));

    public static final DeferredBlock<EssenceOreBlock> EXPLOSION_ORE = BLOCKS.register("explosion_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.EXPLOSION, 0));

    public static final DeferredBlock<EssenceOreBlock> DEFENCE_ORE = BLOCKS.register("defence_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.DEFENCE, 0));

    public static final DeferredBlock<EssenceOreBlock> ICE_ORE = BLOCKS.register("ice_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.ICE, 0));

    public static final DeferredBlock<EssenceOreBlock> ENFORCE_ORE = BLOCKS.register("enforce_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.ENFORCE, 0));

    public static final DeferredBlock<EssenceOreBlock> TOXIC_ORE = BLOCKS.register("toxic_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.TOXIC, 0));

    public static final DeferredBlock<EssenceOreBlock> ASSIST_ORE = BLOCKS.register("assist_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.ASSIST, 0));

    public static final DeferredBlock<EssenceOreBlock> MAGIC_ORE = BLOCKS.register("magic_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.MAGIC, 0));

    public static final DeferredBlock<EssenceOreBlock> FLAME_ORE = BLOCKS.register("flame_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.FLAME, 12));

    public static final DeferredBlock<EssenceOreBlock> SPEAR_ORE = BLOCKS.register("spear_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.SPEAR, 0));

    public static final DeferredBlock<EssenceOreBlock> ARMA_ORE = BLOCKS.register("arma_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.ARMA, 0));

    public static final DeferredBlock<EssenceOreBlock> ELECTRIC_ORE = BLOCKS.register("electric_ore",id ->
            new EssenceOreBlock(id,EssenceTypes.ELECTRIC, 12));

    public static final DeferredBlock<EssenceOreBlock> SHADOW_ORE = BLOCKS.register("shadow_ore", id ->
            new EssenceOreBlock(id,EssenceTypes.SHADOW, 0));

    public static final DeferredBlock<PVZOreBlock> AMETHYST_ORE = BLOCKS.register("amethyst_ore",id ->
            new PVZOreBlock(Block.Properties.ofFullCopy(Blocks.DIAMOND_ORE)
            .requiresCorrectToolForDrops().strength(4F, 6F)
                    .setId(ResourceKey.create(Registries.BLOCK, id))));

    //crops
    public static final DeferredBlock<Block> PEA_PLANT = BLOCKS.register("pea_plant", id ->
            new PeaBlock(cropProperties(id)));

    public static final DeferredBlock<Block> TOXIC_SHROOM = BLOCKS.register("toxic_shroom", id ->
            new ToxicShroomBlock(Block.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)
                    .setId(ResourceKey.create(Registries.BLOCK, id))));

    public static final DeferredBlock<Block> CABBAGE = BLOCKS.register("cabbage", id ->
            new CabbageBlock(cropProperties(id)));

    public static final DeferredBlock<Block> CORN = BLOCKS.register("corn",id ->
            new CornBlock(cropProperties(id)));

    public static final DeferredBlock<Block> CHILI = BLOCKS.register("chili",id ->
            new ChiliBlock(cropProperties(id)));

    public static final DeferredBlock<Block> GARLIC = BLOCKS.registerBlock("garlic", GarlicBlock::new,
            prop -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision()
                    .randomTicks().instabreak().sound(SoundType.CROP));

    //special
    public static final DeferredBlock<Block> LANTERN = BLOCKS.registerBlock("lantern", PvZLanternBlock::new,
        properties -> BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN).strength(5, 9)
                .lightLevel(i -> 14).noOcclusion());

    public static final DeferredBlock<Block> QUESTION_MARK_POT = BLOCKS.registerBlock("question_mark_pot", QuestionMarkPotBlock::new,
            prop -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE)
                    .strength(6.0f, 6.0f));

    public static final DeferredBlock<Block> PLANT_POT = BLOCKS.registerBlock("plant_pot",PlantPotBlock::new,
                    prop -> BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .sound(SoundType.STONE).strength(6.0f, 6.0f));

    public static final DeferredBlock<DoorBlock> SCREEN_DOOR = BLOCKS.register("screen_door",
            id -> new ScreenDoorBlock(BlockSetType.IRON,Block.Properties.of()
                    .mapColor(MapColor.METAL).setId(ResourceKey.create(Registries.BLOCK, id))
                    .strength(25.0f, 25.0f).sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> FLOWER_POT = BLOCKS.registerBlock("flower_pot", PvZFlowerPotBlock::new,
            properties -> BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).strength(1F).noOcclusion());

    public static final DeferredBlock<GoldTileBlock> GOLD_TILE1 = BLOCKS.register("gold_tile1",
            id -> new GoldTileBlock(id,1));
    public static final DeferredBlock<GoldTileBlock> GOLD_TILE2 = BLOCKS.register("gold_tile2",
            id -> new GoldTileBlock(id,2));
    public static final DeferredBlock<GoldTileBlock> GOLD_TILE3 = BLOCKS.register("gold_tile3",
            id -> new GoldTileBlock(id,3));

    public static final DeferredBlock<SunFlowerTrophyBlock> SILVER_SUNFLOWER_TROPHY = BLOCKS.register("silver_sunflower_trophy",
            id -> new SunFlowerTrophyBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id)), 1));

    public static final DeferredBlock<SunFlowerTrophyBlock> GOLD_SUNFLOWER_TROPHY = BLOCKS.register("gold_sunflower_trophy",
            id ->
            new SunFlowerTrophyBlock(Block.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id)), 2));

    public static final DeferredBlock<SunFlowerTrophyBlock> DIAMOND_SUNFLOWER_TROPHY = BLOCKS.register("diamond_sunflower_trophy",
            id ->
            new SunFlowerTrophyBlock(Block.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, id)), 3));

    //gui & te block
    public static final DeferredBlock<SlotMachineBlock> SLOT_MACHINE = BLOCKS.registerBlock("slot_machine", SlotMachineBlock::new,
            pop -> BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK));

    public static final DeferredBlock<SunConverterBlock> SUN_CONVERTER = BLOCKS.registerBlock("sun_converter", SunConverterBlock::new,
            pop -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));

    public static final DeferredBlock<FragmentSpliceBlock> FRAGMENT_SPLICE = BLOCKS.registerBlock("fragment_splice", FragmentSpliceBlock::new,
            pop -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));

     public static final DeferredBlock<EssenceAltarBlock> ESSENCE_ALTAR = BLOCKS.registerBlock("essence_altar", EssenceAltarBlock::new,
           pop -> BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).lightLevel(state -> 15));

    public static final DeferredBlock<CardFusionBlock> CARD_FUSION_TABLE = BLOCKS.registerBlock("card_fusion_table", CardFusionBlock::new,
          pop ->BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));


    /**
     * register block items.
     */
    static {
        // 普通方块物品
        BLOCK_ITEMS.registerSimpleBlockItem(ORIGIN_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(APPEASE_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(LIGHT_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(EXPLOSION_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(DEFENCE_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(ICE_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(ENFORCE_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(TOXIC_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(ASSIST_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(MAGIC_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(FLAME_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(SPEAR_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(ARMA_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(ELECTRIC_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(SHADOW_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(AMETHYST_ORE);
        BLOCK_ITEMS.registerSimpleBlockItem(AMETHYST_BLOCK);
        BLOCK_ITEMS.registerSimpleBlockItem(ORIGIN_BLOCK);
        BLOCK_ITEMS.registerSimpleBlockItem(BUTTER_BLOCK);
        BLOCK_ITEMS.registerSimpleBlockItem(FROZEN_MELON);
        BLOCK_ITEMS.registerSimpleBlockItem(NUT_LEAVES);
        BLOCK_ITEMS.registerSimpleBlockItem(NUT_LOG);
        BLOCK_ITEMS.registerSimpleBlockItem(NUT_WOOD);
        BLOCK_ITEMS.registerSimpleBlockItem(STRIPPED_NUT_WOOD);
        BLOCK_ITEMS.registerSimpleBlockItem(STRIPPED_NUT_LOG);
        BLOCK_ITEMS.registerSimpleBlockItem(NUT_SAPLING);
        BLOCK_ITEMS.registerSimpleBlockItem(CHOMPER);
        BLOCK_ITEMS.registerSimpleBlockItem(LANTERN);
        BLOCK_ITEMS.registerSimpleBlockItem(FLOWER_POT);
        BLOCK_ITEMS.registerSimpleBlockItem(GOLD_TILE1);
        BLOCK_ITEMS.registerSimpleBlockItem(GOLD_TILE2);
        BLOCK_ITEMS.registerSimpleBlockItem(GOLD_TILE3);
        BLOCK_ITEMS.registerSimpleBlockItem(LUNAR_STONE);
        BLOCK_ITEMS.registerSimpleBlockItem(SILVER_SUNFLOWER_TROPHY);
        BLOCK_ITEMS.registerSimpleBlockItem(GOLD_SUNFLOWER_TROPHY);
        BLOCK_ITEMS.registerSimpleBlockItem(DIAMOND_SUNFLOWER_TROPHY);
        BLOCK_ITEMS.registerSimpleBlockItem(STEEL_LADDER);
        BLOCK_ITEMS.registerSimpleBlockItem(SUN_CONVERTER);
        BLOCK_ITEMS.registerSimpleBlockItem(FRAGMENT_SPLICE);
        BLOCK_ITEMS.registerSimpleBlockItem(ESSENCE_ALTAR);
        BLOCK_ITEMS.registerSimpleBlockItem(CARD_FUSION_TABLE);
        BLOCK_ITEMS.registerSimpleBlockItem(WATER_POT);
        BLOCK_ITEMS.registerSimpleBlockItem(POT_GRASS);
        BLOCK_ITEMS.registerSimpleBlockItem(PEA_BLOCK);
        BLOCK_ITEMS.registerSimpleBlockItem(QUESTION_MARK_POT);
        BLOCK_ITEMS.registerSimpleBlockItem(PLANT_POT);

        // 自定义 BlockItem
        BLOCK_ITEMS.registerItem("lily_pad", LilyPadItem::new);
        BLOCK_ITEMS.registerItem("slot_machine", SlotMachineItem::new);
    }

    private static BlockBehaviour.Properties cropProperties(Identifier id) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id)).mapColor(MapColor.PLANT).noCollision()
                .randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.POPPED);
    }

}
