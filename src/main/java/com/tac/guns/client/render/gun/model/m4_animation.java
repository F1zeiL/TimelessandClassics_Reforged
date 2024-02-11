package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.M4AnimationController;
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

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class m4_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        M4AnimationController controller = M4AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M4AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, CARRY);
            }

            renderStock(stack, matrices, renderBuffer, light, overlay, skin);

            renderLaserDevice(stack, matrices, renderBuffer, light, overlay, skin);

            if (transformType.isFirstPerson() || Config.COMMON.gameplay.canSeeLaserThirdSight.get())
                renderLaser(stack, matrices, renderBuffer, light, overlay, skin);

            // If niether trips, render the cover for the side or top, since only one is accessible at once currently, TODO: Have a more streamlined system to handle multi-accesible attachments
            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.get()) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_EXTENDED_SIDE);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_COVER_TOP);
            } else if (Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack) != ItemStack.EMPTY && Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack).getItem() == ModItems.IR_LASER.get()) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_EXTENDED_TOP);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_COVER_SIDE);
            }

            if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack) == ItemStack.EMPTY && Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack) == ItemStack.EMPTY && Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack) == ItemStack.EMPTY) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_DEFAULT);
            } else {
                if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack) == ItemStack.EMPTY && Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack) == ItemStack.EMPTY) {
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_COVER_TOP);
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_COVER_SIDE);
                }
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_EXTENDED);
            }

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);

            matrices.push();
            {
                if (transformType.isFirstPerson()) {
                    Gun gun = ((GunItem) stack.getItem()).getGun();
                    float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

                    if (Gun.hasAmmo(stack)) {
                        // Math provided by Bomb787 on GitHub and Curseforge!!!
                        matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                    } else if (!Gun.hasAmmo(stack)) {
                        {
                            matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                        }
                    }
                }
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
            }
            matrices.pop();
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), M4AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();
        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
