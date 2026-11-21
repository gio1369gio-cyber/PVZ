package com.lying.pvz.common.menu;

import com.lying.pvz.common.init.PvZMenuRegister;
import com.lying.pvz.common.level.blockentity.SlotMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * 老虎机菜单。
 * <p>数据不再经过 {@code ContainerData} 通道：客户端通过 {@link SlotMachineBlockEntity#getUpdatePacket}
 * 同步的 BE 字段读渲染状态，因此本菜单客户端只负责在 {@link #getTe()} 中按 {@code BlockPos} 解析 BE。
 */
public class SlotMachineMenu extends AbstractContainerMenu {

    /** @deprecated 数据已改走 BlockEntity 更新包，此常量仅为兼容外部引用保留。 */
    @Deprecated
    public static final int DATA_SIZE = SlotMachineBlockEntity.DATA_SIZE;

    private final @Nullable SlotMachineBlockEntity te;
    private final @Nullable BlockPos pos;
    private final Player player;

    /** 客户端构造函数，供 IContainerFactory 使用。 */
    public SlotMachineMenu(int id, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
        this(id, playerInv, null, extraData.readBlockPos());
    }

    /** 服务端构造函数。 */
    public SlotMachineMenu(int id, Inventory playerInv, SlotMachineBlockEntity te) {
        this(id, playerInv, te, te.getBlockPos());
    }

    private SlotMachineMenu(int id, Inventory playerInv, @Nullable SlotMachineBlockEntity te, @Nullable BlockPos pos) {
        super(PvZMenuRegister.SLOT_MACHINE.get(), id);
        this.te = te;
        this.pos = pos;
        this.player = playerInv.player;
        this.addStandardInventorySlots(playerInv, 8, 145);
    }

    public Player getPlayer() {
        return this.player;
    }

    public @Nullable SlotMachineBlockEntity getTe() {
        if (this.te != null) return this.te;
        if (this.pos == null) return null;
        var level = this.player.level();
        if (level == null) return null;
        return level.getBlockEntity(this.pos) instanceof SlotMachineBlockEntity s ? s : null;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        SlotMachineBlockEntity te = this.getTe();
        if (te == null) return false;
        switch (id) {
            case 0 -> { te.slowStart(player); return true; }
            case 1 -> { te.fastStart(player); return true; }
            default -> { return false; }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        SlotMachineBlockEntity te = this.getTe();
        return te != null && te.isUsableByPlayer(playerIn);
    }
}