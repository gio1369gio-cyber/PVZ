package com.lying.pvz.common.level.item.tool;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.worldgen.BiomeProgression;
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
import net.minecraft.world.level.levelgen.RandomState;
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
 * <p>落点用 {@code findBiomeHorizontal} 定位群系后，再在附近找非水地表，避免落在海里。
 */
public final class TimeKeyTeleport {

    private static final int SEARCH_RADIUS = 6400;
    /** 与 GardenCompassItem 一致：findClosest 模式逐圈采样边缘，step 太小会卡主线程。 */
    private static final int SEARCH_STEP = 8;
    /** 群系噪声采样的 Y（用维度海平面附近，而非玩家当前 Y，避免高度噪声偏差）。 */
    private static final int SAMPLE_Y = 64;
    /** 落点周围找非水地表的搜索半径。 */
    private static final int LAND_SEARCH_RADIUS = 192;
    /** 最多强制生成多少个区块，防止主线程卡顿。 */
    private static final int MAX_CHUNKS_TO_GENERATE = 64;

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

        BlockPos landPos = findLandInBiome(pvzLevel, player.blockPosition(), target);
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
        BlockPos landPos = findLandInBiome(pvzLevel, player.blockPosition(), target);
        if (landPos == null) {
            PlayerUtil.sendMsgTo(player, Component.translatable("msg.pvz.time_key.locate_fail")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        doTeleport(player, pvzLevel, landPos);
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

    /** 定位目标群系并在附近找非水地表；找不到返回 null。 */
    @Nullable
    private static BlockPos findLandInBiome(ServerLevel level, BlockPos origin, ResourceKey<Biome> target) {
        RandomState randomState = level.getChunkSource().randomState();
        // 用固定采样 Y（维度海平面附近），避免玩家当前 Y 影响 3D 噪声采样
        var result = level.getChunkSource().getGenerator().getBiomeSource().findBiomeHorizontal(
                origin.getX(), SAMPLE_Y, origin.getZ(),
                SEARCH_RADIUS, SEARCH_STEP,
                holder -> holder.is(target), level.getRandom(), true, randomState);
        if (result == null) {
            PvZMod.LOGGER.warn("[TimeKey] findBiomeHorizontal returned null for {} near {}",
                    target.identifier(), origin);
            return null;
        }

        BlockPos center = result.getFirst();
        PvZMod.LOGGER.info("[TimeKey] found {} at {}, searching land...", target.identifier(), center);

        // 阶段1：目标群系内找非水地表（地表方块非空气、非液体）
        BlockPos land = searchLandNear(level, center, target, LAND_SEARCH_RADIUS);
        if (land != null) return land;

        // 阶段2：该群系整片是海时，放宽群系限制，在更大范围找任意陆地
        PvZMod.LOGGER.warn("[TimeKey] no land within {} biome, widening search (ignoring biome)", target.identifier());
        land = searchLandNear(level, center, null, LAND_SEARCH_RADIUS * 3);
        if (land != null) {
            PvZMod.LOGGER.info("[TimeKey] widened land at {} (biome {}) for target {}",
                    land, level.getBiome(land).unwrapKey().map(k -> k.identifier().toString()).orElse("?"),
                    target.identifier());
            return land;
        }

        PvZMod.LOGGER.warn("[TimeKey] no land found at all around {}", center);
        return null;
    }

    /** 在 center 附近找非水地表。requiredBiome 为 null 时不限制群系。 */
    @Nullable
    private static BlockPos searchLandNear(ServerLevel level, BlockPos center,
                                           @Nullable ResourceKey<Biome> requiredBiome, int radius) {
        // 目标区块通常尚未生成：getBlockState 对未生成区块返回空气、heightmap 返回维度底部，
        // 必须先强制生成区块才能真正判定地表。按区块步进，限制生成数量避免卡顿。
        final int STEP = 16;
        int generated = 0;
        for (int dx = -radius; dx <= radius; dx += STEP) {
            for (int dz = -radius; dz <= radius; dz += STEP) {
                BlockPos candidate = new BlockPos(center.getX() + dx, SAMPLE_Y, center.getZ() + dz);
                if (requiredBiome != null && !level.getBiome(candidate).is(requiredBiome)) continue;
                if (generated++ >= MAX_CHUNKS_TO_GENERATE) return null;

                // 强制生成候选所在区块（FULL），之后高度图/地表方块才有效
                level.getChunk(SectionPos.blockToSectionCoord(candidate.getX()),
                        SectionPos.blockToSectionCoord(candidate.getZ()));

                BlockPos surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candidate);
                // surface 是最高阻挡方块上方一格；脚下地表方块必须非空气、非液体
                BlockState ground = level.getBlockState(surface.below());
                if (!ground.isAir() && !ground.liquid()) {
                    PvZMod.LOGGER.info("[TimeKey] land at {} (generated {} chunks)", surface, generated);
                    return surface;
                }
            }
        }
        PvZMod.LOGGER.info("[TimeKey] searched {} chunks, no land (biome {})",
                generated, requiredBiome == null ? "any" : requiredBiome.identifier().toString());
        return null;
    }
}
