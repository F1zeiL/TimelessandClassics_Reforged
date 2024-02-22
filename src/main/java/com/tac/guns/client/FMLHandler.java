package com.tac.guns.client;

import com.tac.guns.Reference;
import com.tac.guns.entity.FireLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FMLHandler {
    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent evt) {
        Minecraft.getInstance().getRenderManager().renderers.values().forEach(r -> {
            if (r instanceof LivingRenderer) {
                attachRenderLayers((LivingRenderer<?, ?>) r);
            }
        });
        Minecraft.getInstance().getRenderManager().getSkinMap().values().forEach(FMLHandler::attachRenderLayers);
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingRenderer<T, M> renderer) {
        renderer.addLayer(new FireLayer<>(renderer));
    }
}
