package com.lying.pvz.api.player;

import com.lying.pvz.utils.enums.Resources;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * 玩家数据只读 / 可写视图。
 *
 * <p>由主模组通过 {@code AttachmentType.serializable(...).sync(...)} 挂到 {@code Player} 上。
 * 附属模组读取或修改玩家资源时，通过 {@code player.getData(PVZAttachments.PLAYER_DATA)}
 * 拿到本接口实现，而不是直接访问实现层的 {@code PlayerDataManager}。
 *
 * <p><b>写入语义</b>：所有 {@code setXxx} / {@code addXxx} 都只修改服务端权威数据，
 * 客户端同步由服务端在主线程节流触发，不在本接口暴露同步细节。
 */
public interface IPlayerData {

    /* ---- 玩家资源 ---- */

    int getResource(Resources res);

    void setResource(Resources res, int value);

    void addResource(Resources res, int delta);

    /* ---- PAZ 解锁 ---- */

    boolean isPAZUnlocked(Identifier pazID);

    void setPAZUnlocked(Identifier pazID, boolean unlocked);

    /* ---- 卡片冷却 ---- */

    int getPAZCardCD(Identifier pazID);

    float getPAZCardBar(Identifier pazID);

    void setPAZCardCD(Identifier pazID, int cd);

    void setPAZCardBar(Identifier pazID, float bar);

    /* ---- 卡片背包 ---- */

    ItemStack getCardAt(int slot);

    void setCardAt(int slot, ItemStack stack);

    int getCurrentSlot();

    void setCurrentSlot(int slot);

    /* ---- 同步状态 ---- */

    /**
     * 服务端权威数据是否有未同步到客户端的变更。
     *
     * <p>客户端调用恒返回 {@code false}（客户端不维护 dirty 标记）。
     * 服务端用于判断是否需要触发同步。
     */
    boolean isDirty();

    /**
     * 上次同步到客户端的游戏 tick。
     *
     * <p>客户端可据此估算数据新鲜度，做乐观 UI。
     * {@code 0} 表示从未同步。
     */
    long getLastSyncTick();

    /* ---- 版本迁移标记 ---- */

    /**
     * 上次登录时的 PVZ mod 版本。
     *
     * <p>用于数据迁移：{@code INIT_VERSION} 表示首次登录；
     * 不等于当前 {@code PVZ.MOD_VERSION} 表示跨版本升级。
     */
    String getLastVersion();

    void setLastVersion(String version);
}