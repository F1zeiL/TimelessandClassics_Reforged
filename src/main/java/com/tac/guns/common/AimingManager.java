package com.tac.guns.common;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Reference;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class AimingManager {
    private static AimingManager instance;

    public static AimingManager get() {
        if (instance == null) {
            instance = new AimingManager();
        }
        return instance;
    }

    protected final Map<PlayerEntity, AimTracker> aimingMap = new WeakHashMap<>();

    private static final double MAX_AIM_PROGRESS = 4;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;
        PlayerEntity player = event.player;
        if(player.isSpectator())
            return;

        AimingManager manager = AimingManager.get();
        boolean isAiming = SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING);
        if (isAiming && ! manager.aimingMap.containsKey(player)) {
            manager.aimingMap.put(player, new AimTracker());
        }
        AimTracker tracker = manager.getAimTracker(player);
        if (tracker != null) {
            tracker.handleAiming(player.getHeldItemMainhand(), isAiming);
            tracker.tickLerpProgress();
            if (!tracker.isAiming()) {
                manager.aimingMap.remove(player);
            }
        }
    }

    @SubscribeEvent
    public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        AimingManager.get().aimingMap.remove(event.getPlayer());
    }

    @Nullable
    public AimTracker getAimTracker(PlayerEntity player) {
        return aimingMap.get(player);
    }

    public Map<PlayerEntity, AimTracker> getAimingMap(){
        return aimingMap;
    }

    public static class AimTracker
    {
        private double currentAim;
        private double previousAim;
        private double amplifier = 0.8;

        private double lerpProgress;
        public void handleAiming(ItemStack heldItem, boolean isAiming) {
            this.previousAim = this.currentAim;
            double vAmplifier = 0.1;
            if (isAiming) {
                if (this.amplifier < 1.3) {
                    amplifier += vAmplifier;
                }
                if (this.currentAim < MAX_AIM_PROGRESS) {
                    double speed = GunEnchantmentHelper.getAimDownSightSpeed(heldItem);
                    speed = GunModifierHelper.getModifiedAimDownSightSpeed(heldItem, speed);
                    this.currentAim += speed * amplifier;
                    if (this.currentAim > MAX_AIM_PROGRESS) {
                        amplifier = 0.5;
                        this.currentAim = (int) MAX_AIM_PROGRESS;
                    }
                }
            } else {
                if (this.currentAim > 0) {
                    if (this.amplifier < 1.3) {
                        amplifier += vAmplifier;
                    }
                    double speed = GunEnchantmentHelper.getAimDownSightSpeed(heldItem);
                    speed = GunModifierHelper.getModifiedAimDownSightSpeed(heldItem, speed);
                    this.currentAim -= speed * amplifier;
                    if (this.currentAim < 0) {
                        amplifier = 0.5;
                        this.currentAim = 0;
                    }
                } else amplifier = 0.8;
            }
        }

        public boolean isAiming() {
            return this.currentAim != 0 || this.previousAim != 0;
        }

        protected void tickLerpProgress(){
            lerpProgress += (getNormalProgress(1) - lerpProgress) * 0.5;
        }

        public double getLerpProgress(){
            return lerpProgress;
        }

        public double getNormalProgress(float partialTicks) {
            return (this.previousAim + (this.currentAim - this.previousAim) * (this.previousAim == 0 || this.previousAim == MAX_AIM_PROGRESS ? 0 : partialTicks)) / (float) MAX_AIM_PROGRESS;
        }
    }
}
