package com.lying.pvz.common.datapack;

import com.google.gson.JsonElement;
import com.lying.pvz.PvZMod;
import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.impl.challenge.DefaultChallengeDefinition;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChallengeTypeLoader extends SimpleJsonResourceReloadListener<JsonElement> {

    public static final String NAME = "challenge";
    private final HolderLookup.Provider registries;

    public ChallengeTypeLoader(HolderLookup.Provider registries) {
        super(ExtraCodecs.JSON, FileToIdConverter.json(NAME + "s"));
        this.registries = registries;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
        final Map<Identifier, IChallengeDefinition> parsed = new LinkedHashMap<>();
        int failed = 0;
        for (Map.Entry<Identifier, JsonElement> e : map.entrySet()) {
            final IChallengeDefinition def = parse(e.getKey(), e.getValue(), this.registries);
            if (def == null) {
                PvZMod.LOGGER.error("Skipping invalid challenge: {}", e.getKey());
                failed++;
                continue;
            }
            parsed.put(e.getKey(), def);
        }
        ChallengeManager.replaceLoaded(parsed);
        PvZMod.LOGGER.info("Loaded {} challenges ({} failed)", ChallengeManager.loadedCount(), failed);
    }

    private static IChallengeDefinition parse(Identifier res, JsonElement json, HolderLookup.Provider regs) {
        final RegistryOps<JsonElement> ops = regs.createSerializationContext(JsonOps.INSTANCE);
        final var result = ChallengeManager.challengeCodec().parse(ops, json);
        if (result.error().isPresent()) {
            PvZMod.LOGGER.error("Error loading challenge {}: {}", res, result.error().get().message());
            return null;
        }
        final IChallengeDefinition def = result.result().get();
        if (def instanceof DefaultChallengeDefinition d) d.initId(res);
        return def;
    }

}