package com.lying.pvz.common.worldgen.structure.island;

import com.lying.pvz.common.init.PvZStructureTypes;
import com.lying.pvz.common.worldgen.structure.PvZFixedPositionStructure;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDensityFunctions;
import com.lying.pvz.data.bootstrap.worldgen.structures.PvZTemplatePools;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * 远古埃及岛心建筑：固定在埃及岛心 (0,0)，顶面 Y=64。
 */
public class EgyptStructure extends PvZFixedPositionStructure {

	public static final MapCodec<EgyptStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance)).apply(instance, EgyptStructure::new));

	private static final int ANCHOR_X = PvZDensityFunctions.ISLAND_CENTERS.get(0).getX();
	private static final int ANCHOR_Z = PvZDensityFunctions.ISLAND_CENTERS.get(0).getZ();
	private static final int ANCHOR_Y = PvZDensityFunctions.TOP_Y;

	public EgyptStructure(StructureSettings settings) {
		super(settings, ANCHOR_X, ANCHOR_Z, ANCHOR_Y, PvZTemplatePools.EGYPT);
	}

	@Override
	public StructureType<?> type() {
		return PvZStructureTypes.EGYPT.get();
	}
}
