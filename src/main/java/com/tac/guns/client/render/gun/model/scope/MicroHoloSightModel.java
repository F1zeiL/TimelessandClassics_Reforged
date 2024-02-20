package com.tac.guns.client.render.gun.model.scope;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.transition.TimelessPistolGunItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;

import static com.tac.guns.client.SpecialModels.MICRO_HOLO_BASE;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MicroHoloSightModel implements IOverrideModel
{
    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/eotech_reticle.png");

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay) {
        if(!(parent.getItem() instanceof TimelessPistolGunItem))
            return;
        // Micro holo crashes worlds from previous versions, soon some standard weapons will be able to take micro optics as well, will handle differently if so
        matrixStack.push();
        /*if (Config.CLIENT.display.redDotSquishUpdate.get() && transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player)) {
            double transition = 1.0D - Math.pow(1.0D - AimingHandler.get().getNormalisedAdsProgress(), 2.0D);
            double zScale = 0.05D + 0.95D * (1.0D - transition);
            matrixStack.scale(1.0F, 1.0F, (float)zScale);
        }
        if (Config.CLIENT.display.redDotSquishUpdate.get() && transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player)) {
//            double prog = 0;
//            if(AimingHandler.get().getNormalisedAdsProgress() > 0.725) {
//                prog = (AimingHandler.get().getNormalisedAdsProgress() - 0.725) * 3.63;
//            }
//            double transition = 1.0D - Math.pow(1.0D - prog, 2.0D);
//            double zScale = 0.05D + 0.95D * (1.0D - transition);
//            matrixStack.scale(1.0F, 1.0F, (float) zScale);

        }*/
        if(!parent.isEmpty()) {
            GunItem gunItem = ((GunItem) parent.getItem());
            if (gunItem.getGun().getModules().getAttachments().getPistolScope().getDoOnSlideMovement() && transformType.isFirstPerson()) {
                //matrixStack.translate(0, 0, 0.025F);
                matrixStack.translate(0, 0, GunRenderingHandler.get().opticMovement * 0.505);
            }
            matrixStack.translate(0, 0.055, 0);
            if (gunItem.getGun().getModules().getAttachments().getPistolScope().getDoRenderMount())
                RenderUtil.renderModel(MICRO_HOLO_BASE.getModel(), stack, matrixStack, renderTypeBuffer, light, overlay);
        }
        RenderUtil.renderModel(stack, parent, matrixStack, renderTypeBuffer, light, overlay);
        matrixStack.translate(0, -0.049, 0);
        matrixStack.pop();
        matrixStack.translate(0, 0.006, 0);
        if(transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player))
        {
            matrixStack.push();
            {
                Matrix4f matrix = matrixStack.getLast().getMatrix();
                Matrix3f normal = matrixStack.getLast().getNormal();

                float size = 1.4F / 16.0F;
                matrixStack.translate(-size / 2, 0.85 * 0.0625, 0.075 * 0.0625);

                IVertexBuilder builder;

                double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                matrixStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 4.0;
                matrixStack.translate(size / 2, size / 2, 0);
                matrixStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                matrixStack.translate(0, 0, 0.0001);
                matrixStack.translate(0, 0, 0.05);
                int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, IAttachment.Type.SCOPE_RETICLE_COLOR, 1);

                float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                float alpha = (float) AimingHandler.get().getNormalisedAdsProgress();

                alpha = (float) (1F * AimingHandler.get().getNormalisedAdsProgress());

                builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(RED_DOT_RETICLE));
                // Walking bobbing
                boolean aimed = false;
                /* The new controlled bobbing */
                if(AimingHandler.get().isAiming())
                    aimed = true;
                GunRenderingHandler.get().applyBobbingTransforms(matrixStack,true);

                /*matrixStack.translate(0, 0, -0.35);
                matrixStack.rotate(Vector3f.YN.rotationDegrees((GunRenderingHandler.get().newSwayYaw * GunRenderingHandler.get().recoilReduction)*0.75F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees((GunRenderingHandler.get().newSwayYaw * GunRenderingHandler.get().weaponsHorizontalAngle * 0.65f * GunRenderingHandler.get().recoilReduction)*0.75F)); // seems to be interesting to
                // increase the force of
                //matrixStack.rotate(Vector3f.ZP.rotationDegrees(newSwayYaw * 2.5f * recoilReduction)); // seems to be interesting to increase the force of
                matrixStack.rotate(Vector3f.XN.rotationDegrees((GunRenderingHandler.get().recoilLift * GunRenderingHandler.get().recoilReduction) * 0.20F));
                matrixStack.translate(0, 0, 0.35);
*/
                builder.pos(matrix, 0, (float) (size / scale), 0).color(red, green, blue, alpha).tex(0.0F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, 0, 0, 0).color(red, green, blue, alpha).tex(0.0F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (size / scale), 0, 0).color(red, green, blue, alpha).tex(0.9375F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (size / scale), (float) (size / scale), 0).color(red, green, blue, alpha).tex(0.9375F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            matrixStack.pop();
        }
    }
}
