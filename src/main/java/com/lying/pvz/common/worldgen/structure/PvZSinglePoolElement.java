package com.lying.pvz.common.worldgen.structure;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZPoolElementTypes;
import com.lying.pvz.common.misc.PvZLoot;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Function;

/**
 * PvZ 结构专用 SinglePoolElement。
 * <ul>
 *   <li>支持可选 {@code structure_name} 字段，按结构名选择刷怪笼类型 / 战利品表 / 特殊逻辑</li>
 *   <li>data marker：先清空位置（BlockIgnoreProcessor 只跳过结构方块、不清空地形），再放实体 / 箱子 / 刷怪笼</li>
 *   <li>实体走 {@code WorldGenRegion.addFreshEntity}，不传入真实世界</li>
 *   <li>dave_villa：每个 piece 平整自己模板底面（与当前 chunk 求交）——放置前削高、
 *       放置后填实，让建筑按足迹中位高度贴地且不被地形侵入</li>
 * </ul>
 */
public class PvZSinglePoolElement extends SinglePoolElement {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final MapCodec<PvZSinglePoolElement> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(templateCodec(), processorsCodec(),
                    projectionCodec(), overrideLiquidSettingsCodec(),
                    Codec.STRING.optionalFieldOf("structure_name", "").forGetter(e -> e.structureName)
            ).apply(instance, PvZSinglePoolElement::new)
    );

    private final String structureName;

    public PvZSinglePoolElement(Either<Identifier, StructureTemplate> template,
                                Holder<StructureProcessorList> processors,
                                StructureTemplatePool.Projection projection,
                                Optional<LiquidSettings> overrideLiquidSettings,
                                String structureName) {
        super(template, processors, projection, overrideLiquidSettings);
        this.structureName = structureName;
    }

    public PvZSinglePoolElement(String location, Holder<StructureProcessorList> processors,
                                StructureTemplatePool.Projection projection, String structureName) {
        this(Either.left(Identifier.parse(location)), processors, projection, Optional.empty(), structureName);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return PvZPoolElementTypes.PVZ_SINGLE.get();
    }

    // ════════════════════════════════════════════════════════
    // place：模板放置 + 填坑 + data marker 分派
    // ════════════════════════════════════════════════════════
    @Override
    public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level,
                         StructureManager structureManager, ChunkGenerator generator,
                         BlockPos offset, BlockPos pos, Rotation rotation,
                         BoundingBox box, RandomSource random,
                         LiquidSettings liquidSettings, boolean keepJigsaws) {
        StructureTemplate template = this.template
                .map(structureTemplateManager::getOrCreate, Function.identity());
        StructurePlaceSettings settings = this.getSettings(rotation, box, liquidSettings, keepJigsaws);

        // ── DaveVilla 平整地基：放置前先削平本 piece 底面 footprint 上突出的地形 ──
        // 必须在 placeInWorld 之前，否则模板放置后被高处地形包裹/掩埋。
        if ("dave_villa".equals(this.structureName) && level instanceof WorldGenRegion region) {
            clearTemplateFootprint(region, template, settings, offset, box);
        }

        boolean placed = template.placeInWorld(level, offset, pos, settings, random, 18);

        // ── DaveVilla 填坑：每个 piece 只填自己模板底面（向下填至实心） ──
        if ("dave_villa".equals(this.structureName) && level instanceof WorldGenRegion region) {
            fillTemplateFootprint(region, template, settings, offset, box);
        }

        StructurePlaceSettings markerSettings = new StructurePlaceSettings().setRotation(rotation)
                .setMirror(settings.getMirror()).setRotationPivot(settings.getRotationPivot())
                .setBoundingBox(box);

        for (StructureTemplate.StructureBlockInfo info : StructureTemplate.processBlockInfos(
                level, offset, pos, markerSettings,
                this.getDataMarkers(structureTemplateManager, offset, rotation, false), template)) {
            if (box.isInside(info.pos())) {
                this.handleDataMarker(level, info, offset, rotation, random, box);
            }
        }
        return placed;
    }

    // ════════════════════════════════════════════════════════
    // 削高：本 piece 模板底面 ∩ 当前 chunk，铲掉高于地板层的地形
    // （中位数高度放建筑时，个别高出的山包会侵入房间，放置前必须清掉）
    // ════════════════════════════════════════════════════════
    private static void clearTemplateFootprint(WorldGenRegion region, StructureTemplate template,
                                               StructurePlaceSettings settings, BlockPos offset,
                                               BoundingBox chunkBB) {
        final int floorY = offset.getY();                          // 模板地板层；其下为地基
        BoundingBox templateBB = template.getBoundingBox(settings, offset);

        int minX = Math.max(templateBB.minX(), chunkBB.minX());
        int maxX = Math.min(templateBB.maxX(), chunkBB.maxX());
        int minZ = Math.max(templateBB.minZ(), chunkBB.minZ());
        int maxZ = Math.min(templateBB.maxZ(), chunkBB.maxZ());
        if (minX > maxX || minZ > maxZ) {
            return;
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                // WORLD_SURFACE 返回最高方块之上的第一个 Y（含树叶）；只清地板层及以上
                int colTop = region.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                for (int y = floorY; y < colTop; y++) {
                    BlockPos p = new BlockPos(x, y, z);
                    if (!canWriteInRegion(region, p)) break;
                    if (region.getBlockState(p).isAir()) continue;
                    setBlockSafe(region, p, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    // ════════════════════════════════════════════════════════
    // 填坑：本 piece 模板底面 ∩ 当前 chunk，向下填至实心方块
    // ════════════════════════════════════════════════════════
    private static void fillTemplateFootprint(WorldGenRegion region, StructureTemplate template,
                                              StructurePlaceSettings settings, BlockPos offset,
                                              BoundingBox chunkBB) {
        final int maxFillDepth = 64;
        final int baseY = offset.getY() - 1;                      // 要求 groundLevelDelta = 0
        final int minY = Math.max(region.getMinY(), baseY - maxFillDepth);

        // 本 piece 模板的真实底面范围（已应用旋转 / 镜像）
        BoundingBox templateBB = template.getBoundingBox(settings, offset);

        // 与本 chunk 求交：piece 跨 chunk 时每个 chunk 只填落在自己的那段
        int minX = Math.max(templateBB.minX(), chunkBB.minX());
        int maxX = Math.min(templateBB.maxX(), chunkBB.maxX());
        int minZ = Math.max(templateBB.minZ(), chunkBB.minZ());
        int maxZ = Math.min(templateBB.maxZ(), chunkBB.maxZ());

        if (minX > maxX || minZ > maxZ) {
            return;                                               // 本 chunk 不在此 piece 范围内
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                int y = baseY;
                while (y >= minY) {
                    BlockPos tmp = new BlockPos(x, y, z);
                    if (!canWriteInRegion(region, tmp)) break;
                    if (!isFillable(region.getBlockState(tmp))) break;
                    setBlockSafe(region, tmp, Blocks.DIRT.defaultBlockState());
                    y--;
                }
            }
        }
    }

    /** 空气 / 可替换 / 花草 / 雪 / 枯木 / 海草 均视为可填充 */
    private static boolean isFillable(BlockState state) {
        if (state.isAir()) return true;
        if (state.canBeReplaced()) return true;
        if (state.getBlock() instanceof BushBlock) return true;
        if (state.is(Blocks.SNOW) || state.is(Blocks.DEAD_BUSH)) return true;
        if (state.is(Blocks.SEAGRASS) || state.is(Blocks.TALL_SEAGRASS)) return true;
        return false;
    }

    // ════════════════════════════════════════════════════════
    // handleDataMarker：先清空结构方块位置，再放置最终产物
    // ════════════════════════════════════════════════════════
    @Override
    public void handleDataMarker(LevelAccessor level, StructureTemplate.StructureBlockInfo blockInfo,
                                 BlockPos pos, Rotation rotation, RandomSource random, BoundingBox box) {
        if (!(level instanceof WorldGenRegion region)) {
            return;
        }

        String function = blockInfo.nbt() != null
                ? blockInfo.nbt().getStringOr("metadata", "")
                : "";

        BlockPos actualPos = blockInfo.pos();

        setBlockSafe(region, actualPos, Blocks.AIR.defaultBlockState());

        switch (function) {
            // ── 实体：必须生成 ──
            case "dave"   -> spawnEntity(region, PvZEntityRegister.CRAZY_DAVE.get(), actualPos, random);
            case "panney" -> spawnEntity(region, PvZEntityRegister.PANNEY.get(), actualPos, random);

            // ── 实体：概率生成（留空是设计意图） ──
            case "plant_pos1", "plant_pos3", "plant_pos6" -> {
                if (random.nextInt(3) == 0) {
                    spawnEntity(region, PvZEntityRegister.PEA_SHOOTER.get(), actualPos, random);
                }
            }
            case "plant_pos2" -> {
                if (random.nextInt(3) == 0) {
                    spawnEntity(region, PvZEntityRegister.SUN_FLOWER.get(), actualPos, random);
                }
            }
            case "plant_pos4" -> {
                if (random.nextInt(2) == 0) {
                    spawnEntity(region, PvZEntityRegister.WALL_NUT.get(), actualPos, random);
                }
            }
            case "plant_pos5" -> spawnEntity(region, PvZEntityRegister.SUN_FLOWER.get(), actualPos, random);

            // ── 刷怪笼：必须放方块，实体可选 ──
            case "spawner" -> {
                setBlockSafe(region, actualPos, Blocks.SPAWNER.defaultBlockState());
                EntityType<?> spawnerType = getSpawnerEntity(this.structureName, random);
                if (spawnerType != null) {
                    BlockEntity be = region.getBlockEntity(actualPos);
                    if (be instanceof SpawnerBlockEntity spawner) {
                        spawner.getSpawner().setEntityId(spawnerType, null, random, actualPos);
                    }
                } else {
                    LOGGER.warn("[PVZStructure] no spawner entity for structure '{}', placed empty spawner at {}",
                            this.structureName, actualPos);
                }
            }

            // ── 箱子：必须放方块 ──
            case "bonus_chest1", "bonus_chest2", "chest1", "chest2", "chest3" ->
                    createLootChest(region, actualPos, random, getChestLootTable(this.structureName));

            // ── 刷怪笼 + 箱子组合 ──
            case "chest" -> {
                if (!canWriteInRegion(region, actualPos.above())) {
                    LOGGER.warn("[PVZStructure] chest marker above {} out of write region, skipped", actualPos);
                    return;
                }
                setBlockSafe(region, actualPos.above(), Blocks.AIR.defaultBlockState());
                createLootChest(region, actualPos.above(), random, PvZLoot.SUN_TEMPLE_CHEST);
                setBlockSafe(region, actualPos, Blocks.SPAWNER.defaultBlockState());
                BlockEntity be = region.getBlockEntity(actualPos);
                if (be instanceof SpawnerBlockEntity spawner) {
                    spawner.getSpawner().setEntityId(PvZEntityRegister.RA_ZOMBIE.get(), null, random, actualPos);
                }
            }

            // ── 实体：SunTemple 必生成 / 其他概率生成 ──
            case "spawn" -> {
                if (isSunTemple(this.structureName)) {
                    spawnEntity(region, PvZEntityRegister.SUN_DAVE.get(), actualPos, random);
                } else if (random.nextInt(3) == 0) {
                    spawnEntity(region, PvZEntityRegister.YETI_ZOMBIE.get(), actualPos, random);
                }
            }

            // ── 墓碑：概率生成 ──
            case String s when s.startsWith("tomb") -> {
                if (random.nextInt(3) == 0) {
                    spawnEntity(region, PvZEntityRegister.TOMB_STONE.get(), actualPos, random);
                }
            }

            // ── 未知 marker：log，不静默吞掉 ──
            default -> LOGGER.warn("[PVZStructure] unknown data marker '{}' at {} in structure '{}', left as air",
                    function, actualPos, this.structureName);
        }
    }

    // ════════════════════════════════════════════════════════
    // 实体：按高度清空空间，加到 WorldGenRegion，不传真实世界
    // ════════════════════════════════════════════════════════
    private static void spawnEntity(WorldGenRegion region, EntityType<?> type,
                                    BlockPos pos, RandomSource random) {
        Entity entity = type.create(region.getLevel(), EntitySpawnReason.STRUCTURE);
        if (entity == null) {
            LOGGER.warn("[PVZStructure] failed to create entity {} at {}", type, pos);
            return;
        }

        int height = Math.max(1, (int) Math.ceil(entity.getBbHeight()));
        for (int dy = 0; dy < height; dy++) {
            BlockPos p = pos.above(dy);
            if (!region.hasChunk(SectionPos.blockToSectionCoord(p.getX()), SectionPos.blockToSectionCoord(p.getZ()))) {
                LOGGER.warn("[PVZStructure] entity {} at {} out of write region, skipped", type, pos);
                return;
            }
            setBlockSafe(region, p, Blocks.AIR.defaultBlockState());
        }

        float yaw = random.nextFloat() * 360.0F;
        entity.snapTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);

        if (entity instanceof Mob mob) {
            DifficultyInstance difficulty = new DifficultyInstance(
                    region.getLevel().getDifficulty(),
                    region.getLevel().getOverworldClockTime(),
                    0L,
                    region.getLevel().getMoonBrightness(pos)
            );
            mob.finalizeSpawn(region, difficulty, EntitySpawnReason.STRUCTURE, (SpawnGroupData) null);
        }

        region.addFreshEntity(entity);
    }

    // ════════════════════════════════════════════════════════
    // 箱子
    // ════════════════════════════════════════════════════════
    private static void createLootChest(WorldGenRegion region, BlockPos pos,
                                        RandomSource random, ResourceKey<LootTable> lootTable) {
        if (!canWriteInRegion(region, pos)) {
            LOGGER.warn("[PVZStructure] chest marker at {} out of write region, skipped", pos);
            return;
        }
        setBlockSafe(region, pos, Blocks.CHEST.defaultBlockState());
        BlockEntity be = region.getBlockEntity(pos);
        if (be instanceof ChestBlockEntity chest) {
            chest.setLootTable(lootTable, random.nextLong());
        }
    }

    // ════════════════════════════════════════════════════════
    // 按结构名选刷怪笼实体 / 战利品表
    // ════════════════════════════════════════════════════════
    private static ResourceKey<LootTable> getChestLootTable(String structureName) {
        return switch (structureName) {
            case "bucket_house"  -> PvZLoot.BUCKET_HOUSE_CHEST;
            case "dolphin_house" -> PvZLoot.DOLPHIN_HOUSE_CHEST;
            case "grave_house"   -> PvZLoot.GRAVE_YARD_CHEST;
            case "yeti_house"    -> PvZLoot.YETI_HOUSE_CHEST;
            case "dave_villa"    -> PvZLoot.DAVE_VILLA_CHEST;
            case "sun_temple"    -> PvZLoot.SUN_TEMPLE_CHEST;
            default              -> PvZLoot.BUCKET_HOUSE_CHEST;
        };
    }

    private static EntityType<?> getSpawnerEntity(String structureName, RandomSource random) {
        return switch (structureName) {
            case "bucket_house" -> {
                int n = random.nextInt(3);
                yield n == 0 ? PvZEntityRegister.CONEHEAD_ZOMBIE.get()
                        : n == 1 ? PvZEntityRegister.POLE_ZOMBIE.get()
                        : PvZEntityRegister.BUCKETHEAD_ZOMBIE.get();
            }
            case "dolphin_house" -> {
                int n = random.nextInt(3);
                yield n == 0 ? PvZEntityRegister.SNORKEL_ZOMBIE.get()
                        : n == 1 ? PvZEntityRegister.LAVA_ZOMBIE.get()
                        : PvZEntityRegister.DOLPHIN_RIDER_ZOMBIE.get();
            }
            case "grave_house" -> {
                int n = random.nextInt(2);
                yield n == 0 ? PvZEntityRegister.GIGA_FOOTBALL_ZOMBIE.get()
                        : PvZEntityRegister.FOOTBALL_ZOMBIE.get();
            }
            default -> null;
        };
    }

    private static boolean isSunTemple(String structureName) {
        return "sun_temple".equals(structureName);
    }

    // ════════════════════════════════════════════════════════
    // 工具
    // ════════════════════════════════════════════════════════

    private static boolean canWriteInRegion(WorldGenRegion region, BlockPos pos) {
        return region.hasChunk(
                SectionPos.blockToSectionCoord(pos.getX()),
                SectionPos.blockToSectionCoord(pos.getZ()));
    }

    private static void setBlockSafe(WorldGenRegion region, BlockPos pos, BlockState state) {
        if (!canWriteInRegion(region, pos)) {
            return;
        }
        region.setBlock(pos, state, 2, 512);
    }


}