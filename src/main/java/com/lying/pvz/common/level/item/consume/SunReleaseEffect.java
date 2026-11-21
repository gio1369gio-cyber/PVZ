package com.lying.pvz.common.level.item.consume;

import com.lying.pvz.common.init.PvZConsumeEffects;
import com.lying.pvz.common.level.item.tool.plant.SunStorageSaplingItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

/**
 * 食用储光树苗时释放太阳。
 * <p>由 {@link net.minecraft.world.item.component.Consumable#onConsume} 调度，仅服务端执行。
 */
public record SunReleaseEffect() implements ConsumeEffect {

    public static final SunReleaseEffect INSTANCE = new SunReleaseEffect();

    public static final MapCodec<SunReleaseEffect> CODEC = MapCodec.unit(SunReleaseEffect::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, SunReleaseEffect> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return PvZConsumeEffects.SUN_RELEASE.get();
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            return SunStorageSaplingItem.releaseSunTo(player, stack) > 0;
        }
        return false;
    }
}