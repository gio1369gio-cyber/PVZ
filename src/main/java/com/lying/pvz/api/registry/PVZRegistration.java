package com.lying.pvz.api.registry;

import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 附属模组注册 PVZ 类型到主模组自定义 {@link net.minecraft.core.Registry} 的工厂方法。
 *
 * <p><b>用法</b>：
 * <pre>{@code
 * // 声明（在附属模组自己的类里）：
 * private static final DeferredRegister<IPlantType> PLANTS =
 *         PVZRegistration.plants("mymod");
 *
 * public static final DeferredHolder<IPlantType, IPlantType> MY_PLANT =
 *         PLANTS.register("my_plant", () -> PlantTypeBuilder.of("mymod:my_plant")
 *                 .sunCost(100).requiredLevel(5)
 *                 .build());
 *
 * // 在 mod 构造中：
 * PLANTS.register(modBus);
 * }</pre>
 *
 * <p>本工厂只提供 {@link DeferredRegister}，<b>不负责创建 registry</b>。
 * registry 由主模组在 {@code NewRegistryEvent} 阶段创建一次。
 */
public final class PVZRegistration {

    private PVZRegistration() {
    }

    public static DeferredRegister<IPlantType> plants(String namespace) {
        return DeferredRegister.create(PVZRegistries.PLANT, namespace);
    }

    public static DeferredRegister<IZombieType> zombies(String namespace) {
        return DeferredRegister.create(PVZRegistries.ZOMBIE, namespace);
    }

    public static DeferredRegister<ISkillType> skills(String namespace) {
        return DeferredRegister.create(PVZRegistries.SKILL, namespace);
    }

    public static DeferredRegister<ICoolDown> coolDowns(String namespace) {
        return DeferredRegister.create(PVZRegistries.COOL_DOWN, namespace);
    }

    public static DeferredRegister<IRankType> ranks(String namespace) {
        return DeferredRegister.create(PVZRegistries.RANK, namespace);
    }

    public static DeferredRegister<IEssenceType> essences(String namespace) {
        return DeferredRegister.create(PVZRegistries.ESSENCE, namespace);
    }
}