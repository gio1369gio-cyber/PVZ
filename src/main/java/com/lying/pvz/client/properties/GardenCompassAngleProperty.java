package com.lying.pvz.client.properties;

import com.lying.pvz.common.level.item.tool.GardenCompassItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * 花园指南针的角度指针属性（0.0F ~ 1.0F）。
 * <p>
 * 对应 NeoForge 26.3 新数据驱动 item model 系统中的
 * {@link RangeSelectItemModelProperty}。读取 {@link GardenCompassItem#getPos(ItemStack)}
 * 中保存的目标 {@link BlockPos}，计算出指针应指向的角度，供
 * {@code range_dispatch} 在 32 帧材质间切换。
 * <p>
 * 行为对齐 1.21.1 版 {@code ClientProxy#setUpClient()} 中注册的
 * {@code ItemProperties} 指南针逻辑（含摆动 wobble 与随机漂移）。
 */
public final class GardenCompassAngleProperty implements RangeSelectItemModelProperty {

    /** 单例属性实例（供 datagen 与运行时复用）。 */
    public static final GardenCompassAngleProperty INSTANCE = new GardenCompassAngleProperty();

    /** 该属性无需额外数据，使用 unit codec 即可。 */
    public static final MapCodec<GardenCompassAngleProperty> CODEC =
            MapCodec.unit(INSTANCE);

    private final Wobbler wobble = new Wobbler();
    private final Wobbler wobbleRandom = new Wobbler();

    private GardenCompassAngleProperty() {
    }

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level,
                     @Nullable ItemOwner owner, int seed) {
        if (owner == null) {
            return 0.0F;
        }
        if (level == null && owner.level() instanceof ClientLevel cl) {
            level = cl;
        }
        if (level == null) {
            return 0.0F;
        }

        BlockPos target = GardenCompassItem.getPos(itemStack);
        long tick = level.getGameTime();
        Vec3 position = owner.position();

        if (target != null && !(position.distanceToSqr(
                (double) target.getX() + 0.5D, position.y(),
                (double) target.getZ() + 0.5D) < 1.0E-5D)) {

            Player player = owner.asLivingEntity() instanceof Player p ? p : null;
            boolean localPlayer = player != null && player.isLocalPlayer();

            // 26.3 中 ItemOwner#getVisualRotationYInDegrees() 已统一各 owner 类型
            // （玩家 / 物品展示框 / 掉落物）的视觉朝向，无需再分派。
            double d1 = Mth.positiveModulo(owner.getVisualRotationYInDegrees() / 360.0D, 1.0D);
            double d2 = angleTo(Vec3.atCenterOf(target), position) / ((float) Math.PI * 2F);

            double d3;
            if (localPlayer) {
                if (this.wobble.shouldUpdate(tick)) {
                    this.wobble.update(tick, 0.5D - (d1 - 0.25D));
                }
                d3 = d2 + this.wobble.rotation;
            } else {
                d3 = 0.5D - (d1 - 0.25D - d2);
            }
            return Mth.positiveModulo((float) d3, 1.0F);
        } else {
            if (this.wobbleRandom.shouldUpdate(tick)) {
                this.wobbleRandom.update(tick, Math.random());
            }
            double d0 = this.wobbleRandom.rotation
                    + (double) ((float) itemStack.hashCode() / 2.14748365E9F);
            return Mth.positiveModulo((float) d0, 1.0F);
        }
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return CODEC;
    }

    private static double angleTo(Vec3 pos, Vec3 entityPos) {
        return Math.atan2(pos.z() - entityPos.z(), pos.x() - entityPos.x());
    }

    /** 摆动 / 随机漂移辅助，对齐 1.21.1 版 {@code ClientProxy.Angle}。 */
    private static final class Wobbler {
        private double rotation;
        private double deltaRotation;
        private long lastUpdateTick;

        private boolean shouldUpdate(long tick) {
            return this.lastUpdateTick != tick;
        }

        private void update(long tick, double target) {
            this.lastUpdateTick = tick;
            double d0 = target - this.rotation;
            d0 = Mth.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
            this.deltaRotation += d0 * 0.1D;
            this.deltaRotation *= 0.8D;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0D);
        }
    }
}
