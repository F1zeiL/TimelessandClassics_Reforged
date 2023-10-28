package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.handler.command.ScopeEditor;
import com.tac.guns.client.handler.command.data.ScopeData;
import com.tac.guns.client.render.animation.P90AnimationController;
import com.tac.guns.client.render.animation.UDP9AnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
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

import static com.tac.guns.client.SpecialModels.MINI_DOT_BASE;
import static com.tac.guns.client.gunskin.ModelComponent.*;

public class p90_animation extends SkinAnimationModel {

    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/dot_reticle.png");

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        P90AnimationController controller = P90AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), P90AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                RenderUtil.renderModel(getModelComponent(skin, SCOPE_DEFAULT), stack, matrices, renderBuffer, light, overlay);

                //scope dot render
                matrices.translate(0, 0.017, 0);
                if (transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player)) {
                    ScopeData scopeData = ScopeEditor.get().getScopeData() == null || ScopeEditor.get().getScopeData().getTagName() == "item.tac.p90" ? new ScopeData("") : ScopeEditor.get().getScopeData();
                    if (entity.getPrimaryHand() == HandSide.LEFT) {
                        matrices.scale(-1, 1, 1);
                    }
                    float scopePrevSize = (0.965F + 0.99F + 0.975f) + scopeData.getReticleSizeMod();
                    float scopeSize = 1.815F + scopeData.getDrZoomSizeMod();
                    float size = scopeSize / 16.0F;
                    float reticleSize = scopePrevSize / 16.0F;
                    float crop = 0.429F + scopeData.getDrZoomCropMod();//0.43F
                    Minecraft mc = Minecraft.getInstance();
                    MainWindow window = mc.getMainWindow();

                    float texU = ((window.getWidth() - window.getHeight() + window.getHeight() * crop * 2.0F) / 2.0F) / window.getWidth();

                    //matrixStack.rotate(Vector3f.ZP.rotationDegrees(-GunRenderingHandler.get().immersiveWeaponRoll));
                    matrices.push();
                    {
                        Matrix4f matrix = matrices.getLast().getMatrix();
                        Matrix3f normal = matrices.getLast().getNormal();

                        matrices.translate((-size / 2) + scopeData.getDrXZoomMod(), (0.0936175 + 0.3275) + scopeData.getDrYZoomMod(), Config.CLIENT.display.scopeDoubleRender.get() ? (3.915 - 3.605 + scopeData.getDrZZoomMod()) * 0.0625 : (3.075 - 3.605 + scopeData.getDrZZoomMod()) * 0.0625); //3.275

                        float color = (float) AimingHandler.get().getNormalisedAdsProgress() * 0.8F + 0.2F;

                        IVertexBuilder builder;

                        matrices.translate(0.002, -0.21, -0.54);

                        double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                        matrices.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                        double scale = 8.0;
                        matrices.translate(size / 2, size / 2, 0);
                        matrices.translate(-(size / scale) / 2, -(size / scale) / 2, 0);

                        int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, IAttachment.Type.SCOPE_RETICLE_COLOR, 1);

                        float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                        float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                        float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                        float alpha = (float) AimingHandler.get().getNormalisedAdsProgress();

                        alpha = (float) (1F * AimingHandler.get().getNormalisedAdsProgress());

                        //matrixStack.rotate(Vector3f.ZP.rotationDegrees(-GunRenderingHandler.get().immersiveWeaponRoll));
                        GunRenderingHandler.get().applyBobbingTransforms(matrices, true);
                        matrices.scale(6f, 6f, 6f);
                        //matrixStack.translate(-0.00335715, -0.0039355, 0.0000);
                        matrices.translate((-0.00335715 - 0.00375 - 0.00428) + scopeData.getReticleXMod(), (-0.0035055 - 0.00315) + scopeData.getReticleYMod(), 0.0000 + scopeData.getReticleZMod());


                        builder = renderBuffer.getBuffer(RenderType.getEntityTranslucent(RED_DOT_RETICLE));
                        // Walking bobbing
                        boolean aimed = false;
                        /* The new controlled bobbing */
                        if (AimingHandler.get().isAiming())
                            aimed = true;

                        GunRenderingHandler.get().applyDelayedSwayTransforms(matrices, Minecraft.getInstance().player, v, -0.075f);
                        GunRenderingHandler.get().applyBobbingTransforms(matrices,true, 0.1f);
                        GunRenderingHandler.get().applyNoiseMovementTransform(matrices, -0.1f);
                        GunRenderingHandler.get().applyJumpingTransforms(matrices, v,-0.05f);

                        matrices.translate(0, 0, -0.35);
                        matrices.rotate(Vector3f.YP.rotationDegrees(GunRenderingHandler.get().newSwayYaw * 0.15f));
                        matrices.rotate(Vector3f.ZN.rotationDegrees(GunRenderingHandler.get().newSwayPitch * 0.15f));
                        matrices.rotate(Vector3f.XP.rotationDegrees((GunRenderingHandler.get().recoilLift * GunRenderingHandler.get().recoilReduction) * 0.25F));
                        matrices.translate(0, 0, 0.35);

                        int lightmapValue = 15728880;
                        //alpha *= 0.6;
                        builder.pos(matrix, 0, (float) (reticleSize / scale), 0).color(red, green, blue, alpha).tex(0.0F, 0.9375F).overlay(overlay).lightmap(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                        builder.pos(matrix, 0, 0, 0).color(red, green, blue, alpha).tex(0.0F, 0.0F).overlay(overlay).lightmap(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                        builder.pos(matrix, (float) (reticleSize / scale), 0, 0).color(red, green, blue, alpha).tex(0.9375F, 0.0F).overlay(overlay).lightmap(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                        builder.pos(matrix, (float) (reticleSize / scale), (float) (reticleSize / scale), 0).color(red, green, blue, alpha).tex(0.9375F, 0.9375F).overlay(overlay).lightmap(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    }
                    matrices.pop();
                }
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), P90AnimationController.INDEX_BODY, transformType, matrices);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            RenderUtil.renderModel(getModelComponent(skin, BODY), stack, matrices, renderBuffer, light, overlay);
            RenderUtil.renderModel(getModelComponent(skin, RELEASE), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), P90AnimationController.INDEX_MAG, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin, MAG), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), P90AnimationController.INDEX_PULL, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin, PULL), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}