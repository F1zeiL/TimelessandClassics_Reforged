package com.tac.guns.item.transition.grenades.utility;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.specifics.utility.ImpactFlashGrenade;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ImpactFlashGrenadeItem extends FlashGrenadeItem
{
    public ImpactFlashGrenadeItem(Item.Properties properties, int maxCookTime, float speed)
    {
        super(properties, maxCookTime, speed);
    }
    @Override
    public ThrowableGrenadeEntity create(World world, LivingEntity entity, int timeLeft)
    {
        return new ImpactFlashGrenade(world, entity, super.maxCookTime);
    }
    @Override
    public boolean canCook()
    {
        return false;
    }
}
