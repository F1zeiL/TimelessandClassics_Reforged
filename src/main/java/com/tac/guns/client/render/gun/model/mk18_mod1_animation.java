package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.MK18MOD1AnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;

import static com.tac.guns.client.gunskin.ModelComponent.BODY;
import static com.tac.guns.client.gunskin.ModelComponent.BOLT;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class mk18_mod1_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        MK18MOD1AnimationController controller = MK18MOD1AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MK18MOD1AnimationController.INDEX_BODY, transformType, matrices);

            renderLaserDevice(stack, matrices, renderBuffer, light, overlay, skin);

            if (transformType.isFirstPerson() || Config.COMMON.gameplay.canSeeLaserThirdSight.get())
                renderLaser(stack, matrices, renderBuffer, light, overlay, skin);

            renderSight(stack, matrices, renderBuffer, light, overlay, skin);

            renderStock(stack, matrices, renderBuffer, light, overlay, skin);

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);

            matrices.push();
            {
                if (transformType.isFirstPerson()) {
                    CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
                    float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());

                    if (Gun.hasAmmo(stack)) {
                        // Math provided by Bomb787 on GitHub and Curseforge!!!
                        matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                    } else if (!Gun.hasAmmo(stack)) {
                        matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                }
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
            }
            matrices.pop();
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MK18MOD1AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
