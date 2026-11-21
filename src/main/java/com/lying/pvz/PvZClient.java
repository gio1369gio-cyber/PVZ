package com.lying.pvz;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = PvZMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PvZMod.MOD_ID, value = Dist.CLIENT)
public class PvZClient {

    public PvZClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

}
