package com.tac.guns.network.message;

import com.tac.guns.client.toast.GunLevelUpToast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SGunLevelUp implements IMessage{
    private ItemStack gun;
    private int level;
    public SGunLevelUp(){}

    public SGunLevelUp(ItemStack gun,int level){
        this.gun = gun;
        this.level = level;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(gun);
        buffer.writeInt(level);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.gun = buffer.readItemStack();
        this.level = buffer.readInt();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            PlayerEntity player = Minecraft.getInstance().player;
            if(player==null) return;
            if (level == 5 || level == 8){
                Minecraft.getInstance().getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT,
                        new TranslationTextComponent("toast.tac.damage_up"),null));
            } else if (level == 10){
                Minecraft.getInstance().getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT,
                        new TranslationTextComponent("toast.tac.final_level"),null));
            } else{
                Minecraft.getInstance().getToastGui().add(new GunLevelUpToast(gun,
                        new TranslationTextComponent("toast.tac.level_up"),
                        new TranslationTextComponent("toast.tac.sub.level_up")));
            }
            player.getEntityWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.75F, 1.0F);

        });
        supplier.get().setPacketHandled(true);
    }
}
