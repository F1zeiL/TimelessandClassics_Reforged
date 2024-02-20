package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.Type191AnimationController;
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
public class qbz_191_animation extends SkinAnimationModel {

    public qbz_191_animation() {
        extraOffset.put(MUZZLE_SILENCER, new Vector3d(0, 0, -0.0125));
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        Type191AnimationController controller = Type191AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Type191AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) != null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT_FOLDED);
            } else {
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT);
            }

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
                        matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                    } else if (!Gun.hasAmmo(stack)) {
                        matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                }
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
            }
            matrices.pop();
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Type191AnimationController.INDEX_MAG, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        if (controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation()) || controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation())) {
            matrices.push();
            {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), Type191AnimationController.INDEX_EXTRA_MAG, transformType, matrices);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            }
            matrices.pop();
        }

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Type191AnimationController.INDEX_BULLET1, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET1);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Type191AnimationController.INDEX_BULLET2, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET2);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Type191AnimationController.INDEX_RELEASE, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RELEASE);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
