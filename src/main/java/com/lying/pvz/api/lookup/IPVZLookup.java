package com.lying.pvz.api.lookup;

import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.api.content.skill.ISkillType;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.Optional;

/**
 * PVZ 注册表只读查询视图。
 *
 * <p>所有方法在注册表可用（{@code RegisterEvent} 之后）时返回有效结果。
 *
 * <p><b>返回值语义</b>：集合返回值为<b>不可变快照</b>——每次调用返回一份新集合，
 * 调用方修改返回值不影响注册表内部状态。快照在性能上可接受：注册表条目数
 * 在百量级，且查询通常发生在初始化阶段。
 *
 * <p><b>实现提示</b>：实现类每次调用从 {@code BuiltInRegistries.REGISTRY}
 * 按 {@code PVZRegistries} 的 key 惰性获取 {@code Registry}，返回
 * {@code List.copyOf(...)} 之类的新集合，不维护静态缓存。
 */
public interface IPVZLookup {

    /* ---- PAZ 相关 ---- */

    Optional<IPAZType> paz(Identifier id);

    Optional<IPlantType> plant(Identifier id);

    Optional<IZombieType> zombie(Identifier id);

    Collection<IPAZType> pazs();

    Collection<IPlantType> plants();

    Collection<IZombieType> zombies();

    /* ---- 技能 / 冷却 / 等级 / 精华 ---- */

    Optional<ISkillType> skill(Identifier id);

    Optional<ICoolDown> coolDown(Identifier id);

    Optional<IRankType> rank(Identifier id);

    Optional<IEssenceType> essence(Identifier id);

    Collection<ISkillType> skills();

    Collection<ICoolDown> coolDowns();

    Collection<IRankType> ranks();

    Collection<IEssenceType> essences();
}