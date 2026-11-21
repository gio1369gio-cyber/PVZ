package com.lying.pvz.common.level.blockentity;

import com.lying.pvz.common.init.PvZBlockEntityRegister;
import com.lying.pvz.common.menu.EssenceAltarMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class EssenceAltarBlockEntity extends BaseBlockEntity implements MenuProvider {

    public final ItemStacksResourceHandler handler = new ItemStacksResourceHandler(4) {
        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            EssenceAltarBlockEntity.this.setChanged();
        }
    };

    public int tick = 0;

    public EssenceAltarBlockEntity(BlockPos pos, BlockState state) {
        super(PvZBlockEntityRegister.ESSENCE_ALTAR.get(), pos, state);
    }

    // ============================ Handler 辅助 ============================

    public ItemStack getStack(int slot) {
        return this.handler.getResource(slot).toStack((int) this.handler.getAmountAsLong(slot));
    }

    public void setStack(int slot, ItemStack stack) {
        this.handler.set(slot, ItemResource.of(stack), stack.getCount());
    }

    // ============================ tick ============================

    public void tick() {
        ++this.tick;
    }

    // ============================ 持久化 ============================

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.readChild("essence_altar_slots", this.handler);
        this.tick = input.getIntOr("tick", this.tick);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putChild("essence_altar_slots", this.handler);
        output.putInt("tick", this.tick);
    }

    // ============================ 掉落 ============================

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        Level level = this.level;
        if (level == null) {
            return;
        }
        for (int i = 0; i < this.handler.size(); ++i) {
            ItemStack stack = this.getStack(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    // ============================ Menu ============================

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new EssenceAltarMenu(id, inv, this);
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.pvz.essence_altar");
    }
}