package com.lying.pvz.common.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

/**
 * 固定坐标结构基类：结构只生成在<b>一个预定的世界坐标</b>（通常是四主题岛的岛心）。
 *
 * <p>与随机散布结构不同，本类不依赖 {@code StructurePlacement} 的随机网格：
 * {@link #findGenerationPoint} 直接判断当前被检查的区块是否覆盖目标坐标，
 * 是则以目标坐标为锚点（固定 Y）返回 stub，否则返回 {@link Optional#empty()}。
 * 配合 {@code spacing=1} 的 RandomSpread placement（使每个区块都被轮询），
 * 最终效果是"该结构在目标坐标恰好生成一座"。
 *
 * <p>子类只需在构造时传入目标坐标、固定 Y、模板池 key，并实现 {@link #type()}。
 */
public abstract class PvZFixedPositionStructure extends PvZStructureBase {

	/** 目标岛心 / 锚点 X 坐标（方块）。 */
	private final int anchorX;
	/** 目标岛心 / 锚点 Z 坐标（方块）。 */
	private final int anchorZ;
	/** 锚点固定 Y（岛顶面高度）。 */
	private final int anchorY;
	/** 模板池 key，用于获取建筑 NBT。 */
	private final net.minecraft.resources.ResourceKey<StructureTemplatePool> poolKey;

	protected PvZFixedPositionStructure(StructureSettings settings,
										int anchorX, int anchorZ, int anchorY,
										net.minecraft.resources.ResourceKey<StructureTemplatePool> poolKey) {
		super(settings);
		this.anchorX = anchorX;
		this.anchorZ = anchorZ;
		this.anchorY = anchorY;
		this.poolKey = poolKey;
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();

		// 只在该结构目标坐标所在的区块生成；其它区块直接放弃。
		if (chunkPos.x() != (anchorX >> 4) || chunkPos.z() != (anchorZ >> 4)) {
			return Optional.empty();
		}

		BlockPos anchor = new BlockPos(anchorX, anchorY, anchorZ);
		Holder<StructureTemplatePool> pool = getPool(context, poolKey);

		if (!fitsVertically(context, pool, anchor)) {
			return Optional.empty();
		}

		// 固定朝向：建筑 NBT 已按朝向设计，不随机旋转。
		Rotation rotation = Rotation.NONE;

		return Optional.of(new GenerationStub(anchor, builder ->
				addPoolPiece(context, builder, pool, anchor, rotation)));
	}

	@Override
	public abstract StructureType<?> type();
}
