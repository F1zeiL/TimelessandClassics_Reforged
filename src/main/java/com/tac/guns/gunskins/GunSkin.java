package com.tac.guns.gunskins;

import com.tac.guns.client.SpecialModel;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GunSkin {
    protected final Map<ModelComponent, SpecialModel> models = new HashMap<>();
    public final ResourceLocation registerName;
    public final String gun;
    private ResourceLocation icon;
    private ResourceLocation miniIcon;

    public GunSkin(String skinName,String gun){
        this.registerName = ResourceLocation.tryCreate("tac:"+skinName);
        this.gun=gun;
    }

    public GunSkin(ResourceLocation registerName,String gun){
        this.gun=gun;
        this.registerName = registerName;
    }

    public SpecialModel getModel(ModelComponent component){
        return models.getOrDefault(component,SkinManager.getDefaultSkin(gun).getModel(component));
    }

    protected void addComponent(ModelComponent component,SpecialModel model){
        this.models.put(component, model);
    }

    public Map<ModelComponent,SpecialModel> getModels(){
        return this.models;
    }

    public ResourceLocation getIcon() {
        if(icon!=null)return icon;
        else return SkinManager.getDefaultSkin(gun).getIcon();
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
    }

    public ResourceLocation getMiniIcon() {
        if(miniIcon!=null)return miniIcon;
        else return SkinManager.getDefaultSkin(gun).getMiniIcon();
    }

    public void setMiniIcon(ResourceLocation miniIcon) {
        this.miniIcon = miniIcon;
    }

    public void cleanCache(){
        for(SpecialModel model : models.values()){
            model.cleanCache();
        }
    }
}
