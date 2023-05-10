package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.SCAR_MK20AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class scar_mk20_animation implements IOverrideModel {

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        SCAR_MK20AnimationController controller = SCAR_MK20AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.SCAR_MK20_BODY.getModel(), SCAR_MK20AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_FSU.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_FS.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_LIGHT_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);

            } else if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_TAC_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.orElse(ItemStack.EMPTY.getItem())) {
                matrices.push();
                matrices.translate(0, 0, -0.125f);
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
                matrices.translate(0, 0, 0.125f);
                matrices.pop();
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_COMPENSATOR.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_COMPENSATOR.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_BRAKE.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_DEFAULT_BARREL.getModel(), stack, matrices, renderBuffer, light, overlay);

            RenderUtil.renderModel(SpecialModels.SCAR_MK20_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);

            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(SpecialModels.SCAR_MK20_B_LASER.getModel(), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, light, overlay);
                RenderUtil.renderLaserModuleModel(SpecialModels.SCAR_MK20_B_LASER_BEAM.getModel(), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
            }
            else if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() != ModItems.IR_LASER.orElse(ItemStack.EMPTY.getItem()) || Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack).getItem() == ModItems.IR_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(SpecialModels.SCAR_MK20_IR_LASER_DEVICE.getModel(), Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack), matrices, renderBuffer, light, overlay);
                if(transformType.isFirstPerson()) {
                    RenderUtil.renderLaserModuleModel(SpecialModels.SCAR_MK20_IR_LASER.getModel(), Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
                }
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.SCAR_MK20_BODY.getModel(), SCAR_MK20AnimationController.INDEX_MAGAZINE, transformType, matrices);
            if (GunModifierHelper.getAmmoCapacity(stack) > -1) {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.SCAR_MK20_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {
            if(transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(SpecialModels.SCAR_MK20_BODY.getModel(), SCAR_MK20AnimationController.INDEX_MAGAZINE2, transformType, matrices);
                if (GunModifierHelper.getAmmoCapacity(stack) > -1) {
                    RenderUtil.renderModel(SpecialModels.SCAR_MK20_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
                } else {
                    RenderUtil.renderModel(SpecialModels.SCAR_MK20_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
                }
            }
        }
        matrices.pop();

        matrices.push();
        {
            if(transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(SpecialModels.SCAR_MK20_BODY.getModel(), SCAR_MK20AnimationController.INDEX_BOLT, transformType, matrices);
                Gun gun = ((GunItem) stack.getItem()).getGun();
                float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

                AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
                if (Gun.hasAmmo(stack) || shouldOffset) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.225f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                } else if (!Gun.hasAmmo(stack)) {
                    {
                        matrices.translate(0, 0, 0.225f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                }
                matrices.translate(0, 0, 0.025F);
            }
            RenderUtil.renderModel(SpecialModels.SCAR_MK20_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();
        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}