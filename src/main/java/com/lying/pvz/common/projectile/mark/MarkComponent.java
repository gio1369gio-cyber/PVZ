package com.lying.pvz.common.projectile.mark;

import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 实体上的标记组件。
 *
 * <p>服务端持有 {@code (mark, source) → (stacks, expireTick)}；客户端只同步
 * {@link Mark.View}（mark / source / stacks）用于渲染。
 *
 * <p>到期由实体 tick 驱动，不依赖全局 ticker。发射者死亡不立即清除，
 * 按 duration 自然失效。
 */
public final class MarkComponent implements Mark.Holder {

    private static final int MAX_ENTRIES = 8;
    private static final UUID SHARED_SOURCE = new UUID(0L, 0L);

    private final AbstractPAZEntity entity;
    private final Map<Mark.Key, Mark.Entry> entries = new HashMap<>();

    public MarkComponent(AbstractPAZEntity entity) {
        this.entity = entity;
    }

    public static void define(SynchedEntityData.Builder builder) {
        builder.define(AbstractPAZEntity.MARK_VIEWS, List.of());
    }

    /* ================================================================== */
    /* tick / 持久化                                                       */
    /* ================================================================== */

    public void tick() {
        if (this.entity.level().isClientSide()) {
            return;
        }
        final long now = this.entity.level().getGameTime();
        final boolean changed = this.entries.values().removeIf(entry -> entry.expireTick() <= now);
        if (changed) {
            this.publish();
        }
    }

    public void save(ValueOutput output) {
        final ValueOutput.TypedOutputList<Mark.Entry> list =
                output.list("marks", Mark.Entry.CODEC);
        this.entries.values().forEach(list::add);
    }

    public void load(ValueInput input) {
        this.entries.clear();
        for (Mark.Entry entry : input.listOrEmpty("marks", Mark.Entry.CODEC)) {
            this.entries.put(new Mark.Key(entry.mark(), entry.source()), entry);
        }
        this.publish();
    }

    /* ================================================================== */
    /* Mark.Holder                                                         */
    /* ================================================================== */

    @Override
    public int marks(Mark.Key key) {
        final Mark.Key normalized = this.normalize(key);
        if (normalized == null) {
            return 0;
        }
        if (this.entity.level().isClientSide()) {
            for (Mark.View view : this.views()) {
                if (view.mark().equals(normalized.mark()) && view.source().equals(normalized.source())) {
                    return view.stacks();
                }
            }
            return 0;
        }
        final Mark.Entry entry = this.entries.get(normalized);
        if (entry == null || entry.expireTick() <= this.entity.level().getGameTime()) {
            return 0;
        }
        return entry.stacks();
    }

    @Override
    public int marks(net.minecraft.resources.Identifier mark) {
        int total = 0;
        for (Mark.View view : this.views()) {
            if (view.mark().equals(mark)) {
                total += view.stacks();
            }
        }
        return total;
    }

    @Override
    public int addMark(Mark.Key key, int amount) {
        if (amount <= 0) {
            return this.marks(key);
        }
        final Mark.Key normalized = this.normalize(key);
        final Mark.Spec spec = normalized == null ? null : MarkRegistry.get(normalized.mark());
        if (normalized == null || spec == null) {
            return 0;
        }

        final Mark.Entry old = this.entries.get(normalized);
        final int stacks = Math.min(spec.maxStacks(),
                (old == null ? 0 : old.stacks()) + amount);
        final long now = this.entity.level().getGameTime();
        final long expire = (old != null && !spec.refreshOnAdd())
                ? old.expireTick() : now + spec.duration();

        if (old == null) {
            this.evictIfNeeded();
        }
        this.entries.put(normalized,
                new Mark.Entry(normalized.mark(), normalized.source(), stacks, expire));
        this.publish();
        return stacks;
    }

    @Override
    public int removeMark(Mark.Key key, int amount) {
        final Mark.Key normalized = this.normalize(key);
        if (normalized == null || amount <= 0) {
            return 0;
        }
        final Mark.Entry old = this.entries.get(normalized);
        if (old == null) {
            return 0;
        }
        final int removed = Math.min(amount, old.stacks());
        final int left = old.stacks() - removed;
        if (left <= 0) {
            this.entries.remove(normalized);
        } else {
            this.entries.put(normalized,
                    new Mark.Entry(normalized.mark(), normalized.source(), left, old.expireTick()));
        }
        this.publish();
        return removed;
    }

    @Override
    public boolean hasMark(Mark.Key key) {
        return this.marks(key) > 0;
    }

    @Override
    public List<Mark.View> views() {
        if (this.entity.level().isClientSide()) {
            return this.entity.getEntityData().get(AbstractPAZEntity.MARK_VIEWS);
        }
        final List<Mark.View> views = new ArrayList<>(this.entries.size());
        for (Mark.Entry entry : this.entries.values()) {
            views.add(entry.view());
        }
        return List.copyOf(views);
    }

    /* ================================================================== */
    /* 内部                                                                */
    /* ================================================================== */

    @Nullable
    private Mark.Key normalize(Mark.Key key) {
        if (key == null) {
            return null;
        }
        final Mark.Spec spec = MarkRegistry.get(key.mark());
        if (spec == null) {
            return null;
        }
        return spec.scope() == Mark.Scope.SHARED
                ? new Mark.Key(key.mark(), SHARED_SOURCE)
                : key;
    }

    private void evictIfNeeded() {
        if (this.entries.size() < MAX_ENTRIES) {
            return;
        }
        final Mark.Key oldest = this.entries.entrySet().stream()
                .min(Comparator.comparingLong(entry -> entry.getValue().expireTick()))
                .map(Map.Entry::getKey)
                .orElse(null);
        if (oldest != null) {
            this.entries.remove(oldest);
        }
    }

    private void publish() {
        if (!this.entity.level().isClientSide()) {
            final List<Mark.View> views = new ArrayList<>(this.entries.size());
            for (Mark.Entry entry : this.entries.values()) {
                views.add(entry.view());
            }
            this.entity.getEntityData().set(AbstractPAZEntity.MARK_VIEWS, List.copyOf(views));
        }
    }
}
