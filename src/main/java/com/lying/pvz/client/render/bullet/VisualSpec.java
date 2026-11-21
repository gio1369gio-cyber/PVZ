package com.lying.pvz.client.render.bullet;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 弹药视觉描述。服务端只同步 {@code visualKey}，客户端在这里解析成
 * 物品 / 方块 / 模型。
 *
 * <p>方块视觉的 scale 与命中盒半宽同源，渲染时方块中心严格落在实体位置。
 */
public sealed interface VisualSpec {

    record Item(Supplier<ItemStack> stack, float scale) implements VisualSpec {
    }

    /** 外观随变体变化的物品（磁力弹按金属类型取物品）。 */
    record VariantItem(java.util.function.IntFunction<ItemStack> resolver, float scale) implements VisualSpec {
    }

    record Block(Supplier<BlockState> state, float scale) implements VisualSpec {
    }

    record Model(ModelLayerLocation layer,
                 Function<ModelPart, EntityModel<PvZEntityRenderState>> factory,
                 Identifier texture, @Nullable Identifier layerTexture,
                 float scale, ModelRotation rotation, float spinSpeed) implements VisualSpec {
    }

    enum ModelRotation {
        NONE,
        SPIN_X,
        YAW,
        YAW_PITCH
    }
}
