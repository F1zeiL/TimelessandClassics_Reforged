package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.Ak47AnimationController;
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
import net.minecraft.util.math.vector.Vector3d;

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class ak47_animation extends SkinAnimationModel {

    public ak47_animation() {
        extraOffset.put(MUZZLE_SILENCER, new Vector3d(0, 0, -0.295));
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        Ak47AnimationController controller = Ak47AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());
//        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Ak47AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) != null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_SCOPE);
            }

            renderStock(stack, matrices, renderBuffer, light, overlay, skin);

            renderBarrel(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        //Always push
        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Ak47AnimationController.INDEX_BOLT, transformType, matrices);

            /*//We're getting the cooldown tracker for the item - items like the sword, ender pearl, and chorus fruit all have this too.
            Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());
        */
            if (transformType.isFirstPerson()) {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.190f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1));
                GunRenderingHandler.get().opticMovement = 0.190f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1);
                matrices.translate(0, 0, 0.025F);
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        }
        //Always pop
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Ak47AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            if ((controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) &&
                    ReloadHandler.get().getReloadProgress(v, stack) > 0.5) ||
                    Gun.hasAmmo(stack)) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
            }
        }
        matrices.pop();

        if (controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation())) {
            matrices.push();
            {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), Ak47AnimationController.INDEX_EXTRA_MAG, transformType, matrices);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
                if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY)) {
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
                }
            }
            matrices.pop();
        }

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
