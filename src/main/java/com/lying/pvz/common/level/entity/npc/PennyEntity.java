package com.lying.pvz.common.level.entity.npc;

import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.item.tool.TimeKeyTeleport;
import com.lying.pvz.common.menu.shop.PennyShopMenu;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
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
	public InteractionResult interact(Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.Vec3 location) {
		// 手持时空之钥：触发群系传送（优先于商店）
		if (hand == net.minecraft.world.InteractionHand.MAIN_HAND
				&& player.getItemInHand(hand).getItem().equals(PvZItemRegister.TIME_KEY_TO_DEEP.get())
				&& !this.level().isClientSide() && player instanceof ServerPlayer sp) {
			TimeKeyTeleport.onKeyUsed(sp, (ServerLevel) this.level());
			return InteractionResult.SUCCESS;
		}
		return super.interact(player, hand, location);
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