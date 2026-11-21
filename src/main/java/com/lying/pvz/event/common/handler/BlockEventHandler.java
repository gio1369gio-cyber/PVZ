package com.lying.pvz.event.common.handler;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

public class BlockEventHandler {
    /**
     * 挖紫水晶矿惊动周围末影人。
     */
    public static void triggerAmethystAround(BreakBlockEvent ev){
        if(ev.getState().is(PvZBlockRegister.AMETHYST_ORE.get()) && PlayerUtil.isValidPlayer(ev.getPlayer())){
            final float range = 10;
            final AABB aabb = EntityUtil.getEntityAABB(ev.getPlayer(), range, range);
            EntityUtil.getPredicateEntities(ev.getPlayer(), aabb, Enderman.class, (e) -> true)
                    .forEach(enderman ->{
                if(ev.getLevel().getRandom().nextFloat() < ConfigUtil.get(PvZConfig.LOCAL_CONFIG.BlockSettings.AmethystAngerChance)){
                    enderman.setTarget(ev.getPlayer());
                }
            });
        }
    }

    /**
     * 破坏草时有概率掉落豌豆 / 卷心菜种子。
     */
    public static void checkAndDropSeeds(BreakBlockEvent ev) {
        Player player = ev.getPlayer();
        BlockState state = ev.getState();
        BlockPos pos = ev.getPos();
            if(state.getBlock() == Blocks.SHORT_GRASS || state.getBlock() == Blocks.TALL_GRASS) {
                if(player.getRandom().nextDouble() < ConfigUtil.get(PvZConfig.LOCAL_CONFIG.BlockSettings.PeaDropChance)) {
                    player.level().addFreshEntity(new ItemEntity(player.level(), pos.getX(), pos.getY(), pos.getZ(),
                            new ItemStack(PvZItemRegister.PEA.get(), 1)));
                } else if(player.getRandom().nextDouble() < ConfigUtil.get(PvZConfig.LOCAL_CONFIG.BlockSettings.CabbageDropChance)) {
                    player.level().addFreshEntity(new ItemEntity(player.level(), pos.getX(), pos.getY(), pos.getZ(),
                            new ItemStack(PvZItemRegister.CABBAGE_SEEDS.get(), 1)));
                }
            }
    }
}
