package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantDefenderEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class SteelPumpkinZombieEntity extends AbstractZombotanyEntity implements IHasMetal {
    public SteelPumpkinZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) {
            attribute.setBaseValue(ZombieUtil.WALK_SLOW);
        }
    }

    @Override
    public IZombieType getZombieType() {
        return Zombotanies.STEEL_PUMPKIN_ZOMBIE.get();
    }

    @Override
    public MetalTypes getMetalType() {
        return MetalTypes.LADDER;
    }

    private static boolean hasLadderOnEntity(Entity entity) {
        if (!(entity instanceof PlantEntity plant)) {
            return false;
        }
        return plant instanceof PlantDefenderEntity && plant.hasMetal();
    }

    @Override
    public boolean hasMetal() {
        return this.getOuterDefenceLife() > 0;
    }

    public static boolean canTargetPutLadder(Entity target) {
        // Can not put ladder or already has ladder on.
        if (!(target instanceof PlantEntity plant)) {
            return false;
        }
        if (hasLadderOnEntity(plant)) {
            return false;
        }
        if (plant instanceof PlantDefenderEntity) {
            return true;
        }
        return plant.getOuterPlantInfo().isPresent();
    }

    public void putLadderOn(Entity entity) {
        if (entity instanceof PlantEntity plant) {
            plant.increaseMetal();
        }
        this.decreaseMetal();
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity entityIn) {
        if (!level().isClientSide()) {
            this.heal(20);
        }
        if (this.hasMetal() && canTargetPutLadder(entityIn)) {
            this.putLadderOn(entityIn);
        }
        return super.doHurtTarget(level, entityIn);
    }

    @Override
    public boolean canLostHand() {
        return super.canLostHand() && !this.hasMetal();
    }


    @Override
    protected void setBodyStates(ZombieDropBodyEntity body) {
        super.setBodyStates(body);
        body.setHandDefence(this.hasMetal());
    }

    @Override
    public void decreaseMetal() {
        this.setOuterDefenceLife(0);
    }

    @Override
    public void increaseMetal() {
        this.setOuterDefenceLife(this.getOuterLife());
    }

    @Override
    public float getLife() {
        return 125;
    }

    @Override
    public float getOuterLife() {
        return 175;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return PvZSoundRegister.METAL_HIT.get();
    }
}