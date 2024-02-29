package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tac.guns.Reference;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    }

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
        if (!(heldItem.getItem() instanceof GunItem))
            return;

        if (heldItem.getTag() == null)
            return;

        Gun gun = ((GunItem) heldItem.getItem()).getGun();
        if (!gun.getReloads().isHeat())
            return;

        float anchorPointX = event.getWindow().getScaledWidth() / 2F;
        float anchorPointY = event.getWindow().getScaledHeight() / 2F;

        stack.push();
        stack.translate(anchorPointX, anchorPointY, 0);
        stack.scale(Math.min(1F, 1F * heldItem.getTag().getInt("heatValue") / gun.getReloads().getTickToHeat()), 1F, 1F);
        //stack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager().bindTexture(HEAT_BAR[0]);
        blit(stack,  -48,  -48, 0, 0, 96, 96, 96, 96);
        stack.pop();

        stack.push();
        stack.translate(anchorPointX, anchorPointY, 0);
        //stack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager().bindTexture(HEAT_BASE[0]);
        blit(stack, -48, -48, 0, 0, 96, 96, 96, 96);
        stack.pop();
    }
}
