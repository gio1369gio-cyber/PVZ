package com.lying.pvz.api.content;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Optional;

/**
 * 卡片等级（稀有度）。
 *
 * <p>等级用于抽奖权重、拆解收益、交易定价、附魔点数等。
 */
public interface IRankType extends IPVZType {

	/**
	 * 抽奖权重。值越小越稀有。
	 */
	int getWeight();

	/**
	 * 拆解收益（拆卡返还的材料数量基准）。
	 */
	int getValue();

	/**
	 * 商品售价（与村民 / 戴夫交易时的价格基准）。
	 *
	 * <p>默认复用 {@link #getValue()}，保持与旧版行为一致。
	 * 若某个等级的"拆解价值"与"售价"需要分离，覆写本方法即可。
	 */
	default int getTradePrice() {
		return getValue();
	}

	/**
	 * 卡片可获得的附魔点数。返回 0 表示该等级不可附魔（如 MEGA）。
	 */
	int getEnchantPoint();

	/**
	 * 该等级对应的模板卡物品。
	 */
	Item getTemplateCard();

	/**
	 * 该等级卡片的物品标签。可能不存在（如 MEGA）。
	 */
	Optional<TagKey<Item>> getCardTag();

	/**
	 * 该等级卡片拆解产出的材料标签。可能不存在（如 MEGA）。
	 */
	Optional<TagKey<Item>> getMaterial();
}