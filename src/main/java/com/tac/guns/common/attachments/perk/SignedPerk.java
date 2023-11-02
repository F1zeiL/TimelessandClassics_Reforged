package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Function;

public abstract class SignedPerk<T> extends Perk<T> {
    SignedPerk(String key, String keyPositive, String keyNegative, Function<T, String> formatter) {
        super(key, keyPositive, keyNegative, formatter);
    }

    public ITextComponent getPositive(CustomModifierData data){
        T value = getValue(data);
        if(getFormatter()==null) return new TranslationTextComponent(getKeyPositive(), value).mergeStyle(TextFormatting.GREEN);
        else return new TranslationTextComponent(getKeyPositive(),getFormatter().apply(value)).mergeStyle(TextFormatting.GREEN);
    }

    public ITextComponent getNegative(CustomModifierData data){
        T value = getValue(data);
        if(getFormatter()==null) return new TranslationTextComponent(getKeyNegative(), value).mergeStyle(TextFormatting.RED);
        else return new TranslationTextComponent(getKeyNegative(),getFormatter().apply(value)).mergeStyle(TextFormatting.RED);
    }

}
