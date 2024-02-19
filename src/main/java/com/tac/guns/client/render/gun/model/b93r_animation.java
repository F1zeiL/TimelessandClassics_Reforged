package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.B93RAnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import static com.tac.guns.client.gunskin.ModelComponent.*;

/**
 * Author: Timeless Development, and associates.
 */
public class b93r_animation extends SkinAnimationModel {

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        B93RAnimationController controller = B93RAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), B93RAnimationController.INDEX_BODY, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), B93RAnimationController.INDEX_MAG, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), B93RAnimationController.INDEX_BULLET, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
        }
        matrices.pop();

        // reload norm mag
        if (transformType.isFirstPerson() && (controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) || controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_NORMAL))) {
            matrices.push();
            {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), B93RAnimationController.INDEX_EXTRA_MAG, transformType, matrices);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            }
            matrices.pop();

            matrices.push();
            {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), B93RAnimationController.INDEX_EXTRA_BULLET, transformType, matrices);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
            }
            matrices.pop();
        }

        //Always push
        matrices.push();
        if (transformType.isFirstPerson()) {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), B93RAnimationController.INDEX_SLIDE, transformType, matrices);
            Gun gun = ((GunItem) stack.getItem()).getGun();
            int gunRate = (int) Math.min(ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()), 4);
            int rateBias = (int) (ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) - gunRate);
            float cooldownOg = (ShootingHandler.get().getshootMsGap() - rateBias) / gunRate < 0 ? 1 : MathHelper.clamp((ShootingHandler.get().getshootMsGap() - rateBias) / gunRate, 0, 1);

            AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
            boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
            if (Gun.hasAmmo(stack) || shouldOffset) {
                double v = -4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0;
                matrices.translate(0, 0, 0.185f * v);
                GunRenderingHandler.get().opticMovement = 0.185f * v;
            } else if (!Gun.hasAmmo(stack)) {
                double z = 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0);
                matrices.translate(0, 0, z);
                GunRenderingHandler.get().opticMovement = z;
            }
        }
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SLIDE);
        renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SLIDE_LIGHT);

        //Always pop
        matrices.pop();
        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
