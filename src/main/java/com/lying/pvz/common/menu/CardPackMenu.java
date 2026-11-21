package com.lying.pvz.common.menu;

import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.level.item.tool.plant.CardPackItem;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * 卡包菜单。
 *
 * <p><b>关键约束</b>：卡包内容存在<b>主手物品的 {@code CARD_CONTENTS} 组件</b>里，而主手物品在
 * 客户端会因服务端槽位同步被<b>替换实例</b>。因此本菜单<b>不缓存</b> {@code ItemStack} 引用，
 * 每次读写都从 {@link Player#getMainHandItem()} 动态获取。
 *
 * <p>{@link SimpleContainer#setChanged()} 覆盖为 {@link net.minecraft.world.entity.player.Inventory#setChanged()}：
 * 主手物品组件修改后必须标记背包 dirty，否则服务端不保存、不广播。
 */
public class CardPackMenu extends AbstractContainerMenu {

	private final Player player;

	public CardPackMenu(int id, Player player) {
		super(PvZMenuRegister.CARD_PACK.get(), id);
		this.player = player;

		final SimpleContainer backpack = new SimpleContainer(CardPackItem.SLOT_NUM) {
			@Override
			public ItemStack getItem(int slot) {
				return CardPackItem.getContentSlot(player.getMainHandItem(), slot);
			}

			@Override
			public void setItem(int slot, ItemStack s) {
				CardPackItem.setContentSlot(player.getMainHandItem(), slot, s);
			}

			@Override
			public ItemStack removeItem(int slot, int amount) {
				final ItemStack current = this.getItem(slot);
				if (current.isEmpty() || amount <= 0) return ItemStack.EMPTY;
				final int take = Math.min(amount, current.getCount());
				final ItemStack result = current.copyWithCount(take);
				this.setItem(slot, take >= current.getCount()
						? ItemStack.EMPTY
						: current.copyWithCount(current.getCount() - take));
				this.setChanged();
				return result;
			}

			@Override
			public ItemStack removeItemNoUpdate(int slot) {
				final ItemStack current = this.getItem(slot);
				this.setItem(slot, ItemStack.EMPTY);
				this.setChanged();
				return current;
			}

			@Override
			public boolean canPlaceItem(int slot, ItemStack s) {
				return CardPackItem.isValidItemStack(s);
			}

			@Override
			public void setChanged() {
				player.getInventory().setChanged();
			}
		};

		// 4x9 卡包槽位
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(backpack, j + i * 9, 19 + 18 * j, 53 + 18 * i) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						return CardPackItem.isValidItemStack(stack);
					}
				});
			}
		}

		this.addStandardInventorySlots(player.getInventory(), 19, 139);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot quickMovedSlot = this.slots.get(index);
		if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
			ItemStack rawStack = quickMovedSlot.getItem();
			quickMovedStack = rawStack.copy();

			if (index < CardPackItem.SLOT_NUM) {
				// 卡包 -> 玩家背包/热键栏
				if (!this.moveItemStackTo(rawStack, CardPackItem.SLOT_NUM, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (index < CardPackItem.SLOT_NUM + 27) {
				// 玩家背包 -> 卡包，失败再进热键栏
				if (!this.moveItemStackTo(rawStack, 0, CardPackItem.SLOT_NUM, false)
						&& !this.moveItemStackTo(rawStack, CardPackItem.SLOT_NUM + 27, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				// 热键栏 -> 卡包/玩家背包
				if (!this.moveItemStackTo(rawStack, 0, CardPackItem.SLOT_NUM + 27, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (rawStack.isEmpty()) {
				quickMovedSlot.setByPlayer(ItemStack.EMPTY);
			} else {
				quickMovedSlot.setChanged();
			}
			quickMovedSlot.onTake(playerIn, rawStack);
		}
		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return playerIn.getMainHandItem().getItem() == PvZItemRegister.CARD_PACK.get();
	}
}