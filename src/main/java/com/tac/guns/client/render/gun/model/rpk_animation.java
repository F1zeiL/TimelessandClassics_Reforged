package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.RPKAnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class rpk_animation extends SkinAnimationModel {

    public rpk_animation() {
        extraOffset.put(STOCK_HEAVY, new Vector3d(0, 0, 0.1835));
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        RPKAnimationController controller = RPKAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        Gun gun = ((GunItem) stack.getItem()).getGun();
        int gunRate = (int) Math.min(ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()), 4);
        int rateBias = (int) (ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) - gunRate);
        float cooldownOg = (ShootingHandler.get().getshootMsGap() - rateBias) / gunRate < 0 ? 1 : MathHelper.clamp((ShootingHandler.get().getshootMsGap() - rateBias) / gunRate, 0, 1);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), RPKAnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) != null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_SCOPE);
            }

            renderStock(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        //Always push
        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), RPKAnimationController.INDEX_BOLT, transformType, matrices);
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, 0, 0.025F);
            matrices.translate(0, 0, 0.190f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1));
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        }
        //Always pop
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), RPKAnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
            if ((controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY) &&
                    ReloadHandler.get().getReloadProgress(v, stack) > 0.5) ||
                    Gun.hasAmmo(stack)) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
            }
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
