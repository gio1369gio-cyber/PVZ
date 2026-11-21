package com.lying.pvz.utils;

import com.lying.pvz.PvZConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Objects;

public class ConfigUtil {

	public static <T> T get(ModConfigSpec.ConfigValue<T> value) {
		Objects.requireNonNull(value, "ConfigValue");
		try {
			return value.get();
		} catch (IllegalStateException e) {

			return value.getDefault();
		}
	}

	public static boolean isTeamAttackEnable() {
		return get(PvZConfig.LOCAL_CONFIG.RuleSettings.TeamAttack);
	}

	public static boolean enableZombieDropParts() {
		return get(PvZConfig.LOCAL_CONFIG.EntitySettings.ZombieSetting.EnableZombieDropHands);
	}

	public static boolean jackinboxBreak() {
		return get(PvZConfig.LOCAL_CONFIG.EntitySettings.ZombieSetting.JackinboxBreak);
	}

	public static boolean immuineToDamage() {
		return get(PvZConfig.LOCAL_CONFIG.EntitySettings.ZombieSetting.ImmuneDamage);
	}


	public static boolean scatteredInvasions() {
		return get(PvZConfig.LOCAL_CONFIG.InvasionSettings.ScatterInvasions);
	}

	public static boolean isPlantMode() {
		return getPlayerInitialGroup() > 0;
	}

	public static int getPlayerInitialGroup() {
		return get(PvZConfig.LOCAL_CONFIG.EntitySettings.PlayerInitialGroup);
	}


	public static int getBaseSun() {
		return get(PvZConfig.LOCAL_CONFIG.EntitySettings.PlayerBaseSunAmount);
	}

	public static int getLimitPlantCount() {
		return get(PvZConfig.LOCAL_CONFIG.RuleSettings.LimitPlantCount);
	}

	public static int despawnOwnedEntityRange() {
		return get(PvZConfig.LOCAL_CONFIG.RuleSettings.DespawnOwnedEntityRange);
	}

	public static boolean needUnlockToPlant() {
		return get(PvZConfig.LOCAL_CONFIG.RuleSettings.NeedUnlockToPlant);
	}

	public static boolean AllZombieGiveXP() {
		return get(PvZConfig.LOCAL_CONFIG.RuleSettings.AllZombieGiveXP);
	}

	public static boolean renderSunBar() {
		return get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderSunBar);
	}

	public static boolean renderMoneyBar() {
		return get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderMoneyBar);
	}

	public static boolean renderGemBar() {
		return get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderGemBar);
	}

	public static boolean renderTreeLevel() {
		return get(PvZConfig.CLIENT_CONFIG.OverlaySettings.RenderTreeLevel);
	}

	public static boolean isRaidEnable() {
		return get(PvZConfig.LOCAL_CONFIG.ChallengeSettings.EnableChallenge);
	}

	public static int getRaidWaitTime() {
		return get(PvZConfig.LOCAL_CONFIG.ChallengeSettings.ChallengeWaitTime);
	}

	public static int getRaidRange() {
		return get(PvZConfig.LOCAL_CONFIG.ChallengeSettings.ChallengeRange);
	}

	public static int getLimitDamage() {
		return get(PvZConfig.LOCAL_CONFIG.RuleSettings.MaxDamageLimit);
	}

	public static int getPlantMinimumTick() {
		return get(PvZConfig.LOCAL_CONFIG.EntitySettings.PlantSetting.PlantMinimumLiveTick);
	}
}