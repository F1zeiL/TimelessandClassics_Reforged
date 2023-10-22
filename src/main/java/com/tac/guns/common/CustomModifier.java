package com.tac.guns.common;

import com.tac.guns.annotation.Optional;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CustomModifier implements INBTSerializable<CompoundNBT> {
    private ResourceLocation id;
    @Optional
    private ResourceLocation model;
    @Optional
    private float additionalDamage = 0;
    @Optional
    private List<ResourceLocation> canApplyOn;

    public ResourceLocation getId() {
        return id;
    }

    public ResourceLocation getModel() {
        return model;
    }

    public float getAdditionalDamage(){return additionalDamage;}
    @Nullable
    public List<ResourceLocation> getCanApplyOn() {
        return canApplyOn;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("id",id.toString());
        if(model!=null){
            nbt.putString("model",model.toString());
        }
        if(additionalDamage!=0){
            nbt.putFloat("additionalDamage",additionalDamage);
        }
        if(canApplyOn!=null){
            ListNBT listNBT = new ListNBT();
            for(ResourceLocation rl : canApplyOn){
                listNBT.add(StringNBT.valueOf(rl.toString()));
            }
            nbt.put("canApplyOn",listNBT);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(nbt.contains("id")){
            this.id = ResourceLocation.tryCreate(nbt.getString("id"));
        }
        if(nbt.contains("model")){
            this.model = ResourceLocation.tryCreate(nbt.getString("model"));
        }
        if(nbt.contains("additionalDamage")){
            this.additionalDamage = nbt.getFloat("additionalDamage");
        }
        if(nbt.contains("canApplyOn",Constants.NBT.TAG_LIST)){
            ListNBT listNBT = nbt.getList("canApplyOn",Constants.NBT.TAG_STRING);
            if(!listNBT.isEmpty()){
                this.canApplyOn = new ArrayList<>();
                listNBT.forEach((s)->{
                    ResourceLocation location = ResourceLocation.tryCreate(s.getString());
                    if(location!=null){
                        Item item = ForgeRegistries.ITEMS.getValue(location);
                        if(item instanceof TimelessGunItem){
                            canApplyOn.add(location);
                        }
                    }
                });
            }
        }
    }


}