package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.Deagle50AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class deagle_357_animation extends SkinAnimationModel {

    protected boolean isEmp = false;

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        Deagle50AnimationController controller = Deagle50AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Deagle50AnimationController.INDEX_MAG, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            if (!(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) && ReloadHandler.get().getReloadProgress(v, stack) < 0.5) &&
                    !controller.isAnimationRunning(GunAnimationController.AnimationLabel.INSPECT_EMPTY) &&
                            transformType.isFirstPerson()) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Deagle50AnimationController.EXTRA_MAG, transformType, matrices);
            if (controller.isAnimationRunning())
                if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY))
                    isEmp = true;
                else if (!controller.isAnimationRunning(GunAnimationController.AnimationLabel.STATIC))
                    isEmp = false;
            if (transformType.isFirstPerson() && (!isEmp || (controller.isAnimationRunning(GunAnimationController.AnimationLabel.STATIC) && !isEmp))) {
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
                if (!(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) && ReloadHandler.get().getReloadProgress(v, stack) < 0.5) &&
                        !controller.isAnimationRunning(GunAnimationController.AnimationLabel.INSPECT_EMPTY) &&
                        transformType.isFirstPerson()) {
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
                }
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Deagle50AnimationController.INDEX_BODY, transformType, matrices);

            renderBarrel(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
            renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
        }
        matrices.pop();
        //Always push
        matrices.push(); // push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), Deagle50AnimationController.INDEX_SLIDE, transformType, matrices);

        if (transformType.isFirstPerson()) {
            Gun gun = ((GunItem) stack.getItem()).getGun();
            float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

            AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
            boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
            if (Gun.hasAmmo(stack) || shouldOffset) {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.280f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
            } else if (!Gun.hasAmmo(stack)) {
                matrices.translate(0, 0, 0.280f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
            }
            matrices.translate(0.00, 0.0, 0.035);
        }
        renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SLIDE_LIGHT);
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SLIDE);

        //Always pop
        matrices.pop();
        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
