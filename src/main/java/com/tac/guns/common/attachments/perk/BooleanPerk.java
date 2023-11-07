package com.tac.guns.common.attachments.perk;

import com.tac.guns.common.attachments.CustomModifierData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import java.lang.reflect.Field;
import java.util.function.Function;

public class BooleanPerk extends Perk<Boolean>{
    private ITextComponent text;
    public BooleanPerk(String key, String keyTranslation, Function<CustomModifierData, Boolean> getter) {
        super(key, getter);
        this.text = new TranslationTextComponent(keyTranslation).mergeStyle(TextFormatting.GREEN);;
    }

    public ITextComponent getText() {
        return text;
    }

    @Override
    public Boolean getValue(CustomModifierData data) {
        if(getter!=null && data!=null)return getter.apply(data);
        return false;
    }

    @Override
    public void write(CompoundNBT tag, CustomModifierData data) {
        tag.putBoolean(getKey(),getValue(data));
    }

    @Override
    public void read(CompoundNBT tag, CustomModifierData data) {
        try {
            if(tag.contains(getKey(), Constants.NBT.TAG_BYTE)){
                Field field = data.getClass().getDeclaredField(getKey());
                if(field.getType() == boolean.class){
                    field.setAccessible(true);
                    field.setBoolean(data,tag.getBoolean(getKey()));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
