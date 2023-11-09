package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;

/**
 * An attachment class related to under barrels. Use {@link #create(IGunModifier...)} to create an
 * get.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class UnderBarrel extends Attachment
{
    private UnderBarrel(IGunModifier... modifier)
    {
        super(modifier);
    }
    private UnderBarrel(ResourceLocation modifier)
    {
        super(modifier);
    }
    /**
     * Creates an under barrel get
     *
     * @param modifier an array of gun modifiers
     * @return an under barrel get
     */
    public static UnderBarrel create(IGunModifier... modifier)
    {
        return new UnderBarrel(modifier);
    }
    public static UnderBarrel create(ResourceLocation modifier)
    {
        return new UnderBarrel(modifier);
    }
}
