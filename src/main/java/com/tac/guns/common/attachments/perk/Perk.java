package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.function.Function;

/** A single perk entry from the CustomModifier.<br>
 * Use to convenient access to an additional attribute and generate formatting tooltip.<br>
 * Not storage actual data
 */
public abstract class Perk<T> {
    public final String key;
    public final String keyPositive;
    public final String keyNegative;
    @Nullable
    public final Function<T, String> formatter;

    Perk(String key, String keyPositive, String keyNegative, Function<T, String> formatter) {
        this.key = key;
        this.keyPositive = keyPositive;
        this.keyNegative = keyNegative;
        this.formatter = formatter;
    }

    public ITextComponent getPositive(T value){
        if(formatter==null) return new TranslationTextComponent(keyPositive, value).mergeStyle(TextFormatting.GREEN);
        else return new TranslationTextComponent(keyPositive,formatter.apply(value)).mergeStyle(TextFormatting.GREEN);
    }

    public ITextComponent getNegative(T value){
        if(formatter==null) return new TranslationTextComponent(keyNegative, value).mergeStyle(TextFormatting.RED);
        else return new TranslationTextComponent(keyPositive,formatter.apply(value)).mergeStyle(TextFormatting.RED);
    }

    public abstract T getValue(CustomModifierData data);
}
