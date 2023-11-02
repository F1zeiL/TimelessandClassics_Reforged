package com.tac.guns.item.attachments;

import com.tac.guns.common.attachments.*;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.IColored;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IgunSkin;
import com.tac.guns.item.attachment.impl.GunSkin;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GunSkinItem extends AttachmentItem implements IColored {
    public GunSkinItem(Properties properties) {
        super(properties);
    }
    public GunSkinItem(Properties properties, ResourceLocation defaultModifier) {
        super(properties,defaultModifier);
    }

    @Override
    public AttachmentType getType() {
        return AttachmentType.GUN_SKIN;
    }

    @Override
    public boolean canColor(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE || super.canApplyAtEnchantingTable(stack, enchantment);
    }

}