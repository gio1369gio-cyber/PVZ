package com.lying.pvz.common.level.entity.drop;

import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.PVZBaseEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 奖励礼盒。
 *
 * <p>由老虎机、BOSS、挑战奖励等投放，玩家右键打开后把内部物品原样掉落，
 * 自身随后 {@code discard()}。礼盒本体不可被伤害、会发光、可被选中和碰撞。
 *
 * <p><b>物品存储</b>：26.1 起 {@code ItemStackHandler} 已被移除；
 * {@code ItemStacksResourceHandler} 面向 {@code ResourceHandler<ItemResource>}
 * 转移系统，对本场景过重。这里直接用 {@link NonNullList}<{@link ItemStack}>
 * 存物品，并借助 {@code ValueOutput#list} / {@code ValueInput#listOrEmpty} +
 * {@link ItemStack#OPTIONAL_CODEC} 完成存档往返。
 */
public class GiftBoxEntity extends PVZBaseEntity {

    /**
     * 未设置时为 null（保证空礼盒与已设置礼盒语义不同）
     */
    @Nullable
    private NonNullList<ItemStack> drops;

    public GiftBoxEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.setGlowingTag(true);
        this.setPermanentlyInvulnerable(true);    // ← 替代 setInvulnerable(true)
    }

    public void setDrops(NonNullList<ItemStack> items) {
        this.drops = items;
    }


    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (!this.level().isClientSide()) {
            if (this.drops != null) {
                for (ItemStack stack : this.drops) {
                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(this.level(),
                                this.blockPosition().getX(),
                                this.blockPosition().getY(),
                                this.blockPosition().getZ(),
                                stack);
                    }
                }
                EntityUtil.playSound(this, PvZSoundRegister.PRIZE_DROP.get());
            }
            this.discard();
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    // ---------------- 存档 ----------------

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        NonNullList<ItemStack> loaded = NonNullList.create();
        for (ItemStack stack : input.listOrEmpty("reward_items", ItemStack.OPTIONAL_CODEC)) {
            if (!stack.isEmpty()) {
                loaded.add(stack);
            }
        }
        this.drops = loaded.isEmpty() ? null : loaded;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.drops != null) {
            var list = output.list("reward_items", ItemStack.OPTIONAL_CODEC);
            for (ItemStack stack : this.drops) {
                if (!stack.isEmpty()) {
                    list.add(stack);
                }
            }
        }
    }
}