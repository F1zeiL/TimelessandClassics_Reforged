package com.tac.guns.entity.specifics.utility;

import com.tac.guns.Config;
import com.tac.guns.entity.ThrowableStunGrenadeEntity;
import com.tac.guns.init.ModEffects;
import com.tac.guns.init.ModItems;
import com.tac.guns.init.ModSounds;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageStunGrenade;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class StandardStunGrenade extends ThrowableStunGrenadeEntity {

    public StandardStunGrenade(World world, LivingEntity player, int maxCookTime) {
        super(world, player, maxCookTime);
        this.setItem(new ItemStack(ModItems.STANDARD_FLASH_GRENADE.get()));
        this.setShouldBounce(true);
        this.setGravityVelocity(0.03F);
    }
    @Override
    protected Effect getBlindType() {
        return ModEffects.STUNNED.get();
    }
    @Override
    protected boolean canSuppOpac() {
        return false;
    }
}
