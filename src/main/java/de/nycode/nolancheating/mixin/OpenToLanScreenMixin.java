package de.nycode.nolancheating.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;

@Mixin(ShareToLanScreen.class)
@Environment(EnvType.CLIENT)
public abstract class OpenToLanScreenMixin extends Screen {

    private static final Component CHEAT_TEXT = new TranslatableComponent("menu.no-lan-cheating.cheating_tooltip");

    protected OpenToLanScreenMixin(Component title) {
        super(title);
    }

    @Redirect(method = "init", at = @At(value = "NEW", target = "net/minecraft/client/gui/components/Button", ordinal = 3))
    private Button replaceCheatsButton(int x, int y, int width, int height, Component message,
                                       Button.OnPress pressAction) {

        Button widget =
                new Button(x, y, width, height, message, pressAction,
                        (button, matrices, mouseX, mouseY) -> renderTooltip(matrices,
                                Collections.singletonList(CHEAT_TEXT.getVisualOrderText()), mouseX, mouseY));

        // Check if cheats are enabled in this world.
        widget.active = areCheatsEnabled();

        return widget;
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
