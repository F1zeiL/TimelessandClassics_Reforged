package com.tac.guns.common.attachments;

import com.tac.guns.common.attachments.perk.*;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Perks {
    public static class Formatter{
        public static Function<Float, String> TO_HEART = value -> ItemStack.DECIMALFORMAT.format(Math.abs(value/2.0));
        public static Function<Double, String> ROUND_PERCENTAGE =
                value -> new DecimalFormat("#.#").format(Math.abs(1.0-value)*100) + "%";

        public static Function<Float, String> ROUND_PERCENTAGE_F =
                value -> new DecimalFormat("#.#").format(Math.abs(1.0-value)*100) + "%";
        public static Function<Float, String> WEIGHT =
                value -> new DecimalFormat("#.##").format(Math.abs(value));
        public static Function<Float, String> WEIGHT_M =
                value -> new DecimalFormat("#.#").format(Math.abs(value*100))+ "%";
    }
    public static List<Perk<?>> perkList = new ArrayList<>();

    public static BooleanPerk silencedFire = registerPerk(
            new BooleanPerk("silencedFire",
                    "perk.tac.silencedv2", CustomModifierData.General::isSilencedFire)
    );

    public static BooleanPerk blastFire = registerPerk(
            new BooleanPerk("blastFire",
                    "perk.tac.blastv2", CustomModifierData.General::isBlastFire)
    );

    public static BooleanPerk igniteFire = registerPerk(
            new BooleanPerk("igniteFire",
                    "perk.tac.ignitev2", CustomModifierData.General::isIgniteFire)
    );

    public static FloatPerk modifyProjectileBlastDamage = registerPerk(
            new FloatPerk("modifyProjectileBlastDamage",
                    "perk.tac.modified_blast_damage.positivev2","perk.tac.modified_blast_damage.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getModifyProjectileBlastDamage)
    );

    public static FloatPerk modifyProjectileArmorIgnore = registerPerk(
            new FloatPerk("modifyProjectileArmorIgnore",
                    "perk.tac.modified_armor_ignore.positivev2","perk.tac.modified_armor_ignore.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getModifyProjectileArmorIgnore)
    );

    public static FloatPerk modifyProjectileHeadDamage = registerPerk(
            new FloatPerk("modifyProjectileHeadDamage",
                    "perk.tac.modified_head_damage.positivev2","perk.tac.modified_head_damage.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getModifyProjectileHeadDamage)
    );

    public static DoublePerk modifyFireSoundRadius = registerPerk(
            new DoublePerk("modifyFireSoundRadius",
                    "perk.tac.sound_radius.positive","perk.tac.sound_radius.negative",
                    Formatter.ROUND_PERCENTAGE, CustomModifierData.General::getModifyFireSoundRadius,true)
    );

    public static FloatPerk additionalDamage = registerPerk(
            new FloatPerk("additionalDamage",
                    "perk.tac.additional_damage.positivev2", "perk.tac.additional_damage.negativev2",
                    Formatter.TO_HEART, CustomModifierData.General::getAdditionalDamage)
    );

    public static FloatPerk additionalHeadshotDamage = registerPerk(
            new FloatPerk("additionalHeadshotDamage",
                    "perk.tac.additional_damage.positiveh", "perk.tac.additional_damage.negativeh",
                    Formatter.TO_HEART, CustomModifierData.General::getAdditionalHeadshotDamage)
    );

    public static FloatPerk modifyProjectileDamage = registerPerk(
            new FloatPerk("modifyProjectileDamage",
                    "perk.tac.modified_damage.positivev2","perk.tac.modified_damage.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getModifyProjectileDamage)
    );

    public static DoublePerk modifyProjectileSpeed = registerPerk(
            new DoublePerk("modifyProjectileSpeed",
                    "perk.tac.projectile_speed.positivev2","perk.tac.projectile_speed.negativev2",
                    Formatter.ROUND_PERCENTAGE, CustomModifierData.General::getModifyProjectileSpeed)
    );

    public static FloatPerk modifyProjectileSpread = registerPerk(
            new FloatPerk("modifyProjectileSpread",
                    "perk.tac.projectile_spread.positivev2","perk.tac.projectile_spread.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getModifyProjectileSpread,true)
    );

    public static FloatPerk modifyFirstShotSpread = registerPerk(
            new FloatPerk("modifyFirstShotSpread",
                    "perk.tac.projectile_spread_first.positivev2","perk.tac.projectile_spread_first.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getModifyFirstShotSpread,true)
    );

    public static FloatPerk modifyHipFireSpread = registerPerk(
            new FloatPerk("modifyHipFireSpread",
                    "perk.tac.projectile_spread_hip.positivev2","perk.tac.projectile_spread_hip.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getModifyHipFireSpread,true)
    );

    public static FloatPerk recoilModifier = registerPerk(
            new FloatPerk("recoilModifier",
                    "perk.tac.recoil.positivev2","perk.tac.recoil.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getRecoilModifier,true)
    );

    public static FloatPerk horizontalRecoilModifier = registerPerk(
            new FloatPerk("horizontalRecoilModifier",
                    "perk.tac.recoilh.positivev2","perk.tac.recoilh.negativev2",
                    Formatter.ROUND_PERCENTAGE_F, CustomModifierData.General::getHorizontalRecoilModifier,true)
    );

    public static DoublePerk modifyAimDownSightSpeed = registerPerk(
            new DoublePerk("modifyAimDownSightSpeed",
                    "perk.tac.ads_speed.positivev2","perk.tac.ads_speed.negativev2",
                    Formatter.ROUND_PERCENTAGE, CustomModifierData.General::getModifyAimDownSightSpeed)
    );

    public static IntPerk additionalAmmunition = registerPerk(
            new IntPerk("additionalAmmunition","","",null,
                    CustomModifierData.General::getAdditionalAmmunition)
    );

    public static FloatPerk additionalWeaponWeight = registerPerk(
            new FloatPerk("additionalWeaponWeight",
                    "perk.tac.additional_weight.positive","perk.tac.additional_weight.negative",
                    Formatter.WEIGHT, CustomModifierData.General::getAdditionalWeaponWeight,true)
    );

    public static FloatPerk modifyFireSoundVolume = registerPerk(
            new FloatPerk("modifyFireSoundVolume","","",null,
                    CustomModifierData.General::getModifyFireSoundVolume)
    );

    public static FloatPerk modifyRecoilSmoothening = registerPerk(
            new FloatPerk("modifyRecoilSmoothening","","",null,
                    CustomModifierData.General::getModifyRecoilSmoothening)
    );

    public static DoublePerk modifyMuzzleFlashSize = registerPerk(
            new DoublePerk("modifyMuzzleFlashSize","","",null,
                    CustomModifierData.General::getModifyMuzzleFlashSize)
    );

    public static FloatPerk modifyWeaponWeight = registerPerk(
            new FloatPerk("modifyWeaponWeight",
                    "perk.tac.modify_weight.positive","perk.tac.modify_weight.negative",
                    Formatter.WEIGHT_M, CustomModifierData.General::getModifyWeaponWeight,true)
    );

    public static FloatPerk kickModifier = registerPerk(
            new FloatPerk("kickModifier","","",null,
                    CustomModifierData.General::getKickModifier)
    );

    public static <T extends Perk<?>> T registerPerk(T perk){
        perkList.add(perk);
        return perk;
    }

    public static void init(){}
}
