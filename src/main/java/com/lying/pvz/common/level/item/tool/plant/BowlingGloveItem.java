package com.lying.pvz.common.level.item.tool.plant;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.bowling.AbstractBowlingProjectile;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 保龄球手套（Bowling Glove）道具。
 *
 * <p><b>客户端 / 服务端</b>：
 * {@code useOn} 两端都调用，副作用只在服务端执行。日志 {@code A} / {@code B} 是调试用的，
 * 用于排查"手套点地后无实体出现"的问题，可随时移除。
 */
public class BowlingGloveItem extends Item {

	/** 保龄球类型注册表，key = {@link IPlantType}。 */
	private static final Map<Identifier, BowlingType> BOWLINGS = new HashMap<>();

	static {
		registerBowling(PVZPlants.WALL_NUT, PvZEntityRegister.WALL_NUT_BOWLING::get, 1.0F);
		registerBowling(PVZPlants.EXPLODE_O_NUT, PvZEntityRegister.EXPLOSION_BOWLING::get, 1.0F);
		registerBowling(PVZPlants.GIANT_NUT, PvZEntityRegister.GIANT_NUT_BOWLING::get, 1.2F);
	}

	public BowlingGloveItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}
		InteractionHand hand = context.getHand();
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();

		Optional<BowlingType> type = getBowlingType(stack);
		if (type.isEmpty()) {
			if (!world.isClientSide()) {
				PlayerUtil.sendMsgTo(player,
						Component.translatable("help.pvz.bowling_glove.empty").withStyle(ChatFormatting.RED));
				player.getCooldowns().addCooldown(stack, 20);
			}
			return InteractionResult.FAIL;
		}

		BlockPos spawnPos = pos;
		if (!world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()) {
			spawnPos = pos.relative(context.getClickedFace());
		}

		if (context.getClickedFace() != Direction.UP || !world.isEmptyBlock(pos.above())) {
			return InteractionResult.FAIL;
		}

		EntityType<? extends Entity> entityType = type.get().getEntity();
		if (entityType == null) {
			PvZMod.LOGGER.error("BowlingGloveItem Error : no such bowling entity !");
			return InteractionResult.FAIL;
		}

		if (!world.isClientSide()) {
			Entity entity = entityType.create((ServerLevel) player.level(), EntitySpawnReason.TRIGGERED);
			if (entity == null) {
				PvZMod.LOGGER.error("BowlingGloveItem Error : bowling entity create error !");
				return InteractionResult.FAIL;
			}
			// 让球底恰好贴在支撑面顶面（spawnPos 是空方块，球站在其下方方块上）。
			// 支撑面顶面 = spawnPos.getY()，球中心应在顶面 + 半高，避免生成瞬间半埋或悬空。
			final double halfHeight = entity.getBbHeight() / 2.0D;
			entity.snapTo(spawnPos.getX() + 0.5, spawnPos.getY() + halfHeight, spawnPos.getZ() + 0.5,
					player.getYRot(), 0.0F);

			PvZMod.LOGGER.info("[BowlingGlove] A player={} survival={} bowlingType={} face={} clickedPos={} clickedBlock={} aboveBlock={} spawnPos={} spawnEqualsClicked={} entityType={} movedTo=({},{},{}) isBowling={}",
					player.getName().getString(),
					PlayerUtil.isPlayerSurvival(player),
					type.get().getType(),
					context.getClickedFace(),
					pos,
					world.getBlockState(pos).getBlock().getName().getString(),
					world.getBlockState(pos.above()).getBlock().getName().getString(),
					spawnPos,
					spawnPos.equals(pos),
					entityType.getDescription().getString(),
					entity.getX(), entity.getY(), entity.getZ(),
					entity instanceof AbstractBowlingProjectile);

			if (!(entity instanceof AbstractBowlingProjectile bowling)) {
				PvZMod.LOGGER.error("BowlingGloveItem Error : bowling entity type mismatch !");
				return InteractionResult.FAIL;
			}

			player.level().addFreshEntity(entity);
			bowling.shoot(player);

			PvZMod.LOGGER.info("[BowlingGlove] B alive={} removed={} type={} pos=({},{},{}) levelServer={} inLevel={}",
					entity.isAlive(),
					entity.isRemoved(),
					entity.getType().getDescription().getString(),
					entity.getX(), entity.getY(), entity.getZ(),
					entity.level() instanceof ServerLevel,
					entity.level().getEntity(entity.getId()) == entity);

			if (PlayerUtil.isPlayerSurvival(player)) {
				setEmpty(stack);
				player.getCooldowns().addCooldown(stack, 100);
				stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
			}
		}
		return InteractionResult.SUCCESS;
	}

	/**
	 * 右击植物时装入保龄球。
	 *
	 * <p>只在服务端、主手、手持手套、目标为已注册保龄球植物时生效。
	 * 成功时写入 {@code BOWLING_TYPE} 组件并移除植物实体；
	 * 失败时向玩家发送红色提示并 20 tick 冷却。
	 *
	 * <p><b>注意</b>：此静态方法不会被自动调用，需要在 {@code NeoForge.EVENT_BUS} 上注册
	 * （如 {@code @EventBusSubscriber} 或 {@code NeoForge.EVENT_BUS.addListener(BowlingGloveItem::onPickUp)}）。
	 */
	public static void onPickUp(PlayerInteractEvent.EntityInteract ev) {
		if (getBowlingType(ev.getItemStack()).isPresent()) return;
		if (ev.getLevel().isClientSide()) {
			return;
		}
		if (ev.getHand() != InteractionHand.MAIN_HAND) {
			return;
		}
		if (!(ev.getItemStack().getItem() instanceof BowlingGloveItem)) {
			return;
		}

		if (ev.getTarget() instanceof PlantEntity plantEntity && isBowlingPlant(plantEntity)) {
			setBowlingType(ev.getItemStack(), plantEntity.getPlantType());
			plantEntity.discard();
			return;
		}

		PlayerUtil.sendMsgTo(ev.getEntity(),
				Component.translatable("help.pvz.bowling_glove.fail").withStyle(ChatFormatting.RED));
		ev.getEntity().getCooldowns().addCooldown(ev.getItemStack().getItem().getDefaultInstance(), 20);
	}

	public static Optional<BowlingType> getBowlingType(ItemStack stack) {
		return Optional.ofNullable(stack.get(PvZDataComponents.BOWLING_TYPE.get()))
				.map(BOWLINGS::get);
	}

	public static ItemStack setBowlingType(ItemStack stack, IPlantType type) {
		stack.set(PvZDataComponents.BOWLING_TYPE.get(), type.getID());
		return stack;
	}

	public static boolean isBowlingPlant(PlantEntity entity) {
		return BOWLINGS.containsKey(entity.getPlantType().getID());
	}

	public static ItemStack setEmpty(ItemStack stack) {
		stack.remove(PvZDataComponents.BOWLING_TYPE.get());
		return stack;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
								Consumer<Component> builder, TooltipFlag flag) {
		Optional<BowlingType> plant = getBowlingType(stack);
		if (plant.isEmpty()) {
			builder.accept(Component.translatable("tooltip.pvz.bowling_glove.empty")
					.withStyle(ChatFormatting.GOLD));
		} else {
			builder.accept(Component.translatable("tooltip.pvz.bowling_glove.full")
					.withStyle(ChatFormatting.GOLD)
					.append(plant.get().getType().getText().withStyle(ChatFormatting.GREEN)));
		}
	}
	public static Collection<BowlingType> allBowlingTypes() {
		return Collections.unmodifiableCollection(BOWLINGS.values());
	}


	public static void registerBowling(DeferredHolder<IPlantType, ? extends IPlantType> type,
                                       Supplier<EntityType<? extends Entity>> supplier, float size) {
		BOWLINGS.put(type.getId(), new BowlingType(type::get, supplier, size));
	}

	/** 单个保龄球类型的注册信息。 */
	public static class BowlingType {
		private final Supplier<EntityType<? extends Entity>> supplier;
		private final Supplier<IPlantType> type;
		private final float renderSize;

		public BowlingType(Supplier<IPlantType> type,
						   Supplier<EntityType<? extends Entity>> supplier, float size) {
			this.supplier = supplier;
			this.type = type;
			this.renderSize = size;
		}

		public EntityType<? extends Entity> getEntity() {
			return this.supplier.get();
		}

		/** 运行时访问，此时注册已完成。 */
		public IPlantType getType() {
			return this.type.get();
		}

		public float getSize() {
			return this.renderSize;
		}
	}
}