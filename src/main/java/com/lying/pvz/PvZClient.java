package com.lying.pvz;

import com.lying.pvz.client.gui.screen.OverlayPositionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.Collection;
import java.util.List;

@Mod(value = PvZMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PvZMod.MOD_ID, value = Dist.CLIENT)
public class PvZClient {

    public PvZClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (mc, parent) -> new ConfigurationScreen(container, parent, PvZConfigSection::new));
    }

    /**
     * 自定义配置 section：在客户端配置的 Overlay 分组里注入一个"资源条位置"按钮，
     * 点击打开 {@link OverlayPositionScreen} 拖拽调整位置（参考 Peculiar 的歌词位置入口）。
     */
    public static class PvZConfigSection extends ConfigurationScreen.ConfigurationSectionScreen {

        private final ModConfig.Type type;

        public PvZConfigSection(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title) {
            super(parent, type, modConfig, title);
            this.type = type;
        }

        @Override
        protected Collection<? extends Element> createSyntheticValues() {
            // 只在客户端配置显示该入口，避免 LOCAL 配置重复出现
            if (this.type != ModConfig.Type.CLIENT) {
                return List.of();
            }
            Button adjustBtn = Button.builder(
                    Component.translatable("config.pvz.overlay.position.button"),
                    btn -> {
                        Minecraft mc = Minecraft.getInstance();
                        mc.gui.setScreen(new OverlayPositionScreen(mc.gui.screen()));
                    }).width(Button.DEFAULT_WIDTH).build();
            return List.of(new Element(
                    Component.translatable("config.pvz.overlay.position"),
                    Component.translatable("config.pvz.overlay.position.tooltip"),
                    adjustBtn,
                    false));
        }
    }

}
