package com.lying.pvz.common.level.entity.zombie.boss;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.PvZ2BossZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * 狮身终结者（Zombot Sphinx-inator），神秘埃及时空 BOSS。
 *
 * <p>共用四足机甲躯体，头部多出法老头巾（nemes）部件。
 * 技能与 {@link AbstractZombotEntity} 一致：召唤 / 导弹 / 毁灭冲撞 / 跳跃。
 *
 * <p><b>召唤（埃及主题）</b>：Ra 僵尸为主，混入基础僵尸。
 */
public class SphinxInatorEntity extends AbstractZombotEntity {

    public SphinxInatorEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public IZombieType getZombieType() { return PvZ2BossZombies.SPHINX_INATOR.get(); }

    @Override
    public ZombotVariant getZombotVariant() { return ZombotVariant.SPHINX_INATOR; }

    @Nullable
    @Override
    protected Mob getSummonZombie() {
        // 埃及主题：Ra 僵尸优先，混入基础僵尸
        EntityType<?> type = this.getRandom().nextInt(3) == 0
                ? PvZEntityRegister.NORMAL_ZOMBIE.get()
                : PvZEntityRegister.RA_ZOMBIE.get();
        return (Mob) type.create(this.level(), EntitySpawnReason.MOB_SUMMONED);
    }
}
