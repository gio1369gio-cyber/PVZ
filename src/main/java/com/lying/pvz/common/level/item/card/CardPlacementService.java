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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Optional;

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
            PlayerUtil.addResource(player, Resources.SUN_NUM, -card.getBasisSunCost(heldStack));
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