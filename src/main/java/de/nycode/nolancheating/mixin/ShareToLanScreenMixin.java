package de.nycode.nolancheating.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unchecked")
@Mixin(ShareToLanScreen.class)
@Environment(EnvType.CLIENT)
public abstract class ShareToLanScreenMixin extends Screen {

    private static final Component CHEAT_TEXT = Component.translatable("menu.no-lan-cheating.cheating_tooltip");

    protected ShareToLanScreenMixin(Component title) {
        super(title);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CycleButton$Builder;create(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/CycleButton$OnValueChange;)Lnet/minecraft/client/gui/components/CycleButton;", ordinal = 1))
    private CycleButton replaceCheatsButton(CycleButton.Builder builder, int x, int y, int width, int height, Component optionText, CycleButton.OnValueChange callback) {

        final var button = builder
                .withTooltip(t -> Tooltip.create(CHEAT_TEXT))
                .create(x, y, width, height, optionText);
        // Check if cheats are enabled in this world.
        button.active = areCheatsEnabled();

        return button;
    }

    @Unique
    private boolean areCheatsEnabled() {
        if (minecraft == null || minecraft.getSingleplayerServer() == null || minecraft.player == null) {
            return false;
        }

        return minecraft.getSingleplayerServer()
                .getPlayerList()
                .isAllowCheatsForAllPlayers() || minecraft.player.hasPermissions(2);
    }
}
