package com.lying.pvz.common.level.entity.npc;

import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.menu.shop.PennyShopMenu;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PennyEntity extends AbstractDaveEntity {

	public PennyEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.setPermanentlyInvulnerable(true);
		this.restoreTransaction(StringUtil.id("penny"));
	}

	@Override
	protected boolean canOpenShop(Player player, ItemStack heldItem) {
		return heldItem.getItem().equals(PvZItemRegister.CAR_KEY.get());
	}

	@Override
	protected void openContainer(ServerPlayer player) {
		player.openMenu(new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return Component.translatable("menu.pvz.penny");
			}

			@Override
			public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
				return new PennyShopMenu(id, playerEntity, PennyEntity.this.getId());
			}
		}, buffer -> buffer.writeInt(PennyEntity.this.getId()));
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(1.8f, 2f);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return PvZSoundRegister.PENNY_SAY.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.GENERIC_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.GENERIC_DEATH;
	}
}