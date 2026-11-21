package com.lying.pvz.utils.enums;

import com.mojang.datafixers.util.Pair;

public class Colors {
	//red
	public static final int RED = 0xFFFF0000;
	public static final int RED_2 = 0xFFE50000;
	public static final int DARK_RED = 0xFFA80E0E;
	public static final int PINK = 0xFFFF8BBC;
	public static final int PIG_PINK = 0xFFF0A5A2;
	//orange
	public static final int ORANGE = 0xFFDF9900;
	public static final int ORANGE_YELLOW = 0xFFF6B201;
	public static final int NUT = 0xFFFFC800;

	//yellow
	public static final int HONNEY_YELLOW = 0xFFEDC343;
	public static final int YELLOW = 0xFFFFFF00;
	public static final int POTATO = 0xFFFFFF9F;
	public static final int LITTLE_YELLOW1 = 0xFFFFF87E;
	public static final int LITTLE_YELLOW2 = 0xFFFBF9A6;
	public static final int YELLOW_GREEN = 0xFFDEFF00;
	public static final int GOLD_YELLOW = 0xFFFFDF00;

	//green
	public static final int CREEPER_GREEN = 0xFF0DA70B;
	public static final int GREEN = 0xFF00FF00;
	public static final int DARK_GREEN = 0xFF009900;
	public static final int PEA_GREEN = 0xFF98FF7E;
	public static final int SQUASH = 0xFFD9FF54;

	//aqua
	public static final int LITTLE_AQUA = 0xFF8FF1D7;
	public static final int DARK_AQUA = 0xFF006DA1;

	//blue
	public static final int BLUE = 0xFF0000FF;
	public static final int ICE = 0xFF00D8FF;
	public static final int BLUE_ICE = 0xFF00B9EF;
	public static final int DOLPHIN_BLUE = 0xFFC6C7E5;
	public static final int BLUE_WHITE = 0xFFEEF3FF;
	public static final int DARK_BLUE = 0xFF000060;

	//purple
	public static final int PURPLE = 0xFFC140D7;
	public static final int LIGHT_PURPLE = 0xFFFF00FF;

	//black
	public static final int BLACK = 0xFF000000;
	public static final int BAT_BLACK = 0xFF4C3E30;

	//other
	public static final int ZOMBIE_SKIN = 0xFF5A8272;
	public static final int STONE = 0xFFA1A1A1;
	public static final int LIT_PINK = 0xFFFFCFF5;
	public static final int SILVER = 0xFFE3E8E8;
	public static final int BONE = 0xFFFAFA96;
	public static final int BRIGHT_TURQUOISE = 0xFF00E5ED;
	public static final int BROWN = 0xFF926239;
	public static final int CYAN = 0xFF00FFFF;
	public static final int DARK_GRAY = 0xFF1E1D16;
	public static final int DARK_VIOLET = 0xFF8200B2;
	public static final int DE_YORK = 0xFF84BC7C;
	public static final int DEEP_PINK = 0xFFFF0072;
	public static final int ELECTRIC_BLUE = 0xFF7CFFFF;
	public static final int ELECTRIC_LIME = 0xFFCFDD00;
	public static final int GOLD = 0xFFCCAC00;
	public static final int GOLDEN_POPPY = 0xFFD6BD00;
	public static final int HELIOTROPE = 0xFFC249FF;
	public static final int IRIS_BLUE = 0xFF00ADD8;
	public static final int LAVENDER_BLUSH = 0xFFFFE2FB;
	public static final int LIGHT_CORAL = 0xFFEF8977;
	public static final int MANGO_TANGO = 0xFFDD6700;
	public static final int MISTY_ROSE = 0xFFFFE8E8;
	public static final int OLIVE = 0xFF5A6800;

	public static final int PIGMENT_GREEN = 0xFF009342;
	public static final int TANGERINE_YELLOW = 0xFFE8D000;
	public static final int TOXIC_GREEN = 0xFF336600;
	public static final int TYRIAN_PURPLE = 0xFF6B0052;
	public static final int WHITE = 0xFFFFFFFF;
	public static final int SILVER_FISH = 0xFF6E6E6E;

	public static final Pair<Integer,Integer> APPEASE_COLOR = new Pair<>(PEA_GREEN, GREEN);
	public static final Pair<Integer,Integer> LIGHT_COLOR = new Pair<>(YELLOW, LITTLE_YELLOW1);
	public static final Pair<Integer,Integer> EXPLOSION_COLOR = new Pair<>(DARK_RED, RED);
	public static final Pair<Integer,Integer> DEFENCE_COLOR = new Pair<>(NUT, ORANGE);
	public static final Pair<Integer,Integer> ICE_COLOR = new Pair<>(LITTLE_AQUA, BLUE);
	public static final Pair<Integer,Integer> ENFORCE_COLOR = new Pair<>(DARK_GREEN, CREEPER_GREEN);
	public static final Pair<Integer,Integer> FLAME_COLOR = new Pair<>(RED, DARK_RED);
	public static final Pair<Integer,Integer> SPEAR_COLOR = new Pair<>(BLACK, SILVER);
	public static final Pair<Integer,Integer> ZOMBIE_COLOR = new Pair<>(ZOMBIE_SKIN, BLACK);
	public static final Pair<Integer,Integer> TOXIC_COLOR = new Pair<>(PURPLE, WHITE);
	public static final Pair<Integer,Integer> ASSIST_COLOR = new Pair<>(SILVER_FISH, DARK_GRAY);
	public static final Pair<Integer,Integer> MAGIC_COLOR = new Pair<>(PINK, PURPLE);
	public static final Pair<Integer,Integer> ARMA_COLOR = new Pair<>(ORANGE_YELLOW, BROWN);
}