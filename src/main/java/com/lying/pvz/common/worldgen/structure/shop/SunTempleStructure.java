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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class SunTempleStructure extends PvZStructureBase {

	public static final MapCodec<SunTempleStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, SunTempleStructure::new)
	);

	/** 浮空岛基准高度 */
	private static final int BASE_Y = 160;
	/** 浮空高度随机浮动 */
	private static final int Y_VARIATION = 10;

	public SunTempleStructure(StructureSettings settings) {
		super(settings);
	}

	// ── ① 群系：数据包 biomes 已由父类保证，这里只附加"沙漠"语义 ──
	@Override
	protected boolean extraBiomeFilter(GenerationContext context, GenerationStub stub, Holder<Biome> biome) {
		return  biome.is(PvZBiomeTags.HAS_SUN_TEMPLE);
	}

	// ── ② 位置：区块中心 + 固定 Y（浮空），带随机浮动 ──
	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		int y = BASE_Y + context.random().nextInt(Y_VARIATION);
		BlockPos pos = resolveAnchor(context, Anchor.CHUNK_CENTER_FIXED_Y, y);

		Holder<StructureTemplatePool> pool = getPool(context, PvZTemplatePools.SUN_TEMPLE);

		// 浮空岛仍需检查垂直范围（防止 Y 超出世界高度）
		if (!fitsVertically(context, pool, pos)) {
			return Optional.empty();
		}

		Rotation rotation = randomRotation(context.random());
		return Optional.of(new GenerationStub(pos, builder ->
				addPoolPiece(context, builder, pool, pos, rotation)));
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.SUN_TEMPLE.get();
	}
}