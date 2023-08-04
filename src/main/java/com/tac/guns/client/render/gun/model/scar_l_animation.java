package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.SCAR_LAnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
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
public class scar_l_animation extends SkinAnimationModel {

    @Override
    public void init(){
        defaultModels = new HashMap<>();

        defaultModels.put(BODY,SpecialModels.SCAR_L_BODY.getModel());

        defaultModels.put(BOLT,SpecialModels.SCAR_L_BOLT.getModel());

        defaultModels.put(SIGHT,SpecialModels.SCAR_L_FSU.getModel());
        defaultModels.put(SIGHT_FOLDED,SpecialModels.SCAR_L_FS.getModel());

        defaultModels.put(GRIP_LIGHT,SpecialModels.SCAR_L_LIGHT_GRIP.getModel());
        defaultModels.put(GRIP_TACTICAL,SpecialModels.SCAR_L_TAC_GRIP.getModel());

        defaultModels.put(LASER_BASIC,SpecialModels.SCAR_L_MINI_LASER_BEAM.getModel());
        defaultModels.put(LASER_BASIC_DEVICE,SpecialModels.SCAR_L_MINI_LASER.getModel());
        defaultModels.put(LASER_IR,SpecialModels.SCAR_L_IR_LASER.getModel());
        defaultModels.put(LASER_IR_DEVICE,SpecialModels.SCAR_L_IR_DEVICE.getModel());

        defaultModels.put(MUZZLE_DEFAULT,SpecialModels.SCAR_L_DEFAULT_BARREL.getModel());
        defaultModels.put(MUZZLE_BRAKE,SpecialModels.SCAR_L_BRAKE.getModel());
        defaultModels.put(MUZZLE_COMPENSATOR,SpecialModels.SCAR_L_COMPENSATOR.getModel());
        defaultModels.put(MUZZLE_SILENCER,SpecialModels.SCAR_L_SUPPRESSOR.getModel());

        defaultModels.put(MAG_STANDARD,SpecialModels.SCAR_L_STANDARD_MAG.getModel());
        defaultModels.put(MAG_EXTENDED,SpecialModels.SCAR_L_EXTENDED_MAG.getModel());

        extraOffset.put(MUZZLE_SILENCER,new Vector3d(0, 0, -0.0225));
    }

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        SCAR_LAnimationController controller = SCAR_LAnimationController.getInstance();

        GunSkin skin = SkinManager.getSkin(stack,"scar_l");

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), SCAR_LAnimationController.INDEX_BODY, transformType, matrices);

            renderSight(stack, matrices, renderBuffer, light, overlay, skin);

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_BASIC), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, light, overlay);
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_BASIC_DEVICE), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
            }
            else if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() != ModItems.IR_LASER.orElse(ItemStack.EMPTY.getItem()) || Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack).getItem() == ModItems.IR_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_IR_DEVICE), Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack), matrices, renderBuffer, light, overlay);
                matrices.push();
                if(transformType.isFirstPerson()) {
                    // TODO: Build some sort of scaler for this
                    matrices.translate(0, 0, -0.35);
                    matrices.scale(1, 1, 9);
                    matrices.translate(0, 0, 0.35);
                    RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_IR), Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
                }
                matrices.pop();
            }

            renderBarrelWithDefault(stack,matrices,renderBuffer,light,overlay,skin);

            RenderUtil.renderModel(getModelComponent(skin,BODY), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), SCAR_LAnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            if(transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin,BODY), SCAR_LAnimationController.INDEX_MAGAZINE2, transformType, matrices);
                renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            }
        }
        matrices.pop();

        matrices.push();
        {
            if(transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin,BODY), SCAR_LAnimationController.INDEX_BOLT, transformType, matrices);
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
            RenderUtil.renderModel(getModelComponent(skin,BOLT), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();
        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }

}
