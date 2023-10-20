package com.tac.guns.item;

import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IgunSkin;
import com.tac.guns.item.attachment.impl.GunSkin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class GunSkinItem extends Item implements IgunSkin, IColored {
    private final GunSkin gunSkin;

    public GunSkinItem(GunSkin gunSkin, Properties properties) {
        super(properties);
        this.gunSkin = gunSkin;
    }

    @Override
    public GunSkin getProperties() {
        return this.gunSkin;
    }

    @Override
    public boolean canColor(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public boolean canApplyOn(ItemStack attachment, TimelessGunItem gun){
        if(gun.getRegistryName()==null)return false;
        if(!attachment.hasTag())return true;
        if (attachment.getTag() != null && attachment.getTag().contains("Limit", Constants.NBT.TAG_LIST)) {
            ListNBT listNBT = attachment.getTag().getList("Limit",Constants.NBT.TAG_STRING);

            if(listNBT.isEmpty()) return true;

            for (int i = 0; i < listNBT.size(); i++) {
                String s = listNBT.getString(i);
                if(s.equals(gun.getRegistryName().toString()))return true;
            }

        }
        return false;
    }

    //todo: custom item name
    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack);
    }
}