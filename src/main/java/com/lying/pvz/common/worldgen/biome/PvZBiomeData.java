package com.lying.pvz.common.worldgen.biome;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.data.bootstrap.worldgen.feature.PvZPlaceFeature;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.AmbientMoodSettings;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;

public class PvZBiomeData {


   public  static Biome zenGarden(HolderGetter<PlacedFeature> placedFeatures,HolderGetter<WorldCarver> worldCarvers) {

       BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
               .waterColor(5424306)
               .foliageColorOverride(10271566)
               .dryFoliageColorOverride(12755558)
               .grassColorOverride(11519836)
               .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE);


       MobSpawnSettings.Builder mob = new MobSpawnSettings.Builder()
               .addSpawn(PvZEntityRegister.SUN.get(), 100, new ConstantInt(1));

       BiomeDefaultFeatures.farmAnimals(mob);

       BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
               .addCarver(Carvers.CAVE)
               .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PvZPlaceFeature.PATCH_CHOMPER_PLANT);

      return new Biome.BiomeBuilder()
               .hasPrecipitation(true)
               .temperature(0.8F)
               .downfall(0.35F)
              .setAttribute(EnvironmentAttributes.SKY_COLOR,       ARGB.vector3fFromRGB24(10407919))
              .setAttribute(EnvironmentAttributes.FOG_COLOR,       ARGB.vector3fFromRGB24(15129800))
              .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(5617836))
              .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(PvZParticleRegister.SPORE.get(),
                      0.0025F))
              .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(Optional.empty(), Optional.of(
                      new AmbientMoodSettings(PvZSoundRegister.PLANT_GROW, 6000, 8, 2.0D)),
                      List.of()))
               .specialEffects(effects.build())
               .mobSpawnSettings(mob.build())
               .generationSettings(gen.build())
               .build();
   }

}
