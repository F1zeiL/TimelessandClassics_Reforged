package com.tac.guns.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.client.gunskin.DyeSkin;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class ModelRenderUtil {
    private static void renderQuads(MatrixStack stack, IVertexBuilder buffer, List<BakedQuad> quads, int light, int overlay, int[] color) {
        MatrixStack.Entry entry = stack.getLast();

        for (BakedQuad quad : quads) {
            if(quad.hasTintIndex()){
                int i = quad.getTintIndex();
                if(i>=0 && i<color.length){
                    int c = color[i];
                    Color c1 = new Color(c);

                    buffer.addVertexData(entry, quad, c1.getRed()/255f, c1.getGreen()/255f, c1.getBlue()/255f, 1,
                            light, overlay, false);
                }
            }else {
                buffer.addVertexData(entry, quad, 1, 1, 1, 1, light, overlay, false);
            }
        }

    }

    private static void renderQuads(MatrixStack stack, IVertexBuilder buffer, List<BakedQuad> quads, int light, int overlay) {
        MatrixStack.Entry entry = stack.getLast();

        for (BakedQuad quad : quads) {
            buffer.addVertexData(entry, quad, 1, 1, 1, 1, light, overlay, false);
        }

    }

    public static void renderModel(IBakedModel model, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, int[] color) {
        stack.push();
        {
            stack.translate(-0.5D, -0.5D, -0.5D);

            RenderType renderType = RenderType.getEntityTranslucent(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            IVertexBuilder builder = ItemRenderer.getEntityGlintVertexBuilder(buffer, renderType, true, false);

            Random random = new Random();
            for(Direction direction : Direction.values()) {
                random.setSeed(42L);
                renderQuads(stack, builder, model.getQuads(null, direction, random), light, overlay, color);
            }
            random.setSeed(42L);
            renderQuads(stack, builder, model.getQuads(null, null, random), light, overlay, color);
        }
        stack.pop();
    }

    private static void renderModel(IBakedModel model, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
        stack.push();
        {
            stack.translate(-0.5D, -0.5D, -0.5D);

            RenderType renderType = RenderType.getEntityTranslucent(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            IVertexBuilder builder = ItemRenderer.getEntityGlintVertexBuilder(buffer, renderType, true, false);

            Random random = new Random();
            for(Direction direction : Direction.values()) {
                random.setSeed(42L);
                renderQuads(stack, builder, model.getQuads(null, direction, random), light, overlay);
            }
            random.setSeed(42L);
            renderQuads(stack, builder, model.getQuads(null, null, random), light, overlay);
        }
        stack.pop();
    }

    public static void renderModel(IBakedModel model, @Nonnull ItemStack item, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
        if(item.getItem() instanceof TimelessGunItem){
            GunSkin skin = SkinManager.getSkin(item);
            if(skin instanceof DyeSkin){
                int[] colors = ((DyeSkin) skin).getColors();
                renderModel(model, stack, buffer, light, overlay, colors);
            }else {
                renderModel(model, stack, buffer, light, overlay);
            }
        }
    }



}
