package com.lying.pvz.common.menu.shop;

import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class DaveShopMenu extends AbstractDaveShopMenu {

	public DaveShopMenu(int id, Player player, int entityId) {
		super(PvZMenuRegister.DAVE_SHOP.get(), id, player, entityId);
	}

	@Override
	protected Resources getCurrency() { return Resources.MONEY; }

	@Override
	protected @Nullable SoundEvent getPurchaseSound() {
		return PvZSoundRegister.DAVE_HAPPY.get();
	}
}