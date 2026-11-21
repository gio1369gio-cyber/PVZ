package com.lying.pvz.api.invasion;

import java.util.Collection;
import java.util.UUID;

/**
 * 单玩家入侵运行时视图。
 */
public interface IInvasion {

    UUID getOwner();

    boolean isRunning();

    IInvasionType getPrimary();

    Collection<IInvasionType> getAssists();

    int getWaveIndex();

    /**
     * 当前任务类型（可能为 {@code null}）。
     */
    IMissionType getMissionType();

    int getMissionStage();

    int getMissionValue();

    void enable();

    void disable();
}