package com.lying.pvz.common.impl.challenge.placement;

import com.lying.pvz.api.raid.IPlacementDefinition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public record OuterPlacement(int min, int max, boolean onSurface) implements IPlacementDefinition {

    public static final String TYPE = "outer";
    public static final MapCodec<OuterPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.optionalFieldOf("min", 0).forGetter(OuterPlacement::min),
            Codec.INT.optionalFieldOf("max", 0).forGetter(OuterPlacement::max),
            Codec.BOOL.optionalFieldOf("ground", true).forGetter(OuterPlacement::onSurface)
    ).apply(i, OuterPlacement::new));

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public BlockPos getPlacePosition(Level level, BlockPos origin) {
        final int lo = Math.min(this.min, this.max);
        final int hi = Math.max(this.min, this.max);
        final RandomSource rand = level.getRandom();
        final double radius = rand.nextDouble() * (hi - lo) + lo;
        final double delta = rand.nextDouble() * 2 * Math.PI;
        final int dx = (int) (radius * Math.sin(delta));
        final int dz = (int) (radius * Math.cos(delta));
        final int y = this.onSurface
                ? level.getHeight(Heightmap.Types.WORLD_SURFACE, origin.getX() + dx, origin.getZ() + dz)
                : origin.getY();
        return new BlockPos(origin.getX() + dx, y, origin.getZ() + dz);
    }
}