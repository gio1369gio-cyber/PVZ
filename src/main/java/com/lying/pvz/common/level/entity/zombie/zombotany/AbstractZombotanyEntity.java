package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.entity.projectile.ICanShoot;
import com.lying.pvz.api.entity.projectile.IPAZProjectile;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 僵尸植物基类。
 *
 * <p>僵尸植物是“僵尸阵营但会像植物一样发射弹药”的实体。
 * 实现 {@link ICanShoot}，与植物共用 {@code shootFrom(IPAZEntity)} 契约，
 * 弹药 {@code ownerGroup} 自然拿到 。
 *
 * <p><b>{@link #createProjectile()} 的默认行为</b>：抛出
 * {@link UnsupportedOperationException}。原因是相当一部分僵尸植物
 * （大坚果、高坚果、南瓜、窝瓜、火爆辣椒等）不是射手，但为了复用
 * 僵尸植物公共字段而继承了本类，不应当被 {@code ICanShoot} 调用。
 * 真正的射手（{@link PeaShooterZombieEntity} 系列）自行覆写。
 *
 * <p>攻击范围 {@link #getAttackRange()} 与间隔 {@link #getAttackInterval()}
 * 给出通用默认值，供 AI goal 使用；需要时子类覆写。
 */
public abstract class AbstractZombotanyEntity extends ZombieEntity implements ICanShoot {

    public AbstractZombotanyEntity(EntityType<? extends ZombieEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.91f;
    }

    @Override
    public IPAZProjectile createProjectile() {
        throw new UnsupportedOperationException(
                "Zombotany " + this.getZombieType().getID() + " does not create projectiles");
    }

    @Override
    public float getAttackRange() {
        return 30F;
    }

    @Override
    public int getAttackInterval() {
        return 40;
    }
}