package com.lying.pvz.data.bootstrap.misc;

import com.lying.pvz.common.damage.PvZDamageTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public class PvZDamageType {

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(PvZDamageTypes.PVZ_GENERIC,new DamageType(
                "pvz.generic", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                0.1F, DamageEffects.HURT, DeathMessageType.DEFAULT));
    }

}
