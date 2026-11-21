package com.lying.pvz.client.render.state.entity.plant;

import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.common.level.entity.plant.arma.BurstKernelPultEntity;
import com.lying.pvz.common.level.entity.plant.arma.KernelPultEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import java.util.List;

/**
 * 植物通用渲染状态。
 *
 * <p>所有植物共用一个 state 类，避免 100+ 个子类爆炸。
 * 字段不区分具体模型（PeaShooter 用不到的字段保持默认值）。
 */
public class PvZPlantRenderState extends LivingEntityRenderState {


    public Identifier plantTypeId;
    public Identifier texture;
    public float renderScale = 1.0F;

    public int existTick;

    public boolean superMode;
    public int superTime;
    public int shootTick;
    public int shootCD;

    public int attackTime;
    public int pultAnimTime;

    public boolean eatingTomb;

    public int eatTombCD;

    public float percentHealth;

    public int readyTime;

    public int attackCD;

    public boolean mineReady;
    public boolean risingFromDirt;
    public int prepareCD;

    public int growStage;

    public float cactusHeight;

    public boolean scared;

    public int scareTime;

    public double innerDefenceLife;

    public double outerDefenceLife;

   public boolean charmed;

   public  int boostTime;

   public boolean metal;

    public boolean cold;
    public boolean frozen;
    public boolean buttered;
    public List<Mark.View> marks = List.of();
    public int statusParticleTick = -1;

    public int lightTick;

    public boolean sunLight;

    public final ItemStackRenderState metalItem = new ItemStackRenderState();

    public boolean hasMetalItem;

    public float metalScale = 1.0F;
    public int animTime;
    public int animCD;
    public int roundTick;
    public boolean isPlantInSuperMode;

    public KernelPultEntity.CornTypes currentBullet;

    public BurstKernelPultEntity.CornTypes currentBullet1;

    public boolean isMagnetActive;
    public MetalTypes metalType;
    public int restTick;
    public RandomSource random;
    public boolean isPlayerRiding;
    public int cornNum;

    public int signChangeCD;

    public boolean isResting;
    public boolean isCactusPowered;
    public int spikeNumCount;
    public int spikeNum;
    public int count;
    public boolean isAlive;
    public boolean isImitating;
    public int imitaterChance;
    public boolean isSolid;

    /** 是否被钢南瓜套住（STEEL_PUMPKIN_FLAG），用于区分南瓜/钢南瓜外层装甲。 */
    public boolean steelPumpkin;

    public boolean magnifyingBeam;
    public float beamLength;
    public float beamPitch;
    public float beamYaw;
    public float beamEyeHeight;
}