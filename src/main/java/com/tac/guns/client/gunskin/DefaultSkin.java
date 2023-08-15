package com.tac.guns.client.gunskin;

import com.tac.guns.client.SpecialModel;

import java.util.HashMap;
import java.util.Map;

public class DefaultSkin extends GunSkin{
    public DefaultSkin(String gun) {
        super("default",gun,null);
    }

    @Override
    public SpecialModel getModel(ModelComponent component){
        return models.get(component);
    }
}
