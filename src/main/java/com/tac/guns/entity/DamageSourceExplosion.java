package com.tac.guns.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class DamageSourceExplosion extends EntityDamageSource {
    private ResourceLocation item;
    public DamageSourceExplosion(@Nullable Entity damageSourceEntityIn,ResourceLocation item) {
        super("explosion.player", damageSourceEntityIn);
        this.item = item;
    }
    public ResourceLocation getItem() {
        return item;
    }
}
