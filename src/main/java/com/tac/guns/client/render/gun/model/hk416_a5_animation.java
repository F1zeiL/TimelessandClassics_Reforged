package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.HK416A5AnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.gunskins.GunSkin;
import com.tac.guns.gunskins.SkinManager;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;

import static com.tac.guns.gunskins.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class hk416_a5_animation extends SkinAnimationModel {
//    public void init(){
//        defaultModels = new HashMap<>();
//        defaultModels.put(BODY,SpecialModels.HK416_A5_BODY.getModel());
//
//        defaultModels.put(BOLT,SpecialModels.HK416_A5_BOLT.getModel());
//        defaultModels.put(BULLET,SpecialModels.HK416_A5_BULLET.getModel());
//
//        defaultModels.put(SIGHT,SpecialModels.HK416_A5_UNFOLDED.getModel());
//        defaultModels.put(SIGHT_FOLDED,SpecialModels.HK416_A5_FOLDED.getModel());
//
//        defaultModels.put(LASER_BASIC,SpecialModels.HK416_A5_B_LASER.getModel());
//        defaultModels.put(LASER_BASIC_DEVICE,SpecialModels.HK416_A5_B_LASER_DEVICE.getModel());
//        defaultModels.put(LASER_IR,SpecialModels.HK416_A5_IR_LASER.getModel());
//        defaultModels.put(LASER_IR_DEVICE,SpecialModels.HK416_A5_IR_LASER_DEVICE.getModel());
//
//        defaultModels.put(GRIP_LIGHT,SpecialModels.HK416_A5_LIGHT_GRIP.getModel());
//        defaultModels.put(GRIP_TACTICAL,SpecialModels.HK416_A5_TACTICAL_GRIP.getModel());
//
//        defaultModels.put(STOCK_LIGHT,SpecialModels.HK416_A5_LIGHT_STOCK.getModel());
//        defaultModels.put(STOCK_TACTICAL,SpecialModels.HK416_A5_TACTICAL_STOCK.getModel());
//        defaultModels.put(STOCK_HEAVY,SpecialModels.HK416_A5_HEAVY_STOCK.getModel());
//
//        defaultModels.put(MUZZLE_DEFAULT,SpecialModels.HK416_A5_DEFAULT_MUZZLE.getModel());
//        defaultModels.put(MUZZLE_SILENCER,SpecialModels.HK416_A5_SUPPRESSOR.getModel());
//        defaultModels.put(MUZZLE_COMPENSATOR,SpecialModels.HK416_A5_COMPENSATOR.getModel());
//        defaultModels.put(MUZZLE_BRAKE,SpecialModels.HK416_A5_BRAKE.getModel());
//
//        defaultModels.put(MAG_EXTENDED,SpecialModels.HK416_A5_EXTENDED_MAG.getModel());
//        defaultModels.put(MAG_STANDARD,SpecialModels.HK416_A5_STANDARD_MAG.getModel());
//
//        extraOffset.put(MUZZLE_SILENCER,new Vector3d(0, 0, -0.0125));
//    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        HK416A5AnimationController controller = HK416A5AnimationController.getInstance();

        GunSkin skin = SkinManager.getSkin(stack, "hk416_a5");

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY),HK416A5AnimationController.INDEX_BODY,transformType,matrices);

            renderSight(stack, matrices, renderBuffer, light, overlay, skin);

            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_BASIC_DEVICE), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, light, overlay);
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_BASIC), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
            }
            else if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() != ModItems.IR_LASER.orElse(ItemStack.EMPTY.getItem()) || Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack).getItem() == ModItems.IR_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_IR_DEVICE), Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack), matrices, renderBuffer, light, overlay);
                if(transformType.isFirstPerson()) {
                    RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_IR), Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
                }
            }

            renderStock(stack, matrices, renderBuffer, light, overlay, skin);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            RenderUtil.renderModel(getModelComponent(skin,BODY), stack, matrices, renderBuffer, light, overlay);

            matrices.push();
            {
                if(transformType.isFirstPerson()) {
                    Gun gun = ((GunItem) stack.getItem()).getGun();
                    float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

                    if (Gun.hasAmmo(stack)) {
                        // Math provided by Bomb787 on GitHub and Curseforge!!!
                        matrices.translate(0, 0, 0.205f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                    } else if (!Gun.hasAmmo(stack)) {
                        matrices.translate(0, 0, 0.205f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                    matrices.translate(0, 0, 0.025F);
                }
            RenderUtil.renderModel(getModelComponent(skin,BOLT), stack, matrices, renderBuffer, light, overlay);
            }
            matrices.pop();
        }
        matrices.pop();
        /*if(Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.STANDARD_FLASHLIGHT.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_CQB_STANDARD_FLASHLIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }*/

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY),HK416A5AnimationController.INDEX_MAGAZINE,transformType,matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        //if(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_NORMAL)) {
        matrices.push();
        {
            if(transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin,BODY), HK416A5AnimationController.INDEX_EXTRA_MAGAZINE, transformType, matrices);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            }
        }
        matrices.pop();

        matrices.push();
        {
            if(transformType.isFirstPerson() && controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation())) {
                controller.applySpecialModelTransform(getModelComponent(skin,BODY), HK416A5AnimationController.INDEX_MAGAZINE, transformType, matrices);
                RenderUtil.renderModel(getModelComponent(skin,BULLET), stack, matrices, renderBuffer, light, overlay);
            } else if (transformType.isFirstPerson() && controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation())) {
                controller.applySpecialModelTransform(getModelComponent(skin,BODY), HK416A5AnimationController.INDEX_EXTRA_MAGAZINE, transformType, matrices);
                RenderUtil.renderModel(getModelComponent(skin,BULLET), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();
        //}
        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }

}
