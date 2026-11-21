package com.lying.pvz.common.menu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public abstract class AbstractOptionMenu extends AbstractContainerMenu {

	public AbstractOptionMenu(MenuType<?> type, int id) {
		super(type, id);
	}

	/**
	 * 判断给定槽位是否属于本菜单的功能/制作槽位。
	 * 用于在 quickMoveStack 等逻辑中区分数据槽与玩家背包槽。
	 */
	public abstract boolean isCraftSlot(Slot slot);
}