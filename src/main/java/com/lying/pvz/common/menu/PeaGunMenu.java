package com.lying.pvz.common.menu;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.level.item.tool.plant.PeaGunItem;
import com.lying.pvz.common.tag.PvZItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PeaGunMenu extends AbstractContainerMenu {

	private static final int MODE_SLOT = 0;
	private static final int GUN_SLOT_END = PeaGunItem.PEA_GUN_SLOT_NUM;      // 28
	private static final int PLAYER_INV_START = GUN_SLOT_END;                // 28
	private static final int PLAYER_INV_END = PLAYER_INV_START + 27;         // 55
	private static final int HOTBAR_START = PLAYER_INV_END;                  // 55
	private static final int HOTBAR_END = HOTBAR_START + 9;                  // 64

	private final SimpleContainer backpack;
	private final Player player;
	private final ItemStack stack;

	public PeaGunMenu(int id, Player player) {
		super(PvZMenuRegister.PEA_GUN.get(), id);
		this.player = player;
		this.stack = this.player.getOffhandItem();

		if (this.stack.getItem() != PvZItemRegister.PEA_GUN.get()) {
			PvZMod.LOGGER.warn("PeaGunMenu: offhand is not a pea gun, opening an empty container (will close on next tick)");
			this.backpack = new SimpleContainer(PeaGunItem.PEA_GUN_SLOT_NUM);
		} else {
			this.backpack = PeaGunItem.getContainer(this.stack);
		}

		/* mode slot: 0 */
		this.addSlot(new Slot(this.backpack, 0, 80, 21) {
			@Override
			public boolean mayPlace(ItemStack s) {
				return PeaGunItem.isValidMode(s);
			}
		});

		/* bullet slots: 1-27 */
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(this.backpack, j + i * 9 + 1, 8 + 18 * j, 45 + 18 * i) {
					@Override
					public boolean mayPlace(ItemStack s) {
						return s.is(PvZItemTags.PEA_GUN_BULLETS);
					}
				});
			}
		}

		this.addStandardInventorySlots(player.getInventory(), 8, 105);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot slot = this.slots.get(index);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

		ItemStack rawStack = slot.getItem();
		ItemStack quickMovedStack = rawStack.copy();

		if (index == MODE_SLOT) {
			// 模式槽 -> 玩家背包/热键栏（倒序，优先热键栏）
			if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (index < GUN_SLOT_END) {
			// 子弹槽 -> 玩家背包/热键栏
			if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (index < PLAYER_INV_END) {
			// 玩家背包 -> 枪内槽，失败即停（不再往热键栏挪）
			if (!this.moveItemStackTo(rawStack, 0, GUN_SLOT_END, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			// 热键栏 -> 枪内槽 + 玩家背包
			if (!this.moveItemStackTo(rawStack, 0, PLAYER_INV_END, false)) {
				return ItemStack.EMPTY;
			}
		}

		if (rawStack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}
		slot.onTake(playerIn, rawStack);

		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		if (playerIn.level().isClientSide()) {
			return true;
		}
		return playerIn.getOffhandItem().getItem() == PvZItemRegister.PEA_GUN.get();
	}
}