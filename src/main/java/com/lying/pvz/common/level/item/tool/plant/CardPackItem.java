package com.lying.pvz.common.level.item.tool.plant;

import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.common.menu.CardPackMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡包（Card Pack）道具——容纳至多 {@value #SLOT_NUM} 张召唤卡。
 *
 * <p><b>存储</b>：内容存于 {@code CARD_CONTENTS} 组件（{@link ItemContainerContents}）。
 * {@link ItemContainerContents} 不可变且会裁剪尾部空槽；读取时越界槽一律返回
 * {@link ItemStack#EMPTY}，外部逻辑始终按 {@link #SLOT_NUM} 个逻辑槽处理。
 *
 * <p><b>使用</b>：主手右键打开 {@link CardPackMenu}。打开逻辑只在服务端执行。
 *
 * <p><b>内容过滤</b>：只允许 {@link AbstractCardItem} 进入（{@link #isValidItemStack}）。
 */
public class CardPackItem extends Item {

	public static final int SLOT_NUM = 36;

	public CardPackItem(Properties properties) {
		super(properties);
	}

	// ============================ 存储访问 ============================

	public static ItemContainerContents getContents(ItemStack host) {
		return host.getOrDefault(PvZDataComponents.CARD_CONTENTS.get(), ItemContainerContents.EMPTY);
	}

	public static ItemStack getContentSlot(ItemStack host, int slot) {
		if (slot < 0 || slot >= SLOT_NUM) return ItemStack.EMPTY;
		final ItemContainerContents contents = getContents(host);
		if (slot >= contents.getSlots()) return ItemStack.EMPTY;
		return contents.getTemplateInSlot(slot)
				.map(ItemStackTemplate::create)
				.orElse(ItemStack.EMPTY);
	}

	public static void setContentSlot(ItemStack host, int slot, ItemStack value) {
		if (slot < 0 || slot >= SLOT_NUM) return;
		final ItemContainerContents contents = getContents(host);
		final int stored = contents.getSlots();

		// 写入空槽且超出已存储范围：无变化，跳过重建（高频操作优化）
		if (value.isEmpty() && slot >= stored) return;

		// 写入内容与当前一致：跳过（避免无意义地触发背包 dirty）
		if (slot < stored) {
			final ItemStack current = contents.getTemplateInSlot(slot)
					.map(ItemStackTemplate::create)
					.orElse(ItemStack.EMPTY);
			if (ItemStack.matches(current, value)) return;
		}

		final List<ItemStack> list = new ArrayList<>(SLOT_NUM);
		for (int i = 0; i < SLOT_NUM; i++) {
			if (i == slot) {
				list.add(value);
			} else if (i < stored) {
				list.add(contents.getTemplateInSlot(i)
						.map(ItemStackTemplate::create)
						.orElse(ItemStack.EMPTY));
			} else {
				list.add(ItemStack.EMPTY);
			}
		}
		host.set(PvZDataComponents.CARD_CONTENTS.get(), ItemContainerContents.fromItems(list));
	}

	// ============================ GUI 视图 ============================


	public static SimpleContainer getContainer(ItemStack host) {
		return new SimpleContainer(SLOT_NUM) {
			@Override
			public ItemStack getItem(int slot) {
				return getContentSlot(host, slot);
			}

			@Override
			public void setItem(int slot, ItemStack s) {
				setContentSlot(host, slot, s);
				this.setChanged();
			}

			@Override
			public ItemStack removeItem(int slot, int amount) {
				final ItemStack current = getContentSlot(host, slot);
				if (current.isEmpty() || amount <= 0) return ItemStack.EMPTY;
				final int take = Math.min(amount, current.getCount());
				final ItemStack result = current.copyWithCount(take);
				setContentSlot(host, slot, take >= current.getCount()
						? ItemStack.EMPTY
						: current.copyWithCount(current.getCount() - take));
				this.setChanged();
				return result;
			}

			@Override
			public ItemStack removeItemNoUpdate(int slot) {
				final ItemStack current = getContentSlot(host, slot);
				setContentSlot(host, slot, ItemStack.EMPTY);
				this.setChanged();
				return current;
			}

			@Override
			public boolean canPlaceItem(int slot, ItemStack s) {
				return isValidItemStack(s);
			}
		};
	}

	// ============================ 物品过滤 ============================

	public static boolean isValidItemStack(ItemStack stack) {
		return stack.getItem() instanceof AbstractCardItem;
	}

	// ============================ 使用逻辑 ============================

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(new MenuProvider() {
				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
					return new CardPackMenu(id, p);
				}

				@Override
				public Component getDisplayName() {
					return Component.translatable("gui.pvz.card_pack.show");
				}
			});
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}
}