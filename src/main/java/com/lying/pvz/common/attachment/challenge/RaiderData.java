package com.lying.pvz.common.attachment.challenge;


import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 玩家参与挑战的运行时数据。
 *
 * <p>只保存"当前挑战 ID"一个字段，其余状态由 {@code Challenge} 实例持有。
 */
public class RaiderData implements IRaiderData {

    public int challenge = 0;

    public RaiderData() {
    }

    public RaiderData(int challenge) {
        this.challenge = challenge;
    }

    @Override
    public void init(int ch) {
        this.challenge = ch;
    }

    @Override
    public int getChallengeID() {
        return this.challenge;
    }

    @Override
    public void setChallengeID(int ch) {
        this.challenge = ch;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putInt("in_challenge", this.challenge);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.challenge = input.getIntOr("in_challenge", 0);
    }
}