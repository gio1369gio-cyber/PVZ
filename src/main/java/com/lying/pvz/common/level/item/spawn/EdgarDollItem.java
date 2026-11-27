package com.lying.pvz.common.level.item.spawn;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.common.worldgen.BiomeProgression;
import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 埃德加玩偶：手持右键，按所在 PVZ 群系召唤对应 BOSS。
 *
 * <p><b>群系→BOSS 映射表</b>：四个 PVZ2 时空群系分别召唤对应的四足机甲 BOSS：
 * 远古埃及→狮身终结者、海盗港湾→甲板漫步者、狂野西部→战争马车、未来世界→明日破译者。
 * 其余未映射群系回退到 {@code Edgar090517}。
 * 以后要给某个群系换 BOSS，调用 {@link #registerBoss(ResourceKey, Supplier)} 覆盖即可。
 *
 * <p>设计要点（对齐需求）：
 * <ul>
 *   <li>玩偶只发一次、用后不消耗、不移除——反复使用只判定所在群系再召唤；</li>
 *   <li>防止重复召唤：附近 64 格已有存活 BOSS 时拒绝；</li>
 *   <li>服务端权威生成于玩家面前地表。</li>
 * </ul>
 */
public class EdgarDollItem extends Item {

    private static final int BOSS_CHECK_RADIUS = 64;

    /** 群系 → BOSS 类型映射。默认表见 {@link #defaultBoss()}，可运行时覆盖。 */
    private static final Map<ResourceKey<Biome>, Supplier<EntityType<? extends Entity>>> BOSS_MAP = new HashMap<>();

    public EdgarDollItem(Properties props) {
        super(props);
    }

    /** 覆盖某个群系的 BOSS。传 null 供应商可移除覆盖、回退默认。 */
    public static void registerBoss(ResourceKey<Biome> biome, Supplier<EntityType<? extends Entity>> boss) {
        if (boss == null) BOSS_MAP.remove(biome);
        else BOSS_MAP.put(biome, boss);
    }

    /** 取群系对应的 BOSS 类型；未映射则用默认。 */
    public static EntityType<? extends Entity> bossFor(ResourceKey<Biome> biome) {
        Supplier<EntityType<? extends Entity>> supplier = BOSS_MAP.get(biome);
        return supplier != null ? supplier.get() : defaultBoss();
    }

    static {
        // PVZ2 四时空群系 → 对应四足机甲 BOSS
        registerBoss(PvZBiome.ANCIENT_EGYPT, PvZEntityRegister.SPHINX_INATOR::get);
        registerBoss(PvZBiome.PIRATE_HARBOR, PvZEntityRegister.PLANK_WALKER::get);
        registerBoss(PvZBiome.WILD_WEST, PvZEntityRegister.WAR_WAGON::get);
        registerBoss(PvZBiome.FUTURE_WORLD, PvZEntityRegister.TOMORROW_TRON::get);
    }

    private static EntityType<? extends Entity> defaultBoss() {
        return PvZEntityRegister.EDGAR_090517.get();
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return super.use(level, player, hand);
        if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer sp)) {
            return InteractionResult.SUCCESS;
        }

        // 判定所在群系
        BlockPos pos = player.blockPosition();
        Holder<Biome> holder = serverLevel.getBiome(pos);
        ResourceKey<Biome> biomeKey = progressionKey(holder);
        if (biomeKey == null) {
            PlayerUtil.sendMsgTo(sp, Component.translatable("msg.pvz.edgar_doll.wrong_biome")
                    .withStyle(ChatFormatting.RED));
            return InteractionResult.FAIL;
        }

        // 防重复召唤：检测任意 BOSS 僵尸（Edgar 系列 / 狮身终结者）
        boolean bossAlive = !serverLevel.getEntitiesOfClass(
                AbstractBossZombieEntity.class,
                player.getBoundingBox().inflate(BOSS_CHECK_RADIUS),
                e -> !e.isDeadOrDying()
        ).isEmpty();
        if (bossAlive) {
            PlayerUtil.sendMsgTo(sp, Component.translatable("msg.pvz.edgar_doll.already")
                    .withStyle(ChatFormatting.RED));
            return InteractionResult.FAIL;
        }

        // 在玩家面前地表生成对应 BOSS
        var facing = player.getLookAngle();
        BlockPos spawnPos = pos.offset((int) (facing.x * 6), 0, (int) (facing.z * 6));
        spawnPos = serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnPos);
        EntityType<? extends Entity> bossType = bossFor(biomeKey);
        Entity boss = bossType.spawn(serverLevel, null, player, spawnPos,
                EntitySpawnReason.MOB_SUMMONED, true, true);
        if (boss == null) return InteractionResult.FAIL;

        PlayerUtil.sendMsgTo(sp, Component.translatable("msg.pvz.edgar_doll.success")
                .withStyle(ChatFormatting.GOLD));
        // 不消耗：玩偶是永久钥匙
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(Component.translatable("tooltip.pvz.edgar_doll")
                .withStyle(ChatFormatting.GRAY));
    }

    /** 从群系 Holder 反查解锁链 key（非链群系返回 null）。 */
    private static ResourceKey<Biome> progressionKey(Holder<Biome> biome) {
        for (ResourceKey<Biome> key : BiomeProgression.ORDER) {
            if (biome.is(key)) return key;
        }
        return null;
    }
}
