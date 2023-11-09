package com.tac.guns.util;

import com.tac.guns.common.Gun;
import com.tac.guns.common.attachments.CustomModifierData;
import com.tac.guns.common.attachments.Perks;
import com.tac.guns.common.attachments.perk.DoublePerk;
import com.tac.guns.common.attachments.perk.FloatPerk;
import com.tac.guns.common.container.slot.SlotType;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.item.attachment.impl.Attachment;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunModifierHelper
{
    private static final IGunModifier[] EMPTY = {};
    public static double applyMultiplier(ItemStack weapon, double input, DoublePerk p){
        double output = input;
        for(int i = 0; i < SlotType.values().length; i++) {
            CustomModifierData modifier = getCustomModifier(weapon,SlotType.values()[i]);
            double perk = 0;
            if (modifier != null) {
                perk = p.getValue(modifier);
            }
            if(perk>0){
                output *= perk;
            }
        }
        return output;
    }

    public static float applyMultiplier(ItemStack weapon, float input, FloatPerk p){
        float output = input;
        for(int i = 0; i < SlotType.values().length; i++) {
            CustomModifierData modifier = getCustomModifier(weapon,SlotType.values()[i]);
            float perk = 0;
            if (modifier != null) {
                perk = p.getValue(modifier);
            }
            if(perk>0){
                output *= perk;
            }
        }
        return output;
    }

    public static float applyAdditional(ItemStack weapon, float input, FloatPerk p){
        float output = input;
        for(int i = 0; i < SlotType.values().length; i++) {
            CustomModifierData modifier = getCustomModifier(weapon,SlotType.values()[i]);
            if (modifier != null) {
                output += p.getValue(modifier);
            }
        }
        return output;
    }
    @Nullable
    public static CustomModifierData getCustomModifier(ItemStack weapon, SlotType type){
        if(weapon.getItem() instanceof TimelessGunItem){
            ItemStack stack = Gun.getAttachment(type,weapon);
            if(stack!=null){
                return Attachment.getCustomModifier(stack);
            }
        }
        return null;
    }

    private static IGunModifier[] getModifiers(ItemStack weapon)
    {
        if(!weapon.isEmpty())
        {
            if(weapon.getItem() instanceof TimelessGunItem)
            {
                TimelessGunItem gunItem = (TimelessGunItem) weapon.getItem();
                return gunItem.getModifiers();
            }
        }
        return EMPTY;
    }

    public static int getModifiedProjectileLife(ItemStack weapon, int life)
    {
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                life = modifier.modifyProjectileLife(life);
//            }
//        }

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            life = modifier.modifyProjectileLife(life);
        }
        return life;
    }

    public static double getModifiedProjectileGravity(ItemStack weapon, double gravity)
    {
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                gravity = modifier.modifyProjectileGravity(gravity);
//            }
//        }

        IGunModifier[] modifiersD = getModifiers(weapon);
        for(IGunModifier modifier : modifiersD)
        {
            gravity = modifier.modifyProjectileGravity(gravity);
        }

//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                gravity += modifier.additionalProjectileGravity();
//            }
//        }

        IGunModifier[] modifierD = getModifiers(weapon);
        for(IGunModifier modifier : modifierD)
        {
            gravity += modifier.additionalProjectileGravity();
        }
        return gravity;
    }

    public static float getModifiedSpread(ItemStack weapon, float spread)
    {
        spread = applyMultiplier(weapon,spread,Perks.modifyProjectileSpread);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            spread = modifier.modifyProjectileSpread(spread);
        }
        return spread;
    }

    public static float getModifiedFirstShotSpread(ItemStack weapon, float spread)
    {
        spread = applyMultiplier(weapon,spread,Perks.modifyFirstShotSpread);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            spread = modifier.modifyFirstShotSpread(spread);
        }
        return spread;
    }

    public static float getModifiedHipFireSpread(ItemStack weapon, float spread)
    {
        spread = applyMultiplier(weapon,spread,Perks.modifyHipFireSpread);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            spread = modifier.modifyHipFireSpread(spread);
        }
        return spread;
    }

    public static double getModifiedProjectileSpeed(ItemStack weapon, double speed)
    {
        speed = applyMultiplier(weapon,speed,Perks.modifyProjectileSpeed);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            speed = modifier.modifyProjectileSpeed(speed);
        }
        return speed;
    }

    public static float getFireSoundVolume(ItemStack weapon)
    {
        float volume = 1.0F;
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                volume = modifier.modifyFireSoundVolume(volume);
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            volume = modifier.modifyFireSoundVolume(volume);
        }
        return MathHelper.clamp(volume, 0.0F, 16.0F);
    }

    public static double getMuzzleFlashSize(ItemStack weapon, double size)
    {
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                size = modifier.modifyMuzzleFlashSize(size);
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            size = modifier.modifyMuzzleFlashSize(size);
        }
        return size;
    }

    public static float getKickReduction(ItemStack weapon)
    {
        float kickReduction = 1.0F;
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                kickReduction *= MathHelper.clamp(modifier.kickModifier(), 0.0F, 1.0F);
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            kickReduction *= MathHelper.clamp(modifier.kickModifier(), 0.0F, 1.0F);
        }
        return 1.0F - kickReduction;
    }

    public static float getRecoilSmootheningTime(ItemStack weapon)
    {
        float recoilTime = 1;
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                recoilTime *= MathHelper.clamp(modifier.modifyRecoilSmoothening(), 1.0F, 2.0F);
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            recoilTime *= MathHelper.clamp(modifier.modifyRecoilSmoothening(), 1.0F, 2.0F);
        }
        return recoilTime;
    }

    public static float getRecoilModifier(ItemStack weapon)
    {
        float recoilReduction = 1.0F;

        recoilReduction = applyMultiplier(weapon,recoilReduction,Perks.recoilModifier);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            recoilReduction *= MathHelper.clamp(modifier.recoilModifier(), 0.0F, 1.0F);
        }
        return 1.0F - recoilReduction;
    }

    public static float getHorizontalRecoilModifier(ItemStack weapon)
    {
        float reduction = 1.0F;

        reduction = applyMultiplier(weapon,reduction,Perks.horizontalRecoilModifier);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            reduction *= MathHelper.clamp(modifier.horizontalRecoilModifier(), 0.0F, 1.0F);
        }
        return 1.0F - reduction;
    }

    public static boolean isSilencedFire(ItemStack weapon)
    {
        for(int i = 0; i < SlotType.values().length; i++) {
            CustomModifierData modifier = getCustomModifier(weapon,SlotType.values()[i]);
            if (modifier != null) {
                if(modifier.getGeneral().isSilencedFire()){
                    return true;
                }
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            if(modifier.silencedFire())
            {
                return true;
            }
        }
        return false;
    }



    public static double getModifiedFireSoundRadius(ItemStack weapon, double radius)
    {
        double minRadius = radius;

        minRadius = applyMultiplier(weapon,minRadius,Perks.modifyFireSoundRadius);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            double newRadius = modifier.modifyFireSoundRadius(radius);
            if(newRadius < minRadius)
            {
                minRadius = newRadius;
            }
        }
        return MathHelper.clamp(minRadius, 0.0, Double.MAX_VALUE);
    }

    public static float getAdditionalDamage(ItemStack weapon) {
        float additionalDamage = 0.0F;
        //from attachment item
        additionalDamage = applyAdditional(weapon,additionalDamage,Perks.additionalDamage);

        for(int i = 0; i < IAttachment.Type.values().length; i++) {
            //from attachment nbt
            ItemStack attachment = Gun.getAttachment(IAttachment.Type.values()[i], weapon);
            if(attachment.hasTag()){
                additionalDamage += Gun.getAdditionalDamage(attachment);
            }
        }

        //from gun nbt
        additionalDamage += Gun.getAdditionalDamage(weapon);
        //from gun item
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers) {
            additionalDamage += modifier.additionalDamage();
        }
        return additionalDamage;
    }

    public static float getAdditionalHeadshotDamage(ItemStack weapon)
    {
        float additionalDamage = 0.0F;

        additionalDamage = applyAdditional(weapon,additionalDamage,Perks.additionalHeadshotDamage);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            additionalDamage += modifier.additionalHeadshotDamage();
        }
        return additionalDamage;
    }

    public static float getModifiedProjectileDamage(ItemStack weapon, float damage)
    {
        float finalDamage = damage;

        finalDamage = applyMultiplier(weapon,finalDamage,Perks.modifyProjectileDamage);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            finalDamage = modifier.modifyProjectileDamage(finalDamage);
        }
        return finalDamage;
    }

    public static float getModifiedDamage(ItemStack weapon, Gun modifiedGun, float damage)
    {
        float finalDamage;

        finalDamage = applyMultiplier(weapon,damage,Perks.modifyProjectileDamage);

        IGunModifier[] modifiersD1 = getModifiers(weapon);
        for(IGunModifier modifier : modifiersD1)
        {
            finalDamage = modifier.modifyProjectileDamage(finalDamage);
        }

        return finalDamage;
    }

    public static double getModifiedAimDownSightSpeed(ItemStack weapon, double speed)
    {
        speed = applyMultiplier(weapon,speed,Perks.modifyAimDownSightSpeed);

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            speed = modifier.modifyAimDownSightSpeed(speed);
        }
        return MathHelper.clamp(speed, 0.01, Double.MAX_VALUE);
    }

    public static int getModifiedRate(ItemStack weapon, int rate)
    {
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                rate = modifier.modifyFireRate(rate);
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            rate = modifier.modifyFireRate(rate);
        }
        return MathHelper.clamp(rate, 0, Integer.MAX_VALUE);
    }

    public static float getCriticalChance(ItemStack weapon)
    {
        float chance = 0F;
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                chance += modifier.criticalChance();
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            chance += modifier.criticalChance();
        }
        chance += GunEnchantmentHelper.getPuncturingChance(weapon);
        return MathHelper.clamp(chance, 0F, 1F);
    }

    public static float getAdditionalWeaponWeight(ItemStack weapon)
    {
        float additionalWeight = 0.0F;
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                additionalWeight += modifier.additionalWeaponWeight();
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            additionalWeight += modifier.additionalWeaponWeight();
        }
        return additionalWeight;
    }

    public static float getModifierOfWeaponWeight(ItemStack weapon)
    {
        float modifierWeight = 0.0F;
//        for(int i = 0; i < IAttachment.Type.values().length; i++)
//        {
//            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
//            for(IGunModifier modifier : modifiers)
//            {
//                modifierWeight += modifier.modifyWeaponWeight();
//            }
//        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            modifierWeight += modifier.modifyWeaponWeight();
        }
        return modifierWeight;
    }

    public static int getAmmoCapacity(ItemStack weapon, Gun modifiedGun)
    {
        int capacity = modifiedGun.getReloads().isOpenBolt() ? modifiedGun.getReloads().getMaxAmmo() : modifiedGun.getReloads().getMaxAmmo()+1;
        int level = getAmmoCapacityWeight(weapon);
        if(level > -1 && level < modifiedGun.getReloads().getMaxAdditionalAmmoPerOC().length)
        {
            capacity += modifiedGun.getReloads().getMaxAdditionalAmmoPerOC()[level];
        }
        else if(level > -1)
        {
            capacity += (capacity / 2) * level-3;
        }
        return capacity;
    }
    public static int getAmmoCapacityWeight(ItemStack weapon)
    {
        int modifierWeight = -1;
        for(int i = 0; i < SlotType.values().length; i++) {
            CustomModifierData modifier = getCustomModifier(weapon,SlotType.values()[i]);
            if (modifier != null) {
                modifierWeight = Math.max(modifier.getGeneral().getAdditionalAmmunition(), modifierWeight);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            modifierWeight = Math.max(modifier.additionalAmmunition(), modifierWeight);
        }
        modifierWeight = Math.min(modifierWeight,2);
        return modifierWeight;
    }

    public static ResourceLocation getAdditionalSkin(ItemStack weapon) {
        CustomModifierData modifier = getCustomModifier(weapon,SlotType.GUN_SKIN);

        if(modifier!=null){
            return modifier.getSkin();
        }

        return null;
    }
}
