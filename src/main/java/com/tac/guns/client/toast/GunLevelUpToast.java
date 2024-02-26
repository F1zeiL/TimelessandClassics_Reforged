package com.tac.guns.client.toast;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GunLevelUpToast implements IToast {
   private ITextComponent title;
   private ITextComponent subTitle;
   private ItemStack icon;

   public GunLevelUpToast(ItemStack icon,ITextComponent titleComponent, @Nullable ITextComponent subtitle) {
      this.icon = icon;
      this.title = titleComponent;
      this.subTitle = subtitle;
   }

   public Visibility func_230444_a_(MatrixStack stack, ToastGui gui, long p_230444_3_) {
      gui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
      RenderSystem.color3f(1.0F, 1.0F, 1.0F);

      gui.blit(stack, 0, 0, 0, 0, this.func_230445_a_(), this.func_238540_d_());

      List<IReorderingProcessor> list = gui.getMinecraft().fontRenderer.trimStringToWidth(subTitle, 125);
      int i = 16776960;
      if (list.size() == 1) {
         gui.getMinecraft().fontRenderer.func_243248_b(stack, title, 30.0F, 7.0F, i | -16777216);
         gui.getMinecraft().fontRenderer.func_238422_b_(stack, list.get(0), 30.0F, 18.0F, -1);
      } else {
          if (p_230444_3_ < 1500L) {
            int k = MathHelper.floor(MathHelper.clamp((float)(1500L - p_230444_3_) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
            gui.getMinecraft().fontRenderer.func_243248_b(stack, title, 30.0F, 11.0F, i | k);
         } else {
            int i1 = MathHelper.floor(MathHelper.clamp((float)(p_230444_3_ - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
            int l = this.func_238540_d_() / 2 - list.size() * 9 / 2;

            for(IReorderingProcessor ireorderingprocessor : list) {
               gui.getMinecraft().fontRenderer.func_238422_b_(stack, ireorderingprocessor, 30.0F, (float)l, 16777215 | i1);
               l += 9;
            }
         }
      }

      gui.getMinecraft().getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(icon, 8, 8);
      return p_230444_3_ >= 5000L ? Visibility.HIDE : Visibility.SHOW;
   }
}