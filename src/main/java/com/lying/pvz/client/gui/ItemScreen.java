package com.lying.pvz.client.gui;

import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.client.gui.screen.ChallengeDescriptionScreen;
import com.lying.pvz.client.gui.screen.ChallengeInfoScreen;
import com.lying.pvz.common.level.item.display.ChallengeEnvelopeItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ItemScreen {

    public static void init() {
        EnvelopeClientBridge.set(ItemScreen::open);
    }

    private static void open(ItemStack stack, InteractionHand hand) {
        ChallengeEnvelopeItem.getRaidComponent(stack).ifPresent(def -> {
            if (hand == InteractionHand.MAIN_HAND) {
                ClientUtil.getMinecraft().gui.setScreen(new ChallengeDescriptionScreen(def));
            } else {
                ClientUtil.getMinecraft().gui.setScreen(new ChallengeInfoScreen(def));
            }
        });
    }
}
