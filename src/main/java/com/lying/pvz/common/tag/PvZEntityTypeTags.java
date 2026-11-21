package com.lying.pvz.common.tag;


import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class PvZEntityTypeTags {

    // NeoForge
    public static final TagKey<EntityType<?>> PVZ_PLANT_GROUP_ENTITIES = neoForge("pvz_plant_group_entities");
    public static final TagKey<EntityType<?>> PVZ_ZOMBIE_GROUP_ENTITIES = neoForge("pvz_zombie_group_entities");
    public static final TagKey<EntityType<?>> PVZ_OTHER_MONSTERS = neoForge("pvz_other_monsters");
    public static final TagKey<EntityType<?>> PVZ_OTHER_GUARDIANS = neoForge("pvz_other_guardians");
    public static final TagKey<EntityType<?>> PVZ_NOT_MONSTERS = neoForge("pvz_not_monsters");
    public static final TagKey<EntityType<?>> PVZ_NOT_GUARDIANS = neoForge("pvz_not_guardians");

    // pvz
    public static final TagKey<EntityType<?>> PVZ_PLANTS = pvzTag("pvz_plants");
    public static final TagKey<EntityType<?>> PVZ_ZOMBIES = pvzTag("pvz_zombies");
    public static final TagKey<EntityType<?>> BUNGEE_SPAWNS = pvzTag("bungee_spawns");
    public static final TagKey<EntityType<?>> TOMB_STONE = pvzTag("tomb_stone");
    public static final TagKey<EntityType<?>> FLAME_WOOD = pvzTag("flame_wood");
    public static final TagKey<EntityType<?>> BLUE_FLAME_WOOD = pvzTag("blue_flame_wood");

    private static TagKey<EntityType<?>> pvzTag(String name){
        return TagKey.create(Registries.ENTITY_TYPE, StringUtil.id(name));
    }

    private static TagKey<EntityType<?>> neoForge(String name){
        return TagKey.create(Registries.ENTITY_TYPE, StringUtil.idNeoforge( name));
    }
}