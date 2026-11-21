package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.common.attachment.challenge.RaiderData;
import com.lying.pvz.common.attachment.player.*;
import com.lying.pvz.common.invasion.InvasionSnapshot;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * PVZ 的 Data Attachment 注册入口。
 *
 */
public final class PvZAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PvZMod.MOD_ID);

    /** 玩家资源（等级 / 阳光 / 金币 / 槽位 / 分组等）。sync 全量。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerStats>> PLAYER_STATS =
            ATTACHMENT_TYPES.register("player_stats",
                    () -> AttachmentType.serializable(PlayerStats::new)
                            .copyOnDeath()
                            .sync(PlayerStats.STREAM_CODEC)
                            .build());

    /** PAZ 解锁状态。sync 全量（低频）。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PazState>> PLAYER_PAZ =
            ATTACHMENT_TYPES.register("player_paz",
                    () -> AttachmentType.serializable(PazState::new)
                            .copyOnDeath()
                            .sync(PazState.STREAM_CODEC)
                            .build());

    /** 玩家群系解锁状态（玩家 NBT）。sync 全量，新玩家默认解锁远古埃及。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BiomeUnlockState>> PLAYER_BIOME_UNLOCK =
            ATTACHMENT_TYPES.register("player_biome_unlock",
                    () -> AttachmentType.serializable(BiomeUnlockState::new)
                            .copyOnDeath()
                            .sync(BiomeUnlockState.STREAM_CODEC)
                            .build());

    /** PAZ 冷却账本。权威走 vanilla {@code ItemCooldowns}，此附件登出保存、登录恢复。不 sync。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PazCooldown>> PAZ_COOLDOWN =
            ATTACHMENT_TYPES.register("paz_cooldown",
                    () -> AttachmentType.serializable(PazCooldown::new)
                            .copyOnDeath()
                            .build());

    /** 玩家杂项（入侵安全期 / 音效节流 / lastVersion）。纯服务端。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerMeta>> PLAYER_META =
            ATTACHMENT_TYPES.register("player_meta",
                    () -> AttachmentType.serializable(PlayerMeta::new)
                            .copyOnDeath()
                            .build());

    /** 入侵展示态快照。仅 sync，不落盘。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<InvasionSnapshot>> INVASION_SNAPSHOT =
            ATTACHMENT_TYPES.register("invasion_snapshot",
                    () -> AttachmentType.builder(() -> InvasionSnapshot.IDLE)
                            .sync(InvasionSnapshot.STREAM_CODEC)
                            .build());

    /** 实体级挑战归属。无 sync。 */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<RaiderData>> CHALLENGE_DATA =
            ATTACHMENT_TYPES.register("challenge_data",
                    () -> AttachmentType.serializable(() -> new RaiderData())
                            .build());


}



