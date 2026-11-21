package com.lying.pvz.client.render.renderer.itemstack;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.common.level.item.tool.plant.BowlingGloveItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public record BowlingGloveRenderer(ModelPart root, Function<Identifier, RenderType> renderType,
                                   Map<Identifier, Entry> entriesByType)
        implements SpecialModelRenderer<BowlingGloveRenderer.Argument> {

    public record Entry(Identifier texture, float scale) {}

    public record Argument(Identifier typeId, float angle, boolean rotate, Vector3f position) {}

    @Override
    public @Nullable Argument extractArgument(ItemStack stack) {
        return BowlingGloveItem.getBowlingType(stack)
                .map(type -> new Argument(type.getType().getID(), computeAngle(), true,
                        new Vector3f())).orElse(null);
    }

    @Override
    public void submit(@Nullable Argument arg, PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        if (arg == null) return;
        Entry entry = this.entriesByType.get(arg.typeId());
        if (entry == null) return;

        poseStack.pushPose();
        poseStack.translate(arg.position().x(), arg.position().y(), arg.position().z());
        float scale = 0.4F * entry.scale();
        poseStack.scale(scale, scale, scale);
        poseStack.scale(1F, -1F, 1F);
        poseStack.translate(0F, -0.6F, 0F);
        if (arg.rotate()) {
            poseStack.rotateDegrees(Axis.XP, arg.angle());
        }
        poseStack.translate(0F, -1F, 0F);

        collector.submitModelPart(
                this.root, poseStack, this.renderType.apply(entry.texture()),
                lightCoords, overlayCoords, null, -1, outlineColor);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.root.getExtentsForGui(poseStack, output);
    }

    public static float computeAngle() {
        Minecraft mc = Minecraft.getInstance();
        float partial = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        long time = mc.level != null ? mc.level.getGameTime() : 0L;
        return (time + partial) * 12.0F;
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Argument> {

        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Argument> bake(SpecialModelRenderer.BakingContext ctx) {
            ModelPart root = ctx.entityModelSet().bakeLayer(WallNutModel.LAYER_LOCATION);
            Function<Identifier, RenderType> renderType = RenderTypes::entityCutout;

            Map<Identifier, Entry> entries = new HashMap<>();

            for (BowlingGloveItem.BowlingType type : BowlingGloveItem.allBowlingTypes()) {
                PVZAPI.client().paz(type.getType().getID()).ifPresent(info ->
                        entries.put(type.getType().getID(),
                                new Entry(info.getTexture(), info.getRenderScale())));
            }

            return new BowlingGloveRenderer(root, renderType, Map.copyOf(entries));
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}