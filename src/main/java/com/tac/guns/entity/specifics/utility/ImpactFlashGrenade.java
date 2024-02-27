package com.tac.guns.entity.specifics.utility;

import com.tac.guns.Config;
import com.tac.guns.entity.ThrowableStunGrenadeEntity;
import com.tac.guns.init.ModEffects;
import com.tac.guns.init.ModItems;
import com.tac.guns.init.ModSounds;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageStunGrenade;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class ImpactFlashGrenade extends ThrowableStunGrenadeEntity {
    public ImpactFlashGrenade(World world, LivingEntity player, int maxCookTime) {
        super(world, player, maxCookTime);
        this.setShouldBounce(false);
        this.setItem(new ItemStack(ModItems.STANDARD_FLASH_GRENADE.get()));
        this.setGravityVelocity(0.01F);
    }
    @Override
    protected boolean calculateAndApplyEffect(Effect effect, Config.EffectCriteria criteria, LivingEntity entity, Vector3d grenade, Vector3d eyes, double distance, double angle)
    {
        double angleMax = criteria.angleEffect.get() * 0.5;
        //TODO: Apply a more cohesive config so other grenade types can be customized, for example impact flash should have a shorter time of flash and smaller radius
        if(distance <= criteria.radius.get()/1.25f && angleMax > 0 && angle <= angleMax)
        {
            // Verify that light can pass through all blocks obstructing the entity's line of sight to the grenade
            if(effect != ModEffects.BLINDED.get() || !Config.SERVER.stunGrenades.blind.criteria.raytraceOpaqueBlocks.get() || rayTraceOpaqueBlocks(this.world, eyes, grenade, false, false, false) == null)
            {
                // Duration attenuated by distance
                int durationBlinded = (int) Math.round(criteria.durationMax.get() - (criteria.durationMax.get() - criteria.durationMin.get()) * (distance / criteria.radius.get()));

                // Duration further attenuated by angle
                durationBlinded *= 1 - (angle * (1 - criteria.angleAttenuationMax.get())) / angleMax;

                entity.addPotionEffect(new EffectInstance(effect, durationBlinded/2, 0, false, false));

                return !(entity instanceof PlayerEntity);
            }
        }
        return false;
    }
}
