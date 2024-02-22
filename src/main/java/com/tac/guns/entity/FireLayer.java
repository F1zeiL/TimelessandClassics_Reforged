package com.tac.guns.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.effect.IgniteEffect;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class FireLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private final Random random = new Random();

    public FireLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ModifiableAttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null)
            return;

        AttributeModifier speedModifier = speed.getModifier(IgniteEffect.IGNITE_UUID);
        if (speedModifier == null)
            return;

        stack.push();
        stack.scale(Math.min(entity.getWidth(), 2.0F), MathHelper.clamp(entity.getHeight(), 1.0F, 1.5F), Math.min(entity.getWidth(), 2.0F));
        stack.translate(-0.5F, 1.5F + (1 - MathHelper.clamp(entity.getHeight(), 1.0F, 1.5F)), 0.5F);
        stack.rotate(Vector3f.XP.rotationDegrees(180F));
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(Blocks.FIRE.getDefaultState(), stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        stack.pop();

//        Minecraft mc = Minecraft.getInstance();
//
//        TextureAtlasSprite textureatlassprite = ModelBakery.LOCATION_FIRE_0.getSprite();
//        TextureAtlasSprite textureatlassprite1 = ModelBakery.LOCATION_FIRE_1.getSprite();
//        stack.push();
//        float f = entity.getWidth() * 1.4F;
//        stack.scale(f, f, f);
//        float f1 = 0.5F;
//        float f2 = 0.0F;
//        float f3 = entity.getHeight() / f;
//        float f4 = 0.0F;
//        stack.rotate(Vector3f.YP.rotationDegrees(-mc.getRenderManager().info.getYaw()));
//        stack.translate(0.0D, 0.0D, (double)(-0.3F + (float)((int)f3) * 0.02F));
//        float f5 = 0.0F;
//        int i = 0;
//        IVertexBuilder ivertexbuilder = buffer.getBuffer(Atlases.getCutoutBlockType());
//
//        for(MatrixStack.Entry matrixstack$entry = stack.getLast(); f3 > 0.0F; ++i) {
//            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
//            float f6 = textureatlassprite2.getMinU();
//            float f7 = textureatlassprite2.getMinV();
//            float f8 = textureatlassprite2.getMaxU();
//            float f9 = textureatlassprite2.getMaxV();
//            if (i / 2 % 2 == 0) {
//                float f10 = f8;
//                f8 = f6;
//                f6 = f10;
//            }
//
//            fireVertex(matrixstack$entry, ivertexbuilder, f1 - 0.0F, 0.0F - f4, f5, f8, f9);
//            fireVertex(matrixstack$entry, ivertexbuilder, -f1 - 0.0F, 0.0F - f4, f5, f6, f9);
//            fireVertex(matrixstack$entry, ivertexbuilder, -f1 - 0.0F, 1.4F - f4, f5, f6, f7);
//            fireVertex(matrixstack$entry, ivertexbuilder, f1 - 0.0F, 1.4F - f4, f5, f8, f7);
//            f3 -= 0.45F;
//            f4 -= 0.45F;
//            f1 *= 0.9F;
//            f5 += 0.03F;
//        }
//        stack.pop();
    }
}
