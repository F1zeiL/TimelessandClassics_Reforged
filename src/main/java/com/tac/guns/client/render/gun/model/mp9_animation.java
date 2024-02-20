package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.MP9AnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Objects;

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class mp9_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        MP9AnimationController controller = MP9AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MP9AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin, LASER_BASIC_DEVICE), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, light, overlay);
                if (transformType.isFirstPerson())
                    RenderUtil.renderLaserModuleModel(getModelComponent(skin, LASER_BASIC), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
            }
            if (Gun.getScope(stack) != null) {
                if (Objects.equals(GunModifierHelper.getAdditionalSkin(stack), new ResourceLocation("tac:thunder")))
                    renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, LIGHT);
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, STOCK_DEFAULT);
            } else {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, STOCK_FOLDED);
            }
            if (Objects.equals(GunModifierHelper.getAdditionalSkin(stack), new ResourceLocation("tac:thunder"))) {
                renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
                if (stack.getTag().getInt("CurrentFireMode") == 1) {
                    matrices.push();
                    matrices.rotate(Vector3f.XN.rotationDegrees(20F));
                    matrices.translate(0, 0.004, -0.02);
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SAFETY);
                    matrices.rotate(Vector3f.XP.rotationDegrees(20F));
                    matrices.translate(0, -0.004, 0.02);
                    matrices.pop();
                } else
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, SAFETY);
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MP9AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MP9AnimationController.INDEX_HANDLE, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, HANDLE);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MP9AnimationController.INDEX_BULLET, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
        }
        matrices.pop();

        matrices.push();
        if (transformType.isFirstPerson()) {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MP9AnimationController.INDEX_BODY, transformType, matrices);
            Gun gun = ((GunItem) stack.getItem()).getGun();
            int gunRate = (int) Math.min(ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()), 4);
            int rateBias = (int) (ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) - gunRate);
            float cooldownOg = (ShootingHandler.get().getshootMsGap() - rateBias) / gunRate < 0 ? 1 : MathHelper.clamp((ShootingHandler.get().getshootMsGap() - rateBias) / gunRate, 0, 1);

            if (Gun.hasAmmo(stack)) {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.135f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
            } else if (!Gun.hasAmmo(stack)) {
                matrices.translate(0, 0, 0.135f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
            }
            matrices.translate(0, 0, 0.025F);
        }
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
