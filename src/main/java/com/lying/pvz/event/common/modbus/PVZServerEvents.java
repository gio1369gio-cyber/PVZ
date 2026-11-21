package com.lying.pvz.event.common.modbus;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.damage.PvZDamageTypes;
import com.lying.pvz.common.datapack.ChallengeTypeLoader;
import com.lying.pvz.common.datapack.InvasionTypeLoader;
import com.lying.pvz.common.datapack.LotteryTypeLoader;
import com.lying.pvz.common.datapack.TransactionTypeLoader;
import com.lying.pvz.common.level.block.cubes.OriginBlock;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/**
 * 公共服务器生命周期 game bus 事件入口。
 * 合并 CommonGameBusEvents 与 PVZServerEvents。
 * 无 value，双端加载；这些事件均未实现 IModBusEvent，按源码走 game bus。
 * 注：LevelEvent.Load 在客户端也会触发，放在本类仅为与服务器生命周期相关的 damage type 绑定集中管理。
 */
@EventBusSubscriber(modid = PvZMod.MOD_ID)
public final class PVZServerEvents {

    private PVZServerEvents() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        PvZDamageTypes.bind(event.getServer().registryAccess(), false);
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof Level level) {
            PvZDamageTypes.bind(level.registryAccess(), level.isClientSide());
        }
    }
    @SubscribeEvent
    public static void onAddReloadListener(AddServerReloadListenersEvent event) {
        final HolderLookup.Provider registries = event.getServerResources().getRegistryLookup();
        event.addListener(StringUtil.id("challenges"), new ChallengeTypeLoader(registries));
        event.addListener(StringUtil.id("transactions"), new TransactionTypeLoader(registries));
        event.addListener(StringUtil.id( "invasions"), new InvasionTypeLoader());
        event.addListener(StringUtil.id("lotterys"), new LotteryTypeLoader());
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OriginBlock.updateRadiationMap();
            ShapedRecipePattern.setCraftingSize(5, 5);
        });
    }
}