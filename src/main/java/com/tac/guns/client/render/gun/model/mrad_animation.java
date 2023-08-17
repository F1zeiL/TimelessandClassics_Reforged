package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.MRADAnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

import static com.tac.guns.client.gunskin.ModelComponent.*;

public class mrad_animation extends SkinAnimationModel {

    public mrad_animation() {
        extraOffset.put(BARREL, new Vector3d(0, 0, -0.55));
        extraOffset.put(BIPOD, new Vector3d(0, 0, -0.25));
        extraOffset.put(LASER_BASIC, new Vector3d(0, 0, -0.3));
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        MRADAnimationController controller = MRADAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MRADAnimationController.INDEX_BODY, transformType, matrices);

            renderSight(stack, matrices, renderBuffer, light, overlay, skin);

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            RenderUtil.renderModel(getModelComponent(skin, BARREL), stack, matrices, renderBuffer, light, overlay);
            RenderUtil.renderModel(getModelComponent(skin, BIPOD), stack, matrices, renderBuffer, light, overlay);

            renderLaserDevice(stack, matrices, renderBuffer, light, overlay, skin);

            if (transformType.isFirstPerson() || Config.COMMON.gameplay.canSeeLaserThirdSight.get())
                renderLaser(stack, matrices, renderBuffer, light, overlay, skin);

            RenderUtil.renderModel(getModelComponent(skin, BODY), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MRADAnimationController.INDEX_HANDLE, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin, BOLT_EXTRA), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MRADAnimationController.INDEX_BOLT, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin, BOLT), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MRADAnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            if (controller.isAnimationRunning()) {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), MRADAnimationController.INDEX_BULLET, transformType, matrices);
                if (controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.PULL_BOLT).equals(controller.getPreviousAnimation())) {
                    RenderUtil.renderModel(getModelComponent(skin, BULLET_SHELL), stack, matrices, renderBuffer, light, overlay);
                } else {
                    RenderUtil.renderModel(getModelComponent(skin, BULLET), stack, matrices, renderBuffer, light, overlay);
                }
            }
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}