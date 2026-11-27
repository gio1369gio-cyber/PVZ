package com.lying.pvz.event.client.modbus;


import com.lying.pvz.PvZMod;
import com.lying.pvz.client.PvZEntityRenderer;
import com.lying.pvz.client.PvZModelLayer;
import com.lying.pvz.client.gui.ItemScreen;
import com.lying.pvz.client.gui.screen.*;
import com.lying.pvz.client.gui.screen.shop.DaveShopScreen;
import com.lying.pvz.client.gui.screen.shop.PennyShopScreen;
import com.lying.pvz.client.gui.screen.shop.SunShopScreen;
import com.lying.pvz.client.model.baked.BowlingGloveItemModel;
import com.lying.pvz.client.particle.*;
import com.lying.pvz.client.particle.bomb.CherryBombParticle;
import com.lying.pvz.client.particle.bomb.PotatoMineParticle;
import com.lying.pvz.client.properties.GardenCompassAngleProperty;
import com.lying.pvz.client.render.renderer.itemstack.BowlingGloveRenderer;
import com.lying.pvz.client.render.renderer.itemstack.ImitaterCardRenderer;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.event.client.handler.PvZArmorHandler;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;

@EventBusSubscriber(modid = PvZMod.MOD_ID, value = Dist.CLIENT)
public class ClientModBusEvents {
    /**
     *
     * 粒子
     */
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(PvZParticleRegister.RED_BOMB.get(), CherryBombParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.YELLOW_BOMB.get(), PotatoMineParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.DIRT_BURST_OUT.get(), DirtBurstOutParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.YELLOW_FLAME.get(), YellowFlameParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.BLUE_FLAME.get(), BlueFlameParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.SLEEP.get(), SleepParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.SPORE.get(), SporeParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.FUME.get(), FumeParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.SNOW_FLOWER.get(), SnowFlowerParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.DOOM.get(), DoomParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.MELON_SLICE.get(), MelonSliceParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.FROZEN_MELON_SLICE.get(), FrozenMelonSliceParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.GREEN_SWEEP.get(), GreenSweepParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.POP_CORN.get(), PopCornParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.GOLD_NUGGET.get(),GoldNuggetParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.ORIGINAL_FUME.get(),OriginalFumeParticle.Provider::new);
        event.registerSpriteSet(PvZParticleRegister.SONIC_BOOM.get(), SonicBoomParticle.Provider::new);
    }

    /**
     * 按键绑定
     */
    public static final KeyMapping.Category PVZ_CATEGORY =
            new KeyMapping.Category(StringUtil.id("pvz"));

    public static final Lazy<KeyMapping> SHOW_OVERLAY = Lazy.of(() ->
            new KeyMapping("key.pvz.show_overlay", KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYBOARD, InputConstants.KEY_RBRACKET, PVZ_CATEGORY));

    public static final Lazy<KeyMapping> LEFT_TOGGLE = Lazy.of(() ->
            new KeyMapping("key.pvz.left_toggle", KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYBOARD, InputConstants.KEY_LEFT, PVZ_CATEGORY));

    public static final Lazy<KeyMapping> RIGHT_TOGGLE = Lazy.of(() ->
            new KeyMapping("key.pvz.right_toggle", KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYBOARD, InputConstants.KEY_RIGHT, PVZ_CATEGORY));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(SHOW_OVERLAY.get());
        event.register(LEFT_TOGGLE.get());
        event.register(RIGHT_TOGGLE.get());
    }

    /**
     * 屏幕/菜单
     */
    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(PvZMenuRegister.ALMANAC.get(), AlmanacScreen::new);
        event.register(PvZMenuRegister.PEA_GUN.get(), PeaGunScreen::new);
        event.register(PvZMenuRegister.DAVE_SHOP.get(), DaveShopScreen::new);
        event.register(PvZMenuRegister.SUN_SHOP.get(), SunShopScreen::new);
        event.register(PvZMenuRegister.SUN_CONVERTER.get(), SunConverterScreen::new);
        event.register(PvZMenuRegister.FRAGMENT_SPLICE.get(), FragmentSpliceScreen::new);
        event.register(PvZMenuRegister.SLOT_MACHINE.get(), SlotMachineScreen::new);
        event.register(PvZMenuRegister.PENNY_SHOP.get(), PennyShopScreen::new);
        event.register(PvZMenuRegister.ESSENCE_ALTAR.get(), EssenceAltarScreen::new);
        event.register(PvZMenuRegister.CARD_FUSION.get(), CardFusionScreen::new);
        event.register(PvZMenuRegister.IMITATER.get(), ImitaterScreen::new);
        event.register(PvZMenuRegister.CARD_PACK.get(), CardPackScreen::new);
    }

    /**
     * 特殊物品模型
     */
    @SubscribeEvent
    public static void registerItemModels(RegisterItemModelsEvent event) {
        event.register(StringUtil.id("bowling_glove"), BowlingGloveItemModel.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(StringUtil.id("bowling_glove_wall_nut"), BowlingGloveRenderer.Unbaked.MAP_CODEC);
        event.register(StringUtil.id("imitater_card"), ImitaterCardRenderer.Unbaked.MAP_CODEC);
    }

    /**
     * 花园指南针角度指针属性（NeoForge 26.3 数据驱动 item model property）。
     * 注册后可在 items/garden_compass.json 中以 {@code range_dispatch} +
     * {@code property: "pvz:garden_compass_angle"} 引用。
     */
    @SubscribeEvent
    public static void registerItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(StringUtil.id("garden_compass_angle"), GardenCompassAngleProperty.CODEC);
    }


    /**
     * 模型渲染
     */
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        PvZEntityRenderer.register(event);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        PvZModelLayer.register(event);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        PvZArmorHandler.registerArmorItem(event);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemScreen.init();
    }
}



