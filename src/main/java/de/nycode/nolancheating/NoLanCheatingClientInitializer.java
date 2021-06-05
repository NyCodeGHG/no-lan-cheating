package de.nycode.nolancheating;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;

public class NoLanCheatingClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CrowdinTranslate.downloadTranslations("no-lan-cheating", "no-lan-cheating");
    }
}
