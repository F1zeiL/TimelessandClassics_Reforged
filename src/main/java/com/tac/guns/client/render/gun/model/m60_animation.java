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
            RenderUtil.renderModel(getModelComponent(skin, BODY), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_MAGAZINE, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin, MAG), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_CHAIN, transformType, matrices);
            if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY)
                    || Gun.hasAmmo(stack)) {
                RenderUtil.renderModel(getModelComponent(skin, BULLET_CHAIN), stack, matrices, renderBuffer, light, overlay);

            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_CAPS, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                RenderUtil.renderModel(getModelComponent(skin, SIGHT), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(getModelComponent(skin, SIGHT_FOLDED), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(getModelComponent(skin, CAP), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M60AnimationController.INDEX_HANDLE, transformType, matrices);//HANDLE?
            RenderUtil.renderModel(getModelComponent(skin, HANDLE), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
