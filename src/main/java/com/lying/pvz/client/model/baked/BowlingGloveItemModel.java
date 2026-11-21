package com.lying.pvz.client.model.baked;

import com.lying.pvz.client.render.renderer.itemstack.BowlingGloveRenderer;
import com.lying.pvz.common.level.item.tool.plant.BowlingGloveItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public record BowlingGloveItemModel(ItemQuads gloveQuads, ModelRenderProperties gloveProperties,
        SpecialModelRenderer<BowlingGloveRenderer.Argument> wallNutRenderer) implements ItemModel {

    @Override
    public void update(ItemStackRenderState state, ItemStack stack,
                       ItemModelResolver resolver, ItemDisplayContext ctx,
                       @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        state.appendModelIdentityElement(this);
        state.appendModelIdentityElement(ctx);

        boolean firstPerson = ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        boolean left = ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        boolean thirdPerson = ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;

        // ---------- 手套层 ----------
        ItemStackRenderState.LayerRenderState glove = state.newLayer();
        glove.setQuads(this.gloveQuads);

        // 所有视角统一走模型 display 变换（json 已含 firstperson/thirdperson/ground/gui 各 transform）。
        // 之前第一人称用 setLocalTransform 手动矩阵，位置算错导致手套被移出视野（第一人称看不到手套）。
        this.gloveProperties.applyToLayer(glove, ctx);

        if (stack.hasFoil()) {
            glove.setFoilType(ItemStackRenderState.FoilType.STANDARD);
        }

        // ---------- WallNut 层 ----------
        var type = BowlingGloveItem.getBowlingType(stack);
        if (type.isPresent()) {
            float wnX, wnY, wnZ;
            boolean rotate;
            if (firstPerson) {
                wnX = left ? 0.2F : 0.8F;
                wnY = 0.5F;
                wnZ = -0.2F;
                rotate = true;
            } else if (thirdPerson) {
                wnX = 0.5F;
                wnY = 0.0F;
                wnZ = 0.1F;
                rotate = true;
            } else {
                wnX = 0.35F;
                wnY = 0.3F;
                wnZ = 0.0F;
                rotate = false;
            }

            float angle = rotate ? BowlingGloveRenderer.computeAngle() : 0F;

            BowlingGloveRenderer.Argument arg = new BowlingGloveRenderer.Argument(
                    type.get().getType().getID(),
                    angle,
                    rotate,
                    new Vector3f(wnX, wnY, wnZ)
            );
            state.appendModelIdentityElement(arg);

            ItemStackRenderState.LayerRenderState wn = state.newLayer();
            wn.setupSpecialModel(this.wallNutRenderer, arg);
        }
    }

    public record Unbaked(Identifier gloveModel, SpecialModelRenderer.Unbaked<?> wallNut) implements ItemModel.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Identifier.CODEC.fieldOf("glove_model").forGetter(Unbaked::gloveModel),
                SpecialModelRenderers.CODEC.fieldOf("wall_nut").forGetter(Unbaked::wallNut)
        ).apply(i, Unbaked::new));

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.gloveModel);
        }

        @Override
        @SuppressWarnings("unchecked")
        public ItemModel bake(ItemModel.BakingContext ctx, Matrix4fc parentTransform) {
            ModelBaker baker = ctx.blockModelBaker();
            ResolvedModel resolved = baker.getModel(this.gloveModel);
            TextureSlots slots = resolved.getTopTextureSlots();

            QuadCollection quads = resolved.bakeTopGeometry(
                    slots, baker, BlockModelRotation.IDENTITY
            );

            SpecialModelRenderer<BowlingGloveRenderer.Argument> renderer =
                    (SpecialModelRenderer<BowlingGloveRenderer.Argument>) this.wallNut.bake(ctx);

            return new BowlingGloveItemModel(
                    ItemQuads.split(quads.getAll()),
                    ModelRenderProperties.fromResolvedModel(baker, resolved, slots),
                    renderer
            );
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}