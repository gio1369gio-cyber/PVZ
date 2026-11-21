package com.lying.pvz.common.transaction;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.api.transaction.TradeInjector;
import com.lying.pvz.api.transaction.TransactionType;
import com.lying.pvz.common.level.entity.npc.AbstractDaveEntity;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.ToIntFunction;

/**
 * Dave 的交易运行时。所有与"商品展示"相关的状态在此处维护；
 * {@link AbstractDaveEntity} 只负责 tick/交互/同步。
 */
public class TransactionManager {

    public static final int REFRESH_CD = 24000;

    /** 序列化：Integer 键转 String 键适配 NbtOps。 */
    private static final Codec<Map<Integer, GoodType>> GOODS_CODEC = Codec.unboundedMap(
            Codec.STRING.xmap(Integer::parseInt, String::valueOf),
            GoodType.CODEC);

    private final AbstractDaveEntity owner;
    @Nullable private TransactionType type;

    private final Map<Integer, GoodType> currentGoods = new LinkedHashMap<>();
    private int nextId = 0;
    private boolean neverRefreshed = true;
    private long nextRefreshAt = 0L;

    public TransactionManager(AbstractDaveEntity owner) {
        this.owner = owner;
    }

    /** 运行时切换交易类型：清空现有商品并重新刷新。 */
    public void changeType(@Nullable TransactionType t) {
        this.type = t;
        this.neverRefreshed = true;
        this.currentGoods.clear();
        this.nextId = 0;
    }

    /** 从存档恢复交易类型：不动刷新状态、不清空商品。 */
    public void restoreType(@Nullable TransactionType t) {
        this.type = t;
    }

    /** 清空当前商品（保持 type 与刷新计时）。 */
    public void clear() {
        this.currentGoods.clear();
        this.nextId = 0;
        sync();
    }

    @Nullable public TransactionType getType() { return this.type; }

    public void tick() {
        if (this.type == null) return;
        if (this.neverRefreshed || this.owner.level().getGameTime() >= this.nextRefreshAt) {
            refresh();
        }
    }

    public void refresh() {
        if (this.type == null) return;
        this.currentGoods.clear();
        this.nextId = 0;
        this.neverRefreshed = false;
        this.nextRefreshAt = this.owner.level().getGameTime() + REFRESH_CD;

        // 1. 必出商品
        for (GoodType g : this.type.goods()) {
            if (g.must()) addGood(g);
        }

        // 2. 按权重无放回抽样
        List<GoodType> pool = new ArrayList<>();
        for (GoodType g : this.type.goods()) if (!g.must()) pool.add(g);
        int count = Math.max(0, this.type.goodCount().roll(this.owner.getRandom()));
        WeightedSampler<GoodType> sampler = new WeightedSampler<>(pool, GoodType::weight);
        for (int i = 0; i < count; ++i) {
            GoodType picked = sampler.poll(this.owner.getRandom());
            if (picked == null) break;
            addGood(picked);
        }

        // 3. 注入器
        RandomSource rand = this.owner.getRandom();
        HolderLookup.Provider regs = this.owner.registryAccess();
        for (Identifier id : this.type.injectors()) {
            TradeInjector inj = TransactionCodecs.getInjector(id.toString());
            if (inj == null) {
                PvZMod.LOGGER.warn("Unknown trade injector: {}", id);
                continue;
            }
            inj.inject(new TradeInjector.Context(this.owner, rand, regs, this::addGood));
        }

        sync();
    }

    private void addGood(GoodType g) { this.currentGoods.put(this.nextId++, g); }

    public void sell(int pos) {
        GoodType g = this.currentGoods.get(pos);
        if (g == null) return;
        int nl = Math.max(0, g.limit() - 1);
        this.currentGoods.put(pos, g.withLimit(nl));
        sync();
    }

    public Map<Integer, GoodType> currentGoods() {
        return Collections.unmodifiableMap(this.currentGoods);
    }

    /* ---------------- 持久化 ---------------- */

    /**
     * 把当前状态写回 {@link AbstractDaveEntity} 的同步数据。
     * <p>仅在服务端执行：客户端 {@code load()} 也会调用本方法，必须早退，
     * 否则 {@code setGoods → onSyncedDataUpdated → load → sync} 会递归。
     */
    private void sync() {
        if (this.owner.level().isClientSide()) return;

        RegistryOps<Tag> ops = this.owner.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        Tag tag = GOODS_CODEC.encodeStart(ops, this.currentGoods)
                .resultOrPartial(e -> PvZMod.LOGGER.error("Failed to encode goods: {}", e))
                .orElseGet(CompoundTag::new);
        if (tag instanceof CompoundTag ct) this.owner.setGoods(ct);
        this.owner.setNeverRefreshed(this.neverRefreshed);
        this.owner.setNextRefreshAt(this.nextRefreshAt);
    }

    public void load(CompoundTag goods, boolean neverRefreshed, long nextRefreshAt) {
        RegistryOps<Tag> ops = this.owner.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        GOODS_CODEC.parse(ops, goods)
                .resultOrPartial(e -> PvZMod.LOGGER.error("Failed to load goods: {}", e))
                .ifPresent(m -> { this.currentGoods.clear(); this.currentGoods.putAll(m); });
        this.neverRefreshed = neverRefreshed;
        this.nextRefreshAt = nextRefreshAt;

        // 关键修复：把恢复后的状态写回 entityData。
        // 服务端：保证实体进世界时发出的 ClientboundSetEntityDataPacket 带非空 GOODS。
        // 客户端：sync() 内部早退，不会递归。
        sync();
    }

    /* ---------------- 有放回 / 无放回权重抽样 ---------------- */

    private static final class WeightedSampler<T> {
        private final List<T> items;
        private final List<Integer> weights;
        private int total = 0;

        WeightedSampler(List<T> items, ToIntFunction<T> w) {
            this.items = new ArrayList<>(items);
            this.weights = new ArrayList<>(items.size());
            for (T t : items) {
                int v = Math.max(0, w.applyAsInt(t));
                this.weights.add(v);
                this.total += v;
            }
        }

        @Nullable T poll(RandomSource rand) {
            if (this.items.isEmpty() || this.total <= 0) return null;
            int roll = rand.nextInt(this.total);
            int acc = 0;
            for (int i = 0; i < this.items.size(); ++i) {
                acc += this.weights.get(i);
                if (roll < acc) {
                    T picked = this.items.remove(i);
                    this.total -= this.weights.remove(i);
                    return picked;
                }
            }
            return null;
        }
    }
}