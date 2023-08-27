package com.tac.guns.item.TransitionalTypes.grenades.utility;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.specifics.utility.StandardFlashGrenade;
import com.tac.guns.entity.specifics.utility.StandardStunGrenade;
import com.tac.guns.init.ModSounds;
import com.tac.guns.item.GrenadeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class StunGrenadeItem extends FlashGrenadeItem
{
    public StunGrenadeItem(Properties properties, int maxCookTime, float speed)
    {
        super(properties, maxCookTime,  speed);
    }

    @Override
    public ThrowableGrenadeEntity create(World world, LivingEntity entity, int timeLeft)
    {
        return new StandardStunGrenade(world, entity, super.maxCookTime);
    }

    @Override
    public boolean canCook()
    {
        return false;
    }
}
