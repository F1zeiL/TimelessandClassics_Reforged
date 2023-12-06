package com.tac.guns.client.handler;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.Config.RightClickUse;
import com.tac.guns.client.Keys;
import com.tac.guns.client.render.crosshair.Crosshair;
import com.tac.guns.common.AimingManager;
import com.tac.guns.common.Gun;
import com.tac.guns.duck.MouseSensitivityModifier;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.item.attachment.impl.Scope;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageAim;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.tac.guns.util.math.MathUtil;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class AimingHandler {
    private static AimingHandler instance;

    public static AimingHandler get() {
        if (instance == null) {
            instance = new AimingHandler();
        }
        return instance;
    }

    private final AimingManager.AimTracker localTracker = new AimingManager.AimTracker();
    private double normalisedAdsProgress;
    private double oldProgress;
    private double newProgress;
    private boolean aiming = false;
    private boolean toggledAim = false;

    public int getCurrentScopeZoomIndex() {
        return this.currentScopeZoomIndex;
    }

    public void resetCurrentScopeZoomIndex() {
        this.currentScopeZoomIndex = 0;
    }

    private int currentScopeZoomIndex = 0;

    private boolean isPressed = false;

    private boolean canceling = false;

    private AimingHandler() {
        Keys.SIGHT_SWITCH.addPressCallback(() -> {
            if (!Keys.noConflict(Keys.SIGHT_SWITCH))
                return;

            final Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && (mc.player.getHeldItemMainhand().getItem() instanceof GunItem ||
                    Gun.getScope(mc.player.getHeldItemMainhand()) != null))
                this.currentScopeZoomIndex++;
        });
    }

    @SubscribeEvent
    public void onLocalPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event){
        AimingManager.get().getAimingMap().clear();
    }

    @SubscribeEvent
    public void onClickInput(InputEvent.ClickInputEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        final PlayerEntity player = mc.player;
        assert player != null;
        final ItemStack heldItem = player.getHeldItemMainhand();
        final boolean isGunInHand = heldItem.getItem() instanceof TimelessGunItem;
        if (!isGunInHand) {
            return;
        }

        if (!event.isUseItem()) {
            return;
        }

        final boolean hasMouseOverBlock = mc.objectMouseOver instanceof BlockRayTraceResult;
        if (!hasMouseOverBlock) {
            return;
        }

        assert mc.world != null;
        final BlockRayTraceResult result = (BlockRayTraceResult) mc.objectMouseOver;
        final BlockState state = mc.world.getBlockState(result.getPos());
        final Block block = state.getBlock();
        final RightClickUse config = Config.CLIENT.rightClickUse;
        if (block instanceof ContainerBlock || block.hasTileEntity(state)) {
            if (config.allowChests.get()) {
                return;
            }
        } else if (block == Blocks.CRAFTING_TABLE || block == ModBlocks.WORKBENCH.get()) {
            if (config.allowCraftingTable.get()) {
                return;
            }
        } else if (BlockTags.DOORS.contains(block)) {
            if (config.allowDoors.get()) {
                return;
            }
        } else if (BlockTags.TRAPDOORS.contains(block)) {
            if (config.allowTrapDoors.get()) {
                return;
            }
        } else if (Tags.Blocks.CHESTS.contains(block)) {
            if (config.allowChests.get()) {
                return;
            }
        } else if (Tags.Blocks.FENCE_GATES.contains(block)) {
            if (config.allowFenceGates.get()) {
                return;
            }
        } else if (BlockTags.BUTTONS.contains(block)) {
            if (config.allowButton.get()) {
                return;
            }
        } else if (block == Blocks.LEVER) {
            if (config.allowLever.get()) {
                return;
            }
        } else if (config.allowRestUse.get()) {
            return;
        }

        event.setCanceled(true);
        event.setSwingHand(false);
    }

    public float getAimProgress(PlayerEntity player, float partialTicks) {
        if (player.isUser()) {
            return (float) this.localTracker.getNormalProgress(partialTicks);
        }

        AimingManager.AimTracker tracker = AimingManager.get().getAimTracker(player);
        if (tracker != null) {
            return (float) tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        tickLerpProgress();

        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;

        if (this.aiming)
            player.setSprinting(false);

        if (!Config.CLIENT.controls.holdToAim.get()) {
            if (!Keys.AIM_DOWN_SIGHT.isKeyDown())
                this.isPressed = false;
        }

        if (this.isAiming()) {
            if (!canceling && !this.aiming) {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, true);
                PacketHandler.getPlayChannel().sendToServer(new MessageAim(true));
                this.aiming = true;
            }
            this.localTracker.handleAiming(player.getHeldItemMainhand(), true);
        } else {
            if (this.aiming) {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, false);
                PacketHandler.getPlayChannel().sendToServer(new MessageAim(false));
                this.aiming = false;
            }
            this.localTracker.handleAiming(player.getHeldItemMainhand(), false);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFovUpdate(FOVUpdateEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            float zoomMultiple = 1;
            if (heldItem.getItem() instanceof TimelessGunItem) {
                TimelessGunItem gunItem = (TimelessGunItem) heldItem.getItem();
                if (AimingHandler.get().normalisedAdsProgress != 0 && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING)) {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if (modifiedGun.getModules().getZoom() != null) {
                        float newFov = modifiedGun.getModules().getZoom().getFovModifier();
                        Scope scope = Gun.getScope(heldItem);
                        if (scope != null) {
                            zoomMultiple = scope.getAdditionalZoom().getZoomMultiple();
//                            if (scope.getTagName() == "gener8x" || scope.getTagName() == "vlpvo6" ||
//                                    scope.getTagName() == "acog4x" || scope.getTagName() == "elcan14x" ||
//                                    scope.getTagName() == "qmk152")
//                                newFov = 0.8F;

                            if (!Config.COMMON.gameplay.realisticLowPowerFovHandling.get() || (scope.getAdditionalZoom().getZoomMultiple() > 1 && Config.COMMON.gameplay.realisticLowPowerFovHandling.get()) || gunItem.isIntegratedOptic()) {
                                newFov = (float) MathUtil.magnificationToFovMultiplier(scope.getAdditionalZoom().getZoomMultiple(), mc.gameSettings.fov);
                                if (newFov >= 1) newFov = modifiedGun.getModules().getZoom().getFovModifier();
                                event.setNewfov(newFov + (1.0F - newFov) * (1.0F - (float) this.normalisedAdsProgress));
                            }
                        } else if (!Config.COMMON.gameplay.realisticIronSightFovHandling.get() || gunItem.isIntegratedOptic())
                            event.setNewfov(newFov + (1.0F - newFov) * (1.0F - (float) this.normalisedAdsProgress));
                    }
                }
            }
            double modifier = MathUtil.fovToSenMagnification(event.getNewfov() * mc.gameSettings.fov, mc.gameSettings.fov);
            ((MouseSensitivityModifier) mc.mouseHelper).setSensitivity(mc.gameSettings.mouseSensitivity / modifier);
        }
    }

    private void tickLerpProgress(){
        oldProgress = newProgress;
        newProgress += (normalisedAdsProgress - newProgress) * 0.5;
    }

    /**
     * Prevents the crosshair from rendering when aiming down sight
     */
    @SubscribeEvent(receiveCanceled = true)
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        this.normalisedAdsProgress = this.localTracker.getNormalProgress(event.getPartialTicks());
        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
        if (this.normalisedAdsProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && (crosshair == null || crosshair.isDefault())) {
            event.setCanceled(true);
        }
    }

    public boolean isAiming() {
        Minecraft mc = Minecraft.getInstance();
        if (canceling)
            return false;

        if (mc.player == null)
            return false;

        if (mc.player.isSpectator())
            return false;

        if (mc.currentScreen != null)
            return false;

        ItemStack heldItem = mc.player.getHeldItemMainhand();
        if (!(heldItem.getItem() instanceof GunItem))
            return false;

        Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
        if (gun.getModules().getZoom() == null) {
            return false;
        }

        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldown = tracker.getCooldown(heldItem.getItem(), Minecraft.getInstance().getRenderPartialTicks());

        if (gun.getGeneral().isBoltAction() && (cooldown < 0.8 && cooldown > 0) && Gun.getScope(heldItem) != null) {
            return false;
        }

//        if(!this.localTracker.isAiming() && this.isLookingAtInteractableBlock())
//            return false;

        if (SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
            return false;

        boolean zooming;

        if (Config.CLIENT.controls.holdToAim.get()) {
            zooming = Keys.AIM_DOWN_SIGHT.isKeyDown();
        } else {
            if (Keys.AIM_DOWN_SIGHT.isKeyDown())
                if (!this.isPressed) {
                    this.isPressed = true;
                    this.forceToggleAim();
                }
            zooming = this.toggledAim;
        }
        return zooming;
    }

    public boolean isToggledAim() {
        return this.toggledAim;
    }

    public void forceToggleAim() {
        if (this.toggledAim) {
            this.toggledAim = false;
        } else if (!canceling) {
            this.toggledAim = true;
        }
    }

    public double getNormalisedAdsProgress() {
        return this.normalisedAdsProgress;
    }

    public double getLerpAdsProgress(float partialTicks){
        return MathHelper.lerp(partialTicks, oldProgress, newProgress);
    }

    public void cancelAim() {
        PlayerEntity player = Minecraft.getInstance().player;
        canceling = true;
        cancel(player);
    }

    private void cancel(PlayerEntity player) {
        if (this.aiming || this.toggledAim) {
            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, false);
            PacketHandler.getPlayChannel().sendToServer(new MessageAim(false));
            this.aiming = false;
            this.toggledAim = false;
        }

        this.localTracker.handleAiming(player.getHeldItemMainhand(), false);
    }

    public void setCanceling() {
        if (this.canceling) this.canceling = false;
    }

    public boolean getCanceling() {
        return this.canceling;
    }
}