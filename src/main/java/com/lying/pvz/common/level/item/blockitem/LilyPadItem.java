package com.lying.pvz.common.level.item.blockitem;

import com.lying.pvz.common.init.PvZBlockRegister;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * 荷叶。
 * <p>
 * 继承 {@link BlockItem}，绑定 {@link PvZBlockRegister#LILY_PAD}。
 * 仅当玩家视线命中水源方块时，将放置位置重定向到水源正上方再走标准放置流程。
 */
public class LilyPadItem extends BlockItem {

    public LilyPadItem(Properties properties) {
        super(PvZBlockRegister.LILY_PAD.get(),properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }


    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (result.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        BlockHitResult result1 = result.withPosition(result.getBlockPos().above());
        return super.useOn(new UseOnContext(player, usedHand, result1));
    }


}
