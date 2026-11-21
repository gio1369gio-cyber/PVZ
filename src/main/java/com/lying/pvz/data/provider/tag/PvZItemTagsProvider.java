package com.lying.pvz.data.provider.tag;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.PlantTrait;
import com.lying.pvz.common.enchantment.PvZEnchantments;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.block.ore.EssenceOreBlock;
import com.lying.pvz.common.level.item.card.AbstractCardItem;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.common.level.item.material.TemplateCardItem;
import com.lying.pvz.common.tag.PvZItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class PvZItemTagsProvider extends ItemTagsProvider {

    public PvZItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, PvZMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // ==================== MC tag ====================
        this.tag(PvZItemTags.ARROWS).add(PvZItemRegister.TARGET_ARROW.key());

        // ==================== NeoForge tags ====================
        this.tag(PvZItemTags.PEAS).add(PvZItemRegister.PEA.key());
        this.tag(PvZItemTags.NUTS).add(PvZItemRegister.NUT.key());
        this.tag(PvZItemTags.CHILIPEPPERS).add(PvZItemRegister.PEPPER.key());
        this.tag(PvZItemTags.CHILI_SEEDS).add(PvZItemRegister.CHILI_SEEDS.key());
        this.tag(PvZItemTags.CABBAGES).add(PvZItemRegister.CABBAGE.key());
        this.tag(PvZItemTags.CABBAGE_SEEDS).add(PvZItemRegister.CABBAGE_SEEDS.key());
        this.tag(PvZItemTags.CORNS).add(PvZItemRegister.CORN.key());
        this.tag(PvZItemTags.CORN_SEEDS).add(PvZItemRegister.CORN_SEEDS.key());
        this.tag(PvZItemTags.GARLIC).add(PvZItemRegister.GARLIC.key());
        this.tag(PvZItemTags.AMETHYST_INGOTS).add(PvZItemRegister.AMETHYST_INGOT.key());
        this.tag(PvZItemTags.AMETHYST_ORES).add(blockItemKeyOf(PvZBlockRegister.AMETHYST_ORE.get()));

        // ==================== PVZ tags ====================

        this.tag(PvZItemTags.ORIGIN_TOOL_MATERIALS).add(PvZItemRegister.ORIGIN_INGOT.key());

        this.tag(PvZItemTags.PEN_GUN_REPAIRABLE).add(PvZItemRegister.APPEASE_ESSENCE.key());

        this.tag(ItemTags.HEAD_ARMOR)
                .add(PvZItemRegister.BUCKET_HEAD.key())
                .add(PvZItemRegister.CONE_HEAD.key())
                .add(PvZItemRegister.FOOTBALL_HELMET.key())
                .add(PvZItemRegister.GIGA_HELMET.key());

        this.tag(ItemTags.CHEST_ARMOR)
                .add(PvZItemRegister.FOOTBALL_CHESTPLATE.key())
                .add(PvZItemRegister.GIGA_CHESTPLATE.key());

        this.tag(ItemTags.LEG_ARMOR)
                .add(PvZItemRegister.FOOTBALL_LEGGINGS.key())
                .add(PvZItemRegister.GIGA_LEGGINGS.key());

        this.tag(ItemTags.FOOT_ARMOR)
                .add(PvZItemRegister.FOOTBALL_BOOTS.key())
                .add(PvZItemRegister.GIGA_BOOTS.key());


        this.tag(ItemTags.AXES)
                .add(PvZItemRegister.ORIGIN_AXE.key())
                .add(PvZItemRegister.POLE.key());

        this.tag(ItemTags.PICKAXES)
                .add(PvZItemRegister.ORIGIN_PICKAXE.key());

        this.tag(ItemTags.SWORDS)
                .add(PvZItemRegister.ORIGIN_SWORD.key())
                .add(PvZItemRegister.WARNING_SIGN.key());;

        this.tag(ItemTags.HOES)
                .add(PvZItemRegister.ORIGIN_HOE.key());

        this.tag(ItemTags.SHOVELS)
                .add(PvZItemRegister.ORIGIN_SHOVEL.key());

        this.tag(ItemTags.LEAVES)
                .add(blockItemKeyOf(PvZBlockRegister.NUT_LEAVES.get()));

        this.tag(ItemTags.SAPLINGS)
                .add(blockItemKeyOf(PvZBlockRegister.NUT_SAPLING.get()));

        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(blockItemKeyOf(PvZBlockRegister.NUT_LOG.get()))
                .add(blockItemKeyOf(PvZBlockRegister.NUT_WOOD.get()))
                .add(blockItemKeyOf(PvZBlockRegister.STRIPPED_NUT_WOOD.get()))
                .add(blockItemKeyOf(PvZBlockRegister.STRIPPED_NUT_LOG.get()));

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof AbstractCardItem card) {
                final ResourceKey<Item> key = itemKeyOf(item);

                this.tag(PvZEnchantments.ENCHANTABLE_SUMMON_CARD).add(key);

                final IPAZType rawType = card.getType();
                if (rawType instanceof IPlantType plantType) {
                    this.tag(PvZItemTags.PLANT_CARDS).add(key);
                    if (card.isEnjoy()) {
                        this.tag(PvZItemTags.PLANT_ENJOY_CARDS).add(key);
                    } else {
                        this.tag(PvZItemTags.PLANT_SUMMON_CARDS).add(key);
                    }
                    if (item instanceof PlantCardItem) {
                        final boolean hasPlantBlock = plantType.getPlantBlock().isPresent();
                        final boolean isOuter = plantType.hasTrait(PlantTrait.OUTER);

                        if (!hasPlantBlock && !isOuter) {
                            this.tag(PvZEnchantments.ENCHANTABLE_ENTITY_CARD).add(key);
                        }
                        if (!hasPlantBlock) {
                            this.tag(PvZEnchantments.ENCHANTABLE_PLANT_OR_OUTER).add(key);
                        }
                        if (!isOuter) {
                            this.tag(PvZEnchantments.ENCHANTABLE_NO_OUTER_PLANT).add(key);
                        }
                    }
                }
            } else if (item instanceof BlockItem blockItem) {
                final Block b = blockItem.getBlock();
                if (b instanceof EssenceOreBlock) {
                    this.tag(PvZItemTags.ESSENCE_ORES).add(itemKeyOf(item));
                }
            }
        }

        this.tag(PvZEnchantments.ENCHANTABLE_REACH).addTag(PvZItemTags.REACH_ITEMS);

        for (Item templateCard : getFilterItems(i -> i instanceof TemplateCardItem)) {
            this.tag(PvZItemTags.TEMPLATE_CARDS).add(itemKeyOf(templateCard));
        }

        this.tag(PvZItemTags.PEA_GUN_BULLETS).add(PvZItemRegister.PEA.key())
                .add(PvZItemRegister.SNOW_PEA.key())
                .add(PvZItemRegister.FLAME_PEA.key());

        this.tag(PvZItemTags.REACH_ITEMS).add(PvZItemRegister.RESOURCE_COLLECTOR.key());

        // ---- 卡片材质 ----
        this.tag(PvZItemTags.GRAY_MATERIALS)
                .add(itemKeyOf(Items.STONE))
                .add(itemKeyOf(Items.CLAY));
        this.tag(PvZItemTags.WHITE_MATERIALS)
                .add(itemKeyOf(Items.IRON_INGOT))
                .add(itemKeyOf(Items.QUARTZ));
        this.tag(PvZItemTags.GREEN_MATERIALS)
                .add(itemKeyOf(Items.EMERALD))
                .add(PvZItemRegister.ORIGIN_INGOT.key());
        this.tag(PvZItemTags.BLUE_MATERIALS)
                .add(itemKeyOf(Items.DIAMOND))
                .add(itemKeyOf(Items.LAPIS_BLOCK));
        this.tag(PvZItemTags.PURPLE_MATERIALS)
                .add(PvZItemRegister.AMETHYST_INGOT.key())
                .add(itemKeyOf(Items.CRYING_OBSIDIAN));
        this.tag(PvZItemTags.GOLD_MATERIALS).add(itemKeyOf(Items.GOLD_INGOT));
        this.tag(PvZItemTags.RED_MATERIALS).add(itemKeyOf(Items.REDSTONE_BLOCK));
        this.tag(PvZItemTags.BLACK_MATERIALS).add(itemKeyOf(Items.NETHERITE_SCRAP));

        // ---- 各 Rank 的卡牌 tag ----
        PVZAPI.lookup().ranks().forEach(rank -> {
            rank.getCardTag().ifPresent(cardTag -> {
                this.tag(cardTag)
                        .add(PvZItemRegister.MEGA_CARD.key())
                        .add(itemKeyOf(rank.getTemplateCard()));
            });
        });

        // ---- 植物卡片归入对应 Rank 的卡牌 tag ----
        PVZAPI.lookup().plants().forEach(plant -> {
            plant.getRank().getCardTag().ifPresent(cardTag -> {
                plant.getSummonCard().ifPresent(card -> this.tag(cardTag).add(itemKeyOf(card)));
                plant.getEnjoyCard().ifPresent(card -> this.tag(cardTag).add(itemKeyOf(card)));
            });
        });
    }

    private Item[] getFilterItems(Predicate<Item> predicate) {
        return BuiltInRegistries.ITEM.stream()
                .filter(predicate)
                .sorted(Comparator.comparing(item -> itemKeyOf(item).identifier()))
                .toArray(Item[]::new);
    }

    private static ResourceKey<Item> itemKeyOf(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item)
                .orElseThrow(() -> new IllegalStateException("Unregistered item: " + item));
    }

    private static ResourceKey<Item> blockItemKeyOf(Block block) {
        final Item item = block.asItem();
        if (item == Items.AIR) {
            throw new IllegalStateException("Block has no BlockItem: " + block);
        }
        return itemKeyOf(item);
    }

    @Override
    public String getName() {
        return "Plants vs Zombies item tags";
    }
}