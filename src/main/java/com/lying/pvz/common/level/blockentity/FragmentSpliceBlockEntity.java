package com.lying.pvz.common.level.blockentity;

import com.lying.pvz.common.init.PvZBlockEntityRegister;
import com.lying.pvz.common.init.PvZRecipeRegister;
import com.lying.pvz.common.level.item.component.SunStorage;
import com.lying.pvz.common.level.item.tool.plant.SunStorageSaplingItem;
import com.lying.pvz.common.menu.FragmentSpliceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片拼接方块实体。
 * <p>{@code array} 索引：
 * <ul>
 *   <li>0 = 已吸收太阳数量</li>
 *   <li>1 = 是否满足合成条件（服务端权威，客户端只读）</li>
 * </ul>
 * 客户端<b>不</b>做配方查询，全部依赖 {@code array[1]}。
 */
public class FragmentSpliceBlockEntity extends BaseBlockEntity implements MenuProvider {

	public static final int CRAFT_COST = 10000;

	public static final int ARRAY_SUN = 0;
	public static final int ARRAY_CAN_CRAFT = 1;
	public static final int DATA_ARRAY_SIZE = 2;

	public final ItemStacksResourceHandler handler = new ItemStacksResourceHandler(1 + 1 + 25) {
		@Override
		protected void onContentsChanged(int index, ItemStack previousContents) {
			FragmentSpliceBlockEntity.this.setChanged();
		}
	};

	public final ContainerData array = new SimpleContainerData(DATA_ARRAY_SIZE);
	public int sunAmount = 0;

	public FragmentSpliceBlockEntity(BlockPos pos, BlockState state) {
		super(PvZBlockEntityRegister.FRAGMENT_SPLICE.get(), pos, state);
	}

	// ============================ Handler 辅助 ============================

	public ItemStack getStack(int slot) {
		return this.handler.getResource(slot).toStack((int) this.handler.getAmountAsLong(slot));
	}

	public void setStack(int slot, ItemStack stack) {
		this.handler.set(slot, ItemResource.of(stack), stack.getCount());
	}

	// ============================ tick ============================

	public void tick() {
		Level level = this.level;
		if (level != null && !level.isClientSide()) {
			this.absorbSunAmount();
			this.array.set(ARRAY_SUN, this.sunAmount);
			this.array.set(ARRAY_CAN_CRAFT, this.computeCanCraft(level) ? 1 : 0);
		}
	}

	/**
	 * 服务端权威判定。
	 * <p>三个条件：太阳满、result 槽空、当前输入匹配一个碎片配方。
	 */
	public boolean computeCanCraft(Level level) {
		if (this.sunAmount != CRAFT_COST) return false;
		if (!this.getStack(FragmentSpliceMenu.SLOT_RESULT).isEmpty()) return false;
		if (!(level instanceof ServerLevel serverLevel)) return false;

		final List<ItemStack> items = new ArrayList<>(FragmentSpliceMenu.INPUT_COUNT);
		for (int i = 0; i < FragmentSpliceMenu.INPUT_COUNT; i++) {
			items.add(this.getStack(FragmentSpliceMenu.SLOT_INPUT_START + i));
		}
		final CraftingInput input = CraftingInput.of(5, 5, items);

		return serverLevel.recipeAccess()
				.getRecipeFor(PvZRecipeRegister.FRAGMENT_RECIPE_TYPE.get(), input, serverLevel)
				.isPresent();
	}

	private void absorbSunAmount() {
		ItemStack stack = this.getStack(0);
		if (stack.isEmpty() || !(stack.getItem() instanceof SunStorageSaplingItem)) {
			return;
		}
		SunStorage storage = SunStorageSaplingItem.getStorage(stack);
		if (storage.once()) {
			return;
		}
		long available = storage.amount();
		if (available <= 0L) {
			return;
		}
		int remaining = CRAFT_COST - this.sunAmount;
		if (remaining <= 0) {
			return;
		}
		long decAmount = Math.min(remaining, Math.min(100L, available));
		this.sunAmount += (int) decAmount;
		SunStorageSaplingItem.setStorage(stack, storage.withAmount(available - decAmount));
		this.setStack(0, stack);
	}

	/**
	 * 每次合成每格消耗 1 个。若未来出现"一格消耗多个"的配方需改为按配方匹配数量扣减。
	 */
	public void clearCraftingSlots() {
		for (int i = 0; i < FragmentSpliceMenu.INPUT_COUNT; ++i) {
			int slot = i + FragmentSpliceMenu.SLOT_INPUT_START;
			ItemStack stack = this.getStack(slot);
			if (!stack.isEmpty()) {
				stack.shrink(1);
				this.setStack(slot, stack);
			}
		}
	}

	// ============================ 持久化 ============================

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		input.readChild("item_stack_list", this.handler);
		this.sunAmount = input.getIntOr("sun_amount", 0);
		// 提前刷新，避免打开菜单后 tick 前读到 0
		this.array.set(ARRAY_SUN, this.sunAmount);
		// ARRAY_CAN_CRAFT 由 tick 计算
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putChild("item_stack_list", this.handler);
		output.putInt("sun_amount", this.sunAmount);
	}

	// ============================ 掉落 ============================

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		Level level = this.level;
		if (level == null) return;
		for (int i = 0; i < this.handler.size(); ++i) {
			ItemStack stack = this.getStack(i);
			if (!stack.isEmpty()) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}
	}

	// ============================ Menu ============================

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return new FragmentSpliceMenu(id, inv, this);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("gui.pvz.fragment_splice");
	}
}