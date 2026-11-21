package com.lying.pvz.data.provider.model;


import com.lying.pvz.PvZMod;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;


public class PvZModelProvider extends ModelProvider {


	public PvZModelProvider(PackOutput output) {
		super(output, PvZMod.MOD_ID);
	}


	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		//方块
		PvZBlockModelRules.cubeAllWithItem(blockModels);
		PvZBlockModelRules.cubeBottomTopWithItem(blockModels);
		PvZBlockModelRules.cubeWithItem(blockModels);
		PvZBlockModelRules.cubeColumnWithItem(blockModels);
		PvZBlockModelRules.horizontalWithItem(blockModels);
		PvZBlockModelRules.woodAxisGroup(blockModels);
		PvZBlockModelRules.cropGroup(blockModels);
		PvZBlockModelRules.cornGroup(blockModels);
		PvZBlockModelRules.crossAgedGroup(blockModels);
		PvZBlockModelRules.crossGroup(blockModels);
		PvZBlockModelRules.handwrittenBlockItemGroup(blockModels);
		PvZBlockModelRules.generatedBlockItemGroup(blockModels);
		PvZBlockModelRules.customModelBlockGroup(blockModels);
		PvZBlockModelRules.screenDoor(blockModels);
		//物品
		PvZItemModelRules.registerBasicItems(itemModels);
		PvZItemModelRules.registerSpawnEggs(itemModels);
		PvZItemModelRules.registerPlantCards(itemModels);
		PvZItemModelRules.registerHandheldItems(itemModels);
		PvZItemModelRules.registerSunStorageSaplings(itemModels);
		PvZItemModelRules.registerGardenCompassFrames(itemModels);
		PvZItemModelRules.registerHandwrittenItems(itemModels);
		PvZItemModelRules.registerBowingItems(itemModels);
		PvZItemModelRules.registerImitaterItems(itemModels);
	}


	@Override
	public String getName() {
		return "Plants vs Zombies block models and states";
	}

}