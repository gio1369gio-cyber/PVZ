package com.lying.pvz.common.damage;

import com.lying.pvz.common.level.entity.plant.assist.FodderBushEntity;
import com.lying.pvz.common.level.entity.plant.electric.MagnifyingGrassEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PvZEntityDamageSource extends DamageSource {

    private final List<MobEffectInstance> effects = new ArrayList<>();
    private final String msgId;
    private boolean isAppease = false;
    private boolean isParabola = false;
    private boolean isIceDamage = false;
    private boolean isFlameDamage = false;
    private boolean isExplosionDamage = false;
    private boolean isEatDamage = false;
    private boolean isCrushDamage = false;
    private boolean isThroughDamage = false;
    private boolean isThornDamage = false;
    private boolean isDefended = false;
    private boolean mustHurt = false;
    private int damageCount = 0;

    // 懒加载单例：DCL + volatile 保证多线程下唯一
    private static volatile PvZEntityDamageSource plantWilt;
    private static volatile PvZEntityDamageSource chomperPlant;

    public PvZEntityDamageSource(String name) {
        this(name, null, null);
    }

    public PvZEntityDamageSource(String name, @Nullable Entity attacker) {
        this(name, attacker, attacker);
    }

    public PvZEntityDamageSource(String name, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        super(resolveGenericType(directEntity, causingEntity),
                directEntity, causingEntity, null);
        this.msgId = name;
    }

    // ==================== 单例 ====================

    public static PvZEntityDamageSource plantWilt() {
        PvZEntityDamageSource local = plantWilt;
        if (local == null) {
            synchronized (PvZEntityDamageSource.class) {
                local = plantWilt;
                if (local == null) {
                    local = new PvZEntityDamageSource("plant_wilt");
                    plantWilt = local;
                }
            }
        }
        return local;
    }

    /**
     * “此伤害源无 source entity，对僵尸使用会被 PVZZombieEntity.isZombieInvulnerableTo 免疫；
     * 对僵尸请用 eat(Entity)”
     */
    public static PvZEntityDamageSource chomperPlant() {
        PvZEntityDamageSource local = chomperPlant;
        if (local == null) {
            synchronized (PvZEntityDamageSource.class) {
                local = chomperPlant;
                if (local == null) {
                    local = new PvZEntityDamageSource("chomper_plant").setEatDamage();
                    chomperPlant = local;
                }
            }
        }
        return local;
    }

    // ==================== Holder 解析 ====================

    /**
     * 解析 {@code pvz:generic} 的 {@link Holder.Reference}。
     *
     * <p>有实体 → 用 {@code entity.level().registryAccess()}（服务端 / 客户端各自 registry 正确）。
     * <p>无实体 → 回退到 {@link PvZDamageTypes#genericHolder()}（需先在 level 加载事件里 bind）。
     */
    private static Holder<DamageType> resolveGenericType(@Nullable Entity directEntity,
                                                         @Nullable Entity causingEntity) {
        Entity any = directEntity != null ? directEntity : causingEntity;
        if (any != null && !any.isRemoved()) {
            // 1.21.x: RegistryAccess 使用 lookupOrThrow，Registry 使用 getOrThrow
            return any.level().registryAccess()
                    .lookupOrThrow(Registries.DAMAGE_TYPE)
                    .getOrThrow(PvZDamageTypes.PVZ_GENERIC);
        }
        try {
            return PvZDamageTypes.genericHolder();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "[PVZ] 构造无实体 PvZEntityDamageSource 时 PvZDamageTypes 尚未 bind。"
                            + "请确保 PvZDamageTypes.bind(..., false) 先于任何 "
                            + "plantWilt() / chomperPlant() / 静态初始化调用。", e);
        }
    }

    // ==================== 工厂方法 ====================

    public static PvZEntityDamageSource normal(Entity projectile, Entity attacker) {
        return new PvZEntityDamageSource("normal", projectile, attacker);
    }
    public static PvZEntityDamageSource normal(Entity attacker) {
        return normal(attacker, attacker);
    }

    public static PvZEntityDamageSource eat(Entity projectile, Entity attacker) {
        return new PvZEntityDamageSource("eat", projectile, attacker).setEatDamage();
    }
    public static PvZEntityDamageSource eat(Entity attacker) {
        return eat(attacker, attacker);
    }

    public static PvZEntityDamageSource explode(Entity projectile, Entity shooter) {
        return new PvZEntityDamageSource("explosion", projectile, shooter)
                .setMustHurt().setExplosion();
    }
    public static PvZEntityDamageSource explode(Entity attacker) {
        return explode(attacker, attacker);
    }

    public static PvZEntityDamageSource causeDeadlyDamage(Entity projectile, Entity shooter) {
        return new PvZEntityDamageSource("deadly", projectile, shooter).setMustHurt();
    }
    public static PvZEntityDamageSource causeDeadlyDamage(Entity attacker) {
        return causeDeadlyDamage(attacker, attacker);
    }

    public static PvZEntityDamageSource causeCrushDamage(Entity attacker) {
        return new PvZEntityDamageSource("crush", attacker).setCrushDamage();
    }

    public static PvZEntityDamageSource causeThornDamage(Entity projectile, Entity shooter) {
        return new PvZEntityDamageSource("thorn", projectile, shooter).setThornDamage().setThroughDamage();
    }
    public static PvZEntityDamageSource causeThornDamage(Entity attacker) {
        return causeThornDamage(attacker, attacker);
    }

    public static PvZEntityDamageSource causeIceDamage(Entity projectile, Entity shooter) {
        return new PvZEntityDamageSource("ice", projectile, shooter).setIceDamage();
    }
    public static PvZEntityDamageSource causeIceDamage(Entity attacker) {
        return causeIceDamage(attacker, attacker);
    }

    public static PvZEntityDamageSource causeFlameDamage(Entity projectile, Entity shooter) {
        return new PvZEntityDamageSource("flame", projectile, shooter).setFlameDamage();
    }
    public static PvZEntityDamageSource causeFlameDamage(Entity attacker) {
        return causeFlameDamage(attacker, attacker);
    }
    public static PvZEntityDamageSource magnifyingGrass(MagnifyingGrassEntity entity, Entity shooter) {
        return new PvZEntityDamageSource("magnifying_grass", entity, shooter);
    }
    public static PvZEntityDamageSource bush(FodderBushEntity bush, Entity shooter) {
        return new PvZEntityDamageSource("bush", bush, shooter).setEatDamage().setThroughDamage();
    }
    // ==================== 判定 ====================

    /**
     * 判断是否为“强制伤害”（无视僵尸/植物常规免疫与减免）。
     *
     * <p>修正点：除了 appease / parabola / flame / explosion 外，
     * eat / crush / mustHurt 也是语义上的强制伤害，应一并排除。
     * through（穿透）与 thorn（反伤）是否“强制”由调用方决定，此处保持原有语义不排除。
     */
    public static boolean isEnforceDamage(DamageSource source) {
        if (source instanceof PvZEntityDamageSource pds) {
            return !pds.isAppease() && !pds.isParabola()
                    && !pds.isFlameDamage() && !pds.isExplosionDamage()
                    && !pds.isEatDamage() && !pds.isCrushDamage()
                    && !pds.isMustHurt();
        }
        return !source.is(DamageTypeTags.IS_PROJECTILE)
                && !source.is(DamageTypeTags.WITCH_RESISTANT_TO)
                && !source.is(DamageTypeTags.IS_FIRE)
                && !source.is(DamageTypeTags.IS_EXPLOSION);
    }

    /**
     * 父类 isDirect() 为 causingEntity == directEntity。
     * 对无实体来源（plantWilt）父子皆为 null，父类会误判为 true，此处修正。
     */
    @Override
    public boolean isDirect() {
        return this.getDirectEntity() != null
                && this.getDirectEntity() == this.getEntity();
    }

    // ==================== 死亡消息 ====================

    /**
     * 与原版 DamageSource 一致的参数补全：
     * <ul>
     *   <li>无实体 + 有 killCredit → {@code death.attack.pvz.<id>.player}(victim, credit)</li>
     *   <li>有实体 + 手持自定义名物品 → {@code death.attack.pvz.<id>.item}(victim, name, item)</li>
     *   <li>其它 → {@code death.attack.pvz.<id>}(victim[, name])</li>
     * </ul>
     */
    @Override
    public Component getLocalizedDeathMessage(LivingEntity victim) {
        String key = "death.attack.pvz." + this.msgId;
        Entity causing = this.getEntity();
        Entity direct = this.getDirectEntity();

        if (causing == null && direct == null) {
            LivingEntity credit = victim.getKillCredit();
            return credit != null
                    ? Component.translatable(key + ".player",
                    victim.getDisplayName(), credit.getDisplayName())
                    : Component.translatable(key, victim.getDisplayName());
        }

        Component name = causing != null ? causing.getDisplayName() : direct.getDisplayName();
        ItemStack held = causing instanceof LivingEntity living
                ? living.getMainHandItem()
                : ItemStack.EMPTY;

        if (!held.isEmpty() && held.has(DataComponents.CUSTOM_NAME)) {
            return Component.translatable(key + ".item",
                    victim.getDisplayName(), name, held.getDisplayName());
        }
        return Component.translatable(key, victim.getDisplayName(), name);
    }

    @Override
    public String toString() {
        return "PvZEntityDamageSource (" + this.msgId + ")";
    }

    // ==================== 扩展字段 ====================

    public PvZEntityDamageSource setCount(int cnt) { this.damageCount = cnt; return this; }
    public int getDamageCount() { return this.damageCount; }

    public void addEffect(MobEffectInstance instance) {
        this.effects.add(Objects.requireNonNull(instance, "effect"));
    }
    public List<MobEffectInstance> getEffects() { return this.effects; }

    public void setDefended(boolean is) { this.isDefended = is; }
    public boolean isDefended() { return this.isDefended; }

    public boolean isAppease() { return isAppease; }
    public PvZEntityDamageSource setAppease() { this.isAppease = true; return this; }

    public boolean isParabola() { return isParabola; }
    public PvZEntityDamageSource setParabola() { this.isParabola = true; return this; }

    public boolean isIceDamage() { return isIceDamage; }
    public PvZEntityDamageSource setIceDamage() { this.isIceDamage = true; return this; }

    public boolean isFlameDamage() { return isFlameDamage; }
    public PvZEntityDamageSource setFlameDamage() { this.isFlameDamage = true; return this; }

    public boolean isExplosionDamage() { return isExplosionDamage; }
    public PvZEntityDamageSource setExplosion() { this.isExplosionDamage = true; return this; }

    public boolean isEatDamage() { return isEatDamage; }
    public PvZEntityDamageSource setEatDamage() { this.isEatDamage = true; return this; }

    public boolean isCrushDamage() { return isCrushDamage; }
    public PvZEntityDamageSource setCrushDamage() { this.isCrushDamage = true; return this; }

    public boolean isThroughDamage() { return isThroughDamage; }
    public PvZEntityDamageSource setThroughDamage() { this.isThroughDamage = true; return this; }

    public boolean isThornDamage() { return isThornDamage; }
    public PvZEntityDamageSource setThornDamage() { this.isThornDamage = true; return this; }

    public boolean isMustHurt() { return mustHurt; }
    public PvZEntityDamageSource setMustHurt() { this.mustHurt = true; return this; }

}