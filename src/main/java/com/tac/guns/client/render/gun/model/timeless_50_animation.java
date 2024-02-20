package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.Timeless50AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
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

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class timeless_50_animation extends SkinAnimationModel {

    //The render method, similar to what is in DartEntity. We can render the item
    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        Timeless50AnimationController controller = Timeless50AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        boolean renderClumsy = Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.get() ||
                Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_COMPENSATOR.get();
        double yAdjust = 0; //Neko accidently made the .50 slightly too high in model space compared to the STI

        matrices.push();
        {
            matrices.translate(0, yAdjust, 0);
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Timeless50AnimationController.INDEX_BODY, transformType, matrices);
            if (renderClumsy) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BARREL_EXTENDED);
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, CLUMSYYY);
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.get()) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BARREL_STANDARD);
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, NEKOOO);
                matrices.translate(0, 0, -0.3125);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, MUZZLE_SILENCER);
                matrices.translate(0, 0, 0.3125);
            } else {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BARREL_STANDARD);
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, NEKOOO);
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            matrices.translate(0, yAdjust, 0);
            if (transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), Timeless50AnimationController.INDEX_SLIDE, transformType, matrices);
                Gun gun = ((GunItem) stack.getItem()).getGun();
                int gunRate = (int) Math.min(ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()), 4);
                int rateBias = (int) (ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) - gunRate);
                float cooldownOg = (ShootingHandler.get().getshootMsGap() - rateBias) / gunRate < 0 ? 1 : MathHelper.clamp((ShootingHandler.get().getshootMsGap() - rateBias) / gunRate, 0, 1);
                AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();


                //matrices.translate(0.00, 0.0, 0.035); // Issues with the slide starting out further forward, seems to be ~ a 0.035 movement
                if (Gun.hasAmmo(stack) || shouldOffset) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    double v = -4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0;
                    matrices.translate(0, 0, 0.1925f * v);
                    GunRenderingHandler.get().opticMovement = 0.1925f * v;
                } else if (!Gun.hasAmmo(stack)) {
                    matrices.translate(0, 0, 0.1925f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                }
                matrices.translate(0, 0, 0.025F);
            }
            if (renderClumsy) {
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SLIDE_EXTENDED_LIGHT);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SLIDE_EXTENDED);
            } else {
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SLIDE_LIGHT);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SLIDE);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Timeless50AnimationController.INDEX_HAMMER, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, HAMMER);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), Timeless50AnimationController.INDEX_MAG, transformType, matrices);
            if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) || controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation()))
                matrices.translate(-0.00175, 0, 0); //-0.02, 0.05);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        if (controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation()) && transformType.isFirstPerson()) {
            matrices.push();
            {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), Timeless50AnimationController.INDEX_EXTRA_MAG, transformType, matrices);
                matrices.translate(0.0, -0.1, 2.2);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
                matrices.translate(0, 0.1, -2.2);
            }
            matrices.pop();
        }

        if (!controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.INSPECT_EMPTY).equals(controller.getPreviousAnimation())) {
            matrices.push();
            {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), Timeless50AnimationController.INDEX_BULLET1, transformType, matrices);
                if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) || controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation()))
                    matrices.translate(-0.00175, 0, 0);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET1);
            }
            matrices.pop();
        }

        if (controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation()) && !controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.INSPECT_EMPTY).equals(controller.getPreviousAnimation()) && transformType.isFirstPerson()) {
            matrices.push();
            {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), Timeless50AnimationController.INDEX_BULLET2, transformType, matrices);
                matrices.translate(0, -0.1, 2.2);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET2);
                matrices.translate(0, 0.1, -2.2);
            }
            matrices.pop();
        }

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
