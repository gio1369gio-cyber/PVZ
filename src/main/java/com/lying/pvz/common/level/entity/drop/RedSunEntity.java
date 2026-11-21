package com.lying.pvz.common.level.entity.drop;

import com.lying.pvz.common.attachment.player.PlayerStats;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class RedSunEntity extends SunEntity {
    public RedSunEntity(EntityType<? extends SunEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setAmount(this.getDefaultAmount());
        this.setNoGravity(true);
    }

    public Vec3 ColorBase = new Vec3(240.0, 24.0, 24.0);

    @Override
    protected int getDefaultAmount() {
        return -25;
    }

    public int getIcon() {
        int value = -(this.getAmount());
        return value < 6 ? 0 : (value < 16 ? 1 : (value < 26 ? 2 : 3));
    }

    public void tick() {
        super.tick();
        if ((this.tickCount + this.getId()) % 40 == 0) {
            Player following = this.level().getNearestPlayer(this, 32.0);
            if (EntityUtil.isEntityValid(following)) {
                this.giveSunToTarget(following);
            }
        }
    }

    private void giveSunToTarget(Player player) {
        if (!level().isClientSide()) {
            PlayerStats stats =  player.getData(PvZAttachments.PLAYER_STATS);
            int amount = this.getAmount();
            stats.add(Resources.SLOT_NUM,amount);
            PlayerUtil.playClientSound(player, SoundEvents.PLAYER_HURT_ON_FIRE);
        }
    }
}