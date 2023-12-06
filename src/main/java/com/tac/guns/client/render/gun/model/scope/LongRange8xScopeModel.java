package com.tac.guns.client.render.gun.model.scope;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.command.ScopeEditor;
import com.tac.guns.client.handler.command.data.ScopeData;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.util.OptifineHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import static com.tac.guns.client.SpecialModels.Sx8_BODY;
import static com.tac.guns.client.SpecialModels.Sx8_FRONT;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class LongRange8xScopeModel implements IOverrideModel
{
    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/standard_8x_scope_reticle.png");
    private static final ResourceLocation HIT_MARKER = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/hit_marker/standard_8x_scope_reticle.png");

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay) {
        matrixStack.push();
        matrixStack.translate(0, -0.15, -0.38);
        matrixStack.translate(0, 0, 0.0015);
        if(AimingHandler.get().getNormalisedAdsProgress() < 0.525 || Config.CLIENT.display.scopeDoubleRender.get())
            RenderUtil.renderModel(Sx8_FRONT.getModel(), stack, matrixStack, renderTypeBuffer, light, overlay);
        RenderUtil.renderModel(Sx8_BODY.getModel(), stack, matrixStack, renderTypeBuffer, light, overlay);

        matrixStack.translate(0, 0.15, 0.42);

        matrixStack.pop();
        matrixStack.translate(0, 0, 0.04);
        if(transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player))
        {
            if(entity.getPrimaryHand() == HandSide.LEFT)
            {
                matrixStack.scale(-1, 1, 1);
            }

            ScopeData scopeData = ScopeEditor.get().getScopeData() == null || ScopeEditor.get().getScopeData().getTagName() != "gener8x" ? new ScopeData("") : ScopeEditor.get().getScopeData();
            ScopeItem scopeItem = (ScopeItem)stack.getItem();
            float scopeSize = 1.085F + 0.24375f + 0.03f + 0.3945f + scopeData.getDrZoomSizeMod();
            float scopePrevSize = 1.20F + scopeData.getReticleSizeMod();
            float size = scopeSize / 16.0F;
            float reticleSize = scopePrevSize / 16.0F;

            float crop = scopeItem.getProperties().getAdditionalZoom().getDrCropZoom() + scopeData.getDrZoomCropMod();
            Minecraft mc = Minecraft.getInstance();
            MainWindow window = mc.getMainWindow();


            float texU = ((window.getWidth() - window.getHeight() + window.getHeight() * crop * 2.0F) / 2.0F) / window.getWidth();

            matrixStack.push();
            {
                Matrix4f matrix = matrixStack.getLast().getMatrix();
                Matrix3f normal = matrixStack.getLast().getNormal();

                matrixStack.translate((-size / 2) + scopeData.getDrXZoomMod(), 0.08725 + 0.014 -0.005 + 0.00175 + scopeData.getDrYZoomMod(), Config.CLIENT.display.scopeDoubleRender.get() ? (4.70 -0.54725 + 0.0239 + scopeData.getDrZZoomMod()) * 0.0625 :
                        (2.37 + 0.54725 + 0.0239 + scopeData.getDrZZoomMod()) * 0.0625); //4.70
                float color = (float) AimingHandler.get().getNormalisedAdsProgress() * 0.8F + 0.2F;

                IVertexBuilder builder;

                if(Config.CLIENT.display.scopeDoubleRender.get() && !OptifineHelper.isShadersEnabled())
                {
                    builder = renderTypeBuffer.getBuffer(GunRenderType.getScreen());
                    builder.pos(matrix, 0, size, 0).color(color, color, color, 1.0F).tex(texU, 1.0F - crop).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.pos(matrix, 0, 0, 0).color(color, color, color, 1.0F).tex(texU, crop).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.pos(matrix, size, 0, 0).color(color, color, color, 1.0F).tex(1.0F - texU, crop).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.pos(matrix, size, size, 0).color(color, color, color, 1.0F).tex(1.0F - texU, 1.0F - crop).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                }

                matrixStack.translate(0, 0, 0.0001);

                double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                matrixStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 8.0;
                matrixStack.translate(size / 2, size / 2, 0);
                matrixStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                matrixStack.translate(0, 0, 0.0001);

                int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, IAttachment.Type.SCOPE_RETICLE_COLOR, 1);

                float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                float alpha = (float) AimingHandler.get().getNormalisedAdsProgress();

                alpha = (float) (1F * AimingHandler.get().getNormalisedAdsProgress());
                GunRenderingHandler.get().applyBobbingTransforms(matrixStack,true);
                if (Config.CLIENT.display.scopeDoubleRender.get()) {
                    matrixStack.scale(10f,10f,10f);
                    matrixStack.translate(-0.00455715, -0.00439, 0.001);
                    matrixStack.translate(0.00025875f, 0.0000525f, scopeData.getReticleZMod());
                } else {
                    matrixStack.scale(10f,10f,10f);
                    matrixStack.translate(-0.00455715, -0.00439, 0.001);
                    matrixStack.translate(0.00025875f, 0.0000525f, -0.005 + scopeData.getReticleZMod());
                }

                builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(RED_DOT_RETICLE));
                // Walking bobbing
                boolean aimed = false;
                /* The new controlled bobbing */
                if(AimingHandler.get().isAiming())
                    aimed = true;

                GunRenderingHandler.get().applyDelayedSwayTransforms(matrixStack, Minecraft.getInstance().player, partialTicks, -0.0225f);
                GunRenderingHandler.get().applyBobbingTransforms(matrixStack,true, 0.035f);
                GunRenderingHandler.get().applyNoiseMovementTransform(matrixStack, -0.06f);
                GunRenderingHandler.get().applyJumpingTransforms(matrixStack, partialTicks,-0.06f);

                float recoilReversedMod = 0.15f;
                matrixStack.translate(0, 0, -0.35);
                matrixStack.rotate(Vector3f.YP.rotationDegrees(GunRenderingHandler.get().newSwayYaw*recoilReversedMod*0.2f));
                matrixStack.rotate(Vector3f.ZN.rotationDegrees(GunRenderingHandler.get().newSwayPitch*recoilReversedMod*0.2f));
                matrixStack.rotate(Vector3f.XP.rotationDegrees((GunRenderingHandler.get().recoilLift*0.02f * GunRenderingHandler.get().recoilReduction) * 0.25F));
                matrixStack.translate(0, 0, 0.35);

                builder.pos(matrix, 0, (float) (reticleSize / scale), 0).color(red, green, blue, alpha).tex(0.0F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, 0, 0, 0).color(red, green, blue, alpha).tex(0.0F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (reticleSize / scale), 0, 0).color(red, green, blue, alpha).tex(0.9375F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (reticleSize / scale), (float) (reticleSize / scale), 0).color(red, green, blue, alpha).tex(0.9375F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();

//                if(Config.CLIENT.display.showHitMarkers.get() && HUDRenderingHandler.get().hitMarkerTracker > 0)
//                {
//                    builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(HIT_MARKER));
//
//                    if(HUDRenderingHandler.get().hitMarkerHeadshot)
//                    {
//                        green = 0;
//                        blue = 0;
//                        red = 1;
//                    }
//                    float opac = Math.max(Math.min(HUDRenderingHandler.get().hitMarkerTracker / HUDRenderingHandler.hitMarkerRatio, 100f), 0.25f);
//                    opac *= (float) AimingHandler.get().getNormalisedAdsProgress();
//                    builder.pos(matrix, 0, (float) (reticleSize / scale), 0).color(red, green, blue, opac).tex(0.0F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
//                    builder.pos(matrix, 0, 0, 0).color(red, green, blue, opac).tex(0.0F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
//                    builder.pos(matrix, (float) (reticleSize / scale), 0, 0).color(red, green, blue, opac).tex(0.9375F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
//                    builder.pos(matrix, (float) (reticleSize / scale), (float) (reticleSize / scale), 0).color(red, green, blue, opac).tex(0.9375F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
//
//                }
            }
            matrixStack.pop();
        }//float scopeSize = 1.095F;matrixStack.translate(-size / 2, 0.0645 , 3.45 * 0.0625);
    }
}
