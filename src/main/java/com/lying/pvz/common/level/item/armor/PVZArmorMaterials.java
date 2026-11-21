package com.lying.pvz.common.level.item.armor;


import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

public class PVZArmorMaterials {

	public  static  final  ArmorMaterial FOOTBALL = new ArmorMaterial(25,
			makeDefense(3,7,6,3,5),15,SoundEvents.ARMOR_EQUIP_IRON,
			0.5F,0.1F, ItemTags.REPAIRS_IRON_ARMOR, EquipmentAssets.IRON);


	public  static  final  ArmorMaterial GIGA = new ArmorMaterial(36,
			makeDefense(3,8,6,3,6),15,SoundEvents.ARMOR_EQUIP_NETHERITE,
			2F,0.2F, ItemTags.REPAIRS_NETHERITE_ARMOR, EquipmentAssets.NETHERITE);


	private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
		return Maps.newEnumMap(Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs,
				ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body));
	}
}