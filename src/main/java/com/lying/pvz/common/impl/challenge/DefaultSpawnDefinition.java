package com.lying.pvz.common.impl.challenge;

import com.lying.pvz.api.raid.IAmountDefinition;
import com.lying.pvz.api.raid.IPlacementDefinition;
import com.lying.pvz.api.raid.ISpawnDefinition;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.impl.challenge.amount.ConstantAmount;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public record DefaultSpawnDefinition(EntityType<?> entityType, IAmountDefinition spawnAmount, CompoundTag nbt,
                                     IPlacementDefinition placement, int spawnTick) implements ISpawnDefinition {

    public static final String TYPE = "default";

    public static final MapCodec<DefaultSpawnDefinition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            EntityType.CODEC.fieldOf("entity_type").forGetter(DefaultSpawnDefinition::entityType),
            ChallengeManager.amountCodec()
                    .fieldOf("spawn_amount")
                    .orElse(new ConstantAmount(1))
                    .forGetter(DefaultSpawnDefinition::spawnAmount),
            TagParser.LENIENT_CODEC.optionalFieldOf("nbt")
                    .forGetter(s -> s.nbt().isEmpty() ? Optional.empty() : Optional.of(s.nbt())),
            ChallengeManager.placementCodec()
                    .optionalFieldOf("placement")
                    .forGetter(s -> Optional.ofNullable(s.placement())),
            Codec.INT
                    .optionalFieldOf("spawn_tick", 0)
                    .forGetter(DefaultSpawnDefinition::spawnTick)
    ).apply(i, (type, amount, nbt, placement, tick) ->
            new DefaultSpawnDefinition(type, amount, nbt.orElseGet(CompoundTag::new), placement.orElse(null), tick)));


    public DefaultSpawnDefinition {
        nbt = (nbt == null) ? new CompoundTag() : nbt.copy();
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public int getSpawnTick() { return this.spawnTick; }

    @Override public int getMinAmount() { return this.spawnAmount.getMinAmount(); }
    @Override public int getMaxAmount() { return this.spawnAmount.getMaxAmount(); }
    @Override public int rollAmount(RandomSource random) { return this.spawnAmount.roll(random); }
    @Override
    public IPlacementDefinition getPlacement() { return this.placement; }

    @Override
    public CompoundTag getNBT() { return this.nbt; }

    @Override
    public EntityType<?> getSpawnType() { return this.entityType; }
}