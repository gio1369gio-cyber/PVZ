package com.lying.pvz.api.entity.ability;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * 金属覆层类型。
 *
 * <p>植物侧用于梯子（{@link #LADDER}）：僵尸可以爬梯越过植物而不触发碾压。
 * 僵尸侧用于门、桶等（{@link #SCREEN_DOOR} 等）：提供额外外层防御。
 *
 * <p><b>持久化警告</b>：本枚举的 {@code ordinal()} 被 NBT / 网络持久化。
 * <b>新值只能追加在末尾</b>，不能在中间插入或重排。
 */
public enum MetalTypes {
    EMPTY,
    LADDER,
    SCREEN_DOOR,
    BUCKET_HEAD,
    FOOTBALL_HELMET,
    GIGA_HELMET,
    JACK_BOX,
    IRON_PICKAXE,
    POGO;


    public static Item getMetalItem(MetalTypes type) {
        switch (type) {
            case BUCKET_HEAD: return PvZItemRegister.BUCKET_HEAD.get();
            case SCREEN_DOOR: return  PvZItemRegister.SCREEN_DOOR.get();
            case FOOTBALL_HELMET: return  PvZItemRegister.FOOTBALL_HELMET.get();
            case GIGA_HELMET: return  PvZItemRegister.GIGA_HELMET.get();
            case JACK_BOX: return  PvZItemRegister.JACK_BOX.get();
            case IRON_PICKAXE: return Items.IRON_PICKAXE;
            case LADDER: return  PvZBlockRegister.STEEL_LADDER.get().asItem();
            default:
                return Items.IRON_INGOT;
        }
    }
}