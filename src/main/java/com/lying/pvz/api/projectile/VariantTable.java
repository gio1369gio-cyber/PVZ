package com.lying.pvz.api.projectile;

import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 变体表：id → 变体定义，以及"当前变体 + 触发器 → 新变体"的转移表。
 *
 * <p>0 号变体必须存在，作为默认与兜底。
 *
 * <p><b>触发器的语义</b>：触发器只提供 {@link Identifier}（例如
 * {@code pvz:heat} / {@code pvz:blue_heat}）。是否响应、变成什么，完全由本表决定。
 * 这样火炬树桩不需要知道豌豆的枚举，其他弹药也不会被无关触发器改状态。
 */
public final class VariantTable {

    private final Map<Integer, BulletKind.VariantDef> variants;
    private final Map<BulletKind.TransitionKey, Integer> transitions;

    public VariantTable(Collection<BulletKind.VariantDef> variants,
                        Map<BulletKind.TransitionKey, Integer> transitions) {
        Objects.requireNonNull(variants, "variants");
        Objects.requireNonNull(transitions, "transitions");

        final Map<Integer, BulletKind.VariantDef> byId = new HashMap<>();
        for (BulletKind.VariantDef variant : variants) {
            if (byId.put(variant.id(), variant) != null) {
                throw new IllegalArgumentException("duplicate variant id " + variant.id());
            }
        }
        if (!byId.containsKey(0)) {
            throw new IllegalArgumentException("variant 0 must exist");
        }

        final Map<BulletKind.TransitionKey, Integer> checked = new HashMap<>(transitions);
        for (Map.Entry<BulletKind.TransitionKey, Integer> entry : checked.entrySet()) {
            if (!byId.containsKey(entry.getValue())) {
                throw new IllegalArgumentException("transition target variant "
                        + entry.getValue() + " does not exist");
            }
        }

        this.variants = Map.copyOf(byId);
        this.transitions = Map.copyOf(checked);
    }

    public BulletKind.VariantDef get(int id) {
        final BulletKind.VariantDef variant = this.variants.get(id);
        return variant != null ? variant : this.variants.get(0);
    }

    /** 当前变体在触发器作用下的新 id；未登记则返回原值。 */
    public int next(int current, Identifier trigger) {
        final Integer next = this.transitions.get(new BulletKind.TransitionKey(current, trigger));
        return next != null ? next : current;
    }

    public Collection<BulletKind.VariantDef> all() {
        return this.variants.values();
    }

    public boolean has(int id) {
        return this.variants.containsKey(id);
    }
}
