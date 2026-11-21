package com.lying.pvz.common.level.blockentity;

import com.lying.pvz.common.enchantment.EnchantmentUtil;
import com.lying.pvz.common.init.PvZBlockEntityRegister;
import com.lying.pvz.common.level.entity.drop.DropEntity;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.item.component.SunStorage;
import com.lying.pvz.common.level.item.tool.plant.SunStorageSaplingItem;
import com.lying.pvz.common.menu.SunConverterMenu;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SunConverterBlockEntity extends BaseBlockEntity implements MenuProvider {

	public final ItemStacksResourceHandler handler = new ItemStacksResourceHandler(9) {
		@Override
		protected void onContentsChanged(int index, ItemStack previousContents) {
			SunConverterBlockEntity.this.setChanged();
		}
	};

	public final ContainerData array = new SimpleContainerData(1);

	private final Set<SunEntity> sunSet = new HashSet<>();
	private static final int MAX_SEARCH_TICK = 60;
	private static final double MAX_SEARCH_RANGE = 10;
	private static final double SUN_FLY_SPEED = 0.15D;

	/** 当前正在吸收的槽位；-1 表示无。仅服务端使用。 */
	private int absorbPos = -1;
	/** 生命周期 tick 计数，用于控制搜索频率；不持久化。 */
	public int tickExist = 0;

	public SunConverterBlockEntity(BlockPos pos, BlockState state) {
		super(PvZBlockEntityRegister.SUN_CONVERTER.get(), pos, state);
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
		++this.tickExist;
		Level level = this.level;
		if (level == null || level.isClientSide()) return;

		this.tickSunSet();
		this.array.set(0, this.absorbPos >= 0 ? 1 : 0);
	}

	private void tickSunSet() {
		Level level = this.level;
		if (level == null) return;

		// 1) 清理失效（已被移除 / 已脱离 ABSORB 状态的）
		this.sunSet.removeIf(sun ->
				sun == null || sun.isRemoved() || sun.getDropState() != DropEntity.DropStates.ABSORB);

		// 2) 先算一次可用槽位
		int slot = this.findAbsorbSlot();
		this.absorbPos = slot;
		if (slot < 0) {
			this.releaseAllSuns();
			return;
		}

		// 3) 周期性搜索附近太阳
		if (this.tickExist % MAX_SEARCH_TICK == 0) {
			level.getEntitiesOfClass(SunEntity.class,
					MathUtil.getAABBWithPos(this.worldPosition, MAX_SEARCH_RANGE),
					sun -> sun.getDropState() == DropEntity.DropStates.NORMAL && !this.sunSet.contains(sun)
			).forEach(sun -> {
				sun.setDropState(DropEntity.DropStates.ABSORB);
				this.sunSet.add(sun);
			});
		}

		// 4) 拷贝一份遍历，避免 discard 触发 ConcurrentModification
		final Vec3 target = new Vec3(
				this.worldPosition.getX() + 0.5D,
				this.worldPosition.getY() + 1.0D,
				this.worldPosition.getZ() + 0.5D);

		for (SunEntity sun : new ArrayList<>(this.sunSet)) {
			if (sun.isRemoved() || sun.getDropState() != DropEntity.DropStates.ABSORB) continue;

			Vec3 dir = target.subtract(sun.position());
			if (dir.length() <= 1.0D) {
				this.collectIntoSlot(sun, slot);
				if (sun.isRemoved()) this.sunSet.remove(sun);

				// 收集后槽位可能已满，重查；不再可用则全部释放
				slot = this.findAbsorbSlot();
				this.absorbPos = slot;
				if (slot < 0) {
					this.releaseAllSuns();
					return;
				}
			} else {
				sun.setDeltaMovement(dir.normalize().scale(SUN_FLY_SPEED));
			}
		}
	}

	/**
	 * 找一个可以吸收太阳的槽位。无副作用。
	 *
	 * @return 槽位索引，-1 表示不可工作
	 */
	private int findAbsorbSlot() {
		for (int i = 0; i < this.handler.size(); ++i) {
			ItemStack stack = this.getStack(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof SunStorageSaplingItem) {
				SunStorage storage = SunStorageSaplingItem.getStorage(stack);
				if (!storage.once() && !storage.isFull()) return i;
			} else if (stack.isDamaged()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 把一只太阳收集到指定槽位。收集后若太阳还有剩余，会回写 amount；否则 discard。
	 */
	private void collectIntoSlot(SunEntity sun, int slot) {
		int amount = sun.getAmount();
		if (amount <= 0) {
			sun.discard();
			return;
		}

		ItemStack stack = this.getStack(slot);
		if (stack.isEmpty()) {
			sun.setAmount(amount);
			return;
		}

		if (stack.getItem() instanceof SunStorageSaplingItem) {
			SunStorage storage = SunStorageSaplingItem.getStorage(stack);
			if (storage.once() || storage.isFull()) {
				// 槽位状态已变，保守跳过
				sun.setAmount(amount);
				return;
			}
			long max = storage.max();
			long now = storage.amount();
			long addable = Math.min(amount, max - now);
			if (addable > 0) {
				SunStorageSaplingItem.setStorage(stack, storage.withAmount(now + addable));
				this.setStack(slot, stack);
				amount -= (int) addable;
			}
		} else if (stack.isDamaged()) {
			// TODO: 若 EnchantmentUtil.repairItem 返回实际修复量，改为
			//       int repaired = EnchantmentUtil.repairItem(stack, amount);
			//       amount -= repaired;
			// 当前签名若为 void，剩余 amount 会被丢弃（与旧版行为一致）。
			EnchantmentUtil.repairItem(stack, amount);
			this.setStack(slot, stack);
			amount = 0;
		} else {
			sun.setAmount(amount);
			return;
		}

		if (amount > 0) {
			sun.setAmount(amount);
		} else {
			sun.discard();
		}
	}

	/** 把正在吸收的太阳全部恢复为可拾取状态。 */
	private void releaseAllSuns() {
		for (SunEntity sun : new ArrayList<>(this.sunSet)) {
			if (sun != null && !sun.isRemoved()) {
				sun.setDropState(DropEntity.DropStates.NORMAL);
			}
		}
		this.sunSet.clear();
	}

	// ============================ 持久化 ============================

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		input.readChild("itemstack_list", this.handler);
		// tickExist 不持久化：加载后从 0 开始
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putChild("itemstack_list", this.handler);
	}

	// ============================ 掉落 ============================

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		Level level = this.level;
		if (level == null) return;

		// 先释放正在吸收的太阳，避免卡在 ABSORB
		this.releaseAllSuns();

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
		return new SunConverterMenu(id, inv, this);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.pvz.sun_converter");
	}
}