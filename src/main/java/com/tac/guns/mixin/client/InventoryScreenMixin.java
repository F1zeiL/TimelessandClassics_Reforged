package com.tac.guns.mixin.client;

import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    // Refactor to be customizable, refactor armor to be curiosLoaded compatible, and refactor to be either customizable, or able to be turned off, due to conflicts with other mods.
    /*@Inject(at = @At("TAIL"), method = "drawGuiContainerBackgroundLayer(Lcom/mojang/blaze3d/matrix/MatrixStack;FII)V")
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y, CallbackInfo ci) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("tac", "textures/gui/ammoslots.png"));
        ((InventoryScreen)(Object)this).blit(matrixStack, ((InventoryScreen)(Object)this).getGuiLeft() + 169, ((InventoryScreen)(Object)this).getGuiTop() + 78, 0, 0, 23, 46);
    }*/

}
