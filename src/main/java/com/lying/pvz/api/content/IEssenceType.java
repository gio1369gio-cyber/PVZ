package com.lying.pvz.api.content;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

/**
 * 精华类型（如阳光精华、寒冰精华）。
 *
 * <p>精华物品可从精华矿或辐射方块获取。
 */
public interface IEssenceType extends IPVZType {

	/**
	 * 精华对应的物品。
	 */
	Item getEssenceItem();

	/**
	 * 精华对应的矿石方块。
	 */
	Block getEssenceOre();

	/**
	 * 可转化为精华矿的辐射方块（单一方块变体）。无则为空。
	 */
	Optional<Block> getRadiationBlock();

	/**
	 * 可转化为精华矿的辐射方块标签（批量变体）。无则为空。
	 */
	Optional<TagKey<Block>> getRadiationBlockTag();
}