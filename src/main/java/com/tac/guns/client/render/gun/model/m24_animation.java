package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.M24AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
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
 * Author: ClumsyAlien, codebase and design based off Mr.Pineapple's original addon
 */
public class m24_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        M24AnimationController controller = M24AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M24AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT);
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M24AnimationController.INDEX_BOLT, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M24AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M24AnimationController.INDEX_BULLET, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET_SHELL);
            AnimationMeta bolt = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.PULL_BOLT);
            boolean onlyShell = bolt != null && bolt.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
            if (!onlyShell)
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET_HEAD);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
