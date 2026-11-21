package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.JewelEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * EDGAR-090517（强化版 EDGAR BOSS）。
 *
 * <p>继承 {@link Edgar090505Entity}，本体 1000 + 内层 11000，碰撞 2×9。
 * 五个阶段对应不同的召唤、偷取、投车、元素球冷却与弹速，
 * 通过覆盖 {@link #getShootBallCD()} 等回调实现，不新增同步字段。
 *
 * <p>额外参数：
 * <ul>
 *   <li>{@code shootAutoBall = true}：元素球自动锁定；</li>
 *   <li>{@link #getSpawnCount()} = {@code 在线玩家数 + 1}；</li>
 *   <li>掉落奖励 {@code 4 + 3 * playerCnt}。</li>
 * </ul>
 */
public class Edgar090517Entity extends Edgar090505Entity {

    public Edgar090517Entity(EntityType<? extends Edgar090505Entity> type, Level worldIn) {
        super(type, worldIn);
        this.refreshCountCD = 10;
        this.maxZombieSurround = 60;
        this.maxPlantSurround = 45;
        this.kickRange = 6;
        this.setIsWholeBody();
    }

    @Override
    protected void spawnSpecialDrops() {
        final int playerCnt = this.bossInfo.getPlayers().size();
        for (int i = 0; i < 4 + 3 * playerCnt; ++i) {
            JewelEntity jewel = PvZEntityRegister.JEWEL.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
            if (jewel == null) {return;}
            EntityUtil.onEntityRandomPosSpawn(this.level(), jewel, this.blockPosition().above(5), 4);
        }
    }

    @Override
    public int getBossStage() {
        final float percent = this.bossInfo.getProgress();
        return percent > 4F / 5 ? 1 :
                percent > 3F / 5 ? 2 :
                        percent > 2F / 5 ? 3 :
                                percent > 1F / 5 ? 4 : 5;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(2F, 9F);
    }

    @Override
    public boolean shootAutoBall() {
        return true;
    }

    @Override
    public int getShootBallCD() {
        return this.getBossStage() < 2 ? 700 : this.getBossStage() < 4 ? 500 : 300;
    }

    @Override
    public int getStealPlantCD() {
        return this.getBossStage() < 3 ? 600 : 400;
    }

    @Override
    public float getElementBallSpeed() {
        return this.getBossStage() < 3 ? 0.18F : this.getBossStage() < 5 ? 0.21F : 0.23F;
    }

    @Override
    public int getSpawnCount() {
        return this.bossInfo.getPlayers().size() + 1;
    }

    @Override
    public float getWalkSpeed() {
        return 0;
    }

    @Override
    public float getEatDamage() {
        return ZombieUtil.LITTLE_HIGH;
    }

    @Override
    public float getLife() {
        return 1000;
    }

    @Override
    public float getInnerLife() {
        return 11000;
    }

    @Override
    public IZombieType getZombieType() {
        return RoofZombies.EDGAR_090517.get();
    }
}