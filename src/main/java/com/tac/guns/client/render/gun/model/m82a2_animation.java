package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.M82A2AnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class m82a2_animation extends SkinAnimationModel {

    public m82a2_animation() {
        //extraOffset.put(BARREL, new Vector3d(0, 0, -1.5));
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {

        M82A2AnimationController controller = M82A2AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        Gun gun = ((GunItem) stack.getItem()).getGun();
        int gunRate = (int) Math.min(ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()), 4);
        int rateBias = (int) (ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) - gunRate);
        float cooldownOg = (ShootingHandler.get().getshootMsGap() - rateBias) / gunRate < 0 ? 1 : MathHelper.clamp((ShootingHandler.get().getshootMsGap() - rateBias) / gunRate, 0, 1);

        matrices.push();
        double v1 = -4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0;
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M82A2AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT);
            } else {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SIGHT_FOLDED);
            }

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);

            //matrices.push();
            if (transformType.isFirstPerson()) {
                matrices.translate(0, 0, 0.375f * v1);
                matrices.translate(0, 0, 0.025F);
            }
            matrices.translate(0, 0, -1.5F);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BARREL);
            matrices.translate(0, 0, 1.5F);
            //matrices.pop();
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M82A2AnimationController.INDEX_BOLT, transformType, matrices);
            if (transformType.isFirstPerson()) {
                matrices.translate(0, 0, 0.375f * v1);
                matrices.translate(0, 0, 0.025F);
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M82A2AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M82A2AnimationController.INDEX_BULLET, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M82A2AnimationController.INDEX_HANDLE, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, HANDLE);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
