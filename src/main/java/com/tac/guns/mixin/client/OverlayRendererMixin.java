package com.tac.guns.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayRenderer.class)
public abstract class OverlayRendererMixin {
    @Shadow
    private static void renderFire(Minecraft p_228737_0_, MatrixStack p_228737_1_) {
    }

    @Inject(method = "renderOverlays",at = @At("RETURN"))
    private static void renderOverlays(Minecraft mc, MatrixStack stack, CallbackInfo ci){
        if (mc.player != null) {
            if(mc.player.isBurning() || mc.player.isSpectator()) return;
            if(mc.player.getActivePotionEffect(ModEffects.IGNITE.get()) == null){
                RenderSystem.disableAlphaTest();
                renderFire(mc,stack);
                RenderSystem.enableAlphaTest();
            }
        }
    }
}