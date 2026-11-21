package com.lying.pvz.common.menu;

import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AlmanacMenu extends AbstractOptionMenu {

	private final Player player;

	public AlmanacMenu(int id, Player player) {
		super(PvZMenuRegister.ALMANAC.get(), id);
		this.player = player;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		// AlmanacMenu 当前没有任何槽位，快捷移动无意义。
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		// 两端一致：主手或副手任一为图鉴即可，避免主手切换后界面滞留
		return playerIn.getMainHandItem().getItem() == PvZItemRegister.ALMANAC.get()
				|| playerIn.getOffhandItem().getItem() == PvZItemRegister.ALMANAC.get();
	}

	@Override
	public boolean isCraftSlot(Slot slot) {
		return false;
	}
}