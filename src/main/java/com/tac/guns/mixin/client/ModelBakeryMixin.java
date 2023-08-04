package com.tac.guns.mixin.client;

import com.tac.guns.gunskins.SkinManager;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.profiler.IProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ModelBakery.class})
public abstract class ModelBakeryMixin {
    @Inject(method = "processLoading",at = @At("HEAD"),remap = false)
    public void addSpecialModels(IProfiler profilerIn, int maxMipmapLevel, CallbackInfo ci){
        SkinManager.reload();
    }
}