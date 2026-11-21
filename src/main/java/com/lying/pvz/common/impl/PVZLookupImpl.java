package com.lying.pvz.common.impl;

import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.lookup.IPVZLookup;
import com.lying.pvz.api.registry.PVZRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link IPVZLookup} 的默认实现。
 *
 * <p><b>惰性解析</b>：每次首次访问某个 registry 时，从 {@code BuiltInRegistries.REGISTRY}
 * 按 {@link ResourceKey} 取到 {@link Registry} 并缓存。这样既避免在 bootstrap 阶段
 * 访问尚未就绪的 registry，也避免反复访问 root registry。
 *
 * <p><b>线程安全</b>：缓存用 {@link ConcurrentHashMap}；{@code FMLCommonSetupEvent} 是并行的，
 * 首次访问可能来自多个线程。
 *
 * <p><b>生命周期</b>：本实例由 {@code PVZAPI.bootstrapLookup(...)} 注入；
 * 必须在 {@code FMLCommonSetupEvent} 之后才可能被调用。
 */
public final class PVZLookupImpl implements IPVZLookup {

    private final Map<ResourceKey<?>, Registry<?>> cache = new ConcurrentHashMap<>();

    public PVZLookupImpl() {
    }

    /* ------------------------------------------------------------------ */
    /* 内部工具                                                            */
    /* ------------------------------------------------------------------ */

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> Registry<T> registry(ResourceKey<Registry<T>> key) {
        return (Registry<T>) this.cache.computeIfAbsent(key, k -> {
            // BuiltInRegistries.REGISTRY 是 Registry<Registry<?>>；用 raw 绕过泛型。
            // 若本地 getOptional 签名不是 ResourceKey，可改为 getValueOrThrow(ResourceKey)。
            final Optional<Registry<?>> raw =
                    (Optional<Registry<?>>) (Optional) BuiltInRegistries.REGISTRY.getOptional((ResourceKey) k);
            if (raw.isEmpty()) {
                throw new IllegalStateException("PVZ registry not found: " + k.identifier());
            }
            return raw.get();
        });
    }

    private <T> Optional<T> get(ResourceKey<Registry<T>> key, Identifier id) {
        final ResourceKey<T> resourceKey = ResourceKey.create(key, id);
        // get(ResourceKey) 返回 Optional<Holder.Reference<T>>；.map(h -> h.value()) 拿到 T。
        return this.registry(key).get(resourceKey).map(Holder::value);
    }

    private <T> Collection<T> all(ResourceKey<Registry<T>> key) {
        final Registry<T> registry = this.registry(key);
        final ArrayList<T> out = new ArrayList<>();
        for (T value : registry) {
            out.add(value);
        }
        return Collections.unmodifiableList(out);
    }

    /* ------------------------------------------------------------------ */
    /* PAZ                                                                 */
    /* ------------------------------------------------------------------ */

    @Override
    public Optional<IPAZType> paz(Identifier id) {
        final Optional<IPlantType> plant = this.plant(id);
        if (plant.isPresent()) {
            return Optional.of(plant.get());
        }
        return this.zombie(id).map(z -> z);
    }

    @Override
    public Optional<IPlantType> plant(Identifier id) {
        return this.get(PVZRegistries.PLANT, id);
    }

    @Override
    public Optional<IZombieType> zombie(Identifier id) {
        return this.get(PVZRegistries.ZOMBIE, id);
    }

    @Override
    public Collection<IPAZType> pazs() {
        final ArrayList<IPAZType> out = new ArrayList<>();
        out.addAll(this.plants());
        out.addAll(this.zombies());
        return Collections.unmodifiableList(out);
    }

    @Override
    public Collection<IPlantType> plants() {
        return this.all(PVZRegistries.PLANT);
    }

    @Override
    public Collection<IZombieType> zombies() {
        return this.all(PVZRegistries.ZOMBIE);
    }

    /* ------------------------------------------------------------------ */
    /* 技能 / 冷却 / 等级 / 精华                                            */
    /* ------------------------------------------------------------------ */

    @Override
    public Optional<ISkillType> skill(Identifier id) {
        return this.get(PVZRegistries.SKILL, id);
    }

    @Override
    public Optional<ICoolDown> coolDown(Identifier id) {
        return this.get(PVZRegistries.COOL_DOWN, id);
    }

    @Override
    public Optional<IRankType> rank(Identifier id) {
        return this.get(PVZRegistries.RANK, id);
    }

    @Override
    public Optional<IEssenceType> essence(Identifier id) {
        return this.get(PVZRegistries.ESSENCE, id);
    }

    @Override
    public Collection<ISkillType> skills() {
        return this.all(PVZRegistries.SKILL);
    }

    @Override
    public Collection<ICoolDown> coolDowns() {
        return this.all(PVZRegistries.COOL_DOWN);
    }

    @Override
    public Collection<IRankType> ranks() {
        return this.all(PVZRegistries.RANK);
    }

    @Override
    public Collection<IEssenceType> essences() {
        return this.all(PVZRegistries.ESSENCE);
    }
}