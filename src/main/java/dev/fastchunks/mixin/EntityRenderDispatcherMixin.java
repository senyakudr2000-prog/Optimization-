package dev.fastchunks.mixin;

import dev.fastchunks.Config;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderManager.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
    private void fc$cull(Entity entity, Frustum frustum, double x, double y, double z,
                         CallbackInfoReturnable<Boolean> cir) {
        if (!Config.cullFarEntities || Config.entityMaxDistance <= 0) return;
        if (entity instanceof PlayerEntity) return;
        double max = Config.entityMaxDistance;
        if (entity.squaredDistanceTo(x, y, z) > max * max) cir.setReturnValue(false);
    }
}
