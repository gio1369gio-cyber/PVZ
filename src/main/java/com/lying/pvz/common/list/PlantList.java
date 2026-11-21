package com.lying.pvz.common.list;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.utils.others.WeightList;

public class PlantList {
    public static final WeightList<IPlantType> PLANT = new WeightList<>();

    static {
        PLANT.addItem(PVZPlants.CHORUS_FRUIT_PULT.get(), 100);
        PLANT.addItem(PVZPlants.ICE_CABBAGE_PULT.get(), 90);
        PLANT.addItem(PVZPlants.FIRE_PEASHOOTER.get(), 70);
        PLANT.addItem(PVZPlants.ROTATE_RADISH.get(), 100);
        PLANT.addItem(PVZPlants.STEEL_PUMPKIN.get(), 100);
        PLANT.addItem(PVZPlants.BLAZE_WART.get(), 80);
        PLANT.addItem(PVZPlants.SUN_FLOWER.get(), 90);
        PLANT.addItem(PVZPlants.WALL_NUT.get(), 70);
        PLANT.addItem(PVZPlants.SNOW_PEA.get(), 80);
        PLANT.addItem(PVZPlants.CHERRY_BOMB.get(), 85);
        PLANT.addItem(PVZPlants.MELON_PULT.get(), 70);
        PLANT.addItem(PVZPlants.JALAPENO.get(), 85);
        PLANT.addItem(PVZPlants.ICE_SHROOM.get(), 85);
    }
}
