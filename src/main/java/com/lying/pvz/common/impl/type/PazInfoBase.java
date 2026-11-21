package com.lying.pvz.common.impl.type;


import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * {@link IPAZType} 的共享字段实现基类。
 *
 * <p>package-private：只允许本包的 {@code PlantTypeImpl} / {@code ZombieTypeImpl} 继承。
 *
 * <p><b>关于 {@link #getSkills()}</b>：首次调用时从 {@code List<Supplier<ISkillType>>}
 * 解析成 {@link LinkedHashSet}，之后<b>缓存</b>为不可变 Set。
 *
 * <p><b>为什么可以缓存</b>：所有 {@code Supplier<ISkillType>} 在
 * {@code RegisterEvent} 之后绑定为稳定的 {@code ISkillType} 实例。{@code getSkills()}
 * 的调用点（实体 tick、升级合并、弹药 {@code shootFrom}）均在注册完成后，
 * 缓存不会捕获"未绑定"状态。若未来引入"注册期调用 getSkills"的用法，需改为
 * 每次重建，或加注册完成标志。
 *
 * <p><b>缓存实现</b>：{@code volatile} 字段 + benign race（多线程同时首建会重复构建
 * 一次但结果一致，之后所有调用读同一实例）。
 */
abstract class PazInfoBase implements IPAZType {

    private final Identifier id;
    private final int sunCost;
    private final int requiredLevel;
    private final int xpPoint;
    private final Supplier<ICoolDown> coolDown;
    private final Supplier<IRankType> rank;
    private final Supplier<IEssenceType> essence;
    private final List<Supplier<ISkillType>> skills;
    private final String categoryName;
    private final int sortPriority;
    private final ResourceKey<LootTable> lootTable;
    private final Supplier<EntityType<? extends Mob>> entityType;
    private final Supplier<? extends Item> summonCard;
    private final Supplier<? extends Item> enjoyCard;

    /** 惰性缓存；首次 {@link #getSkills()} 时求值，之后复用。 */
    private volatile Set<ISkillType> cachedSkills;

    PazInfoBase(AbstractPAZTypeBuilder<?> b) {
        this.id = b.id;
        this.sunCost = b.sunCost;
        this.requiredLevel = b.requiredLevel;
        this.xpPoint = b.xpPoint;
        this.coolDown = b.coolDown;
        this.rank = b.rank;
        this.essence = b.essence;
        this.skills = List.copyOf(b.skills);
        this.categoryName = b.categoryName;
        this.sortPriority = b.sortPriority;
        this.lootTable = b.lootTable;
        this.entityType = b.entityType;
        this.summonCard = b.summonCard;
        this.enjoyCard = b.enjoyCard;
    }

    @Override
    public Identifier getID() {
        return this.id;
    }

    @Override
    public MutableComponent getText() {
        return Component.translatable(
                "entity." + this.id.getNamespace() + "." + this.id.getPath());
    }

    @Override
    public int getSunCost() {
        return this.sunCost;
    }

    @Override
    public int getRequiredLevel() {
        return this.requiredLevel;
    }

    @Override
    public int getXpPoint() {
        return this.xpPoint;
    }

    @Override
    public ICoolDown getCoolDown() {
        return this.coolDown.get();
    }

    @Override
    public IRankType getRank() {
        return this.rank.get();
    }

    @Override
    public IEssenceType getEssence() {
        return this.essence.get();
    }

    @Override
    public Set<ISkillType> getSkills() {
        final Set<ISkillType> cached = this.cachedSkills;
        if (cached != null) {
            return cached;
        }
        final Set<ISkillType> result = new LinkedHashSet<>();
        for (Supplier<ISkillType> s : this.skills) {
            result.add(s.get());
        }
        final Set<ISkillType> immutable = Collections.unmodifiableSet(result);
        this.cachedSkills = immutable;
        return immutable;
    }

    @Override
    public String getCategoryName() {
        return this.categoryName;
    }

    @Override
    public int getSortPriority() {
        return this.sortPriority;
    }

    @Override
    public Optional<ResourceKey<LootTable>> getLootTable() {
        return Optional.ofNullable(this.lootTable);
    }

    @Override
    public Optional<EntityType<? extends Mob>> getEntityType() {
        return this.entityType == null ? Optional.empty() : Optional.ofNullable(this.entityType.get());
    }

    @Override
    public Optional<Item> getSummonCard() {
        return this.summonCard == null ? Optional.empty() : Optional.ofNullable(this.summonCard.get());
    }

    @Override
    public Optional<Item> getEnjoyCard() {
        return this.enjoyCard == null ? Optional.empty() : Optional.ofNullable(this.enjoyCard.get());
    }
}