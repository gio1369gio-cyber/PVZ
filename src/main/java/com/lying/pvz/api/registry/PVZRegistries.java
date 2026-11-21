package com.lying.pvz.api.registry;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * PVZ 提供的所有自定义 {@link Registry} 键。
 *
 * <p>附属模组通过 {@link PVZRegistration} 工厂方法创建
 * {@link net.neoforged.neoforge.registries.DeferredRegister}，再 {@code register(bus)} 完成注册。
 *
 * <p>本类常量是 {@link ResourceKey}，可直接用于
 * {@code level.registryAccess().registryOrThrow(key)} 等查询。
 *
 * <p><b>生命周期</b>：registry 由主模组在 {@code NewRegistryEvent} 阶段创建（一次性），
 * 之后才会对 {@code DeferredRegister} / {@code RegisterEvent} 可见。
 */
public final class PVZRegistries {

    public static final String NAMESPACE = PvZMod.MOD_ID;

    public static final ResourceKey<Registry<IPlantType>>  PLANT     = key("plant_type");
    public static final ResourceKey<Registry<IZombieType>> ZOMBIE    = key("zombie_type");
    public static final ResourceKey<Registry<ISkillType>>  SKILL     = key("skill");
    public static final ResourceKey<Registry<ICoolDown>>   COOL_DOWN = key("cool_down");
    public static final ResourceKey<Registry<IRankType>>   RANK      = key("rank");
    public static final ResourceKey<Registry<IEssenceType>> ESSENCE  = key("essence");

    private PVZRegistries() {
    }

    private static <T> ResourceKey<Registry<T>> key(String path) {
        return ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(NAMESPACE, path));
    }
}