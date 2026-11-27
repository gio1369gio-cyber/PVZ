package com.lying.pvz.event.common.modbus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.impl.PVZRegistryBootstrap;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.block.cubes.OriginBlock;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.spawn.SpawnPlacementRegistry;
import com.lying.pvz.common.payload.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZMiscEvents {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                InvasionStatePayload.TYPE, InvasionStatePayload.STREAM_CODEC, InvasionStatePayload.Handler::handle);
        registrar.playToClient(
                InvasionWavePayload.TYPE, InvasionWavePayload.STREAM_CODEC, InvasionWavePayload.Handler::handle);
        registrar.playToClient(
                MissionStagePayload.TYPE, MissionStagePayload.STREAM_CODEC, MissionStagePayload.Handler::handle);
        registrar.playToClient(
                PlaySoundPayload.TYPE, PlaySoundPayload.STREAM_CODEC, PlaySoundPayload.Handler::handle);
        registrar.playToServer(
                ClickButtonPayload.TYPE, ClickButtonPayload.STREAM_CODEC, ClickButtonPayload.Handler::handle);
        registrar.playToClient(
                EntityInteractPayload.TYPE,EntityInteractPayload.STREAM_CODEC,EntityInteractPayload.Handler::handle);
        registrar.playToClient(SpawnParticlePayload.TYPE,SpawnParticlePayload.STREAM_CODEC,SpawnParticlePayload.Handler::handle);
    }


    @SubscribeEvent
    public static void onTagsChange(TagsUpdatedEvent event) {
        if (event.shouldUpdateStaticData()) {
            OriginBlock.updateRadiationMap();
        }
    }

    @SubscribeEvent
    public static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        SpawnPlacementRegistry.register(event);
    }
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent ev) {

        // 同一 EntityType 只允许 put 一次，否则 DefaultAttributes.hasSupplier 会抛异常
        Set<EntityType<?>> seen = new HashSet<>();

        // ---- 植物 ----
        for (var holder : PVZRegistryBootstrap.PLANTS.getEntries()) {
            holder.get().getEntityType()
                    .filter(seen::add)
                    .ifPresent(entity -> ev.put(entity, PlantEntity.createPlantAttributes()));
        }

        // ---- 僵尸 ----
        for (var holder : PVZRegistryBootstrap.ZOMBIES.getEntries()) {
            holder.get().getEntityType()
                    .filter(seen::add)
                    .ifPresent(entity -> ev.put(entity, PVZZombieEntity.createZombieAttributes()));
        }
        List.of(PvZEntityRegister.SUN, PvZEntityRegister.COIN, PvZEntityRegister.JEWEL, PvZEntityRegister.ENERGY,
                        PvZEntityRegister.RED_SUN,
                        PvZEntityRegister.CRAZY_DAVE, PvZEntityRegister.SUN_DAVE, PvZEntityRegister.PANNEY, PvZEntityRegister.FOODIE_ZOMBIE)
                .forEach(h -> ev.put(h.get(), Mob.createMobAttributes().build()));
    }

}
