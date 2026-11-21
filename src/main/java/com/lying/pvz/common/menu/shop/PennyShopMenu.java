package com.lying.pvz.common.menu.shop;

import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.world.entity.player.Player;

public class PennyShopMenu extends AbstractDaveShopMenu {

	public PennyShopMenu(int id, Player player, int entityId) {
		super(PvZMenuRegister.PENNY_SHOP.get(), id, player, entityId);
	}

	@Override
	protected Resources getCurrency() { return Resources.GEM_NUM; }

}