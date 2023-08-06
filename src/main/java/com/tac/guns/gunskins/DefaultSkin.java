package com.tac.guns.gunskins;

import com.tac.guns.client.SpecialModel;

import java.util.HashMap;
import java.util.Map;

public class DefaultSkin extends GunSkin{
    public DefaultSkin(String gun) {
        super("default",gun);
    }
    public void loadDefault(SkinLoader loader){
        Map<String,String> defaultModels = new HashMap<>();
        defaultModels.put("auto","tac:special/"+loader.getGun());
        defaultModels.put("icon","tac:textures/icon/guns/"+loader.getGun()+".png");
        defaultModels.put("mini_icon","tac:textures/icon/guns/mini/"+loader.getGun()+".png");
        loader.load(this,defaultModels);
    }

    @Override
    public SpecialModel getModel(ModelComponent component){
        return models.get(component);
    }
}
