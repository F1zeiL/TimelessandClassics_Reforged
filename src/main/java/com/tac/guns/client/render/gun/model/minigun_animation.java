package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.client.audio.BarrelWhineSound;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.client.render.animation.MINIGUNAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModSounds;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.WeakHashMap;

import static com.tac.guns.client.gunskin.ModelComponent.BARREL;
import static com.tac.guns.client.gunskin.ModelComponent.BODY;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class minigun_animation extends SkinAnimationModel {

    public minigun_animation() {
        extraOffset.put(BARREL, new Vector3d(0, 0, -0.525));
    }
    private WeakHashMap<LivingEntity, Rotations> rotationMap = new WeakHashMap<>();
    private BarrelWhineSound barrel;

    @Override
    public void tick(PlayerEntity entity)
    {
        this.rotationMap.putIfAbsent(entity, new Rotations());
        Rotations rotations = this.rotationMap.get(entity);
        rotations.prevRotation = rotations.rotation;

        boolean shooting = SyncedPlayerData.instance().get(entity, ModSyncedDataKeys.SHOOTING);
        ItemStack heldItem = entity.getHeldItemMainhand();
        if (!Gun.hasAmmo(heldItem) && ((!entity.isCreative() && !Config.SERVER.gameplay.commonUnlimitedCurrentAmmo.get()) ||
                entity.isCreative() && !Config.SERVER.gameplay.creativeUnlimitedCurrentAmmo.get())) {
            shooting = false;
        }

        if (shooting) {
            rotations.rotation += 60;
            if (heldItem.getItem() instanceof TimelessGunItem && heldItem.getTag() != null) {
                Gun gun = ((TimelessGunItem) heldItem.getItem()).getGun();
                if (gun.getReloads().isHeat() && heldItem.getTag().get("heatValue") != null) {
                    heldItem.getTag().putInt("heatValue", heldItem.getTag().getInt("heatValue") + 1);
                }
                if (heldItem.getTag().getInt("heatValue") >= gun.getReloads().getTickToHeat() && heldItem.getTag().get("overHeatLock") != null) {
                    heldItem.getTag().putInt("heatValue", heldItem.getTag().getInt("heatValue") + gun.getReloads().getTickOverHeat());
                    heldItem.getTag().putBoolean("overHeatLock", true);
                }
            }
        } else {
            rotations.rotation += 30;
            if (heldItem.getItem() instanceof TimelessGunItem && heldItem.getTag() != null) {
                Gun gun = ((TimelessGunItem) heldItem.getItem()).getGun();
                if (gun.getReloads().isHeat() && heldItem.getTag().get("heatValue") != null) {
                    heldItem.getTag().putInt("heatValue", Math.max(heldItem.getTag().getInt("heatValue") - 1, 0));
                }
                if (heldItem.getTag().get("overHeatLock") != null) {
                    if (heldItem.getTag().getInt("heatValue") <= 0 && heldItem.getTag().getBoolean("overHeatLock"))
                        heldItem.getTag().putBoolean("overHeatLock", false);
                }
            }
        }

        if (heldItem.getItem() instanceof TimelessGunItem && heldItem.getTag() != null) {
            Gun gun = ((TimelessGunItem) heldItem.getItem()).getGun();
            if (overHeat(entity, heldItem))
                if (heldItem.getTag().getInt("heatValue") >= gun.getReloads().getTickToHeat())
                    entity.sendStatusMessage(new TranslationTextComponent("info.tac.over_heat").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.RED), true);
                else
                    entity.sendStatusMessage(new TranslationTextComponent(heldItem.getTag().getInt("heatValue") * 100 / gun.getReloads().getTickToHeat() + "% / 100%").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.RED), true);
            else
                entity.sendStatusMessage(new TranslationTextComponent("" + (heldItem.getTag().getInt("heatValue") * 100 / gun.getReloads().getTickToHeat()) + "% / 100%").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.WHITE), true);
        }

        if ((this.barrel == null || !Minecraft.getInstance().getSoundHandler().isPlaying(this.barrel))) {
            this.barrel = new BarrelWhineSound(ModSounds.BARREL_WHINE.get(), SoundCategory.MASTER, entity);
            Minecraft.getInstance().getSoundHandler().play(this.barrel);
        }
    }

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        MINIGUNAnimationController controller = MINIGUNAnimationController.getInstance();
        GunSkin skin = SkinManager.getSkin(stack);
        Rotations rotations = this.rotationMap.computeIfAbsent(entity, uuid -> new Rotations());

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MINIGUNAnimationController.INDEX_BODY, transformType, matrices);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BODY);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(getModelComponent(skin, BODY), MINIGUNAnimationController.INDEX_BODY, transformType, matrices);
            RenderUtil.rotateZ(matrices, -0.5F, -0.345F, rotations.prevRotation + (rotations.rotation - rotations.prevRotation) * v);
            renderComponent(stack, matrices, renderBuffer, light, overlay, skin, BARREL);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }

    private static class Rotations {
        private int rotation;
        private int prevRotation;
    }

    @SubscribeEvent
    public void onClientDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        this.rotationMap.clear();
    }

    private boolean overHeat(PlayerEntity player, ItemStack heldItem) {
        if (heldItem.getItem() instanceof TimelessGunItem && !((TimelessGunItem) heldItem.getItem()).getGun().getReloads().isHeat())
            return false;

        return heldItem.getTag().getInt("heatValue") >= ((TimelessGunItem) heldItem.getItem()).getGun().getReloads().getTickToHeat() ||
                heldItem.getTag().getBoolean("overHeatLock");
    }
}
