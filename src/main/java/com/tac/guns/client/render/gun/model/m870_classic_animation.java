package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.M870AnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.util.GunModifierHelper;
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
public class m870_classic_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        M870AnimationController controller = M870AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M870AnimationController.INDEX_BODY, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M870AnimationController.INDEX_PUMP, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, PUMP);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M870AnimationController.INDEX_BULLET, transformType, matrices);
            if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.PUMP) || controller.isAnimationRunning(GunAnimationController.AnimationLabel.INSPECT) || controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_LOOP))
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M870AnimationController.INDEX_BODY, transformType, matrices);
            if (GunModifierHelper.getAmmoCapacityWeight(stack) > -1) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, MAG_EXTENDED);
            }
        }
        matrices.pop();
        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
