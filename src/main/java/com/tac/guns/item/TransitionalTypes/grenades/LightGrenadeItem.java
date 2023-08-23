package com.tac.guns.item.TransitionalTypes.grenades;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.specifics.LightGrenadeEntity;
import com.tac.guns.item.GrenadeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class LightGrenadeItem extends GrenadeItem
{
    private float power;
    public LightGrenadeItem(Item.Properties properties, int maxCookTime, float power, float speed)
    {
        super(properties, maxCookTime, power, speed);
        this.power = power;
    }

    public ThrowableGrenadeEntity create(World world, LivingEntity entity, int timeLeft)
    {
        return new LightGrenadeEntity(world, entity, timeLeft, this.power);
    }

    public boolean canCook()
    {
        return true;
    }

    protected void onThrown(World world, ThrowableGrenadeEntity entity)
    {
    }
}
