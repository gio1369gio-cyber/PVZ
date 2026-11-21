package com.lying.pvz.common.impl;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.registry.PVZRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 主模组侧的 registry 创建与 {@link DeferredRegister} 注册入口。
 *
 * <p><b>为什么需要 6 个唯一的 {@link DeferredRegister} 实例？</b>
 * NeoForge 的 {@code DeferredRegister.makeRegistry(...)} 要求"一个 registry 只能由一个实例创建"。
 * 因此主模组在此持有 6 个"注册表容器"实例；附属模组通过
 * {@link com.lying.pvz.api.registry.PVZRegistration} 创建的 {@code DeferredRegister}
 * 只填值、不建 registry，与这里互不冲突。
 *
 * <p><b>何时调用</b>：仅在 mod 构造器中调用一次 {@link #init(IEventBus)}。
 * 必须在 {@code NewRegistryEvent} 触发之前完成，即构造器内同步执行。
 */
public final class PVZRegistryBootstrap {

    public static final DeferredRegister<IPlantType> PLANTS =
            DeferredRegister.create(PVZRegistries.PLANT, PvZMod.MOD_ID);

    public static final DeferredRegister<IZombieType> ZOMBIES =
            DeferredRegister.create(PVZRegistries.ZOMBIE, PvZMod.MOD_ID);

    public static final DeferredRegister<ISkillType> SKILLS =
            DeferredRegister.create(PVZRegistries.SKILL, PvZMod.MOD_ID);

    public static final DeferredRegister<ICoolDown> COOL_DOWNS =
            DeferredRegister.create(PVZRegistries.COOL_DOWN, PvZMod.MOD_ID);

    public static final DeferredRegister<IRankType> RANKS =
            DeferredRegister.create(PVZRegistries.RANK, PvZMod.MOD_ID);

    public static final DeferredRegister<IEssenceType> ESSENCES =
            DeferredRegister.create(PVZRegistries.ESSENCE, PvZMod.MOD_ID);

    private PVZRegistryBootstrap() {
    }

    /**
     * 创建 registry 并把 {@link DeferredRegister} 挂到 mod bus。
     *
     * <p>调用顺序很重要：
     * <ol>
     *   <li>{@code makeRegistry(...)} —— 必须在 {@code NewRegistryEvent} 之前，否则抛
     *       {@code IllegalStateException("Cannot create a registry after NewRegistryEvent was fired")}</li>
     *   <li>{@code register(modBus)} —— 之后 {@code NewRegistryEvent} 触发时把 registry 挂到 root，
     *       {@code RegisterEvent} 触发时把 deferred 条目填进去</li>
     * </ol>
     */
    public static void init(IEventBus modBus) {

        PLANTS.makeRegistry(builder -> {});
        ZOMBIES.makeRegistry(builder -> {});
        SKILLS.makeRegistry(builder -> {});
        COOL_DOWNS.makeRegistry(builder -> {});
        RANKS.makeRegistry(builder -> {});
        ESSENCES.makeRegistry(builder -> {});


        PLANTS.register(modBus);
        ZOMBIES.register(modBus);
        SKILLS.register(modBus);
        COOL_DOWNS.register(modBus);
        RANKS.register(modBus);
        ESSENCES.register(modBus);
    }
}