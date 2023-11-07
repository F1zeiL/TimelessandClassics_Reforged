package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Function;

/** A single perk entry from the CustomModifier.<br>
 * Use to get additional attribute and generate formatting tooltip.<br>
 * Not storage actual data
 */
public abstract class Perk<T> {
    protected Function<CustomModifierData, T> getter;
    private final String key;

    public Perk(String key, Function<CustomModifierData, T> getter) {
        this.key = key;
        this.getter = getter;
    }

    public String getKey() {
        return key;
    }

    public T getValue(CustomModifierData data){
        return getter.apply(data);
    }
    public abstract void write(CompoundNBT tag, CustomModifierData data);
    public abstract void read(CompoundNBT tag, CustomModifierData data);
}
