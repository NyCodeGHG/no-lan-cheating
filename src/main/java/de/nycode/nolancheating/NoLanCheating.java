package de.nycode.nolancheating;

import net.fabricmc.api.ModInitializer;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NoLanCheating implements ModInitializer {
    public static final String MOD_ID = "no-lan-cheating";

    public static final Identifier ATTEMPTS_TO_CHEAT = new Identifier(MOD_ID, "attempts_to_cheat");

    @Override
    public void onInitialize() {
        Registry.register(Registry.CUSTOM_STAT, "attempts_to_cheat", ATTEMPTS_TO_CHEAT);
        Stats.CUSTOM.getOrCreateStat(ATTEMPTS_TO_CHEAT, StatFormatter.DEFAULT);
    }
}
