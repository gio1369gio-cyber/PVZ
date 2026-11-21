package com.lying.pvz.common.init;


import com.lying.pvz.PvZMod;
import com.lying.pvz.common.menu.*;
import com.lying.pvz.common.menu.shop.DaveShopMenu;
import com.lying.pvz.common.menu.shop.PennyShopMenu;
import com.lying.pvz.common.menu.shop.SunShopMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class PvZMenuRegister {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, PvZMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<AlmanacMenu>> ALMANAC = MENU_TYPES.register("almanac",
            () -> new MenuType<>((id, inv) -> new AlmanacMenu(id, inv.player), FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<CardFusionMenu>> CARD_FUSION = MENU_TYPES.register("card_fusion",
            () -> IMenuTypeExtension.create(CardFusionMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<EssenceAltarMenu>> ESSENCE_ALTAR = MENU_TYPES.register("essence_altar",
            () -> IMenuTypeExtension.create(EssenceAltarMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<SlotMachineMenu>> SLOT_MACHINE = MENU_TYPES.register("slot_machine",
            () -> IMenuTypeExtension.create(SlotMachineMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<FragmentSpliceMenu>> FRAGMENT_SPLICE = MENU_TYPES.register("fragment_splice",
            () -> IMenuTypeExtension.create(FragmentSpliceMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<CardPackMenu>> CARD_PACK = MENU_TYPES.register("card_pack", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new CardPackMenu(windowId, inv.player)));

    public static final DeferredHolder<MenuType<?>, MenuType<SunConverterMenu>> SUN_CONVERTER = MENU_TYPES.register("sun_converter", () ->
            IMenuTypeExtension.create(SunConverterMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<PeaGunMenu>> PEA_GUN = MENU_TYPES.register("pea_gun", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new PeaGunMenu(windowId, inv.player)));

    public static final DeferredHolder<MenuType<?>, MenuType<DaveShopMenu>> DAVE_SHOP = MENU_TYPES.register("dave_shop", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new DaveShopMenu(windowId, inv.player, data.readInt())));

    public static final DeferredHolder<MenuType<?>, MenuType<SunShopMenu>> SUN_SHOP = MENU_TYPES.register("sun_shop", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new SunShopMenu(windowId, inv.player, data.readInt())));


    public static final DeferredHolder<MenuType<?>, MenuType<PennyShopMenu>> PENNY_SHOP = MENU_TYPES.register("penny_shop", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new PennyShopMenu(windowId, inv.player, data.readInt())));


    public static final DeferredHolder<MenuType<?>, MenuType<ImitaterMenu>> IMITATER = MENU_TYPES.register("imitater", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new ImitaterMenu(windowId, inv.player)));


}