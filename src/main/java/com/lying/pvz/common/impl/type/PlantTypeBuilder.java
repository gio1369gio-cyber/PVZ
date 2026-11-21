package com.lying.pvz.common.impl.type;


import com.lying.pvz.api.content.card.ICardPlacement;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.PlantCategory;
import com.lying.pvz.api.content.paz.PlantCombatStyle;
import com.lying.pvz.api.content.paz.PlantTrait;
import com.lying.pvz.api.entity.plant.IPlantInfo;
import com.lying.pvz.common.impl.Placements;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * {@link IPlantType} 的构建器。
 *
 * <p><b>关于 placement 的推导</b>：
 * <ul>
 *   <li>{@link #placement(ICardPlacement)} 是<b>显式设置</b>，优先级最高，覆盖任何推导。</li>
 *   <li>未显式设置时，{@code build()} 按 {@link #traits} 推导（详见 {@link #resolvePlacement()}）：
 *       {@link PlantTrait#OUTER} / {@link PlantTrait#WATER} → {@link Placements#NONE}；
 *       {@link PlantTrait#SHROOM} → {@link Placements#SHROOM}；
 *       {@link PlantTrait#GOLD_PLACEABLE} → {@link Placements#GOLD}；
 *       否则 {@link Placements#COMMON}。</li>
 *   <li><b>trait 是唯一真相</b>：{@link #isShroomPlant()} / {@link #isWaterPlant()} /
 *       {@link #outerPlant(Supplier)} 只向 {@link #traits} 追加标记，
 *       placement 推导在 {@code build()} 时统一完成，不受 setter 调用顺序影响。</li>
 * </ul>
 *
 * <p><b>关于 {@link #outerPlant(Supplier)} 的契约</b>：传入的 Supplier 每次调用必须返回
 * <b>新实例</b>。{@link PlantTypeImpl#getOuterPlantInfo()} 直接返回该 Supplier，
 * 不做任何缓存，由调用方负责每次 get 出新实例。
 *
 * <p><b>关于 builder 复用</b>：每次 {@code DeferredRegister.register(name, () -> Builder.of(...).build())}
 * 都应在 lambda 内<b>新建 builder</b>。禁止在多次 build 之间复用同一 builder 实例——
 * 尽管 {@code PazInfoBase} 对可变集合做了快照，复用仍会让语义变得难以推理。
 *
 * <p><b>示例</b>：
 * <pre>{@code
 * public static final DeferredHolder<IPlantType, IPlantType> PEA_SHOOTER =
 *     PVZRegistryBootstrap.PLANTS.register("pea_shooter", () -> PlantTypeBuilder
 *         .of("pvz", "pea_shooter")
 *         .rank(RankTypes.WHITE)
 *         .essence(EssenceTypes.APPEASE)
 *         .coolDown(CoolDowns.SUPER_FAST)
 *         .group("pvz", 100)
 *         .sunCost(100).requiredLevel(1)
 *         .category(PlantCategory.RANGED)
 *         .combatStyle(PlantCombatStyle.RANGED_DIRECT)
 *         .entityType(EntityRegister.PEA_SHOOTER)
 *         .summonCard(ItemRegister.PEA_SHOOTER_CARD)
 *         .enjoyCard(ItemRegister.PEA_SHOOTER_ENJOY_CARD)
 *         .skill(SkillTypes.PEA_DAMAGE)
 *         .build());
 * }</pre>
 */
public final class PlantTypeBuilder extends AbstractPAZTypeBuilder<PlantTypeBuilder> {

    private ICardPlacement explicitPlacement = null;
    private final List<Supplier<IPlantType>> upgradeFrom = new ArrayList<>();
    private final List<Supplier<IPlantType>> upgradeTo = new ArrayList<>();
    private Supplier<Block> plantBlock = null;
    private Supplier<IPlantInfo> outerPlantInfo = null;

    private PlantCategory category = PlantCategory.SPECIAL;
    private PlantCombatStyle combatStyle = PlantCombatStyle.NONE;
    private final EnumSet<PlantTrait> traits = EnumSet.noneOf(PlantTrait.class);

    private PlantTypeBuilder(Identifier id) {
        super(id);
    }

    public static PlantTypeBuilder of(Identifier id) {
        return new PlantTypeBuilder(id);
    }

    public static PlantTypeBuilder of(String namespace, String path) {
        return new PlantTypeBuilder(Identifier.fromNamespaceAndPath(namespace, path));
    }

    /* ---- 植物专属 setter ---- */

    /**
     * 显式设置放置规则。优先级高于任何 {@link PlantTrait} 推导。
     */
    public PlantTypeBuilder placement(ICardPlacement placement) {
        this.explicitPlacement = Objects.requireNonNull(placement, "placement");
        return this;
    }

    public PlantTypeBuilder upgradeFrom(Supplier<IPlantType> supplier) {
        this.upgradeFrom.add(Objects.requireNonNull(supplier, "upgradeFrom"));
        return this;
    }

    public PlantTypeBuilder upgradeTo(Supplier<IPlantType> supplier) {
        this.upgradeTo.add(Objects.requireNonNull(supplier, "upgradeTo"));
        return this;
    }

    public PlantTypeBuilder category(PlantCategory category) {
        this.category = Objects.requireNonNull(category, "category");
        return this;
    }

    public PlantTypeBuilder combatStyle(PlantCombatStyle style) {
        this.combatStyle = Objects.requireNonNull(style, "combatStyle");
        return this;
    }

    public PlantTypeBuilder trait(PlantTrait... traits) {
        for (PlantTrait t : traits) {
            this.traits.add(Objects.requireNonNull(t, "trait"));
        }
        return this;
    }

    /**
     * 标记为蘑菇植物。添加 {@link PlantTrait#SHROOM}。
     * placement 由 {@code build()} 时统一推导（{@link Placements#SHROOM}）。
     */
    public PlantTypeBuilder isShroomPlant() {
        this.traits.add(PlantTrait.SHROOM);
        return this;
    }

    /**
     * 标记为水生植物。添加 {@link PlantTrait#WATER}。
     * placement 由 {@code build()} 时统一推导（{@link Placements#NONE}）。
     */
    public PlantTypeBuilder isWaterPlant() {
        this.traits.add(PlantTrait.WATER);
        return this;
    }

    public PlantTypeBuilder plantBlock(Supplier<Block> supplier) {
        this.plantBlock = Objects.requireNonNull(supplier, "plantBlock");
        return this;
    }

    /**
     * 标记为外层植物（如南瓜）。添加 {@link PlantTrait#OUTER}。
     * placement 由 {@code build()} 时统一推导（{@link Placements#NONE}）。
     *
     * <p>Supplier 每次调用必须返回<b>新实例</b>。
     */
    public PlantTypeBuilder outerPlant(Supplier<IPlantInfo> supplier) {
        this.outerPlantInfo = Objects.requireNonNull(supplier, "outerPlantInfo");
        this.traits.add(PlantTrait.OUTER);
        return this;
    }

    @Override
    public IPlantType build() {
        this.validateCommon();
        return new PlantTypeImpl(this);
    }

    /* ---- 包内工具：供 build 时解析最终 placement ---- */

    /**
     * 根据显式设置与 {@link #traits} 推导最终 placement。
     *
     * <p>trait 优先级（首个命中生效）：
     * <ol>
     *   <li>显式 {@link #placement(ICardPlacement)}</li>
     *   <li>{@link PlantTrait#OUTER} → {@link Placements#NONE}</li>
     *   <li>{@link PlantTrait#WATER} → {@link Placements#NONE}</li>
     *   <li>{@link PlantTrait#SHROOM} → {@link Placements#SHROOM}</li>
     *   <li>{@link PlantTrait#GOLD_PLACEABLE} → {@link Placements#GOLD}</li>
     *   <li>默认 → {@link Placements#COMMON}</li>
     * </ol>
     *
     * <p>注意：{@code OUTER} 与 {@code WATER} 都推导 {@code NONE}，两者共存（如未来的
     * 水属性外层植物）时顺序无关；但 {@code SHROOM + WATER}（如海蘑菇）会因
     * {@code WATER} 在 {@code SHROOM} 之前命中而得到 {@code NONE}，与旧版行为一致。
     */
    ICardPlacement resolvePlacement() {
        if (this.explicitPlacement != null) {
            return this.explicitPlacement;
        }
        if (this.traits.contains(PlantTrait.OUTER)) {
            return Placements.NONE;
        }
        if (this.traits.contains(PlantTrait.WATER)) {
            return Placements.NONE;
        }
        if (this.traits.contains(PlantTrait.SHROOM)) {
            return Placements.SHROOM;
        }
        if (this.traits.contains(PlantTrait.GOLD_PLACEABLE)) {
            return Placements.GOLD;
        }
        return Placements.COMMON;
    }

    /* ---- Impl ---- */

    private static final class PlantTypeImpl extends PazInfoBase implements IPlantType {

        private final ICardPlacement placement;
        private final List<Supplier<IPlantType>> upgradeFrom;
        private final List<Supplier<IPlantType>> upgradeTo;
        private final Supplier<Block> plantBlock;
        private final Supplier<IPlantInfo> outerPlantInfo;
        private final PlantCategory category;
        private final PlantCombatStyle combatStyle;
        private final EnumSet<PlantTrait> traits;

        PlantTypeImpl(PlantTypeBuilder b) {
            super(b);
            this.placement = b.resolvePlacement();
            this.upgradeFrom = List.copyOf(b.upgradeFrom);
            this.upgradeTo = List.copyOf(b.upgradeTo);
            this.plantBlock = b.plantBlock;
            this.outerPlantInfo = b.outerPlantInfo;
            this.category = b.category;
            this.combatStyle = b.combatStyle;
            // 拷贝，避免 builder 后续 trait() 污染已 build 的 impl
            this.traits = b.traits.isEmpty()
                    ? EnumSet.noneOf(PlantTrait.class)
                    : EnumSet.copyOf(b.traits);
        }

        /* ---- 分类 / 形态 / 特征 ---- */

        @Override
        public PlantCategory getCategory() {
            return this.category;
        }

        @Override
        public PlantCombatStyle getCombatStyle() {
            return this.combatStyle;
        }

        @Override
        public boolean hasTrait(PlantTrait trait) {
            return this.traits.contains(trait);
        }

        @Override
        public Set<PlantTrait> getTraits() {
            return Collections.unmodifiableSet(this.traits);
        }

        /* ---- 放置 ---- */

        @Override
        public ICardPlacement getPlacement() {
            return this.placement;
        }

        /* ---- 升级链 ---- */

        @Override
        public List<IPlantType> getUpgradeFrom() {
            return this.upgradeFrom.stream()
                    .map(Supplier::get)
                    .filter(Objects::nonNull)
                    .toList();
        }

        @Override
        public List<IPlantType> getUpgradeTo() {
            return this.upgradeTo.stream()
                    .map(Supplier::get)
                    .filter(Objects::nonNull)
                    .toList();
        }

        /* ---- 特征薄封装（读 trait） ---- */


        /* ---- 方块 / 外层 ---- */

        @Override
        public Optional<Block> getPlantBlock() {
            return this.plantBlock == null ? Optional.empty() : Optional.ofNullable(this.plantBlock.get());
        }

        @Override
        public Supplier<IPlantInfo> getOuterPlantInfo() {
            // 契约：isOuterPlant() == false 时调用方不应调用本方法。
            if (this.outerPlantInfo == null) {
                throw new IllegalStateException(
                        "getOuterPlantInfo() called on non-outer plant type " + this.getID()
                                + "; check isOuterPlant() first.");
            }
            return this.outerPlantInfo;
        }
    }
}