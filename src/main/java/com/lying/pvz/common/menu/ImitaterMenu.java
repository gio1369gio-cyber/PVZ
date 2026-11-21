package com.lying.pvz.common.menu;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.level.item.card.ImitaterCardItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ImitaterMenu extends AbstractContainerMenu {

	private static final int IMITATER_SLOT = 0;
	private static final int DATA_SLOT_END = 1;
	private static final int PLAYER_INV_START = 1;
	private static final int PLAYER_INV_END = PLAYER_INV_START + 27;  // 28
	private static final int HOTBAR_START = PLAYER_INV_END;           // 28
	private static final int HOTBAR_END = HOTBAR_START + 9;           // 37

	private final Container backpack;
	private final ItemStack stack;

	public ImitaterMenu(int id, Player player) {
		super(PvZMenuRegister.IMITATER.get(), id);
		this.stack = player.getOffhandItem();

		// 正常打开路径由 ImitaterCardItem.use/useOn 保证副手是正版模仿者卡；
		// 这里只做防御日志，行为由 ImitaterBackpack + 组件 API 的类型守卫兜底。
		if (!(this.stack.getItem() instanceof ImitaterCardItem card) || card.isEnjoy()) {
			PvZMod.LOGGER.warn("ImitaterMenu: offhand is not a usable imitater card, opening an empty container");
		}

		this.backpack = new ImitaterBackpack(this.stack);

		this.addSlot(new Slot(this.backpack, 0, 80, 20) {
			@Override
			public boolean mayPlace(ItemStack s) {
				return ImitaterCardItem.isValidContent(s);
			}
		});

		this.addStandardInventorySlots(player.getInventory(), 8, 51);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot slot = this.slots.get(index);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

		// getItem 返回副本；moveItemStackTo 会修改该副本，因此源槽必须显式写回。
		ItemStack rawStack = slot.getItem();
		ItemStack quickMovedStack = rawStack.copy();

		if (index == IMITATER_SLOT) {
			// 模仿者槽 -> 玩家背包/热键栏（倒序，优先热键栏）
			if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (index < PLAYER_INV_END) {
			// 玩家背包 -> 模仿者槽，失败即停
			if (!this.moveItemStackTo(rawStack, 0, DATA_SLOT_END, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			// 热键栏 -> 模仿者槽 + 玩家背包
			if (!this.moveItemStackTo(rawStack, 0, PLAYER_INV_END, false)) {
				return ItemStack.EMPTY;
			}
		}

		// 无论是否清空都要写回，处理"部分移动"的剩余量。
		if (rawStack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setByPlayer(rawStack);
		}
		slot.onTake(playerIn, rawStack);

		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		if (playerIn.level().isClientSide()) {
			return true;
		}
		return playerIn.getOffhandItem().getItem() == PvZItemRegister.IMITATER_CARD.get();
	}

	/**
	 * 只暴露一个槽、全部读写都落到模仿者卡 {@code IMITATED_CARD} 组件上的容器。
	 *
	 * <p>不能用 {@link net.minecraft.world.SimpleContainer} 的匿名子类：它的
	 * {@code removeItem}/{@code removeItemNoUpdate}/{@code isEmpty}/{@code clearContent}
	 * 直接操作内部 {@code items} 列表，绕过覆写的 getItem/setItem，导致左键取出永远拿到空栈。
	 *
	 * <p>非模仿者卡场景（异常路径）由 {@link ImitaterCardItem#getImitatedCard} /
	 * {@link ImitaterCardItem#setImitatedCard} 的类型守卫兜底：读恒为空、写为 no-op。
	 */
	private static final class ImitaterBackpack implements Container {
		private final ItemStack holder;

		ImitaterBackpack(ItemStack holder) {
			this.holder = holder;
		}

		@Override
		public int getContainerSize() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			return ImitaterCardItem.getImitatedCard(this.holder).isEmpty();
		}

		@Override
		public ItemStack getItem(int slot) {
			return slot == 0 ? ImitaterCardItem.getImitatedCard(this.holder) : ItemStack.EMPTY;
		}

		@Override
		public ItemStack removeItem(int slot, int count) {
			if (slot != 0 || count <= 0) return ItemStack.EMPTY;
			// getImitatedCard 返回副本，split 后必须写回组件。
			ItemStack current = ImitaterCardItem.getImitatedCard(this.holder);
			if (current.isEmpty()) return ItemStack.EMPTY;
			ItemStack taken = current.split(count);
			ImitaterCardItem.setImitatedCard(this.holder, current);
			return taken;
		}

		@Override
		public ItemStack removeItemNoUpdate(int slot) {
			if (slot != 0) return ItemStack.EMPTY;
			ItemStack current = ImitaterCardItem.getImitatedCard(this.holder);
			if (!current.isEmpty()) {
				ImitaterCardItem.setImitatedCard(this.holder, ItemStack.EMPTY);
			}
			return current;
		}

		@Override
		public void setItem(int slot, ItemStack s) {
			if (slot == 0) {
				ImitaterCardItem.setImitatedCard(this.holder, s);
			}
		}

		@Override
		public void setChanged() {
			// 组件写入即时生效，无需额外通知。
		}

		@Override
		public boolean stillValid(Player player) {
			return true;
		}

		@Override
		public void clearContent() {
			ImitaterCardItem.setImitatedCard(this.holder, ItemStack.EMPTY);
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}
}