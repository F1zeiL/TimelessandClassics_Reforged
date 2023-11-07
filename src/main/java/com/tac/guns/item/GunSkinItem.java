package com.tac.guns.item;

import com.tac.guns.common.attachments.NetworkModifierManager;
import com.tac.guns.common.attachments.CustomModifierData;
import com.tac.guns.common.attachments.PerkTipsBuilder;
import com.tac.guns.common.attachments.Perks;
import com.tac.guns.common.container.slot.SlotType;
import com.tac.guns.init.ModItems;
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
import java.util.function.Function;

public class GunSkinItem extends Item implements IgunSkin, IColored {
    public static final String CUSTOM_MODIFIER = "CustomModifier";
    private final GunSkin gunSkin;

    public GunSkinItem(GunSkin gunSkin, Properties properties) {
        super(properties);
        this.gunSkin = gunSkin;
    }

    public static boolean hasCustomModifier(ItemStack stack){
        return stack!=null && stack.getTag()!=null && stack.getTag().contains(CUSTOM_MODIFIER,Constants.NBT.TAG_STRING);
    }
    public static void setCustomModifier(ItemStack stack, ResourceLocation location){
        if(stack!=null && location!=null) {
            stack.getOrCreateTag().putString(CUSTOM_MODIFIER, location.toString());
        }
    }



    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            if (ModItems.SKIN_CUSTOM.get().equals(this) && NetworkModifierManager.getCustomModifiers() != null) {
                NetworkModifierManager.getCustomModifiers().forEach((k, v)->{
                    ItemStack stack = new ItemStack(this);
                    setCustomModifier(stack,v.getId());
                    items.add(stack);
                });
            }else {
                super.fillItemGroup(group,items);
            }
        }
    }

    @Override
    public GunSkin getProperties() {
        return this.gunSkin;
    }

    @Override
    public SlotType getSlot() {
        return SlotType.GUN_SKIN;
    }

    @Override
    public boolean canColor(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    //todo: custom item name?
    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack);
    }


}