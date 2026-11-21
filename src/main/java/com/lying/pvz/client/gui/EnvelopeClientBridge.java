package com.lying.pvz.client.gui;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class EnvelopeClientBridge {
    private EnvelopeClientBridge() {}

    @Nullable
    private static Opener instance;

    public static void set(Opener opener) {
        instance = opener;
    }

    public static void open(ItemStack stack, InteractionHand hand) {
        Opener o = instance;
        if (o != null) o.open(stack, hand);
    }

    public interface Opener {
        void open(ItemStack stack, InteractionHand hand);
    }
}
