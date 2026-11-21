package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.projectile.Mark;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PVZ 自定义 {@link EntityDataSerializer}。
 *
 * <p>26.1 移除了 vanilla 的 {@code OPTIONAL_UUID} 与 {@code COMPOUND_TAG}，
 * mod 必须通过 {@link NeoForgeRegistries#ENTITY_DATA_SERIALIZERS} 自行注册。
 *
 * <p>本类把 {@code SKILLS} 的 synched data 类型从 {@code CompoundTag} 直接升级为
 * {@link SkillLevels}——不需要 NBT 中转。
 */
public final class PVZEntityDataSerializers {

    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, PvZMod.MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Optional<UUID>>> OPTIONAL_UUID =
            SERIALIZERS.register("optional_uuid",
                    () -> EntityDataSerializer.forValueType(
                            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional)));

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<SkillLevels>> SKILL_LEVELS =
            SERIALIZERS.register("skill_levels",
                    () -> EntityDataSerializer.forValueType(SkillLevels.STREAM_CODEC));

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<Mark.View>>> MARK_VIEWS =
            SERIALIZERS.register("mark_views",
                    () -> EntityDataSerializer.forValueType(
                            Mark.View.STREAM_CODEC.apply(ByteBufCodecs.list())));

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<CompoundTag>> COMPOUND_TAG =
            SERIALIZERS.register("compound_tag",
                    () -> EntityDataSerializer.forValueType(ByteBufCodecs.COMPOUND_TAG));



    private PVZEntityDataSerializers() {
    }
}