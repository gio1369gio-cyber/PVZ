package com.lying.pvz.api.entity;

import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.entity.ability.ICanBeAttracted;
import com.lying.pvz.api.entity.ability.ICanBeCharmed;
import com.lying.pvz.api.entity.ability.IHasGroup;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.IHasOwner;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;

import java.util.List;

/**
 * 植物与僵尸的共同实体契约。
 *
 * <p>只定义跨植物 / 僵尸的公共能力；植物专属扩展见
 * {@code com.lying.pvz.api.entity.plant.IPlantEntity}，
 * 僵尸专属扩展见 {@code com.lying.pvz.api.entity.zombie.IZombieEntity}。
 */
public interface IPAZEntity
        extends IHasOwner, IHasGroup, ICanBeCharmed, ICanBeAttracted, IHasMetal {

    /**
     * 对应类型元数据。
     */
    IPAZType getPAZType();

    /* ---- 状态 ---- */

    boolean hasState(PAZState state);

    void setState(PAZState state, boolean value);

    /* ---- 能力查询 ---- */

    boolean canBeCold();

    boolean canBeFrozen();

    boolean canBeButtered();

    /**
     * 是否能被迷你化。默认由实现层返回 {@code true}；不需要 mini 的僵尸覆写返回 {@code false}。
     */
    boolean canBeMini();

    boolean canBeInvisible();

    boolean canBeStealByBungee();

    /**
     * 是否能被魅惑（能力查询）。
     *
     * <p>注意：与 {@link ICanBeCharmed#isCharmed()} 不同——后者是"是否已被魅惑"的状态查询。
     */
    boolean canBeCharmed();

    /* ---- 内外层防御生命 ---- */

    double getOuterDefenceLife();

    void setOuterDefenceLife(double life);

    double getInnerDefenceLife();

    void setInnerDefenceLife(double life);

    /* ---- 技能数据 ---- */

    /**
     * 当前实体携带的技能等级。
     */
    SkillLevels getSkillLevels();

    /**
     * 替换当前技能等级，并立即刷新受技能影响的属性
     * （生命上限、伤害、速度等由具体实现决定）。
     */
    void applySkillLevels(SkillLevels skills);

    /* ---- 弹药可命中性 ---- */

    /**
     * 本实体能否被 {@code source} 命中。
     *
     * <p>弹药侧两步判定：先比 {@link IHasGroup#getEntityGroupType()} 是否同阵营，
     * 再调本方法。植物实现通常返回"有外层 / 有梯子时不可命中"，
     * 僵尸实现通常返回"升起时不可命中"。
     *
     * <p>默认返回 {@code true}，即任何非友方弹药都可命中。
     */
    default boolean canBeHitBy(Entity source) {
        return true;
    }

    /* ---- 图鉴 ---- */

    /**
     * 向给定列表追加图鉴条目。调用方负责排序与渲染。
     */
    void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list);
}