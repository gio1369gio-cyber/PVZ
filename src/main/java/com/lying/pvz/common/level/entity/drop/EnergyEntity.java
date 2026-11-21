package com.lying.pvz.common.level.entity.drop;


import com.lying.pvz.PvZConfig;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EnergyEntity extends DropEntity {

    private int changeVTime = 50;

    public EnergyEntity(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.setAmount(1);
        this.setNoGravity(true);
    }

    @Override
    public void onCollectedByPlayer(Player player) {
        if (!this.level().isClientSide()) {
            PlayerUtil.addResource(player, Resources.ENERGY_NUM, this.getAmount());
            PlayerUtil.playClientSound(player, PvZSoundRegister.JEWEL_PICK.get());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getLiveTime() % this.changeVTime == 0) {
            double mult = 0.2f;
            Vec3 v = new Vec3(this.random.nextInt(1000) - 500,
                    this.random.nextInt(1000) - 500,
                    this.random.nextInt(1000) - 500).normalize();
            v = v.scale(mult);
            this.setDeltaMovement(v.x, v.y / 5, v.z);
        }
    }

    @Override
    protected int getMaxLiveTick() {
        return PvZConfig.LOCAL_CONFIG.EntitySettings.EntityLiveTick.EnergyLiveTick.get();
    }
}