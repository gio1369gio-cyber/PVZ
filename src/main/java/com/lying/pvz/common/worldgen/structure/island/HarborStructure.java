package com.lying.pvz.common.worldgen.structure.island;

import com.lying.pvz.common.init.PvZStructureTypes;
import com.lying.pvz.common.worldgen.structure.PvZFixedPositionStructure;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDensityFunctions;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZTemplatePools;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * 海盗港湾岛心建筑：固定在海盗岛心 (500,0)，顶面 Y=64。
 */
public class HarborStructure extends PvZFixedPositionStructure {

	public static final MapCodec<HarborStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, HarborStructure::new));

	private static final int ANCHOR_X = PvZDensityFunctions.ISLAND_CENTERS.get(1).getX();
	private static final int ANCHOR_Z = PvZDensityFunctions.ISLAND_CENTERS.get(1).getZ();
	private static final int ANCHOR_Y = PvZDensityFunctions.TOP_Y;

	public HarborStructure(StructureSettings settings) {
		super(settings, ANCHOR_X, ANCHOR_Z, ANCHOR_Y, PvZTemplatePools.HARBOR);
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.HARBOR.get();
	}
}
