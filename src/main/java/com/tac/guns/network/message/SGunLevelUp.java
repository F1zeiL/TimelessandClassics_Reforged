package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SGunLevelUp implements IMessage{
    private ItemStack gun;
    private int level;
    public SGunLevelUp() {}

    public SGunLevelUp(ItemStack gun,int level) {
        this.gun = gun;
        this.level = level;
    }

    public ItemStack getGun() {
        return this.gun;
    }

    public int getLevel() {
        return this.level;
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
            ClientPlayHandler.handleGunLevelUp(this);
        });
        supplier.get().setPacketHandled(true);
    }
}
