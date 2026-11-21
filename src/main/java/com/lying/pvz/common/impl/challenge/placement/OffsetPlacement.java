package com.lying.pvz.common.impl.challenge.placement;

import com.lying.pvz.api.raid.IPlacementDefinition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record OffsetPlacement(Optional<BlockPos> center, BlockPos offset) implements IPlacementDefinition {

    public static final String TYPE = "offset";
    public static final MapCodec<OffsetPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BlockPos.CODEC.optionalFieldOf("center").forGetter(OffsetPlacement::center),
            BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(OffsetPlacement::offset)
    ).apply(i, OffsetPlacement::new));

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public BlockPos getPlacePosition(Level level, BlockPos origin) {
        return this.center.orElse(origin).offset(this.offset.getX(), this.offset.getY(), this.offset.getZ());
    }
}