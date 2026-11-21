package com.lying.pvz.common.projectile.mark;

import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 标记注册表。
 *
 * <p>标记 id 是全局键；层数按 {@link Mark.Key#source()} 分开。
 * 新增一种标记只需要在这里登记一条 {@link Mark.Spec}。
 */
public final class MarkRegistry {

    public static final Identifier BUNGEE_TARGET = StringUtil.id("bungee_target");
    public static final Identifier BUNGEE_TARGET_VISUAL = StringUtil.id("mark/bungee_target");

    private static final Map<Identifier, Mark.Spec> SPECS = new HashMap<>();

    static {
        register(new Mark.Spec(
                BUNGEE_TARGET, 1, 200, true,
                Mark.Scope.PER_SOURCE, BUNGEE_TARGET_VISUAL));
    }

    private MarkRegistry() {
    }

    public static void register(Mark.Spec spec) {
        Objects.requireNonNull(spec, "spec");
        if (SPECS.putIfAbsent(spec.mark(), spec) != null) {
            throw new IllegalStateException("Duplicate mark spec " + spec.mark());
        }
    }

    public static @Nullable Mark.Spec get(@Nullable Identifier mark) {
        return mark == null ? null : SPECS.get(mark);
    }
}
