package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.HkMp5a5AnimationController;
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

import static com.tac.guns.client.gunskin.ModelComponent.*;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class hk_mp5a5_animation extends SkinAnimationModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        HkMp5a5AnimationController controller = HkMp5a5AnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HkMp5a5AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) != null) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_SCOPE);
            }

            renderStockWithDefault(stack, matrices, renderBuffer, light, overlay, skin);

            if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack) == ItemStack.EMPTY && Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack) == ItemStack.EMPTY) {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_DEFAULT);
            } else {
                renderComponent(stack, matrices, renderBuffer, light, overlay, skin, RAIL_EXTENDED);
            }

            renderGrip(stack, matrices, renderBuffer, light, overlay, skin);

            if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.BASIC_LASER.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderLaserModuleModel(getModelComponent(skin, LASER_BASIC_DEVICE), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, light, overlay);
                RenderUtil.renderLaserModuleModel(getModelComponent(skin, LASER_BASIC), Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack), matrices, renderBuffer, 15728880, overlay); // 15728880 For fixed max light
            }

            renderBarrel(stack, matrices, renderBuffer, light, overlay, skin);

            matrices.push();
            if (transformType.isFirstPerson()) {
                Gun gun = ((GunItem) stack.getItem()).getGun();
                float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

                matrices.translate(0, 0, 0.085f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
            }
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT);
            matrices.pop();

            renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HkMp5a5AnimationController.INDEX_MAGAZINE, transformType, matrices);
            renderMag(stack, matrices, renderBuffer, light, overlay, skin);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), HkMp5a5AnimationController.INDEX_BOLT, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BOLT_HANDLE);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
