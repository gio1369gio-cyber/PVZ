package com.lying.pvz.common.level.item.tool.plant;

import com.lying.pvz.api.entity.ability.ICollectible;
import com.lying.pvz.common.enchantment.EnchantmentUtil;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

/**
 * 资源收集器（Resource Collector）。
 *
 * <p><b>两种模式</b>：
 * <ul>
 *   <li><b>单体射线</b>（普通右键）：沿视线最远 {@value #SINGLE_COLLECT_RANGE} 格，
 *       命中 {@link ICollectible} 则调用其 {@link ICollectible#(Player)}，
 *       触发 {@code SINGLE_COLLECT_COOL_DOWN} tick 冷却。</li>
 *   <li><b>范围收集</b>（潜行右键）：以玩家为中心半径 {@value #RANGE_COLLECT_RANGE} 内
 *       所有可收集实体全部触发 {@link ICollectible (Player)}。
 *       命中至少 1 个则进 {@code RANGE_COLLECT_COOL_DOWN}，否则进 {@code SINGLE_COLLECT_COOL_DOWN}。</li>
 * </ul>
 *
 * <p><b>范围加成</b>：{@link EnchantmentUtil#getReachDistance} 按附魔等级扩展范围。
 *
 * <p><b>返回语义</b>：始终返回 {@code super.use(...)}，即 {@link InteractionResult#PASS}。
 * 副作用只在服务端执行，客户端不预测任何效果。
 */
public class ResourceCollectorItem extends Item {

	/** 单体收集成功后的冷却（tick）。 */
	private static final int SINGLE_COLLECT_COOL_DOWN = 8;

	/** 范围收集成功后的冷却（tick）。 */
	private static final int RANGE_COLLECT_COOL_DOWN = 200;

	/** 单体射线最大距离（格，不含附魔加成）。 */
	private static final int SINGLE_COLLECT_RANGE = 25;

	/** 范围收集基础半径（格，不含附魔加成）。 */
	private static final int RANGE_COLLECT_RANGE = 5;

	public ResourceCollectorItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			ItemStack stack = player.getItemInHand(hand);

			if (player.isShiftKeyDown()) {
				// ===== 范围收集 =====
				float range = EnchantmentUtil.getReachDistance(stack, RANGE_COLLECT_RANGE);
				List<Entity> list = EntityUtil.getPredicateEntities(player,
						EntityUtil.getEntityAABB(player, range, range), Entity.class,
						e -> e instanceof ICollectible collectible && collectible.canCollectBy(player));

				if (!list.isEmpty()) {
					player.awardStat(Stats.ITEM_USED.get(this));
					player.getCooldowns().addCooldown(this.getDefaultInstance(), RANGE_COLLECT_COOL_DOWN);
				} else {
					player.getCooldowns().addCooldown(this.getDefaultInstance(), SINGLE_COLLECT_COOL_DOWN);
				}

				list.forEach(e -> ((ICollectible) e).onCollect(player));
			} else {
				// ===== 单体射线收集 =====
				EntityHitResult entityRay = rayTraceEntities(level, player,
						EnchantmentUtil.getReachDistance(stack, SINGLE_COLLECT_RANGE));

				if (entityRay != null && entityRay.getType() == HitResult.Type.ENTITY) {
					if (entityRay.getEntity() instanceof ICollectible collectible) {
						collectible.onCollect(player);
						player.awardStat(Stats.ITEM_USED.get(this));
						player.getCooldowns().addCooldown(this.getDefaultInstance(), SINGLE_COLLECT_COOL_DOWN);
					}
				}
			}
		}
		return super.use(level, player, hand);
	}

	/**
	 * 沿玩家视线做实体射线检测。
	 *
	 * @param range 最远距离（格），已含附魔加成
	 */
	private static EntityHitResult rayTraceEntities(Level level, Player player, float range) {
		Vec3 eye = player.getEyePosition();
		Vec3 look = player.getLookAngle();
		Vec3 end = eye.add(look.scale(range));

		return ProjectileUtil.getEntityHitResult(
				level,
				player,
				eye,
				end,
				player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D),
				e -> e instanceof ICollectible && !e.isSpectator() && e.isPickable(),
				ProjectileUtil.computeMargin(player)   // ← 新增第 7 参
		);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
								Consumer<Component> builder, TooltipFlag flag) {
		builder.accept(Component.translatable("tooltip.pvz.resource_collector.use")
				.withStyle(ChatFormatting.YELLOW));
		builder.accept(Component.translatable("tooltip.pvz.resource_collector.info",
						(int) EnchantmentUtil.getReachDistance(stack, SINGLE_COLLECT_RANGE),
						(int) EnchantmentUtil.getReachDistance(stack, RANGE_COLLECT_RANGE))
				.withStyle(ChatFormatting.GREEN));
	}
}