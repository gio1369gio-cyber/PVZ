package com.lying.pvz.common.worldgen.structure.shop;

import com.lying.pvz.common.init.PvZStructureTypes;
import com.lying.pvz.common.tag.PvZBiomeTags;
import com.lying.pvz.common.worldgen.structure.PvZStructureBase;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZTemplatePools;
import com.lying.pvz.utils.BiomeUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaveVillaStructure extends PvZStructureBase {

	public static final MapCodec<DaveVillaStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, DaveVillaStructure::new));

	/** 高度采样半径：覆盖四块拼成的 49×58 足迹（含旋转），留出余量 */
	private static final int SAMPLE_RADIUS = 60;
	/** 足迹高度采样步长（越小越精确，越大越省算力） */
	private static final int SAMPLE_STEP = 3;
	/** 单个 piece 的边长，用于四象限偏移（2×2 布局，每个 32 方块 = 2×2 区块） */
	private static final int PIECE_SPAN = 32;
	/**
	 * groundLevelDelta：本建筑靠 PvZSinglePoolElement 平整地基（削高 + 填坑），
	 * 二者均以 {@code offset.getY()} 为模板地板层、{@code offset.getY() - 1} 为地基顶面，
	 * 要求 delta = 0。
	 */
	private static final int GROUND_DELTA = 0;

	public DaveVillaStructure(StructureSettings settings) {
		super(settings);
	}

	// ── ① 群系：数据包 biomes 由父类保证，这里再限定平原 ──
	@Override
	protected boolean extraBiomeFilter(GenerationContext context, GenerationStub stub, Holder<Biome> biome) {
		return biome.is(PvZBiomeTags.HAS_DAVE_VILLA);
	}

	// ── ② 位置：区块最小角 + 足迹地形中位数（尽量贴地） ──
	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		int x = chunkPos.getMinBlockX();
		int z = chunkPos.getMinBlockZ();

		Rotation rotation = randomRotation(context.random());

		// 高度采样四角：方向随 rotation 变化，覆盖建筑旋转后的实际范围
		int dx = 0, dz = 0;
		switch (rotation) {
			case CLOCKWISE_90:        { dx -= SAMPLE_RADIUS; dz += SAMPLE_RADIUS; break; }
			case CLOCKWISE_180:       { dx -= SAMPLE_RADIUS; dz -= SAMPLE_RADIUS; break; }
			case COUNTERCLOCKWISE_90: { dx += SAMPLE_RADIUS; dz -= SAMPLE_RADIUS; break; }
			default:                  { dx += SAMPLE_RADIUS; dz += SAMPLE_RADIUS; break; }
		}

		// 足迹网格采样的中位高度：反映地形"一般水平"，避免个别高山把整栋抬高十几格
		int h = medianFloorY(context, x, z, dx, dz);

		BlockPos anchor = new BlockPos(x, h, z);

		Holder<StructureTemplatePool> pool1 = getPool(context, PvZTemplatePools.DAVE_VILLA_1);
		Holder<StructureTemplatePool> pool2 = getPool(context, PvZTemplatePools.DAVE_VILLA_2);
		Holder<StructureTemplatePool> pool3 = getPool(context, PvZTemplatePools.DAVE_VILLA_3);
		Holder<StructureTemplatePool> pool4 = getPool(context, PvZTemplatePools.DAVE_VILLA_4);

		return Optional.of(new GenerationStub(anchor, builder -> {
			// 2×2 四象限布局：锚点为其中一个角，其余三个按旋转后的世界方向偏移
			BlockPos pos1 = anchor;
			BlockPos pos2, pos3, pos4;
			switch (rotation) {
				case CLOCKWISE_90: {
					pos2 = pos1.offset(0, 0, PIECE_SPAN);
					pos3 = pos1.offset(-PIECE_SPAN, 0, 0);
					pos4 = pos2.offset(-PIECE_SPAN, 0, 0);
					break;
				}
				case CLOCKWISE_180: {
					pos2 = pos1.offset(-PIECE_SPAN, 0, 0);
					pos3 = pos1.offset(0, 0, -PIECE_SPAN);
					pos4 = pos2.offset(0, 0, -PIECE_SPAN);
					break;
				}
				case COUNTERCLOCKWISE_90: {
					pos2 = pos1.offset(0, 0, -PIECE_SPAN);
					pos3 = pos1.offset(PIECE_SPAN, 0, 0);
					pos4 = pos2.offset(PIECE_SPAN, 0, 0);
					break;
				}
				default: {
					pos2 = pos1.offset(PIECE_SPAN, 0, 0);
					pos3 = pos1.offset(0, 0, PIECE_SPAN);
					pos4 = pos2.offset(0, 0, PIECE_SPAN);
					break;
				}
			}

			// 显式 groundLevelDelta=0，与 fillTemplateFootprint 的基准一致
			addPoolPiece(context, builder, pool1, pos1, rotation, GROUND_DELTA);
			addPoolPiece(context, builder, pool2, pos2, rotation, GROUND_DELTA);
			addPoolPiece(context, builder, pool3, pos3, rotation, GROUND_DELTA);
			addPoolPiece(context, builder, pool4, pos4, rotation, GROUND_DELTA);
		}));
	}

	/**
	 * 在锚点沿 (dx,dz) 张成的足迹内按 {@link #SAMPLE_STEP} 网格采样地表高度，取中位数。
	 * <p>中位数代表地形"一般水平"：既不会被单个突起山峰抬高十几格，也不会像取最低那样
	 * 让大片地形高于地板而侵入房间。超高/超低的个别格由填坑层削高 + 填实处理。
	 */
	private static int medianFloorY(GenerationContext context, int anchorX, int anchorZ, int dx, int dz) {
		final int minX = Math.min(anchorX, anchorX + dx);
		final int maxX = Math.max(anchorX, anchorX + dx);
		final int minZ = Math.min(anchorZ, anchorZ + dz);
		final int maxZ = Math.max(anchorZ, anchorZ + dz);

		final List<Integer> heights = new ArrayList<>();
		for (int sx = minX; sx <= maxX; sx += SAMPLE_STEP) {
			for (int sz = minZ; sz <= maxZ; sz += SAMPLE_STEP) {
				heights.add(solidFloorY(context, sx, sz));
			}
		}
		// 保底：至少把锚点自身算进去，避免空集合
		if (heights.isEmpty()) {
			heights.add(solidFloorY(context, anchorX, anchorZ));
		}
		heights.sort(Integer::compareTo);
		return heights.get(heights.size() / 2);
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.DAVE_VILLA.get();
	}
}