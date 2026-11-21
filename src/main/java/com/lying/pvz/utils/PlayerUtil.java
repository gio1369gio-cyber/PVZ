package com.lying.pvz.utils;

import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.payload.PlaySoundPayload;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

import static java.lang.Math.log10;
import static java.lang.Math.round;

/**
 * 玩家数据访问工具。
 *
 * <p>内部全部走 {@link com.lying.pvz.common.init.PvZAttachments} 的拆分附件；
 * {@link PlayerDataManager} 为无状态 facade，每次 {@link #getManager(Player)} 临时 new。
 */
public final class PlayerUtil {


    private static int OLD_PLAYER_LEVEL = 0;
    private static int CACHE_MAX_SUN = 0;

    private PlayerUtil() { }

    /* ============================================================ */
    /* ========================== 等级 / 阳光 ====================== */
    /* ============================================================ */

    public static int getPlayerMaxSunNum(int lvl) {
        if (lvl == OLD_PLAYER_LEVEL) {
            return CACHE_MAX_SUN;
        }
        final int len = 20;
        final int n = lvl / len;
        final int sum = 1000 * n + n * (n - 1) / 2 * 500 + (lvl - len * n) * (50 + n * 25);
        OLD_PLAYER_LEVEL = lvl;
        return (CACHE_MAX_SUN = ConfigUtil.getBaseSun() + sum);
    }

    public static int getPlayerLevelUpXp(int lvl) {
        return (int) round(80 + 400 * log10(Math.max(1, lvl)) + lvl * 30);
    }

    /* ============================================================ */
    /* ========================== manager ========================== */
    /* ============================================================ */

    @Nullable
    public static PlayerDataManager getManager(Player player) {
        return isValidPlayer(player) ? new PlayerDataManager(player) : null;
    }

    @NonNull
    public static Optional<PlayerDataManager> getOptManager(Player player) {
        return Optional.ofNullable(getManager(player));
    }

    /* ============================================================ */
    /* ========================== 资源 ============================= */
    /* ============================================================ */

    public static int getResource(Player player, Resources res) {
        final PlayerDataManager manager = getManager(player);
        return manager != null ? manager.getResource(res) : res.min;
    }

    public static void addResource(Player player, Resources res, int num) {
        getOptManager(player).ifPresent(m -> m.addResource(res, num));
    }

    /* ============================================================ */
    /* ========================== PAZ 解锁 ========================= */
    /* ============================================================ */

    /**
     * 设置 PAZ 解锁状态。
     *
     * @param unlocked {@code true} = 解锁，{@code false} = 锁定
     */
    public static void setPAZUnlocked(Player player, IPAZType type, boolean unlocked) {
        getOptManager(player).ifPresent(m -> m.setPAZUnlocked(type.getID(), unlocked));
    }

    public static boolean isPAZUnlocked(Player player, IPAZType type) {
        final PlayerDataManager manager = getManager(player);
        return manager != null && manager.isPAZUnlocked(type.getID());
    }

    public static boolean isPAZLocked(Player player, IPAZType type) {
        return !isPAZUnlocked(player, type);
    }

    /* ============================================================ */
    /* ========================== PAZ CD =========================== */
    /* ============================================================ */

    /** 设置卡片冷却（同时写 vanilla {@code ItemCooldowns}）。 */
    public static void setPAZCardCD(Player player, IPAZType type, int cd) {
        getOptManager(player).ifPresent(m -> m.setPAZCardCD(type.getID(), cd));
    }

    public static int getPAZCardCD(Player player, IPAZType type) {
        final PlayerDataManager manager = getManager(player);
        return manager != null ? manager.getPAZCardCD(type.getID()) : 0;
    }


    /* ============================================================ */
    /* ========================== 客户端音效 / 标题 ================= */
    /* ============================================================ */

    public static void playClientSound(Player player, SoundEvent ev) {
        if (ev != null && player instanceof ServerPlayer sp) {
            PacketDistributor.sendToPlayer(sp,
                    new PlaySoundPayload(BuiltInRegistries.SOUND_EVENT.getKey(ev).toString()));
        }
    }


    public static void sendMsgTo(Player player, Component text) {
        player.sendSystemMessage(text);
    }


    /* ============================================================ */
    /* ========================== 工具 ============================= */
    /* ============================================================ */

    public static boolean isPlayerSurvival(Player player) {
        return !player.isCreative() && !player.isSpectator();
    }

    public static boolean isValidPlayer(Player player) {
        return player != null && !(player instanceof FakePlayer);
    }
}