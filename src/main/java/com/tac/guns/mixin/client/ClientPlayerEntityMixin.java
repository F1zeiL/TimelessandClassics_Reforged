package com.tac.guns.mixin.client;

import com.tac.guns.event.ClientSetSprintingEvent;
import com.tac.guns.item.GrenadeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Arcomit
 * @updateDate 2023/7/27
 */
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow public abstract boolean isHandActive();
    @Shadow public MovementInput movementInput;

    @ModifyVariable(at = @At("HEAD"),method = "setSprinting")
    public boolean setSprinting(boolean sprinting){
        ClientSetSprintingEvent event = new ClientSetSprintingEvent(sprinting);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);//Send an event when the player's sprint state changes
        return event.getSprinting();
    }

    @Inject(
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isHandActive()Z",ordinal = 0),
            method = "livingTick"
    )
    public void livingTick(CallbackInfo ci){
        if (Minecraft.getInstance().player != null) {
            if (this.isHandActive() && !Minecraft.getInstance().player.isPassenger()) {
                if(Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof GrenadeItem){
                    movementInput.moveStrafe /= 0.2F;
                    movementInput.moveForward /= 0.2F;
                }
            }
        }
    }
}
