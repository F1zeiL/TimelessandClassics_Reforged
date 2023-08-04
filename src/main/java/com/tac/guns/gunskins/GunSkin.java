package com.tac.guns.gunskins;

import com.tac.guns.client.SpecialModel;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GunSkin{
    private final Map<ModelComponent, SpecialModel> models = new HashMap<>();
    public final String skinName;
    public final ResourceLocation registerName;
    private ResourceLocation icon;
    private ResourceLocation miniIcon;

    public GunSkin(String skinName){
        this.skinName = skinName;
        this.registerName = ResourceLocation.tryCreate("tac:"+skinName);
    }

    public GunSkin(ResourceLocation registerName){
        this.skinName = "test";
        this.registerName = registerName;
    }

    public SpecialModel getModel(ModelComponent component){
        return models.get(component);
    }

    protected void addComponent(ModelComponent component,SpecialModel model){
        this.models.put(component, model);
    }

    public Map<ModelComponent,SpecialModel> getModels(){
        return this.models;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
    }

    public ResourceLocation getMiniIcon() {
        return miniIcon;
    }

    public void setMiniIcon(ResourceLocation miniIcon) {
        this.miniIcon = miniIcon;
    }
}
