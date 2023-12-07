package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.command.GunEditor;
import com.tac.guns.client.render.IHeldAnimation;
import com.tac.guns.client.render.animation.module.CameraAnimated;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PistalAnimationController;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.common.network.ServerPlayHandler;
import com.tac.guns.common.tooling.CommandsHandler;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.event.GunReloadEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.*;
import com.tac.guns.item.transition.ITimelessAnimated;
import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.item.attachment.IBarrel;
import com.tac.guns.item.attachment.impl.Barrel;
import com.tac.guns.item.attachment.impl.Scope;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessagePlayerShake;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.IDLNBTUtil;
import com.tac.guns.util.OptifineHelper;
import com.tac.guns.util.math.MathUtil;
import com.tac.guns.util.math.OneDimensionalPerlinNoise;
import com.tac.guns.util.math.SecondOrderDynamics;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;

public class GunRenderingHandler {
    private static GunRenderingHandler instance;
    private final SecondOrderDynamics recoilDynamics = new SecondOrderDynamics(0.5f, 0.6f, 2.65f, 0);
    private final SecondOrderDynamics swayYawDynamics = new SecondOrderDynamics(0.4f, 0.5f, 3.25f, 0);
    private final SecondOrderDynamics swayPitchDynamics = new SecondOrderDynamics(0.3f, 0.4f, 3.5f, 0);
    private final SecondOrderDynamics aimingDynamics = new SecondOrderDynamics(0.45f, 0.92f, 1.1f, 0);
    // Standard Sprint Dynamics
    private final SecondOrderDynamics sprintDynamics = new SecondOrderDynamics(0.22f, 0.7f, 0.6f, 0);
    private final SecondOrderDynamics bobbingDynamics = new SecondOrderDynamics(0.22f, 0.7f, 0.6f, 1);
    private final SecondOrderDynamics speedUpDynamics = new SecondOrderDynamics(0.22f, 0.7f, 0.6f, 0);
    private final SecondOrderDynamics delaySwayDynamics = new SecondOrderDynamics(0.75f, 0.9f, 1.4f, 0);
    private final SecondOrderDynamics sprintDynamicsZ = new SecondOrderDynamics(0.22f, 0.8f, 0.5f, 0);
    private final SecondOrderDynamics jumpingDynamics = new SecondOrderDynamics(0.28f, 1f, 0.65f, 0);
    // High Speed Sprint Dynamics
    private final SecondOrderDynamics sprintDynamicsHSS = new SecondOrderDynamics(0.3f, 0.6f, 0.6f,
            0);
    /* private final SecondOrderDynamics sprintDynamicsZHSS = new SecondOrderDynamics(0.15f,0.7f,
             -2.25f, new Vector3f(0,0,0));*/
    private final SecondOrderDynamics sprintDynamicsZHSS = new SecondOrderDynamics(0.27f, 0.75f, 0.5f,
            0);
    public final SecondOrderDynamics sprintDynamicsHSSLeftHand = new SecondOrderDynamics(0.38f,
            1f, 0f, 0);

    public static GunRenderingHandler get() {
        if (instance == null) {
            instance = new GunRenderingHandler();
        }
        return instance;
    }

    public static final ResourceLocation MUZZLE_FLASH_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/effect/muzzle_flash.png");
    public static final ResourceLocation MUZZLE_SMOKE_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/effect/muzzle_smoke.png");

    private Random random = new Random();
    private Set<Integer> entityIdForMuzzleFlash = new HashSet<>();
    private Set<Integer> entityIdForDrawnMuzzleFlash = new HashSet<>();
    private Queue<ShellInAir> shells = new ArrayDeque<>();
    private Map<Integer, Float> entityIdToRandomValue = new HashMap<>();

    public int sprintTransition;
    private int prevSprintTransition;
    private int sprintCooldown;
    private int restingTimer;
    private final int restingTimerUpper = 5;

    private float offhandTranslate;
    private float prevOffhandTranslate;

    public float muzzleExtraOnEnch = 0f;

    private Field equippedProgressMainHandField;
    private Field prevEquippedProgressMainHandField;

    private float startingDistance = 0f;
    private float speedUpDistanceFrom = 0f;

    public float speedUpDistance = 0.6f;

    public float speedUpProgress = 0f;

    private float additionalSwayProgress = 0f;

    private GunRenderingHandler() {
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        this.updateSprinting();
        this.updateMuzzleFlash();
        this.updateShellCasing();
        this.updateOffhandTranslate();
        if (Minecraft.getInstance().player == null)
            return;
        if (Minecraft.getInstance().player.movementInput.backKeyDown) {
            if (backwardTicker < 8) backwardTicker++;
        } else {
            if (backwardTicker > 0) backwardTicker--;
        }
        if (CommandsHandler.get().getCatCurrentIndex() == 1 && GunEditor.get().getMode() == GunEditor.TaCWeaponDevModes.flash) {
            if (!(Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof TimelessGunItem))
                return;
            GunItem gun = ((TimelessGunItem) Minecraft.getInstance().player.getHeldItemMainhand().getItem());
            Gun modifiedGun = gun.getModifiedGun(Minecraft.getInstance().player.getHeldItemMainhand());
            if (modifiedGun.getDisplay().getFlash() != null || GunEditor.get().getMode() == GunEditor.TaCWeaponDevModes.flash) {
                this.showMuzzleFlashForPlayer(Minecraft.getInstance().player.getEntityId());
            }
        }
    }

    private long fireTime = System.currentTimeMillis();

    @SubscribeEvent
    public void onGunFired(GunFireEvent event) {
        if (event.isClient()) fireTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null)
            return;
        if (!(mc.player.getHeldItemMainhand().getItem() instanceof GunItem) || mc.player.getHeldItemMainhand().getTag() == null)
            return;
        if ((Config.COMMON.gameplay.forceCameraShakeOnFire.get() || Config.CLIENT.display.cameraShakeOnFire.get()) && IDLNBTUtil.getInt(mc.player.getHeldItemMainhand(), "CurrentFireMode") != 0) {
            float cameraShakeDuration = 0.06f; //TODO: Force to be adjusted per shot later in 0.3.4-0.3.5, customizable per gun
            long alphaTime = System.currentTimeMillis() - fireTime;
            float progress = (alphaTime < cameraShakeDuration * 1000 ? 1 - alphaTime / (cameraShakeDuration * 1000f) : 0);
            //apply camera shake when firing.
            float alpha = (progress
                    * (Math.random() - 0.5 < 0 ? -1 : 1)
                    * 0.9f);
            event.setPitch(event.getPitch() - Math.abs(alpha));
            event.setRoll(event.getRoll() + alpha * 0.5f);
        }

        float multiplier = 20.0f;
        GunAnimationController controller = GunAnimationController.fromItem(mc.player.getHeldItemMainhand().getItem());
        if (controller instanceof CameraAnimated) {
            int index = ((CameraAnimated) controller).getCameraNodeIndex();
            Vector3f vector3f = controller.getYPRAngle(index);
            event.setYaw(event.getYaw() + vector3f.getX() * multiplier);
            event.setPitch(event.getPitch() + vector3f.getY() * multiplier);
            event.setRoll(event.getRoll() + vector3f.getZ() * multiplier);
        }
    }

    @SubscribeEvent
    public void onFovModifying(EntityViewRenderEvent.FOVModifier event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !mc.player.isAlive() || mc.player.isSpectator())
            return;
        if (!(mc.player.getHeldItemMainhand().getItem() instanceof GunItem) || mc.player.getHeldItemMainhand().getTag() == null)
            return;

        if ((Config.COMMON.gameplay.forceCameraShakeOnFire.get() || Config.CLIENT.display.cameraShakeOnFire.get()) && mc.player.getHeldItemMainhand().getTag().getInt("CurrentFireMode") != 0) {
            float cameraShakeDuration = 0.06f * (AimingHandler.get().isAiming() ? 1.5f : 1f);
            long alphaTime = System.currentTimeMillis() - fireTime;
            float progress = (alphaTime < cameraShakeDuration * 1000 ? 1 - alphaTime / (cameraShakeDuration * 1000f) : 0);
            event.setFOV(event.getFOV() + progress * 0.5f);
        }
    }

    private void updateSprinting() {
        this.prevSprintTransition = this.sprintTransition;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && (mc.player.isSprinting() && !mc.player.isCrouching()) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.SHOOTING) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING) && !AimingHandler.get().isAiming() && this.sprintCooldown == 0) {
            if (Minecraft.getInstance().player != null) {
                ItemStack heldItem = Minecraft.getInstance().player.getHeldItemMainhand();
                if (heldItem.getItem() instanceof GunItem) {
                    GunItem modifiedGun = (GunItem) heldItem.getItem();
                    GunAnimationController controller = GunAnimationController.fromItem(modifiedGun.getItem());
                    if (this.sprintTransition < 5) {
                        if (controller == null ||
                                (modifiedGun.getGun().getGeneral().getGripType().getHeldAnimation().canApplySprintingAnimation() && !controller.isAnimationRunning())) {
                            this.sprintTransition++;
                        }
                    }
                    if (controller != null && controller.isAnimationRunning()) {
                        if (sprintTransition > 0) {
                            this.sprintTransition--;
                        }
                    }
                }
            }
        } else if (this.sprintTransition > 0) {
            this.sprintTransition = 0;
        }

        if (this.sprintCooldown > 0) {
            this.sprintCooldown--;
        }
        if (sprintTransition == 0) {
            if (restingTimer < restingTimerUpper) {
                restingTimer++;
            }
        } else {
            restingTimer = 0;
        }
    }

    private void updateMuzzleFlash() {
        this.entityIdForMuzzleFlash.removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdToRandomValue.keySet().removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdForDrawnMuzzleFlash.clear();
        this.entityIdForDrawnMuzzleFlash.addAll(this.entityIdForMuzzleFlash);
    }

    private void updateShellCasing() {
        while (shells.peek() != null && shells.peek().livingTick <= 0) shells.poll();
        for (ShellInAir shell : shells) {
            shell.livingTick--;
            shell.velocity.add(new Vector3f(-0.02f * shell.velocity.getX(), -0.25f, -0.02f * shell.velocity.getY())); //simulating gravity
            shell.preDisplacement.setX(shell.displacement.getX());
            shell.preDisplacement.setY(shell.displacement.getY());
            shell.preDisplacement.setZ(shell.displacement.getZ());
            shell.preRotation.setX(shell.rotation.getX());
            shell.preRotation.setY(shell.rotation.getY());
            shell.preRotation.setZ(shell.rotation.getZ());
            shell.displacement.add(shell.velocity);
            shell.rotation.add(shell.angularVelocity);
        }
    }

    private void updateOffhandTranslate() {
        this.prevOffhandTranslate = this.offhandTranslate;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        boolean down = false;
        ItemStack heldItem = mc.player.getHeldItemMainhand();
        if (heldItem.getItem() instanceof GunItem) {
            Gun modifiedGun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            down = !modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem();
        }

        float direction = down ? -0.3F : 0.3F;
        this.offhandTranslate = MathHelper.clamp(this.offhandTranslate + direction, 0.0F, 1.0F);
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Post event) {
        if (!event.isClient())
            return;
        if (Minecraft.getInstance().player == null) return;
        Item item = event.getStack().getItem();
        if (item instanceof ITimelessAnimated) {
            ITimelessAnimated animated = (ITimelessAnimated) item;
            animated.playAnimation("fire", event.getStack(), true);
        }
        this.sprintTransition = 0;
        this.speedUpDistanceFrom = Minecraft.getInstance().player.distanceWalkedModified;
        this.sprintCooldown = 8;

        ItemStack heldItem = event.getStack();
        GunItem gunItem = (GunItem) heldItem.getItem();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);
        if (modifiedGun.getDisplay().getFlash() != null) {
            this.showMuzzleFlashForPlayer(Minecraft.getInstance().player.getEntityId());
        }
        Gun.ShellCasing shellCasing = modifiedGun.getDisplay().getShellCasing();
        if (shellCasing != null) {
            float card = 1f - random.nextFloat() * 2f;
            float vard = 1.2f - random.nextFloat() * 0.4f;
            shells.add(new ShellInAir(
                    new Vector3f((float) shellCasing.getXOffset(), (float) shellCasing.getYOffset(), (float) shellCasing.getZOffset()),
                    new Vector3f(shellCasing.getVelocityX() + shellCasing.getRVelocityX() * card, shellCasing.getVelocityY() + shellCasing.getRVelocityY() * card, shellCasing.getVelocityZ() + shellCasing.getRVelocityZ() * card),
                    new Vector3f(vard * shellCasing.getAVelocityX(), vard * shellCasing.getAVelocityY(), vard * shellCasing.getAVelocityZ()),
                    modifiedGun.getDisplay().getShellCasing().getTickLife()
            ));
        }
    }

    @SubscribeEvent
    public void onGunFirePre(GunFireEvent.Pre event) {
        if (sprintTransition == 0) {
            if (restingTimer < restingTimerUpper) {
                event.setCanceled(true);
            }
        } else {
            event.setCanceled(true);
        }
    }

    public void showMuzzleFlashForPlayer(int entityId) {
        this.entityIdForMuzzleFlash.add(entityId);
        this.entityIdToRandomValue.put(entityId, this.random.nextFloat());
    }

    @SubscribeEvent
    public void onAnimatedGunReload(GunReloadEvent.Pre event) {
        Item item = event.getStack().getItem();
        if (item instanceof ITimelessAnimated) {
            ITimelessAnimated animated = (ITimelessAnimated) item;
            animated.playAnimation("reload", event.getStack(), false);
        }
    }

/*    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) // TEST FROM CGM
    {
        *//*if(!Config.CLIENT.experimental.immersiveCamera.get())
            return;*//*

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        ItemStack heldItem = mc.player.getHeldItemMainhand();
        float targetAngle = heldItem.getItem() instanceof GunItem ? mc.player.movementInput.moveStrafe * 1F: 0F;
        this.immersiveRoll = MathHelper.approach(this.immersiveRoll, targetAngle, 0.4F);
        event.setRoll(-this.immersiveRoll);
    }*/

    public float immersiveWeaponRoll;

    public float walkingDistance1;
    public float walkingDistance = 0.0f; //Abandoned. It is always 0.0f.
    public float walkingCrouch;
    public float walkingCameraYaw;
    public float zoomProgressInv;

    public double xOffset = 0.0;
    public double yOffset = 0.0;
    public double zOffset = 0.0;

    public double opticMovement;
    public double slideKeep;

    public float translateX = 0f;
    public float translateY = 0f;
    public float translateZ = 0f;

    private double fix = 0;

    @SubscribeEvent
    public void onRenderOverlay(RenderHandEvent event) {
        GunAnimationController controller = GunAnimationController.fromItem(event.getItemStack().getItem());
        boolean isAnimated = controller != null;
        MatrixStack matrixStack = event.getMatrixStack();

        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHand() == Hand.MAIN_HAND : event.getHand() == Hand.OFF_HAND;
        ItemStack heldItem = event.getItemStack();

        if (!(heldItem.getItem() instanceof GunItem)) {
            return;
        }

        /* Cancel it because we are doing our own custom render */
        event.setCanceled(true);

        ItemStack overrideModel = ItemStack.EMPTY;
        if (heldItem.getTag() != null) {
            if (heldItem.getTag().contains("Model", Constants.NBT.TAG_COMPOUND)) {
                overrideModel = ItemStack.read(heldItem.getTag().getCompound("Model"));
            }
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        GunItem gunItem = (GunItem) heldItem.getItem();
        if (mc.gameSettings.viewBobbing && mc.getRenderViewEntity() instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) mc.getRenderViewEntity();
            float deltaDistanceWalked = playerentity.distanceWalkedModified - playerentity.prevDistanceWalkedModified;
            float distanceWalked = -(playerentity.distanceWalkedModified + deltaDistanceWalked * event.getPartialTicks());
            float cameraYaw = MathHelper.lerp(event.getPartialTicks(), playerentity.prevCameraYaw, playerentity.cameraYaw);

            /* Reverses the original bobbing rotations and translations so it can be controlled */
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-(Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F)));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(-(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 3.0F)));
            matrixStack.translate(-(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F), -(-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)), 0.0D);

            // Walking bobbing
            boolean aimed = false;
            /* The new controlled bobbing */
            if (AimingHandler.get().isAiming())
                aimed = true;

            //double invertZoomProgress = aimed ? 0.0575 : 0.468; //0.135 : 0.44;//0.94;//aimed ? 1.0 - AimingHandler.get().getNormalisedRepairProgress() : ;
            double invertZoomProgress = aimed ? (Gun.getScope(heldItem) != null ? 0.0575 : 0.0725) : 0.468;
            float crouch = mc.player.isCrouching() ? 148f : 1f;

            if (playerentity.distanceWalkedModified == playerentity.prevDistanceWalkedModified && !playerentity.isSneaking())
                startingDistance = playerentity.distanceWalkedModified;
            if (!mc.player.movementInput.isMovingForward()) {
                speedUpDistanceFrom = playerentity.distanceWalkedModified;
                speedUpProgress -= (new Date().getTime() - prevTime) / 150f;
                if (speedUpProgress < 0) speedUpProgress = 0;
            } else {
                speedUpProgress = (-distanceWalked - speedUpDistanceFrom < speedUpDistance ? (-distanceWalked - speedUpDistanceFrom) / speedUpDistance : 1);
            }
            distanceWalked = distanceWalked + startingDistance;
            this.walkingDistance1 = distanceWalked;
            this.walkingCrouch = crouch;
            this.walkingCameraYaw = cameraYaw;
            this.zoomProgressInv = (float) invertZoomProgress;

            //matrixStack.translate((double) (Math.asin(-MathHelper.sin(distanceWalked*crouch * (float) Math.PI) * cameraYaw * 0.5F)) * invertZoomProgress, ((double) (Math.asin((-Math.abs(-MathHelper.cos(distanceWalked*crouch * (float) Math.PI) * cameraYaw))) * invertZoomProgress)) * 1.140, 0.0D);// * 1.140, 0.0D);
            applyBobbingTransforms(matrixStack, false);
            applyJumpingTransforms(matrixStack, event.getPartialTicks());
            //TODO: Implement config switch, it's not a required mechanic. it's just fun
            applyNoiseMovementTransform(matrixStack);


            matrixStack.rotate(Vector3f.ZP.rotationDegrees((MathHelper.sin(distanceWalked * crouch * (float) Math.PI) * cameraYaw * 3.0F) * (float) invertZoomProgress));
            matrixStack.rotate(Vector3f.XP.rotationDegrees((Math.abs(MathHelper.cos(distanceWalked * crouch * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F) * (float) invertZoomProgress));

            // Weapon movement clanting
            float rollingForceCrouch = mc.player.isCrouching() ? 4f : 1f;
            float rollingForceAim = AimingHandler.get().isAiming() ? 0.425f : 1f;
            /*
                Pretty much from CGM, was going to build something very similar for 0.3, movement update comes early I guess,
                all credit to Mr.Crayfish who developed this weapon roll code for CGM,
                all I added was scaling for other game actions and adjusted rolling values
            */
            float targetAngle = heldItem.getItem() instanceof GunItem ? mc.player.movementInput.moveStrafe * (6.65F * rollingForceCrouch * rollingForceAim) : 0F;
            this.immersiveWeaponRoll = MathHelper.approach(this.immersiveWeaponRoll, targetAngle, 0.335F);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(this.immersiveWeaponRoll));
        }

        if (event.getHand() == Hand.OFF_HAND) {
            if (heldItem.getItem() instanceof GunItem) {
                event.setCanceled(true);
                return;
            }

            float offhand = 1.0F - MathHelper.lerp(event.getPartialTicks(), this.prevOffhandTranslate, this.offhandTranslate);
            matrixStack.translate(0, offhand * -0.6F, 0);

            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null && player.getHeldItemMainhand().getItem() instanceof GunItem) {
                Gun modifiedGun = ((GunItem) player.getHeldItemMainhand().getItem()).getModifiedGun(player.getHeldItemMainhand());
                if (!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem()) {
                    return;
                }
            }

            /* Makes the off hand item move out of view */
            matrixStack.translate(0, -2 * AimingHandler.get().getNormalisedAdsProgress(), 0);
        }

        ClientPlayerEntity entity = mc.player;
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(overrideModel.isEmpty() ? heldItem : overrideModel, entity.world, entity);
        float scaleX = model.getItemCameraTransforms().firstperson_right.scale.getX();
        float scaleY = model.getItemCameraTransforms().firstperson_right.scale.getY();
        float scaleZ = model.getItemCameraTransforms().firstperson_right.scale.getZ();
        this.translateX = model.getItemCameraTransforms().firstperson_right.translation.getX();
        this.translateY = model.getItemCameraTransforms().firstperson_right.translation.getY();
        this.translateZ = model.getItemCameraTransforms().firstperson_right.translation.getZ();

        matrixStack.push();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);

        if (modifiedGun.canAimDownSight()) {
            if (event.getHand() == Hand.MAIN_HAND) {
                this.xOffset = 0.0;
                this.yOffset = 0.0;
                this.zOffset = 0.0;
                Scope scope = Gun.getScope(heldItem);
                boolean isScopeOffsetType = Config.CLIENT.display.gameplayEnchancedScopeOffset.get();
                boolean isScopeRenderType = Config.CLIENT.display.scopeDoubleRender.get();
                /* Creates the required offsets to position the scope into the middle of the screen. */
                if (modifiedGun.canAttachType(IAttachment.Type.SCOPE) && scope != null) {
                    double viewFinderOffset = isScopeOffsetType || OptifineHelper.isShadersEnabled() ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset();
                    if (scope.getAdditionalZoom().getZoomMultiple() > 1)
                        viewFinderOffset = isScopeRenderType ? (isScopeOffsetType || OptifineHelper.isShadersEnabled() ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset()) : (isScopeOffsetType || OptifineHelper.isShadersEnabled() ? scope.getViewFinderOffsetSpecialDR() : scope.getViewFinderOffsetDR()); // switches between either, but either must be populated
                    //if (OptifineHelper.isShadersEnabled()) viewFinderOffset *= 0.735;
                    //if (isScopeRenderType) viewFinderOffset *= 0.735;
                    try {
                        Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getScope();
                        xOffset = -translateX + (modifiedGun.getModules().getZoom().getXOffset() * 0.0625) + -scaledPos.getXOffset() * 0.0625 * scaleX;
                        yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                        zOffset = Config.CLIENT.display.sight1xRealisticPosition.get() && scope.getAdditionalZoom().getZoomMultiple() == 1 ? -translateZ + modifiedGun.getModules().getZoom().getZOffset() * 0.0625 * scaleZ :
                                -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - viewFinderOffset * scaleZ * scaledPos.getScale();
                    } catch (NullPointerException e) {
                        GunMod.LOGGER.info("GunRenderingHandler NPE @509");
                    }

//                    if (!SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.AIMING)) {
                    if (Objects.equals(scope.getTagName(), "qmk152"))
                        this.fix = -0.05;
                    else if (Objects.equals(scope.getTagName(), "elcan14x"))
                        this.fix = -0.06;
                    else if (Objects.equals(scope.getTagName(), "acog4x"))
                        this.fix = -0.03;
                    else if (Objects.equals(scope.getTagName(), "vlpvo6"))
                        this.fix = -0.05;
                    else if (Objects.equals(scope.getTagName(), "gener8x"))
                        this.fix = -0.06;
                    else if (Objects.equals(scope.getTagName(), "aimpoint2") ||
                            Objects.equals(scope.getTagName(), "eotechn") ||
                            Objects.equals(scope.getTagName(), "vortex1") ||
                            Objects.equals(scope.getTagName(), "eotechshort"))
                        this.fix = -0.02;
                    else
                        this.fix = 0;
//                    }
                } else if (modifiedGun.canAttachType(IAttachment.Type.OLD_SCOPE) && scope != null) {
                    double viewFinderOffset = isScopeOffsetType || isScopeRenderType ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset(); // switches between either, but either must be populated
                    if (OptifineHelper.isShadersEnabled()) viewFinderOffset *= 0.735;
                    Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getOldScope();
                    xOffset = -translateX + (modifiedGun.getModules().getZoom().getXOffset() * 0.0625) + -scaledPos.getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                    zOffset = -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - viewFinderOffset * scaleZ * scaledPos.getScale();

//                    if (!SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.AIMING)) {
                    this.fix = -0.05;
//                    }
                } else if (modifiedGun.canAttachType(IAttachment.Type.PISTOL_SCOPE) && scope != null) {
                    double viewFinderOffset = isScopeOffsetType || isScopeRenderType ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset(); // switches between either, but either must be populated
                    if (OptifineHelper.isShadersEnabled()) viewFinderOffset *= 0.735;
                    Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getPistolScope();

                    xOffset = -translateX + (modifiedGun.getModules().getZoom().getXOffset() * 0.0625) + -scaledPos.getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                    zOffset = Config.CLIENT.display.sight1xRealisticPosition.get() && scope.getAdditionalZoom().getZoomMultiple() == 1 ? -translateZ + modifiedGun.getModules().getZoom().getZOffset() * 0.0625 * scaleZ :
                            -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - viewFinderOffset * scaleZ * scaledPos.getScale();

//                    if (!SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.AIMING)) {
                    this.fix = 0;
//                    }
                } else if (modifiedGun.getModules().getZoom() != null) {
                    xOffset = -translateX + modifiedGun.getModules().getZoom().getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - modifiedGun.getModules().getZoom().getYOffset() - 0.2) * 0.0625 * scaleY;
                    zOffset = -translateZ + modifiedGun.getModules().getZoom().getZOffset() * 0.0625 * scaleZ;
                    this.fix = 0;
                }


                /* Controls the direction of the following translations, changes depending on the main hand. */
                float side = right ? 1.0F : -1.0F;
                //double transition = 1.0 - Math.pow(1.0 - AimingHandler.get().getNormalisedRepairProgress(), 2);

                double transition = (float) AimingHandler.get().getLerpAdsProgress(event.getPartialTicks());

                float function = (float) (3f * Math.pow(transition - 0.33f, 2) - 0.33f);
                if (function > 1f)
                    function = 1f;
                if (transition == 0)
                    function = 0;

                float result = aimingDynamics.update(0.05f, function);
                float resultFix = Math.min(result, 1f);
                float resultZ = aimingDynamics.update(0.05f, (float) transition);
                float resultZFix = Math.min(resultZ, 1f);

                /* Reverses the original first person translations */
                //matrixStack.translate(-0.56 * side * transition, 0.52 * transition, 0);
                matrixStack.translate(xOffset * side * resultZ - 0.56 * side * resultZ,
                        (yOffset * result + 0.52 * resultFix - 0.07 + Math.abs(0.5 - resultZFix) * 0.14 + this.fix),
                        zOffset * result - 0.036 + 0.06 * Math.abs(0.6 - resultZ));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees((5 * (1 - result))));
                /* Reverses the first person translations of the item in order to position it in the center of the screen */
                //matrixStack.translate(xOffset * side * transition, yOffset * transition, zOffset * transition);
                matrixStack.translate(0, (0.013 - this.fix) * resultFix, 0);

                if (Config.COMMON.gameplay.realisticAimedBreathing.get()) {
                    /* Apply scope jitter*/
                    double scopeJitterOffset = 0.8;
                    if (scope == null)
                        scopeJitterOffset *= modifiedGun.getModules().getZoom().getStabilityOffset();
                    else
                        scopeJitterOffset *= scope.getStabilityOffset();
                    if (entity.isCrouching())
                        scopeJitterOffset *= 0.30;
                    if (entity.isSprinting() && !entity.isCrouching())
                        scopeJitterOffset *= 4;
                    if (entity.getMotion().getX() != 0.0 || entity.getMotion().getY() != 0.0 || entity.getMotion().getZ() != 0.0)
                        scopeJitterOffset *= 6.5;

                    double yOffsetRatio = ScopeJitterHandler.getInstance().getYOffsetRatio() * (0.0125 * 0.75 * scopeJitterOffset);
                    double xOffsetRatio = ScopeJitterHandler.getInstance().getXOffsetRatio() * (0.0085 * 0.875 * scopeJitterOffset);
                    Objects.requireNonNull(Minecraft.getInstance().player).rotationPitch += yOffsetRatio;
                    Objects.requireNonNull(Minecraft.getInstance().player).rotationYaw += xOffsetRatio;
                }
            }
        }

        //Set Delayed Sway config options

        maxRotationDegree = Config.CLIENT.display.weaponDelayedSwayMaximum.get().floatValue(); // 3.95f or 4.5f
        delayedSwayMultiplier = Config.CLIENT.display.weaponDelayedSwayMultiplier.get().floatValue(); // Lower = a more delayed sway
        YDIR = Config.CLIENT.display.weaponDelayedSwayDirection.get() ? Vector3f.YN : Vector3f.YP;

        applyDelayedSwayTransforms(matrixStack, entity, event.getPartialTicks());
        float equipProgress = this.getEquipProgress(event.getPartialTicks());
        matrixStack.rotate(Vector3f.XP.rotationDegrees(equipProgress * -50F));

        HandSide hand = right ? HandSide.RIGHT : HandSide.LEFT;
        Objects.requireNonNull(entity);
        int blockLight = entity.isBurning() ? 15 : entity.world.getLightFor(LightType.BLOCK, new BlockPos(entity.getEyePosition(event.getPartialTicks())));
        blockLight += (this.entityIdForMuzzleFlash.contains(entity.getEntityId()) ? 3 : 0); // 3
        blockLight = Math.min(blockLight, 15);
        int packedLight = LightTexture.packLight(blockLight, entity.world.getLightFor(LightType.SKY, new BlockPos(entity.getEyePosition(event.getPartialTicks()))));

        /* Translate the item position based on the hand side */
        int offset = right ? 1 : -1;
        matrixStack.translate(0.56 * offset, -0.52, -0.72);

        this.applySprintingTransforms(heldItem, hand, matrixStack, event.getPartialTicks());
        /* Applies recoil and reload rotations */
        this.applyRecoilTransforms(matrixStack, heldItem, modifiedGun);
        if (!isAnimated) this.applyReloadTransforms(matrixStack, hand, event.getPartialTicks(), heldItem);

        /* Renders the first persons arms from the grip type of the weapon */
        matrixStack.push();
        IHeldAnimation pose = modifiedGun.getGeneral().getGripType().getHeldAnimation();
        if (pose != null) {
            if (!isAnimated) matrixStack.translate(-0.56, 0.52, 0.72);
            if (!isAnimated)
                pose.renderFirstPersonArms(Minecraft.getInstance().player, hand, heldItem, matrixStack, event.getBuffers(), event.getLight(), event.getPartialTicks());
        }
        matrixStack.pop();


        /* Renders the weapon */
        ItemCameraTransforms.TransformType transformType = right ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        this.renderWeapon(Minecraft.getInstance().player, heldItem, transformType, event.getMatrixStack(), event.getBuffers(), packedLight, event.getPartialTicks());
        matrixStack.pop();
    }

    private static float maxRotationDegree; // 3.95f or 4.5f
    private static float delayedSwayMultiplier; // Lower = a more delayed sway
    private static Vector3f YDIR;


    private void applyDelayedSwayTransforms(MatrixStack stack, ClientPlayerEntity player, float partialTicks) {
        applyDelayedSwayTransforms(stack, player, partialTicks, 1f);
    }

    private boolean checkIsLongRangeScope(ItemStack itemStack){
        Gun gun = ((GunItem) itemStack.getItem()).getModifiedGun(itemStack);
        IAttachment.Type type = IAttachment.Type.SCOPE;
        if (gun.canAttachType(type)) {
            ItemStack attachmentStack = Gun.getAttachment(type, itemStack);
            if (!attachmentStack.isEmpty()) {

            }
        }
        return false;
    }

    public void applyDelayedSwayTransforms(MatrixStack stack, ClientPlayerEntity player, float partialTicks, float percentage) {
        if (Config.CLIENT.display.weaponDelayedSway.get())
            if (player != null) {
                float f4 = MathHelper.lerp(partialTicks, player.prevRenderArmYaw, player.renderArmYaw);
                float degree = delaySwayDynamics.update(0, (player.getYaw(partialTicks) - f4) * delayedSwayMultiplier);
                if (Math.abs(degree) > maxRotationDegree) degree = degree / Math.abs(degree) * maxRotationDegree;
                degree *= 1 / Math.pow(MathUtil.fovToMagnification(currentHandLayerFov, originHandLayerFov), 2);
                degree *= percentage;

                if ((Config.CLIENT.display.weaponDelayedSwayYNOptical.get() && Gun.getScope(player.getHeldItemMainhand()) != null) || YDIR.equals(Vector3f.YN)) {
                    stack.translate(this.translateX, this.translateY, this.translateZ);
                    stack.rotate(YDIR.rotationDegrees(degree));
                    stack.rotate(Vector3f.ZP.rotationDegrees(degree * 1.5f * (float) (1f - AimingHandler.get().getNormalisedAdsProgress())));
                    stack.translate(-this.translateX, -this.translateY, -this.translateZ);
                } else {
                    stack.translate(-this.translateX, -this.translateY, -this.translateZ);
                    stack.rotate(YDIR.rotationDegrees(degree));
                    stack.rotate(Vector3f.ZP.rotationDegrees(degree * 1.5f * (float) (1f - AimingHandler.get().getNormalisedAdsProgress())));
                    stack.translate(this.translateX, this.translateY, this.translateZ);
                }
            }
    }

    // Sprinting Offset Transition, the same transition aggregate used for all running anims,
    // made public for adjusting hands within animator instances
    public float sOT = 0.0f;
    public float wSpeed = 0.0f;

    private void applyLightWeightAnimation(MatrixStack matrixStack, float leftHanded, float draw) {
        float result = sprintDynamicsHSS.update(0.05f, sOT) * draw;
        float result2 = sprintDynamicsZHSS.update(0.05f, sOT) * draw;

        matrixStack.translate(0.215 * leftHanded * result,
                0.07f * result, -30F * leftHanded * result / 170);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(60f * result2));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(-25f * result2));
    }

    private void applyDefaultAnimation(MatrixStack matrixStack, float leftHanded, float draw) {
        float result = sprintDynamics.update(0.05f, sOT) * draw;
        float result2 = sprintDynamicsZ.update(0.05f, sOT) * draw;

        matrixStack.translate(-0.25 * leftHanded * result, -0.1 * result - 0.1 + Math.abs(0.5 - result) * 0.2, 0);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(28F * leftHanded * result));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(15F * result2));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(20f * result2));
    }

    private void applySprintingTransforms(ItemStack gun, HandSide hand,
                                          MatrixStack matrixStack, float partialTicks) {
        TimelessGunItem modifiedGun = (TimelessGunItem) gun.getItem();
        GunAnimationController controller = GunAnimationController.fromItem(gun.getItem());
        float draw = (controller == null || !controller.isAnimationRunning(GunAnimationController.AnimationLabel.DRAW) ? 1 : 0);
        float leftHanded = hand == HandSide.LEFT ? -1 : 1;
        this.sOT = (this.prevSprintTransition + (this.sprintTransition - this.prevSprintTransition) * partialTicks) / 5F;
        this.wSpeed = getGunWeightSpeed(modifiedGun.getGun(), gun);
        // Lightweight animation, used for SMGS and light rifles like the hk416
        if (wSpeed > 0.094f) {
            applyLightWeightAnimation(matrixStack, leftHanded, draw);
        } else {
            applyDefaultAnimation(matrixStack, leftHanded, draw);
        }
    }

    private static float getGunWeightSpeed(Gun gunType, ItemStack gun) {
        return ServerPlayHandler.calceldGunWeightSpeed(gunType, gun);
    }

    private void applyReloadTransforms(MatrixStack matrixStack, HandSide hand, float partialTicks, ItemStack modifiedGun) {
        float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks, modifiedGun);

        if (reloadProgress > 0) {
            float leftHanded = hand == HandSide.LEFT ? -1 : 1;

            matrixStack.translate(-0.25 * leftHanded, -0.1, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(45F * leftHanded));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-25F));
        }
    }

    public float kickReduction = 0;
    public float recoilReduction = 0;
    public double kick = 0;
    public float recoilLift = 0;
    public float newSwayYawAmount = 0;
    public float weaponsHorizontalAngle = 0;
    public float newSwayPitch = 0;

    public float newSwayYaw = 0;

    public float newSwayYawPitch = 0;
    public float newSwayYawYaw = 0;

    private void applyRecoilTransforms(MatrixStack matrixStack, ItemStack item, Gun gun) {
        Minecraft mc = Minecraft.getInstance();
        double kickReduce = 1;
        double recoilNormal = RecoilHandler.get().getGunRecoilNormal();
        if (Gun.hasAttachmentEquipped(item, gun, IAttachment.Type.SCOPE) || Gun.hasAttachmentEquipped(item, gun, IAttachment.Type.PISTOL_SCOPE) || Gun.hasAttachmentEquipped(item, gun, IAttachment.Type.OLD_SCOPE)) {
            recoilNormal -= recoilNormal * (0.25 * AimingHandler.get().getNormalisedAdsProgress());
            kickReduce = gun.getModules().getZoom().getFovModifier();
            if (kickReduce > 1)
                kickReduce = 1;
            if (kickReduce < 0)
                kickReduce = 0;
        }
        this.kickReduction = 1.0F - GunModifierHelper.getKickReduction(item);
        this.recoilReduction = 1.0F - GunModifierHelper.getRecoilModifier(item);
        this.kick = gun.getGeneral().getRecoilKick() * 0.0625 * recoilNormal * RecoilHandler.get().getAdsRecoilReduction(gun) * kickReduce;
        //this.recoilLift = ((float) (gun.getGeneral().getRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun));
        this.recoilLift = ((float) (RecoilHandler.get().getGunRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun));
        this.newSwayYawAmount = ((float) (2F + 1F * (1.0 - AimingHandler.get().getNormalisedAdsProgress())));// * 1.5f;
        this.newSwayYawPitch = ((float) ((RecoilHandler.get().lastRandPitch * this.newSwayYawAmount - this.newSwayYawAmount / 2F) * recoilNormal)) / 2;
        this.newSwayYawYaw = ((float) ((RecoilHandler.get().lastRandYaw * this.newSwayYawAmount - this.newSwayYawAmount / 2F) * recoilNormal)) / 2;
        float kickTiming = 0.11f;
        if (IDLNBTUtil.getInt(item, "CurrentFireMode") == 1) {
            this.newSwayYawAmount *= 0.5;
            this.recoilLift *= 0.925;
            kickTiming += 0.06f; // Soften the kick a little, helps with tracking and feel
        }
        if (mc.player != null && mc.player.isCrouching()) {
            this.recoilLift *= 0.875;
        }
        this.weaponsHorizontalAngle = ((float) (RecoilHandler.get().getGunHorizontalRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun));
        float newKick = recoilDynamics.update(kickTiming, (float) kick * kickReduction);

        //reduce for scope sight
        double magnification = MathUtil.fovToMagnification(currentHandLayerFov, originHandLayerFov);
        newKick *= 1 / Math.pow(magnification, 0.3);

        matrixStack.translate(0, 0, newKick);
        matrixStack.translate(0, 0.05 * newKick, 0.35 * newKick);

        // TODO: have T/Time updatable per gun, weapons like the pistols, especially the deagle benifits from forcing accurate shots and awaiting front sight reset, unlike the m4 which should have little effect
        newSwayYaw = swayYawDynamics.update(0.09f, newSwayYawYaw * recoilReduction * weaponsHorizontalAngle);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(newSwayPitch * 0.2875f));
        newSwayPitch = swayPitchDynamics.update(0.21f, newSwayYawPitch * recoilReduction * recoilLift);
        matrixStack.rotate(Vector3f.ZN.rotationDegrees(newSwayPitch * 0.215f));

        //matrixStack.rotate(Vector3f.ZP.rotationDegrees(newSwayYaw * recoilReduction)); // seems to be interesting to increase the force of

        if (gun.getGeneral().getWeaponRecoilOffset() != 0)
            matrixStack.rotate(Vector3f.XP.rotationDegrees(this.recoilLift * this.recoilReduction));
        matrixStack.translate(0, -0.05 * newKick, -0.35 * newKick);
    }

    private int backwardTicker = 0;

    public void applyBobbingTransforms(MatrixStack matrixStack, boolean convert) {
        matrixStack.translate(0, 0, 0.25);
        float amplifier = bobbingDynamics.update(0.05f, (float) ((sprintTransition / 2f + 1) * (1 - AimingHandler.get().getNormalisedAdsProgress() * 0.75) /** (RecoilHandler.get().getRecoilProgress() == 0 ? 1 : 0))*/));
        float speedUp = speedUpDynamics.update(0.05f, speedUpProgress * (1 - sOT) * (float) (1 - AimingHandler.get().getNormalisedAdsProgress()));
        float delta = -MathHelper.sin(walkingDistance1 * (float) Math.PI) * walkingCameraYaw * 0.5f * (convert ? -0.5f : 1) * amplifier * (8 - backwardTicker) / 8;
        float delta2 = -MathHelper.sin(walkingDistance1 * (float) Math.PI * 2f) * walkingCameraYaw * 0.5f * (convert ? -0.35f : 1) * amplifier * (12 - backwardTicker) / 12;
        matrixStack.rotate(Vector3f.YP.rotationDegrees(35f * delta * (float) (1 - AimingHandler.get().getNormalisedAdsProgress())));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-3f * speedUp));
        matrixStack.translate(0, 0, -0.25 + 0.07 * speedUp);
        matrixStack.translate(0.45 * delta, 0.25 * delta2, 0);
        if (wSpeed > 0.094f) matrixStack.rotate(Vector3f.XP.rotationDegrees(delta * 5f * sprintTransition));
        else matrixStack.rotate(Vector3f.XP.rotationDegrees(delta * 5f));
    }

    public void applyBobbingTransforms(MatrixStack matrixStack, boolean convert, float effectMultiplier) {
        if (effectMultiplier == 0)
            effectMultiplier = 1f;
        matrixStack.translate(0, 0, 0.25);
        float amplifier = bobbingDynamics.update(0.05f, (float) ((sprintTransition / 2f + 1) * (1 - AimingHandler.get().getNormalisedAdsProgress() * 0.75) * (RecoilHandler.get().getRecoilProgress() == 0 ? 1 : 0)));
        float speedUp = speedUpDynamics.update(0.05f, speedUpProgress * (1 - sOT) * (float) (1 - AimingHandler.get().getNormalisedAdsProgress()));
        float delta = -MathHelper.sin(walkingDistance1 * (float) Math.PI) * walkingCameraYaw * 0.5f * (convert ? -0.5f : 1) * amplifier * (8 - backwardTicker) / 8;
        float delta2 = -MathHelper.sin(walkingDistance1 * (float) Math.PI * 2f) * walkingCameraYaw * 0.5f * (convert ? -0.35f : 1) * amplifier * (12 - backwardTicker) / 12;
        matrixStack.rotate(Vector3f.YP.rotationDegrees(35f * effectMultiplier * delta * (float) (1 - AimingHandler.get().getNormalisedAdsProgress())));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-3f * effectMultiplier * speedUp));
        matrixStack.translate(0, 0, -0.25 + 0.07 * effectMultiplier * speedUp);
        matrixStack.translate(0.45 * effectMultiplier * delta, 0.25 * effectMultiplier * delta2, 0);
        if (wSpeed > 0.094f)
            matrixStack.rotate(Vector3f.XP.rotationDegrees(delta * effectMultiplier * 5f * sprintTransition));
        else matrixStack.rotate(Vector3f.XP.rotationDegrees(delta * effectMultiplier * 5f));
    }

    private float velocity = 0;
    private float acceleration = 0;
    private float stepLength = 0;
    private long prevTime = new Date().getTime();

    public void applyJumpingTransforms(MatrixStack matrixStack, float partialTicks) {
        if (Minecraft.getInstance().player == null) return;
        double posY = MathHelper.lerp(partialTicks, Minecraft.getInstance().player.lastTickPosY, Minecraft.getInstance().player.getPosY());
        float newVelocity = (float) (posY - Minecraft.getInstance().player.lastTickPosY) / partialTicks;
        float newAcceleration = newVelocity - velocity;
        Date date = new Date();
        if (Math.abs(acceleration) < Math.abs(newAcceleration) && Math.abs(newAcceleration) > 0.05) {
            acceleration = newAcceleration;
            stepLength = acceleration / 250f;
        }
        long partialTime = date.getTime() - prevTime;
        if (acceleration > 0) {
            acceleration -= partialTime * stepLength;
            if (acceleration < 0) acceleration = 0;
        }
        if (acceleration < 0) {
            acceleration -= partialTime * stepLength;
            if (acceleration > 0) acceleration = 0;
        }
        float maxMotion = 0.265f;
        float transition = -jumpingDynamics.update(0.05f, (Math.abs(acceleration) < maxMotion ? (acceleration / maxMotion) * 0.15f : Math.abs(acceleration) / acceleration * 0.15f) * (sprintTransition / 3f + 1) * (1f - 0.7f * (float) AimingHandler.get().getNormalisedAdsProgress()));
        if (transition > 0) transition *= 0.8f;
        matrixStack.translate(0, transition, 0);
        velocity = newVelocity;
        prevTime = date.getTime();
    }

    public void applyJumpingTransforms(MatrixStack matrixStack, float partialTicks, float reverser) {
        if (Minecraft.getInstance().player == null) return;
        double posY = MathHelper.lerp(partialTicks, Minecraft.getInstance().player.lastTickPosY, Minecraft.getInstance().player.getPosY());
        float newVelocity = (float) (posY - Minecraft.getInstance().player.lastTickPosY) / partialTicks;
        float newAcceleration = newVelocity - velocity;
        Date date = new Date();
        if (Math.abs(acceleration) < Math.abs(newAcceleration) && Math.abs(newAcceleration) > 0.05) {
            acceleration = newAcceleration;
            stepLength = acceleration / 250f;
        }
        long partialTime = date.getTime() - prevTime;
        if (acceleration > 0) {
            acceleration -= partialTime * stepLength;
            if (acceleration < 0) acceleration = 0;
        }
        if (acceleration < 0) {
            acceleration -= partialTime * stepLength;
            if (acceleration > 0) acceleration = 0;
        }
        float maxMotion = 0.265f;
        float transition = -jumpingDynamics.update(0.05f, (Math.abs(acceleration) < maxMotion ? (acceleration / maxMotion) * 0.15f : Math.abs(acceleration) / acceleration * 0.15f) * (sprintTransition / 3f + 1) * (1f - 0.7f * (float) AimingHandler.get().getNormalisedAdsProgress()));
        if (transition > 0) transition *= 0.8f;
        if (reverser != 0) transition *= reverser;
        matrixStack.translate(0, transition, 0);
        velocity = newVelocity;
        prevTime = date.getTime();
    }


    // TODO: Update noises for breathing animation per new weapon held, aka give weapons customization of their breathing, pistols for example should be realatively unstable, along with lighter weapons
    private final OneDimensionalPerlinNoise noiseX = new OneDimensionalPerlinNoise(-0.003f, 0.003f, 2400);
    private final OneDimensionalPerlinNoise noiseY = new OneDimensionalPerlinNoise(-0.003f, 0.003f, 2800);
    {
        noiseY.setReverse(true);
    }
    private final OneDimensionalPerlinNoise aimed_noiseX = new OneDimensionalPerlinNoise(-0.00075f, 0.00075f, 1650);
    private final OneDimensionalPerlinNoise aimed_noiseY = new OneDimensionalPerlinNoise(-0.00135f, 0.00135f, 1650);
    {
        noiseY.setReverse(true);
    }
    private final OneDimensionalPerlinNoise additionNoiseY = new OneDimensionalPerlinNoise(-0.002f, 0.002f, 1300);

    private final OneDimensionalPerlinNoise noiseRotationY = new OneDimensionalPerlinNoise(-0.8f, 0.8f, 2000);
    private final OneDimensionalPerlinNoise aimed_noiseRotationY = new OneDimensionalPerlinNoise(-0.25f, 0.25f, 1600);

    public void applyNoiseMovementTransform(MatrixStack matrixStack) {
        //matrixStack.translate(noiseX.getValue()* (1 - AimingHandler.get().getNormalisedRepairProgress()), (noiseY.getValue() + additionNoiseY.getValue()) * (1 - AimingHandler.get().getNormalisedRepairProgress()), 0);
        if (AimingHandler.get().getNormalisedAdsProgress() == 1) {
            matrixStack.translate(aimed_noiseX.getValue(), aimed_noiseY.getValue(), 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(aimed_noiseRotationY.getValue()));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees((float) (aimed_noiseRotationY.getValue() * 0.85)));
        } else {
            matrixStack.translate(noiseX.getValue(), noiseY.getValue(), 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(noiseRotationY.getValue()));
        }

    }

    //TODO: Clumsy, simplify this crap
    public void applyNoiseMovementTransform(MatrixStack matrixStack, float reverser) {
        //matrixStack.translate(noiseX.getValue()* (1 - AimingHandler.get().getNormalisedRepairProgress()), (noiseY.getValue() + additionNoiseY.getValue()) * (1 - AimingHandler.get().getNormalisedRepairProgress()), 0);
        if (AimingHandler.get().getNormalisedAdsProgress() == 1) {
            matrixStack.translate(aimed_noiseX.getValue() * (reverser * 2), aimed_noiseY.getValue() * reverser, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(aimed_noiseRotationY.getValue() * reverser));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees((float) (aimed_noiseRotationY.getValue() * 0.85 * reverser)));
        } else {
            matrixStack.translate(noiseX.getValue() * (reverser * 2), noiseY.getValue() * reverser, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(noiseRotationY.getValue() * reverser));
        }

    }

    // Author: https://github.com/Charles445/DamageTilt/blob/1.16/src/main/java/com/charles445/damagetilt/MessageUpdateAttackYaw.java, continued by Timeless devs
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onKnockback(LivingKnockBackEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (player.world.isRemote)
                return;
            if (!(player.getHeldItemMainhand().getItem() instanceof GunItem) && !Config.CLIENT.display.cameraShakeOptionGlobal.get())
                return;

            //Server Side
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                if (serverPlayer.connection == null)
                    return;
                serverPlayer.connection.getNetworkManager();
                PacketHandler.getPlayChannel().sendTo(new MessagePlayerShake(player), serverPlayer.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START))
            return;

        Minecraft mc = Minecraft.getInstance();
        if (!mc.isGameFocused())
            return;

        PlayerEntity player = mc.player;
        if (player == null)
            return;

        if (Minecraft.getInstance().gameSettings.getPointOfView() != PointOfView.FIRST_PERSON)
            return;

        ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
        if (heldItem.isEmpty())
            return;

        if (player.isHandActive() && player.getActiveHand() == Hand.MAIN_HAND && heldItem.getItem() instanceof GrenadeItem) {
            if (!((GrenadeItem) heldItem.getItem()).canCook())
                return;

            int duration = player.getItemInUseMaxCount();
            if (duration >= 10) {
                float cookTime = 1.0F - ((float) (duration - 10) / (float) (player.getActiveItemStack().getUseDuration() - 10));
                if (cookTime > 0.0F) {
                    double scale = 3;
                    MainWindow window = mc.getMainWindow();
                    int i = (int) ((window.getScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);

                    RenderSystem.pushMatrix();
                    {
                        RenderSystem.scaled(scale, scale, scale);
                        int progress = (int) Math.ceil((cookTime) * 17.0F) - 1;
                        MatrixStack matrixStack = new MatrixStack();
                        Screen.blit(matrixStack, j, i, 36, 94, 16, 4, 256, 256);
                        Screen.blit(matrixStack, j, i, 52, 94, progress, 4, 256, 256);
                    }
                    RenderSystem.popMatrix();

                    RenderSystem.disableBlend();
                }
            }
            return;
        }

        /*if (heldItem.getItem() instanceof GunItem) {
            Gun gun = ((GunItem) heldItem.getItem()).getGun(); // Cooldown stuffs, i should look into this
            if (!gun.getGeneral().isAuto()) {
                float coolDown = player.getCooldownTracker().getCooldown(heldItem.getItem(), event.renderTickTime);
                if (coolDown > 0.0F) {
                    double scale = 3;
                    MainWindow window = mc.getMainWindow();
                    int i = (int) ((window.getScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);

                    RenderSystem.pushMatrix();
                    {
                        RenderSystem.scaled(scale, scale, scale);
                        int progress = (int) Math.ceil((coolDown + 0.05) * 17.0F) - 1;
                        MatrixStack matrixStack = new MatrixStack();
                        Screen.blit(matrixStack, j, i, 36, 94, 16, 4, 256, 256);
                        Screen.blit(matrixStack, j, i, 52, 94, progress, 4, 256, 256);
                    }
                    RenderSystem.popMatrix();

                    RenderSystem.disableBlend();
                }
            }
        }*/
    }

    @SubscribeEvent
    public void onRenderHeldItem(RenderItemEvent.Held.Pre event) {
        Hand hand = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHandSide() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND : event.getHandSide() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND;
        LivingEntity entity = event.getEntity();
        ItemStack heldItem = entity.getHeldItem(hand);

        if (hand == Hand.OFF_HAND) {
            if (heldItem.getItem() instanceof GunItem) {
                event.setCanceled(true);
                return;
            }

            if (entity.getHeldItemMainhand().getItem() instanceof GunItem) {
                Gun modifiedGun = ((GunItem) entity.getHeldItemMainhand().getItem()).getModifiedGun(entity.getHeldItemMainhand());
                if (!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem()) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if (heldItem.getItem() instanceof GunItem) {
            event.setCanceled(true);

            if (heldItem.getTag() != null) {
                CompoundNBT compound = heldItem.getTag();
                if (compound.contains("Scale", Constants.NBT.TAG_FLOAT)) {
                    float scale = compound.getFloat("Scale");
                    event.getMatrixStack().scale(scale, scale, scale);
                }
            }

            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if (entity instanceof PlayerEntity) {
                gun.getGeneral().getGripType().getHeldAnimation().applyHeldItemTransforms((PlayerEntity) entity, hand, AimingHandler.get().getAimProgress((PlayerEntity) entity, event.getPartialTicks()), event.getMatrixStack(), event.getRenderTypeBuffer());
            }
            this.renderWeapon(entity, heldItem, event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void onSetupAngles(PlayerModelEvent.SetupAngles.Post event) {
        // Dirty hack to reject first person arms
        if (event.getAgeInTicks() == 0F) {
            event.getModelPlayer().bipedRightArm.rotateAngleX = 0;
            event.getModelPlayer().bipedRightArm.rotateAngleY = 0;
            event.getModelPlayer().bipedRightArm.rotateAngleZ = 0;
            event.getModelPlayer().bipedLeftArm.rotateAngleX = 0;
            event.getModelPlayer().bipedLeftArm.rotateAngleY = 0;
            event.getModelPlayer().bipedLeftArm.rotateAngleZ = 0;
            return;
        }

        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem) {
            PlayerModel model = event.getModelPlayer();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerModelRotation(player, model, Hand.MAIN_HAND, AimingHandler.get().getAimProgress((PlayerEntity) event.getEntity(), event.getPartialTicks()));
            copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
            copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
        }
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest) {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem) {
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerPreRender(player, Hand.MAIN_HAND, AimingHandler.get().getAimProgress((PlayerEntity) event.getEntity(), event.getPartialRenderTick()), event.getMatrixStack(), event.getBuffers());
        }
    }

    @SubscribeEvent
    public void onModelRender(PlayerModelEvent.Render.Pre event) {
        PlayerEntity player = event.getPlayer();
        ItemStack offHandStack = player.getHeldItemOffhand();
        if (offHandStack.getItem() instanceof GunItem) {
            switch (player.getPrimaryHand().opposite()) {
                case LEFT:
                    event.getModelPlayer().leftArmPose = BipedModel.ArmPose.EMPTY;
                    break;
                case RIGHT:
                    event.getModelPlayer().rightArmPose = BipedModel.ArmPose.EMPTY;
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(PlayerModelEvent.Render.Post event) {
        MatrixStack matrixStack = event.getMatrixStack();
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemOffhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem) {
            matrixStack.push();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if (gun.getGeneral().getGripType().getHeldAnimation().applyOffhandTransforms(player, event.getModelPlayer(), heldItem, matrixStack, event.getPartialTicks())) {
                IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, event.getLight(), event.getPartialTicks());
            }
            matrixStack.pop();
        }
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Entity.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (!event.getTransformType().equals(ItemCameraTransforms.TransformType.GUI)) {
            event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
        }
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Gui.Pre event) {

        //MK47AnimationController x = MK47AnimationController.getInstance();
        //PlayerHandAnimation.render(x,event.getTransformType(),event.getMatrixStack(),event.getRenderTypeBuffer(),event.getLight());
        if (!Config.CLIENT.quality.reducedQualityHotBar.get()/* && event.getTransformType().equals(ItemCameraTransforms.TransformType.GUI)*/) {
            Minecraft mc = Minecraft.getInstance();
            event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
        }
        // TODO: Enable some form of either player hand anim preloading, or on game load segment for the held gun, since it seems 90%+ cases don't miss loading hand animations
    }

    @SubscribeEvent
    public void onRenderItemFrame(RenderItemEvent.ItemFrame.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
    }

    public float aimingHandLayerFov = 6.41236f;
    public float originHandLayerFov = 70;
    public float currentHandLayerFov = 70;
    public boolean renderWeapon(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        if (stack.getItem() instanceof GunItem) {
            matrixStack.push();

            ItemStack model = ItemStack.EMPTY;
            if (stack.getTag() != null) {
                if (stack.getTag().contains("Model", Constants.NBT.TAG_COMPOUND)) {
                    model = ItemStack.read(stack.getTag().getCompound("Model"));
                }
            }

            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, matrixStack, transformType, entity);

            if(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND.equals(transformType)) {
                Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                IAttachment.Type type = IAttachment.Type.SCOPE;
                if (gun.canAttachType(type)) {
                    Scope scope = Gun.getScope(stack);
                    if (scope != null) {
                        ItemStack attachmentStack = Gun.getAttachment(type, stack);
                        if (!attachmentStack.isEmpty()) {
                            Gun.ScaledPositioned positioned = gun.getAttachmentPosition(type);
                            if (positioned != null) {
                                double transition = AimingHandler.get().getLerpAdsProgress(partialTicks);
                                double displayX = positioned.getXOffset() * 0.0625;
                                double displayY = positioned.getYOffset() * 0.0625;
                                double displayZ = positioned.getZOffset() * 0.0625;
                                currentHandLayerFov = MathHelper.lerp((float) transition, originHandLayerFov, scope.isNeedSqueeze() ? aimingHandLayerFov : 55f);
                                float zScale = (float) Math.tan(currentHandLayerFov / 180 * Math.PI / 2) / (float) Math.tan(originHandLayerFov / 180 * Math.PI / 2);
                                matrixStack.translate(displayX, displayY, displayZ);
                                matrixStack.translate(0, -0.5, 0);
                                matrixStack.scale(1f, 1f, zScale);
                                matrixStack.translate(0, 0.5, 0);
                                matrixStack.translate(-displayX, -displayY, -displayZ);
                                matrixStack.translate(0, 0, (float) transition * scope.getAdditionalZoom().getZoomZTransition() * 0.0625 / zScale);
                            }
                        }
                    }
                }
            }

            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderAttachments(entity, transformType, stack, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderMuzzleFlash(entity, matrixStack, renderTypeBuffer, stack, transformType);
            this.renderShellCasing(entity, matrixStack, renderTypeBuffer, stack, transformType, renderTypeBuffer, light, partialTicks);
            matrixStack.pop();
            return true;
        }
        return false;
    }

    public boolean renderScope(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        if (stack.getItem() instanceof ScopeItem || stack.getItem() instanceof PistolScopeItem || stack.getItem() instanceof OldScopeItem) {
            matrixStack.push();

            ItemStack model = ItemStack.EMPTY;
            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, matrixStack, transformType, entity);
            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, matrixStack, renderTypeBuffer, light, partialTicks);//matrixStack, renderTypeBuffer, light, partialTicks);
            matrixStack.pop();
            return true;
        }
        return false;
    }

    /*public boolean renderColored(LivingEntity entity, IBakedModel model, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {

            matrixStack.push();

            RenderUtil.applyTransformTypeIB(model, matrixStack, transformType, entity);

            this.renderColoredModel(entity, transformType, model, matrixStack, renderTypeBuffer, light, partialTicks);//matrixStack, renderTypeBuffer, light, partialTicks);
            //this.renderAttachments(entity, transformType, stack, matrixStack, renderTypeBuffer, light, partialTicks);
            //this.renderMuzzleFlash(entity, matrixStack, renderTypeBuffer, stack, transformType);

            matrixStack.pop();
            return true;

        //return false;
    }*/
    private void renderGun(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        /*if(ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GUI))
            return;*/
        if (stack.getItem() instanceof ITimelessAnimated)
            RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, entity);
        if (ModelOverrides.hasModel(stack)) {
            IOverrideModel model = ModelOverrides.getModel(stack);
            if (model != null) {

                //TODO: Only when needed
                if (ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GUI) && !Config.CLIENT.quality.reducedQualityHotBar.get()) {
                    matrixStack.push();
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(25.0F));
                    matrixStack.rotate(Vector3f.YP.rotationDegrees(-145.0F));
                    matrixStack.scale(0.55f, 0.55f, 0.55f);
                }
                model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
            }
        } else {
            RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, entity);
        }
        if (ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GUI) && !Config.CLIENT.quality.reducedQualityHotBar.get())
            matrixStack.pop();
    }
    /*private void renderColoredModel(LivingEntity entity, ItemCameraTransforms.TransformType transformType, IBakedModel model, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        //if(stack.getItem() instanceof ITimelessAnimated) RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, entity);
        IOverrideModel model = ModelOverrides.getModel(stack);
        if (model != null) {
            model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
        }
    }*/

    private void renderAttachments(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        if (stack.getItem() instanceof GunItem) {
            Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
            CompoundNBT gunTag = stack.getOrCreateTag();
            CompoundNBT attachments = gunTag.getCompound("Attachments");
            for (String tagKey : attachments.keySet()) {
                IAttachment.Type type = IAttachment.Type.byTagKey(tagKey);
                if (gun.canAttachType(type)) {
                    ItemStack attachmentStack = Gun.getAttachment(type, stack);
                    if (!attachmentStack.isEmpty()) {
                        Gun.ScaledPositioned positioned = gun.getAttachmentPosition(type);
                        if (positioned != null) {
                            double displayX = positioned.getXOffset() * 0.0625;
                            double displayY = positioned.getYOffset() * 0.0625;
                            double displayZ = positioned.getZOffset() * 0.0625;
                            matrixStack.push();
                            GunAnimationController controller = GunAnimationController.fromItem(stack.getItem());
                            if (controller != null) {
                                if (type != null) {
                                    if (controller instanceof PistalAnimationController
                                            && gun.getModules().getAttachments().getPistolScope() != null
                                            && gun.getModules().getAttachments().getPistolScope().getDoOnSlideMovement()) {
                                        PistalAnimationController pcontroller = (PistalAnimationController) controller;
                                        controller.applyTransform(stack, pcontroller.getSlideNodeIndex(), transformType, entity, matrixStack);
                                    } else
                                        controller.applyAttachmentsTransform(stack, transformType, entity, matrixStack);
                                } else
                                    controller.applyAttachmentsTransform(stack, transformType, entity, matrixStack);
                            }
                            matrixStack.translate(displayX, displayY, displayZ);
                            matrixStack.translate(0, -0.5, 0);
                            matrixStack.scale((float) positioned.getScale(), (float) positioned.getScale(), (float) positioned.getScale());

                            IOverrideModel model = ModelOverrides.getModel(attachmentStack);
                            if (model != null) {
                                model.render(partialTicks, transformType, attachmentStack, stack, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
                            } else {
                                RenderUtil.renderModel(attachmentStack, stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
                            }
                            matrixStack.pop();
                        }
                    }
                }
            }
        }
    }

    private void renderMuzzleFlash(LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemStack weapon, ItemCameraTransforms.TransformType transformType) {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
        if (modifiedGun.getDisplay().getFlash() == null) {
            return;
        }
        if (modifiedGun.canAttachType(IAttachment.Type.BARREL) && GunModifierHelper.isSilencedFire(weapon)) return;

        if (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
            if (this.entityIdForMuzzleFlash.contains(entity.getEntityId())) {
                float randomValue = this.entityIdToRandomValue.get(entity.getEntityId());
                this.drawMuzzleFlash(weapon, modifiedGun, randomValue, randomValue >= 0.5F, matrixStack, buffer);
            }
        }
    }

    private void renderShellCasing(LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemStack weapon, ItemCameraTransforms.TransformType transformType, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
        if (modifiedGun.getDisplay().getShellCasing() == null) {
            return;
        }

        if (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            for (ShellInAir shell : shells) {
                matrixStack.push();

                Vector3f pos = shell.origin.copy();
                Vector3f dis1 = shell.preDisplacement.copy();
                Vector3f dis2 = shell.displacement.copy();
                dis1.mul(1 - partialTicks);
                dis2.mul(partialTicks);
                pos.add(dis1);
                pos.add(dis2);

                Vector3f rot = new Vector3f(0f, 0f, 0f);
                Vector3f rot1 = shell.preRotation.copy();
                Vector3f rot2 = shell.rotation.copy();
                rot1.mul(1 - partialTicks);
                rot2.mul(partialTicks);
                rot.add(rot1);
                rot.add(rot2);

                float displayXv = pos.getX() * 0.0625f;
                float displayYv = pos.getY() * 0.0625f;
                float displayZv = pos.getZ() * 0.0625f;
                float scale = (float) modifiedGun.getDisplay().getShellCasing().getScale();

                matrixStack.translate(displayXv, displayYv, displayZv);
                matrixStack.rotate(Vector3f.XP.rotationDegrees(rot.getX()));
                matrixStack.rotate(Vector3f.YP.rotationDegrees(rot.getY()));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(rot.getZ()));
                matrixStack.scale(scale, scale, scale);

                IBakedModel caseModel;
                if (modifiedGun.getDisplay().getShellCasing().getCasingModel() != null)
                    caseModel = Minecraft.getInstance().getModelManager().getModel(modifiedGun.getDisplay().getShellCasing().getCasingModel());
                else
                    caseModel = SpecialModels.BULLET_SHELL.getModel();
                RenderUtil.renderModel(caseModel, weapon, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);

                matrixStack.pop();
            }
        }
    }

    public double displayX = 0;
    public double displayY = 0;
    public double displayZ = 0;
    public double sizeZ = 0;

    public double adjustedTrailZ = 0;

    private void drawMuzzleFlash(ItemStack weapon, Gun modifiedGun, float random, boolean flip, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        matrixStack.push();

        Gun.Positioned muzzleFlash = modifiedGun.getDisplay().getFlash();
        if (muzzleFlash == null)
            return;

        displayX = muzzleFlash.getXOffset();
        displayY = muzzleFlash.getYOffset();
        displayZ = (muzzleFlash.getZOffset() + this.muzzleExtraOnEnch);

        //GunRenderingHandler.get().adjustedTrailZ = muzzleFlash.get

        double displayXv = displayX * 0.0625;
        double displayYv = displayY * 0.0625;
        double displayZv = displayZ * 0.0625;
        if (GunRenderingHandler.get().muzzleExtraOnEnch != 0)
            this.muzzleExtraOnEnch = 0;

        displayX *= 0.0625;
        displayY *= 0.0625;
        displayZ *= 0.0625;

        adjustedTrailZ = modifiedGun.getDisplay().getFlash().getTrailAdjust();

        matrixStack.translate(displayXv, displayYv, displayZv);
        matrixStack.translate(0, -0.5, 0);

        ItemStack barrelStack = Gun.getAttachment(IAttachment.Type.BARREL, weapon);
        if (!barrelStack.isEmpty() && barrelStack.getItem() instanceof IBarrel) {
            Barrel barrel = ((IBarrel) barrelStack.getItem()).getProperties();
            Gun.ScaledPositioned positioned = modifiedGun.getModules().getAttachments().getBarrel();
            if (positioned != null) {
                matrixStack.translate(0, 0, -barrel.getLength() * 0.0625 * positioned.getScale());
            }
        }

        matrixStack.scale(0.5F, 0.5F, 0.0F);

        double partialSize = modifiedGun.getDisplay().getFlash().getSize() / 5.0;
        float size = (float) (modifiedGun.getDisplay().getFlash().getSize() - partialSize + partialSize * random);
        size = (float) GunModifierHelper.getMuzzleFlashSize(weapon, size);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(360F * random));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(flip ? 180F : 0F));
        matrixStack.translate(-size / 2, -size / 2, 0);

        float sizeForTrail = (float) (1 - partialSize + partialSize);
        //sizeForTrail = (float) GunModifierHelper.getMuzzleFlashSize(weapon, sizeForTrail);

        sizeZ = -sizeForTrail;

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        IVertexBuilder builder = buffer.getBuffer(GunRenderType.getMuzzleFlash());
        builder.pos(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).lightmap(15728880).endVertex();
        builder.pos(matrix, 0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 0).lightmap(15728880).endVertex();

        /*float smokeSize = (float) modifiedGun.getDisplay().getFlash().getSmokeSize();
        builder = buffer.getBuffer(GunRenderType.getMuzzleSmoke());
        matrixStack.translate(size / 2 - smokeSize / 2, size / 2 - smokeSize / 2, 0.01f);
        builder.pos(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, smokeSize, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, smokeSize, smokeSize, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).lightmap(15728880).endVertex();
        builder.pos(matrix, 0, smokeSize, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 0).lightmap(15728880).endVertex();
*/
        matrixStack.pop();
    }

    /**
     * A temporary hack to get the equip progress until Forge fixes the issue.
     *
     * @return
     */
    private float getEquipProgress(float partialTicks) {
        if (this.equippedProgressMainHandField == null) {
            this.equippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "field_187469_f");
            this.equippedProgressMainHandField.setAccessible(true);
        }
        if (this.prevEquippedProgressMainHandField == null) {
            this.prevEquippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "field_187470_g");
            this.prevEquippedProgressMainHandField.setAccessible(true);
        }
        FirstPersonRenderer firstPersonRenderer = Minecraft.getInstance().getFirstPersonRenderer();
        try {
            float equippedProgressMainHand = (float) this.equippedProgressMainHandField.get(firstPersonRenderer);
            float prevEquippedProgressMainHand = (float) this.prevEquippedProgressMainHandField.get(firstPersonRenderer);
            return 1.0F - MathHelper.lerp(partialTicks, prevEquippedProgressMainHand, equippedProgressMainHand);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0.0F;
    }

    public static class ShellInAir {
        public int livingTick;
        public Vector3f preDisplacement = new Vector3f(0f, 0f, 0f);
        public Vector3f displacement = new Vector3f(0f, 0f, 0f);
        public Vector3f preRotation = new Vector3f(0f, 0f, 0f);
        public Vector3f rotation = new Vector3f(0f, 0f, 0f);
        public Vector3f origin;
        public Vector3f velocity;
        public Vector3f angularVelocity;

        public ShellInAir(@Nonnull Vector3f origin, @Nonnull Vector3f velocity, @Nonnull Vector3f angularVelocity, int life) {
            this.origin = origin.copy();
            this.velocity = velocity.copy();
            this.angularVelocity = angularVelocity.copy();
            livingTick = life;
        }
    }
}
