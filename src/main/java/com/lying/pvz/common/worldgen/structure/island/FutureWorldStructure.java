package com.lying.pvz.common.worldgen.structure.island;

import com.lying.pvz.common.init.PvZStructureTypes;
import com.lying.pvz.common.worldgen.structure.PvZFixedPositionStructure;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDensityFunctions;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZTemplatePools;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * 未来世界岛心建筑：固定在未来岛心 (-500,0)，顶面 Y=64。
 */
public class FutureWorldStructure extends PvZFixedPositionStructure {

	public static final MapCodec<FutureWorldStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, FutureWorldStructure::new));

	private static final int ANCHOR_X = PvZDensityFunctions.ISLAND_CENTERS.get(3).getX();
	private static final int ANCHOR_Z = PvZDensityFunctions.ISLAND_CENTERS.get(3).getZ();
	private static final int ANCHOR_Y = PvZDensityFunctions.TOP_Y;

	public FutureWorldStructure(StructureSettings settings) {
		super(settings, ANCHOR_X, ANCHOR_Z, ANCHOR_Y, PvZTemplatePools.FUTURE);
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.FUTURE_WORLD.get();
	}
}
