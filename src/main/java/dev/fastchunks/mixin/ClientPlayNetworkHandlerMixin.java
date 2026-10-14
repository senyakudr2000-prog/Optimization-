package dev.fastchunks.mixin;

import dev.fastchunks.Deferred;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Растягивает обработку пакетов чанков/света/выгрузки по тикам.
 * Работает только в главном потоке (в сетевом потоке пакет пропускается как обычно
 * и сам переносится в главный поток ванилью).
 */
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onChunkData", at = @At("HEAD"), cancellable = true, require = 0)
    private void fc$chunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        if (!MinecraftClient.getInstance().isOnThread()) return;
        ClientPlayNetworkHandler self = (ClientPlayNetworkHandler) (Object) this;
        if (Deferred.deferIfNeeded(() -> self.onChunkData(packet))) ci.cancel();
    }

    @Inject(method = "onUnloadChunk", at = @At("HEAD"), cancellable = true, require = 0)
    private void fc$unload(UnloadChunkS2CPacket packet, CallbackInfo ci) {
        if (!MinecraftClient.getInstance().isOnThread()) return;
        ClientPlayNetworkHandler self = (ClientPlayNetworkHandler) (Object) this;
        if (Deferred.deferIfNeeded(() -> self.onUnloadChunk(packet))) ci.cancel();
    }

    @Inject(method = "onLightUpdate", at = @At("HEAD"), cancellable = true, require = 0)
    private void fc$light(LightUpdateS2CPacket packet, CallbackInfo ci) {
        if (!MinecraftClient.getInstance().isOnThread()) return;
        ClientPlayNetworkHandler self = (ClientPlayNetworkHandler) (Object) this;
        if (Deferred.deferIfNeeded(() -> self.onLightUpdate(packet))) ci.cancel();
    }
}
