package com.tac.guns.gunskins;

import com.tac.guns.client.SpecialModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;
import java.util.Map;

public class GunSkin {
    protected final Map<ModelComponent, SpecialModel> models = new HashMap<>();
    protected Map<ModelComponent, Vector3d> extraOffset;
    public final ResourceLocation registerName;
    public final String gun;
    private ResourceLocation icon;
    private ResourceLocation miniIcon;
    private final DefaultSkin defaultSkin;

    public GunSkin(String skinName,String gun,DefaultSkin skin){
        this.registerName = ResourceLocation.tryCreate("tac:"+skinName);
        this.gun=gun;
        this.defaultSkin=skin;
    }

    public GunSkin(ResourceLocation registerName,String gun,DefaultSkin skin){
        this.gun=gun;
        this.registerName = registerName;
        this.defaultSkin=skin;
    }

    public SpecialModel getModel(ModelComponent component){
        return models.getOrDefault(component,defaultSkin.getModel(component));
    }

    protected void addComponent(ModelComponent component,SpecialModel model){
        this.models.put(component, model);
    }

    public Map<ModelComponent,SpecialModel> getModels(){
        return this.models;
    }

    public ResourceLocation getIcon() {
        if(icon!=null)return icon;
        else return defaultSkin.getIcon();
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
    }

    public ResourceLocation getMiniIcon() {
        if(miniIcon!=null)return miniIcon;
        else return defaultSkin.getMiniIcon();
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
