package com.lying.pvz;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

/**
 * PVZ 主模组入口。
 *
 */
@Mod(PvZMod.MOD_ID)
public class PvZMod {

    public static final String MOD_ID = "pvz";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_VERSION = "1.0.0";

    public PvZMod(IEventBus modEventBus, ModContainer container) {



        {
            final Pair<PvZConfig.Local, ModConfigSpec> specPair =
                    new ModConfigSpec.Builder().configure(PvZConfig.Local::new);
            container.registerConfig(ModConfig.Type.LOCAL, specPair.getRight());
            PvZConfig.LOCAL_CONFIG = specPair.getLeft();
        }
        {
            final Pair<PvZConfig.Client, ModConfigSpec> specPair =
                    new ModConfigSpec.Builder().configure(PvZConfig.Client::new);
            container.registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
            PvZConfig.CLIENT_CONFIG = specPair.getLeft();
        }

        RegistryHandler.deferredRegister(modEventBus);

        RegistryHandler.coreRegister();

        RegistryHandler.bootstrapAPI();


    }
}