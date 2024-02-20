package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.HK416A5AnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class hk416_a5_animation extends SkinAnimationModel {

    public hk416_a5_animation() {
        extraOffset.put(MUZZLE_SILENCER, new Vector3d(0, 0, -0.0125));
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        HK416A5AnimationController controller = HK416A5AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK416A5AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) != null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT_FOLDED);
            } else {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT);
            }

            renderLaserDevice(stack, matrices, renderBuffer, light, overlay, skin);

            if (transformType.isFirstPerson() || Config.COMMON.gameplay.canSeeLaserThirdSight.get())
                renderLaser(stack, matrices, renderBuffer, light, overlay, skin);

            renderStock(stack, matrices, renderBuffer, light, overlay, skin);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);

            matrices.push();
            {
                if (transformType.isFirstPerson()) {
                    Gun gun = ((GunItem) stack.getItem()).getGun();
                    int gunRate = (int) Math.min(ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()), 4);
                    int rateBias = (int) (ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) - gunRate);
                    float cooldownOg = (ShootingHandler.get().getshootMsGap() - rateBias) / gunRate < 0 ? 1 : MathHelper.clamp((ShootingHandler.get().getshootMsGap() - rateBias) / gunRate, 0, 1);

                    if (Gun.hasAmmo(stack)) {
                        // Math provided by Bomb787 on GitHub and Curseforge!!!
                        matrices.translate(0, 0, 0.205f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                    } else if (!Gun.hasAmmo(stack)) {
                        matrices.translate(0, 0, 0.205f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                    matrices.translate(0, 0, 0.025F);
                }
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
            }
            matrices.pop();
        }
        matrices.pop();
        /*if(Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.STANDARD_FLASHLIGHT.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_CQB_STANDARD_FLASHLIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }*/

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK416A5AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            if (!(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) && ReloadHandler.get().getReloadProgress(v, stack) < 0.5) &&
                    !controller.isAnimationRunning(GunAnimationController.AnimationLabel.INSPECT_EMPTY) &&
                    transformType.isFirstPerson()) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
            }
        }
        matrices.pop();

        //if(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_NORMAL)) {
        matrices.push();
        {
            if (transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK416A5AnimationController.INDEX_EXTRA_MAGAZINE, transformType, matrices);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
                if ((controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) &&
                        ReloadHandler.get().getReloadProgress(v, stack) > 0.5) ||
                        Gun.hasAmmo(stack)) {
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
                }
            }
        }
        matrices.pop();
        //}
        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
