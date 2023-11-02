package com.tac.guns.item;

import com.tac.guns.common.attachments.NetworkModifierManager;
import com.tac.guns.common.attachments.CustomModifierData;
import com.tac.guns.common.attachments.PerkTipsBuilder;
import com.tac.guns.common.attachments.Perks;
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

    @Nullable
    public static CustomModifierData getCustomModifier(ItemStack stack){
        if(hasCustomModifier(stack)) {
            assert stack.getTag() != null;
            String raw = stack.getTag().getString(CUSTOM_MODIFIER);
            ResourceLocation loc = ResourceLocation.tryCreate(raw);
            if(loc!=null){
                return NetworkModifierManager.getCustomModifier(loc);
            }
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CustomModifierData info = getCustomModifier(stack);
        if(info!=null){
            List<ITextComponent> perks = new PerkTipsBuilder(info)
                    .add(Perks.additionalDamage)
                    .build();

            if(!perks.isEmpty()){
                tooltip.add(new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
                tooltip.addAll(perks);
            }

            List<ITextComponent> list = getSuitableGuns(info);
            if(!list.isEmpty()){
                tooltip.add(new TranslationTextComponent("limit.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
                tooltip.addAll(list);
            }
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

    private static List<ITextComponent> getSuitableGuns(CustomModifierData modifier){
        List<ITextComponent> list = new ArrayList<>();
        if (modifier.getSuitableGuns() != null) {
            modifier.getSuitableGuns().forEach((rl)->{
                Item item = ForgeRegistries.ITEMS.getValue(rl);
                if(item instanceof TimelessGunItem){
                    list.add(new TranslationTextComponent(item.getTranslationKey()).mergeStyle(TextFormatting.GREEN));
                }
            });
        }
        return list;
    }

}