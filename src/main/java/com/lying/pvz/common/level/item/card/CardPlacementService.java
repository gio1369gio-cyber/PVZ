package com.lying.pvz.common.level.item.card;

import com.lying.pvz.api.card.CardContent;
import com.lying.pvz.api.card.CardErrors;
import com.lying.pvz.api.card.ICardError;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.PlantTrait;
import com.lying.pvz.common.enchantment.EnchantmentUtil;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.block.plants.PvZLilyPadBlock;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.magic.ImitaterEntity;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * 卡片放置/召唤统一执行器。
 * <p>调用方：{@link AbstractCardItem#use} / {@link AbstractCardItem#useOn}。
 */
public final class CardPlacementService {

    private CardPlacementService() {
    }

    /* ------------------------------------------------------------------ */
    /* 入口                                                                */
    /* ------------------------------------------------------------------ */
    public static InteractionResult executeWaterRaycast(Player player, AbstractCardItem card,
                                                        ItemStack heldStack, CardContent content) {
        final BlockHitResult result = Item.getPlayerPOVHitResult(player.level(), player,
                ClipContext.Fluid.SOURCE_ONLY);
        if (result.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }
        final BlockPos pos = result.getBlockPos().above();
        return executeWater(player, card, heldStack, content, pos);
    }
    public static InteractionResult executeWater(Player player, AbstractCardItem card, ItemStack heldStack,
                                                 CardContent content, BlockPos pos) {
        final IPlantType plantType = content.asPlantType();
        if (plantType == null) {
            notify(player, heldStack, CardErrors.INVALID_CONTENT, 0);
            return InteractionResult.FAIL;
        }
        if (!plantType.hasTrait(PlantTrait.WATER)) {
            notify(player, heldStack, PlantPlacementErrors.GROUND_ERROR, 0);
            return InteractionResult.FAIL;
        }
        if (plantType == PVZPlants.CAT_TAIL.get()
                && !EnchantmentUtil.isSoilless(content.effectiveStack())) {
            notify(player, heldStack, PlantPlacementErrors.GROUND_ERROR, 0);
            return InteractionResult.FAIL;
        }
        if (!player.level().isEmptyBlock(pos)
                || !isWaterSupport(player.level(), pos.below())) {
            notify(player, heldStack, PlantPlacementErrors.WATER_ERROR, 0);
            return InteractionResult.FAIL;
        }
        return doPlace(player, card, heldStack, content, plantType, pos);
    }

    public static InteractionResult executeOnBlock(Player player, AbstractCardItem card, ItemStack heldStack, CardContent content,
                                                   UseOnContext ctx) {
        final IPlantType plantType = content.asPlantType();
        if (plantType == null) {
            notify(player, heldStack, CardErrors.INVALID_CONTENT, 0);
            return InteractionResult.FAIL;
        }
        final Level level = ctx.getLevel();
        final BlockPos clicked = ctx.getClickedPos();
        final boolean soilless = EnchantmentUtil.isSoilless(content.effectiveStack());

        if (plantType.hasTrait(PlantTrait.OUTER)) {
            notify(player, heldStack, PlantPlacementErrors.OUTER_ERROR, 0);
            return InteractionResult.FAIL;
        }
        if (plantType.hasTrait(PlantTrait.WATER)) {
            // 水盆视为水体：直接在水盆上方放置（射线只认水源方块，命中不到水盆）
            if (level.getBlockState(clicked).is(PvZBlockRegister.WATER_POT)
                    && (plantType != PVZPlants.CAT_TAIL.get() || soilless)) {
                return executeWater(player, card, heldStack, content, clicked.above());
            }
            if (plantType == PVZPlants.CAT_TAIL.get()) {
                if (soilless && isWaterSupport(level, clicked.above())) {
                    return executeWaterRaycast(player, card, heldStack, content);
                }
            } else if (!soilless || isWaterSupport(level, clicked.above())) {
                return executeWaterRaycast(player, card, heldStack, content);
            }
        }
        // 升级自方块来源（如猫尾草升级自睡莲）：先移除来源方块，再让新植物落在
        // 来源方块原位，使其脚下即为水/水盆等支撑，避免 spawn 在来源方块上方
        // 触发来源方块的 entityInside 破坏逻辑后失去支撑而枯萎掉血。
        boolean upgradeFromBlock = false;
        if (!soilless && !plantType.getUpgradeFrom().isEmpty()) {
            final boolean clickedIsFromBlock = plantType.getUpgradeFrom().stream()
                    .anyMatch(fromType -> fromType.getPlantBlock()
                            .map(block -> level.getBlockState(clicked).is(block))
                            .orElse(false));
            if (!clickedIsFromBlock) {
                notify(player, heldStack, PlantPlacementErrors.UPGRADE_ERROR, 0);
                return InteractionResult.FAIL;
            }
            upgradeFromBlock = true;
        }
        if (!soilless && !plantType.getPlacement().canPlaceOnBlockState(level.getBlockState(clicked))) {
            notify(player, heldStack, PlantPlacementErrors.GROUND_ERROR, 0);
            return InteractionResult.FAIL;
        }
        if (upgradeFromBlock) {
            // 替换来源方块：清空 clicked 格，新植物直接落在 clicked（原来源方块位置）。
            level.removeBlock(clicked, false);
            return doPlace(player, card, heldStack, content, plantType, clicked);
        }
        return doPlace(player, card, heldStack, content, plantType, clicked.above());
    }


    /* ------------------------------------------------------------------ */
    /* 前置检查                                                            */
    /* ------------------------------------------------------------------ */

    /**
     * 该位置能否为水生植物提供支撑：水方块或水盆。
     */
    private static boolean isWaterSupport(Level level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER)
                || level.getBlockState(pos).is(PvZBlockRegister.WATER_POT);
    }

    /** 种植数量限制的扫描半径（格）：以放置玩家为中心。 */
    private static final double PLANT_LIMIT_RADIUS = 30.0D;

    /**
     * 统计玩家附近 {@link #PLANT_LIMIT_RADIUS} 格内、同主人（owner UUID 匹配）的存活植物数量。
     * 所有由卡片种下的植物在 {@code onSpawnedByPlayer} 中写入 owner UUID，故可据此过滤。
     */
    private static int countOwnedPlantsNear(Player player) {
        final Level level = player.level();
        final AABB aabb = new AABB(
                player.getX() - PLANT_LIMIT_RADIUS, player.getY() - PLANT_LIMIT_RADIUS, player.getZ() - PLANT_LIMIT_RADIUS,
                player.getX() + PLANT_LIMIT_RADIUS, player.getY() + PLANT_LIMIT_RADIUS, player.getZ() + PLANT_LIMIT_RADIUS);
        final UUID ownerId = player.getUUID();
        int count = 0;
        for (PlantEntity plant : level.getEntitiesOfClass(PlantEntity.class, aabb)) {
            if (!plant.isRemoved() && plant.getOwnerUUID().filter(ownerId::equals).isPresent()) {
                ++count;
            }
        }
        return count;
    }

    /**
     * 种植数量上限 = 配置值 {@code LimitPlantCount} + 密集种植附魔加成（{@code getExtraPlantNum}）。
     * 附魔读自卡片有效 stack，与阳光/技能计算口径一致。
     */
    public static int getPlantCountLimit(CardContent content) {
        return ConfigUtil.getLimitPlantCount()
                + EnchantmentUtil.getExtraPlantNum(content.effectiveStack());
    }

    /**
     * 当前放置是否超出种植数量上限（附近同主人植物数 &gt;= 上限）。
     */
    public static boolean isOverPlantLimit(Player player, CardContent content) {
        if (player.isCreative()) return false;
        return countOwnedPlantsNear(player) >= getPlantCountLimit(content);
    }

    /**
     * 计算本次<strong>种植</strong>实际消耗的阳光：超出种植数量上限时翻倍（软限制，不拒绝放置）。
     * <p>检查（{@link #checkCommonForPlanting}）与扣费（{@code doPlace}）必须共用本方法，
     * 否则会出现"按翻倍价检查、按原价扣费"的不一致。
     */
    public static int getEffectiveSunCost(Player player, CardContent content, ItemStack heldStack) {
        final int base = (heldStack.getItem() instanceof AbstractCardItem card)
                ? card.getBasisSunCost(heldStack) : 0;
        return isOverPlantLimit(player, content) ? base * 2 : base;
    }

    /**
     * 种植专用前置检查：在通用检查之上追加"种植数量超限翻倍阳光"的校验。
     * 仅由 {@code AbstractCardItem.use/useOn} 的真实种植路径调用；
     * 与植物交互（治疗 / 升级 / 咖啡豆 / 墓碑吞噬者）互不影响。
     */
    public static boolean checkCommonForPlanting(Player player, ItemStack heldStack, CardContent content) {
        if (!checkCommon(player, heldStack, content)) return false;
        if (player.isCreative()) return true;

        final int base = (heldStack.getItem() instanceof AbstractCardItem card)
                ? card.getBasisSunCost(heldStack) : 0;
        if (isOverPlantLimit(player, content)) {
            final int doubled = base * 2;
            if (PlayerUtil.getResource(player, Resources.SUN_NUM) < doubled) {
                // 专用提示，避免玩家误解为何阳光消耗变高。
                notify(player, heldStack, CardErrors.MULTIPLE_SUN_ERROR, doubled);
                return false;
            }
        }
        return true;
    }

    public static boolean checkCommon(Player player, ItemStack heldStack, CardContent content) {
        if (!(heldStack.getItem() instanceof AbstractCardItem card)) return false;
        if (content.isEmpty()) {
            notify(player, heldStack, CardErrors.EMPTY_CONTENT, 0);
            return false;
        }
        if (!player.isCreative() && player.getCooldowns().isOnCooldown(heldStack)) {
            notify(player, heldStack, CardErrors.CD_ERROR, 0);
            return false;
        }
        if (card.isLocked(player, heldStack)) {
            notify(player, heldStack, CardErrors.LOCK_ERROR, 0);
            return false;
        }
        final int sunCost = card.getBasisSunCost(heldStack);
        if (!player.isCreative() && PlayerUtil.getResource(player, Resources.SUN_NUM) < sunCost) {
            notify(player, heldStack, CardErrors.SUN_ERROR, sunCost);
            return false;
        }
        return true;
    }
    /* ------------------------------------------------------------------ */
    /* 执行                                                                */
    /* ------------------------------------------------------------------ */

    private static InteractionResult doPlace(Player player, AbstractCardItem card,
                                             ItemStack heldStack, CardContent content,
                                             IPlantType plantType, BlockPos pos) {
        final Level level = player.level();
        // 必须在生成植物之前锁定阳光成本：否则新植物的 owner 会被计入统计，
        // 导致"检查时未超限、扣费时已超限"的前后不一致。
        final int sunCost = getEffectiveSunCost(player, content, heldStack);

        if (plantType.getPlantBlock().isPresent()) {
            final BlockState state = getBlockStateForPlacement(player, plantType);
            if (state == null) return InteractionResult.FAIL;
            PlantCardItem.handlePlantBlock(level, plantType, state, pos);
        } else {
            final Optional<PlantEntity> opt = spawnEntity(level, player, heldStack, content, pos);
            if (opt.isEmpty()) return InteractionResult.FAIL;
            final PlantEntity plant = opt.get();
            plant.applyCardData(card.getSkillsForPlant(heldStack),
                    card.getEnchantmentsForPlant(heldStack, level.registryAccess()));
            PlantCardItem.enchantPlantEntityByCard(plant, heldStack, level.registryAccess());
            plant.onSpawnedByPlayer(player, card.getBasisSunCost(heldStack));
        }

        if (!player.isCreative()) {
            // 与 checkCommon 同一口径：超出种植数量上限时阳光消耗翻倍（成本在生成前已锁定）。
            PlayerUtil.addResource(player, Resources.SUN_NUM, -sunCost);
        }
        card.onUsedSuccessfully(player, heldStack, content);
        card.fireSummonCardUseEvent(player, heldStack, content);
        return InteractionResult.SUCCESS;
    }
    private static @Nullable BlockState getBlockStateForPlacement(Player player, IPlantType plantType) {
        return plantType.getPlantBlock()
                .map(block -> block instanceof PvZLilyPadBlock lily
                        ? lily.getStateForPlacement(player)
                        : block.defaultBlockState())
                .orElse(null);
    }
    /**
     * 生成植物实体。返回 {@link Optional#empty()} 表示失败（非服务端、类型无实体、spawn 失败）。
     */
    private static Optional<PlantEntity> spawnEntity(Level level, Player player, ItemStack heldStack,
                                                     CardContent content, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return Optional.empty();
        }
        final Optional<EntityType<? extends Mob>> opt = content.spawnType().getEntityType();
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        final Mob mob = opt.get().spawn(serverLevel, heldStack, player, pos,
                EntitySpawnReason.MOB_SUMMONED, true, true);
        if (!(mob instanceof PlantEntity plant)) {
            return Optional.empty();
        }
        if (content.isImitater() && plant instanceof ImitaterEntity imitater) {
            imitater.setImitateCard(content.effectiveStack().copy());
        }
        return Optional.of(plant);
    }

    /* ------------------------------------------------------------------ */
    /* 反馈                                                                */
    /* ------------------------------------------------------------------ */

    private static void notify(Player player, ItemStack heldStack, ICardError error, int arg) {
        CardErrorNotifier.send(player,heldStack,error,arg);
    }
}