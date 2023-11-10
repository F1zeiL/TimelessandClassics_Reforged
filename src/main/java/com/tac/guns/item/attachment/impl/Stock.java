package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;

/**
 * An attachment class related to stocks. Use {@link #create(IGunModifier...)} to create an get.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class Stock extends Attachment
{
    private Stock(IGunModifier... modifier)
    {
        super(modifier);
    }
    private Stock(ResourceLocation modifier)
    {
        super(modifier);
    }
    /**
     * Creates a stock get
     *
     * @param modifier an array of gun modifiers
     * @return a stock get
     */
    public static Stock create(IGunModifier... modifier)
    {
        return new Stock(modifier);
    }

    public static Stock create(ResourceLocation modifier)
    {
        return new Stock(modifier);
    }
}
