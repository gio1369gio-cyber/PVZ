package com.lying.pvz.api.entity.plant;

import com.lying.pvz.api.content.paz.IPlantType;
import net.minecraft.nbt.CompoundTag;

/**
 * 植物信息（内层 / 外层）。
 *
 * <p>每颗植物持有独立的内层信息与（可选）外层信息。
 * {@link IPlantType#getOuterPlantInfo()} 每次返回新实例，本接口即该新实例的类型。
 *
 * <p>NBT 序列化：{@link #read(CompoundTag)} / {@link #write(CompoundTag)} 由
 * 植物实体的 NBT 存储直接驱动，内部实现可用 Codec 编解码具体字段。
 */
public interface IPlantInfo {

    /**
     * 从 NBT 读取数据。
     */
    void read(CompoundTag nbt);

    /**
     * 写入数据到 NBT。
     */
    void write(CompoundTag nbt);

    /**
     * 放置到指定植物实体上。
     *
     * @param plant   宿主植物
     * @param sunCost 召唤此植物花费的阳光
     */
    void placeOn(IPlantEntity plant, int sunCost);

    /**
     * 内层植物进入超级模式时回调。
     */
    void onSuper(IPlantEntity plant);

    /**
     * 该植物信息是否已进入超级模式。
     *
     * <p>用于外层植物（南瓜壳）：外层先强化一次后，下一次 {@code makeSuperMode}
     * 应转而强化内层植物。实现类在 {@link #onSuper} 中调用 {@link #markSuper()}
     * 标记状态。
     */
    default boolean isSuper() {
        return false;
    }

    /**
     * 标记该植物信息已进入超级模式。
     */
    default void markSuper() {
    }

    /**
     * 被治疗时回调。
     *
     * @param percent 恢复比例（相对于最大生命）
     */
    void onHeal(IPlantEntity plant, float percent);

    IPlantType getType();

    void setType(IPlantType type);

    int getSunCost();

    void setSunCost(int cost);
}