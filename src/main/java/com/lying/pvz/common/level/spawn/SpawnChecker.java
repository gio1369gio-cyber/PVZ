package com.lying.pvz.common.level.spawn;

import com.lying.pvz.common.level.entity.creature.FoodieZombieEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

public class SpawnChecker {


    public static boolean canZombieSpawn(EntityType<? extends PVZZombieEntity> zombieType, ServerLevelAccessor level,
                                         EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return checkSpawn(zombieType, level, reason, pos, random);
    }

    public static boolean canLavaZombieSpawn(EntityType<? extends PVZZombieEntity> zombieType, ServerLevelAccessor level,
                                             EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() == Difficulty.HARD && checkSpawn(zombieType, level, reason, pos, random);
    }

    public static boolean canYetiSpawn(EntityType<? extends PVZZombieEntity> zombieType, ServerLevelAccessor level,
                                       EntitySpawnReason reason, BlockPos pos, RandomSource random) {

        if (level instanceof ServerLevel serverLevel
                && canZombieSpawn(zombieType, level, reason, pos, random)) {
            return serverLevel.isThundering()  && random.nextInt(3) == 0;
        }
        return false;
    }


    private static boolean checkSpawn(EntityType<? extends PathfinderMob> zombieType, ServerLevelAccessor level,
                                      EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return isDarkEnough(level, pos)
                && level.getDifficulty() != Difficulty.PEACEFUL
                && Mob.checkMobSpawnRules(zombieType, level, reason, pos, random);
    }

    private static boolean isDarkEnough(ServerLevelAccessor level, BlockPos pos) {
        return level.getBrightness(LightLayer.BLOCK, pos) < 7;
    }

    public static boolean canSpawn(EntityType<FoodieZombieEntity> zombieEntity,
                                   ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {

        return false;
    }
}