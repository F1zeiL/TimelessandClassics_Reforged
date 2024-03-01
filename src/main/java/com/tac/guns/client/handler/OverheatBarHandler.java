package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class OverheatBarHandler extends AbstractGui {
    private static OverheatBarHandler instance;
    private static final ResourceLocation[] HEAT_BASE = new ResourceLocation[] {
            new ResourceLocation(Reference.MOD_ID, "textures/gui/heat_base.png")
    };
    private static final ResourceLocation[] HEAT_BAR = new ResourceLocation[] {
            new ResourceLocation(Reference.MOD_ID, "textures/gui/heat_bar.png")
    };

    public static OverheatBarHandler get() {
        return instance == null ? instance = new OverheatBarHandler() : instance;
    }

    private OverheatBarHandler() {
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e) {
//        if (e.phase != TickEvent.Phase.END)
//            return;
//        PlayerEntity entity = Minecraft.getInstance().player;
//        if (entity == null)
//            return;
//
//        ItemStack heldItem = entity.getHeldItemMainhand();
//        if (!(heldItem.getItem() instanceof GunItem))
//            return;
//
//        Gun gun = ((GunItem) heldItem.getItem()).getGun();
//        if (!gun.getReloads().isHeat())
//            return;
//
//        if (heldItem.getTag() != null) {
//            if (overHeat(entity, heldItem))
//                if (heldItem.getTag().getInt("heatValue") >= gun.getReloads().getTickToHeat())
//                    entity.sendStatusMessage(new TranslationTextComponent("info.tac.over_heat").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.RED), true);
//                else
//                    entity.sendStatusMessage(new TranslationTextComponent(heldItem.getTag().getInt("heatValue") * 100 / gun.getReloads().getTickToHeat() + "% / 100%").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.RED), true);
//            else
//                entity.sendStatusMessage(new TranslationTextComponent("" + (heldItem.getTag().getInt("heatValue") * 100 / gun.getReloads().getTickToHeat()) + "% / 100%").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.WHITE), true);
//        }
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;

        if (!Config.CLIENT.weaponGUI.weaponOverheatBar.showWeaponOverheatBar.get())
            return;

        if (Minecraft.getInstance().player == null)
            return;

        ClientPlayerEntity player = Minecraft.getInstance().player;
        ItemStack heldItem = player.getHeldItemMainhand();
        MatrixStack stack = event.getMatrixStack();
        if (!(heldItem.getItem() instanceof GunItem))
            return;

        if (heldItem.getTag() == null)
            return;

        Gun gun = ((GunItem) heldItem.getItem()).getGun();
        if (!gun.getReloads().isHeat())
            return;

        float anchorPointX = event.getWindow().getScaledWidth() / 2F;
        float anchorPointY = event.getWindow().getScaledHeight() / 2F;
        float heatProgress = Math.min(1F, 1F * heldItem.getTag().getInt("heatValue") / gun.getReloads().getTickToHeat());
        float totalTranslateX = 0;
        float totalTranslateY = 16;
        int heatProgressRound = Math.min(99, Math.round(100 * heatProgress));
        String heatText = heatProgressRound + "%";

        if (heatProgressRound < 10)
            heatText = "0" + heatText;

        stack.push();
        {
            stack.translate(anchorPointX, anchorPointY, 0);
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            double totalAlpha = MathHelper.clamp(Config.CLIENT.weaponGUI.weaponOverheatBar.weaponOverheatBarAlpha.get(), 0.0, 1.0);

            stack.push();
            {
                stack.translate(totalTranslateX, totalTranslateY, 0);
                if (heldItem.getTag().getBoolean("overHeatLock")) {
                    RenderSystem.color4f(1.0f, 0.1f, 0.1f, (float) (1.0f * totalAlpha));
                } else {
                    RenderSystem.color4f(1.0f, 1.0f, 1.0f, (float) (1.0f * totalAlpha));
                }
                Minecraft.getInstance().getTextureManager().bindTexture(HEAT_BASE[0]);
                blit(stack, -48, -48, 0, 0, 96, 96, 96, 96);
            }
            stack.pop();

            stack.push();
            {
                stack.translate(totalTranslateX, totalTranslateY, 0);
                RenderSystem.color4f(0.4f, 0.4f, 0.4f, (float) (0.4f * totalAlpha));
                Minecraft.getInstance().getTextureManager().bindTexture(HEAT_BAR[0]);
                blit(stack, -48, -48, 0, 0, 96, 96, 96, 96);
            }
            stack.pop();

            stack.push();
            {
                stack.scale(heatProgress, 1F, 1F);
                stack.translate(totalTranslateX, totalTranslateY, 0);
                if (heldItem.getTag().getBoolean("overHeatLock")) {
                    RenderSystem.color4f(1.0f, 0.2f, 0.2f, (float) (1.0f * totalAlpha));
                } else {
                    if (heatProgress >= 0.8F) {
                        RenderSystem.color4f(1.0f, 0.5f, 0.0f, (float) (1.0f * totalAlpha));
                    } else if (heatProgress >= 0.5F) {
                        RenderSystem.color4f(1.0f, 0.8f, 0.2f, (float) (1.0f * totalAlpha));
                    } else {
                        RenderSystem.color4f(1.0f, 1.0f, 1.0f, (float) (1.0f * totalAlpha));
                    }
                }
                Minecraft.getInstance().getTextureManager().bindTexture(HEAT_BAR[0]);
                blit(stack, -48, -48, 0, 0, 96, 96, 96, 96);
            }
            stack.pop();

            stack.push();
            {
                stack.translate(totalTranslateX, totalTranslateY, 0);
                stack.translate(-8.5, 14, 0);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, (float) (1.0f * totalAlpha));
                if (overHeat(player, heldItem)) {
                    stack.translate(-15.5, 0, 0);
                    Color color = new Color(255, 51, 51, (int) (255 * totalAlpha));
                    drawString(stack, Minecraft.getInstance().fontRenderer, "OVERHEAT!", 0, 0, color.getRGB());
                    stack.translate(15.5, 0, 0);
                } else {
                    Color color = new Color(255, 255, 255, (int) (255 * totalAlpha));
                    drawString(stack, Minecraft.getInstance().fontRenderer, heatText, 0, 0, color.getRGB());
                }
            }
            stack.pop();
        }
        stack.pop();
    }

    private static boolean overHeat(PlayerEntity player, ItemStack heldItem) {
        if (heldItem.getItem() instanceof TimelessGunItem && !((TimelessGunItem) heldItem.getItem()).getGun().getReloads().isHeat())
            return false;

        return heldItem.getTag().getInt("heatValue") >= ((TimelessGunItem) heldItem.getItem()).getGun().getReloads().getTickToHeat() ||
                heldItem.getTag().getBoolean("overHeatLock");
    }
}