package com.lying.pvz.common.level.blockentity;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.event.lottery.LotteryEvent;
import com.lying.pvz.api.lottery.Lottery;
import com.lying.pvz.api.lottery.LotteryResult;
import com.lying.pvz.api.lottery.SlotEntry;
import com.lying.pvz.common.datapack.LotteryTypeLoader;
import com.lying.pvz.common.init.PvZBlockEntityRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.drop.JewelEntity;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.menu.SlotMachineMenu;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuProviderExtension;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 老虎机方块实体。
 * <p>{@code array} 现在是纯客户端的渲染快照（由 {@link #loadAdditional} 在收到更新包时写入），
 * 服务端不再维护它，也不再通过 {@code ContainerData} 通道同步。
 * <p>运行态字段（{@code changeCnt / changeTick / isRunning / playerId}）不持久化——服务器重启后
 * 正在滚动的老虎机会直接复位为静止状态。
 */
public class SlotMachineBlockEntity extends BaseBlockEntity implements MenuProvider, IMenuProviderExtension, Nameable {

    public static final int DATA_SIZE = 16;

    /** 客户端渲染快照。索引含义：0..11 展示条目；12 changeTick；13 currentPos；14 isRunning；15 total。 */
    public final ContainerData array = new SimpleContainerData(DATA_SIZE);
    protected final RandomSource rand = RandomSource.create();
    protected @Nullable Identifier lotteryId;

    private final int[][] slotIndices = new int[4][3];
    private int[] optionPool = new int[0];

    public int currentPos = 1;
    private static final int MIN_CHANGE_CNT = 12;
    private static final int MAX_CHANGE_CNT = 24;
    private int changeCnt;
    private int changeTick;
    private boolean isRunning;
    private @Nullable UUID playerId;
    private Component name;

    public SlotMachineBlockEntity(BlockPos pos, BlockState state) {
        super(PvZBlockEntityRegister.SLOT_MACHINE.get(), pos, state);
    }

    // ---------------- 运行逻辑 ----------------

    public void fastStart(Player player) {
        final Level level = this.level;
        if (level == null || level.isClientSide()) return;
        if (!this.canRun(player)) return;

        this.onStart(player);
        this.refreshOptionPool();
        if (this.optionPool.length == 0) {
            this.isRunning = false;
            this.syncToClient();
            return;
        }
        this.changeCnt = 0;
        this.changeTick = 0;
        this.genAll();
        this.checkResult();
    }

    public void slowStart(Player player) {
        final Level level = this.level;
        if (level == null || level.isClientSide()) return;
        if (!this.canRun(player)) return;

        this.onStart(player);
        this.refreshOptionPool();
        if (this.optionPool.length == 0) {
            this.isRunning = false;
            this.syncToClient();
            return;
        }
        this.changeCnt = level.getRandom().nextInt(MAX_CHANGE_CNT - MIN_CHANGE_CNT + 1) + MIN_CHANGE_CNT;
        this.genNextRow();
        this.setChanged();
        this.syncToClient();
    }

    public void tick() {
        final Level level = this.level;
        if (level == null || level.isClientSide()) return;
        if (this.changeTick <= 0) return;

        if (this.optionPool.length == 0) {
            this.changeTick = 0;
            this.changeCnt = 0;
            this.isRunning = false;
            this.syncToClient();
            return;
        }

        --this.changeTick;
        if (this.changeTick > 0) {
            this.syncToClient();
            return;
        }

        --this.changeCnt;
        if (this.changeCnt <= 0) {
            this.checkResult();
        } else {
            this.genNextRow();
            this.setChanged();
            this.syncToClient();
        }
    }

    private void syncToClient() {
        final Level level = this.level;
        if (level == null || level.isClientSide()) return;
        level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    private void onStart(Player player) {
        this.playerId = player.getUUID();
        this.isRunning = true;
        PlayerUtil.playClientSound(player, PvZSoundRegister.SLOT_MACHINE.get());
        PlayerUtil.addResource(player, Resources.SUN_NUM, -this.getSunCost());
        PlayerUtil.addResource(player, Resources.LOTTERY_CHANCE, -1);
    }

    protected Optional<LotteryResult> checkResult() {
        this.isRunning = false;
        this.changeTick = 0;

        final Level level = this.level;
        final Lottery lottery = this.getLottery();
        if (level == null || lottery == null) {
            this.changeCnt = 0;
            this.syncToClient();
            return Optional.empty();
        }

        final int leftId  = this.slotIndices[this.currentPos][0];
        final int midId   = this.slotIndices[this.currentPos][1];
        final int rightId = this.slotIndices[this.currentPos][2];

        final SlotEntry left  = lottery.getSlotType(leftId);
        final SlotEntry mid   = lottery.getSlotType(midId);
        final SlotEntry right = lottery.getSlotType(rightId);

        // 任一条目越界（/reload 后 entries 缩小等）→ 本轮作废并重抽
        if (left == null || mid == null || right == null) {
            this.refreshOptionPool();
            if (this.optionPool.length == 0) {
                this.changeCnt = 0;
                this.syncToClient();
                return Optional.empty();
            }
            // 用合法池重建一整行，让视觉与状态一致
            this.genAll();
            this.syncToClient();
            return Optional.empty();
        }

        final Optional<LotteryResult> result;
        if (leftId == midId && midId == rightId)
            result = Optional.of(LotteryResult.win(left, 3));
        else if (leftId == midId)
            result = Optional.of(LotteryResult.win(left, 1));
        else if (leftId == rightId)
            result = Optional.of(LotteryResult.win(left, 1));
        else if (midId == rightId)
            result = Optional.of(LotteryResult.win(right, 1));
        else
            result = Optional.empty();

        final Player player = this.resolvePlayer();
        NeoForge.EVENT_BUS.post(new LotteryEvent(level, this.worldPosition, lottery.id(),
                result.orElseGet(LotteryResult::miss), player));

        result.ifPresent(r -> this.genBonusResult(r.entry(), r.count(), player));

        this.setChanged();
        this.syncToClient();
        return result;
    }

    private void genBonusResult(SlotEntry entry, int num, @Nullable Player player) {
        final Level level = this.level;
        if (level == null) return;

        switch (entry.kind()) {
            case ITEM -> {
                final ItemStack stack = entry.createStack();
                if (stack == null) return;
                for (int i = 0; i < num; ++i) {
                    Containers.dropItemStack(level, this.worldPosition.getX(), this.worldPosition.getY() + 1,
                            this.worldPosition.getZ(), stack.copy());
                }
            }
            case SUN -> {
                final int amount = (num == 1 ? 3 : 10) * this.getSunCost();
                SunEntity.spawnSunsByAmount(level, this.worldPosition, amount, 50, 2);
            }
            case JEWEL -> {
                for (int i = 0; i < (num == 1 ? 2 : 7); ++i) {
                    final JewelEntity jewel = PvZEntityRegister.JEWEL.get().create(level, EntitySpawnReason.TRIGGERED);
                    if (jewel != null) {
                        jewel.setAmount(1);
                        EntityUtil.onEntityRandomPosSpawn(level, jewel, this.worldPosition, 3);
                    }
                }
            }
            case EVENT -> { /* 事件已统一 post */ }
            default -> { }
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PvZTriggers.SLOT_MACHINE.get().trigger(serverPlayer, num, entry.kind().name().toLowerCase());
        }
        level.playSound(null, this.worldPosition, PvZSoundRegister.JEWEL_DROP.get(), SoundSource.BLOCKS, 1F, 1F);
    }

    private @Nullable Player resolvePlayer() {
        if (this.playerId == null) return null;
        if (!(this.level instanceof ServerLevel sl)) return null;
        return sl.getServer().getPlayerList().getPlayer(this.playerId);
    }

    private void genAll() {
        if (this.optionPool.length == 0) return;
        final Level level = this.level;
        if (level == null) return;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.slotIndices[i][j] = this.optionPool[level.getRandom().nextInt(this.optionPool.length)];
            }
        }
    }

    private void genNextRow() {
        if (this.optionPool.length == 0) {
            this.changeTick = 0;
            return;
        }
        final Level level = this.level;
        if (level == null) return;

        this.changeTick = this.getChangeTick();
        this.currentPos = (this.currentPos + 1) % 4;
        final int next = (this.currentPos + 1) % 4;
        for (int i = 0; i < 3; ++i) {
            this.slotIndices[next][i] = this.optionPool[level.getRandom().nextInt(this.optionPool.length)];
        }
    }

    private void refreshOptionPool() {
        final Lottery lottery = this.getLottery();
        if (lottery == null) {
            PvZMod.LOGGER.warn("Slot Machine: lottery type {} not loaded, pool stays empty", this.lotteryId);
            this.optionPool = new int[0];
            return;
        }

        // /reload 后 entries.size() 可能变小，先把越界的 slotIndices 重抽为合法值
        final int max = lottery.getSize();
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.slotIndices[i][j] < 0 || this.slotIndices[i][j] >= max) {
                    this.slotIndices[i][j] = lottery.getRandomEntryIndex(this.rand).orElse(0);
                }
            }
        }

        final int len = lottery.slotCount();
        if (len <= 0) {
            PvZMod.LOGGER.error("Slot Machine: zero-length lottery type");
            this.optionPool = new int[0];
            return;
        }
        this.optionPool = new int[len];
        for (int i = 0; i < len; ++i) {
            this.optionPool[i] = lottery.getRandomEntryIndex(this.rand).orElse(0);
        }
    }

    public int getChangeTick() {
        if (this.changeCnt <= 2) return 16;
        if (this.changeCnt <= 5) return 12;
        if (this.changeCnt <= 12) return 8;
        return 4;
    }

    /** 供客户端 / 服务端统一使用。玩家为 null 或正在运行都返回 false。 */
    public boolean canRun(@Nullable Player player) {
        if (player == null || this.isRunning) return false;
        return PlayerUtil.getResource(player, Resources.SUN_NUM) >= this.getSunCost()
                && PlayerUtil.getResource(player, Resources.LOTTERY_CHANCE) > 0;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void init(@Nullable Identifier id) {
        this.lotteryId = id;
        Lottery lottery = this.getLottery();
        if (lottery == null) {
            final var all = LotteryTypeLoader.getLotteries();
            if (all.isEmpty()) {
                PvZMod.LOGGER.warn("Slot Machine: no lottery type loaded, init skipped");
                this.lotteryId = null;
                return;
            }
            final List<Identifier> keys = new ArrayList<>(all.keySet());
            this.lotteryId = keys.get(this.rand.nextInt(keys.size()));
            lottery = all.get(this.lotteryId);
        }
        if (lottery == null) {
            PvZMod.LOGGER.warn("Slot Machine: lottery type {} not loaded, init skipped", this.lotteryId);
            this.lotteryId = null;
            return;
        }
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.slotIndices[i][j] = lottery.getRandomEntryIndex(this.rand).orElse(0);
            }
        }
        this.setChanged();
        this.syncToClient();
    }

    // ---------------- 菜单 / 名称 ----------------

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new SlotMachineMenu(id, inv, this);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.worldPosition);
    }

    public void setCustomName(@Nullable Component name) {
        this.name = name;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    public MutableComponent getDefaultName() {
        return Component.translatable("block.pvz.slot_machine");
    }

    /** 不再缓存，`/reload` 后自动生效。 */
    @Nullable
    public Lottery getLottery() {
        if (this.lotteryId == null) return null;
        return LotteryTypeLoader.getLotteryType(this.lotteryId).orElse(null);
    }

    public int getSunCost() {
        final Lottery lottery = this.getLottery();
        return lottery == null ? 25 : lottery.sunCost();
    }

    public boolean isUsableByPlayer(Player player) {
        if (this.level == null) return false;
        if (this.level.getBlockState(this.worldPosition).getBlock() != this.getBlockState().getBlock()) return false;
        return player.distanceToSqr(
                this.worldPosition.getX() + 0.5,
                this.worldPosition.getY() + 0.5,
                this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    // ---------------- 网络同步 ----------------

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        final CompoundTag tag = super.getUpdateTag(registries);
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                tag.putInt("slot_" + i + "_" + j, this.slotIndices[i][j]);
            }
        }
        if (this.lotteryId != null) tag.putString("lottery_type", this.lotteryId.toString());
        tag.putInt("current_pos", this.currentPos);
        tag.putInt("change_tick", this.changeTick);
        tag.putInt("change_total", this.getChangeTick());
        tag.putBoolean("is_running", this.isRunning);
        return tag;
    }

    // ---------------- 持久化 ----------------

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        // 先恢复 lottery_type，才能知道当前 entries 的合法索引范围
        input.read("lottery_type", Identifier.CODEC).ifPresent(res -> this.lotteryId = res);
        final Lottery lottery = this.getLottery();
        final int max = lottery == null ? 0 : lottery.getSize();

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                final int idx = i;
                final int sub = j;
                input.getInt("slot_" + i + "_" + j).ifPresent(v -> {
                    if (max > 0 && v >= 0 && v < max) {
                        // 合法索引：原样恢复
                        this.slotIndices[idx][sub] = v;
                    } else if (lottery != null) {
                        // 越界：重抽一个合法索引（旧存档 / 数据包变更）
                        this.slotIndices[idx][sub] = lottery.getRandomEntryIndex(this.rand).orElse(0);
                    }
                    // lottery == null（客户端数据包未就绪）：保留原值，
                    // 渲染时 extractSlotMachineContent 会因 lottery 为 null 直接 return
                });
            }
        }

        this.currentPos = input.getIntOr("current_pos", this.currentPos);
        this.changeTick = input.getIntOr("change_tick", 0);
        this.changeCnt = input.getIntOr("change_cnt", 0);
        this.isRunning = input.getBooleanOr("is_running", false);

        this.name = BlockEntity.parseCustomNameSafe(input, "CustomName");

        // 刷新客户端渲染快照（服务端读存档时也会执行，但服务端不读 array，无害）
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.array.set(i * 3 + j, this.slotIndices[i][j]);
            }
        }
        this.array.set(12, this.changeTick);
        this.array.set(13, this.currentPos);
        this.array.set(14, this.isRunning ? 1 : 0);
        this.array.set(15, input.getIntOr("change_total", this.getChangeTick()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("current_pos", this.currentPos);
        output.storeNullable("lottery_type", Identifier.CODEC, this.lotteryId);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                output.putInt("slot_" + i + "_" + j, this.slotIndices[i][j]);
            }
        }

        if (this.name != null) {
            output.store("CustomName", ComponentSerialization.CODEC, this.name);
        }
    }
}