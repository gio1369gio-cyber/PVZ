package com.lying.pvz.client.render.status;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.projectile.mark.MarkRegistry;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 状态视觉注册表。
 *
 * <p>新增效果/标记只需要登记一条规则：判定条件 + 视觉 + 优先级。
 * {@code DynamicStatusLayer} 负责按优先级渲染。
 */
public final class StatusVisualRegistry {

    private static final Identifier COLD_LAYER =
            StringUtil.id("textures/entity/layer/cold_layer.png");

    private record Rule(Predicate<StatusContext> active, StatusVisual visual, int priority) {
    }

    private static final List<Rule> RULES = new ArrayList<>();

    static {
        // 寒冷：整模型半透明蓝层（后续可替换成带冰纹的贴图）。
        register(ctx -> ctx.cold(),
                new StatusVisual.FullSkin(COLD_LAYER,
                        ARGB.colorFromFloat(0.72F, 0.70F, 0.85F, 1.0F)),
                10);

        // 冰冻：蓝色冰块碎屑，视觉上明确"被冻住"。
        register(ctx -> ctx.frozen(),
                new StatusVisual.Particle(
                        () -> new BlockParticleOption(
                                ParticleTypes.BLOCK, Blocks.BLUE_ICE.defaultBlockState()),
                        1, 0.85F, 5),
                20);

        // 黄油：头顶黄油方块碎屑。
        register(ctx -> ctx.buttered(),
                new StatusVisual.Particle(
                        () -> new BlockParticleOption(
                                ParticleTypes.BLOCK,
                                PvZBlockRegister.BUTTER_BLOCK.get().defaultBlockState()),
                        1, 0.95F, 8),
                30);

        // 蹦极标记：头顶目标箭物品碎屑。只有被标记的目标才会出现。
        register(ctx -> ctx.hasMark(MarkRegistry.BUNGEE_TARGET),
                new StatusVisual.Particle(
                        () -> new ItemParticleOption(
                                ParticleTypes.ITEM, PvZItemRegister.TARGET_ARROW.get()),
                        1, 1.12F, 8),
                40);
    }

    private StatusVisualRegistry() {
    }

    public static void register(Predicate<StatusContext> active,
                                StatusVisual visual, int priority) {
        RULES.add(new Rule(Objects.requireNonNull(active, "active"),
                Objects.requireNonNull(visual, "visual"), priority));
    }

    public static List<StatusVisual> collect(StatusContext context) {
        if (RULES.isEmpty()) {
            return List.of();
        }
        final List<Rule> active = new ArrayList<>();
        for (Rule rule : RULES) {
            if (rule.active().test(context)) {
                active.add(rule);
            }
        }
        if (active.isEmpty()) {
            return List.of();
        }
        active.sort(Comparator.comparingInt(Rule::priority));
        final List<StatusVisual> visuals = new ArrayList<>(active.size());
        for (Rule rule : active) {
            visuals.add(rule.visual());
        }
        return visuals;
    }
}
