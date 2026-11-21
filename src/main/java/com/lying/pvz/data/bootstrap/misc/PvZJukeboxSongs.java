package com.lying.pvz.data.bootstrap.misc;


import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;


public class PvZJukeboxSongs {

    public static final ResourceKey<JukeboxSong> ZOMBIE_ON_YOUR_LAWN_SONG =
            ResourceKey.create(Registries.JUKEBOX_SONG, StringUtil.id("zombie_on_your_lawn"));
    public static final ResourceKey<JukeboxSong> ZEN_GARDEN_SONG =
            ResourceKey.create(Registries.JUKEBOX_SONG, StringUtil.id("zen_garden"));
    public static final ResourceKey<JukeboxSong> POOL_DISC =
            ResourceKey.create(Registries.JUKEBOX_SONG, StringUtil.id("pool_disc"));
    public static final ResourceKey<JukeboxSong> BOWLING_MUSIC_DISC =
            ResourceKey.create(Registries.JUKEBOX_SONG, StringUtil.id("bowling_music_disc"));



    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        HolderGetter<net.minecraft.sounds.SoundEvent> sounds = context.lookup(Registries.SOUND_EVENT);

        // 1.21.1: JukeboxSong 包含声音事件、描述、时长、红石信号
        context.register(ZOMBIE_ON_YOUR_LAWN_SONG, new JukeboxSong(
                sounds.getOrThrow(PvZSoundRegister.ZOMBIE_ON_YOUR_LAWN.key()),
                Component.translatable("item.pvz.music_disc_zombie_on_your_lawn.desc"),
                180.0F,
                15
        ));

        context.register(ZEN_GARDEN_SONG, new JukeboxSong(
                sounds.getOrThrow(PvZSoundRegister.ZEN_GARDEN.key()),
                Component.translatable("item.pvz.music_disc_zen_garden.desc"),
                180.0F,
                15
        ));

        context.register(POOL_DISC, new JukeboxSong(
                sounds.getOrThrow(PvZSoundRegister.POOL.key()), // Holder<SoundEvent>
                Component.translatable("item.pvz.music_disc_pool_disc.desc"),
                180.0F,
                15
        ));

        context.register(BOWLING_MUSIC_DISC, new JukeboxSong(
                sounds.getOrThrow(PvZSoundRegister.BOWLING_MUSIC.key()),
                Component.translatable("item.pvz.music_disc_bowling_music_disc.desc"),
                180.0F,
                15
        ));

    }
}