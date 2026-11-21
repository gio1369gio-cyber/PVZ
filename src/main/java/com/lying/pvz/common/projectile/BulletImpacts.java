package com.lying.pvz.common.projectile;

import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.api.projectile.BulletImpact;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.IBullet;
import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.level.entity.MultiPartEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.IntUnaryOperator;

/**
 * 内置命中步骤。
 *
 * <p>默认组合（按 Kind 的 impact 列表顺序）：
 * <pre>
 * damage()          → 伤害（读旧标记）
 * effectFromVariant() → 变体效果 + 本发 ShotSpec 效果
 * </pre>
 * 标记 / 溅射 / 爆炸 / 落地生成 在后续阶段追加。
 */
public final class BulletImpacts {

    private BulletImpacts() {
    }

    /**
     * 伤害步骤。基础伤害在命中时计算：
     * {@code (baseDamage + damageAdd) * damageMultiplier * variantMultiplier}，
     * 再依次经过所有 {@code DamageTerm}。
     */
    public static BulletImpact.Factory damage() {
        return (owner, variant, kind, spec) -> {
            final List<BulletImpact.DamageTerm> terms = bindTerms(owner, variant, kind, spec);
            return (bullet, hit) -> {
                if (hit.target() != null) {
                    dealDamage(bullet, kind, hit, hit.target(), terms, 1.0F);
                }
                return BulletImpact.Result.CONTINUE;
            };
        };
    }

    /**
     * 溅射：对半径内除直接命中目标外的实体造成 {@code ratio} 倍伤害，
     * 并施加变体效果 / {@link ShotSpec#splashEffects()}。
     */
    public static BulletImpact.Factory splash(double radius, float ratio) {
        return (owner, variant, kind, spec) -> {
            final List<BulletImpact.DamageTerm> terms = bindTerms(owner, variant, kind, spec);
            final List<MobEffectInstance> effects = new ArrayList<>();
            for (BulletKind.EffectSpec effect : kind.variant(variant).effects()) {
                effects.add(effect.create());
            }
            for (MobEffectInstance effect : spec.splashEffects()) {
                effects.add(new MobEffectInstance(effect));
            }

            return (bullet, hit) -> {
                if (!(bullet.level() instanceof ServerLevel)) {
                    return BulletImpact.Result.CONTINUE;
                }
                final net.minecraft.world.phys.AABB area =
                        new net.minecraft.world.phys.AABB(hit.pos(), hit.pos()).inflate(radius);
                for (LivingEntity target : bullet.level()
                        .getEntitiesOfClass(LivingEntity.class, area)) {
                    if (target == hit.target() || !SweptHitResolver.canHit(bullet, target)) {
                        continue;
                    }
                    dealDamage(bullet, kind, hit, target, terms, ratio);
                    if (!effects.isEmpty()) {
                        final Entity source = bullet.ownerEntity();
                        for (MobEffectInstance effect : effects) {
                            target.addEffect(new MobEffectInstance(effect), source);
                        }
                    }
                }
                return BulletImpact.Result.CONTINUE;
            };
        };
    }

    /**
     * 爆炸：半径内所有可命中目标受满额伤害（含直接命中目标），
     * 掉落 {@link ShotSpec#drops()}，播放音效，并销毁弹药。
     *
     * <p>玉米炮使用本步骤替代普通 {@link #damage()}，避免直接命中的目标被结算两次。
     */
    public static BulletImpact.Factory explode(double radius, @Nullable SoundEvent sound) {
        return explode(radius, sound, 0);
    }

    /**
     * @param particleCount {@code >0} 时额外广播一次粒子（鞭炮的 5 号粒子）
     */
    public static BulletImpact.Factory explode(double radius, @Nullable SoundEvent sound,
                                               int particleCount) {
        return (owner, variant, kind, spec) -> {
            final List<BulletImpact.DamageTerm> terms = bindTerms(owner, variant, kind, spec);
            final List<ItemStack> drops = new ArrayList<>();
            for (ItemStack drop : spec.drops()) {
                drops.add(drop.copy());
            }

            return (bullet, hit) -> {
                if (!(bullet.level() instanceof ServerLevel serverLevel)) {
                    return BulletImpact.Result.CONTINUE;
                }
                final net.minecraft.world.phys.AABB area =
                        new net.minecraft.world.phys.AABB(hit.pos(), hit.pos()).inflate(radius);
                for (LivingEntity target : bullet.level()
                        .getEntitiesOfClass(LivingEntity.class, area)) {
                    if (!SweptHitResolver.canHit(bullet, target)) {
                        continue;
                    }
                    dealDamage(bullet, kind, hit, target, terms, 1.0F);
                }

                for (ItemStack drop : drops) {
                    final ItemEntity item = new ItemEntity(serverLevel,
                            hit.pos().x, hit.pos().y + 1.0D, hit.pos().z, drop.copy());
                    item.setDefaultPickUpDelay();
                    serverLevel.addFreshEntity(item);
                }
                if (sound != null) {
                    EntityUtil.playSound(bullet.self(), sound);
                }
                if (particleCount > 0) {
                    EntityUtil.spawnParticle(bullet.self(), particleCount);
                }
                return BulletImpact.Result.CONSUME;
            };
        };
    }

    /**
     * 命中方块时生成植物（土豆雷 / 坚果墙）。
     *
     * <p>从发射者 {@link PlantEntity} 复制技能与卡牌附魔，保持落地植物的卡牌身份。
     *
     * @param yOffset     生成位置相对子弹的 Y 偏移
     * @param oneInChance 1 表示必定生成；N>1 表示 1/N 概率
     */
    public static BulletImpact.Factory spawnPlantOnBlock(EntityType<? extends PlantEntity> type,
                                                         double yOffset, int oneInChance) {
        return (owner, variant, kind, spec) -> (bullet, hit) -> {
            if (hit.blockState() == null) {
                return BulletImpact.Result.CONTINUE;
            }
            if (oneInChance > 1 && bullet.self().getRandom().nextInt(oneInChance) != 0) {
                return BulletImpact.Result.CONTINUE;
            }
            if (!(bullet.level() instanceof ServerLevel serverLevel)) {
                return BulletImpact.Result.CONTINUE;
            }

            final PlantEntity plant = type.create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
            if (plant == null) {
                return BulletImpact.Result.CONTINUE;
            }
            final Entity source = bullet.ownerEntity();
            if (source instanceof PlantEntity from) {
                plant.applyCardData(from.getSkillLevels(), from.getCardEnchantments());
                from.getOwnerUUID().ifPresent(plant::setOwnerUUID);
            }
            plant.setPos(bullet.position().x, bullet.position().y + yOffset, bullet.position().z);
            serverLevel.addFreshEntity(plant);
            return BulletImpact.Result.CONTINUE;
        };
    }

    /**
     * 命中时按变体生成 {@code count} 个指定粒子（西瓜碎片 / 玉米炮爆米花等）。
     *
     * @param typeByVariant 变体 id → 粒子类型（{@link EntityUtil#spawnParticle} 的类型码）；
     *                      返回负值表示该变体不生成
     */
    public static BulletImpact.Factory spawnParticle(IntUnaryOperator typeByVariant, int count) {
        return (owner, variant, kind, spec) -> {
            final int type = typeByVariant.applyAsInt(variant);
            if (type < 0) {
                return (bullet, hit) -> BulletImpact.Result.CONTINUE;
            }
            return (bullet, hit) -> {
                for (int i = 0; i < count; i++) {
                    EntityUtil.spawnParticle(bullet.self(), type);
                }
                return BulletImpact.Result.CONTINUE;
            };
        };
    }

    /**
     * 命中时生成 {@code count} 个固定类型粒子。
     */
    public static BulletImpact.Factory spawnParticle(int type, int count) {
        return spawnParticle(v -> type, count);
    }

    /**
     * 按 {@link ShotSpec#knockback()} 给命中目标施加击退。
     */
    public static BulletImpact.Factory knockback() {
        return (owner, variant, kind, spec) -> {
            final int level = spec.knockback();
            if (level <= 0) {
                return (bullet, hit) -> BulletImpact.Result.CONTINUE;
            }
            return (bullet, hit) -> {
                if (hit.target() instanceof LivingEntity living) {
                    final Vec3 speed = living.getDeltaMovement();
                    final Vec3 now = bullet.velocity();
                    living.setDeltaMovement(speed.add(now).multiply(level, level, level));
                }
                return BulletImpact.Result.CONTINUE;
            };
        };
    }

    /**
     * 命中播放音效并销毁（磁力弹的金属撞击声）。
     */
    public static BulletImpact.Factory sound(@Nullable SoundEvent sound) {
        return (owner, variant, kind, spec) -> (bullet, hit) -> {
            if (sound != null) {
                EntityUtil.playSound(bullet.self(), sound);
            }
            return BulletImpact.Result.CONSUME;
        };
    }

    /**
     * 把变体从 {@code from} 换到 {@code to}（荆棘 GUIDE → NORMAL）。
     */
    public static BulletImpact.Factory variantSwap(int from, int to) {
        return (owner, variant, kind, spec) -> (bullet, hit) -> {
            if (bullet.variant() == from) {
                bullet.setVariant(to);
            }
            return BulletImpact.Result.CONTINUE;
        };
    }

    private static List<BulletImpact.DamageTerm> bindTerms(@Nullable UUID owner, int variant,
                                                           BulletKind kind, ShotSpec spec) {
        final List<BulletImpact.DamageTerm> terms = new ArrayList<>();
        for (BulletImpact.DamageTermFactory factory : kind.damageTerms()) {
            terms.add(factory.bind(owner, variant, kind, spec));
        }
        return terms;
    }

    private static boolean dealDamage(IBullet bullet, BulletKind kind,
                                      BulletImpact.Hit hit, Entity target,
                                      List<BulletImpact.DamageTerm> terms, float multiplier) {
        if (!(bullet.level() instanceof ServerLevel serverLevel)) {
            return false;
        }
        final BulletImpact.Hit context = target == hit.target()
                ? hit
                : new BulletImpact.Hit(target, hit.pos(), null, null, false);

        float damage = (bullet.baseDamage() + bullet.damageAdd())
                * bullet.damageMultiplier()
                * kind.variant(bullet.variant()).damageMultiplier()
                * multiplier;
        for (BulletImpact.DamageTerm term : terms) {
            damage = term.apply(damage, bullet, context);
        }
        if (damage <= 0.0F) {
            return false;
        }

        final DamageSource source = kind.variant(bullet.variant()).damage()
                .create(bullet, target);

        // 命中防具部件（MultiPartEntity，非 LivingEntity）：直接调用部件 hurtServer，
        // 由 PVZHealthPartEntity 标记 defended 并转发到宿主，走外层防御吸收分支。
        if (!(target instanceof LivingEntity living)) {
            return target.hurtServer(serverLevel, source, damage);
        }

        living.setInvulnerableTime(0);
        living.damageCooldownTime = 0;
        return living.hurtServer(serverLevel, source, damage);
    }

    /**
     * 把命中目标解析为可施加效果 / 标记的宿主实体。
     * 命中防具部件（{@link MultiPartEntity}）时返回其宿主，否则原样返回。
     */
    @Nullable
    private static LivingEntity resolveLivingTarget(@Nullable Entity target) {
        if (target instanceof MultiPartEntity part) {
            return part.getOwner();
        }
        return target instanceof LivingEntity living ? living : null;
    }

    /**
     * 施加变体自带效果，再追加本发 {@link ShotSpec#hitEffects()}。
     * 每个目标复制一份实例，避免共享可变状态。
     */
    public static BulletImpact.Factory effectFromVariant() {
        return (owner, variant, kind, spec) -> {
            final List<MobEffectInstance> effects = new ArrayList<>();
            for (BulletKind.EffectSpec effect : kind.variant(variant).effects()) {
                effects.add(effect.create());
            }
            for (MobEffectInstance effect : spec.hitEffects()) {
                effects.add(new MobEffectInstance(effect));
            }
            if (effects.isEmpty()) {
                return (bullet, hit) -> BulletImpact.Result.CONTINUE;
            }
            return (bullet, hit) -> {
                LivingEntity living = resolveLivingTarget(hit.target());
                if (living != null) {
                    final Entity source = bullet.ownerEntity();
                    for (MobEffectInstance effect : effects) {
                        if (canApplyPotionEffect(living, effect)) {
                            living.addEffect(new MobEffectInstance(effect), source);
                        }
                    }
                }
                return BulletImpact.Result.CONTINUE;
            };
        };
    }

    /**
     * 目标能否被施加该效果。对免疫冰冻 / 黄油的僵尸（{@link IPAZEntity#canBeCold()} /
     * {@link IPAZEntity#canBeFrozen()} / {@link IPAZEntity#canBeButtered()} 返回 false）
     * 跳过对应效果，避免绕过免疫检查。
     */
    private static boolean canApplyPotionEffect(LivingEntity living, MobEffectInstance effect) {
        if (!(living instanceof IPAZEntity paz)) {
            return true;
        }
        if (effect.is(PvZEffectRegister.COLD_EFFECT) && !paz.canBeCold()) {
            return false;
        }
        if (effect.is(PvZEffectRegister.FROZEN_EFFECT) && !paz.canBeFrozen()) {
            return false;
        }
        if (effect.is(PvZEffectRegister.BUTTER_EFFECT) && !paz.canBeButtered()) {
            return false;
        }
        return true;
    }

    /**
     * 给命中目标叠标记。来源绑定为发射者 UUID，因此 A 的层 B 读不到。
     */
    public static BulletImpact.Factory mark(Identifier mark, int stacks) {
        return (owner, variant, kind, spec) -> {
            if (owner == null) {
                return (bullet, hit) -> BulletImpact.Result.CONTINUE;
            }
            final Mark.Key key = new Mark.Key(mark, owner);
            return (bullet, hit) -> {
                if (hit.target() instanceof Mark.Holder holder) {
                    holder.addMark(key, stacks);
                }
                return BulletImpact.Result.CONTINUE;
            };
        };
    }

    /**
     * 按目标身上的标记层数加伤。读取的是"本次命中之前"的层数；
     * 与 {@link #mark(Identifier, int)} 配合时，默认顺序为伤害 → 加层。
     */
    public static BulletImpact.DamageTermFactory markDamage(Identifier mark, float perStack) {
        return (owner, variant, kind, spec) -> {
            if (owner == null) {
                return (base, bullet, hit) -> base;
            }
            final Mark.Key key = new Mark.Key(mark, owner);
            return (base, bullet, hit) -> hit.target() instanceof Mark.Holder holder
                    ? base + perStack * holder.marks(key)
                    : base;
        };
    }

    /**
     * 重载存档弹药时重建步骤。
     *
     * <p>只恢复 Kind 能推导的部分（伤害、变体效果）；本发的额外效果
     * （{@code ShotSpec.hitEffects}）不持久化——弹药寿命很短，存档恰好
     * 落在飞行中的情况可接受。
     */
    public static List<BulletImpact.Step> rebuild(BulletKind kind, int variant,
                                                  @Nullable UUID owner) {
        final ShotSpec spec = new ShotSpec().variant(variant);
        final List<BulletImpact.Step> steps = new ArrayList<>();
        for (BulletImpact.Factory factory : kind.impact()) {
            steps.add(factory.bind(owner, variant, kind, spec));
        }
        return List.copyOf(steps);
    }
}
