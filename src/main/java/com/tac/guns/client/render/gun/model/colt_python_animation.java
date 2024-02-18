package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.COLTPYTHONAnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import static com.tac.guns.client.gunskin.ModelComponent.*;

public class colt_python_animation extends SkinAnimationModel {
    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        COLTPYTHONAnimationController controller = COLTPYTHONAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);
        Gun gun = ((GunItem) stack.getItem()).getGun();
        int gunRate = gun.getGeneral().getRate();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / gunRate < 0 ? 1 : MathHelper.clamp(ShootingHandler.get().getshootMsGap() / gunRate, 0, 1);

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_BODY, transformType, matrices);
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        renderComponent(stack, matrices, renderBuffer, 15728880, overlay, skin, SIGHT_LIGHT);
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_MAG, transformType, matrices);
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, MAG);
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_BODY, transformType, matrices);
//        if ((!(controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation()) &&
//                controller.isAnimationRunning()) ||
//                !(controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation()) &&
//                        controller.isAnimationRunning())) && Gun.hasAmmo(stack)) {
        final PlayerEntity player = Minecraft.getInstance().player;
        if (!SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING) && Gun.hasAmmo(stack)) {
            matrices.rotate(Vector3f.XN.rotationDegrees(-35F * (0.74F * 1.74F)));
            matrices.translate(0, (0.74F * 1.74F) * 0.135, -0.0625F * (0.74F * 1.74F));
            if (cooldownOg < 0.74) {
                matrices.rotate(Vector3f.XP.rotationDegrees(-35F * (cooldownOg * 1.74F)));
                matrices.translate(0, -(cooldownOg * 1.74F) * 0.135, 0.0625F * (cooldownOg * 1.74F));
            }
        }
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, HAMMER);
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_MAG, transformType, matrices);
        if (cooldownOg < 0.74) {
            matrices.rotate(Vector3f.ZN.rotationDegrees(-45F * (cooldownOg * 1.74F)));
            matrices.translate(1.45 * (cooldownOg * 1.74F) * 0.0625, -0.625 * (cooldownOg * 1.74F) * 0.0625, 0);
        }
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, ROTATE);
        matrices.pop();

        matrices.push();
        if ((controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation()) ||
                controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation())) &&
                transformType.isFirstPerson() && controller.isAnimationRunning()) {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_LOADER, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, LOADER);
        }
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_BULLET1, transformType, matrices);
        if (cooldownOg < 0.74) {
            matrices.rotate(Vector3f.ZN.rotationDegrees(-45F * (cooldownOg * 1.74F)));
            matrices.translate(1.45 * (cooldownOg * 1.74F) * 0.0625, -0.625 * (cooldownOg * 1.74F) * 0.0625, 0);
        }
        renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET1);
        matrices.pop();

        matrices.push();
        if ((controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL).equals(controller.getPreviousAnimation()) ||
                controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY).equals(controller.getPreviousAnimation())) &&
                transformType.isFirstPerson() && controller.isAnimationRunning()) {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), COLTPYTHONAnimationController.INDEX_BULLET2, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BULLET2);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }
}