package com.lying.pvz.common.menu;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.level.block.special.SunConverterBlock;
import com.lying.pvz.common.level.blockentity.SunConverterBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

public class SunConverterMenu extends AbstractContainerMenu {

	public static final int DATA_SLOT_COUNT = 9;
	public static final int DATA_ARRAY_SIZE = 1;

	private static final int DATA_SLOT_END = DATA_SLOT_COUNT;
	private static final int PLAYER_INV_START = DATA_SLOT_END;
	private static final int PLAYER_INV_END = PLAYER_INV_START + 27;
	private static final int HOTBAR_START = PLAYER_INV_END;
	private static final int HOTBAR_END = HOTBAR_START + 9;

	private final @Nullable SunConverterBlockEntity te;
	private final ContainerLevelAccess access;
	private final ItemStacksResourceHandler handler;
	private final ContainerData data;
	private final Player player;

	/** 客户端构造函数，供 IContainerFactory 使用。 */
	public SunConverterMenu(int id, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
		this(id, playerInv, null, ContainerLevelAccess.NULL,
				new ItemStacksResourceHandler(DATA_SLOT_COUNT),
				new SimpleContainerData(DATA_ARRAY_SIZE));
	}

	/** 服务端构造函数。 */
	public SunConverterMenu(int id, Inventory playerInv, SunConverterBlockEntity te) {
		this(id, playerInv, te,
				ContainerLevelAccess.create(playerInv.player.level(), te.getBlockPos()),
				te.handler, te.array);
	}

	private SunConverterMenu(int id, Inventory playerInv, @Nullable SunConverterBlockEntity te,
							 ContainerLevelAccess access, ItemStacksResourceHandler handler, ContainerData data) {
		super(PvZMenuRegister.SUN_CONVERTER.get(), id);
		this.te = te;
		this.access = access;
		this.handler = handler;
		this.data = data;
		this.player = playerInv.player;

		this.addDataSlots(this.data);

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int idx = i * 3 + j;
				this.addSlot(new ResourceHandlerSlot(
						this.handler, this.handler::set, idx,
						62 + 18 * j, 17 + 18 * i) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						return SunConverterBlock.isValidItem(stack);
					}
				});
			}
		}

		this.addStandardInventorySlots(playerInv, 8, 84);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot slot = this.slots.get(index);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

		ItemStack rawStack = slot.getItem();
		ItemStack quickMovedStack = rawStack.copy();

		if (index < DATA_SLOT_END) {
			// 数据槽 -> 玩家背包/热键栏（倒序优先热键栏，与原版 QUICK_MOVE 一致）
			if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (index < PLAYER_INV_END) {
			// 玩家背包 -> 数据槽，失败即停（不再往热键栏挪）
			if (!this.moveItemStackTo(rawStack, 0, DATA_SLOT_END, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			// 热键栏 -> 数据槽 + 玩家背包
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
		return stillValid(this.access, playerIn, PvZBlockRegister.SUN_CONVERTER.get());
	}
}