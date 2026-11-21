package com.lying.pvz.event.common.handler;

import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.common.worldgen.BiomeProgression;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;

/**
 * 群系解锁进度 handler。
 *
 * <p>击败 Edgar090517 BOSS 后，按 BOSS 所在群系推进解锁链的下一个群系。
 * 归属玩家判定复用 {@link EntityUtil#getEntityOwner}（植物/子弹通过 owner UUID 反查）。
 */
public final class BiomeUnlockHandler {

    private BiomeUnlockHandler() {}

    /**
     * 在 LivingDeathEvent 中调用。仅服务端生效。
     *
     * @param dead  死亡实体
     * @param killer 直接击杀者（子弹/植物），可为 null
     */
    public static void onBossDeath(Entity dead, @Nullable Entity killer) {
        if (!(dead instanceof AbstractBossZombieEntity)) return;
        if (dead.level().isClientSide()) return;

        final Player player = findKillerPlayer(dead.level(), killer);
        if (player == null) return;

        // 按 BOSS 所在位置的群系推进解锁
        Holder<Biome> biome = dead.level().getBiome(dead.blockPosition());
        ResourceKey<Biome> current = currentKey(biome);
        if (current == null) return;

        ResourceKey<Biome> next = BiomeProgression.nextAfter(current);
        if (next == null) return; // 已是最后一群系

        PlayerUtil.getOptManager(player).ifPresent(manager -> unlockAndNotify(manager, player, next));
    }

    private static Player findKillerPlayer(net.minecraft.world.level.Level level, @Nullable Entity killer) {
        Player owner = EntityUtil.getEntityOwner(level, killer);
        if (owner != null) return owner;
        return killer instanceof Player p ? p : null;
    }

    /** 从群系 Holder 反查解锁链中的 ResourceKey（非链群系返回 null）。 */
    @Nullable
    private static ResourceKey<Biome> currentKey(Holder<Biome> biome) {
        for (ResourceKey<Biome> key : BiomeProgression.ORDER) {
            if (biome.is(key)) return key;
        }
        return null;
    }

    private static void unlockAndNotify(PlayerDataManager manager, Player player, ResourceKey<Biome> next) {
        if (!manager.unlockBiome(next)) return; // 已解锁则不重复提示
        if (player instanceof ServerPlayer sp) {
            PlayerUtil.sendMsgTo(sp, Component.translatable(
                    "msg.pvz.biome_unlock.success", biomeName(next)
            ).withStyle(ChatFormatting.GOLD));
        }
    }

    /** 从群系 key 的路径名生成显示文本（如 ancient_egypt → Ancient Egypt）。 */
    private static String biomeName(ResourceKey<Biome> key) {
        String path = key.identifier().getPath();
        StringBuilder sb = new StringBuilder();
        for (String part : path.split("_")) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }
}
