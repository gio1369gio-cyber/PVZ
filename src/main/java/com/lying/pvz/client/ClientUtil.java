package com.lying.pvz.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;


public class ClientUtil {

    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static Font getFont() {
        return Minecraft.getInstance().font;
    }

    public static ClientLevel getClientLevel() {
        return Minecraft.getInstance().level;
    }
}
