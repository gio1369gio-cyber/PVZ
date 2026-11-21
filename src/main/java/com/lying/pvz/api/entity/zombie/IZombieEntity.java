package com.lying.pvz.api.entity.zombie;

import com.lying.pvz.api.entity.IPAZEntity;

/**
 * 僵尸实体契约。
 *
 * <p>僵尸专属行为（断手、断头、攀升、吃植物等）由 {@code entity.ability} 下的
 * 能力接口组合表达，本接口保持空，便于后续按需扩展而不影响已发布 API。
 */
public interface IZombieEntity extends IPAZEntity {
}