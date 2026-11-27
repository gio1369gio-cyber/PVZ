package com.lying.pvz.data.bootstrap.worldgen.dimension;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DistanceMetric;

import java.util.List;

/**
 * PVZ 维度密度函数：四座<strong>实心倒圆锥</strong>浮岛。
 *
 * <p>与旧版（末地 sloped_cheese + 3D 噪声）不同，这里不要任何噪声起伏：
 * 岛体是数学上精确的倒圆锥，内部全实心，顶面绝对水平。公式：
 * <pre>
 *   radius(y) = yClampedGradient(TIP_Y=8 → TOP_Y=64, 0 → 80)
 *   island_i  = radius(y) - horizontalDistanceTo(center_i)
 *   combined  = max(island_0 .. island_3)
 *   final     = min(combined, yClampedGradient(64→65, +100 → -100))
 * </pre>
 * <ul>
 *   <li>{@code radius(y)}：y≤8 半径 0（锥尖），y≥64 半径 80（底面），中间线性——倒圆锥；</li>
 *   <li>{@code sliceY(dist, 0)}：把 Y 固定为 0 取纯 XZ 水平距离；</li>
 *   <li>四岛取 max：任一岛实心即实心，岛间自然分离；</li>
 *   <li>顶部硬切：y≤64 保留（限制值 +100 不起作用），y≥65 密度 -100 → 绝对平顶。</li>
 * </ul>
 *
 * <p>地形只由本类的密度场决定；地表最上层仍由 {@link PvZMaterialRules#PVZ_SURFACE}
 * 按群系铺主题方块，岛体基底方块由 {@link PvZNoiseSettings} 指定（普通石头）。
 */
public final class PvZDensityFunctions {

    /** 岛心间距（方块）。 */
    public static final int ISLAND_SPACING = 500;
    /** 岛底面（顶面）半径（方块）。 */
    public static final int ISLAND_RADIUS = 80;
    /** 平顶高度：y≤该值为岛体，y>该值为空气。 */
    public static final int TOP_Y = 64;
    /** 锥尖高度：y≤该值半径为 0。 */
    public static final int TIP_Y = 8;

    /** 四岛中心（X, Z）：埃及在原点，其余按十字分布。索引与 BiomeProgression.ORDER 对齐。 */
    public static final List<Vec3i> ISLAND_CENTERS = List.of(
            new Vec3i(0, 0, 0),                 // 0 远古埃及（主岛）
            new Vec3i(ISLAND_SPACING, 0, 0),    // 1 海盗港湾
            new Vec3i(0, 0, ISLAND_SPACING),    // 2 狂野西部
            new Vec3i(-ISLAND_SPACING, 0, 0)    // 3 未来世界
    );

    public static final ResourceKey<DensityFunction> ISLANDS =
            ResourceKey.create(net.minecraft.core.registries.Registries.DENSITY_FUNCTION, StringUtil.id("islands"));

    private PvZDensityFunctions() {}

    /** 注册进 DENSITY_FUNCTION 注册表；须在 NOISE_SETTINGS 之前 bootstrap。 */
    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        context.register(ISLANDS, createIslands());
    }

    /** 实心倒圆锥造岛：四岛取 max，缓存包裹。 */
    public static DensityFunction createIslands() {
        // 半径随高度线性增长：y≤TIP_Y → 0，y≥TOP_Y → ISLAND_RADIUS
        DensityFunction radius = DensityFunctions.yClampedGradient(
                TIP_Y, TOP_Y, 0.0F, (float) ISLAND_RADIUS);

        DensityFunction combined = null;
        for (Vec3i center : ISLAND_CENTERS) {
            // sliceY 把 Y 固定为 0 → 纯 XZ 水平距离
            DensityFunction dist = DensityFunctions.sliceY(
                    DensityFunctions.distanceToPoint(center, DistanceMetric.EUCLIDEAN), 0);
            DensityFunction cone = DensityFunctions.sub(radius, dist);
            combined = combined == null ? cone : DensityFunctions.max(combined, cone);
        }

        // 顶面硬切：y≤64 时 +100 不限制内部密度，y≥65 时 -100 强制空气
        DensityFunction topCut = DensityFunctions.yClampedGradient(
                TOP_Y, TOP_Y + 1, 100.0F, -100.0F);
        return DensityFunctions.cache(DensityFunctions.min(combined, topCut));
    }
}
