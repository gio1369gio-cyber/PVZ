package com.lying.pvz.common.impl.type;

import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.content.skill.SkillScope;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link ISkillType} 的构建器。
 *
 * <p><b>等级约定</b>：{@link #values(float...)} 必须比 {@link #costs(int...)} 多一个元素。
 * 若 {@code costs = [1, 2, 3]}，则 {@code values} 必须有 4 个元素（0 级、1 级、2 级、3 级），
 * {@code getMaxLevel() = 3}。
 *
 * <p><b>0 级技能</b>：允许 {@code costs} 为空（即不可升级的"纯开关"技能）。
 * 此时 {@code getMaxLevel() = 0}，调用方不应调用 {@link ISkillType#getCostAt(int)}。
 * 实现层在越界访问时抛出带上下文的 {@link IllegalArgumentException}，而非
 * 裸的 {@link IndexOutOfBoundsException}。
 *
 * <p><b>示例</b>：
 * <pre>{@code
 * public static final DeferredHolder<ISkillType, ISkillType> FAST_CD =
 *     SKILLS.register("fast_cd", () -> SkillTypeBuilder
 *         .of("pvz", "fast_cd")
 *         .scope(SkillScope.CARD)
 *         .values(0F, 1.25F, 2.5F, 3.75F, 5F)
 *         .costs(1, 2, 3, 3)
 *         .build());
 * }</pre>
 */
public final class SkillTypeBuilder {

    private final Identifier id;
    private SkillScope scope = SkillScope.PLANT;
    private final List<Float> values = new ArrayList<>();
    private final List<Integer> costs = new ArrayList<>();
    private String conflictGroup = null;
    private int extraSun = 0;

    private SkillTypeBuilder(Identifier id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public static SkillTypeBuilder of(Identifier id) {
        return new SkillTypeBuilder(id);
    }

    public static SkillTypeBuilder of(String namespace, String path) {
        return new SkillTypeBuilder(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public SkillTypeBuilder scope(SkillScope scope) {
        this.scope = Objects.requireNonNull(scope, "scope");
        return this;
    }

    public SkillTypeBuilder values(float... vs) {
        this.values.clear();
        for (float v : vs) {
            this.values.add(v);
        }
        return this;
    }

    public SkillTypeBuilder costs(int... cs) {
        this.costs.clear();
        for (int c : cs) {
            this.costs.add(c);
        }
        return this;
    }

    /**
     * 冲突组标识。不设置时默认使用 {@code id.toString()}——即每个技能自成一組。
     */
    public SkillTypeBuilder conflictGroup(String group) {
        this.conflictGroup = group;
        return this;
    }

    public SkillTypeBuilder extraSun(int sun) {
        this.extraSun = sun;
        return this;
    }

    public ISkillType build() {
        if (this.values.isEmpty()) {
            throw new IllegalStateException(
                    "Skill " + this.id + " requires at least one value (level 0)");
        }
        if (this.values.size() != this.costs.size() + 1) {
            throw new IllegalStateException(
                    "Skill " + this.id + " requires exactly one more value than cost. "
                            + "Got values=" + this.values.size() + ", costs=" + this.costs.size());
        }
        return new SkillTypeImpl(this);
    }

    private static final class SkillTypeImpl implements ISkillType {

        private final Identifier id;
        private final SkillScope scope;
        private final List<Float> values;
        private final List<Integer> costs;
        private final String conflictGroup;
        private final int extraSun;

        SkillTypeImpl(SkillTypeBuilder b) {
            this.id = b.id;
            this.scope = b.scope;
            this.values = List.copyOf(b.values);
            this.costs = List.copyOf(b.costs);
            this.conflictGroup = b.conflictGroup != null ? b.conflictGroup : b.id.toString();
            this.extraSun = b.extraSun;
        }

        @Override
        public Identifier getID() {
            return this.id;
        }

        @Override
        public MutableComponent getText() {
            return Component.translatable(
                    "skill." + this.id.getNamespace() + "." + this.id.getPath());
        }

        @Override
        public SkillScope getScope() {
            return this.scope;
        }

        @Override
        public float getValueAt(int level) {
            if (level < 0 || level >= this.values.size()) {
                throw new IllegalArgumentException(
                        "Skill " + this.id + " has no value for level " + level
                                + "; valid range is [0, " + (this.values.size() - 1) + "]");
            }
            return this.values.get(level);
        }

        @Override
        public int getCostAt(int level) {
            if (level < 0 || level >= this.costs.size()) {
                throw new IllegalArgumentException(
                        "Skill " + this.id + " has no cost for level " + level
                                + "; valid range is [0, " + (this.costs.size() - 1) + "]");
            }
            return this.costs.get(level);
        }

        @Override
        public int getMaxLevel() {
            return this.costs.size();
        }

        @Override
        public String getConflictGroup() {
            return this.conflictGroup;
        }

        @Override
        public int getExtraSun() {
            return this.extraSun;
        }

        @Override
        public MutableComponent getDescription() {
            return Component.translatable(
                    "skill." + this.id.getNamespace() + "." + this.id.getPath() + ".desc");
        }
    }
}