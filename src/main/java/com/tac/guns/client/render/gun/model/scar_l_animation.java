package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.SCAR_LAnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
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
public class scar_l_animation extends SkinAnimationModel {

    public scar_l_animation() {
        extraOffset.put(MUZZLE_SILENCER, new Vector3d(0, 0, -0.0225));
    }

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        SCAR_LAnimationController controller = SCAR_LAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), SCAR_LAnimationController.INDEX_BODY, transformType, matrices);

            renderSight(stack, matrices, renderBuffer, light, overlay, skin);

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            renderLaserDevice(stack, matrices, renderBuffer, light, overlay, skin);

            if (transformType.isFirstPerson() || Config.COMMON.gameplay.canSeeLaserThirdSight.get())
                renderLaser(stack, matrices, renderBuffer, light, overlay, skin);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), SCAR_LAnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            if (!(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) && ReloadHandler.get().getReloadProgress(partialTicks, stack) < 0.5) &&
                    !controller.isAnimationRunning(GunAnimationController.AnimationLabel.INSPECT_EMPTY) &&
                    transformType.isFirstPerson()) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
            }
        }
        matrices.pop();

        matrices.push();
        {
            if (transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), SCAR_LAnimationController.INDEX_MAGAZINE2, transformType, matrices);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
                if (!(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) && ReloadHandler.get().getReloadProgress(partialTicks, stack) < 0.5) &&
                        !controller.isAnimationRunning(GunAnimationController.AnimationLabel.INSPECT_EMPTY) &&
                        transformType.isFirstPerson()) {
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
                }
            }
        }
        matrices.pop();

        matrices.push();
        {
            if (transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), SCAR_LAnimationController.INDEX_BOLT, transformType, matrices);
                Gun gun = ((GunItem) stack.getItem()).getGun();
                int gunRate = (int) Math.min(ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()), 4);
                int rateBias = (int) (ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) - gunRate);
                float cooldownOg = (ShootingHandler.get().getshootMsGap() - rateBias) / gunRate < 0 ? 1 : MathHelper.clamp((ShootingHandler.get().getshootMsGap() - rateBias) / gunRate, 0, 1);

                AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
                if (Gun.hasAmmo(stack) || shouldOffset) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.225f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                } else if (!Gun.hasAmmo(stack)) {
                    {
                        matrices.translate(0, 0, 0.225f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                }
                matrices.translate(0, 0, 0.025F);
            }

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
