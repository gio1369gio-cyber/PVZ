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
 * 明日破译者（Zombot Tomorrow-tron），未来世界时空 BOSS。
 *
 * <p>总血量 32,000 = 外防御 + 内防御（相对狮身终结者的 18,000 按 16:9 缩放）。
 * 共用四足机甲躯体，无额外头部装饰（纯底座）。
 *
 * <p><b>技能</b>（与 {@link AbstractZombotEntity} 通用四技能一致，仅细节有差）：
 * <ul>
 *   <li>召唤僵尸：分阶段召唤未来僵尸。第一阶段路障、铁桶；第二阶段路障、铁桶、巨人；第三阶段铁桶、橄榄球僵尸。</li>
 *   <li>高科技导弹：发射导弹直接摧毁植物；给目标植物使用能量豆大招可使其失去目标。</li>
 *   <li>毁灭冲撞：损失一段血后使用，摧毁两行内所有植物、僵尸及其它生物，可被植物大招打断。</li>
 *   <li>跳跃：改变位置，落点可压死植物。</li>
 * </ul>
 */
public class TomorrowTronEntity extends AbstractZombotEntity {

    public TomorrowTronEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public IZombieType getZombieType() { return PvZ2BossZombies.TOMORROW_TRON.get(); }

    @Override
    public ZombotVariant getZombotVariant() { return ZombotVariant.TOMORROW_TRON; }

    /** 总血量 32,000，相对狮身（18,000）按 16:9 缩放，外:内 = 1:4。 */
    @Override
    protected float getLife() { return 2666.7F; }

    @Override
    protected float getInnerLife() { return 10666.7F; }

    @Nullable
    @Override
    protected Mob getSummonZombie() {
        // 未来主题：路障僵尸为主，混入基础僵尸
        EntityType<?> type = this.getRandom().nextInt(3) == 0
                ? PvZEntityRegister.NORMAL_ZOMBIE.get()
                : PvZEntityRegister.CONEHEAD_ZOMBIE.get();
        return (Mob) type.create(this.level(), EntitySpawnReason.MOB_SUMMONED);
    }
}
