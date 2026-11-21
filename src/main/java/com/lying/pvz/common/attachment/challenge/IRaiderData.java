package com.lying.pvz.common.attachment.challenge;

import net.neoforged.neoforge.common.util.ValueIOSerializable;

/**
 * 挑战参战数据接口。
 *
 * <p>实现为 {@link ValueIOSerializable}，由 {@code AttachmentType.serializable(...)} 驱动持久化。
 */
public interface IRaiderData extends ValueIOSerializable {

    void init(int ch);

    int getChallengeID();

    void setChallengeID(int ch);
}