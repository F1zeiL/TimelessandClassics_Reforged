package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;

/**
 * An attachment class related to under barrels. Use {@link #create(IGunModifier...)} to create an
 * get.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class IrDevice extends Attachment
{
    private IrDevice(IGunModifier... modifier)
    {
        super(modifier);
    }
    private IrDevice(ResourceLocation modifier)
    {
        super(modifier);
    }

    /**
     * Creates an under barrel get
     *
     * @param modifier an array of gun modifiers
     * @return an under barrel get
     */
    public static IrDevice create(IGunModifier... modifier)
    {
        return new IrDevice(modifier);
    }
    public static IrDevice create(ResourceLocation modifier)
    {
        return new IrDevice(modifier);
    }
}
