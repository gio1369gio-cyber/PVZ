package com.lying.pvz.api.event.lottery;

import com.lying.pvz.api.lottery.LotteryResult;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * 老虎机抽奖事件，每次判定后触发（服务端），未中奖也触发。
 *
 * <p>监听者可用 {@link #isWin()} 区分是否中奖；需要方块实体时自行
 * {@code level.getBlockEntity(pos)}。
 *
 * <p><b>无玩家场景</b>：老虎机可能被红石 / 命令 / 自动化逻辑触发，此时
 * {@link #getPlayer()} 返回 {@link Optional#empty()}。
 */
public class LotteryEvent extends Event {

    private final Level level;
    private final BlockPos pos;
    private final Identifier lotteryId;
    private final LotteryResult result;
    private final @Nullable Player player;

    public LotteryEvent(Level level, BlockPos pos, Identifier lotteryId,
                        LotteryResult result, @Nullable Player player) {
        this.level = level;
        this.pos = pos;
        this.lotteryId = lotteryId;
        this.result = result;
        this.player = player;
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    /**
     * 奖池标识，对应 {@code data/<ns>/lotterys/*.json} 的文件名。
     */
    public Identifier getLotteryId() {
        return this.lotteryId;
    }

    public LotteryResult getResult() {
        return this.result;
    }

    public boolean isWin() {
        return this.result.win();
    }

    public int getCount() {
        return this.result.count();
    }

    /**
     * 触发抽奖的玩家；无玩家触发时返回空。
     */
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(this.player);
    }
}