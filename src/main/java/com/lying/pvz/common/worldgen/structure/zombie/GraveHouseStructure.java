package com.lying.pvz.common.worldgen.structure.zombie;

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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class GraveHouseStructure extends PvZStructureBase {

	public static final MapCodec<GraveHouseStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, GraveHouseStructure::new));

	/** 建筑跨度（16×16，1 个区块） */
	private static final int SPAN = 16;
	/** 四角起伏上限 */
	private static final int MAX_SPREAD = 4;
	/**
	 * 埋入深度 = 泥土层数(3) + 1 格余量。
	 * 以四角最低点为基准，三层泥土全部落在地表以下。
	 */
	private static final int EMBED = 3;
	private static final int GROUND_DELTA = 0;
	public GraveHouseStructure(StructureSettings settings) {
		super(settings);
	}

	// ── ① 群系：数据包 biomes 由父类保证，这里再限定针叶林 ──
	@Override
	protected boolean extraBiomeFilter(GenerationContext context, GenerationStub stub, Holder<Biome> biome) {
		return biome.is(PvZBiomeTags.HAS_GRAVE_HOUSE);
	}

	// ── ② 位置：区块最小角 + 四角最低点（嵌入地形） ──
	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		int x = chunkPos.getMinBlockX();
		int z = chunkPos.getMinBlockZ();

		int hMin = minCornerHeight(context, x, z, SPAN, SPAN);
		int hMax = maxCornerHeight(context, x, z, SPAN, SPAN);
		if (hMax - hMin > MAX_SPREAD) {
			return Optional.empty();
		}

		BlockPos anchor = new BlockPos(x, hMin - EMBED, z);

		Holder<StructureTemplatePool> pool = getPool(context, PvZTemplatePools.GRAVE_HOUSE);
		Rotation rotation = randomRotation(context.random());

		return Optional.of(new GenerationStub(anchor, builder ->
				addPoolPiece(context, builder, pool, anchor, rotation, GROUND_DELTA)));
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.GRAVE_HOUSE.get();
	}
}