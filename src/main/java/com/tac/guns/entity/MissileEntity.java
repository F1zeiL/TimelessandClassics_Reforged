package com.tac.guns.entity;

import com.tac.guns.Config;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MissileEntity extends ProjectileEntity implements IExplosionProvider
{
    private float power;
    private float radius;
    public MissileEntity(EntityType<? extends ProjectileEntity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public MissileEntity(EntityType<? extends ProjectileEntity> entityType, World worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun,0,0);
        this.power = modifiedGun.getProjectile().getBlastDamage();
        this.radius = modifiedGun.getProjectile().getBlastRadius();
    }

    @Override
    protected void onProjectileTick()
    {
        if (this.world.isRemote)
        {
            for (int i = 5; i > 0; i--)
            {
                this.world.addParticle(ParticleTypes.CLOUD, true, this.getPosX() - (this.getMotion().getX() / i), this.getPosY() - (this.getMotion().getY() / i), this.getPosZ() - (this.getMotion().getZ() / i), 0, 0, 0);
            }
            if (this.world.rand.nextInt(2) == 0)
            {
                this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
                this.world.addParticle(ParticleTypes.FLAME, true, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected void onHitEntity(Entity entity, Vector3d hitVec, Vector3d startVec, Vector3d endVec, boolean headshot)
    {
        super.onHitEntity(entity, hitVec, startVec, endVec, false);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, Vector3d hitVec)
    {
        createExplosion(this, this.power, this.radius * Config.SERVER.missiles.explosionRadius.get().floatValue(), null);
        this.life = 0;
    }

    @Override
    public void onExpired()
    {
        createExplosion(this, this.power, this.radius * Config.SERVER.missiles.explosionRadius.get().floatValue(), null);
    }

    @Override
    public DamageSourceExplosion createDamageSource(){
        ResourceLocation item = getWeapon().getItem().getRegistryName();
        return new DamageSourceExplosion(shooter,item);
    }
}
