package com.tac.guns.item.TransitionalTypes.grenades.utility;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.specifics.utility.StandardFlashGrenade;
import com.tac.guns.init.ModSounds;
import com.tac.guns.item.GrenadeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class FlashGrenadeItem extends GrenadeItem
{
    public FlashGrenadeItem(Item.Properties properties, int maxCookTime, float speed)
    {
        super(properties, maxCookTime, 0, 1, speed);
    }

    @Override
    public ThrowableGrenadeEntity create(World world, LivingEntity entity, int timeLeft)
    {
        return new StandardFlashGrenade(world, entity, super.maxCookTime);
    }

    @Override
    public boolean canCook()
    {
        return true;
    }

    @Override
    protected void onThrown(World world, ThrowableGrenadeEntity entity)
    {
        world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), ModSounds.ITEM_GRENADE_PIN.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
