package com.lying.pvz.common.invasion.type;

import com.lying.pvz.api.invasion.ISpawnType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.TagParser;

import java.util.Optional;

public record SpawnType(EntityType<?> entityType, CompoundTag nbt, int weight,
        int invasionLevel, PlaceType placeType) implements ISpawnType {

    public SpawnType {
        nbt = (nbt == null) ? new CompoundTag() : nbt.copy();
    }

    public static final Codec<SpawnType> CODEC = RecordCodecBuilder.create(i -> i.group(
            EntityType.CODEC.fieldOf("entity_type").forGetter(SpawnType::entityType),
            TagParser.LENIENT_CODEC.optionalFieldOf("nbt")
                    .forGetter(s -> s.nbt().isEmpty() ? Optional.empty() : Optional.of(s.nbt())),
            Codec.INT.optionalFieldOf("spawn_weight", 100).forGetter(SpawnType::weight),
            Codec.INT.optionalFieldOf("invasion_level", 1).forGetter(SpawnType::invasionLevel),
            PlaceType.CODEC.optionalFieldOf("placement", PlaceType.SNOW_LAND).forGetter(SpawnType::placeType)
    ).apply(i, (entityType, nbt, weight, invasionLevel, placeType) ->
            new SpawnType(entityType, nbt.orElseGet(CompoundTag::new),
                    weight, invasionLevel, placeType)));

    @Override
    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Override
    public CompoundTag getNbt() {
        return nbt;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getInvasionLevel() {
        return invasionLevel;
    }


    @Override
    public PlaceType getPlaceType() {
        return placeType;
    }

    @Override
    public boolean checkPos(Level level, BlockPos pos) {
        final var below = level.getBlockState(pos.below());
        return switch (placeType) {
            case LAND -> below.isSolid()
                    && below.getFluidState().isEmpty()
                    && level.canSeeSky(pos);

            case SNOW -> (below.is(Blocks.SNOW) || below.is(Blocks.SNOW_BLOCK))
                    && level.canSeeSky(pos);

            case SNOW_LAND -> (below.is(Blocks.SNOW) || below.is(Blocks.SNOW_BLOCK)
                    || (below.isSolid() && below.getFluidState().isEmpty()))
                    && level.canSeeSky(pos);

            case SKY -> level.canSeeSky(pos);

            case WATER -> !below.getFluidState().isEmpty() && level.canSeeSky(pos);
        };
    }
}
