package com.lying.pvz.common.level.item.card;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.card.CardContent;
import com.lying.pvz.api.card.CardErrors;
import com.lying.pvz.api.card.ICardError;
import com.lying.pvz.api.card.ICardType;
import com.lying.pvz.api.card.PvZCardComponents;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.PlantTrait;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.content.skill.SkillScope;
import com.lying.pvz.api.entity.plant.IPlantInfo;
import com.lying.pvz.api.event.card.SummonCardUseEvent;
import com.lying.pvz.common.enchantment.EnchantmentUtil;
import com.lying.pvz.common.impl.CoolDowns;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.magic.CoffeeBeanEntity;
import com.lying.pvz.common.level.entity.plant.magic.ImitaterEntity;
import com.lying.pvz.common.misc.potion.ExciteEffect;
import com.lying.pvz.common.tag.PvZEnchantmentTags;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 卡片基类，实现 {@link ICardType}。
 *
 * <p>传导模型：卡片 → 植物只导出 {@link SkillScope#PLANT} + {@link SkillScope#PROJECTILE} 面，
 * {@link SkillScope#CARD} 面永久留在卡片上。植物 → 子弹的传导不归卡片侧管。
 *
 * <p>非 sealed，允许第三方继承。
 */
public abstract class AbstractCardItem extends Item implements ICardType {

    /** 延迟解析的卡片类型来源。避免在 {@code RegisterEvent} 期间触发 {@code DeferredHolder#get()}。 */
    protected final Supplier<? extends IPAZType> type;
    protected final boolean enjoy;

    protected AbstractCardItem(Properties properties, Supplier<? extends IPAZType> type, boolean enjoy) {
        super(properties);
        this.type = type;
        this.enjoy = enjoy;
    }

    /* ===== 内容解析 ===== */

    /**
     * 外部调用方应通过 content(ItemStack)；直接调 resolveContent 会跳过持有者校验
     * @param stack
     * @return
     */
    @Override
    public abstract CardContent resolveContent(ItemStack stack);

    public final CardContent content(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != this) {
            return CardContent.EMPTY;
        }
        return this.resolveContent(stack);
    }

    @Override
    public IPAZType getType() {
        return this.type.get();
    }

    @Override
    public boolean isEnjoy() {
        return this.enjoy;
    }

    /* ===== 数值（全走 effectiveStack） ===== */
    public final int getBasisSunCost(ItemStack stack) {
        final CardContent content = this.content(stack);
        if (content.isEmpty()) {
            return 0;
        }
        final IPAZType effectiveType = resolveCostSource(content);
        final ItemStack effective = content.effectiveStack();
        final int base = effectiveType.getSunCost();
        final SkillLevels levels = effective.getOrDefault(
                PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY);

        final Identifier lessSunId = SkillTypes.LESS_SUN.get().getID();
        int vary = (int) SkillTypes.LESS_SUN.get().getValueAt(
                levels.getLevel(SkillTypes.LESS_SUN.get()));
        for (var entry : levels.levels().entrySet()) {
            if (entry.getValue() <= 0 || entry.getKey().equals(lessSunId)) {
                continue;
            }
            final ISkillType skill = PVZAPI.lookup().skill(entry.getKey()).orElse(null);
            if (skill != null) {
                vary -= skill.getExtraSun();
            }
        }
        return Math.max(base - vary, 1);
    }

    public final ICoolDown getCoolDown(ItemStack stack) {
        final CardContent content = this.content(stack);
        if (content.isEmpty()) {
            return CoolDowns.DEFAULT.get();
        }
        return resolveCostSource(content).getCoolDown();
    }

    public final int getRequiredLevel(ItemStack stack) {
        final CardContent content = this.content(stack);
        if (content.isEmpty()) {
            // 空模仿者卡等"内容为空但卡片本身仍有效"的情况，回退到卡片自身类型的需求等级。
            return this.getType().getRequiredLevel();
        }
        return requireEffectiveType(content).getRequiredLevel();
    }


    /* ===== 附魔能力值 ===== */

    /**
     * 卡片自身的附魔能力值。
     * <p>体验卡返回 0（不设置 {@code ENCHANTABLE} 组件 = 不可附魔）。
     * <p>第三方卡种可覆写以自定义来源。
     */
    @Override
    public int getCardEnchantmentValue(ItemStack stack) {
        if (this.enjoy) {
            return 0;
        }
        return this.type.get().getRank().getEnchantPoint();
    }

    /* ===== 解锁 ===== */


    /**
     * 检查该卡是否被锁定。
     * <p>返回的列表中任一类型处于锁定状态，卡片即视为锁定。
     * <p>第三方卡种可覆写以自定义解锁规则。
     */

    @Override
    public abstract List<IPAZType> getLockCheckTypes(ItemStack stack);

    public final boolean isLocked(Player player, ItemStack stack) {
        for (IPAZType t : this.getLockCheckTypes(stack)) {
            if (this.isTypeLocked(player, t)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isTypeLocked(Player player, IPAZType type) {
        if (player.isCreative()) return false;
        return PlayerUtil.isPAZLocked(player, type);
    }

    /* ===== 传导（纯查询，无副作用） ===== */

    /**
     * 导出给植物实体的技能（排除 {@link SkillScope#CARD}）。
     * <p>{@link SkillScope#PROJECTILE} 包含在内，由植物暂存、发射时再传给子弹。
     */
    @Override
    public  SkillLevels getSkillsForPlant(ItemStack stack) {
        final CardContent c = this.content(stack);
        final ItemStack source = c.isEmpty() ? stack : c.effectiveStack();
        final SkillLevels levels = source.getOrDefault(
                PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY);
        return levels.filter(id -> PVZAPI.lookup().skill(id)
                .map(skill -> skill.getScope() != SkillScope.CARD)
                .orElse(false));
    }

    /**
     * 导出给植物实体的附魔（排除 {@link PvZEnchantmentTags#CARD}）。
     */
    @Override
    public  ItemEnchantments getEnchantmentsForPlant(ItemStack stack, HolderLookup.Provider registries) {
        final ItemStack source = resolveEffectiveSource(stack);   // 改：模仿者解包为内部卡
        final ItemEnchantments all = source.getOrDefault(
                DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (all.isEmpty()) {
            return ItemEnchantments.EMPTY;
        }
        final Optional<HolderSet.Named<Enchantment>> cardTag = registries
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(PvZEnchantmentTags.CARD);
        if (cardTag.isEmpty()) {
            return all;
        }
        final ItemEnchantments.Mutable result = new ItemEnchantments.Mutable(all);
        result.removeIf(holder -> cardTag.get().contains(holder));
        return result.toImmutable();
    }

    /* ===== 错误反馈 ===== */

    protected void notifyError(Player player, ItemStack stack, ICardError error, int arg) {
        CardErrorNotifier.send(player,stack,error,arg);
    }

    /* ===== 成功出口 ===== */

    public void onUsedSuccessfully(Player player, ItemStack heldStack, CardContent content) {
        if (this.enjoy) {
            if (!player.isCreative()) {
                heldStack.shrink(1);
            }
        } else {
            int cd = this.computeCoolDown(player, heldStack);
            if (EnchantmentUtil.canImmediateCD(heldStack, player.getRandom())) {
                cd = 20;
            }
            if (!player.isCreative()) {
                PlayerUtil.setPAZCardCD(player, this.type.get(), cd);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    protected int computeCoolDown(Player player, ItemStack heldStack) {
        final CardContent content = this.content(heldStack);
        if (content.isEmpty()) {
            return 20;
        }
        final ItemStack effective = content.effectiveStack();
        final int fastCdLevel = effective.getOrDefault(
                        PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY)
                .getLevel(SkillTypes.FAST_CD.get());
        int cd = this.getCoolDown(heldStack).getCD(fastCdLevel);
        // 兴奋药水：按效果等级缩短卡片 CD（每级 -10%，10 级归零）。
        final int exciteAmp = ExciteEffect.getAmplifier(player);
        if (exciteAmp >= 0) {
            cd = ExciteEffect.getCooldownTicks(cd, exciteAmp);
        }
        return cd;
    }

    protected void fireSummonCardUseEvent(Player player, ItemStack heldStack, CardContent content) {
        NeoForge.EVENT_BUS.post(
                new SummonCardUseEvent(player, heldStack, content));
    }

    /* ===== tooltip ===== */

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("tooltip.pvz.card_sun_cost", this.getBasisSunCost(stack))
                .withStyle(ChatFormatting.YELLOW));
        builder.accept(Component.translatable("tooltip.pvz.card_cd",
                        this.getCoolDown(stack).getText().getString())
                .withStyle(ChatFormatting.AQUA));
        this.appendContentTooltip(stack, context, display, builder, flag);
    }

    /* ===== 使用 ===== */

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        final ItemStack heldStack = player.getItemInHand(hand);
        final CardContent content = this.content(heldStack);
        if (content.isEmpty()) {
            this.notifyError(player, heldStack, CardErrors.EMPTY_CONTENT, 0);
            return InteractionResult.FAIL;
        }
        if (!CardPlacementService.checkCommonForPlanting(player, heldStack, content)) {
            return InteractionResult.FAIL;
        }
        return CardPlacementService.executeWaterRaycast(player, this, heldStack, content);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
        final Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        final ItemStack heldStack = context.getItemInHand();
        final CardContent content = this.content(heldStack);
        if (content.isEmpty()) {
            this.notifyError(player, heldStack, CardErrors.EMPTY_CONTENT, 0);
            return InteractionResult.FAIL;
        }
        if (!CardPlacementService.checkCommonForPlanting(player, heldStack, content)) {
            return InteractionResult.FAIL;
        }
        return CardPlacementService.executeOnBlock(player, this, heldStack, content, context);
    }

    /**
     * 返回用于导出技能/附魔/成本的有效 stack。
     * <p>模仿者卡等"内容在内部 stack"的卡型会被解包为内部卡；
     * 普通卡或空内容返回原 stack。
     * <p>供静态上下文（如 {@code PlantCardItem.enchantPlantEntityByCard}）使用。
     */
    public static ItemStack resolveEffectiveSource(ItemStack stack) {
        if (stack.getItem() instanceof AbstractCardItem card) {
            final CardContent c = card.content(stack);
            if (!c.isEmpty()) {
                return c.effectiveStack();
            }
        }
        return stack;
    }

    /**
     * 解析"阳光/冷却"的成本来源类型。
     * <p>模仿者卡读内部卡的植物类型；其他卡读 {@link CardContent#effectiveType()}。
     */
    private static IPAZType resolveCostSource(CardContent content) {
        if (content.isImitater()) {
            final ItemStack inner = content.effectiveStack();
            if (inner.getItem() instanceof ICardType innerCard) {
                return innerCard.getType();
            }
        }
        return requireEffectiveType(content);
    }
    private static IPAZType requireEffectiveType(CardContent content) {
        return Objects.requireNonNull(content.effectiveType(),
                "CardContent.effectiveType() returned null for non-empty content");
    }
    /**
     * 子类可覆写以追加内容（技能列表、模仿者内部卡说明等）。
     */
    protected void appendContentTooltip(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                        Consumer<Component> builder, TooltipFlag flag) {
        final SkillLevels skills = this.getCardSkills(stack);
        if (skills.isEmpty()) {
            return;
        }
        for (var entry : skills.levels().entrySet()) {
            final int level = entry.getValue();
            if (level <= 0) {
                continue;
            }
            final ISkillType skill = PVZAPI.lookup().skill(entry.getKey()).orElse(null);
            if (skill == null) {
                continue;
            }
            builder.accept(skill.getText().copy()
                    .append(Component.literal(" Lv." + level))
                    .withStyle(ChatFormatting.GREEN));
            if (flag.isAdvanced()) {
                builder.accept(skill.getDescription().copy().withStyle(ChatFormatting.GRAY));
            }
        }
    }

    /** 卡片当前携带的全部技能（含 CARD 面，用于 tooltip 展示）。 */
    private SkillLevels getCardSkills(ItemStack stack) {
        final CardContent content = this.content(stack);
        final ItemStack source = content.isEmpty() ? stack : content.effectiveStack();
        return source.getOrDefault(PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY);
    }

    /* ================================================================== */
    /* 用在已有植物上的交互                                                  */
    /* ================================================================== */
    public boolean tryInteractWithPlant(Player player, PlantEntity host,
                                        ItemStack stack, CardContent content) {
        final IPlantType plantType = content.asPlantType();
        if (plantType == null) return false;

        final int cost = this.getBasisSunCost(stack);
        final boolean handled =
                tryHeal(host, stack, plantType)
                        || tryUpgrade(player, host, stack, content, plantType, cost)
                        || tryOuter(player, host, stack, content, plantType, cost)
                        || tryCoffeeBean(player, host, stack, content, plantType);

        if (!handled) return false;
       commitUse(player, stack, content, cost);
        return true;
    }

    private boolean tryHeal(PlantEntity host, ItemStack stack, IPlantType plantType) {
        if (plantType.hasTrait(PlantTrait.OUTER)) {
            final Optional<IPlantInfo> outer = host.getOuterPlantInfo();
            if (outer.isEmpty() || plantType != outer.get().getType()) return false;
        } else {
            if (plantType != host.getPlantType()) return false;
        }
        final float percent = EnchantmentUtil.getHealPercent( AbstractCardItem.resolveEffectiveSource(stack));
        return host.onHealBy(plantType, percent);
    }

    private boolean tryUpgrade(Player player, PlantEntity host, ItemStack stack,
                               CardContent content, IPlantType plantType, int cost) {
        final List<IPlantType> cardFrom = plantType.getUpgradeFrom();
        if (cardFrom.isEmpty()) return false;

        if (plantType.hasTrait(PlantTrait.OUTER)) {
            final Optional<IPlantInfo> outer = host.getOuterPlantInfo();
            if (outer.isEmpty()) return false;
            if (!cardFrom.contains(outer.get().getType())) {
                this.notifyError(player, stack, PlantPlacementErrors.UPGRADE_WRONG_PLANT, 0);
                return false;
            }
            if (content.isImitater()) {
                final Optional<PlantEntity> opt = spawnPlant(player, this, stack, content, host.blockPosition());
                if (opt.isEmpty()) return false;
                if (opt.get() instanceof ImitaterEntity imitater) {
                    imitater.setImitateType(ImitaterEntity.ImitateType.OUTER);
                    imitater.setTargetEntity(host);
                    imitater.setImitateAction(p -> {
                        host.removeOuter();
                        host.placeOuter(plantType, cost);
                    });
                } else {
                    opt.get().discard();
                    return false;
                }
            } else {
                host.removeOuter();
                host.placeOuter(plantType, cost);
            }
            return true;
        }

        if (!cardFrom.contains(host.getPlantType())) {
            this.notifyError(player, stack, PlantPlacementErrors.UPGRADE_WRONG_PLANT, 0);
            return false;
        }
        if (!host.canBeUpgrade(player)) return false;

        final Optional<PlantEntity> newPlant = spawnPlant(player, this, stack, content, host.blockPosition());
        if (newPlant.isEmpty()) return false;
        if (content.isImitater() && newPlant.get() instanceof ImitaterEntity imitater) {
            imitater.setImitateAction(host::onPlantUpgrade);
        } else {
            host.onPlantUpgrade(newPlant.get());
        }
        return true;
    }

    private boolean tryOuter(Player player, PlantEntity host, ItemStack stack,
                             CardContent content, IPlantType plantType, int cost) {
        if (!plantType.hasTrait(PlantTrait.OUTER)) return false;
        if (!plantType.getUpgradeFrom().isEmpty()) return false;
        if (!host.canPlaceOuter()) {
            this.notifyError(player, stack, PlantPlacementErrors.OUTER_FULL, 0);
            return false;
        }
        if (content.isImitater()) {
            final Optional<PlantEntity> opt = spawnPlant(player, this, stack, content, host.blockPosition());
            if (opt.isEmpty()) return false;
            final PlantEntity newPlant = opt.get();
            if (newPlant instanceof ImitaterEntity imitater) {
                imitater.setImitateType(ImitaterEntity.ImitateType.OUTER);
                imitater.setTargetEntity(host);
                imitater.setImitateAction(p -> host.onPlaceOuterPlant(plantType, cost));
            } else {
                newPlant.discard();
                return false;
            }
        } else {
            host.placeOuter(plantType, cost);
        }
        return true;
    }

    private boolean tryCoffeeBean(Player player, PlantEntity host, ItemStack stack,
                                  CardContent content, IPlantType plantType) {
        if (plantType != PVZPlants.COFFEE_BEAN.get()) return false;

        final boolean match = host.getPlantType().hasTrait(PlantTrait.SHROOM)
                || host.getOuterPlantInfo()
                .map(i -> i.getType().hasTrait(PlantTrait.SHROOM))
                .orElse(false);
        if (!match) return false;

        final Optional<PlantEntity> bean = spawnPlant(player, this, stack, content, host.blockPosition());
        if (bean.isEmpty()) return false;
        final Consumer<PlantEntity> action = p -> {
            if (p instanceof CoffeeBeanEntity c) c.startRiding(host);
        };
        if (content.isImitater() && bean.get() instanceof ImitaterEntity imitater) {
            imitater.setImitateAction(action);
        } else {
            action.accept(bean.get());
        }
        return true;
    }

    public static Optional<PlantEntity> spawnPlant(Player player, AbstractCardItem card,
                                                    ItemStack heldStack, CardContent content, BlockPos pos) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return Optional.empty();
        }
        final Optional<EntityType<? extends Mob>> opt = content.spawnType().getEntityType();
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        final Mob mob = opt.get().spawn(serverLevel, null, player, pos,
                EntitySpawnReason.MOB_SUMMONED, true, true);
        if (!(mob instanceof PlantEntity plant)) {
            return Optional.empty();
        }
        plant.applyCardData(card.getSkillsForPlant(heldStack),
                card.getEnchantmentsForPlant(heldStack, player.level().registryAccess()));
        PlantCardItem.enchantPlantEntityByCard(plant, heldStack, player.level().registryAccess());
        plant.onSpawnedByPlayer(player, card.getBasisSunCost(heldStack));

        if (content.isImitater() && plant instanceof ImitaterEntity imitater) {
            imitater.setImitateCard(content.effectiveStack().copy());
        }
        return Optional.of(plant);
    }

    public  void commitUse(Player player, ItemStack stack, CardContent content, int cost) {
        if (!player.isCreative()) {
            PlayerUtil.addResource(player, Resources.SUN_NUM, -cost);
        }
        this.onUsedSuccessfully(player, stack, content);
        this.fireSummonCardUseEvent(player, stack, content);
    }
}