package com.lying.pvz.common.worldgen.biome;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.data.bootstrap.worldgen.feature.PvZPlaceFeature;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.AmbientMoodSettings;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
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

       BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
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

   /**
    * 远古埃及：高温无雨的黄沙主题，烈日与远距离能见度。
    */
   public static Biome ancientEgypt(HolderGetter<PlacedFeature> placedFeatures,HolderGetter<WorldCarver> worldCarvers) {

       BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
               .waterColor(0x4A7A8C)
               .foliageColorOverride(0xA8995A)
               .dryFoliageColorOverride(0xB9A465)
               .grassColorOverride(0xC2A86B);

       MobSpawnSettings.Builder mob = new MobSpawnSettings.Builder()
               .addSpawn(PvZEntityRegister.RA_ZOMBIE.get(), 8, new ConstantInt(1))
               .addSpawn(PvZEntityRegister.TOMB_STONE.get(), 4, new ConstantInt(1));

       BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
       BiomeDefaultFeatures.addDefaultOres(gen);
       BiomeDefaultFeatures.addDefaultSoftDisks(gen);
       BiomeDefaultFeatures.addDesertVegetation(gen);
       BiomeDefaultFeatures.addDesertExtraVegetation(gen);
       BiomeDefaultFeatures.addDesertExtraDecoration(gen);

      return new Biome.BiomeBuilder()
               .hasPrecipitation(false)
               .temperature(1.4F)
               .downfall(0.0F)
               .setAttribute(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(0xE8C57A))
               .setAttribute(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(0xD9B878))
               .setAttribute(EnvironmentAttributes.FOG_START_DISTANCE, 40.0F)
               .setAttribute(EnvironmentAttributes.FOG_END_DISTANCE, 240.0F)
               .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(0x6E8E96))
               .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
               .specialEffects(effects.build())
               .mobSpawnSettings(mob.build())
               .generationSettings(gen.build())
               .build();
   }

   /**
    * 海盗港湾：温暖湿润的浅海群岛，晴空与海雾。
    */
   public static Biome pirateHarbor(HolderGetter<PlacedFeature> placedFeatures,HolderGetter<WorldCarver> worldCarvers) {

       BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
               .waterColor(0x2478A8)
               .foliageColorOverride(0x4A8F5C)
               .dryFoliageColorOverride(0x6A9A52)
               .grassColorOverride(0x6AA84F);

       MobSpawnSettings.Builder mob = new MobSpawnSettings.Builder()
               .addSpawn(PvZEntityRegister.ZOMBIE_DOLPHIN.get(), 6, new ConstantInt(1))
               .addSpawn(PvZEntityRegister.DOLPHIN_RIDER_ZOMBIE.get(), 4, new ConstantInt(1))
               .addSpawn(PvZEntityRegister.SNORKEL_ZOMBIE.get(), 6, new ConstantInt(1));
       BiomeDefaultFeatures.farmAnimals(mob);

       BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
       BiomeDefaultFeatures.addDefaultOres(gen);
       BiomeDefaultFeatures.addDefaultSoftDisks(gen);
       BiomeDefaultFeatures.addNearWaterVegetation(gen);
       BiomeDefaultFeatures.addDefaultGrass(gen);

      return new Biome.BiomeBuilder()
               .hasPrecipitation(true)
               .temperature(0.9F)
               .downfall(0.8F)
               .setAttribute(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(0x87CEEB))
               .setAttribute(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(0xB0D8E8))
               .setAttribute(EnvironmentAttributes.FOG_START_DISTANCE, 60.0F)
               .setAttribute(EnvironmentAttributes.FOG_END_DISTANCE, 220.0F)
               .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(0x2E7FA8))
               .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
               .specialEffects(effects.build())
               .mobSpawnSettings(mob.build())
               .generationSettings(gen.build())
               .build();
   }

   /**
    * 狂野西部：干燥荒漠，落日色天空与扬沙能见度。
    */
   public static Biome wildWest(HolderGetter<PlacedFeature> placedFeatures,HolderGetter<WorldCarver> worldCarvers) {

       BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
               .waterColor(0x6B7B5A)
               .foliageColorOverride(0x8B7B45)
               .dryFoliageColorOverride(0xA08B55)
               .grassColorOverride(0xA08B55);

       MobSpawnSettings.Builder mob = new MobSpawnSettings.Builder()
               .addSpawn(PvZEntityRegister.NORMAL_ZOMBIE.get(), 8, new ConstantInt(1))
               .addSpawn(PvZEntityRegister.CONEHEAD_ZOMBIE.get(), 6, new ConstantInt(1))
               .addSpawn(PvZEntityRegister.GARGANTUAR.get(), 2, new ConstantInt(1));

       BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
       BiomeDefaultFeatures.addDefaultOres(gen);
       BiomeDefaultFeatures.addDefaultSoftDisks(gen);
       BiomeDefaultFeatures.addDesertVegetation(gen);
       BiomeDefaultFeatures.addDesertExtraVegetation(gen);
       BiomeDefaultFeatures.addDesertExtraDecoration(gen);

      return new Biome.BiomeBuilder()
               .hasPrecipitation(false)
               .temperature(1.1F)
               .downfall(0.1F)
               .setAttribute(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(0xE8A86B))
               .setAttribute(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(0xC98F5F))
               .setAttribute(EnvironmentAttributes.FOG_START_DISTANCE, 35.0F)
               .setAttribute(EnvironmentAttributes.FOG_END_DISTANCE, 200.0F)
               .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(0x7A7A5A))
               .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
               .specialEffects(effects.build())
               .mobSpawnSettings(mob.build())
               .generationSettings(gen.build())
               .build();
   }

   /**
    * 未来世界：科技蓝紫天幕、霓虹青雾、电弧粒子。
    */
   public static Biome futureWorld(HolderGetter<PlacedFeature> placedFeatures,HolderGetter<WorldCarver> worldCarvers) {

       BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
               .waterColor(0x2C5F8A)
               .foliageColorOverride(0x4A7FA8)
               .dryFoliageColorOverride(0x5E7BA0)
               .grassColorOverride(0x4E7C93);

       MobSpawnSettings.Builder mob = new MobSpawnSettings.Builder()
               .addSpawn(PvZEntityRegister.TARGET_ARROW_ZOMBIE.get(), 5, new ConstantInt(1))
               .addSpawn(PvZEntityRegister.MC_ZOMBIE.get(), 4, new ConstantInt(1));
       BiomeDefaultFeatures.farmAnimals(mob);

       BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
       BiomeDefaultFeatures.addDefaultOres(gen, true);
       BiomeDefaultFeatures.addFossilDecoration(gen);
       BiomeDefaultFeatures.addDefaultGrass(gen);

      return new Biome.BiomeBuilder()
               .hasPrecipitation(false)
               .temperature(0.65F)
               .downfall(0.35F)
               .setAttribute(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(0x3B3A7A))
               .setAttribute(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(0x4A9CB8))
               .setAttribute(EnvironmentAttributes.FOG_START_DISTANCE, 20.0F)
               .setAttribute(EnvironmentAttributes.FOG_END_DISTANCE, 140.0F)
               .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(0x2C5F8A))
               .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(PvZParticleRegister.BLUE_FLAME.get(), 0.002F))
               .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD)
               .specialEffects(effects.build())
               .mobSpawnSettings(mob.build())
               .generationSettings(gen.build())
               .build();
   }

}
