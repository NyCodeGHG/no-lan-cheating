package de.nycode.nolancheating.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OpenToLanScreen.class)
@Environment(EnvType.CLIENT)
public abstract class OpenToLanScreenMixin extends Screen {

    @Shadow
    @Final
    private static Text ALLOW_COMMANDS_TEXT;

    protected OpenToLanScreenMixin(Text title) {
        super(title);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "init", at = @At(value = "NEW", target =
            "(IIIILnet/minecraft/text/Text;" + "Lnet/minecraft" + "/client/gui/widget/ButtonWidget$PressAction;)" +
                    "Lnet/minecraft/client/gui/widget/ButtonWidget;"))
    private ButtonWidget replaceCheatsButton(int x, int y, int width, int height, Text message,
                                             ButtonWidget.PressAction pressAction) {

        // Check if we are setting the open to lan button
        if (message != ALLOW_COMMANDS_TEXT) {
            return new ButtonWidget(x, y, width, height, message, pressAction);
        }

        ButtonWidget widget = new ButtonWidget(x, y, width, height, message, pressAction);

        // Check if cheats are enabled in this world.
        widget.active = areCheatsEnabled();
        return widget;
    }

    private boolean areCheatsEnabled() {
        if (client == null || client.getServer() == null || client.player == null) {
            return false;
        }

        return client.getServer().getPlayerManager().areCheatsAllowed() || client.player.hasPermissionLevel(2);
    }
}
