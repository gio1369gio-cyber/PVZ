package com.lying.pvz.common.list;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.*;
import com.lying.pvz.utils.others.WeightList;

public class ZombieList {
    public static final WeightList<IZombieType> ZOMBIE = new WeightList<>();
    static {
        ZOMBIE.addItem(GrassZombies.NORMAL_ZOMBIE.get(), 100);
        ZOMBIE.addItem(GrassZombies.NEWSPAPER_ZOMBIE.get(), 90);
        ZOMBIE.addItem(GrassZombies.SCREENDOOR_ZOMBIE.get(), 70);
        ZOMBIE.addItem(PoolZombies.SNORKEL_ZOMBIE.get(), 100);
        ZOMBIE.addItem(PoolZombies.BALLOON_ZOMBIE.get(), 80);
        ZOMBIE.addItem(RoofZombies.IMP.get(), 80);
        ZOMBIE.addItem(GrassZombies.CONEHEAD_ZOMBIE.get(), 100);
        ZOMBIE.addItem(GrassZombies.POLE_ZOMBIE.get(), 90);
        ZOMBIE.addItem(GrassZombies.DANCING_ZOMBIE.get(), 70);
        ZOMBIE.addItem(GrassZombies.OLD_ZOMBIE.get(), 80);
        ZOMBIE.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE.get(), 85);
        ZOMBIE.addItem(PoolZombies.DIGGER_ZOMBIE.get(), 70);
        ZOMBIE.addItem(PoolZombies.POGO_ZOMBIE.get(), 85);
        ZOMBIE.addItem(CustomZombies.RED_EDGE_ZOMBIE.get(), 75);
        ZOMBIE.addItem(Zombotanies.SUN_FLOWER_ZOMBIE.get(), 85);
    }
}
