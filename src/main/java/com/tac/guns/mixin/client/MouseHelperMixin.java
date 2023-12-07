package com.tac.guns.mixin.client;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.duck.MouseSensitivityModifier;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.impl.Scope;
import com.tac.guns.util.math.MathUtil;
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
//    private double originSensitivity;
    private double sensitivity;
//    @Final
//    @Shadow
//    private Minecraft minecraft;

//    @Inject(method = "updatePlayerLook", at = @At("HEAD"))
//    public void modifySensitivity(CallbackInfo ci) {
//        originSensitivity = minecraft.gameSettings.mouseSensitivity;
//        if (AimingHandler.get().isAiming()) {
//            minecraft.gameSettings.mouseSensitivity = this.sensitivity;
//        }
//    }
//
//    @Inject(method = "updatePlayerLook", at = @At("RETURN"))
//    public void backtraceSensitivity(CallbackInfo ci) {
//        minecraft.gameSettings.mouseSensitivity = originSensitivity;
//    }

    @Override
    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    @ModifyVariable(method = "updatePlayerLook()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 2)
    private double sensitivity(double original) {
        float additionalAdsSensitivity = 1.0F;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if (heldItem.getItem() instanceof GunItem) {
                GunItem gunItem = (GunItem) heldItem.getItem();
                if (AimingHandler.get().isAiming() && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING)) {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if (modifiedGun.getModules().getZoom() != null) {
                        float newFov = modifiedGun.getModules().getZoom().getFovModifier();
                        Scope scope = Gun.getScope(heldItem);
                        if (scope != null) {
                            float zoomFov = (float) MathUtil.magnificationToFovMultiplier(scope.getAdditionalZoom().getZoomMultiple(), mc.gameSettings.fov);
                            float fovDelta = newFov - zoomFov;
                            if (zoomFov >= 1) {
                                newFov *= 1.75F;
                            } else
                                newFov -= fovDelta * (Config.CLIENT.display.scopeDoubleRender.get() ? 0.833F : 1F);
                        } else {
                            newFov *= 1.75F;
                        }
                        additionalAdsSensitivity = MathHelper.clamp(1.0F - (1.0F / newFov) / 10F, 0.0F, 1.0F);
                    }
                }
            }
        }
        double adsSensitivity = Config.CLIENT.controls.aimDownSightSensitivity.get();
        return original * (1.0 - (1.0 - adsSensitivity) * AimingHandler.get().getNormalisedAdsProgress()) * additionalAdsSensitivity;
    }
}