package com.lying.pvz.utils;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.entity.ability.*;
import com.lying.pvz.api.projectile.IBullet;
import com.lying.pvz.common.init.PvZAttributes;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.common.level.entity.EntityGroupHander;
import com.lying.pvz.common.level.entity.MultiPartEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.common.level.entity.zombie.pool.BalloonZombieEntity;
import com.lying.pvz.common.payload.SpawnParticlePayload;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class EntityUtil {
    public static final RandomSource RAND = RandomSource.create();

    public static Vec3 getNormalisedVector2d(@NonNull Entity a, @NonNull Entity b) {
        final double dx = b.getX() - a.getX();
        final double dz = b.getZ() - a.getZ();
        final double dis = Math.sqrt(dx * dx + dz * dz);
        // 贴脸/完全重叠时水平距离为 0，避免 0/0 产生 NaN 污染后续计算。
        if (dis < 1.0E-4D) {
            return new Vec3(1.0D, 0, 0);
        }
        return new Vec3(dx / dis, 0, dz / dis);
    }

    public static boolean canEntityBeRemoved(Entity entity) {
        if(entity instanceof ZombieEntity zombie) {
            return zombie.canZombieBeRemoved();
        }
        if(!BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getNamespace().equals(PvZMod.MOD_ID)
                && (entity instanceof LivingEntity living)) {
            return living.getMaxHealth() <= 20F;
        }
        return true;
    }


    public static void spawnParticle(Entity entity, int type) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null,
                    entity.getX(), entity.getY(), entity.getZ(), 40.0,
                    new SpawnParticlePayload(type, entity.getX(), entity.getY(), entity.getZ()));
        }
    }

    public static void spawnSpeedParticle(Entity entity, ParticleOptions type, float speed) {
        WorldUtil.spawnRandomSpeedParticle(entity.level(), type,
                entity.position().add(0, entity.getBbHeight(), 0), speed);
    }


    public static void playSound(Entity entity, SoundEvent ev) {
        if(ev != null) {
            entity.playSound(ev, 1.0F, RAND.nextFloat() * 0.2F + 0.9F);
        }
    }

    public static boolean isEntityValid(Entity target) {
        return target != null && target.isAlive();
    }

    public static List<LivingEntity> getRandomLivingInRange(Level world, LivingEntity attacker, AABB aabb, int cnt) {
        List<LivingEntity> list = new ArrayList<>();
        for(LivingEntity living : EntityUtil.getTargetableLivings(attacker, aabb)){
            list.add(living);
            if(-- cnt <= 0) break;
        }
        return list;
    }

    public static float getCurrentHealth(LivingEntity target) {
        if(target instanceof AbstractPAZEntity paz) {
            return (float) paz.getCurrentHealth();
        }
        return target.getHealth();
    }


    public static float getCurrentMaxHealth(LivingEntity target) {
        if(target instanceof AbstractPAZEntity paz) {
            return (float) paz.getCurrentMaxHealth();
        }
        return target.getMaxHealth();
    }

    public static float getMaxHealthDamage(LivingEntity target) {
        return getMaxHealthDamage(target, 1);
    }

    public static float getMaxHealthDamage(LivingEntity target, float multiple) {
        return (float) (EntityUtil.getCurrentMaxHealth(target) * multiple);
    }

    public static boolean isEntityBoss(@NonNull LivingEntity entity) {
        if(entity instanceof AbstractPAZEntity) {
            if(entity instanceof ZombieEntity) {
                return entity instanceof AbstractBossZombieEntity;
            }
            if(entity instanceof PlantEntity) {
                return false;
            }
        }
        return entity.getHealth() > 100F;
    }

    public static boolean canSeeEntity(Entity entity, Entity target) {
        Vec3 start = entity.position().add(0, entity.getEyeHeight(), 0);
        Vec3 lowerEnd = target.position();
        Vec3 upperEnd = lowerEnd.add(0, target.getBbHeight(), 0);
        ClipContext ray1 = new ClipContext(start, lowerEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
        ClipContext ray2 = new ClipContext(start, upperEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
        return entity.level().clip(ray1).getType() != HitResult.Type.BLOCK
                || entity.level().clip(ray2).getType() != HitResult.Type.BLOCK;
    }

    public static boolean canEntityPass(Entity entity, Vec3 vec, float length) {
        Vec3 lowStart = entity.position();
        Vec3 upperStart = entity.position().add(0, entity.getBbHeight(), 0);
        Vec3 lowerEnd = lowStart.add(vec.scale(length));
        Vec3 upperEnd = upperStart.add(vec.scale(length));
        ClipContext ray1 = new ClipContext(lowStart, lowerEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
        ClipContext ray2 = new ClipContext(upperStart, upperEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
        return entity.level().clip(ray1).getType() != HitResult.Type.BLOCK
                && entity.level().clip(ray2).getType() != HitResult.Type.BLOCK;
    }


    public static void createEntityAndSpawn(Level world, EntityType<?> type, BlockPos pos) {
        Entity entity = type.create(world,EntitySpawnReason.MOB_SUMMONED);
        if (entity == null) return;
        onEntitySpawn(world, entity, pos);
    }

    public static void onEntitySpawn(LevelAccessor world, Entity entity, BlockPos pos) {
        entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if(entity instanceof Mob mob && world instanceof ServerLevelAccessor sla) {
            mob.finalizeSpawn(sla, sla.getCurrentDifficultyAt(entity.blockPosition()),  EntitySpawnReason.SPAWNER, null);
        }
        world.addFreshEntity(entity);
    }

    public static void onEntityRandomPosSpawn(LevelAccessor world, Entity entity, BlockPos pos, int dis) {
        pos = pos.offset(MathUtil.getRandomInRange(world.getRandom(), dis),
                world.getRandom().nextInt(dis) + 1,
                MathUtil.getRandomInRange(world.getRandom(), dis));
        onEntitySpawn(world, entity, pos);
    }


    public static boolean isOnSnow(Entity entity) {
        BlockPos pos = entity.blockPosition();
        return entity.level().getBlockState(pos).is(Blocks.SNOW)
                || entity.level().getBlockState(pos.below()).is(Blocks.SNOW_BLOCK);
    }

    public static boolean isOnIce(Entity entity) {
        BlockPos pos = entity.blockPosition();
        return entity.level().getBlockState(pos).is(net.minecraft.tags.BlockTags.ICE)
                || entity.level().getBlockState(pos.below()).is(net.minecraft.tags.BlockTags.ICE);
    }

    public static double getAttackRange(Entity a, Entity b, double r) {
        double two = Math.sqrt(2);
        double dis = (a.getBbWidth() / two + b.getBbWidth() / two + r);
        return dis * dis;
    }

    public static double getNearestDistance(Entity a, Entity b) {
        double dx = a.getX() - b.getX();
        double dz = a.getZ() - b.getZ();
        double dy = 0;
        if(a.getY() > b.getY() + b.getBbHeight()) {
            dy = a.getY() - b.getY() - b.getBbHeight();
        } else if(b.getY() > a.getY() + a.getBbHeight()) {
            dy = b.getY() - a.getY() - a.getBbHeight();
        }
        return dx * dx + dy * dy + dz * dz;
    }

    @Nullable
    public static Player getEntityOwner(Level world, @Nullable Entity entity) {
        UUID uuid = null;
        if(entity instanceof IHasOwner owner) {
            uuid = owner.getOwnerUUID().orElse(null);
        }
        return uuid == null ? null : world.getPlayerByUUID(uuid);
    }

    /** 目标被迷雾隐藏（FOG_HIDDEN）且攻击者及其 owner 玩家没有光眼时，返回 true（不可选中/攻击）。 */
    public static boolean isFogHiddenTo(@Nullable Entity attacker, Entity target) {
        if (!(target instanceof LivingEntity living) || !living.hasEffect(PvZEffectRegister.FOG_HIDDEN)) {
            return false;
        }
        return !hasLightEye(attacker);
    }

    private static boolean hasLightEye(@Nullable Entity attacker) {
        if (attacker instanceof IBullet bullet) {
            attacker = bullet.ownerEntity();
        }
        if (attacker == null) {
            return false;
        }
        if (attacker instanceof Player player) {
            return player.hasEffect(PvZEffectRegister.LIGHT_EYE_EFFECT);
        }
        final Player owner = getEntityOwner(attacker.level(), attacker);
        return owner != null && owner.hasEffect(PvZEffectRegister.LIGHT_EYE_EFFECT);
    }

    public static boolean canTargetEntity(Entity attacker, Entity target) {
        if(attacker instanceof AbstractPAZEntity paz) {
            return paz.checkCanPAZTarget(target);
        }
        return checkCanEntityBeTarget(attacker, target);
    }

    public static boolean canAttackEntity(Entity attacker, Entity target) {
        if(attacker instanceof AbstractPAZEntity paz) {
            return paz.checkCanPAZAttack(target);
        }
        return checkCanEntityBeAttack(attacker, target);
    }

    public static boolean checkCanEntityBeTarget(Entity attacker, Entity target) {
        return checkCanEntityBeTarget(attacker, target, false);
    }

    /**
     * 目标是否可被选中。{@code ignoreFog} 为 true 时跳过迷雾隐怪检查，
     * 供范围伤害（爆炸/火焰/冰冻/溅射）这类"不依赖视野锁定"的攻击使用。
     */
    public static boolean checkCanEntityBeTarget(Entity attacker, Entity target, boolean ignoreFog) {
        if(attacker == null || target == null || EntityUtil.isOpEntity(target)) {
            return false;
        }
        if(!ignoreFog && isFogHiddenTo(attacker, target)) {
            return false;
        }
        if(target instanceof MultiPartEntity part) {
            return checkCanEntityBeTarget(attacker, part.getOwner(), ignoreFog);
        }
        if(ConfigUtil.isTeamAttackEnable()) {
            final var team1 = getEntityTeam(attacker.level(), attacker);
            final var team2 = getEntityTeam(attacker.level(), target);
            if(team1 != null && team2 != null) {
                return isEntityCharmed(attacker) ^ isEntityCharmed(target)
                        ? team1.isAlliedTo(team2) : ! team1.isAlliedTo(team2);
            }
        }
        if(attacker instanceof LivingEntity living) {
            LivingEntity s = living.getLastHurtByMob();
            if (s != null) {
                if (target.is(s) && checkCanEntityBeAttack(attacker, target)) {
                    return true;
                }
            }
        }
        return EntityGroupHander.checkCanTarget(getEntityGroup(attacker), getEntityGroup(target));
    }

    public static boolean checkCanEntityBeAttack(Entity attacker, Entity target) {
        if(attacker == null || target == null) {
            return false;
        }
        if(isFogHiddenTo(attacker, target)) {
            return false;
        }
        if(target instanceof MultiPartEntity part) {
            return checkCanEntityBeAttack(attacker, part.getOwner());
        }
        if(target instanceof Player player && !PlayerUtil.isPlayerSurvival(player)) {
            return false;
        }
        if(ConfigUtil.isTeamAttackEnable()) {
            final var team1 = getEntityTeam(attacker.level(), attacker);
            final var team2 = getEntityTeam(attacker.level(), target);
            if(team1 != null && team2 != null) {
                return isEntityCharmed(attacker) ^ isEntityCharmed(target)
                        ? team1.isAlliedTo(team2) : ! team1.isAlliedTo(team2);
            }
        }
        return EntityGroupHander.checkCanAttack(getEntityGroup(attacker), getEntityGroup(target));
    }

    public static boolean isFriendly(Entity a, Entity b){
        return (a == null || b == null) ? false : ! EntityGroupHander.checkCanTarget(getEntityGroup(a), getEntityGroup(b));
    }


    public static GroupType getEntityGroup(Entity entity) {
        if(entity instanceof Player player){
            return EntityGroupHander.getPlayerGroup(player);
        }
        return (entity instanceof IHasGroup group) ? group.getEntityGroupType()
                : EntityGroupHander.getEntityGroupType(entity);
    }

    @Nullable
    public static Team getEntityTeam(Level world, Entity entity){
        if(entity instanceof Player) {
            return entity.getTeam();
        }
        if(entity instanceof IHasOwner owner && owner.getOwnerUUID().isPresent()) {
            Player player = world.getPlayerByUUID(owner.getOwnerUUID().get());
            return player == null ? null : player.getTeam();
        }
        return entity.getTeam();
    }

    public static boolean isEntityCharmed(Entity entity){
        if(entity instanceof ICanBeCharmed charmed) {
            return charmed.isCharmed();
        }
        return false;
    }

    public static List<LivingEntity> getTargetableLivings(@NonNull Entity attacker, AABB aabb){
        return getPredicateEntities(attacker, aabb, LivingEntity.class, target -> canTargetEntity(attacker, target));
    }

    /** 同 {@link #getTargetableLivings}，但无视迷雾隐怪（范围伤害用）。 */
    public static List<LivingEntity> getTargetableLivingsIgnoreFog(@NonNull Entity attacker, AABB aabb){
        return getPredicateEntities(attacker, aabb, LivingEntity.class,
                target -> checkCanEntityBeTarget(attacker, target, true));
    }

    public static List<LivingEntity> getViewableTargetableEntity(@NonNull Entity attacker, AABB aabb){
        return getTargetableLivings(attacker, aabb).stream()
                .filter(target -> canSeeEntity(attacker, target)).collect(Collectors.toList());
    }

    /** 同 {@link #getViewableTargetableEntity}，但无视迷雾隐怪（范围伤害用）。 */
    public static List<LivingEntity> getViewableTargetableEntityIgnoreFog(@NonNull Entity attacker, AABB aabb){
        return getTargetableLivingsIgnoreFog(attacker, aabb).stream()
                .filter(target -> canSeeEntity(attacker, target)).collect(Collectors.toList());
    }

    public static List<Entity> getTargetableEntities(@NonNull Entity attacker, AABB aabb){
        return getPredicateEntities(attacker, aabb, Entity.class, target -> canTargetEntity(attacker, target));
    }

    /** 同 {@link #getTargetableEntities}，但无视迷雾隐怪（范围伤害用）。 */
    public static List<Entity> getTargetableEntitiesIgnoreFog(@NonNull Entity attacker, AABB aabb){
        return getPredicateEntities(attacker, aabb, Entity.class,
                target -> checkCanEntityBeTarget(attacker, target, true));
    }

    public static List<LivingEntity> getFriendlyLivings(@NonNull Entity attacker, AABB aabb){
        return getPredicateEntities(attacker, aabb, LivingEntity.class, target -> isFriendly(attacker, target));
    }

    private static List<Entity> getTargetableEntitiesIngoreCheck(@NonNull Entity attacker, AABB aabb){
        return getPredicateEntities(attacker, aabb, Entity.class, target -> checkCanEntityBeTarget(attacker, target));
    }

    public static <T extends Entity> List<T> getPredicateEntities(@NonNull Entity attacker, AABB aabb,
                                                                  Class<T> tClass, Predicate<T> predicate){
        if(attacker == null) return new ArrayList<>();
        return attacker.level().getEntitiesOfClass(tClass, aabb).stream()
                .filter(target -> ! attacker.equals(target) && predicate.test(target))
                .collect(Collectors.toList());
    }

    public static List<Entity> getWholeTargetableEntities(@NonNull Entity attacker, AABB aabb) {
        List<Entity> list = new ArrayList<>();
        if (attacker == null) return list;

        final List<Entity> targets = getTargetableEntitiesIngoreCheck(attacker, aabb);
        final IntOpenHashSet seen = new IntOpenHashSet();

        for (Entity target : targets) {
            if (target instanceof MultiPartEntity) continue;
            if (!seen.add(target.getId())) continue;
            list.add(target);
        }
        for (Entity target : targets) {
            if (!(target instanceof MultiPartEntity part)) continue;

            final LivingEntity owner = part.getOwner();
            if (owner == null) {
                if (!seen.add(target.getId())) continue;
                list.add(target);
                continue;
            }

            if (!seen.add(owner.getId())) continue;
            list.add(owner);
        }

        return list;
    }


    /** 同 {@link #getWholeTargetableEntities}，但无视迷雾隐怪（范围伤害用）。 */
    public static List<Entity> getWholeTargetableEntitiesIgnoreFog(@NonNull Entity attacker, AABB aabb) {
        List<Entity> list = new ArrayList<>();
        if (attacker == null) return list;

        final List<Entity> targets = getPredicateEntities(attacker, aabb, Entity.class,
                target -> checkCanEntityBeTarget(attacker, target, true));
        final IntOpenHashSet seen = new IntOpenHashSet();

        for (Entity target : targets) {
            if (target instanceof MultiPartEntity) continue;
            if (!seen.add(target.getId())) continue;
            list.add(target);
        }
        for (Entity target : targets) {
            if (!(target instanceof MultiPartEntity part)) continue;

            final LivingEntity owner = part.getOwner();
            if (owner == null) {
                if (!seen.add(target.getId())) continue;
                list.add(target);
                continue;
            }

            if (!seen.add(owner.getId())) continue;
            list.add(owner);
        }

        return list;
    }

    public static void addPotionEffect(Entity entity, MobEffectInstance effect) {
        if (entity instanceof MultiPartEntity part) {
            addPotionEffect(part.getOwner(), effect);
        } else if (entity instanceof ZombieEntity zombie) {
            zombie.checkAndAddPotionEffect(effect);
        } else if (entity instanceof PlantEntity plant) {
            plant.checkAndAddPotionEffect(effect);
        } else if (entity instanceof LivingEntity living) {
            living.addEffect(effect);
        }

    }

    public static boolean isEntityCold(LivingEntity entity) {
        return hasSpeedModifier(entity, PvZEffectRegister.COLD_EFFECT.getId());
    }

    public static boolean isEntityFrozen(LivingEntity entity) {
        return hasSpeedModifier(entity, PvZEffectRegister.FROZEN_EFFECT.getId());
    }

    public static boolean isEntityButter(LivingEntity entity) {
        return hasSpeedModifier(entity, PvZEffectRegister.BUTTER_EFFECT.getId());
    }


    private static boolean hasSpeedModifier(LivingEntity entity, Identifier modifierId) {
        AttributeInstance attr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        return attr != null && attr.getModifier(modifierId) != null;
    }

    public static boolean isEntityInSky(Entity entity) {
        if(entity instanceof Bat) {
            return true;
        }
        if(entity instanceof BalloonZombieEntity balloon && balloon.hasBalloon()) {
            return true;
        }
        return ! entity.onGround() && ! entity.isInWater() && ! entity.isInLava();
    }

    public static boolean hasNearBy(Level world, BlockPos pos, double r, Predicate<Entity> pre) {
        return world.getEntitiesOfClass(Entity.class, BlockUtil.getAABB(pos, r, r)).stream()
                .anyMatch(pre);
    }


    public static AABB getEntityAABB(Entity entity, double w, double h){
        return BlockUtil.getAABB(entity.blockPosition(), w, h);
    }

    public static boolean isOpEntity(Entity entity){
        if(entity instanceof Player player && ! PlayerUtil.isPlayerSurvival(player)){
            return true;
        }
        return entity.isInvulnerable();
    }

    public static boolean isEnemy(Entity a, Entity b){
        return (a == null || b == null) ? false : EntityGroupHander.checkCanTarget(getEntityGroup(a), getEntityGroup(b));
    }


    public static float getCurrentDefenceHealth(LivingEntity entity) {
        float health = 0;
        AttributeInstance inner = entity.getAttribute(PvZAttributes.INNER_DEFENCE_HP);
        AttributeInstance outer = entity.getAttribute(PvZAttributes.OUTER_DEFENCE_HP);

        if (inner != null) {
            health = (float) (health + inner.getValue());
        }
        if (outer != null) {
            health = (float) (health + outer.getValue());
        }
        return health;
    }
}
