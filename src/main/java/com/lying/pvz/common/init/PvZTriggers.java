package com.lying.pvz.common.init;


import com.lying.pvz.PvZMod;
import com.lying.pvz.common.advancement.trigger.*;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PvZTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, PvZMod.MOD_ID);

    public static final Supplier<SunAmountTrigger> SUN_AMOUNT = TRIGGERS.register("sun_amount", SunAmountTrigger::new);
    public static final Supplier<TreeLevelTrigger> TREE_LEVEL = TRIGGERS.register("tree_level", TreeLevelTrigger::new);
    public static final Supplier<EntityEffectAmountTrigger> ENTITY_EFFECT_AMOUNT = TRIGGERS.register("entity_effect_amount", EntityEffectAmountTrigger::new);
    public static final Supplier<PlayerPlacePAZTrigger> PLAYER_PLACE_PAZ = TRIGGERS.register("player_place_paz", PlayerPlacePAZTrigger::new);
    public static final Supplier<CharmZombieTrigger> CHARM_ZOMBIE = TRIGGERS.register("charm_zombie", CharmZombieTrigger::new);
    public static final Supplier<PlantLevelTrigger> PLANT_LEVEL = TRIGGERS.register("plant_level", PlantLevelTrigger::new);
    public static final Supplier<MoneyTrigger> MONEY = TRIGGERS.register("money", MoneyTrigger::new);
    public static final Supplier<PlantSuperTrigger> PLANT_SUPER = TRIGGERS.register("plant_super", PlantSuperTrigger::new);
    public static final Supplier<ChallengeTrigger> CHALLENGE = TRIGGERS.register("challenge", ChallengeTrigger::new);
    public static final Supplier<InvasionTrigger> INVASION = TRIGGERS.register("invasion", InvasionTrigger::new);
    public static final Supplier<InvasionMissionTrigger> INVASION_MISSION = TRIGGERS.register("invasion_mission", InvasionMissionTrigger::new);
    public static final Supplier<PVZTradeTrigger> TRADE = TRIGGERS.register("trade", PVZTradeTrigger::new);
    public static final Supplier<SlotMachineTrigger> SLOT_MACHINE = TRIGGERS.register("slot_machine", SlotMachineTrigger::new);
    public static final Supplier<PlayerPlantKillTrigger> PLAYER_PLANT_KILL = TRIGGERS.register("player_plant_kill", PlayerPlantKillTrigger::new);
}