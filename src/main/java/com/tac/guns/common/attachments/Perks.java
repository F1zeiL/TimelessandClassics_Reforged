package com.tac.guns.common.attachments;

import com.tac.guns.common.attachments.perk.BooleanPerk;
import com.tac.guns.common.attachments.perk.DoublePerk;
import com.tac.guns.common.attachments.perk.FloatPerk;
import com.tac.guns.common.attachments.perk.Perk;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Perks {
    public static class Formatter{
        public static Function<Float, String> TO_HEART = value -> ItemStack.DECIMALFORMAT.format(value/2.0);
        public static Function<Double, String> ROUND_PERCENTAGE =
                value -> new DecimalFormat("#.#").format(Math.abs(1.0-value)*100) + "%";
    }
    public static List<Perk<?>> perkList = new ArrayList<>();

    public static BooleanPerk silencedFire = registerPerk(
            new BooleanPerk("silencedFire",
                    "perk.tac.silencedv2",  CustomModifierData::isSilencedFire)
    );
    public static DoublePerk modifyFireSoundRadius = registerPerk(
            new DoublePerk("modifyFireSoundRadius",
                    "perk.tac.sound_radius.positive","perk.tac.sound_radius.negative",
                    Formatter.ROUND_PERCENTAGE,CustomModifierData::getModifyFireSoundRadius)
    );

    public static FloatPerk additionalDamage = registerPerk(
            new FloatPerk("additionalDamage",
                    "perk.tac.additional_damage.positivev2", "perk.tac.additional_damage.negativev2",
                    Formatter.TO_HEART, CustomModifierData::getAdditionalDamage)
    );

    public static <T extends Perk<?>> T registerPerk(T perk){
        perkList.add(perk);
        return perk;
    }

    public static void init(){}
}
