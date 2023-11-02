package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Supplier;

/** float type perk.
 */
public class FloatPerk extends SignedPerk<Float>{
    private final Function<CustomModifierData,Float> getter;
    public FloatPerk(String key, String keyPositive, String keyNegative, Function<Float, String> formatter,
                     Function<CustomModifierData,Float> getter) {
        super(key, keyPositive, keyNegative,formatter);
        this.getter = getter;
    }

    @Override
    public Float getValue(CustomModifierData data) {
        if(getter!=null)return getter.apply(data);
        return 0.0f;
    }

    @Override
    public void write(CompoundNBT tag, CustomModifierData data) {
        tag.putFloat(getKey(),getValue(data));
    }

    @Override
    public void read(CompoundNBT tag, CustomModifierData data){
        try {
            if(tag.contains(getKey(), Constants.NBT.TAG_FLOAT)){
                Field field = data.getClass().getDeclaredField(getKey());
                if(field.getType() == float.class){
                    field.setAccessible(true);
                    field.setFloat(data,tag.getFloat(getKey()));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
