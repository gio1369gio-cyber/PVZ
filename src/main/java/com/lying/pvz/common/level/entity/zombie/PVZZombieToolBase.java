package com.lying.pvz.common.level.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 僵尸"工具"实体基类。
 *
 * <p>用于依附在僵尸身上的临时实体，例如梯子、蹦极绳索、投掷车等。
 * 本体不可被伤害（{@code setInvulnerable}），通过 {@link #aiStep()} 中的
 * 乘客计数做生命周期管理：
 * <ul>
 *   <li>有乘客时 {@code liveTick} 归零，实体保持存活；</li>
 *   <li>无乘客时累计 {@code liveTick}，超过 {@link #getMaxLiveTick()} 后 {@link #discard()}。</li>
 * </ul>
 *
 * <p>不是 {@link PVZZombieEntity} 的子类：
 * 工具实体不参与僵尸 AI、不掉落部件、不进 BossBar。子类只需实现
 * 具体工具的行为，无需覆写生命周期逻辑；如需调整存活阈值，覆写
 * {@link #getMaxLiveTick()}。
 *
 * <p>存档入口 {@link ValueInput} / {@link ValueOutput}（1.21.1）。
 */
public abstract class PVZZombieToolBase extends Mob {

    protected int liveTick = 0;

    public PVZZombieToolBase(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.setPermanentlyInvulnerable(true);
    }

    protected int getMaxLiveTick() {
        return 3;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            if (this.getPassengers().isEmpty()) {
                this.liveTick++;
            } else {
                this.liveTick = 0;
            }
            if (this.liveTick >= this.getMaxLiveTick()) {
                this.discard();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.liveTick = input.getIntOr("live_tick", 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("live_tick", this.liveTick);
    }
}