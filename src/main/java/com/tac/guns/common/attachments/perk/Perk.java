package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Function;

/** A single perk entry from the CustomModifier.<br>
 * Use to get additional attribute and generate formatting tooltip.<br>
 * Not storage actual data
 */
public abstract class Perk<T> {
    protected Function<CustomModifierData.General, T> getter;
    private final String key;

    public Perk(String key, Function<CustomModifierData.General, T> getter) {
        this.key = key;
        this.getter = getter;
    }

    public String getKey() {
        return key;
    }
    public T getValue(CustomModifierData data){
        return getValue(data.getGeneral());
    }
    public abstract T getValue(CustomModifierData.General data);
    public abstract void write(CompoundNBT tag, CustomModifierData.General data);
    public abstract void read(CompoundNBT tag, CustomModifierData.General data);
}
