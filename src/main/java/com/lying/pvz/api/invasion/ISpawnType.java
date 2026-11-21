package com.lying.pvz.api.invasion;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 生成条目配置。
 */
public interface ISpawnType {

    EntityType<?> getEntityType();

    CompoundTag getNbt();

    int getWeight();

    int getInvasionLevel();

    PlaceType getPlaceType();

    boolean checkPos(Level level, BlockPos pos);

    /**
     * 生成位置类型。
     *
     * <p><b>持久化警告</b>：本枚举通过 {@link #getSerializedName()} 序列化，
     * 字符串名即持久化标识。<b>已有名字不得更改</b>，新增只能追加。
     */
    enum PlaceType implements StringRepresentable {

        LAND("land"),
        WATER("water"),
        SNOW("snow"),
        SKY("sky"),
        SNOW_LAND("snow_land");

        public static final Codec<PlaceType> CODEC = StringRepresentable.fromEnum(PlaceType::values);

        private final String name;

        PlaceType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}