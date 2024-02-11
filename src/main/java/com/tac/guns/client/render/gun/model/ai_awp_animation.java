package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.AWPAnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
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
 * Author: ClumsyAlien, codebase and design based off Mr.Pineapple's original addon
 */
public class ai_awp_animation extends SkinAnimationModel {

    public ai_awp_animation() {
        extraOffset.put(MUZZLE_SILENCER, new Vector3d(0, 0, -0.4));
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        AWPAnimationController controller = AWPAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), AWPAnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT);
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
            }

            renderBarrel(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), AWPAnimationController.INDEX_BOLT, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), AWPAnimationController.INDEX_BOLT_EXTRA, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT_EXTRA);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), AWPAnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            if (controller.isAnimationRunning() && controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.PULL_BOLT).equals(controller.getPreviousAnimation())) {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), AWPAnimationController.INDEX_BULLET, transformType, matrices);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET_SHELL);
            }
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
