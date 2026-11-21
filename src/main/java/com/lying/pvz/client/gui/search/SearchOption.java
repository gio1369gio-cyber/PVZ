package com.lying.pvz.client.gui.search;


import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchOption {

	private static final List<SearchOption> OPTIONS = new ArrayList<>();
	private static final Map<SearchOption, Integer> OPTION_MAP = new HashMap<>();

	static {
		PVZAPI.lookup().pazs().forEach(type -> {
			if(type.getSummonCard().isPresent()){
				putAlmanac(new SearchOption(type));
			}
		});
	}

	private static void putAlmanac(SearchOption a) {
		OPTION_MAP.put(a, OPTIONS.size());
		OPTIONS.add(a);
	}

	public static SearchOption get(IPAZType type) {
		return OPTIONS.get(type.getSortPriority());
	}

	public static ItemStack getItemStackByOption(SearchOption a) {
		if(a.getType().getSummonCard().isPresent()){
			return new ItemStack(a.getType().getSummonCard().get());
		}
		return ItemStack.EMPTY;
	}

	public static List<SearchOption> getSearchOptionsByCategory(CategoryToggleWidget.SearchCategories category) {
		List<SearchOption> list = new ArrayList<>();
		SearchOption.OPTIONS.forEach((a) -> {
			if(category == CategoryToggleWidget.SearchCategories.ALL){
				list.add(a);
			} else if(a.isPlantType() && category == CategoryToggleWidget.SearchCategories.PLANTS){
				list.add(a);
			} else if(a.isZombieType() && category == CategoryToggleWidget.SearchCategories.ZOMBIES){
				list.add(a);
			}
		});
		return list;
	}

	private final IPAZType type;
	public SearchOption(IPAZType type) {
		this.type = type;
	}

	public IPAZType getType() {
		return this.type;
	}

	public boolean isPlantType() {
		return this.type instanceof IPlantType;
	}

	public boolean isZombieType() {
		return this.type instanceof IZombieType;
	}

	public int ordinal() {
		return OPTION_MAP.get(this);
	}

}