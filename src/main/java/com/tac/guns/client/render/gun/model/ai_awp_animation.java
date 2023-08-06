package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.render.animation.AWPAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.gunskins.GunSkin;
import com.tac.guns.gunskins.SkinManager;
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
 * Author: ClumsyAlien, codebase and design based off Mr.Pineapple's original addon
 */
public class ai_awp_animation extends SkinAnimationModel {

    /*
        I plan on making a very comprehensive description on my render / rendering methods, currently I am unable to give a good explanation on each part and will be supplying one later one in development!

        If you are just starting out I don't recommend attempting to create an animated part of your weapon is as much as I can comfortably give at this point!
    */
//    @Override
//    public void init(){
//        defaultModels = new HashMap<>();
//        defaultModels.put(BODY,SpecialModels.AI_AWP.getModel());
//        defaultModels.put(BOLT,SpecialModels.AI_AWP_BOLT.getModel());
//        defaultModels.put(BOLT_EXTRA,SpecialModels.AI_AWP_BOLT_EXTRA.getModel());
//        defaultModels.put(SIGHT,SpecialModels.AI_AWP_SIGHT.getModel());
//
//        defaultModels.put(MUZZLE_SILENCER,SpecialModels.AI_AWP_SUPPRESSOR.getModel());
//        defaultModels.put(MUZZLE_COMPENSATOR,SpecialModels.AI_AWP_COMPENSATOR.getModel());
//        defaultModels.put(MUZZLE_BRAKE,SpecialModels.AI_AWP_BRAKE.getModel());
//
//        defaultModels.put(MAG_EXTENDED,SpecialModels.AI_AWP_MAG_EXTENDED.getModel());
//        defaultModels.put(MAG_STANDARD,SpecialModels.AI_AWP_MAG.getModel());
//
//        defaultModels.put(BULLET_SHELL,SpecialModels.AI_AWP_BULLET_SHELL.getModel());
//
//        extraOffset.put(MUZZLE_SILENCER,new Vector3d(0,0,-0.4));
//    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        AWPAnimationController controller = AWPAnimationController.getInstance();

        GunSkin skin = SkinManager.getSkin(stack, "ai_awp");

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), AWPAnimationController.INDEX_BODY,transformType,matrices);
            if (Gun.getScope(stack) == null) {
                RenderUtil.renderModel(getModelComponent(skin,SIGHT), stack, matrices, renderBuffer, light, overlay);
            }

            renderBarrel(stack, matrices, renderBuffer, light, overlay, skin);

            RenderUtil.renderModel(getModelComponent(skin,BODY), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), AWPAnimationController.INDEX_HANDLE, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin,BOLT), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), AWPAnimationController.INDEX_BOLT, transformType, matrices);
            RenderUtil.renderModel(getModelComponent(skin,BOLT_EXTRA), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin,BODY), AWPAnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            if(controller.isAnimationRunning()) {
                controller.applySpecialModelTransform(getModelComponent(skin,BODY), AWPAnimationController.INDEX_BULLET, transformType, matrices);
                RenderUtil.renderModel(getModelComponent(skin,BULLET_SHELL), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
