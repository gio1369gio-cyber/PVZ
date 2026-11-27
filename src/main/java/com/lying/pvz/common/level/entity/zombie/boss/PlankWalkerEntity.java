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
 * 甲板漫步者（Zombot Plank Walker），海盗港湾时空 BOSS。
 *
 * <p>总血量 21,000 = 外防御 + 内防御（相对狮身终结者的 18,000 按 7:6 缩放）。
 * 共用四足机甲躯体，头部多出桅杆（trick_rod）+ 海盗旗（flag）部件。
 *
 * <p><b>技能</b>（与 {@link AbstractZombotEntity} 通用四技能一致，仅细节有差）：
 * <ul>
 *   <li>召唤僵尸：分阶段召唤海盗僵尸。第一阶段为路障和普通僵尸；第二阶段加入铁桶、巨人；第三阶段为铁桶、普通僵尸。</li>
 *   <li>小鬼僵尸炮：从「眼睛」处露出大炮，连续发射 1~3 波，每波 6~12 只小鬼僵尸，抛物线越过前排防御（替代通用导弹技能）。</li>
 *   <li>毁灭冲撞：损失一段血后使用，摧毁所在两行所有植物、僵尸及其它生物，可被植物大招打断。</li>
 *   <li>跳跃：改变位置，落点可压死植物。</li>
 * </ul>
 */
public class PlankWalkerEntity extends AbstractZombotEntity {

    public PlankWalkerEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public IZombieType getZombieType() { return PvZ2BossZombies.PLANK_WALKER.get(); }

    @Override
    public ZombotVariant getZombotVariant() { return ZombotVariant.PLANK_WALKER; }

    /** 甲板漫步者用「小鬼炮」替代通用导弹。 */
    @Override
    public boolean canUseMissile() { return false; }

    @Override
    public boolean canUseImpCannon() { return true; }

    /** 总血量 21,000，相对狮身（18,000）按 7:6 缩放，外:内 = 1:4。 */
    @Override
    protected float getLife() { return 1750F; }

    @Override
    protected float getInnerLife() { return 7000F; }

    @Nullable
    @Override
    protected Mob getSummonZombie() {
        // 海盗主题：小鬼僵尸为主，混入基础僵尸
        EntityType<?> type = this.getRandom().nextInt(3) == 0
                ? PvZEntityRegister.NORMAL_ZOMBIE.get()
                : PvZEntityRegister.IMP.get();
        return (Mob) type.create(this.level(), EntitySpawnReason.MOB_SUMMONED);
    }
}
