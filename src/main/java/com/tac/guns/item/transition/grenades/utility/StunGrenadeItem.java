package com.tac.guns.item.transition.grenades.utility;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.specifics.utility.StandardStunGrenade;
import net.minecraft.entity.LivingEntity;
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
