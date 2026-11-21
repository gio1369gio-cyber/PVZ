package com.lying.pvz.event.common.handler;

import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.api.entity.zombie.IZombieEntity;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class LivingEventHandler {

    public static void handleHurtEffects(LivingEntity target, PvZEntityDamageSource source) {
        if (!source.isDefended()) { // source not defended by armor.
            if (source.isFlameDamage()) {
                if (target.hasEffect(PvZEffectRegister.COLD_EFFECT)) {
                    target.removeEffect(PvZEffectRegister.COLD_EFFECT);
                }
                if (target.hasEffect(PvZEffectRegister.FROZEN_EFFECT)) {
                    target.removeEffect(PvZEffectRegister.FROZEN_EFFECT);
                }
            }
            source.getEffects().forEach(effect -> EntityUtil.addPotionEffect(target, effect));
        }
    }

    public static void handleHurtSounds(LivingEntity target, PvZEntityDamageSource source) {
        if (source.isEatDamage()) {
            EntityUtil.playSound(target, PvZSoundRegister.CHOMP.get());
        }
        if (source.isFlameDamage() && source.isAppease()) {
            EntityUtil.playSound(target, PvZSoundRegister.FLAME_HIT.get());
        }
    }

    public static void handleHurtDamage(final LivingDamageEvent.Pre ev) {

        if (ev.getSource() instanceof PvZEntityDamageSource
                && !(ev.getEntity() instanceof IPAZEntity && ev.getSource().getEntity() instanceof IPAZEntity)) {
            ev.setNewDamage(Math.min(ConfigUtil.getLimitDamage(), ev.getNewDamage()));
        }

        if (ev.getEntity() instanceof IZombieEntity
                && ev.getSource().getEntity() instanceof IZombieEntity
                && (!(ev.getSource().getEntity() instanceof AbstractBossZombieEntity)
                || (ev.getEntity() instanceof AbstractBossZombieEntity))) {
            ev.setNewDamage(Math.min(100, ev.getNewDamage()));
        }
        if (!ev.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)
                && ev.getNewDamage() > ev.getEntity().getMaxHealth() * 0.8
                && ev.getEntity() instanceof AbstractBossZombieEntity) {
            ev.setNewDamage(ConfigUtil.getLimitDamage());
        }
    }


}
