package com.lying.pvz.api.content.paz;

import com.lying.pvz.api.content.card.ICardPlacement;
import com.lying.pvz.api.entity.plant.IPlantInfo;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 植物类型。
 *
 * <p>分类（{@link PlantCategory}）、战斗形态（{@link PlantCombatStyle}）、
 * 特征（{@link PlantTrait}）三者正交，共同描述植物：
 * <ul>
 *   <li>大类决定"干什么"（近战 / 远程 / 防御 / 特殊）</li>
 *   <li>战斗形态决定"怎么打 / 怎么防"（影响碰撞、目标选择、发射行为）</li>
 *   <li>特征描述"是什么"（蘑菇、水生、外层……）</li>
 * </ul>
 *
 * <p>具体是不是蘑菇 / 水生 / 外层，一律通过 {@link #hasTrait(PlantTrait)} 判断，
 * 不使用独立的布尔方法，避免"分类枚举与布尔方法"两套 API 并存。
 */
public interface IPlantType extends IPAZType {

    /**
     * 植物大类。未显式配置时返回 {@link PlantCategory#SPECIAL}。
     */
    PlantCategory getCategory();

    /**
     * 战斗形态。未显式配置时返回 {@link PlantCombatStyle#NONE}。
     */
    PlantCombatStyle getCombatStyle();

    /**
     * 是否拥有指定特征。
     */
    boolean hasTrait(PlantTrait trait);

    /**
     * 全部特征，不可变集合。
     */
    Set<PlantTrait> getTraits();

    /**
     * 卡片放置规则。
     */
    ICardPlacement getPlacement();

    /**
     * 前置形态（升级来源）。无前置返回空列表。
     */
    List<IPlantType> getUpgradeFrom();

    /**
     * 后续形态（升级目标）。不可升级返回空列表。一个植物可以有多个升级分支。
     */
    List<IPlantType> getUpgradeTo();

    /**
     * 该植物对应的方块形态（如向日葵种子种下的花）。
     */
    Optional<Block> getPlantBlock();

    /**
     * 外层植物信息工厂。
     *
     * <p><b>契约</b>：每次调用必须返回<b>新实例</b>。每颗植物各自持有独立的外层信息，
     * 不能返回共享单例，否则多颗植物的 sunCost / type / 额外字段会互相覆盖。
     *
     * <p>调用方应先检查 {@code hasTrait(PlantTrait.OUTER)} 再调用本方法。
     * 非外层植物（{@code !hasTrait(OUTER)}）实现方<b>应当</b>抛
     * {@link IllegalStateException}。
     */
    Supplier<IPlantInfo> getOuterPlantInfo();
}