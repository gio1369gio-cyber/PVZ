package com.lying.pvz.api.entity.ability;

/**
 * 可被吸引（通常由防御类植物使用）。
 */
public interface ICanBeAttracted {

    /**
     * 是否能被指定防御者吸引。
     */
    boolean canBeAttractedBy(ICanAttract defender);

    /**
     * 执行被吸引。
     */
    void attractBy(ICanAttract defender);
}