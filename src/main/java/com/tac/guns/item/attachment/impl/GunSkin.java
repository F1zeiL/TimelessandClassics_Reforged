package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;

public class GunSkin extends Attachment {
    public ResourceLocation getSkin() {
        return skin;
    }

    private ResourceLocation skin;


    private GunSkin(ResourceLocation modifier) {
        super(modifier);
    }
    private GunSkin(String skin) {
        super((ResourceLocation) null);
        this.skin = ResourceLocation.tryCreate("tac:"+skin);
    }

    public static GunSkin create(ResourceLocation modifier) {
        return new GunSkin(modifier);
    }

    public static GunSkin create(String skin) {
        return new GunSkin(skin);
    }
}
