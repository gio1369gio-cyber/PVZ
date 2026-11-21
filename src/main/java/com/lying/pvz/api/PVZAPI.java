package com.lying.pvz.api;

import com.lying.pvz.api.client.IClientLookup;
import com.lying.pvz.api.invasion.IInvasionAPI;
import com.lying.pvz.api.lookup.IPVZLookup;
import com.lying.pvz.api.raid.IRaidAPI;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class PVZAPI {

    private static final AtomicReference<@Nullable IPVZLookup> LOOKUP     = new AtomicReference<>();
    private static final AtomicReference<@Nullable IRaidAPI>    RAID       = new AtomicReference<>();
    private static final AtomicReference<@Nullable IClientLookup> CLIENT   = new AtomicReference<>();
    private static final AtomicReference<@Nullable IInvasionAPI> INVASION  = new AtomicReference<>();

    private PVZAPI() {
    }

    public static IPVZLookup lookup() {
        return require(LOOKUP, "lookup");
    }

    public static IRaidAPI raid() {
        return require(RAID, "raid");
    }

    public static IInvasionAPI invasion() {
        return require(INVASION, "invasion");
    }

    public static IClientLookup client() {
        return require(CLIENT, "client");
    }

    private static <T> T require(AtomicReference<@Nullable T> ref, String name) {
        return Objects.requireNonNull(
                ref.get(),
                () -> "PVZAPI." + name + "() called before bootstrap. "
                        + "Ensure your access happens after the PVZ mod's RegisterEvent stage.");
    }

    /* bootstrap 系列：参数本身在 @NullMarked 包内即为 @NonNull，直接 set 即可 */

    public static void bootstrapLookup(IPVZLookup lookup) {
        LOOKUP.set(lookup);
    }

    public static void bootstrapRaid(IRaidAPI raid) {
        RAID.set(raid);
    }

    public static void bootstrapInvasion(IInvasionAPI invasion) {
        INVASION.set(invasion);
    }

    public static void bootstrapClient(IClientLookup client) {
        CLIENT.set(client);
    }
}