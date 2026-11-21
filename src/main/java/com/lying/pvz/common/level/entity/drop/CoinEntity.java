package com.lying.pvz.common.level.entity.drop;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CoinEntity extends DropEntity {

    public static final int COIN_TYPES = 3;

    public CoinEntity(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.setAmountByType(this.getRandomType());
    }

    @Override
    protected void onDropped() {
        super.onDropped();
        if (!level().isClientSide()) {
            EntityUtil.playSound(this, PvZSoundRegister.COIN_DROP.get());
        }
    }

    @Override
    public void onCollectedByPlayer(Player player) {
        if (!this.level().isClientSide()) {
            PlayerUtil.addResource(player, Resources.MONEY, this.getAmount());
            PlayerUtil.playClientSound(player, PvZSoundRegister.COIN_PICK.get());
        }
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose poseIn) {
        final float t = (float) Math.log10(this.getAmount());
        final float w = t * 0.18f + 0.4f;
        return EntityDimensions.fixed(w, w);
    }

    /** spawn coin by type. */
    public static void spawnCoin(Level world, BlockPos pos, CoinType type) {
        final CoinEntity coin = PvZEntityRegister.COIN.get().create(world, EntitySpawnReason.MOB_SUMMONED);
        if (coin == null) return;
        coin.setAmount(type.money);
        EntityUtil.onEntitySpawn(world, coin, pos);
    }

    public void setAmountByType(CoinType type) {
        this.setAmount(type.money);
    }

    protected CoinType getRandomType() {
        return CoinType.values()[this.random.nextInt(COIN_TYPES)];
    }

    @Override
    protected int getMaxLiveTick() {
        return PvZConfig.LOCAL_CONFIG.EntitySettings.EntityLiveTick.CoinLiveTick.get();
    }

    public enum CoinType {
        COPPER(1), SILVER(10), GOLD(100);
        public final int money;
        CoinType(int money) { this.money = money; }
    }
}