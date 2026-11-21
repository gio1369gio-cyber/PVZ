package com.lying.pvz.utils.others;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WeightList<T> {

    private final List<T> itemList = new ArrayList<>();
    private final List<Integer> weightList = new ArrayList<>();
    private Optional<T> leftItem = Optional.empty();
    private int total = 0;

    public WeightList() {
    }

    public WeightList<T> addItem(T item, int w) {
        this.itemList.add(item);
        this.weightList.add(w);
        this.total += w;
        return this;
    }

    public WeightList<T> setLeftItem(T item) {
        this.leftItem = Optional.ofNullable(item);
        return this;
    }

    public WeightList<T> setTotal(int tot) {
        this.total = tot;
        return this;
    }

    public boolean isEmpty() {
        return this.itemList.isEmpty();
    }

    public int getLen() {
        return this.itemList.size();
    }

    public void clear() {
        this.itemList.clear();
        this.weightList.clear();
        this.total = 0;
    }

    public void addAll(WeightList<T> list) {
        for (int i = 0; i < list.getLen(); ++i) {
            this.addItem(list.getItem(i), list.getWeight(i));
        }
    }

    public List<T> getItemList() {
        return this.itemList;
    }

    public T getItem(int pos) {
        return this.itemList.get(pos);
    }

    private int getWeight(int pos) {
        return this.weightList.get(pos);
    }

    public Optional<T> getRandomItem(RandomSource rand) {
        final int pos = rand.nextInt(this.total);
        int now = 0;
        for (int i = 0; i < this.itemList.size(); ++i) {
            now += this.weightList.get(i);
            if (pos < now) {
                return Optional.of(this.itemList.get(i));
            }
        }
        return this.leftItem;
    }

    @SafeVarargs
    public static <W> WeightList<W> of(Pair<W, Integer>... pairs) {
        WeightList<W> list = new WeightList<>();
        for (Pair<W, Integer> p : pairs) {
            list.addItem(p.getFirst(), p.getSecond());
        }
        return list;
    }

    @SafeVarargs
    public static <W> WeightList<W> of(int tot, Pair<W, Integer>... pairs) {
        return of(pairs).setTotal(tot);
    }
}