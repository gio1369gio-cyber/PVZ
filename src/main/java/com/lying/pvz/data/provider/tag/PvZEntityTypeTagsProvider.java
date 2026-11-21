package com.lying.pvz.data.provider.tag;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.misc.PvZEnumProxy;
import com.lying.pvz.common.tag.PvZEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.entity.MobCategory;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PvZEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public PvZEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, PvZMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        /* forge / neoforge tags */
        this.tag(PvZEntityTypeTags.PVZ_PLANT_GROUP_ENTITIES)
                .addTag(PvZEntityTypeTags.PVZ_PLANTS);

        this.tag(PvZEntityTypeTags.PVZ_ZOMBIE_GROUP_ENTITIES)
                .addTag(PvZEntityTypeTags.PVZ_ZOMBIES);

        this.tag(PvZEntityTypeTags.PVZ_OTHER_GUARDIANS)
                .add(EntityTypeIds.CAT).add(EntityTypeIds.DOLPHIN).add(EntityTypeIds.FOX).add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.LLAMA).add(EntityTypeIds.VILLAGER).add(EntityTypeIds.WANDERING_TRADER)
                .add(EntityTypeIds.SNOW_GOLEM).add(EntityTypeIds.IRON_GOLEM).add(EntityTypeIds.POLAR_BEAR)
                .add(EntityTypeIds.PANDA);

        this.tag(PvZEntityTypeTags.PVZ_OTHER_MONSTERS)
                .addAll(getFilterKeys(provider, type -> type.getCategory() == MobCategory.MONSTER));

        this.tag(PvZEntityTypeTags.PVZ_NOT_MONSTERS).add(EntityTypeIds.PIG);
        this.tag(PvZEntityTypeTags.PVZ_NOT_GUARDIANS).add(EntityTypeIds.PIG);

        /* pvz tags */
        this.tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(PvZEntityRegister.PEA.key())
                .add(PvZEntityRegister.SPORE.key())
                .add(PvZEntityRegister.FUME.key())
                .add(PvZEntityRegister.STAR.key())
                .add(PvZEntityRegister.THORN.key())
                .add(PvZEntityRegister.METAL.key())
                .add(PvZEntityRegister.FIRE_CRACKER.key())
                // ===== pult bullets =====
                .add(PvZEntityRegister.CABBAGE.key())
                .add(PvZEntityRegister.KERNEL.key())
                .add(PvZEntityRegister.BUTTER.key())
                .add(PvZEntityRegister.CORN.key())
                .add(PvZEntityRegister.MELON.key())
                .add(PvZEntityRegister.BALL.key())
                .add(PvZEntityRegister.NUT.key())
                .add(PvZEntityRegister.POTATO.key())
                // ===== special =====
                .add(PvZEntityRegister.TARGET_ARROW.key());
        this.tag(PvZEntityTypeTags.TOMB_STONE)
                .add(PvZEntityRegister.TOMB_STONE.key());

        this.tag(PvZEntityTypeTags.FLAME_WOOD)
                .add(PvZEntityRegister.TORCH_WOOD.key());

        this.tag(PvZEntityTypeTags.BLUE_FLAME_WOOD)
                .add(PvZEntityRegister.TORCH_WOOD.key());

        this.tag(PvZEntityTypeTags.PVZ_PLANTS)
                .addAll(getFilterKeys(provider, type -> type.getCategory() == PvZEnumProxy.PVZ_PLANT_PROXY.getValue()))
                .add(PvZEntityRegister.CRAZY_DAVE.key());

        final List<ResourceKey<EntityType<?>>> pvzZombies =
                getFilterKeys(provider, type -> type.getCategory() == PvZEnumProxy.PVZ_ZOMBIE_PROXY.getValue())
                        .toList();
        this.tag(PvZEntityTypeTags.PVZ_ZOMBIES).addAll(pvzZombies);

        this.tag(PvZEntityTypeTags.BUNGEE_SPAWNS)
                .add(PvZEntityRegister.NORMAL_ZOMBIE.key())
                .add(PvZEntityRegister.CONEHEAD_ZOMBIE.key())
                .add(PvZEntityRegister.BUCKETHEAD_ZOMBIE.key())
                .add(PvZEntityRegister.LADDER_ZOMBIE.key());

        this.tag(EntityTypeTags.UNDEAD).addAll(pvzZombies);

        this.tag(EntityTypeTags.ARROWS).add(PvZEntityRegister.TARGET_ARROW.key());

    }


    private Stream<ResourceKey<EntityType<?>>> getFilterKeys(HolderLookup.Provider provider,
                                                             Predicate<EntityType<?>> predicate) {
        return provider.lookupOrThrow(Registries.ENTITY_TYPE)
                .listElements()
                .filter(holder -> predicate.test(holder.value()))
                .map(holder -> (ResourceKey<EntityType<?>>) holder.key())
                .sorted(Comparator.comparing(ResourceKey::identifier));
    }

    @Override
    public String getName() {
        return "Plants vs Zombies entity type tags";
    }
}