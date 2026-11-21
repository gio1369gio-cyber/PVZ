package com.lying.pvz.common.datapack;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.invasion.InvasionManager;
import com.lying.pvz.common.invasion.type.InvasionType;
import com.lying.pvz.common.invasion.type.InvasionTypeData;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class InvasionTypeLoader extends SimpleJsonResourceReloadListener<InvasionTypeData> {

    public InvasionTypeLoader() {
        super(InvasionTypeData.CODEC, FileToIdConverter.json("invasions"));
    }

    @Override
    protected void apply(Map<Identifier, InvasionTypeData> map, ResourceManager manager, ProfilerFiller profiler) {
        InvasionManager.clearDatapack();
        map.forEach((id, data) -> InvasionManager.registerDatapack(id, new InvasionType(id, data)));
        PvZMod.LOGGER.info("Loaded invasions: api={}, datapack={}, unique={}",
                InvasionManager.apiCount(),
                InvasionManager.datapackCount(),
                InvasionManager.loadedCount());
    }
}