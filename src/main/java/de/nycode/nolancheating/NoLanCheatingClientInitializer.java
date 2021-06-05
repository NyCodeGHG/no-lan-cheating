package de.nycode.nolancheating;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;

import static de.nycode.nolancheating.NoLanCheating.MOD_ID;

public class NoLanCheatingClientInitializer implements ClientModInitializer {

    public static final String CROWDIN_PROJECT = "no-lan-cheating";

    @Override
    public void onInitializeClient() {
        CrowdinTranslate.downloadTranslations(CROWDIN_PROJECT, MOD_ID);
    }
}
