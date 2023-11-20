package com.tac.guns.entity.specifics.explosives;

import com.tac.guns.entity.DamageSourceExplosion;
import com.tac.guns.entity.IExplosionProvider;
import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.ThrowableItemEntity;
import com.tac.guns.init.ModEntities;
import com.tac.guns.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class BaseballGrenadeEntity extends ThrowableGrenadeEntity implements IExplosionProvider
{
    public BaseballGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public BaseballGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World world, LivingEntity entity)
    {
        super(entityType, world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.055F);
        this.setItem(new ItemStack(ModItems.BASEBALL_GRENADE.get()));
        //this.setMaxLife(20 * 2);
    }

    public BaseballGrenadeEntity(World world, LivingEntity entity, int timeLeft, float power, float radius)
    {
        super(ModEntities.THROWABLE_GRENADE.get(), world, entity);
        this.power = power;
        this.radius = radius;
        this.setShouldBounce(true);
        this.setGravityVelocity(0.0425F);
        this.setItem(new ItemStack(ModItems.BASEBALL_GRENADE.get()));
        this.setMaxLife(timeLeft);
    }

    @Override
    public void tick()
    {
        super.tick();
        this.prevRotation = this.rotation;
        double speed = this.getMotion().length();
        if (speed > 0.085)
        {
            this.rotation += speed * 235;
        }
        if (this.world.isRemote)
        {
            this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY() + 0.25, this.getPosZ(), 0, 0.075, 0);
        }
    }
    @Override
    public DamageSourceExplosion createDamageSource(){
        return new DamageSourceExplosion(func_234616_v_(),ModItems.BASEBALL_GRENADE.getId());
    }
}
