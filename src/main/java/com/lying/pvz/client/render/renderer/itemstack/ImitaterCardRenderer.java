package com.lying.pvz.client.render.renderer.itemstack;

import com.lying.pvz.common.level.item.card.ImitaterCardItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ImitaterCardRenderer  implements SpecialModelRenderer<ItemStack> {

    @Override
    public @Nullable ItemStack extractArgument(ItemStack stack) {
        ItemStack summon = resolveSummonCard(stack);
        return summon.isEmpty() ? null : summon;
    }

    @Override
    public void submit(@Nullable ItemStack summonCard, PoseStack poseStack,
                       SubmitNodeCollector collector, int lightCoords, int overlayCoords,
                       boolean hasFoil, int outlineColor) {
        if (summonCard == null || summonCard.isEmpty()) return;

        ItemStackRenderState tmp = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver()
                .updateForTopItem(tmp, summonCard, ItemDisplayContext.NONE, null, null, 0);

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        tmp.submit(poseStack, collector, lightCoords, overlayCoords, outlineColor);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
    }

    private static ItemStack resolveSummonCard(ItemStack stack) {
        if (!(stack.getItem() instanceof ImitaterCardItem)) {
            return ItemStack.EMPTY;
        }
        final ItemStack inner = ImitaterCardItem.getImitatedCard(stack);
        return ImitaterCardItem.isValidContent(inner) ? inner.copy() : ItemStack.EMPTY;
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<ItemStack> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<ItemStack> bake(BakingContext ctx) {
            return new ImitaterCardRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
