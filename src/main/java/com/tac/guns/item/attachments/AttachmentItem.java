package com.tac.guns.item.attachments;

import com.tac.guns.common.attachments.*;
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

    public abstract AttachmentType getType();

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
    public static CustomModifierData getModifier(ItemStack stack){
        if(stack.getItem() instanceof AttachmentItem){
            CustomModifierData data = null;
            if(hasCustomModifier(stack)) {
                assert stack.getTag() != null;
                String raw = stack.getTag().getString(CUSTOM_MODIFIER);
                ResourceLocation loc = ResourceLocation.tryCreate(raw);
                if(loc!=null){
                    data = NetworkModifierManager.getCustomModifier(loc);
                }
            }
            if(data==null){
                data = NetworkModifierManager.getCustomModifier( ((AttachmentItem) stack.getItem()).defaultModifier);
            }
            return data;
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
    public static ITextComponent perks_title = new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD);
    public static ITextComponent limits_title = new TranslationTextComponent("limit.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD);
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CustomModifierData info = getModifier(stack);
        if(info!=null){
            List<ITextComponent> perks = new PerkTipsBuilder(info)
                    .add(Perks.additionalDamage)
                    .build();

            if(!perks.isEmpty()){
                tooltip.add(perks_title);
                tooltip.addAll(perks);
            }

            List<ITextComponent> list = getSuitableGuns(info);

            if (!list.isEmpty()) {
                tooltip.add(limits_title);
                tooltip.addAll(list);
            }
        }

    }

}
