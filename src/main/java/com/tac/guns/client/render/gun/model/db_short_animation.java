package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.DBShotgunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import static com.tac.guns.client.gunskin.ModelComponent.*;

public class db_short_animation extends SkinAnimationModel {
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        DBShotgunAnimationController controller = DBShotgunAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), DBShotgunAnimationController.INDEX_REAR, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), DBShotgunAnimationController.INDEX_FRONT, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BARREL);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), DBShotgunAnimationController.INDEX_LEVER, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, HAMMER);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), DBShotgunAnimationController.INDEX_BULLET1, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET1);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), DBShotgunAnimationController.INDEX_BULLET2, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET2);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}
