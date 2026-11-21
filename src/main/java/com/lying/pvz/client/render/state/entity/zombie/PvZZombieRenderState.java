package com.lying.pvz.client.render.state.entity.zombie;

import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.entity.zombie.base.EdgarRobotEntity;
import com.lying.pvz.common.level.entity.zombie.roof.GargantuarEntity;
import com.lying.pvz.api.projectile.Mark;
import it.unimi.dsi.fastutil.bytes.AbstractByte2FloatSortedMap;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 僵尸通用渲染状态。
 */
public class PvZZombieRenderState extends LivingEntityRenderState {

    /* 基础 */
    public @Nullable Identifier zombieTypeId;
    public @Nullable Identifier texture;
    public float renderScale = 1.0F;

    public int animTime;
    public int animShootCD;
    public int animStealCD;
    public int animThrowCD;
    public int risingAnimTime;
    public boolean aggressive;
    public boolean hasHand;
    public boolean hasHead;
    public boolean hasHandDefence;
    public boolean mini;
    public boolean lightEyeEffect;
    public int attackTime;
    public boolean hasMetal;
    public boolean carShaking;
    public  BlockPos ropeOriginPos;
    public @Nullable Vec3 ropeEyePos;
    public int ropeStartBlockLight;
    public int ropeEndBlockLight;
    public int ropeStartSkyLight;
    public int ropeEndSkyLight;
    public double innerDefenceLife;
    public double outerDefenceLife;

    public ZombieEntity.VariantType variantType = ZombieEntity.VariantType.NORMAL;
    public boolean charmed;
    public boolean cold;
    public boolean frozen;
    public boolean buttered;
    public List<Mark.View> marks = List.of();
    public int statusParticleTick = -1;
    public boolean sunLight;

    public int innerDefenceStage;
    public int summonTime;
    /** SphinxTerminator：冲刺朝向（度），驱动模型冲刺转向动画。 */
    public float dashYaw;
    public boolean isAngry;
    public int poleJumpCD;
    public boolean hasPole;
    public float percentHealth;
    public boolean isRightShake;

    public boolean hasBalloon;
    public boolean hasPickaxe;
    public boolean hasPogo;
    public @Nullable Vec3 deltaMovement ;
    public @Nullable List<Entity> passengers;
    public float stayTime;
    public float ballCount;
    public float maxBallUse;
    public EdgarRobotEntity.EdgarStates robotState = EdgarRobotEntity.EdgarStates.NORMAL;

    public int crushCD;
    public boolean isSad;
    public boolean hasImp;
    public GargantuarEntity.GargantuarType toolType = GargantuarEntity.GargantuarType.POLE;
    public int pultAnimTime;
    public boolean hasLine;
    public boolean isResting;
    public RandomSource random;
    public int existTick;
    public boolean canPartsExist;
}