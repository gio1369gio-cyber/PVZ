package com.lying.pvz.common.impl.challenge.placement;

import com.lying.pvz.api.raid.IPlacementDefinition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public record CenterPlacement(int radius, boolean onSurface) implements IPlacementDefinition {

    public static final String TYPE = "center";
    public static final MapCodec<CenterPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.optionalFieldOf("radius", 1).forGetter(CenterPlacement::radius),
            Codec.BOOL.optionalFieldOf("ground", true).forGetter(CenterPlacement::onSurface)
    ).apply(i, CenterPlacement::new));

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public BlockPos getPlacePosition(Level level, BlockPos origin) {
        final RandomSource rand = level.getRandom();
        final int dx = rand.nextInt(this.radius * 2 + 1) - this.radius;
        final int dz = rand.nextInt(this.radius * 2 + 1) - this.radius;
        final int y = this.onSurface
                ? level.getHeight(Heightmap.Types.WORLD_SURFACE, origin.getX() + dx, origin.getZ() + dz)
                : origin.getY();
        return new BlockPos(origin.getX() + dx, y, origin.getZ() + dz);
    }
}