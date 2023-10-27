package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;

import java.util.function.Function;

/** float type perk.
 */
public abstract class FloatPerk extends Perk<Float>{
    public FloatPerk(String key, String keyPositive, String keyNegative, Function<Float, String> formatter) {
        super(key, keyPositive, keyNegative,formatter);
    }

    @Override
    public Float getValue(CustomModifierData data) {
        return 0.0f;
    }
}
