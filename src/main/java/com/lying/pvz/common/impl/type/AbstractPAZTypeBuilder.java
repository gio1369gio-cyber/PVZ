package com.lying.pvz.common.impl.type;

import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * {@link IPAZType} 的共享字段构建器。
 *
 * <p>package-private：只允许本包的 {@link PlantTypeBuilder} / {@link ZombieTypeBuilder} 继承。
 *
 * <p><b>必填字段</b>：{@code coolDown} / {@code rank} / {@code essence}。
 * 若 build 时仍为 {@code null}，抛出 {@link IllegalStateException}。
 *
 * <p><b>Supplier 字段</b>：所有类型的字段都是 {@link Supplier}，
 * 以便在 {@code RegisterEvent} 之前构造 Builder（此时 {@code DeferredHolder} 尚未绑定实际值）。
 */
abstract class AbstractPAZTypeBuilder<B extends AbstractPAZTypeBuilder<B>> {

    final Identifier id;
    int sunCost = 9999;
    int requiredLevel = 100;
    int xpPoint = 0;
    Supplier<ICoolDown> coolDown = null;
    Supplier<IRankType> rank = null;
    Supplier<IEssenceType> essence = null;
    final List<Supplier<ISkillType>> skills = new ArrayList<>();
    String categoryName = "pvz";
    int sortPriority = 100;
    ResourceKey<LootTable> lootTable = null;
    Supplier<EntityType<? extends Mob>> entityType = null;
    Supplier<? extends Item> summonCard = null;
    Supplier<? extends Item> enjoyCard = null;

    AbstractPAZTypeBuilder(Identifier id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    @SuppressWarnings("unchecked")
    final B self() {
        return (B) this;
    }

    /**
     * 构建不可变类型。
     *
     */
    public abstract IPAZType build();

    /* ---- 共享链式 setter ---- */

    public final B sunCost(int cost) {
        this.sunCost = cost;
        return self();
    }

    public final B requiredLevel(int level) {
        this.requiredLevel = level;
        return self();
    }

    public final B xp(int point) {
        this.xpPoint = point;
        return self();
    }

    public final B coolDown(Supplier<ICoolDown> supplier) {
        this.coolDown = Objects.requireNonNull(supplier, "coolDown");
        return self();
    }

    public final B rank(Supplier<IRankType> supplier) {
        this.rank = Objects.requireNonNull(supplier, "rank");
        return self();
    }

    public final B essence(Supplier<IEssenceType> supplier) {
        this.essence = Objects.requireNonNull(supplier, "essence");
        return self();
    }

    public final B skill(Supplier<ISkillType> supplier) {
        this.skills.add(Objects.requireNonNull(supplier, "skill"));
        return self();
    }

    @SafeVarargs
    public final B skill(Supplier<ISkillType>... suppliers) {
        for (Supplier<ISkillType> s : suppliers) {
            this.skill(s);
        }
        return self();
    }

    public final B skills(Collection<? extends Supplier<ISkillType>> suppliers) {
        for (Supplier<ISkillType> s : suppliers) {
            this.skill(s);
        }
        return self();
    }

    public final B category(String categoryName) {
        this.categoryName = Objects.requireNonNull(categoryName, "categoryName");
        return self();
    }

    public final B sortPriority(int priority) {
        this.sortPriority = priority;
        return self();
    }

    /**
     * 一次性设置 category + sortPriority，常用于分类聚合。
     */
    public final B group(String categoryName, int priority) {
        this.category(categoryName);
        this.sortPriority(priority);
        return self();
    }

    public final B loot(ResourceKey<LootTable> table) {
        this.lootTable = table;
        return self();
    }

    public final B entityType(Supplier<EntityType<? extends Mob>> supplier) {
        this.entityType = Objects.requireNonNull(supplier, "entityType");
        return self();
    }

    public final B summonCard(Supplier<? extends Item> supplier) {
        this.summonCard = supplier;
        return self();
    }

    public final B enjoyCard(Supplier<? extends Item> supplier) {
        this.enjoyCard = supplier;
        return self();
    }

    /**
     * 校验共享必填字段。子类应在 {@code build()} 开头调用。
     */
    protected final void validateCommon() {
        if (this.coolDown == null) {
            throw new IllegalStateException("coolDown is required for " + this.id);
        }
        if (this.rank == null) {
            throw new IllegalStateException("rank is required for " + this.id);
        }
        if (this.essence == null) {
            throw new IllegalStateException("essence is required for " + this.id);
        }
    }
}