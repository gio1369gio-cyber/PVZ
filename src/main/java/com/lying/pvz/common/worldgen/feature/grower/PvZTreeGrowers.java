package com.lying.pvz.common.worldgen.feature.grower;

import com.lying.pvz.PvZMod;
import com.lying.pvz.data.bootstrap.worldgen.feature.PvZFeatures;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;

public class PvZTreeGrowers {
    public static final TreeGrower NUT_TREE = new TreeGrower(
            PvZMod.MOD_ID + ":nut_tree",
            WeightedList.of(new Weighted<>(PvZFeatures.NUT_TREE, 9).value()),
            WeightedList.of(),
            WeightedList.of(),
            PvZFeatures.NUT_TREE
    );
}
