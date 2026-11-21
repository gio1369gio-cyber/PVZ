package com.lying.pvz.common.level.spawn;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/**
 * 实体生成放置规则注册管理器。
 * 仅由 PVZModBusEvents 在 RegisterSpawnPlacementsEvent（mod bus）中调用；
 * 不订阅事件、不读配置、不持状态，只做注册分发。
 */
public final class SpawnPlacementRegistry {

    private SpawnPlacementRegistry() {}

    /** 头顶可见天空，且上方 5 格也可见天空。 */
    public static final SpawnPlacementType IN_SKY = (level, pos, type) ->
            level.canSeeSky(pos) && level.canSeeSky(pos.offset(0, 5, 0));

    /** 脚下为雪或下方为雪块。 */
    public static final SpawnPlacementType ON_SNOW = (level, pos, type) ->
            level.getBlockState(pos).is(Blocks.SNOW) || level.getBlockState(pos.below()).is(Blocks.SNOW_BLOCK);

    public static void register(RegisterSpawnPlacementsEvent event) {
        registerSunAndNpc(event);
        registerGroundZombies(event);
        registerSpecialZombies(event);
        registerPlantZombies(event);
    }

    private static void registerSunAndNpc(RegisterSpawnPlacementsEvent event) {
        event.register(PvZEntityRegister.SUN.get(),
                IN_SKY,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SunEntity::canSunSpawn,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(PvZEntityRegister.CRAZY_DAVE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static void registerGroundZombies(RegisterSpawnPlacementsEvent event) {
        event.register(PvZEntityRegister.NORMAL_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.FLAG_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.CONEHEAD_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.POLE_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.BUCKETHEAD_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.TOMB_STONE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.GIGA_TOMB_STONE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.NEWSPAPER_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.SCREENDOOR_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.FOOTBALL_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.GIGA_FOOTBALL_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.DANCING_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.BACKUP_DANCER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.OLD_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.SUNDAY_EDITION_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.ZOMBONI.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.PUMPKIN_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.TRICK_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.JACK_IN_BOX_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.DIGGER_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.POGO_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.LADDER_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.CATAPULT_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.GARGANTUAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.GIGA_GARGANTUAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static void registerSpecialZombies(RegisterSpawnPlacementsEvent event) {
        event.register(PvZEntityRegister.SNORKEL_ZOMBIE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.BOBSLE_TEAM.get(), ON_SNOW, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.DOLPHIN_RIDER.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.LAVA_ZOMBIE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canLavaZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.BALLOON_ZOMBIE.get(), IN_SKY, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.YETI_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canYetiSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.BUNGEE_ZOMBIE.get(), IN_SKY, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static void registerPlantZombies(RegisterSpawnPlacementsEvent event) {
        event.register(PvZEntityRegister.PEASHOOTER_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.WALLNUT_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.GATLINGPEA_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.TALLNUT_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.SQUASH_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(PvZEntityRegister.JALAPENO_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canZombieSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}