package com.lying.pvz.common.level.entity.drop;


import com.lying.pvz.PvZConfig;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class JewelEntity extends DropEntity {

    public JewelEntity(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.setAmount(1);
    }

    @Override
    protected void onDropped() {
        super.onDropped();
        if (!level().isClientSide()) {
            EntityUtil.playSound(this, PvZSoundRegister.JEWEL_DROP.get());
        }
    }

    @Override
    public void onCollectedByPlayer(Player player) {
        if (!this.level().isClientSide()) {
            PlayerUtil.addResource(player, Resources.GEM_NUM, this.getAmount());
            PlayerUtil.playClientSound(player, PvZSoundRegister.JEWEL_PICK.get());
        }
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(0.9f, 0.9f);
    }

    @Override
    protected int getMaxLiveTick() {
        return PvZConfig.LOCAL_CONFIG.EntitySettings.EntityLiveTick.JewelLiveTick.get();
    }
}