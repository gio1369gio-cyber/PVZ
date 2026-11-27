package com.lying.pvz.common.level.item.tool;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.worldgen.BiomeProgression;
import com.lying.pvz.common.worldgen.biome.PvZIslandBiomeSource;
import com.lying.pvz.data.bootstrap.worldgen.dimension.PvZDimension;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.portal.TeleportTransition;

import javax.annotation.Nullable;

/**
 * TimeKeyItem 的传送逻辑：手持钥匙点击 Penny 时调用。
 *
 * <p>两种场景：
 * <ul>
 *   <li><b>主世界</b>：传送到 pvz_2 维度的起始群系（远古埃及），地表非水；首次进入发放一个埃德加玩偶。</li>
 *   <li><b>pvz_2 维度内</b>：在已解锁群系链中循环切到下一个，地表非水。</li>
 * </ul>
 *
 * <p><b>落点固定化</b>：每个群系的落点只在「首次到达」时用 {@code findBiomeHorizontal} 定位一次，
 * 随即记入玩家数据（{@link PlayerDataManager#setBiomeAnchor}）。之后再次传送到该群系直接复用记忆坐标，
 * 不再随机找落点——避免误触 TimeKey 时被乱传到群系的另一处。
 */
public final class TimeKeyTeleport {

    /** 岛心采样 Y（末地剖面：岛体在 y≈32~56 最厚，64 在岛面上方附近）。 */
    private static final int SAMPLE_Y = 64;

    private TimeKeyTeleport() {}

    /** 手持 TimeKeyItem 点击 Penny 时调用（服务端）。 */
    public static void onKeyUsed(ServerPlayer player, ServerLevel pennyLevel) {
        var opt = PlayerUtil.getOptManager(player);
        if (opt.isEmpty()) return;
        PlayerDataManager mgr = opt.get();

        ResourceKey<Biome> target;
        ServerLevel pvzLevel;
        // 是否因本次从主世界进入新维度而需要发玩偶（传送成功后才发）
        boolean grantDollOnArrival = false;

        if (pennyLevel.dimension() == Level.OVERWORLD) {
            // 主世界 → 新维度埃及
            target = BiomeProgression.start();
            pvzLevel = getPvzLevel(player);
            if (pvzLevel == null) {
                PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.time_key.dimension_missing")
                        .withStyle(ChatFormatting.RED));
                return;
            }
            grantDollOnArrival = !mgr.isEdgarDollGiven();
        } else {
            // pvz_2 内（点 Penny）→ 下一个已解锁群系
            teleportToNextUnlocked(player, pennyLevel, mgr);
            return;
        }

        BlockPos landPos = resolveLandingPos(player, pvzLevel, target, mgr);
        if (landPos == null) {
            PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.time_key.locate_fail")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        doTeleport(player, pvzLevel, landPos);

        // 传送成功后才发玩偶：避免落点查找失败时"标志已置但玩偶没到手"
        if (grantDollOnArrival && !mgr.isEdgarDollGiven()) {
            player.addItem(new ItemStack(PvZItemRegister.EDGAR_DOLL.get()));
            mgr.setEdgarDollGiven(true);
            PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.time_key.got_doll")
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    /**
     * 手持 TimeKeyItem 在 PVZ 群系内右键空气时调用（服务端）。
     * 前置：必须在 pvz_2 维度、当前群系已解锁，否则不传送。
     */
    public static void onRightClickAir(ServerPlayer player, ServerLevel level) {
        // 只在 pvz_2 维度生效（主世界仍需点 Penny 进入）
        if (level.dimension() != pvzDimension()) return;

        Holder<Biome> holder = level.getBiome(player.blockPosition());
        ResourceKey<Biome> key = progressionKey(holder);
        if (key == null) return; // 非 PVZ 群系不响应

        var opt = PlayerUtil.getOptManager(player);
        if (opt.isEmpty()) return;
        PlayerDataManager mgr = opt.get();

        // 前提：当前群系已解锁
        if (!mgr.isBiomeUnlocked(key)) {
            PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.time_key.biome_locked")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        teleportToNextUnlocked(player, level, mgr);
    }

    /** pvz_2 内切到下一个已解锁群系（点 Penny 与右键空气共用）。 */
    private static void teleportToNextUnlocked(ServerPlayer player, ServerLevel pvzLevel, PlayerDataManager mgr) {
        ResourceKey<Biome> target = nextUnlockedBiome(pvzLevel, player, mgr);
        if (target == null) {
            PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.time_key.no_target")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        BlockPos landPos = resolveLandingPos(player, pvzLevel, target, mgr);
        if (landPos == null) {
            PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.time_key.locate_fail")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        doTeleport(player, pvzLevel, landPos);
    }

    /**
     * 解析目标群系的固定落点：优先复用已记忆坐标；首次到达才定位一次并记录。
     *
     * <p>这样反复使用 TimeKey 时落点恒定，避免误触被随机传送到群系另一处。
     */
    @Nullable
    private static BlockPos resolveLandingPos(ServerPlayer player, ServerLevel pvzLevel,
                                              ResourceKey<Biome> target, PlayerDataManager mgr) {
        final BlockPos remembered = mgr.getBiomeAnchor(target);
        if (remembered != null) return remembered;

        final BlockPos land = findIslandSurface(pvzLevel, target);
        if (land != null) {
            mgr.setBiomeAnchor(target, land); // 首次到达：记住该岛心落点
        }
        return land;
    }

    private static void doTeleport(ServerPlayer player, ServerLevel level, BlockPos landPos) {
        Vec3 pos = Vec3.atBottomCenterOf(landPos.above());
        TeleportTransition transition = new TeleportTransition(
                level, pos, Vec3.ZERO, player.getYRot(), player.getXRot(),
                TeleportTransition.PLAY_PORTAL_SOUND);
        player.teleport(transition);
    }

    private static ResourceKey<Level> pvzDimension() {
        return ResourceKey.create(Registries.DIMENSION, PvZDimension.PVZ_2.identifier());
    }

    /** 从群系 Holder 反查解锁链 key（非链群系返回 null）。 */
    @Nullable
    private static ResourceKey<Biome> progressionKey(Holder<Biome> biome) {
        for (ResourceKey<Biome> key : BiomeProgression.ORDER) {
            if (biome.is(key)) return key;
        }
        return null;
    }

    @Nullable
    private static ServerLevel getPvzLevel(ServerPlayer player) {
        return player.level().getServer().getLevel(
                ResourceKey.create(Registries.DIMENSION, PvZDimension.PVZ_2.identifier()));
    }

    /**
     * 在已解锁链中，返回「除当前群系外」的下一个已解锁群系；到末尾回起始。
     * 若当前群系是唯一已解锁群系（找不到别的），返回 null —— 避免在当前群系原地传送。
     */
    @Nullable
    private static ResourceKey<Biome> nextUnlockedBiome(ServerLevel level, ServerPlayer player, PlayerDataManager mgr) {
        Holder<Biome> currentHolder = level.getBiome(player.blockPosition());
        ResourceKey<Biome> currentKey = progressionKey(currentHolder);
        int currentIndex = BiomeProgression.indexOf(currentHolder);
        if (currentIndex < 0) currentIndex = -1;

        // 从当前群系的下一个开始，最多遍历整圈（排除当前群系本身）
        for (int step = 1; step <= BiomeProgression.ORDER.size(); step++) {
            int idx = (currentIndex + step) % BiomeProgression.ORDER.size();
            ResourceKey<Biome> candidate = BiomeProgression.ORDER.get(idx);
            if (candidate.equals(currentKey)) continue; // 跳过当前群系，避免原地传送
            if (mgr.isBiomeUnlocked(candidate)) return candidate;
        }
        return null;
    }

    /**
     * 取目标群系所在浮岛的固定岛心地表：强制生成岛心那一个区块后读高度图。
     *
     * <p>新维度是末地式四浮岛（岛心固定，见 {@link PvZIslandBiomeSource#centerOf}），
     * 不再需要 findBiomeHorizontal + 半径搜索 + 群系放宽那套脆弱逻辑。
     * 岛心一定在岛内（密度最大），只需生成一个区块即可拿到真实地表。
     */
    @Nullable
    private static BlockPos findIslandSurface(ServerLevel level, ResourceKey<Biome> target) {
        BlockPos center = PvZIslandBiomeSource.centerOf(target);
        int cx = center.getX();
        int cz = center.getZ();

        // 目标区块通常未生成：先强制生成岛心区块，高度图才有效（否则返回维度底部）
        level.getChunk(SectionPos.blockToSectionCoord(cx), SectionPos.blockToSectionCoord(cz));

        BlockPos probe = new BlockPos(cx, SAMPLE_Y, cz);
        BlockPos surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, probe);
        BlockState ground = level.getBlockState(surface.below());
        if (!ground.isAir() && !ground.liquid()) {
            PvZMod.LOGGER.info("[TimeKey] island surface for {} at {} (ground {})",
                    target.identifier(), surface, ground.getBlock());
            return surface;
        }

        // 兜底：岛心正上方直接用采样 Y（末地剖面中心岛体在 y≈32~64，64 通常落在岛面上）
        PvZMod.LOGGER.warn("[TimeKey] island surface empty at {}, falling back to Y={}", center, SAMPLE_Y);
        return probe;
    }
}
