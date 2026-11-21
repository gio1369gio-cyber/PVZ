package com.lying.pvz;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.card.PvZCardComponents;
import com.lying.pvz.client.PVZClientSetup;
import com.lying.pvz.client.gui.ItemScreen;
import com.lying.pvz.client.impl.PVZClientLookupImpl;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.impl.*;
import com.lying.pvz.common.impl.plant.CustomPlants;
import com.lying.pvz.common.impl.plant.MemePlants;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.impl.zombie.*;
import com.lying.pvz.common.init.*;
import com.lying.pvz.common.invasion.InvasionManager;
import com.lying.pvz.common.invasion.type.MissionTypes;
import com.lying.pvz.common.level.block.cubes.OriginBlock;
import com.lying.pvz.common.transaction.TransactionCodecs;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * 只负责 DeferredRegister 的注册与 common 侧初始化。
 *
 */
public class RegistryHandler {

    public static void deferredRegister(IEventBus bus) {
        //6个自定义registry（必须在 NewRegistryEvent 之前）
        PVZRegistryBootstrap.init(bus);
        //NeoForge DeferredRegister 挂到 mod bus
        PvZBlockRegister.BLOCKS.register(bus);
        PvZBlockRegister.BLOCK_ITEMS.register(bus);
        PvZItemRegister.ITEMS.register(bus);
        PvZBlockEntityRegister.BLOCK_ENTITY_TYPES.register(bus);
        PvZEntityRegister.ENTITY_TYPES.register(bus);
        PvZMenuRegister.MENU_TYPES.register(bus);
        PvZSoundRegister.SOUNDS.register(bus);
        PvZEffectRegister.EFFECTS.register(bus);
        PvZPotionRegister.POTIONS.register(bus);
        PvZParticleRegister.PARTICLE_TYPES.register(bus);
        PvZRecipeRegister.RECIPE_TYPES.register(bus);
        PvZRecipeRegister.RECIPE_SERIALIZERS.register(bus);
        PvZAttributes.ATTRIBUTES.register(bus);
        PvZAttachments.ATTACHMENT_TYPES.register(bus);
        PvZConsumeEffects.CONSUME_EFFECT_TYPES.register(bus);
        PvZDataComponents.COMPONENTS.register(bus);
        PVZEntityDataSerializers.SERIALIZERS.register(bus);
        PvZTriggers.TRIGGERS.register(bus);
        PvZCardComponents.COMPONENTS.register(bus);
        PvZItemGroups.TABS.register(bus);
        PvZStructureTypes.STRUCTURE_TYPES.register(bus);
        PvZPoolElementTypes.POOL_ELEMENT_TYPES.register(bus);
        // 客户端专属 setup：仅客户端加载 PVZClientSetup 类，服务端不触发
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            PVZClientSetup.register(bus);
        }
    }


    public static void coreRegister() {
        CoolDowns.init();
        EssenceTypes.init();
        RankTypes.init();
        SkillTypes.init();
        PVZPlants.init();
        OtherPlants.init();
        MemePlants.init();
        CustomPlants.init();
        Zombotanies.init();
        RoofZombies.init();
        PoolZombies.init();
        OtherZombies.init();
        GrassZombies.init();
        CustomZombies.init();
        TransactionCodecs.init();
    }

    public static void bootstrapAPI() {
        PVZAPI.bootstrapLookup(new PVZLookupImpl());
        ChallengeManager raid = new ChallengeManager();
        raid.initRegistry();
        PVZAPI.bootstrapRaid(raid);
        PVZAPI.bootstrapInvasion(new InvasionManager());
        MissionTypes.bootstrap();

    }

}