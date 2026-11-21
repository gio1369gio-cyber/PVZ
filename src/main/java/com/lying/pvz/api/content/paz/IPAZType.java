package com.lying.pvz.api.content.paz;

import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IPVZType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;
import java.util.Set;

/**
 * 植物与僵尸的共同类型元数据。
 *
 * <p>渲染相关数据（模型 / 贴图 / 缩放）不在本接口；由客户端
 * {@code IClientPAZInfo} 按 {@link #getID()} 提供。
 */
public interface IPAZType extends IPVZType {

    int getSunCost();

    int getRequiredLevel();

    int getXpPoint();

    ICoolDown getCoolDown();

    IRankType getRank();

    IEssenceType getEssence();

    /**
     * 该类型可携带的技能集合。返回值不可变；具体等级由卡片 / 实体侧数据决定。
     */
    Set<ISkillType> getSkills();

    String getCategoryName();

    int getSortPriority();

    Optional<ResourceKey<LootTable>> getLootTable();

    Optional<EntityType<? extends Mob>> getEntityType();

    Optional<Item> getSummonCard();

    Optional<Item> getEnjoyCard();
}