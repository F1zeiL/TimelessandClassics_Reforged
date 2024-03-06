package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.client.gunskin.GunSkin;
import com.tac.guns.client.gunskin.SkinManager;
import com.tac.guns.common.Gun;
import com.tac.guns.common.ReloadTracker;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.Objects;

public class HUDRenderingHandler extends AbstractGui {
    private static HUDRenderingHandler instance;

    private static final ResourceLocation[] AMMO_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/counterassule_rifle.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/counterlmg.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/counterpistol.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/countershotgun.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/countersmg.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/countersniper.png")
            };

    private static final ResourceLocation[] FIREMODE_ICONS_OLD = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/safety.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/semi.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/full.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/burst.png"),
            };
    private static final ResourceLocation[] FIREMODE_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_safety.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_semi.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_auto.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/firemode_burst.png"),
            };
    private static final ResourceLocation[] RELOAD_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/gui/reloadbar.png")
            };

    private static final ResourceLocation[] HIPFIRE_ICONS = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/crosshair_hit/hit_marker.png")
            };
    private static final ResourceLocation[] NOISE_S = new ResourceLocation[]
            {
                    new ResourceLocation(Reference.MOD_ID, "textures/screen_effect/noise1.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/screen_effect/noise2.png")
                    /*new ResourceLocation(Reference.MOD_ID, "textures/screen_effect/noise4.png"),
                    new ResourceLocation(Reference.MOD_ID, "textures/screen_effect/noise5.png")*/
            };

    public static HUDRenderingHandler get() {
        return instance == null ? instance = new HUDRenderingHandler() : instance;
    }

    private HUDRenderingHandler() {
    }

    private int ammoReserveCount = 0;

    private ResourceLocation heldAmmoID = new ResourceLocation("");

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END)
            return;
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;
        if (Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof GunItem) {
            GunItem gunItem = (GunItem) Minecraft.getInstance().player.getHeldItemMainhand().getItem();
            this.ammoReserveCount = ReloadTracker.calcMaxReserveAmmo(Gun.findAmmo(Minecraft.getInstance().player, gunItem.getGun().getProjectile().getItem()));
            // Only send if current id doesn't equal previous id, otherwise other serverside actions can force this to change like reloading
            if ((player.isCreative() && Config.SERVER.gameplay.creativeUnlimitedCurrentAmmo.get()) ||
                    (!player.isCreative() && Config.SERVER.gameplay.commonUnlimitedCurrentAmmo.get()))
                return;
            //if(gunItem.getGun().getProjectile().getItem().compareTo(heldAmmoID) != 0 || ammoReserveCount == 0) {
            heldAmmoID = gunItem.getGun().getProjectile().getItem();
            //}
        }

    }


    // Jitter minecraft player screen a tiny bit per system nano time
    private void jitterScreen(float partialTicks) {
        long time = System.nanoTime();
        float jitterX = (float) (Math.sin(time / 1000000000.0) * 0.0005);
        float jitterY = (float) (Math.cos(time / 1000000000.0) * 0.0005);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);//beginWrite(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(jitterX, jitterY, 0);
        tessellator.draw();
    }


    // EnchancedVisuals-1.16.5 helped with this one
    private ResourceLocation getNoiseTypeResource(boolean doNoise) {
        long time = Math.abs(System.nanoTime() / 3000000 / 50);
        return NOISE_S[(int) (time % NOISE_S.length)];
    }

    // A method that tints the screen green like night vision if true
    private void renderNightVision(boolean doNightVision) {
        brightenScreen(doNightVision);
        if (doNightVision) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(0.05F, 1.55F, 0.05F, 0.0825F);
            RenderSystem.disableAlphaTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.shadeModel(7425);
            RenderSystem.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(0.0D, (double) Minecraft.getInstance().getMainWindow().getScaledHeight(), -90.0D).endVertex();
            bufferbuilder.pos((double) Minecraft.getInstance().getMainWindow().getScaledWidth(), (double) Minecraft.getInstance().getMainWindow().getScaledHeight(), -90.0D).endVertex();
            bufferbuilder.pos((double) Minecraft.getInstance().getMainWindow().getScaledWidth(), 0.0D, -90.0D).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).endVertex();
            tessellator.draw();
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            RenderSystem.enableAlphaTest();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
    }

    // Final
    private double defaultGameGamma = 0;


    // "Brightens the screen" However this example is more useful less gimick, I want to be a bit more gimicky, but a great V1 to be honest - ClumsyAlien
    private void brightenScreen(boolean doNightVision) {

        // Basic force gammed night vision
        if (doNightVision) {
            if (defaultGameGamma == 0)
                defaultGameGamma = Minecraft.getInstance().gameSettings.gamma;
            Minecraft.getInstance().gameSettings.gamma = 200;
        } else {
            Minecraft.getInstance().gameSettings.gamma = defaultGameGamma;
        }
    }

    private static final ResourceLocation fleshHitMarker = new ResourceLocation(Reference.MOD_ID, "textures/crosshair_hit/hit_marker_no_opac.png");
    public boolean hitMarkerHeadshot = false;
    public static final float hitMarkerRatio = 14f;
    public float hitMarkerTracker = 0;

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if (Minecraft.getInstance().player == null) {
            return;
        }

        ClientPlayerEntity player = Minecraft.getInstance().player;
        ItemStack heldItem = player.getHeldItemMainhand();
        MatrixStack stack = event.getMatrixStack();
        float anchorPointX = event.getWindow().getScaledWidth() / 12F * 11F;
        float anchorPointY = event.getWindow().getScaledHeight() / 10F * 9F;

        float configScaleWeaponCounter = Config.CLIENT.weaponGUI.weaponAmmoCounter.weaponAmmoCounterSize.get().floatValue();
        float configScaleWeaponFireMode = Config.CLIENT.weaponGUI.weaponFireMode.weaponFireModeSize.get().floatValue();
        float configScaleWeaponReloadBar = Config.CLIENT.weaponGUI.weaponReloadTimer.weaponReloadTimerSize.get().floatValue();

        float counterSize = 1.8F * configScaleWeaponCounter;
        float fireModeSize = 32.0F * configScaleWeaponFireMode;
        float ReloadBarSize = 32.0F * configScaleWeaponReloadBar;

        float hitMarkerSize = 128.0F;

        RenderSystem.enableAlphaTest();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        int width = event.getWindow().getWidth();
        int height = event.getWindow().getHeight();

        int centerX = event.getWindow().getScaledWidth() / 2;
        int centerY = event.getWindow().getScaledHeight() / 2;

        if (Config.CLIENT.display.showHitMarkers.get()) {
            if (this.hitMarkerTracker > 0)//Hit Markers
            {
                RenderSystem.enableAlphaTest();
                stack.push();
                {

                    RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                    Minecraft.getInstance().getTextureManager().bindTexture(fleshHitMarker); // Future options to render bar types

                    float opac = Math.max(Math.min(this.hitMarkerTracker / hitMarkerRatio, 100f), 0.20f);
                    if (HUDRenderingHandler.get().hitMarkerHeadshot)
                        RenderSystem.color4f(1.0f, 0.075f, 0.075f, opac); // Only render red
                    else
                        RenderSystem.color4f(1.0f, 1.0f, 1.0f, opac);
                    blit(stack, centerX - 8, centerY - 8, 0, 0, 16, 16, 16, 16); //-264 + (int)(-9.0/4),-134,
                }
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                stack.pop();
            }
        }

        // All code for rendering night vision, still only a test
        if (false) {
            renderNightVision(Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.get());
            if (Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.get()) {

                RenderSystem.enableAlphaTest();
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                stack.push();

                Minecraft.getInstance().getTextureManager().bindTexture(getNoiseTypeResource(true));
                float opacity = 0.25f;//0.125f;// EnchancedVisuals-1.16.5 helped with this one, instead have a fading opacity visual.getOpacity();
                Matrix4f matrix = stack.getLast().getMatrix();
                buffer.pos(matrix, 0, width, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, opacity).endVertex();
                buffer.pos(matrix, width, height, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, opacity).endVertex();
                buffer.pos(matrix, width, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, opacity).endVertex();
                buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, opacity).endVertex();

                buffer.finishDrawing();
                WorldVertexBufferUploader.draw(buffer);
                stack.pop();
            }
        }

        if (!(Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof TimelessGunItem))
            return;
        TimelessGunItem gunItem = (TimelessGunItem) heldItem.getItem();
        Gun gun = gunItem.getGun();
        if (!Config.CLIENT.weaponGUI.weaponGui.get()) {
            return;
        }

        //render icon
        stack.translate(Config.CLIENT.weaponGUI.mainX.get(), -Config.CLIENT.weaponGUI.mainY.get(), 0);
        if (Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.get()) {
            GunSkin skin = SkinManager.getSkin(heldItem);
            if (skin != null) {
                if (skin.getIcon() != null) {
                    RenderSystem.enableAlphaTest();
                    stack.push();
                    {
                        float scale = Config.CLIENT.weaponGUI.weaponTypeIcon.weaponIconSize.get().floatValue() * 0.7f;
                        stack.translate(anchorPointX - 8 - 90 * scale + Config.CLIENT.weaponGUI.weaponTypeIcon.x.get() - 31.46,
                                anchorPointY - 25 - 90 * scale + Config.CLIENT.weaponGUI.weaponTypeIcon.y.get() + 18.21, 0);
                        stack.scale(scale, scale, scale);
                        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                        Minecraft.getInstance().getTextureManager().bindTexture(skin.getIcon());
                        blit(stack, 0, 0, 0, 0, 96, 96, 96, 96);
                    }
                    stack.pop();
                }
            }
        }

        if (Config.CLIENT.weaponGUI.weaponFireMode.showWeaponFireMode.get()) {
            // FireMode rendering
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.defaultAlphaFunc();
            buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            stack.push();
            {
                stack.translate(anchorPointX - (fireModeSize * 2) / 4F, anchorPointY - (fireModeSize * 2) / 5F * 3F, 0);
                stack.translate(-fireModeSize + (-0.7) + (-Config.CLIENT.weaponGUI.weaponFireMode.x.get().floatValue() + 34.74), -fireModeSize + 53.2 + (-Config.CLIENT.weaponGUI.weaponFireMode.y.get().floatValue()), 0);

                stack.translate(20, 5, 0);
                int fireMode;

                if (player.getHeldItemMainhand().getItem() instanceof GunItem) {
                    try {
                        if (heldItem.getTag() == null) {
                            heldItem.getOrCreateTag();
                        }
                        int[] gunItemFireModes = heldItem.getTag().getIntArray("supportedFireModes");
                        if (ArrayUtils.isEmpty(gunItemFireModes)) {
                            gunItemFireModes = gun.getGeneral().getRateSelector();
                            heldItem.getTag().putIntArray("supportedFireModes", gunItemFireModes);
                            heldItem.getTag().putInt("CurrentFireMode", gunItemFireModes[0]);
                        } else if (!Arrays.equals(gunItemFireModes, gun.getGeneral().getRateSelector())) {
                            heldItem.getTag().putIntArray("supportedFireModes", gun.getGeneral().getRateSelector());
                            if (!heldItem.getTag().contains("CurrentFireMode"))
                                heldItem.getTag().putInt("CurrentFireMode", gunItemFireModes[0]);
                        }
                        if (player.getHeldItemMainhand().getTag() == null) {
                            if (!Config.COMMON.gameplay.safetyExistence.get() && Objects.requireNonNull(player.getHeldItemMainhand().getTag()).getInt("CurrentFireMode") == 0 && gunItemFireModes.length > 1)
                                fireMode = gunItemFireModes[1];
                            else
                                fireMode = gunItemFireModes[0];
                        } else if (player.getHeldItemMainhand().getTag().getInt("CurrentFireMode") == 0 && gunItemFireModes.length > 1)
                            if (!Config.COMMON.gameplay.safetyExistence.get()) {
                                fireMode = gunItemFireModes[0];
                                if (fireMode == 0)
                                    fireMode = gunItemFireModes[1];
                            } else
                                fireMode = gunItemFireModes[0];
                        else {
                            fireMode = Objects.requireNonNull(player.getHeldItemMainhand().getTag()).getInt("CurrentFireMode");
                            if (!Config.COMMON.gameplay.safetyExistence.get() && fireMode == 0) {
                                fireMode = gunItemFireModes[0];
                                if (fireMode == 0)
                                    fireMode = gunItemFireModes[1];
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        fireMode = gun.getGeneral().getRateSelector()[0];
                    } catch (Exception e) {
                        fireMode = 0;
                        GunMod.LOGGER.log(Level.ERROR, "TaC HUD_RENDERER has failed obtaining the fire mode");
                    }
                    Minecraft.getInstance().getTextureManager().bindTexture(FIREMODE_ICONS[fireMode]); // Render true firemode

                    Matrix4f matrix = stack.getLast().getMatrix();
                    buffer.pos(matrix, 0, fireModeSize / 2, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                    buffer.pos(matrix, fireModeSize / 2, fireModeSize / 2, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                    buffer.pos(matrix, fireModeSize / 2, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                    buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
                }
            }
            stack.pop();
            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
        }
        if (Config.CLIENT.weaponGUI.weaponAmmoCounter.showWeaponAmmoCounter.get()) {
            // Text rendering
            stack.push();
            {
                stack.translate(
                        (anchorPointX - (counterSize * 32) / 2) + (-Config.CLIENT.weaponGUI.weaponAmmoCounter.x.get().floatValue() + 39.74),
                        (anchorPointY - (counterSize * 32) / 4) + (-Config.CLIENT.weaponGUI.weaponAmmoCounter.y.get().floatValue()),
                        0
                );
                if (player.getHeldItemMainhand().getTag() != null) {
                    IFormattableTextComponent currentAmmo;
                    IFormattableTextComponent reserveAmmo;
                    IFormattableTextComponent currentNoMagAmmo;

                    int ammo = player.getHeldItemMainhand().getTag().getInt("AmmoCount");
                    int ammoReserve = this.ammoReserveCount;
                    boolean isNoMag = false;
                    if (player.getHeldItemMainhand().getItem() instanceof TimelessGunItem)
                        if (((TimelessGunItem) player.getHeldItemMainhand().getItem()).getModifiedGun(player.getHeldItemMainhand()).getReloads().isNoMag()) {
                            ammo = ReloadTracker.calcMaxReserveAmmo(Gun.findAmmo(Minecraft.getInstance().player, gunItem.getGun().getProjectile().getItem()));
                            ammoReserve = 0;
                            isNoMag = true;
                        }

                    if (ammo <= Math.min(10, gun.getReloads().getMaxAmmo() / 4)) {
                        currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo)).mergeStyle(TextFormatting.RED);
                        currentNoMagAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo)).mergeStyle(TextFormatting.RED);
                    } else {
                        currentAmmo = byPaddingZeros(Math.min(ammo, 999)).append(new TranslationTextComponent("" + (ammo >= 1000 ? 999 : ammo)).mergeStyle(TextFormatting.WHITE));
                        currentNoMagAmmo = byPaddingZeros(Math.min(ammo, 9999)).append(new TranslationTextComponent("" + (ammo >= 10000 ? 9999 : ammo)).mergeStyle(TextFormatting.WHITE));
                    }

                    if (ammoReserve <= gun.getReloads().getMaxAmmo() && !isNoMag)
                        reserveAmmo = byPaddingZeros(Math.min(ammoReserve, 10000)).append(new TranslationTextComponent("" +
                                (ammoReserve >= 10000 ? 9999 : ammoReserve))).mergeStyle(TextFormatting.RED);
                    else
                        reserveAmmo = byPaddingZeros(Math.min(ammoReserve, 10000)).append(new TranslationTextComponent("" +
                                (ammoReserve >= 10000 ? 9999 : ammoReserve))).mergeStyle(TextFormatting.GRAY);

//                    if (ammo <= gun.getReloads().getMaxAmmo() / 4 && this.ammoReserveCount <= gun.getReloads().getMaxAmmo()) {
//                        currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo)).mergeStyle(TextFormatting.RED);
//                        reserveAmmo = byPaddingZeros(Math.min(ammoReserve, 10000)).append(new TranslationTextComponent("" +
//                                (ammoReserve > 10000 ? 9999 : ammoReserve))).mergeStyle(TextFormatting.RED);
//                    } else if (ammoReserve <= gun.getReloads().getMaxAmmo()) {
//                        currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo).mergeStyle(TextFormatting.WHITE));
//                        reserveAmmo = byPaddingZeros(Math.min(ammoReserve, 10000)).append(new TranslationTextComponent("" +
//                                (ammoReserve > 10000 ? 9999 : ammoReserve))).mergeStyle(TextFormatting.RED);
//                    } else if (ammo <= gun.getReloads().getMaxAmmo() / 4) {
//                        currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo)).mergeStyle(TextFormatting.RED);
//                        reserveAmmo = byPaddingZeros(Math.min(ammoReserve, 10000)).append(new TranslationTextComponent("" +
//                                (ammoReserve > 10000 ? 9999 : ammoReserve))).mergeStyle(TextFormatting.GRAY);
//                    } else {
//                        currentAmmo = byPaddingZeros(ammo).append(new TranslationTextComponent("" + ammo).mergeStyle(TextFormatting.WHITE));
//                        reserveAmmo = byPaddingZeros(Math.min(ammoReserve, 10000)).append(new TranslationTextComponent("" +
//                                (ammoReserve > 10000 ? 9999 : ammoReserve))).mergeStyle(TextFormatting.GRAY);
//                    }
                    stack.scale(counterSize, counterSize, counterSize);
                    stack.push();
                    {
                        stack.translate(-21.15, 0, 0);
                        if (gun.getReloads().isNoMag()) {
                            stack.translate(-6, 0, 0);
                            drawString(stack, Minecraft.getInstance().fontRenderer, currentNoMagAmmo, 0, 0, 0xffffff); // No mag gun ammo
                        } else
                            drawString(stack, Minecraft.getInstance().fontRenderer, currentAmmo, 0, 0, 0xffffff); // Gun ammo
                    }
                    stack.pop();

                    stack.push();
                    {
                        stack.scale(0.56f, 0.56f, 0.56f);
                        stack.translate(
                                (3.7 - 6.0),
                                (3.4 - 4.5),
                                0);
                        drawString(stack, Minecraft.getInstance().fontRenderer, reserveAmmo, 0, 0, 0xffffff); // Reserve ammo
                    }
                    stack.pop();
                }
            }
            stack.pop();


            stack.push();
            RenderSystem.enableAlphaTest();
            buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            stack.translate(anchorPointX - (ReloadBarSize * 4.35) / 4F, anchorPointY + (ReloadBarSize * 1.625F) / 5F * 3F, 0);//stack.translate(anchorPointX - (fireModeSize*6) / 4F, anchorPointY - (fireModeSize*1F) / 5F * 3F, 0); // *68for21F
            stack.translate(-ReloadBarSize, -ReloadBarSize, 0);

            stack.translate(-16.25 - 7.3, 0.15 + 1.6, 0);
            // stack.translate(0, 0, );
            stack.scale(3.05F, 0.028F, 0); // *21F
            Minecraft.getInstance().getTextureManager().bindTexture(RELOAD_ICONS[0]); // Future options to render bar types

            Matrix4f matrix = stack.getLast().getMatrix();
//            buffer.pos(matrix, 0, ReloadBarSize, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
//            buffer.pos(matrix, ReloadBarSize, ReloadBarSize, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
//            buffer.pos(matrix, ReloadBarSize, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
//            buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();

            stack.translate(19.25, (1.5 + (-63.4)) * 10, 0);
            // stack.translate(0, 0, );
            stack.scale(0.0095F, 20.028F, 0); // *21F

//            buffer.pos(matrix, 0, ReloadBarSize, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
//            buffer.pos(matrix, ReloadBarSize, ReloadBarSize, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
//            buffer.pos(matrix, ReloadBarSize, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();
//            buffer.pos(matrix, 0, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 0.99F).endVertex();

            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
            stack.pop();
        }
        stack.translate(-Config.CLIENT.weaponGUI.mainX.get(), Config.CLIENT.weaponGUI.mainY.get(), 0);
    }
            /*if (Minecraft.getInstance().gameSettings.viewBobbing) {
                if (Minecraft.getInstance().player.ticksExisted % 2 == 0) {
                    Minecraft.getInstance().getTextureManager().bindTexture(NOISE_S[0]);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.5F);
                    RenderSystem.disableAlphaTest();
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef(0.0F, 0.0F, -0.01F);
                    float f = 5.0F;
                    RenderSystem.scalef(f, f, f);
                    float f1 = (float) (Minecraft.getInstance().player.ticksExisted % 3000) / 3000.0F / f;
                    float f2 = 0.0F;
                    float f3 = 0.0F;
                    float f4 = 0.0F;
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    bufferbuilder.pos(0.0D, (double) Minecraft.getInstance().getMainWindow().getScaledHeight(), (double) Minecraft.getInstance().getMainWindow().getScaledHeight()).tex((float) (f1 + f4), (float) (f2 + f3)).endVertex();
                    bufferbuilder.pos((double) Minecraft.getInstance().getMainWindow().getScaledWidth(), (double) Minecraft.getInstance().getMainWindow().getScaledHeight(), (double) Minecraft.getInstance().getMainWindow().getScaledHeight()).tex((float) (f1 + 1.0F / f + f4), (float) (f2 + f3)).endVertex();
                    bufferbuilder.pos((double) Minecraft.getInstance().getMainWindow().getScaledWidth(), 0.0D, (double) Minecraft.getInstance().getMainWindow().getScaledHeight()).tex((float) (f1 + 1.0F / f + f4), (float) (f2 + 1.0F / f + f3)).endVertex();
                    bufferbuilder.pos(0.0D, 0.0D, (double) Minecraft.getInstance().getMainWindow().getScaledHeight()).tex((float) (f1 + f4), (float) (f2 + 1.0F / f + f3)).endVertex();
                    tessellator.draw();
                    RenderSystem.popMatrix();
                    RenderSystem.enableAlphaTest();
                    RenderSystem.disableBlend();
                }
            }*/

    private static IFormattableTextComponent byPaddingZeros(int number) {
        String text = String.format("%0" + (byPaddingZerosCount(number) + 1) + "d", 1);
        text = text.substring(0, text.length() - 1);
        return new TranslationTextComponent(text).mergeStyle(TextFormatting.GRAY);
    }

    private static int byPaddingZerosCount(int length) {
        if (length < 10)
            return 2;
        if (length < 100)
            return 1;
        if (length < 1000)
            return 0;
        return 0;
    }
}