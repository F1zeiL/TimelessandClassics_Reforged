package com.tac.guns.mixin.client;

import com.tac.guns.event.ClientSetSprintingEvent;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author Arcomit
 * @updateDate 2023/7/27
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @ModifyVariable(at = @At("HEAD"),method = "setSprinting")
    public boolean setSprinting(boolean sprinting){
        ClientSetSprintingEvent event = new ClientSetSprintingEvent(sprinting);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);//Send an event when the player's sprint state changes
        return event.getSprinting();
    }
}
