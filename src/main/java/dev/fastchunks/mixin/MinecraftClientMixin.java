package dev.fastchunks.mixin;

import dev.fastchunks.Deferred;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("TAIL"), require = 0)
    private void fc$tickEnd(CallbackInfo ci) {
        Deferred.onTickEnd();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At("HEAD"), require = 0)
    private void fc$disconnect(Screen s, boolean transferring, CallbackInfo ci) {
        Deferred.clear();
    }
}
