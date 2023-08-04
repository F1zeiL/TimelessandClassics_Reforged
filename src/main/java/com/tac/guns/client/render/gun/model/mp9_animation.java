package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.MP9AnimationController;
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

import java.util.HashMap;

import static com.tac.guns.gunskins.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class mp9_animation extends SkinAnimationModel {
    @Override
    public void init() {
        defaultModels = new HashMap<>();

        defaultModels.put(BODY,SpecialModels.MP9.getModel());

        defaultModels.put(LASER_BASIC,SpecialModels.MP9_B_LASER.getModel());
        defaultModels.put(LASER_BASIC_DEVICE,SpecialModels.MP9_B_LASER_DEVICE.getModel());

        defaultModels.put(STOCK_DEFAULT,SpecialModels.MP9_STOCK_FOLDED.getModel());
        defaultModels.put(STOCK_ANY,SpecialModels.MP9_STOCK_EXTENDED.getModel());

        defaultModels.put(MAG_STANDARD,SpecialModels.MP9_STANDARD_MAG.getModel());
        defaultModels.put(MAG_EXTENDED,SpecialModels.MP9_EXTENDED_MAG.getModel());

        defaultModels.put(HANDLE,SpecialModels.MP9_HANDLE.getModel());
        defaultModels.put(BOLT,SpecialModels.MP9_BOLT.getModel());

        defaultModels.put(BULLET,SpecialModels.MP9_BULLET.getModel());

    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        MP9AnimationController controller = MP9AnimationController.getInstance();

        GunSkin skin = SkinManager.getSkin(stack,"mp9");

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), MP9AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_BASIC_DEVICE), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, light, overlay);
                RenderUtil.renderLaserModuleModel(getModelComponent(skin,LASER_BASIC), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
            }
            if (Gun.getScope(stack) != null) {
                RenderUtil.renderModel(getModelComponent(skin,STOCK_ANY), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(getModelComponent(skin,STOCK_DEFAULT), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(getModelComponent(skin,BODY), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), MP9AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), MP9AnimationController.INDEX_HANDLE,transformType,matrices);
            RenderUtil.renderModel(getModelComponent(skin,HANDLE), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), MP9AnimationController.INDEX_BULLET,transformType,matrices);
            RenderUtil.renderModel(getModelComponent(skin,BULLET), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        if(transformType.isFirstPerson()) {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), MP9AnimationController.INDEX_BODY, transformType, matrices);
            Gun gun = ((GunItem) stack.getItem()).getGun();
            float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

            if (Gun.hasAmmo(stack)) {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.135f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
            } else if (!Gun.hasAmmo(stack)) {
                {
                    matrices.translate(0, 0, 0.135f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                }
            }
            matrices.translate(0, 0, 0.025F);
        }
        RenderUtil.renderModel(getModelComponent(skin,BOLT), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();

        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
