package com.lying.pvz.common.init;


import com.lying.pvz.PvZMod;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZSoundRegister {

    // https://minecraft.gamepedia.com/Sounds.json#Java_Edition_values
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, PvZMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ADVANCEMENT = registerSound("advancement");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_SIREN = registerSound("zombie_siren");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANT_SIREN = registerSound("plant_siren");
    public static final DeferredHolder<SoundEvent, SoundEvent> BALLOON_INFLATE = registerSound("balloon_inflate");
    public static final DeferredHolder<SoundEvent, SoundEvent> BALLOON_POP = registerSound("balloon_pop");
    public static final DeferredHolder<SoundEvent, SoundEvent> BASKETBALL = registerSound("basketball");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIG_CHOMP = registerSound("big_chomp");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOVER = registerSound("blover");
    public static final DeferredHolder<SoundEvent, SoundEvent> POLE = registerSound("pole");
    public static final DeferredHolder<SoundEvent, SoundEvent> POLE_JUMP = registerSound("pole_jump");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAMMER_BONK = registerSound("hammer_bonk");
    public static final DeferredHolder<SoundEvent, SoundEvent> EDGAR_SHOOT = registerSound("edgar_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> EDGAR_EXPLOSION = registerSound("edgar_explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOWLING = registerSound("bowling");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOWLING_HIT = registerSound("bowling_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUG_SPRAY = registerSound("bug_spray");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUNGEE_SCREAM = registerSound("bungee_scream");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUTTER = registerSound("butter");
    public static final DeferredHolder<SoundEvent, SoundEvent> NO = registerSound("no");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHERRY_BOMB = registerSound("cherry_bomb");
    public static final DeferredHolder<SoundEvent, SoundEvent> JEWEL_DROP = registerSound("jewel_drop");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHOMP = registerSound("chomp");
    public static final DeferredHolder<SoundEvent, SoundEvent> COB_LAUNCH = registerSound("cob_launch");
    public static final DeferredHolder<SoundEvent, SoundEvent> COIN_PICK = registerSound("coin_pick");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRAZY_DAVE = registerSound("crazy_dave");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRAZY_SAY = registerSound("crazy_say");
    public static final DeferredHolder<SoundEvent, SoundEvent> DAVE_HURT = registerSound("dave_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> DAVE_HAPPY = registerSound("dave_happy");
    public static final DeferredHolder<SoundEvent, SoundEvent> DANCE_MUSIC = registerSound("dance_music");
    public static final DeferredHolder<SoundEvent, SoundEvent> JEWEL_PICK = registerSound("jewel_pick");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIRT_RISE = registerSound("dirt_rise");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOLPHIN_SAY = registerSound("dolphin_say");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOLPHIN_JUMP = registerSound("dolphin_jump");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOOM_SHROOM = registerSound("doom_shroom");
    public static final DeferredHolder<SoundEvent, SoundEvent> EDGAR_LAUGH = registerSound("edgar_laugh");
    public static final DeferredHolder<SoundEvent, SoundEvent> COIN_DROP = registerSound("coin_drop");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAR_EXPLOSION = registerSound("car_explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> FERTILIZER = registerSound("fertilizer");
    public static final DeferredHolder<SoundEvent, SoundEvent> FINAL_WIN = registerSound("final_win");
    public static final DeferredHolder<SoundEvent, SoundEvent> FINAL_WAVE = registerSound("final_wave");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAME_HIT = registerSound("flame_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRAG = registerSound("drag");
    public static final DeferredHolder<SoundEvent, SoundEvent> FROZEN = registerSound("frozen");
    public static final DeferredHolder<SoundEvent, SoundEvent> FUME = registerSound("fume");
    public static final DeferredHolder<SoundEvent, SoundEvent> GROUND_SHAKE = registerSound("ground_shake");
    public static final DeferredHolder<SoundEvent, SoundEvent> GARGANTUAR_DEATH = registerSound("gargantuar_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_GROAN = registerSound("zombie_groan");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANT_DEATH = registerSound("plant_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> HUGE_WAVE = registerSound("huge_wave");
    public static final DeferredHolder<SoundEvent, SoundEvent> ROBOT_MOVE = registerSound("robot_move");
    public static final DeferredHolder<SoundEvent, SoundEvent> IMP_HAPPY = registerSound("imp_happy");
    public static final DeferredHolder<SoundEvent, SoundEvent> JACK_SURPRISE = registerSound("jack_surprise");
    public static final DeferredHolder<SoundEvent, SoundEvent> JACK_MUSIC = registerSound("jack_music");
    public static final DeferredHolder<SoundEvent, SoundEvent> JALAPENO = registerSound("jalapeno");
    public static final DeferredHolder<SoundEvent, SoundEvent> STEEL_LADDER = registerSound("steel_ladder");
    public static final DeferredHolder<SoundEvent, SoundEvent> LAWN_MOWER = registerSound("lawn_mower");
    public static final DeferredHolder<SoundEvent, SoundEvent> GARGANTUAR_GROAN = registerSound("gargantuar_groan");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOSE_MUSIC = registerSound("lose_music");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGNET = registerSound("magnet");
    public static final DeferredHolder<SoundEvent, SoundEvent> MELON_HIT = registerSound("melon_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> HYPNO = registerSound("hypno");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_ANGRY = registerSound("zombie_angry");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHANGE_PAPER = registerSound("change_paper");
    public static final DeferredHolder<SoundEvent, SoundEvent> PAPER_BROKEN = registerSound("paper_broken");
    public static final DeferredHolder<SoundEvent, SoundEvent> PHONOGRAPH = registerSound("phonograph");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLACE_PLANT_GROUND = registerSound("place_plant_ground");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLACE_PLANT_WATER = registerSound("place_plant_water");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLASTIC_HIT = registerSound("plastic_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> POGO = registerSound("pogo");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUN_PICK = registerSound("sun_pick");
    public static final DeferredHolder<SoundEvent, SoundEvent> POOL_CLEANER = registerSound("pool_cleaner");
    public static final DeferredHolder<SoundEvent, SoundEvent> POTATO_MINE = registerSound("potato_mine");
    public static final DeferredHolder<SoundEvent, SoundEvent> PRIZE_DROP = registerSound("prize_drop");
    public static final DeferredHolder<SoundEvent, SoundEvent> PUFF = registerSound("puff");
    public static final DeferredHolder<SoundEvent, SoundEvent> READY = registerSound("ready");
    public static final DeferredHolder<SoundEvent, SoundEvent> DAVE_SCREAM = registerSound("dave_scream");
    public static final DeferredHolder<SoundEvent, SoundEvent> METAL_HIT = registerSound("metal_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHOVEL = registerSound("shovel");
    public static final DeferredHolder<SoundEvent, SoundEvent> SLOT_MACHINE = registerSound("slot_machine");
    public static final DeferredHolder<SoundEvent, SoundEvent> SLURP = registerSound("slurp");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNOW_SHOOT = registerSound("snow_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPLAT = registerSound("splat");
    public static final DeferredHolder<SoundEvent, SoundEvent> SQUASH_HMM = registerSound("squash_hmm");
    public static final DeferredHolder<SoundEvent, SoundEvent> SWING = registerSound("swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> PULT_THROW = registerSound("pult_throw");
    public static final DeferredHolder<SoundEvent, SoundEvent> VASE_BREAKING = registerSound("vase_breaking");
    public static final DeferredHolder<SoundEvent, SoundEvent> WAKE_UP = registerSound("wake_up");
    public static final DeferredHolder<SoundEvent, SoundEvent> WIN_MUSIC = registerSound("win_music");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANT_GROW = registerSound("plant_grow");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANTERN = registerSound("plantern");
    public static final DeferredHolder<SoundEvent, SoundEvent> YUCK = registerSound("yuck");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAR = registerSound("car");
    public static final DeferredHolder<SoundEvent, SoundEvent> BRUH = registerSound("bruh");
    public static final DeferredHolder<SoundEvent, SoundEvent> PENNY_SAY = registerSound("penny_say");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUZZER = registerSound("buzzer");

    // disc
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_ON_YOUR_LAWN = registerSound("zombie_on_your_lawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZEN_GARDEN = registerSound("zen_garden");
    public static final  DeferredHolder<SoundEvent, SoundEvent> POOL = registerSound("pool");
    public static final  DeferredHolder<SoundEvent, SoundEvent> BOWLING_MUSIC = registerSound("bowling_music");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(StringUtil.id(name)));
    }
}