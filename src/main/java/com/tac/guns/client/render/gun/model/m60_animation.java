package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.M60AnimationController;
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
 * Author: Timeless Development, and associates.
 */
public class m60_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        M60AnimationController controller = M60AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_BODY, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, MAG);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_CHAIN, transformType, matrices);
            if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY)
                    || Gun.hasAmmo(stack)) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET_CHAIN);

            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_CAPS, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT);
            } else {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT_FOLDED);
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, CAP);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_HANDLE, transformType, matrices);//HANDLE?
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, HANDLE);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
