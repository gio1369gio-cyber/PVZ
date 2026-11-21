package com.lying.pvz.compat.jade.provider;


import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.entity.ability.IHasOwner;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.compat.jade.JadeRegister;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.DecimalFormat;
import java.util.Optional;


public class PvZEntityProvider implements IEntityComponentProvider {

    public static final PvZEntityProvider INSTANCE = new PvZEntityProvider();

    private static final DecimalFormat DF_COMMAS = new DecimalFormat("#,###.##");
	private static final Identifier UID = StringUtil.id("entity_provider");


    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (config.get(JadeRegister.CONFIG_SHOW_DEFENCE_HEALTH)) {
            appendDefenceHealth(accessor.getEntity(), tooltip);
        }
        if (config.get(JadeRegister.CONFIG_SHOW_OWNER)) {
            appendOwner(accessor.getLevel(), accessor.getEntity(), tooltip);
        }
        if (config.get(JadeRegister.CONFIG_SHOW_SKILLS)) {
            appendSkills(accessor.getLevel(), accessor.getEntity(), tooltip);
        }
        //hide info need shift to check.
        if (accessor.getPlayer().getPose() == Pose.CROUCHING) {

        }
    }

    @Override
    public Identifier getUid() {
        return UID;
    }


    private void appendOwner(Level level, Entity entity, ITooltip tooltip) {
        if (!(entity instanceof IHasOwner)) {
            return;
        }
        ((IHasOwner) entity).getOwnerUUID().flatMap(uuid -> Optional.ofNullable(level.getPlayerByUUID(uuid))).ifPresent(player -> {
            tooltip.add(Component.translatable("tooltip.pvz.owner").append(" : " + player.getName().getString()).withStyle(ChatFormatting.GREEN));
        });
    }

    private void appendDefenceHealth(Entity entity, ITooltip tooltip) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        double health = EntityUtil.getCurrentDefenceHealth((LivingEntity) entity);
        if (health == 0) {
            return;
        }
        tooltip.add(Component.translatable("tooltip.pvz.defence")
                .append(" : " + DF_COMMAS.format(health))
                .withStyle(ChatFormatting.RED));
    }

    private void appendSkills(Level level, Entity entity, ITooltip tooltip) {
        if (entity instanceof AbstractPAZEntity pazEntity) {
            final IPAZType type = pazEntity.getPAZType();
            final SkillLevels skills = pazEntity.getSkillLevels();
            if (skills == null || skills == SkillLevels.EMPTY) {
                return;
            }
            type.getSkills().forEach(skillType -> {
                final int lvl = skills.getLevel(skillType);
                if (lvl > 0) {
                    tooltip.add(skillType.getText()
                            .append(StringUtil.getRomanString(lvl))
                            .withStyle(ChatFormatting.DARK_PURPLE));
                }
            });
        }
    }


}
