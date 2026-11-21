package com.lying.pvz.compat.modonomicon;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.neoforged.fml.ModList;

/**
 * Modonomicon 软依赖兼容层。
 *
 * <p>Modonomicon 是可选依赖（软依赖）：未安装时本书所有内容静默失效，
 * 不触发类加载异常。所有对 Modonomicon 类的引用都必须收拢在
 * {@link #getGuideBook(Identifier)} 内部，外部通过 {@link #isLoaded()} 判断后再调用。
 *
 * <p>书籍内容本身由 datagen 生成（见 {@code compat.modonomicon.datagen} 包），
 * 运行时只需在玩家首次进入时发放一本"PVZ 指南"即可。
 */
public final class ModonomiconCompat {

    /** Modonomicon 的 mod id。 */
    public static final String MOD_ID = "modonomicon";

    /** PVZ 指南书的 book id。 */
    public static final Identifier GUIDE_BOOK_ID = Identifier.fromNamespaceAndPath("pvz", "pvz_guide");

    private ModonomiconCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    /**
     * 创建一本指定 book id 的 Modonomicon 书。
     * 仅在 {@link #isLoaded()} 为 true 时调用。
     */
    public static ItemStack getGuideBook(Identifier bookId) {
        // 延迟到运行时再解析 Modonomicon 的类，避免未安装时类加载报错。
        ItemStack stack = new ItemStack(com.klikli_dev.modonomicon.registry.ItemRegistry.MODONOMICON.get());
        stack.set(com.klikli_dev.modonomicon.registry.DataComponentRegistry.BOOK_ID.get(), bookId);
        return stack;
    }

    /**
     * 向玩家发放 PVZ 指南书（Modonomicon 已加载时）。
     */
    public static void giveGuideBook(Player player) {
        if (!isLoaded()) {
            return;
        }
        player.addItem(getGuideBook(GUIDE_BOOK_ID));
    }

    /**
     * 返回指南书合成配方产物模板（{@link ItemStackTemplate}），用于 datagen 生成合成配方。
     * <p>Modonomicon 是软依赖，所有对 Modonomicon 类（{@code ItemRegistry} /
     * {@code DataComponentRegistry}）的引用必须收拢在此处，并通过 {@link #isLoaded()}
     * 守卫；未安装时返回 {@code null}，调用方应跳过该配方。
     *
     * @return 带 BOOK_ID 数据组件的 Modonomicon 书模板；未安装 Modonomicon 时返回 {@code null}
     */
    public static ItemStackTemplate getGuideBookTemplate() {
        if (!isLoaded()) {
            return null;
        }
        // 延迟到运行时再解析 Modonomicon 的类，避免未安装时类加载报错。
        Item bookItem = com.klikli_dev.modonomicon.registry.ItemRegistry.MODONOMICON.get();
        DataComponentPatch patch = DataComponentPatch.builder()
                .set(com.klikli_dev.modonomicon.registry.DataComponentRegistry.BOOK_ID.get(), GUIDE_BOOK_ID)
                .build();
        return new ItemStackTemplate(bookItem, patch);
    }
}
