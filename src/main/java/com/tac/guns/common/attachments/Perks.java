package com.tac.guns.common.attachments;

import com.tac.guns.common.attachments.perk.FloatPerk;
import com.tac.guns.common.attachments.perk.Perk;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Perks {
    public static class Formatter{
        public static Function<Float, String> TO_HEART = value -> ItemStack.DECIMALFORMAT.format(value/2.0);
    }
    public static List<Perk<?>> perkList = new ArrayList<>();

    public static FloatPerk additionalDamage = registerPerk(
            new FloatPerk("additionalDamage",
                    "perk.tac.additional_damage.positivev2", "perk.tac.additional_damage.positivev2",
                    Formatter.TO_HEART, CustomModifierData::getAdditionalDamage));

    public static <T extends Perk<?>> T registerPerk(T perk){
        perkList.add(perk);
        return perk;
    }
}
