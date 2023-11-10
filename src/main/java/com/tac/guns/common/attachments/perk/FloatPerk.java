package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.lang.reflect.Field;
import java.util.function.Function;

/** float type perk.
 */
public class FloatPerk extends SignedPerk<Float>{
    public FloatPerk(String key, String keyPositive, String keyNegative, Function<Float, String> formatter,
                     Function<CustomModifierData.General, Float> getter) {
        super(key, keyPositive, keyNegative,formatter,getter);
    }
    public FloatPerk(String key, String keyPositive, String keyNegative, Function<Float, String> formatter,
                     Function<CustomModifierData.General, Float> getter,boolean harmful) {
        super(key, keyPositive, keyNegative,formatter,getter,harmful);
    }
    @Override
    public Float getValue(CustomModifierData.General data) {
        if(getter!=null && data!=null)return getter.apply(data);
        return 0.0f;
    }

    @Override
    public void write(CompoundNBT tag, CustomModifierData.General data) {
        tag.putFloat(getKey(),getValue(data));
    }

    @Override
    public void read(CompoundNBT tag, CustomModifierData.General data){
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
