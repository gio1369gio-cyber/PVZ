package com.lying.pvz.event.common.handler;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.common.enchantment.EnchantmentRegister;
import com.lying.pvz.common.enchantment.EnchantmentUtil;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.invasion.Invasion;
import com.lying.pvz.common.invasion.InvasionManager;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.item.display.ChallengeEnvelopeItem;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class PlayerEventHandler {
    private PlayerEventHandler() { }

    public static void onPlayerKillEntity(Player player, DamageSource source, LivingEntity living) {
        if (living instanceof AbstractPAZEntity pazEntity) {
            final boolean raider = living.level() instanceof ServerLevel sl && PVZAPI.raid().isRaider(sl, living);
            final boolean invasion = InvasionManager.findOwning(living) != null;

            if (EntityUtil.isEnemy(player, living)
                    && (pazEntity.canGiveXP || ConfigUtil.AllZombieGiveXP() || raider || invasion)) {
                PlayerUtil.addResource(player, Resources.TREE_XP, pazEntity.getPAZType().getXpPoint());
            }
        }
        if (player instanceof ServerPlayer sp) {
            Invasion inv = InvasionManager.getActive(sp);
            if (inv != null && inv.isRunning()) {
                inv.onEntityDeath(living, source.getEntity());
            }
        }
    }

    /* ============ 登录 ============ */
    public static void onPlayerLogin(ServerPlayer player) {
        PlayerUtil.getOptManager(player).ifPresent(m -> {
            m.loadSummonCardCDs();
                    if (!m.isStarterGiftGiven()) {
                        grantStarterGift(player);
                        m.setStarterGiftGiven(true);
                        final int days = PvZConfig.LOCAL_CONFIG.InvasionSettings.SafeDayLength.getDefault() ;
                        if (days > 0) {
                            m.setInvasionSafeDays(days);
                            m.setLastSafeDaysNotified(days);
                            PlayerUtil.sendMsgTo(player, Component.translatable("invasion.pvz.safe_days", days)
                                    .withStyle(ChatFormatting.YELLOW));
                        }
                    }
            unLockPAZs(player);
        });
    }

    /** 首登礼包：4 张基础卡 + 首封信封。 */
    private static void grantStarterGift(ServerPlayer player) {
        if (ConfigUtil.get(PvZConfig.LOCAL_CONFIG.RuleSettings.GiveBeginnerReward)) {
            player.addItem(new ItemStack(PvZItemRegister.PEA_SHOOTER_CARD.get()));
            player.addItem(new ItemStack(PvZItemRegister.SUN_FLOWER_CARD.get()));
            player.addItem(new ItemStack(PvZItemRegister.WALL_NUT_CARD.get()));
            player.addItem(new ItemStack(PvZItemRegister.POTATO_MINE_CARD.get()));
        }
        player.addItem(ChallengeEnvelopeItem.getChallengeEnvelope(StringUtil.id("strange_help")));
    }

    /* ============ 克隆（死亡/末地返回） ============ */
    public static void clonePlayerData(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wasDeath) {
        if (wasDeath && !ConfigUtil.get(PvZConfig.LOCAL_CONFIG.RuleSettings.KeepSunWhenDie)) {
            newPlayer.getData(PvZAttachments.PLAYER_STATS).set(Resources.SUN_NUM, 50);
            newPlayer.syncData(PvZAttachments.PLAYER_STATS);
        }
        InvasionManager.handlePlayerClone(oldPlayer, newPlayer);
    }

    /* ============ 死亡撒阳光 ============ */
    public static void handlePlayerDeath(LivingDeathEvent ev, Player player) {
        if (player != null && !player.level().isClientSide() && PlayerUtil.isValidPlayer(player)) {
            spawnSunAroundPlayer(player);
        }
    }

    private static void spawnSunAroundPlayer(Player player) {
        final int amount = PlayerUtil.getResource(player, Resources.SUN_NUM);
        final int spawn = amount > 50 ? Mth.clamp((amount - 50) / 10, 25, 250) : 0;
        if (amount > 15) {
            SunEntity.spawnSunsByAmount(player.level(), player.blockPosition(), spawn, 50, 3);
        }
    }

    /* ============ 等级解锁 PAZ ============ */
    public static void unLockPAZs(Player player) {
        final int level = PlayerUtil.getResource(player, Resources.TREE_LVL);
        PVZAPI.lookup().pazs().forEach(type -> {
            if (type.getRequiredLevel() <= level && PlayerUtil.isPAZLocked(player, type)) {
                PlayerUtil.setPAZUnlocked(player, type, true);
                if (ConfigUtil.needUnlockToPlant()) {
                    PlayerUtil.sendMsgTo(player,type.getText()
                                    .append(Component.translatable("help.pvz.is_unlocked"))
                                    .withStyle(ChatFormatting.GREEN));
                }
            }
        });
    }

    /*=====超级模式=======*/
    public static void makeSuperMode(Player player, Entity entity, ItemStack heldStack) {
        if (entity instanceof PlantEntity plantEntity && EntityUtil.isEntityValid(entity)) {
            if (heldStack.getItem().equals(PvZItemRegister.ORIGIN_SWORD.get())
                    || EnchantmentUtil.getLevel(heldStack, EnchantmentRegister.ENERGY_TRANSFER) > 0) {
                if (plantEntity.canStartSuperMode() && (!PlayerUtil.isPlayerSurvival(player)
                        || PlayerUtil.getResource(player, Resources.ENERGY_NUM) > 0)) {
                    if (PlayerUtil.isPlayerSurvival(player)) {
                        PlayerUtil.addResource(player, Resources.ENERGY_NUM, -1);
                    }
                    // 有外层植物（南瓜壳）时：只强化外层（NORMAL → SUPER 生命），
                    // 内层植物不进入超级模式、不显示 super layer。
                    plantEntity.makeSuper();
                    final int treeLevel = PlayerUtil.getResource(player, Resources.TREE_LVL);
                    player.addEffect(new MobEffectInstance(PvZEffectRegister.ENERGETIC_EFFECT,
                            100 + (treeLevel + 1) / 2, 0));
                }
            }
        }
    }


    /*=====移除植物=======*/
    public static void quickRemoveByPlayer(Player player, Entity entity, ItemStack stack) {
        if (!PlayerUtil.isPlayerSurvival(player) || ((entity instanceof AbstractPAZEntity pazEntity)
                && pazEntity.getOwnerUUID().isPresent()
                && player.getUUID().equals(pazEntity.getOwnerUUID().get()))) {
            boolean removed = false;
            if (entity instanceof PlantEntity plantEntity && stack.is(ItemTags.SHOVELS)) {
                if (plantEntity.getOuterPlantInfo().isPresent()) {
                    SunEntity.spawnSunsByAmount(player.level(), plantEntity.blockPosition(),
                            EnchantmentUtil.getSunShovelAmount(stack, plantEntity.getOuterPlantInfo().get().getSunCost()));
                    plantEntity.removeOuterPlant();
                } else if (plantEntity.getPlantInfo().isPresent()) {
                    SunEntity.spawnSunsByAmount(player.level(), plantEntity.blockPosition(),
                            EnchantmentUtil.getSunShovelAmount(stack, plantEntity.getPlantInfo().get().getSunCost()));
                    plantEntity.discard();
                }
                removed = !(stack.getItem() == PvZItemRegister.ORIGIN_SHOVEL.get());
                EntityUtil.playSound(plantEntity, PvZSoundRegister.PLACE_PLANT_GROUND.get());

            } else if (entity instanceof ZombieEntity) {
                // TODO fast remove zombie.
            }
            if (removed && PlayerUtil.isPlayerSurvival(player)) {
                stack.hurtAndBreak(3, player, EquipmentSlot.MAINHAND);
            }
        }
    }
}
