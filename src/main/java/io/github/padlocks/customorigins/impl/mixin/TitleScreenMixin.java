package io.github.padlocks.customorigins.impl.mixin;

import io.github.padlocks.customorigins.client.Config;
import io.github.padlocks.customorigins.client.gui.AutoUpdateGreetingScreen;
import io.github.padlocks.customorigins.client.gui.UpdateToast;
import io.github.padlocks.customorigins.updater.CustomOriginsUpdater;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(at = @At(value = "RETURN"), method = "render")
    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Config.isDisplayGreetingScreen()) {
            MinecraftClient.getInstance().openScreen(new AutoUpdateGreetingScreen((TitleScreen) (Object) this));
        } else {
            if (CustomOriginsUpdater.NEW_UPDATE) {
                UpdateToast.add();
            }
        }
    }
}
