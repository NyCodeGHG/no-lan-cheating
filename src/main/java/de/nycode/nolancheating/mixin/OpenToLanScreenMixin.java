package de.nycode.nolancheating.mixin;

import de.nycode.nolancheating.NoLanCheating;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;

@Mixin(OpenToLanScreen.class)
@Environment(EnvType.CLIENT)
public abstract class OpenToLanScreenMixin extends Screen {

    @Shadow
    @Final
    private static Text ALLOW_COMMANDS_TEXT;

    private static final Text CHEAT_TEXT = new TranslatableText("menu.no-lan-cheating.cheating_tooltip");

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

        ButtonWidget widget =
                new ButtonWidget(x, y, width, height, message, pressAction, (button, matrices, mouseX, mouseY) -> {
                    renderOrderedTooltip(matrices, Collections
                            .singletonList(CHEAT_TEXT.asOrderedText()), mouseX, mouseY);
                });

        // Check if cheats are enabled in this world.
        widget.active = areCheatsEnabled();

        if (!widget.active && client != null && client.player != null) {
            client.player.incrementStat(NoLanCheating.ATTEMPTS_TO_CHEAT);
        }

        return widget;
    }

    private boolean areCheatsEnabled() {
        if (client == null || client.getServer() == null || client.player == null) {
            return false;
        }

        return client.getServer().getPlayerManager().areCheatsAllowed() || client.player.hasPermissionLevel(2);
    }
}
