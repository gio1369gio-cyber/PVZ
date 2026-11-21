package com.lying.pvz.common.datapack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.lying.pvz.PvZMod;
import com.lying.pvz.api.raid.IAmountDefinition;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.api.transaction.TransactionType;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.impl.challenge.amount.ConstantAmount;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionTypeLoader extends SimplePreparableReloadListener<Map<Identifier, JsonElement>> {

    public static final String NAME = "transaction";
    private static final Map<Identifier, TransactionType> TRANSACTIONS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().create();

    /** JSON 定义的形状（不含 id）。 */
    private record Def(
            IAmountDefinition goodCount,
            List<GoodType> goods,
            List<Identifier> injectors
    ) {
        static final Codec<Def> CODEC = RecordCodecBuilder.create(i -> i.group(
                ChallengeManager.amountCodec()
                        .optionalFieldOf("good_count", new ConstantAmount(1))
                        .forGetter(Def::goodCount),
                GoodType.CODEC.listOf()
                        .optionalFieldOf("goods", List.of())
                        .forGetter(Def::goods),
                Identifier.CODEC.listOf()
                        .optionalFieldOf("injectors", List.of())
                        .forGetter(Def::injectors)
        ).apply(i, Def::new));
    }

    private final HolderLookup.Provider registries;

    public TransactionTypeLoader(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    protected Map<Identifier, JsonElement> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, JsonElement> out = new HashMap<>();
        FileToIdConverter lister = new FileToIdConverter(NAME + "s", ".json");
        for (Map.Entry<Identifier, net.minecraft.server.packs.resources.Resource> e
                : lister.listMatchingResources(manager).entrySet()) {
            Identifier id = lister.fileToId(e.getKey());
            try (Reader reader = e.getValue().openAsReader()) {
                out.put(id, StrictJsonParser.parse(reader));
            } catch (Exception ex) {
                PvZMod.LOGGER.error("Failed to read transaction file {}: {}", e.getKey(), ex);
            }
        }
        return out;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> raw, ResourceManager manager, ProfilerFiller profiler) {
        TRANSACTIONS.clear();
        RegistryOps<JsonElement> ops = this.registries.createSerializationContext(JsonOps.INSTANCE);
        raw.forEach((id, json) ->
                Def.CODEC.parse(ops, json)
                        .resultOrPartial(err -> PvZMod.LOGGER.error(
                                "Parsing error for transaction {}: {}", id, err))
                        .ifPresent(def -> TRANSACTIONS.put(id, new TransactionType(
                                id, def.goodCount(), def.goods(), def.injectors())))
        );
        PvZMod.LOGGER.info("Loaded {} transaction types", TRANSACTIONS.size());
    }

    @Nullable
    public static TransactionType getTransaction(Identifier id) {
        return TRANSACTIONS.get(id);
    }

    public static Map<Identifier, TransactionType> transactions() {
        return Collections.unmodifiableMap(TRANSACTIONS);
    }
}