package com.lying.pvz.common.attachment.player;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * PAZ 冷却账本。
 *
 * <p>权威源是 vanilla {@code player.getCooldowns()}；本 attachment 作为"按 PAZ 类型"的统一账本，
 * 在登出时保存（含 {@code bar} 比例），登录时恢复。
 *
 * <p>不参与网络同步：客户端 UI 直接读 vanilla {@code ItemCooldowns}。
 */
public final class PazCooldown implements ValueIOSerializable {

    private static final Codec<Map<Identifier, Integer>> CD_CODEC = Codec.unboundedMap(
            Codec.STRING.xmap(Identifier::parse, Identifier::toString), Codec.INT);
    private static final Codec<Map<Identifier, Float>> BAR_CODEC = Codec.unboundedMap(
            Codec.STRING.xmap(Identifier::parse, Identifier::toString), Codec.FLOAT);

    private final Map<Identifier, Integer> cd = new HashMap<>();
    private final Map<Identifier, Float> bar = new HashMap<>();

    public int getCD(Identifier id) {
        return cd.getOrDefault(id, 0);
    }

    public void setCD(Identifier id, int v) {
        if (v <= 0) {
            cd.remove(id);
        } else {
            cd.put(id, v);
        }
    }

    public float getBar(Identifier id) {
        return bar.getOrDefault(id, 1F);
    }

    public void setBar(Identifier id, float v) {
        bar.put(id, v);
    }

    public Map<Identifier, Integer> allCDs() {
        return Collections.unmodifiableMap(this.cd);
    }

    public void clear() {
        cd.clear();
        bar.clear();
    }

    /* ---------------- 序列化 ---------------- */

    @Override
    public void serialize(ValueOutput out) {
        out.store("cd", CD_CODEC, cd);
        out.store("bar", BAR_CODEC, bar);
    }

    @Override
    public void deserialize(ValueInput in) {
        cd.clear();
        bar.clear();
        in.read("cd", CD_CODEC).ifPresent(cd::putAll);
        in.read("bar", BAR_CODEC).ifPresent(bar::putAll);
    }
}