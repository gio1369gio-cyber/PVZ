package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.JewelEntity;
import com.lying.pvz.common.level.entity.zombie.base.EdgarRobotEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * EDGAR-090505（弱化版 EDGAR BOSS）。
 *
 * <p>继承 {@link EdgarRobotEntity}，差异：
 * <ul>
 *   <li>本体 1000 + 内层 4000，碰撞 2×7.5；</li>
 *   <li>{@code refreshCountCD = 10}，{@code kickRange = 6}；</li>
 *   <li>{@link #zombieTick()} 定期把自身锚定到 {@code ORIGIN_POS}，超出 10 格时
 *       重置周围 4 格半径地形（下层草方块、上层空气），并传送回锚点；</li>
 *   <li>掉落奖励按在线玩家数计算：{@code 2 + 2 * playerCnt} 颗宝石。</li>
 * </ul>
 *
 * <p>阶段划分见 {@link #getBossStage()}，进入下一阶段后 {@code getSpawnCount} 变化。
 */
public class Edgar090505Entity extends EdgarRobotEntity {

    private static final EntityDataAccessor<BlockPos> ORIGIN_POS =
            SynchedEntityData.defineId(Edgar090505Entity.class, EntityDataSerializers.BLOCK_POS);

    public Edgar090505Entity(EntityType<? extends EdgarRobotEntity> type, Level worldIn) {
        super(type, worldIn);
        this.refreshCountCD = 10;
        this.maxZombieSurround = 40;
        this.maxPlantSurround = 50;
        this.kickRange = 6;
        this.setIsWholeBody();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ORIGIN_POS, BlockPos.ZERO);
    }

    @Override
    public void kill(ServerLevel level) {
        super.kill(level);
    }

    @Override
    public void zombieTick() {
        super.zombieTick();
        if (!this.level().isClientSide()) {
            if (this.getOriginPos().equals(BlockPos.ZERO)) {
                this.setOriginPos(this.blockPosition());
            } else {
                if (MathUtil.getPosDisToVec(this.getOriginPos(), this.position()) >= 10) {
                    final int range = 4;
                    for (int i = -range; i <= range; ++i) {
                        for (int j = -range; j <= range; ++j) {
                            final BlockPos tmp = this.getOriginPos().offset(i, -1, j);
                            if (this.level().getBlockState(tmp).isAir()) {
                                this.level().setBlockAndUpdate(tmp, Blocks.GRASS_BLOCK.defaultBlockState());
                            }
                            for (int k = 0; k <= 10; ++k) {
                                this.level().setBlockAndUpdate(this.getOriginPos().offset(i, k, j),
                                        Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                    this.setPos(this.getOriginPos().getX(), this.getOriginPos().getY() + 1,
                            this.getOriginPos().getZ());
                }
            }
        }
    }

    @Override
    public int getBossStage() {
        final float percent = this.bossInfo.getProgress();
        return percent > 3F / 4 ? 1 :
                percent > 2F / 4 ? 2 :
                        percent > 1F / 4 ? 3 : 4;
    }

    @Override
    protected void spawnSpecialDrops() {
        final int playerCnt = this.bossInfo.getPlayers().size();
        for (int i = 0; i < 2 + 2 * playerCnt; ++i) {
            JewelEntity jewel = PvZEntityRegister.JEWEL.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
            if (jewel == null) {return;}
            EntityUtil.onEntityRandomPosSpawn(this.level(), jewel, this.blockPosition().above(5), 4);
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(2F, 7.5F);
    }

    @Override
    public int getSpawnCount() {
        return (this.bossInfo.getPlayers().size() + 1) / 2 + 1;
    }

    @Override
    public float getWalkSpeed() {
        return 0;
    }

    @Override
    public float getEatDamage() {
        return ZombieUtil.NORMAL_DAMAGE;
    }

    @Override
    public float getLife() {
        return 1000;
    }

    @Override
    public float getInnerLife() {
        return 4000;
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setOriginPos(new BlockPos(
                input.getIntOr("origin_pos_x", 0),
                input.getIntOr("origin_pos_y", 0),
                input.getIntOr("origin_pos_z", 0)));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("origin_pos_x", this.getOriginPos().getX());
        output.putInt("origin_pos_y", this.getOriginPos().getY());
        output.putInt("origin_pos_z", this.getOriginPos().getZ());
    }

    public BlockPos getOriginPos() {
        return this.entityData.get(ORIGIN_POS);
    }

    public void setOriginPos(BlockPos pos) {
        this.entityData.set(ORIGIN_POS, pos);
    }

    @Override
    public IZombieType getZombieType() {
        return RoofZombies.EDGAR_090505.get();
    }
}