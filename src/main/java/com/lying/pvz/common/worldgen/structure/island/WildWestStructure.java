package com.lying.pvz.common.worldgen.structure.island;

import com.lying.pvz.common.init.PvZStructureTypes;
import com.lying.pvz.common.worldgen.structure.PvZFixedPositionStructure;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDensityFunctions;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZTemplatePools;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * 狂野西部岛心建筑：固定在西部岛心 (0,500)，顶面 Y=64。
 */
public class WildWestStructure extends PvZFixedPositionStructure {

	public static final MapCodec<WildWestStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, WildWestStructure::new));

	private static final int ANCHOR_X = PvZDensityFunctions.ISLAND_CENTERS.get(2).getX();
	private static final int ANCHOR_Z = PvZDensityFunctions.ISLAND_CENTERS.get(2).getZ();
	private static final int ANCHOR_Y = PvZDensityFunctions.TOP_Y;

	public WildWestStructure(StructureSettings settings) {
		super(settings, ANCHOR_X, ANCHOR_Z, ANCHOR_Y, PvZTemplatePools.WILD);
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.WILD_WEST.get();
	}
}
