package com.tac.guns.client.audio;

import com.tac.guns.Config;
import com.tac.guns.common.Gun;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BarrelWhineSound extends EntityTickableSound
{
    private float configVolume;
    public BarrelWhineSound(SoundEvent sound, SoundCategory category, Entity entity, float volume)
    {
        super(sound, category, entity);
        this.repeat = true;
        this.repeatDelay = 0;
        this.configVolume = volume;
    }

    @Override
    public void tick()
    {
        super.tick();
        ClientPlayerEntity player = Minecraft.getInstance().player;
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
        this.volume = this.configVolume * (1.0F - Math.min(1.0F, (float) Math.sqrt(player.getDistanceSq(x, y, z)) / distance));
        this.volume *= this.volume;
    }
}