package com.lying.pvz.common.level.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * 读 {@link PvZSpawnEggItem} 里存的两个颜色，按 foreground 区分底色/高光。
 * <p>
 * 对应 item definition 里的 tint 项：
 * {"type": "pvz:spawn_egg_color", "foreground": false}
 */
public record PvZEggTint(boolean foreground) implements ItemTintSource {

    public static final MapCodec<PvZEggTint> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.BOOL.optionalFieldOf("foreground", false)
                    .forGetter(PvZEggTint::foreground)
    ).apply(inst, PvZEggTint::new));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        if (stack.getItem() instanceof PvZSpawnEggItem egg) {
            return egg.getColor(foreground ? 1 : 0);
        }
        return 0xFFFFFF;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}