package com.lying.pvz.api.entity.ability;

import java.util.Optional;
import java.util.UUID;

/**
 * 拥有者信息。
 */
public interface IHasOwner {

    /**
     * 拥有者的 UUID；无主（如自然环境生成的僵尸）时返回 {@link Optional#empty()}。
     */
    Optional<UUID> getOwnerUUID();
}