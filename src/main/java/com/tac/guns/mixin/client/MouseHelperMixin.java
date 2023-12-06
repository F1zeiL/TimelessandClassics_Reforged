package com.tac.guns.mixin.client;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.duck.MouseSensitivityModifier;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.impl.Scope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mixin(MouseHelper.class)
public abstract class MouseHelperMixin implements MouseSensitivityModifier
{
    private double originSensitivity;
    private double sensitivity;
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "updatePlayerLook", at = @At("HEAD"))
    public void modifySensitivity(CallbackInfo ci) {
        originSensitivity = minecraft.gameSettings.mouseSensitivity;
        if (AimingHandler.get().isAiming()) {
            minecraft.gameSettings.mouseSensitivity = this.sensitivity;
        }
    }

    @Inject(method = "updatePlayerLook", at = @At("RETURN"))
    public void backtraceSensitivity(CallbackInfo ci) {
        minecraft.gameSettings.mouseSensitivity = originSensitivity;
    }

    @Override
    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }
}