package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.Type81AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.item.GunItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import com.tac.guns.util.GunModifierHelper;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class type81_x_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        
        Type81AnimationController controller = Type81AnimationController.getInstance();
        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.TYPE81_X.getModel(),Type81AnimationController.INDEX_BODY,transformType,matrices);
            if (Gun.getScope(stack) != null) {
                RenderUtil.renderModel(SpecialModels.TYPE81_X_MOUNT.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(SpecialModels.TYPE81_X.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.TYPE81_X.getModel(),Type81AnimationController.INDEX_MAGAZINE,transformType,matrices);

            if(GunModifierHelper.getAmmoCapacityWeight(stack) > -1)
            {
                RenderUtil.renderModel(SpecialModels.TYPE81_X_EXT_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else
            {
                RenderUtil.renderModel(SpecialModels.TYPE81_X_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

        }
        matrices.pop();

        //Always push
        matrices.push();
        {
            if(transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(SpecialModels.TYPE81_X.getModel(), Type81AnimationController.INDEX_BOLT, transformType, matrices);
                AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
                if (Gun.hasAmmo(stack) || shouldOffset) {
                    matrices.translate(0, 0, 0.280f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                    GunRenderingHandler.get().opticMovement = 0.280f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
                } else if (!Gun.hasAmmo(stack)) {
                    {
                        matrices.translate(0, 0, 0.280f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                        GunRenderingHandler.get().opticMovement = 0.280f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0);
                    }
                }
                matrices.translate(0, 0, 0.025F);
            }
            RenderUtil.renderModel(SpecialModels.TYPE81_X_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }

     

    //TODO comments
}
