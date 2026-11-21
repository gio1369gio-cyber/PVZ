package com.lying.pvz.common.damage;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import javax.annotation.Nullable;

public class PvZDamageTypes {

    private PvZDamageTypes() {}

    /** datapack registry key：{@code pvz:generic} */
    public static final ResourceKey<DamageType> PVZ_GENERIC =
            ResourceKey.create(Registries.DAMAGE_TYPE, StringUtil.id("generic"));

    /** 服务端 registry 的 holder，用于构造需要网络编码的 DamageSource */
    private static volatile Holder<DamageType> serverHolder;
    /** 客户端 registry 的 holder，仅客户端本地使用 */
    private static volatile Holder<DamageType> clientHolder;

    /**
     * 无实体构造时的回退 holder。
     * 优先返回服务端 holder；服务端尚未 bind 时回退客户端 holder（避免类加载期崩溃）。
     */
    public static Holder<DamageType> genericHolder() {
        Holder<DamageType> h = serverHolder;
        if (h != null) {
            return h;
        }
        h = clientHolder;
        if (h != null) {
            return h;
        }
        throw new IllegalStateException(
                "[PVZ] PvZDamageTypes.PVZ_GENERIC holder 尚未绑定 registry。"
                        + "请确认在 ServerStartedEvent / 服务端 LevelEvent.Load 中调用 "
                        + "PvZDamageTypes.bind(..., false)");
    }

    /**
     * 注入 holder。
     *
     * @param registries registry 访问器
     * @param clientSide 是否为客户端 registry
     */
    public static void bind(@Nullable HolderLookup.Provider registries, boolean clientSide) {
        if (registries == null) {
            return;
        }
        Holder<DamageType> holder = registries.lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(PVZ_GENERIC);
        if (clientSide) {
            clientHolder = holder;
        } else {
            serverHolder = holder;
        }
    }
}