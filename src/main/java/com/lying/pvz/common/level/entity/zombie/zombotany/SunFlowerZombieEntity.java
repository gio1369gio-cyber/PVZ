package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.RedSunEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;
import java.util.Objects;

public class SunFlowerZombieEntity extends AbstractZombotanyEntity {
    private int producTick = 0;

    public SunFlowerZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) {
            attribute.setBaseValue(0.25);
        }
    }

    @Override
    public void normalZombieTick() {
        super.normalZombieTick();
        if (!this.level().isClientSide()) {
            ++this.producTick;
            if (this.producTick >= this.getFixedProducCD()) {
                this.setAttackTime(1);
                this.producTick = 0;
            }

            if (this.getAttackTime() > 0) {
                this.setAttackTime(this.getAttackTime() - 1);
                this.genSomething();
            }
        }
    }

    public float getAttackDamage() {
        return 2.0F;
    }

    @Override
    public float getLife() {
        return 20.0F;
    }

    protected int getFixedProducCD() {
        int now = this.getShootCD();
        if (this.hasEffect(PvZEffectRegister.COLD_EFFECT)) {
            int lvl = Objects.requireNonNull(this.getEffect(PvZEffectRegister.COLD_EFFECT)).getAmplifier();
            now += 3 * lvl;
        }

        return now;
    }

    protected int getShootCD() {
        return 120;
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.producTick = input.getIntOr("produc_tick", this.producTick);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("produc_tick", this.producTick);
    }

    public int getGenCD() {
        return this.level().isBrightOutside() ? (this.level().isRaining() ? 500 : 250) : 750;
    }

    @Override
    public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
        super.addAlmanacEntries(list);
        list.add(Pair.of(PAZAlmanacs.GEN_CD, this.getGenCD()));
    }

    protected void genSun(int num, int cnt) {
        RedSunEntity.spawnSunsByAmount(this.level(), this.blockPosition(), num, num / cnt, 2.0);
        EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
    }

    public void genSomething() {
        RedSunEntity sun = PvZEntityRegister.RED_SUN.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
        if (sun != null) {
            sun.setAmount(sun.getAmount());
            EntityUtil.onEntityRandomPosSpawn(this.level(), sun, this.blockPosition(), 1);
        }
        EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
    }

    @Override
    public IZombieType getZombieType() {
        return Zombotanies.SUN_FLOWER_ZOMBIE.get();
    }
}