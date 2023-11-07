package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.lang.reflect.Field;
import java.util.function.Function;

/** float type perk.
 */
public class DoublePerk extends SignedPerk<Double>{
    public DoublePerk(String key, String keyPositive, String keyNegative, Function<Double, String> formatter,
                      Function<CustomModifierData, Double> getter) {
        super(key, keyPositive, keyNegative,formatter,getter);
    }

    @Override
    public Double getValue(CustomModifierData data) {
        if(getter!=null && data!=null)return getter.apply(data);
        return 0.0;
    }

    @Override
    public void write(CompoundNBT tag, CustomModifierData data) {
        tag.putDouble(getKey(),getValue(data));
    }

    @Override
    public void read(CompoundNBT tag, CustomModifierData data){
        try {
            if(tag.contains(getKey(), Constants.NBT.TAG_DOUBLE)){
                Field field = data.getClass().getDeclaredField(getKey());
                if(field.getType() == double.class){
                    field.setAccessible(true);
                    field.setDouble(data,tag.getFloat(getKey()));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
