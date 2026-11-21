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

public class DolphinHouseStructure extends PvZStructureBase {

	public static final MapCodec<DolphinHouseStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, DolphinHouseStructure::new));

	public DolphinHouseStructure(StructureSettings settings) {
		super(settings);
	}

	// ── ① 群系：数据包 biomes 由父类保证，这里再限定海洋 ──
	@Override
	protected boolean extraBiomeFilter(GenerationContext context, GenerationStub stub, Holder<Biome> biome) {
		return biome.is(PvZBiomeTags.HAS_DOLPHIN_HOUSE);
	}

	// ── ② 位置：区块最小角 + 海平面（维度自适应，不写死 63） ──
	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		int x = chunkPos.getMinBlockX();
		int z = chunkPos.getMinBlockZ();

		int seaLevel = context.chunkGenerator().getSeaLevel();
		BlockPos anchor = new BlockPos(x, seaLevel, z);

		Holder<StructureTemplatePool> pool = getPool(context, PvZTemplatePools.DOLPHIN_HOUSE);
		Rotation rotation = randomRotation(context.random());

		return Optional.of(new GenerationStub(anchor, builder ->
				addPoolPiece(context, builder, pool, anchor, rotation)));
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.DOLPHIN_HOUSE.get();
	}
}