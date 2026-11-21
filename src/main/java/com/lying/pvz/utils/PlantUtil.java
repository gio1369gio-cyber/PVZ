package com.lying.pvz.utils;


import com.lying.pvz.common.level.entity.plant.PlantEntity;

public class PlantUtil {

	public static int CURRENT_PLANT_NUM = 0;
	public static final int MAX_PLANT_LEVEL = 20;
	public static final float PUMPKIN_LIFE = 400;
	public static final float PUMPKIN_SUPER_LIFE = 400;

	/** 把源植物 from 的卡牌身份数据覆盖式克隆到目标 to。 */
	public static void copyPlantData(PlantEntity from, PlantEntity to) {
		// 技能 + 附魔必须一起复制；只 setSkills 会丢 cardEnchantments，
		// 导致 getSkillValue(...) 结果与源植物不一致。
		to.applyCardData(from.getSkillLevels(), from.getCardEnchantments());
		to.setCharmed(from.isCharmed());
		// 源无 owner 时不覆盖，避免清空目标已有 owner。
		from.getOwnerUUID().ifPresent(to::setOwnerUUID);
	}
}