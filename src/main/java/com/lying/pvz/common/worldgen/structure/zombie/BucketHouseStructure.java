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

public class BucketHouseStructure extends PvZStructureBase {

	public static final MapCodec<BucketHouseStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, BucketHouseStructure::new));

	/** 建筑跨度（16×16，1 个区块），用于四角采样 */
	private static final int SPAN = 16;
	/** 四角起伏上限：无泥土层地基式建筑，超过此值会明显悬空 */
	private static final int MAX_SPREAD = 4;
	/** 无泥土层，锚点即模板底面，显式 0 以绕过 jigsaw 默认 delta */
	private static final int GROUND_DELTA = 0;

	public BucketHouseStructure(StructureSettings settings) {
		super(settings);
	}

	// ── ① 群系：数据包 biomes 由父类保证，这里再限定陆地 ──
	@Override
	protected boolean extraBiomeFilter(GenerationContext context, GenerationStub stub, Holder<Biome> biome) {
		return biome.is(PvZBiomeTags.HAS_BUCKET_HOUSE);
	}
	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		int x = chunkPos.getMinBlockX();
		int z = chunkPos.getMinBlockZ();

		if (lowestCornerUnderwater(context, x, z, SPAN, SPAN)) {
			return Optional.empty();
		}
		int hMax = maxCornerHeight(context, x, z, SPAN, SPAN);
		int hMin = minCornerHeight(context, x, z, SPAN, SPAN);
		if (hMax - hMin > MAX_SPREAD) {
			return Optional.empty();
		}
		BlockPos anchor = new BlockPos(x, hMax, z);

		Holder<StructureTemplatePool> pool = getPool(context, PvZTemplatePools.BUCKET_HOUSE);
		Rotation rotation = randomRotation(context.random());

		return Optional.of(new GenerationStub(anchor, builder ->
				addPoolPiece(context, builder, pool, anchor, rotation, GROUND_DELTA)));
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.BUCKET_HOUSE.get();
	}
}