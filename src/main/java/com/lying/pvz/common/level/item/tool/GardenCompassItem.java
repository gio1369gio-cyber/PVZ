package com.lying.pvz.common.level.item.tool;

import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.RandomState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;

public class GardenCompassItem extends Item {

    public GardenCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return super.use(level, player, hand);
        }
        if (!level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof GardenCompassItem) {
                setPos(stack, getPointPosition(player));
            }
        }
        return InteractionResult.SUCCESS;
    }

    // ===== 组件读写 =====

    @Nullable
    public static BlockPos getPos(ItemStack stack) {
        return stack.get(PvZDataComponents.COMPASS_TARGET.get());
    }

    public static ItemStack setPos(ItemStack stack, @Nullable BlockPos pos) {
        stack.set(PvZDataComponents.COMPASS_TARGET.get(), pos);
        return stack;
    }

    // ===== Biome 查找 =====

    @Nullable
    private static BlockPos getPointPosition(LivingEntity player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        BlockPos origin = BlockPos.containing(player.position());
        RandomState randomState = serverLevel.getChunkSource().randomState();

        Pair<BlockPos, Holder<Biome>> result = serverLevel.getChunkSource()
                .getGenerator()
                .getBiomeSource()
                .findBiomeHorizontal(
                        origin.getX(), origin.getY(), origin.getZ(),
                        6400,
                        8,
                        holder -> holder.is(PvZBiome.ZEN_GARDEN),
                        serverLevel.getRandom(),
                        true,
                        randomState);

        return result == null ? null : result.getFirst();
    }

    // ===== 维度切换时重置坐标 =====

    @SubscribeEvent
    public static void disturb(PlayerEvent.PlayerChangedDimensionEvent ev) {
        Player player = ev.getEntity();
        for (ItemStack item : player.getInventory().getNonEquipmentItems()) {
            if (item.getItem() instanceof GardenCompassItem) {
                setPos(item, null);
            }
        }
    }
}