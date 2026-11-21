package com.lying.pvz.common.menu.shop;

import com.lying.pvz.api.transaction.GoodKind;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.npc.AbstractDaveEntity;
import com.lying.pvz.common.transaction.goodkind.EnergyGood;
import com.lying.pvz.common.transaction.goodkind.MoneyGood;
import com.lying.pvz.common.transaction.goodkind.SlotGood;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDaveShopMenu extends AbstractContainerMenu {

    /* ---------- 槽位常量 ---------- */
    public static final int SLOT_DELIVERY     = 0;
    public static final int SLOT_INV_START    = 1;
    public static final int SLOT_INV_END      = 28;  // exclusive，主背包 27 格
    public static final int SLOT_HOTBAR_START = 28;
    public static final int SLOT_HOTBAR_END   = 37;  // exclusive，快捷栏 9 格

    /** 允许交互的最大距离平方（8 格）。 */
    private static final double MAX_DIST_SQR = 64.0;

    protected final Player player;
    @Nullable protected final AbstractDaveEntity dave;

    /** 交付缓冲槽：购买成功后物品先落在这里，玩家手动取走。 */
    protected final SimpleContainer delivery = new SimpleContainer(1);

    public AbstractDaveShopMenu(MenuType<?> type, int id, Player player, int entityId) {
        super(type, id);
        this.player = player;

        Entity entity = player.level().getEntity(entityId);
        this.dave = entity instanceof AbstractDaveEntity d ? d : null;

        // 0：交付槽，只出不进
        this.addSlot(new Slot(this.delivery, 0, 171, 86) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });

        // 1..27 主背包 + 28..36 快捷栏
        this.addStandardInventorySlots(player.getInventory(), 117, 113);
    }

    /* ======================== 子类扩展点 ======================== */

    /** 本商店使用的货币资源。 */
    protected abstract Resources getCurrency();

    /** 购买成功音效；返回 null 表示静音。 */
    @Nullable
    protected SoundEvent getPurchaseSound() { return null; }

    /** 购买成功回调（仅服务端）。默认按 {@link #getPurchaseSound()} 播音效。 */
    protected void onPurchaseSuccess(Player player, GoodType good) {
        SoundEvent sound = this.getPurchaseSound();
        if (sound != null) {
            PlayerUtil.playClientSound(player, sound);
        }
    }

    /* ======================== 商品视图 ======================== */

    /**
     * 一条可用于 GUI 展示的商品。
     * <p>{@code key} 是 {@code currentGoods} 的原始键，购买时原样回传，
     * 不会因为过滤/排序而错位。
     */
    public record GoodEntry(int key, GoodType good) {}

    /** 过滤并排序后的商品列表。每项携带稳定 key。 */
    public List<GoodEntry> getDisplayGoods(Player viewer) {
        if (this.dave == null) return List.of();

        List<GoodEntry> out = new ArrayList<>();
        for (Map.Entry<Integer, GoodType> e : this.dave.getGoodMap().entrySet()) {
            GoodType g = e.getValue();
            if (!g.kind().isValidFor(viewer)) continue;
            if (!g.must() && g.limit() <= 0) continue;
            out.add(new GoodEntry(e.getKey(), g));
        }
        out.sort(Comparator.comparingInt((GoodEntry x) -> x.good().kind().priority()).reversed());
        return out;
    }

    /* ======================== 购买（服务端权威） ======================== */

    /**
     * GUI 点击购买时调用
     * {@code minecraft.gameMode.handleInventoryButtonClick(this.containerId, key)}，
     * 服务端回调此方法。
     */
    @Override
    public boolean clickMenuButton(Player p, int key) {
        if (p != this.player) return false;
        return this.tryPurchase(key);
    }

    /** 真正执行购买。仅服务端。 */
    public boolean tryPurchase(int key) {
        if (this.dave == null || !this.stillValid(this.player)) return false;

        GoodType good = this.dave.getGoodMap().get(key);
        if (good == null) return false;

        // 库存 / must
        if (!good.must() && good.limit() <= 0) return false;

        // 玩家资格（等级类商品靠它校验）
        if (!good.kind().isValidFor(this.player)) return false;

        // 货币校验
        int price = good.price();
        Resources currency = this.getCurrency();
        if (price > 0 && PlayerUtil.getResource(this.player, currency) < price) return false;

        // 上次没取走的交付物，禁止连续购买
        if (!this.delivery.getItem(0).isEmpty()) return false;

        // 1) 扣钱
        if (price > 0) PlayerUtil.addResource(this.player, currency, -price);

        // 2) 交付
        if (!this.deliver(good)) {
            if (price > 0) PlayerUtil.addResource(this.player, currency, price); // 回滚
            return false;
        }

        // 3) 扣库存（内部 sync → 触发 GOODS 同步）
        this.dave.sellGoodForTransactions(key);

        // 4) 成就/统计
        if (this.player instanceof ServerPlayer sp) {
            PvZTriggers.TRADE.get().trigger(sp, this.dave, price);
        }

        // 5) 子类扩展点
        this.onPurchaseSuccess(this.player, good);
        return true;
    }

    /** 按 {@link GoodKind} 实际类型交付。 */
    private boolean deliver(GoodType good) {
        GoodKind kind = good.kind();

        if (kind instanceof EnergyGood) {
            PlayerUtil.addResource(this.player, Resources.MAX_ENERGY_NUM, 1);
            return true;
        }
        if (kind instanceof SlotGood) {
            PlayerUtil.addResource(this.player, Resources.SLOT_NUM, 1);
            return true;
        }
        if (kind instanceof MoneyGood m) {
            // 注意：这里永远是 MONEY，与 getCurrency() 无关
            PlayerUtil.addResource(this.player, Resources.MONEY, m.amount());
            return true;
        }

        // 物品类（含 ItemGood 及任何自定义 kind）
        ItemStack stack = kind.createStack(this.player.level().registryAccess());
        if (stack.isEmpty()) return false;
        this.delivery.setItem(0, stack);
        return true;
    }

    /* ======================== 查询 ======================== */

    public Optional<AbstractDaveEntity> getDave() { return Optional.ofNullable(this.dave); }

    /** 距下次刷新剩余 tick；未初始化或无 Dave 时为空。 */
    public Optional<Integer> getLeftRefreshTime() {
        if (this.dave == null) return Optional.empty();
        long at = this.dave.getNextRefreshAt();
        if (at <= 0) return Optional.empty();
        long now = this.player.level().getGameTime();
        return Optional.of((int) Math.max(0, at - now));
    }

    /** 交付槽为空才允许再次购买。GUI 可据此禁用按钮。 */
    public boolean canBuy() { return this.delivery.getItem(0).isEmpty(); }

    /** 交付槽内容，供 GUI 渲染。 */
    public ItemStack getDeliveryStack() { return this.delivery.getItem(0); }

    /* ======================== 容器生命周期 ======================== */

    @Override
    public boolean stillValid(Player p) {
        if (this.dave == null || this.dave.isRemoved()) return false;
        if (p != this.player) return false;
        if (this.dave.getCustomer() != null && this.dave.getCustomer() != p) return false;
        return p.distanceToSqr(this.dave) <= MAX_DIST_SQR;
    }

    @Override
    public ItemStack quickMoveStack(Player p, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack original = slot.getItem().copy();
        boolean moved;

        if (index == SLOT_DELIVERY) {
            moved = this.moveItemStackTo(slot.getItem(), SLOT_INV_START, SLOT_HOTBAR_END, true);
        } else if (index < SLOT_INV_END) {
            moved = this.moveItemStackTo(slot.getItem(), SLOT_HOTBAR_START, SLOT_HOTBAR_END, false);
        } else {
            moved = this.moveItemStackTo(slot.getItem(), SLOT_INV_START, SLOT_INV_END, false);
        }

        if (!moved) return ItemStack.EMPTY;

        if (slot.getItem().isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        return original;
    }

    @Override
    public void removed(Player p) {
        super.removed(p);
        if (this.dave != null) this.dave.setCustomer(null);
        // 交付槽若还有物品，放回背包/掉落
        this.clearContainer(p, this.delivery);
    }
}