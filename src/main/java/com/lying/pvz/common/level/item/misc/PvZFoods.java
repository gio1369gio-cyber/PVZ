package com.lying.pvz.common.level.item.misc;

import net.minecraft.world.food.FoodProperties;

public class PvZFoods {

	public static final FoodProperties FAKE_BRAIN = new FoodProperties.Builder()
			.nutrition(5).saturationModifier(0.7F).build();

	public static final FoodProperties REAL_BRAIN = new FoodProperties.Builder()
			.nutrition(5).saturationModifier(0.5F)
			.build();

	public static final FoodProperties CANDY = new FoodProperties.Builder()
			.nutrition(1).saturationModifier(0.4F).build();

	public static final FoodProperties CHOCOLATE = new FoodProperties.Builder()
			.nutrition(3).saturationModifier(1F).build();

	public static final FoodProperties PEA_SOUP = new FoodProperties.Builder()
			.nutrition(5).saturationModifier(0.6f).build();

	public static final FoodProperties COOKED_BRAIN = new FoodProperties.Builder()
			.nutrition(7).saturationModifier(1.0f).build();

	public static final FoodProperties CABBAGE = new FoodProperties.Builder()
			.nutrition(3).saturationModifier(0.5f).build();

	public static final FoodProperties ICE_CABBAGE = new FoodProperties.Builder()
			.nutrition(4).saturationModifier(0.5f).build();


	public static final FoodProperties CORN = new FoodProperties.Builder()
			.nutrition(4).saturationModifier(0.4f).build();

	public static final FoodProperties POP_CORN = new FoodProperties.Builder()
			.nutrition(5).saturationModifier(0.5f).build();

	public static final FoodProperties TACOS = new FoodProperties.Builder()
			.nutrition(8).saturationModifier(1f).build();

	public static final FoodProperties PEPPER = new FoodProperties.Builder()
			.nutrition(5).saturationModifier(1.2f).build();

    public static final FoodProperties GARLIC = new FoodProperties.Builder()
			.nutrition(2).saturationModifier(0.3f).build();
}