package de.nycode.nolancheating.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@SuppressWarnings("unchecked")
@Mixin(ShareToLanScreen.class)
@Environment(EnvType.CLIENT)
public abstract class ShareToLanScreenMixin extends Screen implements ScreenAccessor {

    private static final Component CHEAT_TEXT = Component.translatable("menu.no-lan-cheating.cheating_tooltip");

    protected ShareToLanScreenMixin(Component title) {
        super(title);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CycleButton$Builder;create(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/CycleButton$OnValueChange;)Lnet/minecraft/client/gui/components/CycleButton;", ordinal = 1))
    private CycleButton replaceCheatsButton(CycleButton.Builder builder, int x, int y, int width, int height, Component optionText, CycleButton.OnValueChange callback) {

        final var button = builder
                .withTooltip(t -> Collections.singletonList(CHEAT_TEXT.getVisualOrderText()))
                .create(x, y, width, height, optionText);
        // Check if cheats are enabled in this world.
        button.active = areCheatsEnabled();

        return button;
    }

    @Inject(method = "render", at = @At(value = "RETURN"))
    private void renderTooltips(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for (Widget widget : getRenderables()) {
            if (widget instanceof CycleButton button && button.isHoveredOrFocused()) {
                renderTooltip(matrices, button.getTooltip(), mouseX, mouseY);
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
