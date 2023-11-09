package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;

public class GunSkin extends Attachment {
    private GunSkin(IGunModifier... modifier) {
        super(modifier);
    }
    private GunSkin(ResourceLocation modifier) {
        super(modifier);
    }

    public static GunSkin create(IGunModifier... modifier) {
        return new GunSkin(modifier);
    }
    public static GunSkin create(ResourceLocation modifier) {
        return new GunSkin(modifier);
    }
}
