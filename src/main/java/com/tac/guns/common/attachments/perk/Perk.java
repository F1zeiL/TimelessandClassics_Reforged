package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.function.Function;

/** A single perk entry from the CustomModifier.<br>
 * Use to get additional attribute and generate formatting tooltip.<br>
 * Not storage actual data
 */
public abstract class Perk<T> {
    private final String key;
    private final String keyPositive;
    private final String keyNegative;
    @Nullable
    private final Function<T, String> formatter;

    public Perk(String key, String keyPositive, String keyNegative, Function<T, String> formatter) {
        this.key = key;
        this.keyPositive = keyPositive;
        this.keyNegative = keyNegative;
        this.formatter = formatter;
    }

    public String getKey() {
        return key;
    }
    public String getKeyPositive() {
        return keyPositive;
    }
    public String getKeyNegative() {
        return keyNegative;
    }
    @Nullable
    public Function<T, String> getFormatter() {
        return formatter;
    }
    public abstract T getValue(CustomModifierData data);
    public abstract void write(CompoundNBT tag, CustomModifierData data);
    public abstract void read(CompoundNBT tag, CustomModifierData data);
}
