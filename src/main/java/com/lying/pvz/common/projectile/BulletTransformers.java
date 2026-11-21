package com.lying.pvz.common.projectile;

import com.lying.pvz.api.projectile.BulletTransformer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 飞行触发器注册表。
 *
 * <p>注册单位是"provider 谓词 + 变压器"：provider 通常是某个植物实体，
 * 谓词可以直接查 entity type / entity tag。Phase 5 会在这里注册
 * 火炬树桩与蓝火树桩。
 */
public final class BulletTransformers {

    record Entry(Predicate<Entity> provider, BulletTransformer transformer) {
    }

    private static final List<Entry> ENTRIES = new ArrayList<>();

    private BulletTransformers() {
    }

    public static void register(Predicate<Entity> provider, BulletTransformer transformer) {
        Objects.requireNonNull(provider, "provider");
        Objects.requireNonNull(transformer, "transformer");
        ENTRIES.add(new Entry(provider, transformer));
    }

    static boolean isEmpty() {
        return ENTRIES.isEmpty();
    }

    static List<BulletTransformer> forProvider(Entity provider) {
        if (ENTRIES.isEmpty()) {
            return List.of();
        }
        final List<BulletTransformer> result = new ArrayList<>();
        for (Entry entry : ENTRIES) {
            if (entry.provider().test(provider)) {
                result.add(entry.transformer());
            }
        }
        return result.isEmpty() ? List.of() : Collections.unmodifiableList(result);
    }
}
