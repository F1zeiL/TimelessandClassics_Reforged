package com.tac.guns.item.attachment.impl;

import com.tac.guns.Reference;
import com.tac.guns.common.attachments.NetworkModifierManager;
import com.tac.guns.common.attachments.CustomModifierData;
import com.tac.guns.common.attachments.PerkTipsBuilder;
import com.tac.guns.common.attachments.Perks;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.GunSkinItem;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * The base attachment object
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public abstract class Attachment {
    public static final String CUSTOM_MODIFIER = "CustomModifier";
    private ResourceLocation defaultModifier;
    private List<ITextComponent> perks = null;


    public Attachment(ResourceLocation defaultModifier) {
        this.defaultModifier = defaultModifier;
    }

    public Attachment(IGunModifier[] modifier) {
    }

    public static boolean hasCustomModifier(ItemStack stack){
        return stack!=null && stack.getTag()!=null && stack.getTag().contains(CUSTOM_MODIFIER,Constants.NBT.TAG_STRING);
    }
    public static CustomModifierData getCustomModifier(ItemStack stack, boolean isLocal){
        if(!(stack.getItem() instanceof IAttachment)){
            return null;
        }
        ResourceLocation loc = ((IAttachment<?>) stack.getItem()).getProperties().getDefaultModifier();
        if(hasCustomModifier(stack)) {
            if (stack.getTag() != null) {
                String raw = stack.getTag().getString(CUSTOM_MODIFIER);
                ResourceLocation tmp = ResourceLocation.tryCreate(raw);
                if(tmp!=null) loc = tmp;
            }
        }
        if(loc!=null){
            if(isLocal){
                return NetworkModifierManager.getLocalCustomModifier(loc);
            }else {
                return NetworkModifierManager.getCustomModifier(loc);
            }
        }
        return null;
    }

    public ResourceLocation getDefaultModifier() {
        return defaultModifier;
    }

    /** Extra check of an attachment, limited by the tag on it.
     * @param attachment the attachment about to apply
     * @param gun the gun item
     * */
    public static boolean canApplyOn(ItemStack attachment, TimelessGunItem gun,boolean isLocal){
        if(gun.getRegistryName()==null)return false;

        CustomModifierData data = getCustomModifier(attachment,isLocal);
        if(data!=null){
            return data.canApplyOn(gun);
        }

        return true;
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void addInformationEvent(ItemTooltipEvent event) {
        ItemStack stack= event.getItemStack();
        List<ITextComponent> tooltip = event.getToolTip();
        CustomModifierData info = getCustomModifier(stack,true);
        if(info!=null){
            List<ITextComponent> perks = new PerkTipsBuilder(info)
                    .add(Perks.silencedFire)
                    .addPercentage(Perks.modifyFireSoundRadius)
                    .add(Perks.additionalDamage)
                    .add(Perks.additionalHeadshotDamage)
                    .addPercentage(Perks.modifyProjectileDamage)
                    .addPercentage(Perks.modifyProjectileSpeed)
                    .addPercentage(Perks.modifyProjectileSpread)
                    .addPercentage(Perks.modifyFirstShotSpread)
                    .addPercentage(Perks.modifyHipFireSpread)
                    .addPercentage(Perks.recoilModifier)
                    .addPercentage(Perks.horizontalRecoilModifier)
                    .addPercentage(Perks.modifyAimDownSightSpeed)
                    .add(Perks.additionalWeaponWeight)
                    .add(Perks.modifyWeaponWeight)
                    .build();

            if(!perks.isEmpty()){
                tooltip.add(new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GOLD, TextFormatting.BOLD));
                tooltip.addAll(perks);
            }

            if(!info.isHideLimitInfo()){
                List<ITextComponent> list = getSuitableGuns(info);
                if(!list.isEmpty()){
                    tooltip.add(new TranslationTextComponent("limit.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
                    tooltip.addAll(list);
                }
            }

            if(info.getExtraTooltip()!=null){
                info.getExtraTooltip().forEach((s)-> tooltip.add(new TranslationTextComponent(s)));
            }
        }
    }

    /* Determines the perks of attachments and caches them */
//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
//    public static void addInformationEvent(ItemTooltipEvent event)
//    {
//        if(true)return;
//        ItemStack stack = event.getItemStack();
//        if(stack.getItem() instanceof IAttachment<?>)
//        {
//            IAttachment<?> attachment = (IAttachment<?>) stack.getItem();
//            List<ITextComponent> perks = attachment.getProperties().getPerks();
//
//            if (perks != null && !perks.isEmpty()) {
//                event.getToolTip().add(new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GOLD, TextFormatting.BOLD));
//                event.getToolTip().addAll(perks);
//                return;
//            }
//
//            List<ITextComponent> positivePerks = new ArrayList<>();
//            List<ITextComponent> negativePerks = new ArrayList<>();
//
//            /* Test for fire sound volume *//*
//            float inputSound = 1.0F;
//            float outputSound = inputSound;
//            for (IGunModifier modifier : modifiers) {
//                outputSound = modifier.modifyFireSoundVolume(outputSound);
//            }
//            if (outputSound > inputSound) {
//                addPerk(negativePerks, "perk.tac.fire_volume.negative", new TranslationTextComponent("+" + String.valueOf((1.0F - Math.round(outputSound)) * 100) + "% Volume").mergeStyle(TextFormatting.RED));
//            } else if (outputSound < inputSound) {
//                addPerk(negativePerks, "perk.tac.fire_volume.negative", new TranslationTextComponent("" + String.valueOf((1.0F - Math.round(outputSound)) * 100) + "% Volume").mergeStyle(TextFormatting.GREEN));
//                //addPerk(positivePerks, "perk.tac.fire_volume.positive", TextFormatting.GREEN, "-" + String.valueOf((1.0F - outputSound) * 100) + new TranslationTextComponent("perk.tac.vol"));
//            }*/
//
//            /* Test for silenced */
//            for (IGunModifier modifier : modifiers) {
//                if (modifier.silencedFire()) {
//                    addPerkP(positivePerks, "perk.tac.silenced.positive", new TranslationTextComponent("perk.tac.silencedv2").mergeStyle(TextFormatting.GREEN));
//                    break;
//                }
//            }
//
//            /* Test for sound radius */
//            double inputRadius = 10.0;
//            double outputRadius = inputRadius;
//            for (IGunModifier modifier : modifiers) {
//                outputRadius = modifier.modifyFireSoundRadius(outputRadius);
//            }
//            if (outputRadius > inputRadius) {
//                addPerkN(negativePerks, "perk.tac.sound_radius.negative", new TranslationTextComponent("-")
//                        .append(new TranslationTextComponent("perk.tac.sound_radiusv2",Math.round(outputRadius)).mergeStyle(TextFormatting.RED)));
//            } else if (outputRadius < inputRadius) {
//                addPerkP(positivePerks, "perk.tac.sound_radius.positive", new TranslationTextComponent("+")
//                        .append(new TranslationTextComponent("perk.tac.sound_radiusv2",Math.round(outputRadius)).mergeStyle(TextFormatting.GREEN)));
//            }
//
//            /* Test for additional damage */
//            float additionalDamage = 0.0F;
//            for (IGunModifier modifier : modifiers) {
//                additionalDamage += modifier.additionalDamage();
//            }
//            if (additionalDamage > 0.0F) {
//                addPerkP(positivePerks, "perk.tac.additional_damage.positivev2", ItemStack.DECIMALFORMAT.format(additionalDamage / 2.0));
//            } else if (additionalDamage < 0.0F) {
//                addPerkN(negativePerks, "perk.tac.additional_damage.negativev2", ItemStack.DECIMALFORMAT.format(additionalDamage / 2.0));
//            }
//
//            /* Test for additional headshot damage */
//            float additionalHeadshotDamage = 0.0F;
//            for (IGunModifier modifier : modifiers) {
//                additionalHeadshotDamage += modifier.additionalHeadshotDamage();
//            }
//            if (additionalHeadshotDamage > 0.0F) {
//                addPerkP(positivePerks, "perk.tac.additional_damage.positiveh", ItemStack.DECIMALFORMAT.format(additionalHeadshotDamage / 2.0));
//            } else if (additionalHeadshotDamage < 0.0F) {
//                addPerkN(negativePerks, "perk.tac.additional_damage.negativeh", ItemStack.DECIMALFORMAT.format(additionalHeadshotDamage / 2.0));
//            }
//
//            /* Test for modified damage */
//            float inputDamage = 10.0F;
//            float outputDamage = inputDamage;
//            for (IGunModifier modifier : modifiers) {
//                outputDamage = modifier.modifyProjectileDamage(outputDamage);
//            }
//            if (outputDamage > inputDamage) {
//                addPerkP(positivePerks, "perk.tac.modified_damage.positive", new TranslationTextComponent("perk.tac.modified_damage.positivev2", outputDamage).mergeStyle(TextFormatting.GREEN));
//            } else if (outputDamage < inputDamage) {
//                addPerkN(positivePerks, "perk.tac.modified_damage.negative", new TranslationTextComponent("perk.tac.modified_damage.negativev2", outputDamage).mergeStyle(TextFormatting.RED));
//            }
//
//            /* Test for modified damage */
//            double inputSpeed = 10.0;
//            double outputSpeed = inputSpeed;
//            for (IGunModifier modifier : modifiers) {
//                outputSpeed = modifier.modifyProjectileSpeed(outputSpeed);
//            }
//            if (outputSpeed > inputSpeed) {
//                addPerkP(positivePerks, "perk.tac.projectile_speed.positive", new TranslationTextComponent("perk.tac.projectile_speed.positivev2", Math.round((10.0F - outputSpeed) * 10)+"%"));
//            } else if (outputSpeed < inputSpeed) {
//                addPerkN(negativePerks, "perk.tac.projectile_speed.negative", new TranslationTextComponent("perk.tac.projectile_speed.negativev2", Math.round((10.0F - outputSpeed) * 10)+"%"));
//            }
//
//            /* Test for modified projectile spread */
//            float inputSpread = 10.0F;
//            float outputSpread = inputSpread;
//            for (IGunModifier modifier : modifiers) {
//                outputSpread = modifier.modifyProjectileSpread(outputSpread);
//            }
//            if (outputSpread > inputSpread) {
//                addPerkN(negativePerks, "perk.tac.projectile_spread.negative", new TranslationTextComponent("perk.tac.projectile_spread.negativev2", Math.round((10.0F - outputSpread) * 10)+"%").mergeStyle(TextFormatting.RED));
//            } else if (outputSpread < inputSpread) {
//                addPerkP(positivePerks, "perk.tac.projectile_spread.positive", new TranslationTextComponent("perk.tac.projectile_spread.positivev2", Math.round((10.0F - outputSpread) * 10)+"%").mergeStyle(TextFormatting.GREEN));
//            }
//
//            /* Test for modified projectile spread */
//            float inputFirstSpread = 10.0F;
//            float outputFirstSpread = inputFirstSpread;
//            for (IGunModifier modifier : modifiers) {
//                outputFirstSpread = modifier.modifyFirstShotSpread(outputFirstSpread);
//            }
//            if (outputFirstSpread > inputFirstSpread) {
//                addPerkN(negativePerks, "perk.tac.projectile_spread_first.negativev2", Math.round((10.0F - outputFirstSpread) * 10f) + "%");
//            } else if (outputFirstSpread < inputFirstSpread) {
//                addPerkP(positivePerks, "perk.tac.projectile_spread_first.positivev2", Math.round((10.0F - outputFirstSpread) * 10f) + "%");
//            }
//
//            /* Test for modified projectile spread */
//            float inputHipFireSpread = 10.0F;
//            float outputHipFireSpread = inputHipFireSpread;
//            for (IGunModifier modifier : modifiers) {
//                outputHipFireSpread = modifier.modifyHipFireSpread(outputHipFireSpread);
//            }
//            if (outputHipFireSpread > inputHipFireSpread) {
//                addPerkN(negativePerks, "perk.tac.projectile_spread_hip.negativev2", Math.round((10.0F - outputHipFireSpread) * 10f) + "%");
//            } else if (outputHipFireSpread < inputHipFireSpread) {
//                addPerkP(positivePerks, "perk.tac.projectile_spread_hip.positivev2", Math.round((10.0F - outputHipFireSpread) * 10f) + "%");
//            }
//
//            /* Test for modified projectile life */
//            int inputLife = 100;
//            int outputLife = inputLife;
//            for (IGunModifier modifier : modifiers) {
//                outputLife = modifier.modifyProjectileLife(outputLife);
//            }
//            if (outputLife > inputLife) {
//                addPerkP(positivePerks, "perk.tac.projectile_life.positivev2", String.valueOf(outputLife));
//            } else if (outputLife < inputLife) {
//                addPerkN(positivePerks, "perk.tac.projectile_life.negativev2", String.valueOf(outputLife));
//            }
//
//            /* Test for modified recoil */
//            float inputRecoil = 10.0F;
//            float outputRecoil = inputRecoil;
//            for (IGunModifier modifier : modifiers) {
//                outputRecoil *= modifier.recoilModifier();
//            }
//            if (outputRecoil > inputRecoil) {
//                addPerkN(negativePerks, "perk.tac.recoil.negativev2", Math.round((10.0F - outputRecoil) * -10f) + "%");
//            } else if (outputRecoil < inputRecoil) {
//                addPerkP(positivePerks, "perk.tac.recoil.positivev2", Math.round((10.0F - outputRecoil) * -10f) + "%");
//            }
//
//            float inputHRecoil = 10.0F;
//            float outputHRecoil = inputHRecoil;
//            for (IGunModifier modifier : modifiers) {
//                outputHRecoil *= modifier.horizontalRecoilModifier();
//            }
//            if (outputHRecoil > inputHRecoil) {
//                addPerkN(negativePerks, "perk.tac.recoilh.negativev2", Math.round((10.0F - outputHRecoil) * -10f) + "%");
//            } else if (outputHRecoil < inputHRecoil) {
//                addPerkP(positivePerks, "perk.tac.recoilh.positivev2", Math.round((10.0F - outputHRecoil) * -10f) + "%");
//            }
//
//            /* Test for aim down sight speed */
//            double inputAdsSpeed = 10.0;
//            double outputAdsSpeed = inputAdsSpeed;
//            for (IGunModifier modifier : modifiers) {
//                outputAdsSpeed = modifier.modifyAimDownSightSpeed(outputAdsSpeed);
//            }
//            if (outputAdsSpeed > inputAdsSpeed) {
//                addPerkP(positivePerks, "perk.tac.ads_speed.positivev2", Math.round((10.0F - outputAdsSpeed) * 10f) + "%");
//            } else if (outputAdsSpeed < inputAdsSpeed) {
//                addPerkN(negativePerks, "perk.tac.ads_speed.negativev2", Math.round((10.0F - outputAdsSpeed) * 10f) + "%");
//            }
//
//            /* Test for fire rate */
//            int inputRate = 10;
//            int outputRate = inputRate;
//            for (IGunModifier modifier : modifiers) {
//                outputRate = modifier.modifyFireRate(outputRate);
//            }
//            if (outputRate > inputRate) {
//                addPerkN(negativePerks, "perk.tac.rate.negative");
//            } else if (outputRate < inputRate) {
//                addPerkP(positivePerks, "perk.tac.rate.positive");
//            }
//
//            positivePerks.addAll(negativePerks);
//            attachment.getProperties().setPerks(positivePerks);
//            if (!positivePerks.isEmpty()) {
//                event.getToolTip().add(new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
//                event.getToolTip().addAll(positivePerks);
//            }
//            if (stack.getTag() != null && stack.getTag().contains("Limit", Constants.NBT.TAG_LIST)) {
//                List<ITextComponent> list = getSuitableGuns(stack);
//                if(!list.isEmpty()){
//                    event.getToolTip().add(new TranslationTextComponent("limit.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
//                    event.getToolTip().addAll(list);
//                }
//            }
//        }
//    }

    private static void addPerk(List<ITextComponent> components, String id, Object... params)
    {
        //TextFormatting format,   components.add(new TranslationTextComponent("perk.tac.entry.negative", new TranslationTextComponent(id, params).mergeStyle(format)));
        components.add(new TranslationTextComponent("perk.tac.entry.negative", new TranslationTextComponent(id, params).mergeStyle(TextFormatting.AQUA)));
    }

    private static void addPerkP(List<ITextComponent> components, String id, Object... params)
    {
        //TextFormatting format,   components.add(new TranslationTextComponent("perk.tac.entry.negative", new TranslationTextComponent(id, params).mergeStyle(format)));
        components.add(new TranslationTextComponent(id, params).mergeStyle(TextFormatting.GREEN));
    }
    private static void addPerkN(List<ITextComponent> components, String id, Object... params)
    {
        //TextFormatting format,   components.add(new TranslationTextComponent("perk.tac.entry.negative", new TranslationTextComponent(id, params).mergeStyle(format)));
        components.add(new TranslationTextComponent(id, params).mergeStyle(TextFormatting.RED));
    }
}
