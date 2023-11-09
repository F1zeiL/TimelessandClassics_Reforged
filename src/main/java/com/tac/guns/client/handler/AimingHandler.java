package com.tac.guns.client.handler;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.Config.RightClickUse;
import com.tac.guns.GunMod;
import com.tac.guns.client.Keys;
import com.tac.guns.client.render.crosshair.Crosshair;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.impl.Scope;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageAim;
import com.tac.guns.network.message.MessageAimingState;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.WeakHashMap;

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

    private static final double MAX_AIM_PROGRESS = 4;
    private final AimTracker localTracker = new AimTracker();
    private final Map<PlayerEntity, AimTracker> aimingMap = new WeakHashMap<>();
    private double normalisedAdsProgress;
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

    private boolean originalSprint = false;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;
        /*if(!this.aiming)
            ScopeJitterHandler.getInstance().resetBreathingTickBuffer();*/
        PlayerEntity player = event.player;
        AimTracker tracker = getAimTracker(player);
        if (tracker != null) {
            tracker.handleAiming(player, player.getHeldItem(Hand.MAIN_HAND));
            if (!tracker.isAiming()) {
                this.aimingMap.remove(player);
            }
        }
        if (player == Minecraft.getInstance().player) {
            if (isAiming()) {
                if (player.isSprinting()) {
                    originalSprint = true;
                    player.setSprinting(false);
                }
            } else if (originalSprint) {
                originalSprint = false;
                player.setSprinting(true);
            }
        }
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

    @Nullable
    private AimTracker getAimTracker(PlayerEntity player) {
        if (SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING) && !this.aimingMap.containsKey(player)) {
            this.aimingMap.put(player, new AimTracker());
        }
        return this.aimingMap.get(player);
    }

    public float getAimProgress(PlayerEntity player, float partialTicks) {
        if (player.isUser()) {
            return (float) this.localTracker.getNormalProgress(partialTicks);
        }

        AimTracker tracker = this.getAimTracker(player);
        if (tracker != null) {
            return (float) tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;

        if (!Config.CLIENT.controls.holdToAim.get()) {
            if (!Keys.AIM_TOGGLE.isKeyDown())
                this.isPressed = false;
        }

        ItemStack heldItem = player.getHeldItemMainhand();
        if (heldItem.getItem() instanceof TimelessGunItem) {
            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING_STATE, 1F - (float) this.getNormalisedAdsProgress());
            PacketHandler.getPlayChannel().sendToServer(new MessageAimingState(1F - (float) this.getNormalisedAdsProgress()));
        }

        if (this.isAiming()) {
            if (!canceling && !this.aiming) {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, true);
                PacketHandler.getPlayChannel().sendToServer(new MessageAim(true));
                this.aiming = true;
            }
        } else if (this.aiming) {
            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, false);
            PacketHandler.getPlayChannel().sendToServer(new MessageAim(false));
            this.aiming = false;
        }

        this.localTracker.handleAiming(player, player.getHeldItem(Hand.MAIN_HAND));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFovUpdate(FOVUpdateEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if (heldItem.getItem() instanceof TimelessGunItem) {
                TimelessGunItem gunItem = (TimelessGunItem) heldItem.getItem();
                if (AimingHandler.get().isAiming() && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING)) {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if (modifiedGun.getModules().getZoom() != null) {
                        float newFov = modifiedGun.getModules().getZoom().getFovModifier();
                        Scope scope = Gun.getScope(heldItem);
                        if (scope != null) {
                            if (scope.getTagName() == "gener8x" || scope.getTagName() == "vlpvo6" ||
                                    scope.getTagName() == "acog4x" || scope.getTagName() == "elcan14x" ||
                                    scope.getTagName() == "qmk152")
                                newFov = 0.8F;

                            if (!Config.COMMON.gameplay.realisticLowPowerFovHandling.get() || (scope.getAdditionalZoom().getFovZoom() > 0 && Config.COMMON.gameplay.realisticLowPowerFovHandling.get()) || gunItem.isIntegratedOptic()) {
                                newFov -= scope.getAdditionalZoom().getFovZoom() * (Config.CLIENT.display.scopeDoubleRender.get() ? 1F : 1.2F);
                                event.setNewfov(newFov + (1.0F - newFov) * (1.0F - (float) this.normalisedAdsProgress));
                            }
                        } else if (!Config.COMMON.gameplay.realisticIronSightFovHandling.get() || gunItem.isIntegratedOptic())
                            event.setNewfov(newFov + (1.0F - newFov) * (1.0F - (float) this.normalisedAdsProgress));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        this.aimingMap.clear();
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
            zooming = Keys.AIM_HOLD.isKeyDown();
        } else {
            if (Keys.AIM_TOGGLE.isKeyDown())
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

    public class AimTracker {
        private double currentAim;
        private double previousAim;
        private double amplifier = 0.8;

        private void handleAiming(PlayerEntity player, ItemStack heldItem) {
            this.previousAim = this.currentAim;
            double vAmplifier = 0.1;
            if (SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING) || (player.isUser() && (AimingHandler.this.isAiming()))) {
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
//            float t = (float) (1F - currentAim / 4);
//            float dist = (t >= 0 || t <= 1 ? t : 0);
//            if(prevDist != dist) {
//                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING_STATE, dist);
//                PacketHandler.getPlayChannel().sendToServer(new MessageAimingState(dist));
//            }
//            prevDist = dist;
        }

        public boolean isAiming() {
            return this.currentAim != 0 || this.previousAim != 0;
        }

        public double getNormalProgress(float partialTicks) {
            return (this.previousAim + (this.currentAim - this.previousAim) * (this.previousAim == 0 || this.previousAim == MAX_AIM_PROGRESS ? 0 : partialTicks)) / (float) MAX_AIM_PROGRESS;
        }
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

        this.localTracker.handleAiming(player, player.getHeldItem(Hand.MAIN_HAND));
    }

    public void setCanceling() {
        if (this.canceling) this.canceling = false;
    }

    public boolean getCanceling() {
        return this.canceling;
    }
}