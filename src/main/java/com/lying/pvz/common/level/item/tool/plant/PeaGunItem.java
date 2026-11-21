package com.lying.pvz.common.level.item.tool.plant;

import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.*;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.common.menu.PeaGunMenu;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.common.tag.PvZItemTags;
import com.lying.pvz.event.custom.PeaGunShootEvent;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PeaGunItem extends Item {

	public static final int PEA_GUN_SLOT_NUM = 28;

	private static final MobEffectInstance DEFAULT_COLD_EFFECT =
			new MobEffectInstance(MobEffects.SLOWNESS, 60, 1);
	private static final MobEffectInstance DEFAULT_FROZEN_EFFECT =
			new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 0);

	private static final List<Supplier<IPlantType>> SHOOT_MODES = new ArrayList<>();

	static {
		SHOOT_MODES.add(PVZPlants.PEA_SHOOTER);
		SHOOT_MODES.add(PVZPlants.SNOW_PEA);
		SHOOT_MODES.add(PVZPlants.REPEATER);
		SHOOT_MODES.add(PVZPlants.THREE_PEATER);
		SHOOT_MODES.add(PVZPlants.SPLIT_PEA);
		SHOOT_MODES.add(PVZPlants.GATLING_PEA);
		SHOOT_MODES.add(PVZPlants.STAR_FRUIT);
		SHOOT_MODES.add(OtherPlants.ANGEL_STAR_FRUIT);
	}

	public PeaGunItem(Properties properties) {
		super(properties);
	}

	public static void registerPeaGunShootMode(IPlantType type) {
		SHOOT_MODES.add(() -> type);
	}

	// ============================ 装备 ============================



	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
		return armorType == EquipmentSlot.HEAD;
	}

	// ============================ 存储代理 ============================

	private static ItemStack getContentSlot(ItemStack host, int slot) {
		if (slot < 0 || slot >= PEA_GUN_SLOT_NUM) return ItemStack.EMPTY;
		ItemContainerContents contents = host.getOrDefault(
				PvZDataComponents.CARD_CONTENTS.get(), ItemContainerContents.EMPTY);
		if (slot >= contents.getSlots()) return ItemStack.EMPTY;
		return contents.getTemplateInSlot(slot).map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
	}

	private static void setContentSlot(ItemStack host, int slot, ItemStack value) {
		if (slot < 0 || slot >= PEA_GUN_SLOT_NUM) return;
		ItemContainerContents contents = host.getOrDefault(
				PvZDataComponents.CARD_CONTENTS.get(), ItemContainerContents.EMPTY);
		int stored = contents.getSlots();
		List<ItemStack> list = new ArrayList<>(PEA_GUN_SLOT_NUM);
		for (int i = 0; i < PEA_GUN_SLOT_NUM; i++) {
			if (i == slot) {
				list.add(value);
			} else if (i < stored) {
				list.add(contents.getTemplateInSlot(i).map(ItemStackTemplate::create).orElse(ItemStack.EMPTY));
			} else {
				list.add(ItemStack.EMPTY);
			}
		}
		host.set(PvZDataComponents.CARD_CONTENTS.get(), ItemContainerContents.fromItems(list));
	}

	public static SimpleContainer getContainer(ItemStack host) {
		return new SimpleContainer(PEA_GUN_SLOT_NUM) {
			@Override
			public ItemStack getItem(int slot) {
				return getContentSlot(host, slot);
			}

			@Override
			public void setItem(int slot, ItemStack s) {
				setContentSlot(host, slot, s);
				this.setChanged();
			}

			@Override
			public ItemStack removeItem(int slot, int amount) {
				ItemStack current = getContentSlot(host, slot);
				if (current.isEmpty() || amount <= 0) return ItemStack.EMPTY;
				int take = Math.min(amount, current.getCount());
				ItemStack result = current.copyWithCount(take);
				setContentSlot(host, slot,
						take >= current.getCount() ? ItemStack.EMPTY : current.copyWithCount(current.getCount() - take));
				this.setChanged();
				return result;
			}

			@Override
			public ItemStack removeItemNoUpdate(int slot) {
				ItemStack current = getContentSlot(host, slot);
				setContentSlot(host, slot, ItemStack.EMPTY);
				return current;
			}

			@Override
			public boolean canPlaceItem(int slot, @Nonnull ItemStack s) {
				if (slot == 0) return isValidMode(s);
				return s.is(PvZItemTags.PEA_GUN_BULLETS);
			}
		};
	}

	// ============================ use ============================

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (hand != InteractionHand.MAIN_HAND) {
			if (!level.isClientSide() && player instanceof ServerPlayer sp) {
				sp.openMenu(new SimpleMenuProvider(
						(id, inventory, p) -> new PeaGunMenu(id, p),
						Component.translatable("gui.pvz.pea_gun.show")));
			}
			return InteractionResult.SUCCESS;
		}

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			if (!level.isClientSide()) {
				PlayerUtil.sendMsgTo(player,
						Component.translatable("help.pvz.broken").withStyle(ChatFormatting.RED));
			}
			player.getCooldowns().addCooldown(this.getDefaultInstance(), 20);
			return InteractionResult.FAIL;
		}

		SimpleContainer inv = getContainer(stack);
		if (firstBulletSlot(inv) < 0) {
			if (!level.isClientSide()) {
				PlayerUtil.sendMsgTo(player,
						Component.translatable("help.pvz.no_bullet").withStyle(ChatFormatting.RED));
			}
			player.getCooldowns().addCooldown(this.getDefaultInstance(), 20);
			return InteractionResult.FAIL;
		}
		if (getShootMode(inv.getItem(0)) == null) {
			if (!level.isClientSide()) {
				PlayerUtil.sendMsgTo(player,
						Component.translatable("help.pvz.no_shoot_mode").withStyle(ChatFormatting.RED));
			}
			player.getCooldowns().addCooldown(this.getDefaultInstance(), 20);
			return InteractionResult.FAIL;
		}

		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	// ============================ onUseTick ============================

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remainingUseDuration) {
		if (level.isClientSide()) return;
		if (!(living instanceof Player player)) return;
		if (remainingUseDuration + 5 >= this.getUseDuration(stack, living)) return;

		int cd = getShootCD(living, stack);
		if (cd <= 0 || remainingUseDuration % cd != 0) return;

		SimpleContainer inv = getContainer(stack);
		IPlantType type = getShootMode(inv.getItem(0));
		this.performShoot(level, player, stack, type);
	}

	// ============================ 头部自动射击 ============================

	public static void checkHeadShoot(Player player) {
		ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!(stack.getItem() instanceof PeaGunItem peaGun)) return;
		if (player.getCooldowns().isOnCooldown(stack.getItem().getDefaultInstance())) return;

		SimpleContainer inv = getContainer(stack);
		if (stack.getDamageValue() < stack.getMaxDamage()
				&& firstBulletSlot(inv) >= 0
				&& getShootMode(inv.getItem(0)) != null) {
			IPlantType mode = getShootMode(inv.getItem(0));
			peaGun.performShoot(player.level(), player, stack, mode);
			player.getCooldowns().addCooldown(stack.getItem().getDefaultInstance(),
					Math.max(5, getShootCD(player, stack)));
		} else {
			player.getCooldowns().addCooldown(stack.getItem().getDefaultInstance(), 200);
		}
	}

	// ============================ 射击 ============================

	/**
	 * 执行一次射击。
	 *
	 * <p><b>只在服务端生效</b>：客户端调用会立即 return，发射、扣弹、损耗、事件全部由服务端驱动，
	 * 客户端通过实体同步和容器同步收到结果。
	 *
	 * @param mode 当前模式；null 时直接 return，不消耗子弹
	 */
	public void performShoot(Level level, Player player, ItemStack gunStack, IPlantType mode) {
		if (level.isClientSide()) return;
		if (mode == null) return;

		SimpleContainer inv = getContainer(gunStack);
		int bulletPos = firstBulletSlot(inv);
		if (bulletPos < 0) return;
		ItemStack bulletStack = inv.getItem(bulletPos);
		if (bulletStack.isEmpty()) return;

		if (NeoForge.EVENT_BUS.post(new PeaGunShootEvent(player, gunStack, mode)).isCanceled()) return;

		if (mode == PVZPlants.PEA_SHOOTER.get()) {
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.5, 0, 0);
		} else if (mode == PVZPlants.SNOW_PEA.get()) {
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.5, 0, 0);
		} else if (mode == PVZPlants.REPEATER.get()) {
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.5, 0, 0);
			this.shootPea(level, player, mode, bulletStack, gunStack, 0, 0, 0);
		} else if (mode == PVZPlants.THREE_PEATER.get()) {
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.25, -0.25, -15);
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.25, 0, 0);
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.25, 0.25, 15);
		} else if (mode == PVZPlants.SPLIT_PEA.get()) {
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.25, 0, 0);
			this.shootPea(level, player, mode, bulletStack, gunStack, 0, 0, 180);
			this.shootPea(level, player, mode, bulletStack, gunStack, -0.5, 0, 180);
		} else if (mode == PVZPlants.STAR_FRUIT.get()) {
			int base = player.getRandom().nextInt(72);
			for (int i = 0; i < 5; i++) {
				this.shootPea(level, player, mode, bulletStack, gunStack, 0.25, 0, base + 72 * i);
			}
		} else if (mode == OtherPlants.ANGEL_STAR_FRUIT.get()) {
			for (int i = 0; i < 5; i++) {
				this.shootPea(level, player, mode, bulletStack, gunStack, 0.25, 0, 72 * i);
			}
		} else if (mode == PVZPlants.GATLING_PEA.get()) {
			this.shootPea(level, player, mode, bulletStack, gunStack, 1.5, 0, 0);
			this.shootPea(level, player, mode, bulletStack, gunStack, 1, 0, 0);
			this.shootPea(level, player, mode, bulletStack, gunStack, 0.5, 0, 0);
			this.shootPea(level, player, mode, bulletStack, gunStack, 0, 0, 0);
		} else {
			// 未知模式：不发子弹、不扣子弹、不损耗
			return;
		}

		SoundEvent sound = (mode == PVZPlants.SNOW_PEA.get()
				|| bulletStack.getItem().equals(PvZItemRegister.SNOW_PEA.get()))
				? PvZSoundRegister.SNOW_SHOOT.get()
				: SoundEvents.SNOW_GOLEM_SHOOT;
		EntityUtil.playSound(player, sound);

		this.shrinkItemStack(player, gunStack, inv, bulletPos);

		if (PlayerUtil.isPlayerSurvival(player)) {
			gunStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
		}
	}

	/**
	 * 发射一颗豌豆。仅服务端有效。
	 */
	public void shootPea(Level level, Player player, IPlantType mode, ItemStack bulletStack, ItemStack gunStack,
						 double forwardOffset, double rightOffset, float angle) {
		if (!(level instanceof ServerLevel serverLevel)) return;

		Vec3 look = player.getLookAngle();
		double deltaX = forwardOffset * look.x - rightOffset * look.z;
		double deltaZ = forwardOffset * look.z + rightOffset * look.x;
		double spawnX = player.getX() + deltaX;
		double spawnY = player.getEyeY() - 0.4;
		double spawnZ = player.getZ() + deltaZ;

		final int variant = resolveVariant(mode, bulletStack);

		int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(
				lookup(player, Enchantments.POWER), gunStack);

		final ShotSpec spec = new ShotSpec()
				.variant(variant)
				.baseDamage(1.5F + powerLevel * 0.5F);
		if (variant == BulletKinds.PEA_ICE) {
			spec.hitEffect(DEFAULT_COLD_EFFECT);
			spec.hitEffect(DEFAULT_FROZEN_EFFECT);
		}

		Vec3 dir = rotateYaw(look, angle).normalize();
		BulletLauncher.launch(serverLevel, PvZEntityRegister.PEA.get(), player,
				EntityUtil.getEntityGroup(player), BulletKinds.PEA, spec,
				new Vec3(spawnX, spawnY, spawnZ), null, dir);
	}

	private static int resolveVariant(IPlantType mode, ItemStack bulletStack) {
		if (mode == PVZPlants.SNOW_PEA.get()) return BulletKinds.PEA_ICE;
		Item item = bulletStack.getItem();
		if (item == PvZItemRegister.SNOW_PEA.get()) return BulletKinds.PEA_ICE;
		if (item == PvZItemRegister.FLAME_PEA.get()) return BulletKinds.PEA_FIRE;
		return BulletKinds.PEA_NORMAL;
	}

	private static Vec3 rotateYaw(Vec3 dir, double angleDeg) {
		if (angleDeg == 0D) return dir;
		double rad = Math.toRadians(angleDeg);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		return new Vec3(
				dir.x * cos - dir.z * sin,
				dir.y,
				dir.x * sin + dir.z * cos
		);
	}

	private void shrinkItemStack(Player player, ItemStack gunStack, SimpleContainer inv, int pos) {
		PlayerDataManager manager = PlayerUtil.getManager(player);
		int lvl = manager == null ? 0 : manager.getResource(Resources.TREE_LVL);
		boolean infinity = EnchantmentHelper.getItemEnchantmentLevel(
				lookup(player, Enchantments.INFINITY), gunStack) > 0;

		boolean noShrink = player.hasEffect(PvZEffectRegister.ENERGETIC_EFFECT)
				|| !PlayerUtil.isPlayerSurvival(player)
				|| (infinity && player.getRandom().nextInt(100) < 50 + (lvl + 9) / 10);

		if (!noShrink) {
			inv.removeItem(pos, 1);
		}
	}

	// ============================ 附魔工具 ============================

	private static HolderLookup.RegistryLookup<Enchantment> lookupRegistry(LivingEntity living) {
		return living.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
	}

	private static Holder<Enchantment> lookup(LivingEntity living, ResourceKey<Enchantment> key) {
		return lookupRegistry(living).getOrThrow(key);
	}

	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return enchantment.is(Enchantments.QUICK_CHARGE)
				|| enchantment.is(Enchantments.INFINITY)
				|| enchantment.is(Enchantments.POWER)
				|| enchantment.is(Enchantments.UNBREAKING);
	}

	@Override
	public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		return supportsEnchantment(stack, enchantment);
	}

	// ============================ 其它 ============================

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
								Consumer<Component> builder, TooltipFlag flag) {
		builder.accept(Component.translatable("tooltip.pvz.pea_gun").withStyle(ChatFormatting.GREEN));
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 100000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	public static IPlantType getShootMode(ItemStack stack) {
		if (!(stack.getItem() instanceof AbstractCardItem card) || card.isEnjoy()) return null;
		IPAZType type = card.getType();
		if (!(type instanceof IPlantType plant)) return null;
		for (Supplier<IPlantType> mode : SHOOT_MODES) {
			if (mode.get() == plant) return plant;
		}
		return null;
	}

	public static boolean isValidMode(ItemStack stack) {
		return getShootMode(stack) != null;
	}

	public static boolean hasShootMode(ItemStack stack) {
		return getShootMode(stack) != null;
	}

	// -------- 子弹槽辅助 --------

	/** 第一个非空子弹槽索引，1..27；-1 表示没有。 */
	private static int firstBulletSlot(SimpleContainer inv) {
		for (int i = 1; i < PEA_GUN_SLOT_NUM; i++) {
			if (!inv.getItem(i).isEmpty()) return i;
		}
		return -1;
	}

	public static boolean hasBullet(ItemStack stack) {
		return firstBulletSlot(getContainer(stack)) >= 0;
	}

	public static boolean hasBullet(SimpleContainer inv) {
		return firstBulletSlot(inv) >= 0;
	}

	public static ItemStack getFirstBullets(ItemStack stack) {
		SimpleContainer inv = getContainer(stack);
		int pos = firstBulletSlot(inv);
		return pos < 0 ? ItemStack.EMPTY : inv.getItem(pos);
	}

	public static int getFirstPos(ItemStack stack) {
		return firstBulletSlot(getContainer(stack));
	}

	public static int getShootCD(LivingEntity living, ItemStack stack) {
		if (living.hasEffect(PvZEffectRegister.ENERGETIC_EFFECT)) return 2;
		int lvl = EnchantmentHelper.getItemEnchantmentLevel(
				lookup(living, Enchantments.QUICK_CHARGE), stack);
		return lvl == 0 ? 30 : 25 - Math.min(3, lvl) * 5;
	}
}