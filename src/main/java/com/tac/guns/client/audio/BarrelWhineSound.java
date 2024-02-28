package com.tac.guns.client.audio;

import com.tac.guns.Config;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEffects;
import com.tac.guns.init.ModSounds;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BarrelWhineSound extends EntityTickableSound
{
    public BarrelWhineSound(SoundEvent sound, SoundCategory category, Entity entity)
    {
        super(sound, category, entity);
        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public void tick()
    {
        super.tick();
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null || !player.isAlive() || !(player.getHeldItemMainhand().getItem() instanceof TimelessGunItem)) {
            this.finishPlaying();
            return;
        }

        ItemStack stack = player.getHeldItemMainhand();
        Gun gun = ((TimelessGunItem) stack.getItem()).getModifiedGun(stack);
        if (!gun.getReloads().isBarrel()) {
            this.finishPlaying();
            return;
        }

        float distance = Config.SERVER.gunShotMaxDistance.get().floatValue();
        this.volume = Config.SERVER.barrelVolume.get().floatValue() * (1.0F - Math.min(1.0F, (float) Math.sqrt(player.getDistanceSq(x, y, z)) / distance));
        this.volume *= this.volume;
    }
}