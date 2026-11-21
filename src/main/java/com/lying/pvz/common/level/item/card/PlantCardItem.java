package com.lying.pvz.common.level.item.card;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.card.CardContent;
import com.lying.pvz.api.card.PvZCardComponents;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.PlantTrait;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.registry.PVZRegistries;
import com.lying.pvz.common.enchantment.EnchantmentRegister;
import com.lying.pvz.common.enchantment.EnchantmentUtil;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.magic.ImitaterEntity;
import com.lying.pvz.common.misc.PvZEnumProxy;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 普通/体验植物卡。非 final，第三方可继承。
 */
public class PlantCardItem extends AbstractCardItem {

    /** 已持有 {@link IPlantType} 实例时的便捷构造（运行期用）。 */
    public PlantCardItem(Properties properties, IPlantType type, boolean enjoy) {
        super(properties, () -> type, enjoy);
    }

    /** 注册期构造：只持有 {@link DeferredHolder}，绝不在构造期调用 {@code get()}。 */
    public PlantCardItem(Identifier id, DeferredHolder<IPlantType, ? extends IPlantType> type,
                         boolean enjoy) {
        super(defaultProperties(id, type, enjoy), type, enjoy);
    }

    public static Properties defaultProperties(Identifier id, DeferredHolder<IPlantType, ? extends IPlantType> type,
                                               boolean enjoy) {
        final ResourceKey<IPlantType> plantKey = type.key();
        final Identifier plantId = plantKey.identifier();

        return new Properties()
                .stacksTo(enjoy ? 16 : 1)
                .setId(ResourceKey.create(Registries.ITEM, id))
                .delayedComponent(DataComponents.RARITY, ctx ->
                        PvZEnumProxy.getRarityByRank(
                                ctx.lookupOrThrow(PVZRegistries.PLANT)
                                        .getOrThrow(plantKey)
                                        .value()
                                        .getRank()))
                .component(PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY)
                .component(DataComponents.USE_COOLDOWN,
                        new UseCooldown(0.05f, Optional.of(plantId)));
    }

    @Override
    public CardContent resolveContent(ItemStack stack) {
        return CardContent.of(getType(), stack);
    }

    @Override
    public List<IPAZType> getLockCheckTypes(ItemStack stack) {
        return List.of(getType());
    }

    /**
     * 从植物类型推导方块状态。
     *
     * <p>通用实现：任何声明了 {@link IPlantType#getPlantBlock()} 的植物都能被"方块型放置"。
     * 若某植物的方块需要特殊朝向（如睡莲），在 block 内部覆写 {@code defaultBlockState()} 即可，
     * 或后续在实现层按 {@code plantType} 定制。
     */
    public static BlockState getBlockState(Direction direction, IPlantType plant) {
        return plant.getPlantBlock()
                .map(Block::defaultBlockState)
                .orElse(null);
    }

    /**
     * 在指定位置放置植物方块并播放音效。
     */
    public static void handlePlantBlock(Level level, IPlantType plantType, BlockState state, BlockPos pos) {
        if (state == null) {
            return;
        }
        level.setBlock(pos, state, 11);
        final SoundEvent sound = plantType.hasTrait(PlantTrait.WATER)
                ? PvZSoundRegister.PLACE_PLANT_WATER.get()
                : PvZSoundRegister.PLACE_PLANT_GROUND.get();
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundSource.BLOCKS, 1F, 1F);
    }

    /**
     * 在指定位置生成植物实体并应用卡上技能。
     *
     * <p>不负责扣阳光 / 冷却 / 事件——由  或调用方负责。
     */
    public static boolean handlePlantEntity(Player player, IPlantType plantType, ItemStack plantStack,
                                            BlockPos pos, Consumer<PlantEntity> consumer) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }
        final Optional<EntityType<? extends Mob>> opt = plantType.getEntityType();
        if (opt.isEmpty()) {
            PvZMod.LOGGER.error("Plant Card : Summon wrong plant entity!");
            return false;
        }
        final Mob mob = opt.get().spawn(serverLevel, plantStack, player, pos,
                EntitySpawnReason.MOB_SUMMONED, true, true);
        if (!(mob instanceof PlantEntity plant)) {
            PvZMod.LOGGER.error("Plant Card : No such plant entity!");
            return false;
        }
        applyCardSkillsToPlant(plant, plantStack);
        enchantPlantEntityByCard(plant, plantStack, player.level().registryAccess());
        consumer.accept(plant);
        return true;
    }

    /**
     * 从卡片导出植物侧技能（排除 {@code CARD} 面）。
     * 静态版本，便于非实例上下文使用。
     */
    public static SkillLevels getSkillsFromCard(ItemStack stack) {
        if (stack.getItem() instanceof AbstractCardItem card) {
            return card.getSkillsForPlant(stack);
        }
        return SkillLevels.EMPTY;
    }
    /**
     * 应用卡上技能到植物实体并刷新属性。
     */
    public static void applyCardSkillsToPlant(PlantEntity plant, ItemStack stack) {
        plant.applySkillLevels(getSkillsFromCard(stack));
    }

    /**
     * 应用卡片附魔到植物实体。
     *
     */
    public static void enchantPlantEntityByCard(PlantEntity plant, ItemStack stack,
                                                HolderLookup.Provider registries) {
        final ItemStack source = AbstractCardItem.resolveEffectiveSource(stack);
        if (EnchantmentUtil.getLevel(source, EnchantmentRegister.CHARM) > 0) {
            plant.onCharmedBy(null);
        }
        if (EnchantmentUtil.isSoilless(source)) {
            plant.setImmuneToWeak(true);
        }
    }

    /**
     * 从卡片导出植物侧附魔（排除卡片专属 tag）。静态版本。
     */
    public static ItemEnchantments getEnchantmentsFromCard(ItemStack stack, HolderLookup.Provider registries) {
        if (stack.getItem() instanceof AbstractCardItem card) {
            return card.getEnchantmentsForPlant(stack, registries);
        }
        return ItemEnchantments.EMPTY;
    }


    public boolean trySpawnAndApply(Player player, Entity target, ItemStack stack,
                                    CardContent content,
                                    Predicate<IPlantType> typeMatcher,
                                    Consumer<PlantEntity> action) {
        final IPlantType plantType = content.asPlantType();
        if (plantType == null || !typeMatcher.test(plantType)) return false;

        final Optional<PlantEntity> opt = AbstractCardItem.spawnPlant(player, this, stack, content,
                target.blockPosition());
        if (opt.isEmpty()) return false;

        final PlantEntity plant = opt.get();
        if (content.isImitater() && plant instanceof ImitaterEntity imitater) {
            imitater.setImitateAction(action);
        } else {
            action.accept(plant);
        }

        commitUse(player, stack, content, this.getBasisSunCost(stack));
        return true;
    }


}