package com.tac.guns.common.attachments;

import com.tac.guns.annotation.Optional;
import com.tac.guns.common.attachments.perk.Perk;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/**Data class used to serialize/deserialize and transfer from data pack.
 * The data should be read from data pack rather than change or create directly.
 */
public class CustomModifierData implements INBTSerializable<CompoundNBT> {
    private ResourceLocation id;
    @Optional private ResourceLocation skin;
    @Optional private List<String> canApplyOn;
    @Optional private List<String> extraTooltip;
    private final List<ITag<Item>> tags = new ArrayList<>();
    private final List<ResourceLocation> items = new ArrayList<>();
    public ResourceLocation getId() {
        return id;
    }
    public ResourceLocation getSkin() {
        return skin;
    }
    public List<String> getExtraTooltip() {
        return extraTooltip;
    }

    public void init() {
        if(canApplyOn!=null){
            for(String raw : canApplyOn){
                if(raw.startsWith("#")){
                    ResourceLocation location = ResourceLocation.tryCreate(raw.substring(1));
                    if(location!=null){
                        ITag<Item> tag = ItemTags.getCollection().get(location);
                        tags.add(tag);
                    }
                }else {
                    ResourceLocation location = ResourceLocation.tryCreate(raw);
                    if(location!=null) {
                        Item item = ForgeRegistries.ITEMS.getValue(location);
                        if (item instanceof TimelessGunItem) {
                            items.add(location);
                        }
                    }
                }
            }
        }
    }

    public static class General implements INBTSerializable<CompoundNBT>{

        @Optional private boolean silencedFire = false;
        @Optional private double modifyFireSoundRadius = 1;
        @Optional private float additionalDamage = 0;
        @Optional private float additionalHeadshotDamage = 0;
        @Optional private float modifyProjectileDamage = 1;
        @Optional private double modifyProjectileSpeed = 1;
        @Optional private float modifyProjectileSpread = 1;
        @Optional private float modifyFirstShotSpread = 1;
        @Optional private float modifyHipFireSpread = 1;
        @Optional private float modifyProjectileLife = 1;
        @Optional private float recoilModifier = 1;
        @Optional private float horizontalRecoilModifier = 1;
        @Optional private double modifyAimDownSightSpeed = 1;
        @Optional private int additionalAmmunition = -1;


        public boolean isSilencedFire() {
            return silencedFire;
        }
        public double getModifyFireSoundRadius() {
            return modifyFireSoundRadius;
        }
        public float getAdditionalDamage() {
            return additionalDamage;
        }
        public float getAdditionalHeadshotDamage() {
            return additionalHeadshotDamage;
        }
        public float getModifyProjectileDamage() {
            return modifyProjectileDamage;
        }
        public double getModifyProjectileSpeed() {
            return modifyProjectileSpeed;
        }
        public float getModifyProjectileSpread() {
            return modifyProjectileSpread;
        }
        public float getModifyFirstShotSpread() {
            return modifyFirstShotSpread;
        }
        public float getModifyHipFireSpread() {
            return modifyHipFireSpread;
        }
        public float getModifyProjectileLife() {
            return modifyProjectileLife;
        }
        public float getRecoilModifier() {
            return recoilModifier;
        }
        public float getHorizontalRecoilModifier() {
            return horizontalRecoilModifier;
        }
        public double getModifyAimDownSightSpeed() {
            return modifyAimDownSightSpeed;
        }
        public int getAdditionalAmmunition() {
            return additionalAmmunition;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            for (Perk<?> perk : Perks.perkList){
                perk.write(nbt,this);
            }
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            for (Perk<?> perk : Perks.perkList){
                perk.read(nbt,this);
            }
        }
    }
    private General general = new General();

    public General getGeneral() {
        return general;
    }

    @Nullable
    public List<ResourceLocation> getSuitableGuns() {
        return items;
    }

    public boolean canApplyOn(TimelessGunItem item){
        if(item.getRegistryName()!=null){
            if(items.isEmpty() && tags.isEmpty())return true;
            else {
                if (items.contains(item.getRegistryName())) {
                    return true;
                }
                return tags.stream().anyMatch(item::isIn);
            }
        }
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("id",id.toString());
        if(skin!=null){
            nbt.putString("model", skin.toString());
        }
        if(canApplyOn!=null){
            ListNBT listNBT = new ListNBT();
            for(String s : canApplyOn){
                listNBT.add(StringNBT.valueOf(s));
            }
            nbt.put("canApplyOn",listNBT);
        }
        if(extraTooltip!=null){
            ListNBT listNBT = new ListNBT();
            for(String s : extraTooltip){
                listNBT.add(StringNBT.valueOf(s));
            }
            nbt.put("extraTooltip",listNBT);
        }
        nbt.put("General", this.general.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(nbt.contains("id")){
            this.id = ResourceLocation.tryCreate(nbt.getString("id"));
        }
        if(nbt.contains("model")){
            this.skin = ResourceLocation.tryCreate(nbt.getString("model"));
        }
        if(nbt.contains("canApplyOn",Constants.NBT.TAG_LIST)){
            ListNBT listNBT = nbt.getList("canApplyOn",Constants.NBT.TAG_STRING);
            if(!listNBT.isEmpty()){
                this.canApplyOn = new ArrayList<>();
                listNBT.forEach((s)->{
                    canApplyOn.add(s.getString());
                });
            }
        }
        if(nbt.contains("extraTooltip",Constants.NBT.TAG_LIST)){
            ListNBT listNBT = nbt.getList("extraTooltip",Constants.NBT.TAG_STRING);
            if(!listNBT.isEmpty()){
                this.extraTooltip = new ArrayList<>();
                listNBT.forEach((s)->{
                    extraTooltip.add(s.getString());
                });
            }
        }
        if (nbt.contains("General", Constants.NBT.TAG_COMPOUND)) {
            this.general.deserializeNBT(nbt.getCompound("General"));
        }
        this.init();
    }

}