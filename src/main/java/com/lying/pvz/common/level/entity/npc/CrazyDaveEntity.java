package com.lying.pvz.common.level.entity.npc;


import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.common.menu.shop.DaveShopMenu;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

public class CrazyDaveEntity extends AbstractDaveEntity {

	public CrazyDaveEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.restoreTransaction(StringUtil.id("crazy_dave"));
	}

	@Override
	protected void openContainer(ServerPlayer player) {
		player.openMenu(new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return Component.translatable("menu.pvz.crazy_dave");
			}

			@Override
			public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
				return new DaveShopMenu(id, playerEntity, CrazyDaveEntity.this.getId());
			}
		},  buffer -> buffer.writeInt(CrazyDaveEntity.this.getId()));
	}


	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return EntityDimensions.scalable(0.9f, 2.6f);
	}


	@Override
	public GroupType getEntityGroupType() {
		return GroupType.PLANTS;
	}
}