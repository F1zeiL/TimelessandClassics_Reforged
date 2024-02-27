package com.tac.guns.effect;

import com.tac.guns.Config;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class IgniteEffect extends Effect {
    public static final UUID IGNITE_UUID = UUID.fromString("2024221-1935-1145-1419-11451452121");
    public static final DamageSource burned = new DamageSource("tac_burned").setDamageBypassesArmor().setFireDamage();
    public IgniteEffect(EffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
        addAttributesModifier(Attributes.MOVEMENT_SPEED, IgniteEffect.IGNITE_UUID.toString(), -Config.SERVER.gameplay.decelerationFromTacIgnite.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier){
        if(!entity.isImmuneToFire()){
            entity.attackEntityFrom(burned, amplifier);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier){
        return duration % 10 == 0;
    }
}