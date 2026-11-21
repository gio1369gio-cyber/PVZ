package com.lying.pvz.common.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * PvZ 结构基类。三层职责：
 * <ul>
 *   <li>① 群系：数据包 {@code biomes} 必过 + 子类附加过滤</li>
 *   <li>② 位置：锚点策略 + 地形工具</li>
 *   <li>③ 拼图：真实包围盒构建，父类不接收尺寸参数</li>
 * </ul>
 * 子类只需实现 {@link #findGenerationPoint}、{@link #type()}，
 * 并按需覆盖 {@link #extraBiomeFilter} / {@link #canGenerateAt}。
 */
public abstract class PvZStructureBase extends Structure {

	protected PvZStructureBase(StructureSettings settings) {
		super(settings);
	}

	// ════════════════════════════════════════════════════════
	// ① 群系层
	// ════════════════════════════════════════════════════════

	/**
	 * 先过数据包 {@code StructureSettings.biomes}（{@code context.validBiome()}），
	 * 再过 {@link #extraBiomeFilter}（子类附加）。二者是 AND，子类不能绕过数据包。
	 * <p>群系查询只做一次，结果作为参数传给 {@link #extraBiomeFilter} 复用。
	 */
	@Override
	public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
		Optional<GenerationStub> stub = this.findGenerationPoint(context);
		if (stub.isEmpty()) {
			return stub;
		}
		GenerationStub s = stub.get();
		Holder<Biome> biome = noiseBiomeAt(context, s.position());
		if (!context.validBiome().test(biome)) {
			return Optional.empty();
		}
		if (!this.extraBiomeFilter(context, s, biome)) {
			return Optional.empty();
		}
		return stub;
	}

	/**
	 * 子类附加群系过滤；默认放行。
	 * 数据包 {@code biomes} 已由父类保证，这里只做"在数据包基础上再收窄"（例如仅沙漠）。
	 *
	 * @param biome 锚点处的噪声群系，已由父类查询，可直接复用（避免重复计算）
	 */
	protected boolean extraBiomeFilter(GenerationContext context, GenerationStub stub, Holder<Biome> biome) {
		return true;
	}

	/** 便捷：查询某坐标的生物群系（子类需要额外采样时使用） */
	protected static Holder<Biome> noiseBiomeAt(GenerationContext context, BlockPos pos) {
		return context.biomeResolver().getNoiseBiome(
				QuartPos.fromBlock(pos.getX()),
				QuartPos.fromBlock(pos.getY()),
				QuartPos.fromBlock(pos.getZ()));
	}

	// ════════════════════════════════════════════════════════
	// ② 位置层
	// ════════════════════════════════════════════════════════

	/** 锚点策略 */
	protected enum Anchor {
		/** 区块中心 (X,Z) + 地表高度 */
		CHUNK_CENTER_SURFACE,
		/** 区块最小角 (X,Z) + 地表高度 */
		CHUNK_MIN_SURFACE,
		/** 区块中心 (X,Z) + 固定 Y（浮空 / 自定义维度） */
		CHUNK_CENTER_FIXED_Y,
		/** 区块最小角 (X,Z) + 固定 Y */
		CHUNK_MIN_FIXED_Y,
		/** 子类完全自算（传给 {@link #resolveAnchor} 会抛异常） */
		CUSTOM
	}

	/**
	 * 解析锚点坐标。
	 *
	 * @param yOrOffset SURFACE 类为相对地表的偏移（通常 0）；
	 *                  FIXED_Y 类为绝对 Y
	 */
	protected static BlockPos resolveAnchor(GenerationContext context, Anchor anchor, int yOrOffset) {
		ChunkPos cp = context.chunkPos();

		int x = switch (anchor) {
			case CHUNK_CENTER_SURFACE, CHUNK_CENTER_FIXED_Y -> cp.getMiddleBlockX();
			case CHUNK_MIN_SURFACE, CHUNK_MIN_FIXED_Y -> cp.getMinBlockX();
			case CUSTOM -> throw new UnsupportedOperationException(
					"Anchor.CUSTOM requires the subclass to compute the position; do not pass it to resolveAnchor");
		};
		int z = switch (anchor) {
			case CHUNK_CENTER_SURFACE, CHUNK_CENTER_FIXED_Y -> cp.getMiddleBlockZ();
			case CHUNK_MIN_SURFACE, CHUNK_MIN_FIXED_Y -> cp.getMinBlockZ();
			case CUSTOM -> throw new UnsupportedOperationException(
					"Anchor.CUSTOM requires the subclass to compute the position; do not pass it to resolveAnchor");
		};
		int y = switch (anchor) {
			case CHUNK_CENTER_SURFACE, CHUNK_MIN_SURFACE -> solidFloorY(context, x, z) + yOrOffset;
			case CHUNK_CENTER_FIXED_Y, CHUNK_MIN_FIXED_Y -> yOrOffset;
			case CUSTOM -> throw new UnsupportedOperationException(
					"Anchor.CUSTOM requires the subclass to compute the position; do not pass it to resolveAnchor");
		};
		return new BlockPos(x, y, z);
	}

	/**
	 * 地表取样：{@code MOTION_BLOCKING_NO_LEAVES} 高度图。
	 * <ul>
	 *   <li>排除树叶（{@code #leaves}）</li>
	 *   <li>流体视为占用，锚点落在流体顶面之上（陆地群系下无流体）</li>
	 *   <li>排除花草、雪层、藤蔓等不阻挡运动方块</li>
	 * </ul>
	 * 返回"地面实心方块上方第一个空位"的 Y。
	 */
	protected static int solidFloorY(GenerationContext context, int x, int z) {
		return context.chunkGenerator().getFirstOccupiedHeight(
				x, z, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				context.heightAccessor(), context.randomState());
	}

	/**
	 * 采样 (minX,minZ) ~ (minX+sizeX, minZ+sizeZ) 四角地表高度，
	 * 判断起伏是否 ≤ maxDelta。用于多区块建筑的地形平整检查。
	 */
	protected static boolean isTerrainFlatEnough(GenerationContext context,
												 int minX, int minZ, int sizeX, int sizeZ, int maxDelta) {
		int h0 = solidFloorY(context, minX, minZ);
		int h1 = solidFloorY(context, minX + sizeX, minZ);
		int h2 = solidFloorY(context, minX, minZ + sizeZ);
		int h3 = solidFloorY(context, minX + sizeX, minZ + sizeZ);
		int min = Math.min(Math.min(h0, h1), Math.min(h2, h3));
		int max = Math.max(Math.max(h0, h1), Math.max(h2, h3));
		return max - min <= maxDelta;
	}

	// ════════════════════════════════════════════════════════
	// ③ 条件层
	// ════════════════════════════════════════════════════════

	/**
	 * 子类可覆盖：锚点确定后、成桩前的过滤。
	 * 在 {@code findGenerationPoint} 内主动调用；返回 false 则不生成。
	 */
	protected boolean canGenerateAt(GenerationContext context, BlockPos anchor) {
		return true;
	}

	/**
	 * 检查 pool 内模板的最大 Y 跨度是否超出世界高度范围。
	 * <p>{@code StructureTemplatePool.getMaxSize} 返回池中元素的 YSpan（从模板原点向上的生长量）。
	 * 因此顶部按 {@code anchor + maxY} 严格检查；底部以锚点自身为下界
	 * （模板原点通常在底部，不向下延伸）。比"向下也减 maxY"更精确，避免误拒可放置位置。
	 */
	protected static boolean fitsVertically(GenerationContext context,
											Holder<StructureTemplatePool> pool, BlockPos anchor) {
		int maxY = pool.value().getMaxSize(context.structureTemplateManager());
		if (maxY <= 0) {
			return true; // 空池 / 仅空元素，不拦
		}
		return anchor.getY() + maxY <= context.heightAccessor().getMaxY()
				&& anchor.getY() >= context.heightAccessor().getMinY();
	}

	// ════════════════════════════════════════════════════════
	// ④ 拼图层
	// ════════════════════════════════════════════════════════

	private static @Nullable PoolElementStructurePiece buildPiece(
			GenerationContext context, Holder<StructureTemplatePool> pool,
			BlockPos pos, Rotation rotation, int groundLevelDelta) {
		StructureTemplateManager tm = context.structureTemplateManager();
		StructurePoolElement element = pool.value().getRandomTemplate(context.random());
		if (element == EmptyPoolElement.INSTANCE) {
			return null;
		}
		BoundingBox bb = element.getBoundingBox(tm, pos, rotation);
		int delta = groundLevelDelta == Integer.MIN_VALUE
				? element.getGroundLevelDelta()
				: groundLevelDelta;
		return new PoolElementStructurePiece(
				tm, element, pos, delta, rotation, bb,
				LiquidSettings.APPLY_WATERLOGGING);
	}

	/** 构建并添加；空池静默跳过。groundLevelDelta 取 element 默认值。 */
	protected static void addPoolPiece(GenerationContext context, StructurePiecesBuilder builder,
									   Holder<StructureTemplatePool> pool, BlockPos pos, Rotation rotation) {
		addPoolPiece(context, builder, pool, pos, rotation, Integer.MIN_VALUE);
	}

	/**
	 * 构建并添加；空池静默跳过。
	 *
	 * @param groundLevelDelta 显式地面高度差；传 {@code Integer.MIN_VALUE} 表示取 element 默认值
	 */
	protected static void addPoolPiece(GenerationContext context, StructurePiecesBuilder builder,
									   Holder<StructureTemplatePool> pool, BlockPos pos, Rotation rotation,
									   int groundLevelDelta) {
		PoolElementStructurePiece piece = buildPiece(context, pool, pos, rotation, groundLevelDelta);
		if (piece != null) {
			builder.addPiece(piece);
		}
	}

	// ════════════════════════════════════════════════════════
	// ⑤ 工具
	// ════════════════════════════════════════════════════════

	/** 取 template pool holder；key 未注册时抛异常 */
	protected static Holder<StructureTemplatePool> getPool(GenerationContext context, ResourceKey<StructureTemplatePool> key) {
		return context.registryAccess()
				.lookupOrThrow(Registries.TEMPLATE_POOL)
				.getOrThrow(key);
	}

	/** 随机水平旋转 */
	protected static Rotation randomRotation(RandomSource random) {
		return Util.getRandom(Rotation.values(), random);
	}

	/** 四角最低地表高度（用于埋入式结构，保证不露出地表） */
	protected static int minCornerHeight(GenerationContext context,
										 int minX, int minZ, int sizeX, int sizeZ) {
		return Math.min(
				Math.min(solidFloorY(context, minX, minZ),        solidFloorY(context, minX + sizeX, minZ)),
				Math.min(solidFloorY(context, minX, minZ + sizeZ), solidFloorY(context, minX + sizeX, minZ + sizeZ)));
	}

	/** 四角最高地表高度（用于地基式结构，避免被埋） */
	protected static int maxCornerHeight(GenerationContext context,
										 int minX, int minZ, int sizeX, int sizeZ) {
		return Math.max(
				Math.max(solidFloorY(context, minX, minZ),        solidFloorY(context, minX + sizeX, minZ)),
				Math.max(solidFloorY(context, minX, minZ + sizeZ), solidFloorY(context, minX + sizeX, minZ + sizeZ)));
	}

	/** 四角最低点是否在水下（地表高度 ≤ 海平面 → 该点位于水面或水下地面） */
	protected static boolean lowestCornerUnderwater(GenerationContext context,
													int minX, int minZ, int sizeX, int sizeZ) {
		return minCornerHeight(context, minX, minZ, sizeX, sizeZ)
				<= context.chunkGenerator().getSeaLevel();
	}


}