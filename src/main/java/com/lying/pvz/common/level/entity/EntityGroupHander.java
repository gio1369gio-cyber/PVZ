package com.lying.pvz.common.level.entity;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.common.tag.PvZEntityTypeTags;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

public class EntityGroupHander {

    public static GroupType getEntityGroupType(Entity entity){
        if(entity.is(PvZEntityTypeTags.PVZ_PLANT_GROUP_ENTITIES)){
            return GroupType.PLANTS;
        }
        if(entity.is(PvZEntityTypeTags.PVZ_ZOMBIE_GROUP_ENTITIES)){
            return GroupType.ZOMBIES;
        }
        if((entity.getType().getCategory() == MobCategory.MONSTER || entity.is(PvZEntityTypeTags.PVZ_OTHER_MONSTERS)) && ! entity.is(PvZEntityTypeTags.PVZ_NOT_MONSTERS)){
            return GroupType.OTHER_MONSTERS;
        }
        if((entity instanceof TamableAnimal || entity.is(PvZEntityTypeTags.PVZ_OTHER_GUARDIANS)) && ! entity.is(PvZEntityTypeTags.PVZ_NOT_GUARDIANS)){
            return GroupType.OTHER_GUARDIANS;
        }
        return GroupType.NEUTRALS;
    }

    public static GroupType getPlayerGroup(Player player){
        final int group = PlayerUtil.getResource(player, Resources.GROUP_TYPE);
        return getGroup(group);
    }

    public static GroupType getGroup(int type){
        final int group = Mth.clamp(type, Resources.GROUP_TYPE.min, Resources.GROUP_TYPE.max);
        return GroupType.values()[group + 2];
    }

    public static boolean isMonsterGroup(GroupType groupType){
        return groupType.ordinal() - 2 < 0;
    }

    public static boolean checkCanAttack(GroupType g1, GroupType g2) {
        return g1 != g2;
    }

    public static boolean checkCanTarget(GroupType g1, GroupType g2) {
        return (g1.ordinal() - 2) * (g2.ordinal() - 2) < 0;
    }
}