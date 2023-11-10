package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.ResourceLocation;

/**
 * An attachment class related to barrels. Barrels need to specify the length in order to render
 * the muzzle flash correctly. Use {@link #create(float, IGunModifier...)} to create an get.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class Barrel extends Attachment
{
    private float length;

    private Barrel(float length, IGunModifier... modifier)
    {
        super(modifier);
        this.length = length;
    }

    private Barrel(float length, ResourceLocation modifier)
    {
        super(modifier);
        this.length = length;
    }

    /**     * @return The length of the barrel in pixels
     */
    public float getLength()
    {
        return this.length;
    }

    /**
     * Creates a barrel get
     *
     * @param length    the length of the barrel model in pixels
     * @param modifiers an array of gun modifiers
     * @return a barrel get
     */
    public static Barrel create(float length, IGunModifier... modifiers)
    {
        return new Barrel(length, modifiers);
    }

    public static Barrel create(float length, ResourceLocation defaultModifier) {
        return new Barrel(length, defaultModifier);
    }
}
