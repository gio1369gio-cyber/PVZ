package com.lying.pvz.common.invasion.type;

import com.lying.pvz.api.invasion.IInvasionFog;
import com.lying.pvz.api.invasion.IInvasionType;
import com.lying.pvz.api.invasion.ISpawnType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public final class InvasionType implements IInvasionType {

    private final Identifier id;
    private final InvasionTypeData data;
    private final List<ISpawnType> spawns;
    private final int requireLevel;
    private final int[] waveTimes;
    private final int[] waveSpawnCounts;

    public InvasionType(Identifier id, InvasionTypeData data) {
        this.id = id;
        this.data = data;
        this.spawns = data.spawns().stream().map(s -> (ISpawnType) s).toList();
        this.requireLevel = data.requireLevel().orElseGet(() ->
                this.spawns.stream().mapToInt(ISpawnType::getInvasionLevel).min().orElse(0));
        this.waveTimes = data.waves().stream().mapToInt(Wave::tick).toArray();
        this.waveSpawnCounts = data.waves().stream().mapToInt(Wave::count).toArray();
    }

    @Override
    public Identifier getID() {
        return id;
    }

    @Override
    public boolean isAssist() {
        return data.isAssist();
    }

    @Override
    public int getTriggerChance() {
        return data.triggerChance();
    }

    @Override
    public int getRequireLevel() {
        return requireLevel;
    }

    @Override
    public List<ISpawnType> getSpawns() {
        return spawns;
    }

    @Override
    public int getTotalWaveCount() {
        return waveTimes.length;
    }

    @Override
    public int[] getWaveTimes() {
        return waveTimes.clone();
    }

    @Override
    public int[] getWaveSpawnCounts() {
        return waveSpawnCounts.clone();
    }

    @Override
    public IInvasionFog getFog() {
        return null;
    }

    @Override
    public Optional<Identifier> getBonus() {
        return data.bonus();
    }

    @Override
    public TextColor getDisplayColor() {
        return data.displayColor();
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable("invasion." + id.getNamespace() + "." + id.getPath() + ".name")
                .withStyle(s -> s.withColor(data.displayColor()));
    }
}
