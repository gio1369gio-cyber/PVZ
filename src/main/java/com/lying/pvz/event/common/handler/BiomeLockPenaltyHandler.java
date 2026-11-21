package com.lying.pvz.event.common.handler;

import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.worldgen.BiomeProgression;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDimension;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;

/**
 * 未解锁群系惩罚：玩家在 pvz_2 维度踏入未解锁的 PVZ 群系时，
 * 每秒扣血 + 施加负面 Buff，直到离开或解锁（对齐暮色森林的惩罚机制）。
 */
public final class BiomeLockPenaltyHandler {

    /** 扣血间隔（tick）：每秒一次。 */
    private static final int DAMAGE_INTERVAL = 20;
    private static final float DAMAGE = 1.0F;
    /** Buff 时长（tick）：10 秒。 */
    private static final int EFFECT_DURATION = 200;
    /** Buff 补加间隔（tick）：10 秒，与时长一致，不用频繁刷。 */
    private static final int EFFECT_INTERVAL = 200;

    private BiomeLockPenaltyHandler() {}

    /** 在 PlayerTickEvent.Post 中调用（服务端）。 */
    public static void onPlayerTick(ServerPlayer player) {
        if (player.level().dimension() != pvzDimension()) return;

        Holder<Biome> holder = player.level().getBiome(player.blockPosition());
        ResourceKey<Biome> key = progressionKey(holder);

        var opt = PlayerUtil.getOptManager(player);
        if (opt.isEmpty()) return;
        PlayerDataManager mgr = opt.get();

        // 离开锁定区 / 进入已解锁区：清空提示状态，下次进入会重新提示一次
        if (key == null || mgr.isBiomeUnlocked(key)) {
            if (mgr.getLockedBiomeNotified() != null) {
                mgr.setLockedBiomeNotified(null);
            }
            return;
        }

        String biomeId = key.identifier().toString();
        long gameTime = player.level().getGameTime();

        // 提示 + 首次施加 Buff：只在刚进入该锁定群系时触发一次
        if (!biomeId.equals(mgr.getLockedBiomeNotified())) {
            mgr.setLockedBiomeNotified(biomeId);
            applyEffects(player);
            PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.biome_locked")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        // 常驻期间：每秒扣血；每 10 秒补一次 Buff（不刷屏）
        if (gameTime % DAMAGE_INTERVAL == 0) {
            player.hurt(player.level().damageSources().magic(), DAMAGE);
        }
        if (gameTime % EFFECT_INTERVAL == 0) {
            applyEffects(player);
        }
    }

    private static void applyEffects(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, EFFECT_DURATION, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, EFFECT_DURATION, 1, false, false));
    }

    private static ResourceKey<Level> pvzDimension() {
        return ResourceKey.create(Registries.DIMENSION, PvZDimension.PVZ_2.identifier());
    }

    @Nullable
    private static ResourceKey<Biome> progressionKey(Holder<Biome> biome) {
        for (ResourceKey<Biome> key : BiomeProgression.ORDER) {
            if (biome.is(key)) return key;
        }
        return null;
    }
}
