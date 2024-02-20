package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;

public class AmmoPlug extends Attachment {
    private AmmoPlug(IGunModifier... modifier)
    {
        super(modifier);
    }

    private AmmoPlug(ResourceLocation modifier)
    {
        super(modifier);
    }

    public static AmmoPlug create(IGunModifier... modifier)
    {
        return new AmmoPlug(modifier);
    }

    public static AmmoPlug create(ResourceLocation modifier)
    {
        return new AmmoPlug(modifier);
    }
}