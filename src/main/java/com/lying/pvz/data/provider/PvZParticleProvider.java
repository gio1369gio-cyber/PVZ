package com.lying.pvz.data.provider;

import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

public class PvZParticleProvider extends ParticleDescriptionProvider {

    /**
     * Creates an instance of the data provider.
     *
     * @param output the expected root directory the data generator outputs to
     */
    public PvZParticleProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void addDescriptions() {

        this.spriteSet(PvZParticleRegister.BLUE_FLAME.get(), StringUtil.id( "blue_flame"));

        this.spriteSet(PvZParticleRegister.DIRT_BURST_OUT.get(), StringUtil.id( "dirt_burst_out"));

        this.spriteSet(PvZParticleRegister.DOOM.get(), StringUtil.id( "doom_0"));

        this.spriteSet(PvZParticleRegister.FROZEN_MELON_SLICE.get(), StringUtil.id( "frozen_melon_slice"));

        this.spriteSet(PvZParticleRegister.FUME.get(), StringUtil.id( "fume"));

        this.spriteSet(PvZParticleRegister.MELON_SLICE.get(), StringUtil.id( "melon_slice"));

        this.spriteSet(PvZParticleRegister.POP_CORN.get(), StringUtil.id( "pop_corn"));

        this.spriteSet(PvZParticleRegister.SLEEP.get(), StringUtil.id( "sleep"));

        this.spriteSet(PvZParticleRegister.SNOW_FLOWER.get(), StringUtil.id( "snow_flower"));

        this.spriteSet(PvZParticleRegister.SPORE.get(), StringUtil.id( "spore"));

        this.spriteSet(PvZParticleRegister.YELLOW_FLAME.get(), StringUtil.id( "yellow_flame"));

        this.spriteSet(PvZParticleRegister.GOLD_NUGGET.get(), StringUtil.id("gold_nugget"));

        this.spriteSet(PvZParticleRegister.ORIGINAL_FUME.get(), StringUtil.id("original_fume"));

        this.spriteSet(PvZParticleRegister.SONIC_BOOM.get(), StringUtil.id("sonic_boom"),6,false);

        this.spriteSet(PvZParticleRegister.RED_BOMB.get(),
                Identifier.withDefaultNamespace("explosion"), 16, false);

        this.spriteSet(PvZParticleRegister.YELLOW_BOMB.get(),
                Identifier.withDefaultNamespace("explosion"), 16, false);

        this.spriteSet(PvZParticleRegister.GREEN_SWEEP.get(),
                Identifier.withDefaultNamespace("sweep"), 8, false);

    
    }
}