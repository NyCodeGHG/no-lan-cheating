package de.nycode.nolancheating.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
@Mixin(ShareToLanScreen.class)
@Environment(EnvType.CLIENT)
public abstract class OpenToLanScreenMixin extends Screen implements ScreenAccessor {

    private static final Component CHEAT_TEXT = new TranslatableComponent("menu.no-lan-cheating.cheating_tooltip");

    protected OpenToLanScreenMixin(Component title) {
        super(title);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CycleButton$Builder;create(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/CycleButton$OnValueChange;)Lnet/minecraft/client/gui/components/CycleButton;", ordinal = 1))
    private CycleButton replaceCheatsButton(CycleButton.Builder instance, int i, int j, int k, int l, Component component, CycleButton.OnValueChange onValueChange) {

        final var button = instance
                .withTooltip(t -> Collections.singletonList(CHEAT_TEXT.getVisualOrderText()))
                .create(i, j, k, l, component, onValueChange);
        // Check if cheats are enabled in this world.
        button.active = areCheatsEnabled();

        return button;
    }

    @Inject(method = "render", at = @At(value = "RETURN"))
    private void renderTooltips(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        for (Widget widget : getRenderables()) {
            if (widget instanceof CycleButton button && button.isHovered()) {
                renderTooltip(poseStack, button.getTooltip(), i, j);
            }
        }
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
