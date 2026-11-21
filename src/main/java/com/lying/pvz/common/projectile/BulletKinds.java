package com.lying.pvz.common.projectile;

import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.BulletStats;
import com.lying.pvz.api.projectile.VariantTable;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.BushBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 子弹 Kind 注册表。
 *
 * <p>EntityType → BulletKind 一一对应。Phase 2 只注册豌豆；后续阶段
 * 在这里追加卷心菜、西瓜、黄油等，不新增实体类。
 */
public final class BulletKinds {

    /* 豌豆变体 id（稳定值，网络只传 id）。 */
    public static final int PEA_NORMAL = 0;
    public static final int PEA_ICE = 1;
    public static final int PEA_FIRE = 2;
    public static final int PEA_BLUE_FIRE = 3;

    /* 西瓜变体 id。 */
    public static final int MELON_NORMAL = 0;
    public static final int MELON_ICE = 1;
    public static final int MELON_GOLD = 2;

    /* 卷心菜变体 id。 */
    public static final int CABBAGE_NORMAL = 0;
    public static final int CABBAGE_ICE = 1;

    /* 星星变体 id（大小 × 颜色）。 */
    public static final int STAR_NORMAL_YELLOW = 0;
    public static final int STAR_NORMAL_PINK = 1;
    public static final int STAR_BIG_YELLOW = 2;
    public static final int STAR_BIG_PINK = 3;
    public static final int STAR_HUGE_YELLOW = 4;
    public static final int STAR_HUGE_PINK = 5;

    /* 荆棘变体 id：mode*2 + powered。mode 0 普通 / 1 制导 / 2 自动。 */
    public static final int THORN_NORMAL = 0;
    public static final int THORN_NORMAL_POWERED = 1;
    public static final int THORN_GUIDE = 2;
    public static final int THORN_GUIDE_POWERED = 3;
    public static final int THORN_AUTO = 4;
    public static final int THORN_AUTO_POWERED = 5;

    private static final Map<EntityType<?>, BulletKind> KINDS = new HashMap<>();

    public static final BulletKind PEA = buildPea();
    public static final BulletKind CABBAGE = buildCabbage();
    public static final BulletKind KERNEL = buildKernel();
    public static final BulletKind BUTTER = buildButter();
    public static final BulletKind MELON = buildMelon();
    public static final BulletKind CORN = buildCorn();
    public static final BulletKind BALL = buildBall();
    public static final BulletKind FIRE_CRACKER = buildFireCracker();
    public static final BulletKind POTATO = buildPotato();
    public static final BulletKind NUT = buildNut();
    public static final BulletKind STAR = buildStar();
    public static final BulletKind SPORE = buildSpore();
    public static final BulletKind FUME = buildFume();
    public static final BulletKind THORN = buildThorn();
    public static final BulletKind METAL = buildMetal();
    public static final BulletKind BEE = buildBee();
    public static final BulletKind STONE_PEA = buildStonePea();
    public static final BulletKind LIGHT_BEAM = buildLightBeam();
    public static final BulletKind CHORUS_FRUIT = buildChorusFruit();


    private static boolean builtinRegistered = false;

    private BulletKinds() {
    }

    public static synchronized void register(EntityType<?> type, BulletKind kind) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(kind, "kind");
        if (KINDS.putIfAbsent(type, kind) != null) {
            throw new IllegalStateException("Duplicate BulletKind for " + type);
        }
    }

    public static synchronized void ensureBuiltin() {
        if (builtinRegistered) {
            return;
        }
        builtinRegistered = true;
        register(PvZEntityRegister.PEA.get(), PEA);
        register(PvZEntityRegister.CABBAGE.get(), CABBAGE);
        register(PvZEntityRegister.KERNEL.get(), KERNEL);
        register(PvZEntityRegister.BUTTER.get(), BUTTER);
        register(PvZEntityRegister.MELON.get(), MELON);
        register(PvZEntityRegister.CORN.get(), CORN);
        register(PvZEntityRegister.BALL.get(), BALL);
        register(PvZEntityRegister.FIRE_CRACKER.get(), FIRE_CRACKER);
        register(PvZEntityRegister.POTATO.get(), POTATO);
        register(PvZEntityRegister.NUT.get(), NUT);
        register(PvZEntityRegister.STAR.get(), STAR);
        register(PvZEntityRegister.SPORE.get(), SPORE);
        register(PvZEntityRegister.FUME.get(), FUME);
        register(PvZEntityRegister.THORN.get(), THORN);
        register(PvZEntityRegister.METAL.get(), METAL);
        register(PvZEntityRegister.BEE.get(), BEE);
        register(PvZEntityRegister.STONE_PEA.get(), STONE_PEA);
        register(PvZEntityRegister.LIGHT_BEAM.get(), LIGHT_BEAM);
        register(PvZEntityRegister.CHORUS_FRUIT.get(), CHORUS_FRUIT);
    }

    public static BulletKind get(EntityType<?> type) {
        ensureBuiltin();
        return KINDS.get(type);
    }

    /* ================================================================== */
    /* 豌豆                                                                */
    /* ================================================================== */

    private static BulletKind buildPea() {
        final VariantTable variants = new VariantTable(
                List.of(
                        new BulletKind.VariantDef(PEA_NORMAL, 1.0F, BulletVisuals.PEA, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "pea", bullet.self(), bullet.ownerEntity()).setAppease(),
                                List.of()),
                        new BulletKind.VariantDef(PEA_ICE, 1.0F, BulletVisuals.SNOW_PEA, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "snow_pea", bullet.self(), bullet.ownerEntity())
                                        .setAppease().setIceDamage(),
                                List.of()),
                        new BulletKind.VariantDef(PEA_FIRE, 1.5F, BulletVisuals.FLAME_PEA, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "flame_pea", bullet.self(), bullet.ownerEntity())
                                        .setAppease().setFlameDamage(),
                                List.of()),
                        new BulletKind.VariantDef(PEA_BLUE_FIRE, 1.75F, BulletVisuals.BLUE_FLAME_PEA, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "flame_pea", bullet.self(), bullet.ownerEntity())
                                        .setAppease().setFlameDamage(),
                                List.of())),
                Map.of(
                        new BulletKind.TransitionKey(PEA_NORMAL, BulletTriggers.HEAT), PEA_FIRE,
                        new BulletKind.TransitionKey(PEA_ICE, BulletTriggers.HEAT), PEA_NORMAL,
                        new BulletKind.TransitionKey(PEA_NORMAL, BulletTriggers.BLUE_HEAT), PEA_FIRE,
                        new BulletKind.TransitionKey(PEA_ICE, BulletTriggers.BLUE_HEAT), PEA_FIRE,
                        new BulletKind.TransitionKey(PEA_FIRE, BulletTriggers.BLUE_HEAT), PEA_BLUE_FIRE
                )
        );

        return BulletKind.builder(BulletVisuals.PEA_KIND)
                .stats(new BulletStats(
                        20.0F, 1.5F, 40, 0.0F,
                        EntityDimensions.scalable(0.25F, 0.25F),
                        0, 0.0D, false))
                .motion(BulletMotions.direct())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .transformerTag(BulletVisuals.HEATABLE)
                .passThrough(true)
                .build();
    }

    /* ================================================================== */
    /* 投手系                                                              */
    /* ================================================================== */

    private static BulletKind buildCabbage() {
        final VariantTable variants = new VariantTable(
                List.of(
                        new BulletKind.VariantDef(CABBAGE_NORMAL, 1.0F, BulletVisuals.CABBAGE, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "cabbage", bullet.self(), bullet.ownerEntity()).setParabola(),
                                List.of()),
                        new BulletKind.VariantDef(CABBAGE_ICE, 1.0F, BulletVisuals.ICE_CABBAGE, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "ice_cabbage", bullet.self(), bullet.ownerEntity())
                                        .setParabola().setIceDamage(),
                                List.of())
                ),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.CABBAGE_KIND)
                .stats(lobStats(20.0F, 0.4F))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .build();
    }

    private static BulletKind buildKernel() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 0.5F, BulletVisuals.KERNEL, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "kernel", bullet.self(), bullet.ownerEntity()).setParabola(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.KERNEL_KIND)
                .stats(lobStats(20.0F, 0.4F))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .build();
    }

    private static BulletKind buildButter() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.BUTTER, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "butter", bullet.self(), bullet.ownerEntity()).setParabola(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.BUTTER_KIND)
                .stats(lobStats(20.0F, 0.5F))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .build();
    }

    private static BulletKind buildMelon() {
        final VariantTable variants = new VariantTable(
                List.of(
                        new BulletKind.VariantDef(MELON_NORMAL, 1.0F, BulletVisuals.MELON, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "melon", bullet.self(), bullet.ownerEntity()).setParabola(),
                                List.of()),
                        new BulletKind.VariantDef(MELON_ICE, 1.0F, BulletVisuals.FROZEN_MELON, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "winter_melon", bullet.self(), bullet.ownerEntity())
                                        .setParabola().setIceDamage(),
                                List.of()),
                        new BulletKind.VariantDef(MELON_GOLD, 1.0F, BulletVisuals.GOLDEN_MELON, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "golden_melon", bullet.self(), bullet.ownerEntity())
                                        .setParabola(),
                                List.of())
                ),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.MELON_KIND)
                .stats(lobStats(40.0F, 0.8F))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .impact(BulletImpacts.splash(3.0D, 0.5F))
                .impact(BulletImpacts.spawnParticle(v -> v == MELON_ICE ? 2 : 1, 10))
                .build();
    }

    private static BulletKind buildCorn() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.CORN, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "corn", bullet.self(), bullet.ownerEntity())
                                .setParabola().setExplosion(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.CORN_KIND)
                .stats(new BulletStats(
                        100.0F, 1.5F, 200, 0.1F,
                        EntityDimensions.scalable(0.5F, 0.5F),
                        0, 0.0D, false))
                .motion(BulletMotions.lob(20.0F))
                .variants(variants)
                .impact(BulletImpacts.spawnParticle(8, 3))
                .impact(BulletImpacts.spawnParticle(9, 20))
                .impact(BulletImpacts.explode(4.0D, PvZSoundRegister.CHERRY_BOMB.get()))
                .build();
    }

    private static BulletKind buildBall() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.BALL, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "ball", bullet.self(), bullet.ownerEntity()).setParabola(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.BALL_KIND)
                .stats(lobStats(20.0F, 0.4F))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .build();
    }

    private static BulletKind buildFireCracker() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.FIRE_CRACKER, null,
                        (bullet, target) -> PvZEntityDamageSource.explode(
                                bullet.self(), bullet.ownerEntity()),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.FIRE_CRACKER_KIND)
                .stats(new BulletStats(
                        60.0F, 1.5F, 200, 0.1F,
                        EntityDimensions.scalable(0.3F, 0.3F),
                        0, 0.0D, false))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.explode(3.0D, PvZSoundRegister.POTATO_MINE.get(), 5))
                .build();
    }

    private static BulletKind buildPotato() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.POTATO, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "cabbage", bullet.self(), bullet.ownerEntity()).setParabola(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.POTATO_KIND)
                .stats(new BulletStats(
                        0.0F, 1.5F, 120, 0.06F,
                        EntityDimensions.scalable(0.4F, 0.4F),
                        0, 0.0D, false))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.spawnPlantOnBlock(PvZEntityRegister.POTATO_MINE.get(), 0.0D, 1))
                .build();
    }

    private static BulletKind buildNut() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.NUT, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "cabbage", bullet.self(), bullet.ownerEntity()).setParabola(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.NUT_KIND)
                .stats(new BulletStats(
                        2.0F, 1.5F, 80, 0.06F,
                        EntityDimensions.scalable(0.4F, 0.4F),
                        0, 0.0D, false))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.spawnPlantOnBlock(PvZEntityRegister.WALL_NUT.get(), 1.0D, 12))
                .build();
    }

    private static BulletKind buildStar() {
        final VariantTable variants = new VariantTable(
                List.of(
                        starVariant(STAR_NORMAL_YELLOW, BulletVisuals.STAR_YELLOW, 0.2F),
                        starVariant(STAR_NORMAL_PINK, BulletVisuals.STAR_PINK, 0.2F),
                        starVariant(STAR_BIG_YELLOW, BulletVisuals.STAR_BIG_YELLOW, 0.5F),
                        starVariant(STAR_BIG_PINK, BulletVisuals.STAR_BIG_PINK, 0.5F),
                        starVariant(STAR_HUGE_YELLOW, BulletVisuals.STAR_HUGE_YELLOW, 0.8F),
                        starVariant(STAR_HUGE_PINK, BulletVisuals.STAR_HUGE_PINK, 0.8F)
                ),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.STAR_KIND)
                .stats(new BulletStats(
                        0.0F, 1.5F, 30, 0.0F,
                        EntityDimensions.scalable(0.2F, 0.2F),
                        0, 0.0D, false))
                .motion(BulletMotions.direct())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .build();
    }

    private static BulletKind.VariantDef starVariant(int id, Identifier visual, float width) {
        return new BulletKind.VariantDef(id, 1.0F, visual,
                EntityDimensions.scalable(width, 0.2F),
                (bullet, target) -> new PvZEntityDamageSource(
                        "star", bullet.self(), bullet.ownerEntity()).setAppease(),
                List.of());
    }

    private static BulletKind buildSpore() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.SPORE, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "spore", bullet.self(), bullet.ownerEntity()).setAppease(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.SPORE_KIND)
                .stats(new BulletStats(
                        0.0F, 1.5F, 24, 0.0012F,
                        EntityDimensions.scalable(0.25F, 0.25F),
                        0, 0.0D, false))
                .motion(BulletMotions.direct())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .trail(new BulletKind.TrailSpec(() -> PvZParticleRegister.SPORE.get(), 0F, 1))
                .build();
    }

    private static BulletKind buildFume() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.SPORE, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "fume", bullet.self(), bullet.ownerEntity())
                                .setAppease().setThroughDamage(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.FUME_KIND)
                .stats(new BulletStats(
                        0.0F, 1.5F, 10, 0.002F,
                        EntityDimensions.scalable(0.25F, 0.25F),
                        -1, 0.0D, false))
                .motion(BulletMotions.direct())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .impact(BulletImpacts.knockback())
                .ignoreBlocks(state -> state.getBlock() instanceof BushBlock)
                .trail(new BulletKind.TrailSpec(() -> PvZParticleRegister.FUME.get(), 0.05F, 3))
                .build();
    }

    private static BulletKind buildThorn() {
        final VariantTable variants = new VariantTable(
                List.of(
                        thornVariant(THORN_NORMAL, BulletVisuals.THORN),
                        thornVariant(THORN_NORMAL_POWERED, BulletVisuals.POWER_THORN),
                        thornVariant(THORN_GUIDE, BulletVisuals.THORN),
                        thornVariant(THORN_GUIDE_POWERED, BulletVisuals.POWER_THORN),
                        thornVariant(THORN_AUTO, BulletVisuals.THORN_AUTO),
                        thornVariant(THORN_AUTO_POWERED, BulletVisuals.POWER_THORN_AUTO)
                ),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.THORN_KIND)
                .stats(new BulletStats(
                        20.0F, 0.55F, 150, 0.0F,
                        EntityDimensions.scalable(0.3F, 0.3F),
                        0, 0.0D, true))
                .motion(BulletMotions.thorn())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .impact(BulletImpacts.variantSwap(THORN_GUIDE, THORN_NORMAL))
                .impact(BulletImpacts.variantSwap(THORN_GUIDE_POWERED, THORN_NORMAL_POWERED))
                .ignoreBlocks(state -> true)
                .build();
    }

    private static BulletKind.VariantDef thornVariant(int id, Identifier visual) {
        return new BulletKind.VariantDef(id, 1.0F, visual, null,
                (bullet, target) -> PvZEntityDamageSource.causeThornDamage(
                        bullet.self(), bullet.ownerEntity()),
                List.of());
    }

    private static BulletKind buildMetal() {
        final List<BulletKind.VariantDef> defs = new ArrayList<>();
        final MetalTypes[] types = MetalTypes.values();
        for (int i = 0; i < types.length; i++) {
            final int id = i;
            defs.add(new BulletKind.VariantDef(id, 1.0F, BulletVisuals.METAL, null,
                    (bullet, target) -> new PvZEntityDamageSource(
                            "metal", bullet.self(), bullet.ownerEntity()).setAppease(),
                    List.of()));
        }
        return BulletKind.builder(BulletVisuals.METAL_KIND)
                .stats(new BulletStats(
                        0.0F, 0.8F, 0, 0.0F,
                        EntityDimensions.scalable(0.3F, 0.3F),
                        0, 0.0D, false))
                .motion(BulletMotions.metal())
                .variants(new VariantTable(defs, Map.of()))
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.sound(PvZSoundRegister.METAL_HIT.get()))
                .build();
    }

    /* ================================================================== */
    /* 新植物弹药                                                            */
    /* ================================================================== */

    /**
     * 蜜蜂（追踪弹）。复用荆棘运动的制导模式：变体 id 2 → mode 1（制导），
     * 始终追踪目标，命中即消耗。变体 0 仅为 {@link VariantTable} 的默认兜底。
     */
    private static BulletKind buildBee() {
        final VariantTable variants = new VariantTable(
                List.of(
                        new BulletKind.VariantDef(0, 1.0F, BulletVisuals.BEE, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "bee", bullet.self(), bullet.ownerEntity()),
                                List.of()),
                        new BulletKind.VariantDef(2, 1.0F, BulletVisuals.BEE, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "bee", bullet.self(), bullet.ownerEntity()),
                                List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.BEE_KIND)
                .stats(new BulletStats(
                        0.0F, 0.9F, 500, 0.0F,
                        EntityDimensions.scalable(0.4F, 0.4F),
                        0, 0.0D, false))
                .motion(BulletMotions.thorn())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .build();
    }

    /**
     * 原始豌豆（石豌豆）。NORMAL 命中减速；FIRE 火焰伤害并减速。
     */
    private static BulletKind buildStonePea() {
        final MobEffectInstance slow = new MobEffectInstance(MobEffects.SLOWNESS, 40, 8);
        final VariantTable variants = new VariantTable(
                List.of(
                        new BulletKind.VariantDef(0, 1.0F, BulletVisuals.STONE_PEA, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "stone_pea", bullet.self(), bullet.ownerEntity()).setAppease(),
                                List.of(new BulletKind.EffectSpec(slow))),
                        new BulletKind.VariantDef(1, 1.0F, BulletVisuals.MAGMA_PEA, null,
                                (bullet, target) -> new PvZEntityDamageSource(
                                        "magma_pea", bullet.self(), bullet.ownerEntity())
                                        .setAppease().setFlameDamage(),
                                List.of(new BulletKind.EffectSpec(slow)))),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.STONE_PEA_KIND)
                .stats(new BulletStats(
                        0.0F, 1.5F, 40, 0.0F,
                        EntityDimensions.scalable(0.4F, 0.4F),
                        0, 0.0D, false))
                .motion(BulletMotions.direct())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .build();
    }

    /**
     * 放大镜草光束。直线、命中即消耗。
     *
     * <p>用发光粒子（末地烛）做拖尾，使飞行路径清晰可见（否则微小实体
     * 高速飞过几乎不可见）。
     */
    private static BulletKind buildLightBeam() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.LIGHT_BEAM, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "light_beam", bullet.self(), bullet.ownerEntity()),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.LIGHT_BEAM_KIND)
                .stats(new BulletStats(
                        0.0F, 1.0F, 40, 0.0F,
                        EntityDimensions.scalable(0.3F, 0.3F),
                        0, 0.0D, false))
                .motion(BulletMotions.direct())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .trail(new BulletKind.TrailSpec(() -> ParticleTypes.END_ROD, 0.03F, 2))
                .build();
    }

    /**
     * 合唱果（投掷弹）。抛物线 + 伤害 + 溅射（半额）。
     */
    private static BulletKind buildChorusFruit() {
        final VariantTable variants = new VariantTable(
                List.of(new BulletKind.VariantDef(0, 1.0F, BulletVisuals.CHORUS_FRUIT, null,
                        (bullet, target) -> new PvZEntityDamageSource(
                                "chorus_fruit", bullet.self(), bullet.ownerEntity()).setParabola(),
                        List.of())),
                Map.of()
        );
        return BulletKind.builder(BulletVisuals.CHORUS_FRUIT_KIND)
                .stats(lobStats(0.0F, 0.6F))
                .motion(BulletMotions.lob())
                .variants(variants)
                .impact(BulletImpacts.damage())
                .impact(BulletImpacts.effectFromVariant())
                .impact(BulletImpacts.splash(2.5D, 0.5F))
                .build();
    }

    private static BulletStats lobStats(float damage, float hitboxSize) {
        return new BulletStats(
                damage, 1.5F, 200, 0.1F,
                EntityDimensions.scalable(hitboxSize, hitboxSize),
                0, 0.0D, false);
    }
}
