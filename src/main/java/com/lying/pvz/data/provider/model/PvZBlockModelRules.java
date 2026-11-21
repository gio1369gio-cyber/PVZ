package com.lying.pvz.data.provider.model;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.block.plants.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

/**
 * 方块相关的 blockstate / block model / block item model 生成规则。
 */
public final class PvZBlockModelRules {

    private static final String MOD_ID = "pvz";

    private PvZBlockModelRules() {
    }

    /* ============ cube_all：block + item ============ */
    public static void cubeAllWithItem(BlockModelGenerators generators) {
        List<Block> blocks = List.of(
                PvZBlockRegister.AMETHYST_BLOCK.get(),
                PvZBlockRegister.AMETHYST_ORE.get(),
                PvZBlockRegister.APPEASE_ORE.get(),
                PvZBlockRegister.ASSIST_ORE.get(),
                PvZBlockRegister.BUTTER_BLOCK.get(),
                PvZBlockRegister.DEFENCE_ORE.get(),
                PvZBlockRegister.ELECTRIC_ORE.get(),
                PvZBlockRegister.ENFORCE_ORE.get(),
                PvZBlockRegister.EXPLOSION_ORE.get(),
                PvZBlockRegister.FLAME_ORE.get(),
                PvZBlockRegister.GOLD_TILE1.get(),
                PvZBlockRegister.GOLD_TILE2.get(),
                PvZBlockRegister.GOLD_TILE3.get(),
                PvZBlockRegister.ICE_ORE.get(),
                PvZBlockRegister.LIGHT_ORE.get(),
                PvZBlockRegister.LUNAR_STONE.get(),
                PvZBlockRegister.MAGIC_ORE.get(),
                PvZBlockRegister.NUT_LEAVES.get(),
                PvZBlockRegister.ORIGIN_BLOCK.get(),
                PvZBlockRegister.ORIGIN_ORE.get(),
                PvZBlockRegister.PEA_BLOCK.get(),
                PvZBlockRegister.SHADOW_ORE.get(),
                PvZBlockRegister.SPEAR_ORE.get()
        );
        for (Block block : blocks) {
            generators.createTrivialCube(block);
        }
    }

    /* ============ cube_bottom_top：arma_ore、toxic_ore ============ */
    public static void cubeBottomTopWithItem(BlockModelGenerators generators) {
        List<Block> blocks = List.of(PvZBlockRegister.ARMA_ORE.get(), PvZBlockRegister.TOXIC_ORE.get());
        for (Block block : blocks) {
            generators.createTrivialBlock(block, TexturedModel.CUBE_BOTTOM_TOP);
        }
    }

    /* ============ 六面不同：fragment_splice ============ */
    public static void cubeWithItem(BlockModelGenerators generators) {
        Block block = PvZBlockRegister.FRAGMENT_SPLICE.get();
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.DOWN,    TextureMapping.getBlockTexture(block, "_down"))
                .put(TextureSlot.UP,      TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.NORTH,   TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.SOUTH,   TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.EAST,    TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.WEST,    TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.PARTICLE,TextureMapping.getBlockTexture(block, "_side"));
        generators.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(block,
                        BlockModelGenerators.plainVariant(
                                ModelTemplates.CUBE.create(block, mapping, generators.modelOutput))));
    }

    /* ============ cube_column：frozen_melon ============ */
    public static void cubeColumnWithItem(BlockModelGenerators generators) {
        // COLUMN：side=<block>_side、end=<block>_top（对应原 cubeColumn(path, _side, _top)）
        generators.createTrivialBlock(PvZBlockRegister.FROZEN_MELON.get(), TexturedModel.COLUMN);
    }

    /* ============ horizontal：slot_machine ============ */
    public static void horizontalWithItem(BlockModelGenerators generators) {
        Block block = PvZBlockRegister.SLOT_MACHINE.get();
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.DOWN,    TextureMapping.getBlockTexture(block, "_down"))
                .put(TextureSlot.UP,      TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.NORTH,   TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.SOUTH,   TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.EAST,    TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.WEST,    TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.PARTICLE,TextureMapping.getBlockTexture(block, "_side"));
        generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                                BlockModelGenerators.plainVariant(
                                        ModelTemplates.CUBE.create(block, mapping, generators.modelOutput)))
                        .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    /* ============ axis：nut_log ============ */
    public static void woodAxisGroup(BlockModelGenerators generators) {
        Block nutLog = PvZBlockRegister.NUT_LOG.get();
        generators.woodProvider(nutLog).logWithHorizontal(nutLog);
        generators.woodProvider(nutLog).wood(PvZBlockRegister.NUT_WOOD.get());

        Block stripped = PvZBlockRegister.STRIPPED_NUT_LOG.get();
        generators.woodProvider(stripped).logWithHorizontal(stripped)
                .wood(PvZBlockRegister.STRIPPED_NUT_WOOD.get());
    }
    /* ============ crop：cabbage(0..3)、pea_plant(0..7) ============ */
    public static void cropGroup(BlockModelGenerators generators) {
        createCropManually(generators, PvZBlockRegister.CABBAGE.get(),
                (IntegerProperty) CabbageBlock.AGE, 4);
        createCropManually(generators, PvZBlockRegister.PEA_PLANT.get(),
                (IntegerProperty) CropBlock.AGE, 8);
        createCropManually(generators,PvZBlockRegister.CHILI.get(),
                (IntegerProperty) ChiliBlock.AGE,4);
        createCropManually(generators,PvZBlockRegister.GARLIC.get(),
                (IntegerProperty) GarlicBlock.AGE,4);

    }
    private static void createCropManually(BlockModelGenerators generators, Block block,
                                           IntegerProperty age, int stages) {
        Identifier[] models = new Identifier[stages];
        for (int i = 0; i < stages; i++) {
            models[i] = generators.createSuffixedVariant(
                    block, "_stage" + i, ModelTemplates.CROP, TextureMapping::crop);
        }
        generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block).with(
                        PropertyDispatch.initial(age).generate(a -> {
                            int idx = Math.min(a, stages - 1);
                            return BlockModelGenerators.plainVariant(models[idx]);
                        })));
    }
    /* ============ corn：age 0/1→corn_0、2→corn_1 … 7→corn_6 ============ */
    public static void cornGroup(BlockModelGenerators generators) {
        Block block = PvZBlockRegister.CORN.get();
        IntegerProperty age = CornBlock.AGE;

        Identifier[] models = new Identifier[7];
        for (int i = 0; i < models.length; i++) {
            models[i] = generators.createSuffixedVariant(
                    block, "_" + i, ModelTemplates.CROP, TextureMapping::crop);
        }

        generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block).with(
                        PropertyDispatch.initial(age).generate(a -> {
                            int modelIdx = a <= 1 ? 0 : a - 1;
                            return BlockModelGenerators.plainVariant(models[modelIdx]);
                        })));
    }

    /* ============ cross + age：toxic_shroom(0..3) ============ */
    public static void crossAgedGroup(BlockModelGenerators generators) {
        Block block = PvZBlockRegister.TOXIC_SHROOM.get();
        IntegerProperty age = (IntegerProperty) ToxicShroomBlock.AGE;
        generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block).with(
                        PropertyDispatch.initial(age).generate(a -> {
                            Identifier modelId = generators.createSuffixedVariant(
                                    block, "_" + a, ModelTemplates.CROSS, TextureMapping::cross);
                            return BlockModelGenerators.plainVariant(modelId);
                        })));
    }

    /* ============ cross：nut_sapling（item 交给 generatedBlockItemGroup，这里仅注册 block） ============ */
    public static void crossGroup(BlockModelGenerators generators) {
        Block block = PvZBlockRegister.NUT_SAPLING.get();
        // 不要用 createCrossBlock：它内部会 registerSimpleFlatItemModel，会和 generatedBlockItemGroup 冲突
        generators.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(block,
                        BlockModelGenerators.plainVariant(
                                ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generators.modelOutput))));
    }

    /* ============ 手写 block：item parent=block/<name> ============ */
    public static void handwrittenBlockItemGroup(BlockModelGenerators generators) {
        List<Block> blocks = List.of(
                PvZBlockRegister.CARD_FUSION_TABLE.get(),
                PvZBlockRegister.CHOMPER.get(),
                PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY.get(),
                PvZBlockRegister.ESSENCE_ALTAR.get(),
                PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.get(),
                PvZBlockRegister.LANTERN.get(),
                PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.get(),
                PvZBlockRegister.SUN_CONVERTER.get()
        );
        for (Block block : blocks) {
            // 直接用 block 自己的模型位置作为模型 id（= pvz:block/<path>）
            generators.createNonTemplateModelBlock(block);
        }
    }

    /* ============ 手写 item：parent=item/generated ============ */
    public static void generatedBlockItemGroup(BlockModelGenerators generators) {
        flatItem(generators, "lily_pad",     MOD_ID, "item/lily_pad");
        flatItem(generators, "steel_ladder", MOD_ID, "block/steel_ladder");
        flatItem(generators, "flower_pot",   "minecraft", "item/flower_pot");
        flatItem(generators, "nut_sapling",  MOD_ID, "block/nut_sapling");
    }

    /**
     * 给 pvz:<itemPath> 生成 item/generated 模型，并注册 item model。
     */
    private static void flatItem(BlockModelGenerators generators,
                                 String itemPath,
                                 String textureNamespace,
                                 String texturePath) {
        Item item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(MOD_ID, itemPath));
        if (item == Items.AIR) {
            return; // 物品未注册，跳过
        }
        Identifier modelId = ModelLocationUtils.getModelLocation(item);
        Material texture = new Material(Identifier.fromNamespaceAndPath(textureNamespace, texturePath));
        ModelTemplates.FLAT_ITEM.create(modelId, TextureMapping.layer0(texture), generators.modelOutput);
        generators.registerSimpleItemModel(item, modelId);
    }

    /* ============ 手写 block 模型：只生成 blockstate，引用已有模型 ============ */
    public static void customModelBlockGroup(BlockModelGenerators generators) {
        // LILY_PAD：HORIZONTAL_FACING，模型 pvz:block/lily_pad
        generators.createNonTemplateHorizontalBlock(PvZBlockRegister.LILY_PAD.get());

        // STEEL_LADDER：HORIZONTAL_FACING，模型 pvz:block/steel_ladder
        generators.createNonTemplateHorizontalBlock(PvZBlockRegister.STEEL_LADDER.get());

        // FLOWER_POT：无朝向，模型 pvz:block/flower_pot
        generators.createNonTemplateHorizontalBlock(PvZBlockRegister.FLOWER_POT.get());

        // WATER_POT：HORIZONTAL_FACING，模型 pvz:block/water_pot
        generators.createNonTemplateHorizontalBlock(PvZBlockRegister.WATER_POT.get());

        // POT_GRASS：HORIZONTAL_FACING，模型 pvz:block/pot_grass
        generators.createNonTemplateHorizontalBlock(PvZBlockRegister.POT_GRASS.get());

        generators.createNonTemplateHorizontalBlock(PvZBlockRegister.QUESTION_MARK_POT.get());

        generators.createNonTemplateHorizontalBlock(PvZBlockRegister.PLANT_POT.get());

    }


    public static void screenDoor(BlockModelGenerators generators) {
        DoorBlock door = PvZBlockRegister.SCREEN_DOOR.get();
        MultiVariant bottom = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(door, "_bottom"));
        MultiVariant bottomRH = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(door, "_bottom_rh"));
        MultiVariant top = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(door, "_top"));
        MultiVariant topRH = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(door, "_top_rh"));

        generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(door)
                        .with(PropertyDispatch.initial(
                                        BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF,
                                        BlockStateProperties.DOOR_HINGE, BlockStateProperties.OPEN)
                                .generate((facing, half, hinge, open) -> {
                                    boolean lower = half == DoubleBlockHalf.LOWER;

                                    MultiVariant model;
                                    int extraY;
                                    if (hinge == DoorHingeSide.LEFT && !open) {
                                        model = lower ? bottomRH : topRH;
                                        extraY = 0;
                                    } else if (hinge == DoorHingeSide.LEFT) {
                                        model = lower ? bottom : top;
                                        extraY = 270;
                                    } else if (hinge == DoorHingeSide.RIGHT && !open) {
                                        model = lower ? bottom : top;
                                        extraY = 180;
                                    } else {
                                        model = lower ? bottomRH : topRH;
                                        extraY = 270;
                                    }

                                    int facingOffset = switch (facing) {
                                        case EAST -> 90;
                                        case SOUTH -> 180;
                                        case WEST -> 270;
                                        default -> 0;
                                    };

                                    return rotateY(model, (facingOffset + extraY) % 360);
                                })
                        )
        );
    }

    private static MultiVariant rotateY(MultiVariant base, int degrees) {
        return switch (degrees) {
            case 90 -> base.with(BlockModelGenerators.Y_ROT_90);
            case 180 -> base.with(BlockModelGenerators.Y_ROT_180);
            case 270 -> base.with(BlockModelGenerators.Y_ROT_270);
            default -> base;
        };
    }

}