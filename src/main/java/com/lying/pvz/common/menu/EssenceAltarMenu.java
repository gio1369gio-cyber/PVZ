package com.lying.pvz.common.menu;

import com.lying.pvz.api.card.PvZCardComponents;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.level.blockentity.EssenceAltarBlockEntity;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.common.level.item.card.ImitaterCardItem;
import com.lying.pvz.common.level.item.material.EssenceItem;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EssenceAltarMenu extends AbstractContainerMenu {

	private static final int LEARN_COST = 4;
	public static final int HANDLER_SIZE = 4;

	private static final int DATA_SLOT_END = HANDLER_SIZE;
	private static final int PLAYER_INV_START = DATA_SLOT_END;
	private static final int PLAYER_INV_END = PLAYER_INV_START + 27;
	private static final int HOTBAR_START = PLAYER_INV_END;
	private static final int HOTBAR_END = HOTBAR_START + 9;

	private final @Nullable EssenceAltarBlockEntity te;
	private final ContainerLevelAccess access;
	private final ItemStacksResourceHandler handler;
	private final Player player;

	public EssenceAltarMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
		this(containerId, playerInv, null, ContainerLevelAccess.NULL,
				new ItemStacksResourceHandler(HANDLER_SIZE));
	}

	public EssenceAltarMenu(int containerId, Inventory playerInv, EssenceAltarBlockEntity te) {
		this(containerId, playerInv, te,
				ContainerLevelAccess.create(playerInv.player.level(), te.getBlockPos()),
				te.handler);
	}

	private EssenceAltarMenu(int containerId, Inventory playerInv, @Nullable EssenceAltarBlockEntity te,
							 ContainerLevelAccess access, ItemStacksResourceHandler handler) {
		super(PvZMenuRegister.ESSENCE_ALTAR.get(), containerId);
		this.te = te;
		this.access = access;
		this.handler = handler;
		this.player = playerInv.player;

		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, 0, 27, 9) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack stack) {
				return !stack.isEmpty()
						&& stack.getItem() instanceof AbstractCardItem card
						&& !(card instanceof ImitaterCardItem)
						&& !card.isEnjoy();
			}
		});

		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, 1, 49, 63) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack stack) {
				return !stack.isEmpty()
						&& stack.getItem() instanceof AbstractCardItem card
						&& card.isEnjoy();
			}
		});

		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, 2, 14, 38) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof EssenceItem;
			}
		});

		this.addSlot(new ResourceHandlerSlot(this.handler, this.handler::set, 3, 40, 38));

		this.addStandardInventorySlots(playerInv, 8, 84);
	}

	private ItemStack getStack(int slot) {
		if (this.te != null) {
			return this.te.getStack(slot);
		}
		return this.handler.getResource(slot).toStack(this.handler.getAmountAsInt(slot));
	}

	public ItemStack getSummonCard() { return getStack(0); }
	public ItemStack getEnjoyCard()  { return getStack(1); }
	public ItemStack getEssence()    { return getStack(2); }
	public ItemStack getMaterial()   { return getStack(3); }

	public void learnSkillAt(int pos) {
		if (this.te == null) return;

		final List<ISkillType> skills = getAvailableSkills();
		if (pos < 0 || pos >= skills.size()) return;

		final ISkillType skill = skills.get(pos);
		if (skill == null) return;

		// summon card 可能在客户端点击与服务端处理之间被移走
		final ItemStack summon = getSummonCard();
		if (summon.isEmpty() || !(summon.getItem() instanceof AbstractCardItem)) return;

		final int lvl = getCurrentSkillLevel(skill);
		if (lvl < 0 || lvl >= skill.getMaxLevel()) return;

		final ItemStack newCard = summon.copy();
		setSkillLevel(newCard, skill, lvl + 1);
		this.te.setStack(0, newCard);

		shrinkSlot(1, skill.getCostAt(lvl));
		shrinkSlot(2, LEARN_COST);
		shrinkSlot(3, LEARN_COST);

		this.te.setChanged();
		EntityUtil.playSound(this.player, SoundEvents.ENCHANTMENT_TABLE_USE);
	}

	private static void setSkillLevel(ItemStack card, ISkillType skill, int level) {
		final SkillLevels old = card.getOrDefault(
				PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY);
		final Map<Identifier, Integer> levels = new HashMap<>(old.levels());
		if (level <= 0) {
			levels.remove(skill.getID());
		} else {
			levels.put(skill.getID(), level);
		}
		card.set(PvZCardComponents.CARD_SKILLS.get(), new SkillLevels(levels));
	}

	private void shrinkSlot(int slot, int amount) {
		if (this.te == null) return;
		final ItemStack stack = this.te.getStack(slot);
		if (stack.isEmpty()) return;
		final int shrink = Math.min(amount, stack.getCount());
		final ItemStack copy = stack.copy();
		copy.shrink(shrink);
		this.te.setStack(slot, copy.isEmpty() ? ItemStack.EMPTY : copy);
	}

	public Optional<IPAZType> getPAZType() {
		final ItemStack card = getSummonCard();
		if (card.getItem() instanceof AbstractCardItem c) {
			return Optional.ofNullable(c.getType());
		}
		return Optional.empty();
	}

	public int getFragmentCount() {
		final ItemStack summon = getSummonCard();
		final ItemStack enjoy = getEnjoyCard();
		if (summon.getItem() instanceof AbstractCardItem a
				&& enjoy.getItem() instanceof AbstractCardItem b) {
			final IPAZType summonType = a.getType();
			if (summonType != null && summonType.equals(b.getType())) {
				return enjoy.getCount();
			}
		}
		return 0;
	}

	public List<ISkillType> getAvailableSkills() {
		final List<ISkillType> list = new ArrayList<>();
		final ItemStack summon = getSummonCard();
		if (!(summon.getItem() instanceof AbstractCardItem)) return list;

		final Optional<IPAZType> pazTypeOpt = getPAZType();
		if (pazTypeOpt.isEmpty() || !isMaterialEnough(pazTypeOpt.get())) return list;

		final IPAZType pazType = pazTypeOpt.get();
		final Set<String> set = new HashSet<>();
		final int point = getFragmentCount();
		pazType.getSkills().forEach(skill -> {
			final int lvl = getCurrentSkillLevel(skill);
			if (!set.contains(skill.getConflictGroup())
					&& lvl < skill.getMaxLevel()
					&& skill.getCostAt(lvl) <= point) {
				list.add(skill);
				set.add(skill.getConflictGroup());
			}
		});
		return list;
	}

	public int getCurrentSkillLevel(ISkillType skill) {
		final SkillLevels levels = getSummonCard().getOrDefault(
				PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY);
		return levels.getLevel(skill);
	}

	/**
	 * 新签名：用调用方传入的 skills 快照计算等级，保证长度与 skills 严格一致。
	 *
	 * <p>无参版本在内部重新调 {@link #getAvailableSkills()}，在客户端一帧内可能拿到
	 * 不同长度，导致渲染时 {@code skills/levels} 越界。
	 */
	public List<Integer> getCurrentSkillLevel(List<ISkillType> skills) {
		final List<Integer> list = new ArrayList<>(skills.size());
		skills.forEach(skill -> list.add(getCurrentSkillLevel(skill)));
		return list;
	}

	private boolean isMaterialEnough(IPAZType type) {
		final ItemStack essence = getEssence();
		final ItemStack material = getMaterial();
		if (!type.getEssence().getEssenceItem().equals(essence.getItem())
				|| essence.getCount() < LEARN_COST) {
			return false;
		}
		final Optional<TagKey<Item>> materialTag = type.getRank().getMaterial();
		return materialTag.isPresent()
				&& material.is(materialTag.get())
				&& material.getCount() >= LEARN_COST;
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (player != this.player) return false;
		if (id < 0) return false;
		this.learnSkillAt(id);
		return true;
	}

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
				if (!this.moveItemStackTo(rawStack, 0, DATA_SLOT_END, false)
						&& !this.moveItemStackTo(rawStack, HOTBAR_START, HOTBAR_END, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.moveItemStackTo(rawStack, 0, PLAYER_INV_END, false)) {
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
		return stillValid(this.access, playerIn, PvZBlockRegister.ESSENCE_ALTAR.get());
	}
}