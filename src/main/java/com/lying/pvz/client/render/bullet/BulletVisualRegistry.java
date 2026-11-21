package com.lying.pvz.client.render.bullet;

import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.client.model.entity.bullet.BallModel;
import com.lying.pvz.client.model.entity.bullet.BeeEntityModel;
import com.lying.pvz.client.model.entity.bullet.CornModel;
import com.lying.pvz.client.model.entity.bullet.KernelModel;
import com.lying.pvz.client.model.entity.bullet.StarModel;
import com.lying.pvz.client.model.entity.bullet.ThornModel;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.projectile.BulletVisuals;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 视觉键 → {@link VisualSpec}。
 *
 * <p>客户端专用。新增一个变体的外观只需要在这里登记一条，
 * 不需要新增渲染器。
 */
public final class BulletVisualRegistry {

    private static final Map<Identifier, VisualSpec> SPECS = new HashMap<>();

    static {
        register(BulletVisuals.PEA, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.PEA.get()), 1.0F));
        register(BulletVisuals.SNOW_PEA, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.SNOW_PEA.get()), 1.0F));
        register(BulletVisuals.FLAME_PEA, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.FLAME_PEA.get()), 1.0F));
        // 目前没有独立的蓝火豌豆物品，先用火焰豌豆的物品外观。
        register(BulletVisuals.BLUE_FLAME_PEA, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.FLAME_PEA.get()), 1.0F));

        register(BulletVisuals.CABBAGE, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.CABBAGE.get()), 1.4F));
        register(BulletVisuals.ICE_CABBAGE, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.ICE_CABBAGE.get()), 1.4F));

        register(BulletVisuals.KERNEL, new VisualSpec.Model(
                KernelModel.LAYER_LOCATION, KernelModel::new,
                StringUtil.id("textures/entity/misc/kernel.png"), null,
                1.0F, VisualSpec.ModelRotation.SPIN_X, 15.0F));

        register(BulletVisuals.BUTTER, new VisualSpec.Block(
                () -> PvZBlockRegister.BUTTER_BLOCK.get().defaultBlockState(), 0.5F));

        register(BulletVisuals.MELON, new VisualSpec.Block(
                () -> Blocks.MELON.defaultBlockState(), 0.8F));
        register(BulletVisuals.FROZEN_MELON, new VisualSpec.Block(
                () -> PvZBlockRegister.FROZEN_MELON.get().defaultBlockState(), 0.8F));
        register(BulletVisuals.GOLDEN_MELON, new VisualSpec.Block(
                () -> Blocks.GOLD_BLOCK.defaultBlockState(), 0.8F));

        register(BulletVisuals.CORN, new VisualSpec.Model(
                CornModel.LAYER_LOCATION, CornModel::new,
                StringUtil.id("textures/entity/misc/corn.png"), null,
                1.2F, VisualSpec.ModelRotation.YAW_PITCH, 0.0F));

        register(BulletVisuals.BALL, new VisualSpec.Model(
                BallModel.LAYER_LOCATION, BallModel::new,
                StringUtil.id("textures/entity/misc/ball.png"), null,
                1.2F, VisualSpec.ModelRotation.YAW_PITCH, 0.0F));

        register(BulletVisuals.FIRE_CRACKER, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.FIRE_CRACKER.get()), 1.0F));
        register(BulletVisuals.POTATO, new VisualSpec.Item(
                () -> new ItemStack(Items.POTATO), 1.0F));
        register(BulletVisuals.NUT, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.NUT.get()), 0.5F));

        // 孢子 / 烟雾共用孢子物品外观。
        register(BulletVisuals.SPORE, new VisualSpec.Item(
                () -> new ItemStack(PvZItemRegister.SPORE.get()), 1.0F));

        final Identifier starTex = StringUtil.id("textures/entity/misc/star.png");
        final Identifier pinkStarTex = StringUtil.id("textures/entity/misc/pink_star.png");
        final Identifier sunLight = StringUtil.id("textures/entity/layer/sun_light.png");
        final Identifier healLight = StringUtil.id("textures/entity/layer/heal_light.png");
        register(BulletVisuals.STAR_YELLOW, new VisualSpec.Model(
                StarModel.LAYER_LOCATION, StarModel::new, starTex, sunLight,
                0.8F, VisualSpec.ModelRotation.YAW, 0.0F));
        register(BulletVisuals.STAR_PINK, new VisualSpec.Model(
                StarModel.LAYER_LOCATION, StarModel::new, pinkStarTex, healLight,
                0.8F, VisualSpec.ModelRotation.YAW, 0.0F));
        register(BulletVisuals.STAR_BIG_YELLOW, new VisualSpec.Model(
                StarModel.LAYER_LOCATION, StarModel::new, starTex, sunLight,
                1.5F, VisualSpec.ModelRotation.YAW, 0.0F));
        register(BulletVisuals.STAR_BIG_PINK, new VisualSpec.Model(
                StarModel.LAYER_LOCATION, StarModel::new, pinkStarTex, healLight,
                1.5F, VisualSpec.ModelRotation.YAW, 0.0F));
        register(BulletVisuals.STAR_HUGE_YELLOW, new VisualSpec.Model(
                StarModel.LAYER_LOCATION, StarModel::new, starTex, sunLight,
                2.0F, VisualSpec.ModelRotation.YAW, 0.0F));
        register(BulletVisuals.STAR_HUGE_PINK, new VisualSpec.Model(
                StarModel.LAYER_LOCATION, StarModel::new, pinkStarTex, healLight,
                2.0F, VisualSpec.ModelRotation.YAW, 0.0F));

        final Identifier thornTex = StringUtil.id("textures/entity/misc/thorn.png");
        final Identifier powerThornTex = StringUtil.id("textures/entity/misc/power_thorn.png");
        register(BulletVisuals.THORN, new VisualSpec.Model(
                ThornModel.LAYER_LOCATION, ThornModel::new, thornTex, null,
                1.2F, VisualSpec.ModelRotation.YAW_PITCH, 0.0F));
        register(BulletVisuals.POWER_THORN, new VisualSpec.Model(
                ThornModel.LAYER_LOCATION, ThornModel::new, powerThornTex, null,
                1.2F, VisualSpec.ModelRotation.YAW_PITCH, 0.0F));
        register(BulletVisuals.THORN_AUTO, new VisualSpec.Model(
                ThornModel.LAYER_LOCATION, ThornModel::new, thornTex, null,
                2.5F, VisualSpec.ModelRotation.YAW_PITCH, 0.0F));
        register(BulletVisuals.POWER_THORN_AUTO, new VisualSpec.Model(
                ThornModel.LAYER_LOCATION, ThornModel::new, powerThornTex, null,
                2.5F, VisualSpec.ModelRotation.YAW_PITCH, 0.0F));

        register(BulletVisuals.METAL, new VisualSpec.VariantItem(variant -> {
            final MetalTypes[] types = MetalTypes.values();
            final MetalTypes type = (variant >= 0 && variant < types.length)
                    ? types[variant] : MetalTypes.EMPTY;
            return new ItemStack(MetalTypes.getMetalItem(type));
        }, 1.0F));

        // 蜜蜂（追踪弹）。模型复用原版蜜蜂几何，贴图复用原版愤怒蜜蜂。
        register(BulletVisuals.BEE, new VisualSpec.Model(
                BeeEntityModel.LAYER_LOCATION, BeeEntityModel::new,
                Identifier.withDefaultNamespace("textures/entity/bee/bee_angry.png"), null,
                0.45F, VisualSpec.ModelRotation.YAW_PITCH, 0.0F));

        // 原始豌豆（石豌豆 / 熔岩豌豆）。
        register(BulletVisuals.STONE_PEA, new VisualSpec.Block(
                () -> PvZBlockRegister.PEA_BLOCK.get().defaultBlockState(), 0.4F));
        register(BulletVisuals.MAGMA_PEA, new VisualSpec.Block(
                () -> Blocks.MAGMA_BLOCK.defaultBlockState(), 0.4F));

        // 放大镜草光束。用发光方块（海晶灯）渲染 + 末地烛拖尾粒子，使其清晰可见。
        register(BulletVisuals.LIGHT_BEAM, new VisualSpec.Block(
                () -> Blocks.SEA_LANTERN.defaultBlockState(), 0.6F));

        // 合唱果。
        register(BulletVisuals.CHORUS_FRUIT, new VisualSpec.Block(
                () -> Blocks.CHORUS_PLANT.defaultBlockState(), 0.625F));
    }

    private BulletVisualRegistry() {
    }

    public static void register(Identifier key, VisualSpec spec) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(spec, "spec");
        SPECS.put(key, spec);
    }

    public static @Nullable VisualSpec get(@Nullable Identifier key) {
        return key == null ? null : SPECS.get(key);
    }
}
