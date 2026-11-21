package com.lying.pvz.common.menu;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.init.PvZRecipeRegister;
import com.lying.pvz.common.level.blockentity.FragmentSpliceBlockEntity;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.common.level.item.tool.plant.SunStorageSaplingItem;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FragmentSpliceMenu extends AbstractContainerMenu {

	public static final int SLOT_SUN = 0;
	public static final int SLOT_RESULT = 1;
	public static final int SLOT_INPUT_START = 2;
	public static final int INPUT_COUNT = 25;
	public static final int HANDLER_SIZE = SLOT_INPUT_START + INPUT_COUNT; // 27

	public static final int DATA_ARRAY_SIZE = FragmentSpliceBlockEntity.DATA_ARRAY_SIZE;

	private static final int DATA_SLOT_END = HANDLER_SIZE;              // 27
	private static final int PLAYER_INV_START = DATA_SLOT_END;          // 27
	private static final int PLAYER_INV_END = PLAYER_INV_START + 27;    // 54
	private static final int HOTBAR_START = PLAYER_INV_END;             // 54
	private static final int HOTBAR_END = HOTBAR_START + 9;             // 63

	private final @Nullable FragmentSpliceBlockEntity te;
	private final ContainerLevelAccess access;
	private final ItemStacksResourceHandler handler;
	private final ContainerData data;
	private final Player player;

	/** 客户端构造函数，供 IContainerFactory 使用。 */
	public FragmentSpliceMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
		this(containerId, playerInv, null, ContainerLevelAccess.NULL,
				new ItemStacksResourceHandler(HANDLER_SIZE),
				new SimpleContainerData(DATA_ARRAY_SIZE));
	}

	/** 服务端构造函数。 */
	public FragmentSpliceMenu(int containerId, Inventory playerInv, FragmentSpliceBlockEntity te) {
		this(containerId, playerInv, te,
				ContainerLevelAccess.create(playerInv.player.level(), te.getBlockPos()),
				te.handler, te.array);
	}

	private FragmentSpliceMenu(int containerId, Inventory playerInv, @Nullable FragmentSpliceBlockEntity te,
							   ContainerLevelAccess access, ItemStacksResourceHandler handler, ContainerData data) {
		super(PvZMenuRegister.FRAGMENT_SPLICE.get(), containerId);
		this.te = te;
		this.access = access;
		this.handler = handler;
		this.data = data;
		this.player = playerInv.player;

		this.addDataSlots(this.data);

		// 0: sun provider
		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, SLOT_SUN, 7, 119) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof SunStorageSaplingItem;
			}
		});

		// 1: result（只出不进）
		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, SLOT_RESULT, 187, 62) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack stack) {
				return false;
			}
		});

		// 2..26: 5x5 inputs
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				int idx = SLOT_INPUT_START + j + i * 5;
				this.addSlot(new ResourceHandlerSlot(
						this.handler, this.handler::set, idx,
						61 + j * 18, 26 + i * 18));
			}
		}

		// 玩家背包 + 热键栏
		this.addStandardInventorySlots(playerInv, 25, 143);
	}

	// ------------------------------------------------------------------
	// 合成
	// ------------------------------------------------------------------


	/**
	 * 服务端权威合成。校验全部在服务端完成，客户端伪造包无法绕过。
	 * @return 是否真的执行了合成
	 */
	public boolean onCraft(Player player) {
		if (this.te == null) return false;
		if (player == null) return false;
		if (!player.getUUID().equals(this.player.getUUID())) return false;
		if (this.te.sunAmount != FragmentSpliceBlockEntity.CRAFT_COST) return false;
		if (!this.handler.getResource(SLOT_RESULT).isEmpty()) return false;

		final ItemStack result = this.getResult();
		if (result.isEmpty()) return false;

		this.te.setStack(SLOT_RESULT, result.copy());
		this.te.clearCraftingSlots();
		this.te.sunAmount = 0;
		this.te.array.set(FragmentSpliceBlockEntity.ARRAY_SUN, 0);
		this.te.array.set(FragmentSpliceBlockEntity.ARRAY_CAN_CRAFT, 0);
		this.te.setChanged();

		if (result.getItem() instanceof AbstractCardItem card && card.getType() != null) {
			PlayerUtil.setPAZUnlocked(this.player, card.getType(), false);
		}
		return true;
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id == 0) return this.onCraft(player);
		return false;
	}

	public int getSlotSun() {
		return this.data.get(FragmentSpliceBlockEntity.ARRAY_SUN);
	}

	/**
	 * 仅服务端可返回非空（客户端无配方索引）。
	 * <p>客户端 UI 用 {@link #canCraft()} 的服务端同步标志即可。
	 */
	public ItemStack getResult() {
		final List<ItemStack> items = new ArrayList<>(INPUT_COUNT);
		for (int i = 0; i < INPUT_COUNT; i++) {
			int idx = SLOT_INPUT_START + i;
			items.add(this.handler.getResource(idx).toStack(this.handler.getAmountAsInt(idx)));
		}
		final CraftingInput input = CraftingInput.of(5, 5, items);

		if (this.player.level() instanceof ServerLevel serverLevel) {
			return serverLevel.recipeAccess()
					.getRecipeFor(PvZRecipeRegister.FRAGMENT_RECIPE_TYPE.get(), input, serverLevel)
					.map(RecipeHolder::value)
					.map(recipe -> recipe.assemble(input))
					.orElse(ItemStack.EMPTY);
		}
		return ItemStack.EMPTY;
	}

	/** 服务端权威标志位，客户端读同步值。 */
	public boolean canCraft() {
		return this.data.get(FragmentSpliceBlockEntity.ARRAY_CAN_CRAFT) == 1;
	}

	// ------------------------------------------------------------------
	// 快捷键
	// ------------------------------------------------------------------

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot quickMovedSlot = this.slots.get(index);
		if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
			ItemStack rawStack = quickMovedSlot.getItem();
			quickMovedStack = rawStack.copy();

			if (index < DATA_SLOT_END) {
				if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index < PLAYER_INV_END) {
				if (!this.moveItemStackTo(rawStack, SLOT_INPUT_START, DATA_SLOT_END, false)
						&& !this.moveItemStackTo(rawStack, HOTBAR_START, HOTBAR_END, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.moveItemStackTo(rawStack, SLOT_INPUT_START, PLAYER_INV_END, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (rawStack.isEmpty()) {
				quickMovedSlot.setByPlayer(ItemStack.EMPTY);
			} else {
				quickMovedSlot.setChanged();
			}
			quickMovedSlot.onTake(playerIn, rawStack);
		}
		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(this.access, playerIn, PvZBlockRegister.FRAGMENT_SPLICE.get());
	}
}