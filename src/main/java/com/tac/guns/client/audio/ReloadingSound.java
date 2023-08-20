package com.tac.guns.client.audio;

import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ReloadingSound extends EntityTickableSound {
    //TODO: Find wether MC already has an easier way to call localized tickable sounds, or simply do it ourselves using a dist algo and Override tick()
    public ReloadingSound(SoundEvent sound, SoundCategory category, Entity entity) {
        super(sound, category, entity);
    }
    @Override
    public void tick(){
        super.tick();
    }
}
