package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, PvZMod.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> INNER_DEFENCE_HP =
            ATTRIBUTES.register("inner_defence_hp",
                    () -> new RangedAttribute("attribute.name.pvz.inner_defence_hp",
                            0D, 0D, 1000000D).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> OUTER_DEFENCE_HP =
            ATTRIBUTES.register("outer_defence_hp",
                    () -> new RangedAttribute("attribute.name.pvz.outer_defence_hp",
                            0D, 0D, 1000000D).setSyncable(true));

}
