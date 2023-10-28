package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.COLTPYTHONAnimationController;
import com.tac.guns.client.render.animation.TEC9AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.vector.Vector3f;

import static com.tac.guns.client.gunskin.ModelComponent.*;

public class colt_python_animation extends SkinAnimationModel {
    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        COLTPYTHONAnimationController controller = COLTPYTHONAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);
        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_BODY, transformType, matrices);
        RenderUtil.renderModel(getModelComponent(skin, BODY), stack, matrices, renderBuffer, light, overlay);
        RenderUtil.renderModel(getModelComponent(skin, SIGHT_LIGHT), stack, matrices, renderBuffer, 15728880, overlay);
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_MAG, transformType, matrices);
        RenderUtil.renderModel(getModelComponent(skin, MAG), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_MAG, transformType, matrices);
        if (cooldownOg < 0.74) {
            matrices.rotate(Vector3f.ZN.rotationDegrees(-45F * (cooldownOg * 1.74F)));
            matrices.translate(1.45 * (cooldownOg * 1.74F) * 0.0625, -0.625 * (cooldownOg * 1.74F) * 0.0625, 0);
        }
        RenderUtil.renderModel(getModelComponent(skin, ROTATE), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();

        matrices.push();
        if ((controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation()) ||
                controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation())) &&
                transformType.isFirstPerson()) {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_LOADER, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin, LOADER), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_BULLET1, transformType, matrices);
        if (cooldownOg < 0.74) {
            matrices.rotate(Vector3f.ZN.rotationDegrees(-45F * (cooldownOg * 1.74F)));
            matrices.translate(1.45 * (cooldownOg * 1.74F) * 0.0625, -0.625 * (cooldownOg * 1.74F) * 0.0625, 0);
        }
        RenderUtil.renderModel(getModelComponent(skin, BULLET1), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();

        matrices.push();
        if ((controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation()) ||
                controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation())) &&
                transformType.isFirstPerson()) {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_BULLET2, transformType, matrices);
            if (cooldownOg < 0.74) {
                matrices.rotate(Vector3f.ZN.rotationDegrees(-45F * (cooldownOg * 1.74F)));
                matrices.translate(1.45 * (cooldownOg * 1.74F) * 0.0625, -0.625 * (cooldownOg * 1.74F) * 0.0625, 0);
            }
            RenderUtil.renderModel(getModelComponent(skin, BULLET2), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}