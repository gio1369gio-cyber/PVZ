package com.lying.pvz.event.common.gamebus;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.zombie.other.CoffinEntity;
import com.lying.pvz.event.common.handler.BlockEventHandler;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.function.Predicate;

@EventBusSubscriber(modid = PvZMod.MOD_ID)
public class PvZBlockEvents {

    @SubscribeEvent
    public static void onPlayerBreakBlock(BreakBlockEvent ev) {
        if (ev.isCanceled()) return;
        if (!ev.getPlayer().level().isClientSide()) {
            BlockEventHandler.checkAndDropSeeds(ev);
            BlockEventHandler.triggerAmethystAround(ev);
        }
    }

    /**
     *
     * 把树苗下方的方块替换成 Origin Ore
     */
    @SubscribeEvent
    public static void onTreeGrow(BlockGrowFeatureEvent ev) {
        if (!ev.getLevel().isClientSide()) {
            final boolean isBlocked = !ev.getLevel().isEmptyBlock(ev.getPos().above());
            if (isBlocked) {
                final double chance = ConfigUtil.get(PvZConfig.LOCAL_CONFIG.BlockSettings.SaplingTurnChance);
                if (MathUtil.randDouble(ev.getRandom(), isBlocked ? chance * 1.2 : chance) && ev.getPos().getY() > 2) {
                    ev.setCanceled(true);
                    ev.getLevel().setBlock(ev.getPos().below(), PvZBlockRegister.ORIGIN_ORE.get().defaultBlockState(), 3);
                }
            }
        }
    }
    private static final BlockPattern COFFIN_PATTERN = BlockPatternBuilder.start()
            .aisle("~^~", "#^#", "~^~")
            .where('#', stateIs(BlockState::isAir))
            .where('^', stateIs(s -> s.is(Blocks.OBSIDIAN)))
            .where('~', stateIs(s -> s.is(Blocks.WITHER_ROSE)))
            .build();

    /** b 或 b.getState() 为 null（chunk 未加载）时返回 false，视为不匹配。 */
    private static Predicate<BlockInWorld> stateIs(Predicate<BlockState> predicate) {
        return b -> {
            if (b == null) return false;
            final BlockState state = b.getState();
            return state != null && predicate.test(state);
        };
    }
    /**
     *
     * 召唤COFFIN
     */
    @SubscribeEvent
    public static void onBlockPlacedBy(BlockEvent.EntityPlaceEvent ev) {
        if (ev.isCanceled()) return;
        if (!ev.getPlacedBlock().is(Blocks.WITHER_ROSE)) return;
        if (!(ev.getLevel() instanceof ServerLevel level)) return;

        final BlockPos pos = ev.getPos();
        if (pos.getY() < 2) return;
        if (level.getDifficulty() == Difficulty.PEACEFUL) return;

        final BlockPattern.BlockPatternMatch match = COFFIN_PATTERN.find(level, pos);
        if (match == null) return;

        // 1) 清空图案方块
        //    flag = 3 = UPDATE_NEIGHBORS | UPDATE_CLIENTS
        //    setBlock 内部会自动 updateNeighborsAt，不需要手动 blockUpdated
        for (int x = 0; x < match.getWidth(); x++) {
            for (int y = 0; y < match.getHeight(); y++) {
                final BlockInWorld b = match.getBlock(x, y, 0);
                final BlockState oldState = b.getState();
                if (oldState == null) continue;              // ← null 防御

                level.setBlock(b.getPos(), Blocks.AIR.defaultBlockState(), 3);
                level.levelEvent(2001, b.getPos(), Block.getId(oldState));
            }
        }

        // 2) 召唤 Coffin
        final CoffinEntity coffin = PvZEntityRegister.COFFIN.get()
                .create(level, EntitySpawnReason.MOB_SUMMONED);
        if (coffin == null) return;

        final BlockPos spawnPos = match.getBlock(1, 2, 0).getPos();
        final float yRot = match.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;

        EntityUtil.onEntitySpawn(level, coffin, spawnPos);
        coffin.snapTo(spawnPos.getX() + 0.5, spawnPos.getY() + 0.55, spawnPos.getZ() + 0.5, yRot, 0.0F);
        coffin.yBodyRot = yRot;

        // 3) 召唤成就
        for (ServerPlayer sp : level.getEntitiesOfClass(ServerPlayer.class,
                coffin.getBoundingBox().inflate(50.0D))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(sp, coffin);
        }

    }






}
