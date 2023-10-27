package com.tac.guns.item;

import com.tac.guns.common.attachments.CustomModifierData;
import com.tac.guns.common.attachments.NetworkModifierManager;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public abstract class AttachmentItem extends Item {
    public static final String CUSTOM_MODIFIER = "CustomModifier";

    private ResourceLocation defaultModifier;

    public AttachmentItem(Properties properties) {
        super(properties);
    }

    public AttachmentItem(Properties properties, ResourceLocation defaultModifier) {
        super(properties);
        this.defaultModifier = defaultModifier;
    }

    public static boolean hasCustomModifier(ItemStack stack){
        return stack!=null && stack.getItem() instanceof AttachmentItem && stack.getTag()!=null &&
                stack.getTag().contains(CUSTOM_MODIFIER, Constants.NBT.TAG_STRING);
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CustomModifierData info = getCustomModifier(stack);
        if(info!=null){
            List<ITextComponent> perks = new PerkTipsBuilder()
                    .add(info.getAdditionalDamage(), GunSkinItem.Perks.additionalDamage)
                    .build();

            if(!perks.isEmpty()){
                tooltip.add(new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
                tooltip.addAll(perks);
            }

            List<ITextComponent> list = getSuitableGuns(info);
            if (!list.isEmpty()) {
                tooltip.add(new TranslationTextComponent("limit.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
                tooltip.addAll(list);
            }
        }

    }

    public static class PerkTipsBuilder{
        List<ITextComponent> positivePerks = new ArrayList<>();
        List<ITextComponent> negativePerks = new ArrayList<>();
        PerkTipsBuilder add(float value, Perks perk){
            if (value > 0.0F) {
                positivePerks.add(perk.getPositive(value));
            } else if (value < 0.0F) {
                negativePerks.add(perk.getNegative(value));
            }
            return this;
        }

        List<ITextComponent> build(){
            positivePerks.addAll(negativePerks);
            return positivePerks;
        }
    }

}
