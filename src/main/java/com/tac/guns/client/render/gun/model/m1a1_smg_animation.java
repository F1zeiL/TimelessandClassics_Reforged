package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.HkMp5a5AnimationController;
import com.tac.guns.client.render.animation.M1A1AnimationController;
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
public class m1a1_smg_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        M1A1AnimationController controller = M1A1AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HkMp5a5AnimationController.INDEX_BODY, transformType, matrices);

            matrices.push();
            if (transformType.isFirstPerson()) {
                Gun gun = ((GunItem) stack.getItem()).getGun();
                float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

                matrices.translate(0, 0, -0.085f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
            matrices.pop();

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HkMp5a5AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderDrumMag(stack, matrices, renderBuffer, light, overlay, skin);
            if ((controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) &&
                    ReloadHandler.get().getReloadProgress(v, stack) > 0.5) ||
                    Gun.hasAmmo(stack)) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
            }
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
