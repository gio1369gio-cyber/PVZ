package com.lying.pvz.common.menu;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.init.PvZRecipeRegister;
import com.lying.pvz.common.level.blockentity.CardFusionBlockEntity;
import com.lying.pvz.common.level.item.material.EssenceItem;
import com.lying.pvz.common.level.item.tool.plant.SunStorageSaplingItem;
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

import java.util.ArrayList;
import java.util.List;

public class CardFusionMenu extends AbstractContainerMenu {

	// handler 内的数据槽索引
	public static final int SLOT_SUN = 0;
	public static final int SLOT_ESSENCE = 1;
	public static final int SLOT_RESULT = 2;
	public static final int SLOT_CRAFT_START = 3;
	public static final int SLOT_CRAFT_COUNT = 9;
	public static final int HANDLER_SIZE = 12; // 3 + 9
	public static final int DATA_SIZE = CardFusionBlockEntity.DATA_SIZE;

	// 菜单内槽位区间（addSlot 的注册顺序）
	// 0..11   : handler 数据槽
	// 12..38  : 玩家背包 27 格
	// 39..47  : 玩家热键栏 9 格
	private static final int DATA_SLOT_END = HANDLER_SIZE;              // 12
	private static final int PLAYER_INV_START = DATA_SLOT_END;          // 12
	private static final int PLAYER_INV_END = PLAYER_INV_START + 27;    // 39
	private static final int HOTBAR_START = PLAYER_INV_END;             // 39
	private static final int HOTBAR_END = HOTBAR_START + 9;             // 48

	private final @Nullable CardFusionBlockEntity te;
	private final ContainerLevelAccess access;
	private final ItemStacksResourceHandler handler;
	private final ContainerData data;
	private final Player player;

	/** 客户端构造函数，供 IContainerFactory 使用。 */
	public CardFusionMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
		this(containerId, playerInv, null, ContainerLevelAccess.NULL,
				new ItemStacksResourceHandler(HANDLER_SIZE), new SimpleContainerData(DATA_SIZE));
	}

	/** 服务端构造函数。 */
	public CardFusionMenu(int containerId, Inventory playerInv, CardFusionBlockEntity te) {
		this(containerId, playerInv, te,
				ContainerLevelAccess.create(playerInv.player.level(), te.getBlockPos()),
				te.handler, te.array);
	}

	private CardFusionMenu(int containerId, Inventory playerInv, @Nullable CardFusionBlockEntity te,
						   ContainerLevelAccess access, ItemStacksResourceHandler handler, ContainerData data) {
		super(PvZMenuRegister.CARD_FUSION.get(), containerId);
		this.te = te;
		this.access = access;
		this.handler = handler;
		this.data = data;
		this.player = playerInv.player;

		this.addDataSlots(this.data);

		// 0: sun storage sapling
		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, SLOT_SUN, 9, 80) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof SunStorageSaplingItem;
			}
		});
		// 1: essence
		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, SLOT_ESSENCE, 153, 80) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof EssenceItem;
			}
		});
		// 2: result（只出不进）
		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, SLOT_RESULT, 81, 98) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});
		// 3..11: 3x3 craft
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int idx = SLOT_CRAFT_START + i * 3 + j;
				this.addSlot(new ResourceHandlerSlot(
						this.handler, this.handler::set, idx,
						63 + j * 18, 26 + i * 18));
			}
		}
		// 12..47: player inventory + hotbar
		this.addStandardInventorySlots(playerInv, 9, 143);
	}

	// ------------------------------------------------------------------
	// 合成
	// ------------------------------------------------------------------

	/**
	 * 服务端权威合成。所有校验在服务端完成，客户端伪造包无法绕过。
	 * @return 是否真的执行了合成
	 */
	public boolean onCraft(Player player) {
		if (this.te == null) return false;
		if (player == null) return false;
		if (!player.getUUID().equals(this.player.getUUID())) return false;
		if (this.te.sunAmount != CardFusionBlockEntity.CRAFT_SUN_COST) return false;
		if (this.te.essenceAmount != CardFusionBlockEntity.CRAFT_ESSENCE_COST) return false;
		if (!this.handler.getResource(SLOT_RESULT).isEmpty()) return false;

		final ItemStack result = this.getResult();
		if (result.isEmpty()) return false;

		this.te.setStack(SLOT_RESULT, result.copy());
		this.te.clearCraftingSlots();
		this.te.sunAmount = 0;
		this.te.essenceAmount = 0;
		this.te.array.set(CardFusionBlockEntity.ARRAY_SUN, 0);
		this.te.array.set(CardFusionBlockEntity.ARRAY_ESSENCE, 0);
		this.te.array.set(CardFusionBlockEntity.ARRAY_CAN_CRAFT, 0);
		this.te.setChanged();
		return true;
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id == 0) return this.onCraft(player);
		return false;
	}

	/**
	 * 仅服务端可返回非空（客户端无配方索引）。
	 * <p>客户端 UI 用 {@link #canCraft()} 的服务端同步标志即可。
	 */
	public ItemStack getResult() {
		final List<ItemStack> items = new ArrayList<>(SLOT_CRAFT_COUNT);
		for (int i = 0; i < SLOT_CRAFT_COUNT; i++) {
			int idx = SLOT_CRAFT_START + i;
			items.add(this.handler.getResource(idx).toStack(this.handler.getAmountAsInt(idx)));
		}
		final CraftingInput input = CraftingInput.of(3, 3, items);

		if (this.player.level() instanceof ServerLevel serverLevel) {
			return serverLevel.recipeAccess()
					.getRecipeFor(PvZRecipeRegister.FUSION_RECIPE_TYPE.get(), input, serverLevel)
					.map(RecipeHolder::value)
					.map(recipe -> recipe.assemble(input))
					.orElse(ItemStack.EMPTY);
		}
		return ItemStack.EMPTY;
	}

	/** 服务端权威标志位，客户端读同步值。 */
	public boolean canCraft() {
		return this.data.get(CardFusionBlockEntity.ARRAY_CAN_CRAFT) == 1;
	}

	public int getSunAmount() {
		return this.data.get(CardFusionBlockEntity.ARRAY_SUN);
	}

	public int getEssenceAmount() {
		return this.data.get(CardFusionBlockEntity.ARRAY_ESSENCE);
	}

	// ------------------------------------------------------------------
	// 快捷键
	// ------------------------------------------------------------------

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot quickMovedSlot = this.slots.get(index);
		if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
			ItemStack rawStack = quickMovedSlot.getItem();
			quickMovedStack = rawStack.copy();

			if (index >= PLAYER_INV_START && index < HOTBAR_END) {
				if (!this.moveItemStackTo(rawStack, 0, DATA_SLOT_END, false)) {
					if (index < HOTBAR_START) {
						if (!this.moveItemStackTo(rawStack, HOTBAR_START, HOTBAR_END, false)) {
							return ItemStack.EMPTY;
						}
					} else {
						if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_START, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
			} else {
				if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, true)) {
					return ItemStack.EMPTY;
				}
			}

			if (rawStack.isEmpty()) {
				quickMovedSlot.setByPlayer(ItemStack.EMPTY);
			} else {
				quickMovedSlot.setChanged();
			}
			quickMovedSlot.onTake(player, rawStack);
		}
		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, PvZBlockRegister.CARD_FUSION_TABLE.get());
	}
}