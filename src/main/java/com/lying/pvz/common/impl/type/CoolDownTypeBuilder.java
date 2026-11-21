package com.lying.pvz.common.impl.type;

import com.lying.pvz.api.content.skill.ICoolDown;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

/**
 * {@link ICoolDown} 的构建器。
 *
 * <p>冷却按公式定义（而非数组），便于在等级跨度大时描述。
 *
 * <p><b>示例</b>：
 * <pre>{@code
 * public static final DeferredHolder<ICoolDown, ICoolDown> SUPER_FAST =
 *     COOL_DOWNS.register("super_fast", () -> CoolDownTypeBuilder
 *         .of("pvz", "super_fast")
 *         .cd(level -> MathUtil.getIncreaseAverage(level, 5, 80, 60))
 *         .build());
 * }</pre>
 */
public final class CoolDownTypeBuilder {

    private final Identifier id;
    private IntUnaryOperator cdFunction = level -> 50;

    private CoolDownTypeBuilder(Identifier id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public static CoolDownTypeBuilder of(Identifier id) {
        return new CoolDownTypeBuilder(id);
    }

    public static CoolDownTypeBuilder of(String namespace, String path) {
        return new CoolDownTypeBuilder(Identifier.fromNamespaceAndPath(namespace, path));
    }

    /**
     * 冷却函数。
     *
     * @param fn 接收技能等级（{@code FAST_CD} 的等级），返回冷却 tick 数
     */
    public CoolDownTypeBuilder cd(IntUnaryOperator fn) {
        this.cdFunction = Objects.requireNonNull(fn, "fn");
        return this;
    }

    public ICoolDown build() {
        return new CoolDownTypeImpl(this);
    }

    private static final class CoolDownTypeImpl implements ICoolDown {

        private final Identifier id;
        private final IntUnaryOperator cdFunction;

        CoolDownTypeImpl(CoolDownTypeBuilder b) {
            this.id = b.id;
            this.cdFunction = b.cdFunction;
        }

        @Override
        public Identifier getID() {
            return this.id;
        }

        @Override
        public MutableComponent getText() {
            return Component.translatable(
                    "misc." + this.id.getNamespace() + ".cd." + this.id.getPath());
        }

        @Override
        public int getCD(int level) {
            return this.cdFunction.applyAsInt(level);
        }
    }
}