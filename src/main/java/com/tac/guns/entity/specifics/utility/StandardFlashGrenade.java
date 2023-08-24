package com.tac.guns.entity.specifics.utility;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.ThrowableStunGrenadeEntity;
import com.tac.guns.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class StandardFlashGrenade extends ThrowableStunGrenadeEntity {

    public StandardFlashGrenade(World world, LivingEntity player, int maxCookTime) {
        super(world, player, maxCookTime);
        this.setItem(new ItemStack(ModItems.STANDARD_FLASH_GRENADE.get()));
        this.setShouldBounce(true);
        this.setGravityVelocity(0.03F);
    }
}
