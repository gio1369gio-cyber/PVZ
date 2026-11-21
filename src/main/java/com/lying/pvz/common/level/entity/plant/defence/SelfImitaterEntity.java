package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantDefenderEntity;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlantUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Arrays;
import java.util.List;

/**
 * 自我模仿者（防御型植物）。
 * 血量低于 20% 且仍有模仿次数时，被非碾压类实体伤害命中会在原地生成一株继承数据的分身，
 * 分身消耗一次模仿次数、原株随后移除；进入超级模式可额外获得模仿次数奖励。
 */
public class SelfImitaterEntity extends PlantDefenderEntity {

    /** 当前剩余模仿次数；-1 表示尚未初始化，首次访问时从技能 IMITATER_CHANCE 惰性读取。 */
    protected int imitaterChance = -1;

    public SelfImitaterEntity(EntityType<? extends PlantDefenderEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public float getLife() {
        if (this.getImitaterChance() > 0) {
            return 100F + this.getImitaterChance() * 10F;
        }
        return 100F;
    }

    @Override
    public float getSuperLife() {
        return this.getLife() / 1.5F;
    }

    protected int getSuperBonusChance() {
        return 3;
    }

    @Override
    public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
        super.addAlmanacEntries(list);
        list.addAll(Arrays.asList(
                Pair.of(PAZAlmanacs.HEALTH, this.getLife()),
                Pair.of(PAZAlmanacs.AGAIN_CHANCE, this.getSuperBonusChance())
        ));
    }


    public boolean isImitating() {
        return this.canNormalUpdate() && this.getHealth() / this.getMaxHealth() <= 0.2F;
    }


    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (this.isImitating() && this.getImitaterChance() > 0) {
            EntityUtil.playSound(this, PvZSoundRegister.WAKE_UP.get());
            if (source instanceof PvZEntityDamageSource pvzSource && !pvzSource.isCrushDamage()) {
                this.imitate();
            }
            // hurtServer 只在服务端执行，原先写在 client 分支里的爆炸粒子改用 sendParticles 下发，保留原有受击视觉反馈。
            serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                    this.position().x, this.position().y, this.position().z,
                    1, 0.01, 0.01, 0.01, 0.0);
        }
        return super.hurtServer(serverLevel, source, amount);
    }

    /**
     * 触发模仿：在原位置生成一株携带相同数据的分身，分身模仿次数减一并转移骑乘关系与攻击目标，
     * 最后移除自身。
     */
    public void imitate() {
        if (this.getImitaterChance() > 0 && !this.level().isClientSide()) {
            this.getOwnerPlayer().ifPresent(player -> PlantCardItem.handlePlantEntity(
                    player, this.getPlantType(), PvZItemRegister.SELF_IMITATER_CARD.get().getDefaultInstance(),
                    this.blockPosition(), (plantEntity) -> {
                        PlantUtil.copyPlantData(this, plantEntity);
                        if (plantEntity instanceof SelfImitaterEntity imitater) {
                            imitater.setImitaterChance(this.getImitaterChance() - 1);
                        }
                        if (this.getVehicle() != null) {
                            this.stopRiding();
                            plantEntity.startRiding(this.getVehicle());
                        }
                        if (this.getTarget() != null) {
                            plantEntity.setTarget(this.getTarget());
                        }
                        this.discard();
                    }));
        }
    }

    public int getImitaterChance() {
        if (this.imitaterChance < 0) {
            this.imitaterChance = (int) this.getSkillValue(SkillTypes.IMITATER_CHANCE.get());
        }
        return this.imitaterChance;
    }

    public void setImitaterChance(int imitaterChance) {
        this.imitaterChance = imitaterChance;
    }


    @Override
    public void onSuperStart(boolean first) {
        super.onSuperStart(first);
        if (!this.level().isClientSide()) {
            this.setImitaterChance(this.getImitaterChance() + this.getSuperBonusChance());
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.imitaterChance = input.getIntOr("imitater_chance", this.imitaterChance);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("imitater_chance", this.imitaterChance);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(0.7F, 1.25F);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.SELF_IMITATER.get();
    }
}
