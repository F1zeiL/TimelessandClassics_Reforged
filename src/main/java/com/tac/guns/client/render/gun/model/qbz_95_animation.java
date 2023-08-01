package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.Type95LAnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.gunskins.GunSkin;
import com.tac.guns.item.GunItem;
import com.tac.guns.util.GunModifierHelper;
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
public class qbz_95_animation extends SkinAnimationModel {
    @Override
    public void init(){
        defaultModels = new HashMap<>();
        defaultModels.put(BODY,SpecialModels.QBZ_95_BODY.getModel());
        defaultModels.put(BOLT,SpecialModels.QBZ_95_BOLT.getModel());

        defaultModels.put(MUZZLE_DEFAULT,SpecialModels.QBZ_95_DEFAULT_MUZZLE.getModel());
        defaultModels.put(MUZZLE_SILENCER,SpecialModels.QBZ_95_SUPPRESSOR.getModel());
        defaultModels.put(MUZZLE_COMPENSATOR,SpecialModels.QBZ_95_COMPENSATOR.getModel());
        defaultModels.put(MUZZLE_BRAKE,SpecialModels.QBZ_95_BRAKE.getModel());

        defaultModels.put(MAG_STANDARD,SpecialModels.QBZ_95_MAG.getModel());
        defaultModels.put(MAG_DRUM,SpecialModels.QBZ_95_DRUM_MAG.getModel());
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {

        Type95LAnimationController controller = Type95LAnimationController.getInstance();

        GunSkin skin = getGunSkin(stack, "qbz95");

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), Type95LAnimationController.INDEX_BODY,transformType,matrices);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            RenderUtil.renderModel(getModelComponent(skin,BODY), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), Type95LAnimationController.INDEX_MAGAZINE,transformType,matrices);
            if(GunModifierHelper.getAmmoCapacity(stack) > -1)
            {
                RenderUtil.renderModel(getModelComponent(skin,MAG_DRUM), stack, matrices, renderBuffer, light, overlay);
            }
            else
            {
                RenderUtil.renderModel(getModelComponent(skin,MAG_STANDARD), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), Type95LAnimationController.INDEX_BOLT,transformType,matrices);

            Gun gun = ((GunItem) stack.getItem()).getGun();
            float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

            if(transformType.isFirstPerson()) {
                AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
                if (Gun.hasAmmo(stack) || shouldOffset) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                } else if (!Gun.hasAmmo(stack)) {
                    matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                }
            }
            RenderUtil.renderModel(getModelComponent(skin,BOLT), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
