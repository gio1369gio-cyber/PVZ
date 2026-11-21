package com.lying.pvz.api.entity.ability;

/**
 * 携带金属覆层。
 */
public interface IHasMetal {

    boolean hasMetal();

    void increaseMetal();

    void decreaseMetal();

    MetalTypes getMetalType();
}