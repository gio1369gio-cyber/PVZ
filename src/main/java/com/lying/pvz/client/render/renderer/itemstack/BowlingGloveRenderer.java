package com.lying.pvz.client.render.renderer.itemstack;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.client.model.entity.plant.defence.TallNutModel;
import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.common.impl.plant.PVZPlants;
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

public record BowlingGloveRenderer(Function<Identifier, RenderType> renderType,
                                   Map<Identifier, Entry> entriesByType)
        implements SpecialModelRenderer<BowlingGloveRenderer.Argument> {

    public record Entry(Identifier texture, float scale, float size, ModelPart model) {}

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
        float scale = 0.4F * entry.scale() * entry.size();
        poseStack.scale(scale, scale, scale);
        poseStack.scale(1F, -1F, 1F);
        poseStack.translate(0F, -0.6F, 0F);
        if (arg.rotate()) {
            poseStack.rotateDegrees(Axis.XP, arg.angle());
        }
        poseStack.translate(0F, -1F, 0F);

        collector.submitModelPart(
                entry.model(), poseStack, this.renderType.apply(entry.texture()),
                lightCoords, overlayCoords, null, -1, outlineColor);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        // 用默认坚果模型计算 GUI 缩放范围即可，各类型差异不大。
        for (Entry entry : this.entriesByType.values()) {
            entry.model().getExtentsForGui(poseStack, output);
            break;
        }
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
            Function<Identifier, RenderType> renderType = RenderTypes::entityCutout;

            Map<Identifier, Entry> entries = new HashMap<>();

            for (BowlingGloveItem.BowlingType type : BowlingGloveItem.allBowlingTypes()) {
                PVZAPI.client().paz(type.getType().getID()).ifPresent(info -> {
                    ModelPart model = bakeModel(ctx, type.getType().getID());
                    entries.put(type.getType().getID(),
                            new Entry(info.getTexture(), info.getRenderScale(), type.getSize(), model));
                });
            }

            return new BowlingGloveRenderer(renderType, Map.copyOf(entries));
        }

        /** 根据保龄球类型选择正确的植物模型层。巨型坚果用高坚果模型，其余用坚果模型。 */
        private ModelPart bakeModel(SpecialModelRenderer.BakingContext ctx, Identifier typeId) {
            if (PVZPlants.GIANT_NUT.getId().equals(typeId)) {
                return ctx.entityModelSet().bakeLayer(TallNutModel.LAYER_LOCATION);
            }
            if (PVZPlants.EXPLODE_O_NUT.getId().equals(typeId)) {
                return ctx.entityModelSet().bakeLayer(WallNutModel.LAYER_LOCATION_2);
            }
            return ctx.entityModelSet().bakeLayer(WallNutModel.LAYER_LOCATION);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}