package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.HK_G3AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
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
import net.minecraft.util.math.vector.Vector3d;

import static com.tac.guns.client.gunskin.ModelComponent.*;

public class hk_g3_animation extends SkinAnimationModel {

    public hk_g3_animation() {
        extraOffset.put(MUZZLE_SILENCER, new Vector3d(0, 0, -0.485));
        extraOffset.put(MUZZLE_COMPENSATOR, new Vector3d(0, 0, -0.11));
        extraOffset.put(MUZZLE_DEFAULT, new Vector3d(0, 0, -0.11));
        extraOffset.put(MUZZLE_BRAKE, new Vector3d(0, 0, -0.11));
    }

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        HK_G3AnimationController controller = HK_G3AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK_G3AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) != null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_SCOPE);
            }

            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin, LASER_BASIC_DEVICE), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, light, overlay);
                RenderUtil.renderLaserModuleModel(getModelComponent(skin, LASER_BASIC), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_EXTENDED);
            } else {
                if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem()) ||
                        Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem())) {
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_EXTENDED);
                } else {
                    renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_DEFAULT);
                }
            }

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            renderBarrelWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            renderStockWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK_G3AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            if (transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK_G3AnimationController.INDEX_BOLT, transformType, matrices);
                Gun gun = ((GunItem) stack.getItem()).getGun();
                float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

                AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
                if (Gun.hasAmmo(stack) || shouldOffset) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.175f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                } else if (!Gun.hasAmmo(stack)) {
                    {
                        matrices.translate(0, 0, 0.175f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                }
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
        }
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK_G3AnimationController.INDEX_PULL, transformType, matrices);
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, PULL);
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK_G3AnimationController.INDEX_HANDLE, transformType, matrices);
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, HANDLE);
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), HK_G3AnimationController.INDEX_BULLET, transformType, matrices);
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET);
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}