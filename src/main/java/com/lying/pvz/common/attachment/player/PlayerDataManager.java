package com.lying.pvz.common.attachment.player;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.event.player.PlayerLevelChangeEvent;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.NeoForge;

/**
 * 玩家数据 facade。
 *
 * <p><b>无状态</b>：每次访问由 {@link PlayerUtil#getManager(Player)} 临时 new，
 * 仅持有 {@link Player} 引用并转发到 5 个 attachment。
 *
 * <p>写方法内部负责 {@code player.syncData(...)}（仅 sync 型 attachment）。
 * 读方法直接转发。
 *
 * <p>不实现 {@code ValueIOSerializable}，不注册为 attachment。
 */
public final class PlayerDataManager {

    private final Player player;

    public PlayerDataManager(Player player) {
        this.player = player;
    }

    /* ============================================================ */
    /* ========================== 资源 ============================= */
    /* ============================================================ */

    public int getResource(Resources r) {
        return stats().get(r);
    }

    /**
     * 设置资源值。特殊处理：
     * <ul>
     *   <li>{@link Resources#TREE_XP}：走 {@link #addTreeXp(int)}，不允许直接 set 绝对值</li>
     *   <li>{@link Resources#SUN_NUM}：上限由 {@code TREE_LVL} 动态决定</li>
     *   <li>{@link Resources#ENERGY_NUM}：上限受 {@link Resources#MAX_ENERGY_NUM} 约束</li>
     * </ul>
     */
    public void setResource(Resources r, int v) {
        switch (r) {
            case TREE_XP -> {
                stats().set(Resources.TREE_XP, 0);
                addTreeXp(v);
            }
            case SUN_NUM -> {
                final int max = PlayerUtil.getPlayerMaxSunNum(getResource(Resources.TREE_LVL));
                stats().set(Resources.SUN_NUM, Mth.clamp(v, 0, max));
                syncStats();
            }
            case ENERGY_NUM -> {
                final int max = getResource(Resources.MAX_ENERGY_NUM);
                stats().set(Resources.ENERGY_NUM, Mth.clamp(v, 0, max));
                syncStats();
            }
            default -> {
                stats().set(r, v);
                syncStats();
            }
        }
    }

    public void addResource(Resources r, int delta) {
        if (delta == 0) return;
        if (r == Resources.TREE_XP) {
            addTreeXp(delta);
            return;
        }
        setResource(r, getResource(r) + delta);
    }

    /**
     * TREE_XP 溢出升级。递归触发 {@link PlayerLevelChangeEvent} 与 {@link PvZTriggers#TREE_LEVEL}。
     * 升级时同步 {@link Resources#SLOT_NUM}。
     */
    private void addTreeXp(int delta) {
        if (delta == 0) return;

        final PlayerStats stats = stats();
        int lvl = stats.get(Resources.TREE_LVL);
        int xp = stats.get(Resources.TREE_XP);

        if (delta > 0) {
            xp += delta;
            while (lvl < Resources.TREE_LVL.max) {
                final int req = PlayerUtil.getPlayerLevelUpXp(lvl);
                if (xp < req) break;
                xp -= req;
                final int oldLvl = lvl;
                lvl++;
                stats.set(Resources.TREE_LVL, lvl);
                updateSlotNum(lvl);
                fireLevelChange(oldLvl, lvl);
            }
            stats.set(Resources.TREE_XP, xp);
        } else {
            // 降级简化：只清空 XP，不降等级
            stats.set(Resources.TREE_XP, Math.max(0, xp + delta));
        }
        syncStats();
    }

    /** 按等级推导 {@link Resources#SLOT_NUM}（1→1，50→9，线性）。 */
    private void updateSlotNum(int lvl) {
        final int slot = Mth.clamp((lvl * 9 + 49) / 50, 1, 9);
        if (slot > getResource(Resources.SLOT_NUM)) {
            stats().set(Resources.SLOT_NUM, slot);
        }
    }

    private void fireLevelChange(int oldLvl, int newLvl) {
        if (newLvl == oldLvl) return;
        if (player instanceof ServerPlayer sp) {
            PvZTriggers.TREE_LEVEL.get().trigger(sp, newLvl);
            NeoForge.EVENT_BUS.post(new PlayerLevelChangeEvent(sp, oldLvl, newLvl));
        }
    }

    /* ============================================================ */
    /* ========================= PAZ 解锁 =========================== */
    /* ============================================================ */

    public boolean isPAZUnlocked(Identifier id) {
        return paz().isUnlocked(id);
    }

    public boolean isPAZUnlocked(IPAZType type) {
        return isPAZUnlocked(type.getID());
    }

    public boolean isPAZLocked(IPAZType type) {
        return !isPAZUnlocked(type);
    }

    public void setPAZUnlocked(Identifier id, boolean unlocked) {
        final PazState paz = paz();
        if (paz.isUnlocked(id) == unlocked) return;
        paz.setUnlocked(id, unlocked);
        syncPaz();
    }

    public void setPAZUnlocked(IPAZType type, boolean unlocked) {
        setPAZUnlocked(type.getID(), unlocked);
    }

    /* ============================================================ */
    /* ====================== 群系解锁 ============================= */
    /* ============================================================ */

    public boolean isBiomeUnlocked(ResourceKey<Biome> biome) {
        return biomeUnlock().isUnlocked(biome);
    }

    public boolean isEdgarDollGiven() {
        return biomeUnlock().isEdgarDollGiven();
    }

    /** 标记已发放埃德加玩偶（只发一次）。 */
    public void setEdgarDollGiven(boolean given) {
        biomeUnlock().setEdgarDollGiven(given);
    }

    /** 服务端解锁群系并同步。返回本次是否为新解锁。 */
    public boolean unlockBiome(ResourceKey<Biome> biome) {
        final boolean changed = biomeUnlock().unlock(biome);
        if (changed) {
            syncBiomeUnlock();
        }
        return changed;
    }

    /** 读取群系的记忆传送落点；从未到达过返回 null。 */
    @javax.annotation.Nullable
    public net.minecraft.core.BlockPos getBiomeAnchor(ResourceKey<Biome> biome) {
        return biomeUnlock().getAnchor(biome);
    }

    /** 是否已记忆该群系落点。 */
    public boolean hasBiomeAnchor(ResourceKey<Biome> biome) {
        return biomeUnlock().hasAnchor(biome);
    }

    /** 记录群系落点并同步（首次到达该群系时调用，之后固定复用）。 */
    public void setBiomeAnchor(ResourceKey<Biome> biome, net.minecraft.core.BlockPos pos) {
        biomeUnlock().setAnchor(biome, pos);
        syncBiomeUnlock();
    }

    /* ============================================================ */
    /* ========================== PAZ CD ============================ */
    /* ============================================================ */

    public int getPAZCardCD(Identifier id) {
        return cooldown().getCD(id);
    }

    public int getPAZCardCD(IPAZType type) {
        return getPAZCardCD(type.getID());
    }

    /**
     * 设置 PAZ 冷却。同时写 {@link PazCooldown} 账本与 vanilla {@code ItemCooldowns}。
     */
    public void setPAZCardCD(Identifier id, int cd) {
        cooldown().setCD(id, cd);
        if (player instanceof ServerPlayer sp && cd > 0) {
            PVZAPI.lookup().paz(id).flatMap(IPAZType::getSummonCard).ifPresent(item ->
                    sp.getCooldowns().addCooldown(item.getDefaultInstance(), cd));
        }
    }

    public void setPAZCardCD(IPAZType type, int cd) {
        setPAZCardCD(type.getID(), cd);
    }

    public float getPAZCardBar(Identifier id) {
        return cooldown().getBar(id);
    }

    public void setPAZCardBar(Identifier id, float bar) {
        cooldown().setBar(id, bar);
    }

    /** 登录时从 {@link PazCooldown} 恢复 vanilla 冷却。 */
    public void loadSummonCardCDs() {
        if (!(player instanceof ServerPlayer sp)) return;
        cooldown().allCDs().forEach((id, ticks) -> {
            if (ticks <= 0) return;
            PVZAPI.lookup().paz(id).flatMap(IPAZType::getSummonCard).ifPresent(item ->
                    sp.getCooldowns().addCooldown(item.getDefaultInstance(), ticks));
        });
    }

    /** 登出时保存 vanilla 冷却比例到 {@link PazCooldown}。 */
    public void saveSummonCardCDs() {
        if (!(player instanceof ServerPlayer sp)) return;
        cooldown().allCDs().keySet().forEach(id ->
                PVZAPI.lookup().paz(id).flatMap(IPAZType::getSummonCard).ifPresent(item ->
                        cooldown().setBar(id, sp.getCooldowns().getCooldownPercent(item.getDefaultInstance(), 0F))));
    }

    /* ============================================================ */
    /* ========================== Meta ============================= */
    /* ============================================================ */


    public int getInvasionSafeDays() {
        return meta().safeDays;
    }

    public void setInvasionSafeDays(int ticks) {
        meta().safeDays = Math.max(0, ticks);
    }

    public int getPlaySoundTick() {
        return meta().playSoundTick;
    }

    public void setPlaySoundTick(int tick) {
        meta().playSoundTick = Math.max(0, tick);
    }

    public boolean isStarterGiftGiven() { return meta().starterGiftGiven; }
    public void setStarterGiftGiven(boolean given) { meta().starterGiftGiven = given; }
    public int getLastSafeDaysNotified() { return meta().lastSafeDaysNotified; }
    public void setLastSafeDaysNotified(int days) { meta().lastSafeDaysNotified = days; }

    public String getLockedBiomeNotified() { return meta().lockedBiomeNotified; }
    public void setLockedBiomeNotified(String biomeId) { meta().lockedBiomeNotified = biomeId; }

    /* ============================================================ */
    /* ========================= accessor ========================== */
    /* ============================================================ */

    private PlayerStats stats() {
        return player.getData(PvZAttachments.PLAYER_STATS);
    }

    private PazState paz() {
        return player.getData(PvZAttachments.PLAYER_PAZ);
    }

    private BiomeUnlockState biomeUnlock() {
        return player.getData(PvZAttachments.PLAYER_BIOME_UNLOCK);
    }

    private PazCooldown cooldown() {
        return player.getData(PvZAttachments.PAZ_COOLDOWN);
    }

    private PlayerMeta meta() {
        return player.getData(PvZAttachments.PLAYER_META);
    }

    private void syncStats() {
        if (player instanceof ServerPlayer sp) {
            sp.syncData(PvZAttachments.PLAYER_STATS);
        }
    }

    private void syncPaz() {
        if (player instanceof ServerPlayer sp) {
            sp.syncData(PvZAttachments.PLAYER_PAZ);
        }
    }

    private void syncBiomeUnlock() {
        if (player instanceof ServerPlayer sp) {
            sp.syncData(PvZAttachments.PLAYER_BIOME_UNLOCK);
        }
    }
}