package com.tac.guns.item.transition;


import com.tac.guns.GunMod;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.util.Process;


public class M1GunItem extends TimelessOldRifleGunItem {
    public M1GunItem(Process<Properties> properties, IGunModifier... modifiers)
    {
        super(properties1 -> properties.process(new Properties().maxStackSize(1).group(GunMod.GROUP)), modifiers);
    }

    public M1GunItem() {
        this(properties -> properties);
    }
}