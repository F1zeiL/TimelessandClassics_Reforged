package com.tac.guns.client.audio;

import com.tac.guns.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ReloadingSound extends EntityTickableSound {
    public ReloadingSound(SoundEvent sound, SoundCategory category, Entity entity) {
        super(sound, category, entity);
    }
    private float flatVolume = 0;
    @Override
    public void tick(){
        super.tick();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            //TODO: Make distance customizable, should still use ease like gunshots except with custom range. Would be nice to block sound if passing through blocks but not understood yet*
            float distance = 24.0F;
            this.flatVolume = 1.0F - Math.min(1.0F, (float) Math.sqrt(player.getDistanceSq(super.x, super.y, super.z)) / distance);
            this.volume *= this.flatVolume;
        }
    }
}
