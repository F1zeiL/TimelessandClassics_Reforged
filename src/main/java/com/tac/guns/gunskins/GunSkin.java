package com.tac.guns.gunskins;

import com.tac.guns.client.SpecialModel;

import java.util.HashMap;
import java.util.Map;

public class GunSkin{
    private final Map<ModelComponent, SpecialModel> models = new HashMap<>();
    public final String skinName;

    public GunSkin(String skinName){
        this.skinName = skinName;
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
}
