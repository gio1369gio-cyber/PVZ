package com.lying.pvz.common.list;


import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.utils.others.WeightList;

public class PlantItemList {
    public static final WeightList<PlantCardItem> PLANT_ITEM = new WeightList<>();
    static {
        PLANT_ITEM.addItem(PvZItemRegister.PEA_SHOOTER_ENJOY_CARD.get(),100);
        PLANT_ITEM.addItem(PvZItemRegister.ICE_SHROOM_ENJOY_CARD.get(),90);
        PLANT_ITEM.addItem(PvZItemRegister.WALL_NUT_ENJOY_CARD.get(),80);
        PLANT_ITEM.addItem(PvZItemRegister.POTATO_MINE_ENJOY_CARD.get(),90);
        PLANT_ITEM.addItem(PvZItemRegister.SQUASH_ENJOY_CARD.get(),100);
        PLANT_ITEM.addItem(PvZItemRegister.THREE_PEATER_ENJOY_CARD.get(),70);
        PLANT_ITEM.addItem(PvZItemRegister.PLANTERN_ENJOY_CARD.get(),70);
        PLANT_ITEM.addItem(PvZItemRegister.CHERRY_BOMB_ENJOY_CARD.get(),80);
    }
}
