package de.nycode.nolancheating.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OpenToLanScreen.class)
@Environment(EnvType.CLIENT)
public class OpenToLanScreenMixin {

    @Shadow
    @Final
    private static Text ALLOW_COMMANDS_TEXT;

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
        widget.active = false;
        return widget;
    }
}
