package com.lying.pvz.common.level.item.card;

import com.lying.pvz.api.card.CardContent;
import com.lying.pvz.api.card.PvZCardComponents;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.registry.PVZRegistries;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.menu.ImitaterMenu;
import com.lying.pvz.common.misc.PvZEnumProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 模仿者卡。final：模仿者语义固定，不允许第三方继承。
 *
 * <p>体验变体（{@code enjoy == true}）仅作为合成材料，不参与使用流程。
 */
public final class ImitaterCardItem extends AbstractCardItem {

    /** 复用同一个 MenuProvider，避免每次右键创建匿名对象。 */
    private static final MenuProvider MENU_PROVIDER = new MenuProvider() {
        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
            return new ImitaterMenu(id, p);
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("gui.pvz.imitater.show");
        }
    };

    public ImitaterCardItem(Identifier id, boolean enjoy) {
        super(defaultProperties(id, PVZPlants.IMITATER, enjoy), PVZPlants.IMITATER, enjoy);
    }

    private static Properties defaultProperties(Identifier id,
                                                DeferredHolder<IPlantType, IPlantType> type,
                                                boolean enjoy) {
        // DeferredHolder 构造时已持有 ResourceKey，key() 不触发 bind，安全。
        final ResourceKey<IPlantType> plantKey = type.key();
        final Identifier plantId = plantKey.identifier();

        return new Properties()
                .stacksTo(enjoy ? 16 : 1)
                .setId(ResourceKey.create(Registries.ITEM, id))
                .delayedComponent(DataComponents.RARITY, ctx ->
                        PvZEnumProxy.getRarityByRank(
                                ctx.lookupOrThrow(PVZRegistries.PLANT)
                                        .getOrThrow(plantKey)
                                        .value()
                                        .getRank()))
                .component(PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY)
                // 注意：不再把 IMITATED_CARD 的默认值设为 ItemContainerContents.EMPTY。
                // 组件的"存在"即语义上代表"已填装内层卡"，空卡不应携带该组件；
                // 否则 has_component 条件模型对空卡恒为 true，会走 special 渲染分支并不可见。
                .component(DataComponents.USE_COOLDOWN,
                        new UseCooldown(0.05f, Optional.of(plantId)));
    }

    @Override
    public CardContent resolveContent(ItemStack stack) {
        final ItemStack inner = getImitatedCard(stack);
        if (!isValidContent(inner)) {
            return CardContent.EMPTY;
        }
        return CardContent.of(getType(), inner);
    }

    @Override
    public List<IPAZType> getLockCheckTypes(ItemStack stack) {
        final CardContent content = this.content(stack);
        if (content.isEmpty()) {
            return List.of(getType());
        }
        final IPAZType effectiveType = content.effectiveStack().getItem() instanceof AbstractCardItem inner
                ? inner.getType()
                : getType();
        return List.of(getType(), effectiveType);
    }

    /* ===== 内部卡内容 ===== */

    public static boolean isValidContent(ItemStack inner) {
        return inner.getItem() instanceof PlantCardItem card && !card.isEnjoy();
    }

    public static ItemStack getImitatedCard(ItemStack stack) {
        if (!(stack.getItem() instanceof ImitaterCardItem)) {
            return ItemStack.EMPTY;
        }
        // 组件不存在时 getOrDefault 返回 EMPTY，nonEmptyItemCopyStream 为空流，最终 orElse(EMPTY)。
        return stack.getOrDefault(PvZCardComponents.IMITATED_CARD.get(), ItemContainerContents.EMPTY)
                .nonEmptyItemCopyStream().findFirst().orElse(ItemStack.EMPTY);
    }

    public static void setImitatedCard(ItemStack stack, ItemStack inner) {
        if (!(stack.getItem() instanceof ImitaterCardItem)) {
            return;
        }
        // 空卡必须走 remove 而不是 set(EMPTY)。
        // PatchedDataComponentMap.set(type, EMPTY) 会在 prototype 无默认值时把 EMPTY 写入 patch，
        // 让 has() 变回 true，重新踩回"空卡不可见"的坑；
        // 而 remove() 在 prototype 无默认值时只从 patch 里删掉，最终 stack 完全干净。
        if (inner.isEmpty()) {
            stack.remove(PvZCardComponents.IMITATED_CARD.get());
        } else {
            stack.set(PvZCardComponents.IMITATED_CARD.get(),
                    ItemContainerContents.fromItems(List.of(inner)));
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (this.enjoy) {
            return InteractionResult.FAIL;
        }
        if (hand == InteractionHand.OFF_HAND) {
            if (!level.isClientSide()) {
                this.openImitaterMenu(player);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this.enjoy) {
            return InteractionResult.FAIL;
        }
        if (context.getHand() == InteractionHand.OFF_HAND) {
            if (!context.getLevel().isClientSide()) {
                this.openImitaterMenu(context.getPlayer());
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private void openImitaterMenu(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(MENU_PROVIDER);
        }
    }

    @Override
    protected void appendContentTooltip(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                        Consumer<Component> builder, TooltipFlag flag) {
        if (this.enjoy) {
            return;
        }
        final CardContent content = this.content(stack);
        if (content.isEmpty()) {
            builder.accept(Component.translatable("tooltip.pvz.imitater_card.empty")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        final ItemStack inner = content.effectiveStack();
        if (inner.getItem() instanceof PlantCardItem innerCard) {
            builder.accept(Component.translatable("tooltip.pvz.imitater_card.full",
                    innerCard.getType().getText().getString()).withStyle(ChatFormatting.LIGHT_PURPLE));
            innerCard.appendHoverText(inner, context, display, builder, flag);
        }
    }
}