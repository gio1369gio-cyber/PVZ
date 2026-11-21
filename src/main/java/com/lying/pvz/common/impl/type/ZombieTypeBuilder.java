package com.lying.pvz.common.impl.type;


import com.lying.pvz.api.content.paz.IZombieType;
import net.minecraft.resources.Identifier;

/**
 * {@link IZombieType} 的构建器。
 *
 * <p>僵尸类型没有额外的专属字段；本类只是 {@link AbstractPAZTypeBuilder} 的公开入口。
 *
 * <p><b>示例</b>：
 * <pre>{@code
 * public static final DeferredHolder<IZombieType, IZombieType> NORMAL_ZOMBIE =
 *     ZOMBIES.register("normal_zombie", () -> ZombieTypeBuilder
 *         .of("pvz", "normal_zombie")
 *         .rank(RankTypes.WHITE)
 *         .essence(EssenceTypes.ORIGIN)
 *         .coolDown(CoolDowns.NORMAL)
 *         .group("grass", 100)
 *         .xp(5)
 *         .entityType(EntityRegister.NORMAL_ZOMBIE)
 *         .loot(PvZLoot.NORMAL_ZOMBIE)
 *         .skill(SkillTypes.TOUGH_BODY, SkillTypes.FAST_CD, SkillTypes.HIGH_EAT_DAMAGE)
 *         .build());
 * }</pre>
 */
public final class ZombieTypeBuilder extends AbstractPAZTypeBuilder<ZombieTypeBuilder> {

    private ZombieTypeBuilder(Identifier id) {
        super(id);
    }

    public static ZombieTypeBuilder of(Identifier id) {
        return new ZombieTypeBuilder(id);
    }

    public static ZombieTypeBuilder of(String namespace, String path) {
        return new ZombieTypeBuilder(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public IZombieType build() {
        this.validateCommon();
        return new ZombieTypeImpl(this);
    }

    private static final class ZombieTypeImpl extends PazInfoBase implements IZombieType {

        ZombieTypeImpl(ZombieTypeBuilder b) {
            super(b);
        }
    }
}