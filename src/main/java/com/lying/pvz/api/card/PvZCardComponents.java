package com.lying.pvz.api.card;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.skill.SkillLevels;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

/**
 * 卡片相关的数据组件 key。
 * ...
 */
public final class PvZCardComponents {

    /**
     * 卡片相关组件的 DeferredRegister。
     * 主模组在 mod 构造中调用 {@code COMPONENTS.register(bus)} 完成注册。
     *
     * <p>故意使用通用 {@code DeferredRegister<DataComponentType<?>>} 而非
     * {@code DeferredRegister.DataComponents}：后者的继承类型实参
     * {@code DeferredRegister<DataComponentType<?>>} 声明在未 {@code @NullMarked}
     * 的 NeoForge 中，本类位于 {@code @NullMarked} 包内会把它重解读为
     * {@code ? extends @NonNull Object}，导致字段赋值报"为带有可为 null 类型实参的类赋值"。
     * 便利方法 {@code registerComponentType} 在本类末尾等价实现。
     */
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, PvZMod.MOD_ID);

    /**
     * 卡片上附加的技能等级。
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SkillLevels>> CARD_SKILLS =
            registerComponent("card_skills", builder -> builder
                    .persistent(SkillLevels.CODEC)
                    .networkSynchronized(SkillLevels.STREAM_CODEC)
            );

    /**
     * 模仿者卡内部卡的内容槽。
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> IMITATED_CARD =
            registerComponent("imitated_card", builder -> builder
                    .persistent(ItemContainerContents.CODEC)
                    .networkSynchronized(ItemContainerContents.STREAM_CODEC)
            );

    /**
     * 等价于 {@code DeferredRegister.DataComponents#registerComponentType}。
     */
    private static <D> DeferredHolder<DataComponentType<?>, DataComponentType<D>> registerComponent(
            String name, UnaryOperator<DataComponentType.Builder<D>> builder) {
        return COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    private PvZCardComponents() {
    }
}