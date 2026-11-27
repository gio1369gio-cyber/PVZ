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
 * 战争马车（Zombot War Wagon），狂野西部时空 BOSS。
 *
 * <p>总血量 25,000 = 外防御 + 内防御（相对狮身终结者的 18,000 按 25:18 缩放）。
 * 共用四足机甲躯体，头部多出火车排障器（cow）部件。
 *
 * <p><b>技能</b>（与 {@link AbstractZombotEntity} 通用四技能一致，仅细节有差）：
 * <ul>
 *   <li>召唤僵尸：分阶段召唤西部僵尸。第一阶段普僵、路障；第二阶段路障、巨人、小鬼；第三阶段铁桶。</li>
 *   <li>导弹：发射 4 枚（小概率 1~3 枚），瞄准植物；给被瞄准植物使用能量豆大招可使导弹失去目标。</li>
 *   <li>毁灭冲撞：摧毁两行内所有植物、僵尸及其它生物，可被植物大招打断。</li>
 *   <li>跳跃：改变位置，落点可压死植物。</li>
 * </ul>
 */
public class WarWagonEntity extends AbstractZombotEntity {

    public WarWagonEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public IZombieType getZombieType() { return PvZ2BossZombies.WAR_WAGON.get(); }

    @Override
    public ZombotVariant getZombotVariant() { return ZombotVariant.WAR_WAGON; }

    /** 总血量 25,000，相对狮身（18,000）按 25:18 缩放，外:内 = 1:4。 */
    @Override
    protected float getLife() { return 2083.3F; }

    @Override
    protected float getInnerLife() { return 8333.3F; }

    @Nullable
    @Override
    protected Mob getSummonZombie() {
        // 西部主题：铁桶僵尸为主，混入基础僵尸
        EntityType<?> type = this.getRandom().nextInt(3) == 0
                ? PvZEntityRegister.NORMAL_ZOMBIE.get()
                : PvZEntityRegister.BUCKETHEAD_ZOMBIE.get();
        return (Mob) type.create(this.level(), EntitySpawnReason.MOB_SUMMONED);
    }
}
