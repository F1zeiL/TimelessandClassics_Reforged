package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.lang.reflect.Field;
import java.util.function.Function;

/** float type perk.
 */
public class IntPerk extends SignedPerk<Integer>{
    public IntPerk(String key, String keyPositive, String keyNegative, Function<Integer, String> formatter,
                   Function<CustomModifierData.General, Integer> getter) {
        super(key, keyPositive, keyNegative,formatter,getter);
    }
    public IntPerk(String key, String keyPositive, String keyNegative, Function<Integer, String> formatter,
                   Function<CustomModifierData.General, Integer> getter, boolean harmful) {
        super(key, keyPositive, keyNegative,formatter,getter,harmful);
    }
    @Override
    public Integer getValue(CustomModifierData.General data) {
        if(getter!=null && data!=null)return getter.apply(data);
        return 0;
    }

    @Override
    public void write(CompoundNBT tag, CustomModifierData.General data) {
        tag.putInt(getKey(),getValue(data));
    }

    @Override
    public void read(CompoundNBT tag, CustomModifierData.General data){
        try {
            if(tag.contains(getKey(), Constants.NBT.TAG_INT)){
                Field field = data.getClass().getDeclaredField(getKey());
                if(field.getType() == int.class){
                    field.setAccessible(true);
                    field.setInt(data,tag.getInt(getKey()));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
