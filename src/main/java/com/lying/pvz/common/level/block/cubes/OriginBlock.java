package com.lying.pvz.common.level.block.cubes;


import com.lying.pvz.PvZConfig;
import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.event.origin.OriginEffectEvent;
import com.lying.pvz.common.impl.PVZRegistryBootstrap;
import com.lying.pvz.common.level.entity.effect.OriginEffectEntity;
import com.lying.pvz.utils.enums.Colors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;

public class OriginBlock extends Block {

    private static final Map<Block, IEssenceType> BLOCK_TO_ESSENCE = new HashMap<>();
    private static final int RADIATION_RANGE = 1;

    public OriginBlock(Properties properties) {
        super(properties);
    }

    /**
     * update map when tags change.
     */
    public static void updateRadiationMap() {
        BLOCK_TO_ESSENCE.clear();
        PVZRegistryBootstrap.ESSENCES.getEntries().forEach(holder -> {
            final IEssenceType essence = holder.get();

            essence.getRadiationBlock().ifPresent(block ->
                    BLOCK_TO_ESSENCE.put(block, essence));

            essence.getRadiationBlockTag().ifPresent(tag ->
                    BuiltInRegistries.BLOCK.getTagOrEmpty(tag)
                            .forEach(h -> BLOCK_TO_ESSENCE.put(h.value(), essence)));
        });
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!level.isClientSide()) {
            if (!level.isAreaLoaded(pos, 3)) return;
            final int r = RADIATION_RANGE;
            boolean success = false;
            for (int i = -r; i <= r; ++i) {
                for (int j = -r; j <= r; ++j) {
                    for (int k = -r; k <= r; ++k) {
                        if (random.nextDouble() < PvZConfig.LOCAL_CONFIG.BlockSettings.OriginEffectChance.get()) {
                            success |= checkAndGrow(level, pos.getX() + i, pos.getY() + j, pos.getZ() + k);
                        }
                    }
                }
            }
            if (success) {
                OriginEffectEntity.create(level, pos, Colors.GREEN);
            }
        }
    }

    /**
     * check specific block and grow if matched.
     */
    private boolean checkAndGrow(Level level, int x, int y, int z) {
        final BlockPos pos = new BlockPos(x, y, z);
        final BlockState blockstate = level.getBlockState(pos);
        final Block block = blockstate.getBlock();
        if (BLOCK_TO_ESSENCE.containsKey(block)) {
            final OriginEffectEvent event = new OriginEffectEvent(level, pos);
            NeoForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                level.setBlockAndUpdate(pos, BLOCK_TO_ESSENCE.get(block).getEssenceOre().defaultBlockState());
                return true;
            }
        }
        return false;
    }
}