package com.lying.pvz.data.provider.model;


import com.lying.pvz.PvZMod;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

/**
 * 把 assets/pvz/textures/screenshot/** 注册进 minecraft:blocks atlas。
 * 生成文件：src/generated/resources/assets/pvz/atlases/blocks.json
 * NeoForge 会把它合并进 minecraft:blocks atlas。
 *
 * <p>sourcePath = "screenshot" → 扫 textures/screenshot 下所有 png
 * <p>idPrefix   = "screenshot/" → sprite id 前缀（结尾斜杠必需）
 *   例：textures/screenshot/plant/angel_star_fruit.png → sprite id pvz:screenshot/plant/angel_star_fruit
 */
public class PvZSpriteSourceProvider extends SpriteSourceProvider {


    public PvZSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, PvZMod.MOD_ID);
    }

    @Override
    protected void gather() {
        this.atlas(AtlasIds.ITEMS).addSource(
                new DirectoryLister("screenshot", "screenshot/")
        );
    }
}