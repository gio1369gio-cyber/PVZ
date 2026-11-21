package com.lying.pvz.common.level.blockentity;

import com.lying.pvz.common.init.PvZBlockEntityRegister;
import com.lying.pvz.common.init.PvZRecipeRegister;
import com.lying.pvz.common.level.item.component.SunStorage;
import com.lying.pvz.common.level.item.material.EssenceItem;
import com.lying.pvz.common.level.item.tool.plant.SunStorageSaplingItem;
import com.lying.pvz.common.menu.CardFusionMenu;
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
 * 卡片融合台方块实体。
 *
 * <p>{@code array} 索引：
 * <ul>
 *   <li>0 = 已吸收太阳数量</li>
 *   <li>1 = 已吸收精华数量</li>
 *   <li>2 = 是否满足合成条件（服务端权威，客户端只读）</li>
 * </ul>
 * 客户端<b>不</b>做配方查询，全部依赖 {@code array[2]}。
 */
public class CardFusionBlockEntity extends BaseBlockEntity implements MenuProvider {

	public static final int CRAFT_ESSENCE_COST = 8;
	public static final int CRAFT_SUN_COST = 5000;

	public static final int ARRAY_SUN = 0;
	public static final int ARRAY_ESSENCE = 1;
	public static final int ARRAY_CAN_CRAFT = 2;
	public static final int DATA_SIZE = 3;

	public final ItemStacksResourceHandler handler = new ItemStacksResourceHandler(12) {
		@Override
		protected void onContentsChanged(int index, ItemStack previousContents) {
			CardFusionBlockEntity.this.setChanged();
		}
	};

	public final ContainerData array = new SimpleContainerData(DATA_SIZE);
	public int sunAmount = 0;
	public int essenceAmount = 0;

	public CardFusionBlockEntity(BlockPos pos, BlockState state) {
		super(PvZBlockEntityRegister.CARD_FUSION.get(), pos, state);
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
			this.absorbEssences();
			this.array.set(ARRAY_SUN, this.sunAmount);
			this.array.set(ARRAY_ESSENCE, this.essenceAmount);
			this.array.set(ARRAY_CAN_CRAFT, this.computeCanCraft(level) ? 1 : 0);
		}
	}

	/**
	 * 服务端权威判定：太阳满、精华满、result 空、3x3 匹配融合配方。
	 */
	public boolean computeCanCraft(Level level) {
		if (this.sunAmount != CRAFT_SUN_COST) return false;
		if (this.essenceAmount != CRAFT_ESSENCE_COST) return false;
		if (!this.getStack(CardFusionMenu.SLOT_RESULT).isEmpty()) return false;
		if (!(level instanceof ServerLevel serverLevel)) return false;

		final List<ItemStack> items = new ArrayList<>(CardFusionMenu.SLOT_CRAFT_COUNT);
		for (int i = 0; i < CardFusionMenu.SLOT_CRAFT_COUNT; i++) {
			items.add(this.getStack(CardFusionMenu.SLOT_CRAFT_START + i));
		}
		final CraftingInput input = CraftingInput.of(3, 3, items);

		return serverLevel.recipeAccess()
				.getRecipeFor(PvZRecipeRegister.FUSION_RECIPE_TYPE.get(), input, serverLevel)
				.isPresent();
	}

	private void absorbSunAmount() {
		ItemStack stack = this.getStack(0);
		if (stack.isEmpty() || !(stack.getItem() instanceof SunStorageSaplingItem)) return;
		SunStorage storage = SunStorageSaplingItem.getStorage(stack);
		if (storage.once()) return;
		long available = storage.amount();
		if (available <= 0L) return;
		int remaining = CRAFT_SUN_COST - this.sunAmount;
		if (remaining <= 0) return;
		long decAmount = Math.min(remaining, Math.min(100L, available));
		this.sunAmount += (int) decAmount;
		SunStorageSaplingItem.setStorage(stack, storage.withAmount(available - decAmount));
		this.setStack(0, stack);
	}

	private void absorbEssences() {
		if (this.essenceAmount >= CRAFT_ESSENCE_COST) return;
		ItemStack stack = this.getStack(1);
		if (stack.isEmpty() || !(stack.getItem() instanceof EssenceItem)) return;

		// 原实现每 tick 只吸 1 个（8 tick 才满）；改为一次吸满可用数量，减少界面等待
		final int wanted = CRAFT_ESSENCE_COST - this.essenceAmount;
		final int take = Math.min(wanted, stack.getCount());
		stack.shrink(take);
		this.setStack(1, stack);
		this.essenceAmount += take;
	}

	public void clearCraftingSlots() {
		for (int i = 0; i < CardFusionMenu.SLOT_CRAFT_COUNT; ++i) {
			int slot = CardFusionMenu.SLOT_CRAFT_START + i;
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
		input.readChild("itemstack_list", this.handler);
		this.sunAmount = input.getIntOr("sun_amount", 0);
		this.essenceAmount = input.getIntOr("essence_amount", 0);
		// 立即刷新客户端显示快照，避免打开菜单后 tick 前读到 0
		this.array.set(ARRAY_SUN, this.sunAmount);
		this.array.set(ARRAY_ESSENCE, this.essenceAmount);
		// ARRAY_CAN_CRAFT 由 tick 计算
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putChild("itemstack_list", this.handler);
		output.putInt("sun_amount", this.sunAmount);
		output.putInt("essence_amount", this.essenceAmount);
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
		return new CardFusionMenu(id, inv, this);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("gui.pvz.card_fusion");
	}
}