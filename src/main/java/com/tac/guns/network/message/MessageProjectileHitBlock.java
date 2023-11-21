package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageProjectileHitBlock implements IMessage
{
    private double x;
    private double y;
    private double z;
    private BlockPos pos;
    private Direction face;
    private boolean isBlast;

    public MessageProjectileHitBlock() {
    }

    public MessageProjectileHitBlock(double x, double y, double z, BlockPos pos, Direction face, boolean isBlast) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = pos;
        this.face = face;
        this.isBlast = isBlast;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
        buffer.writeBlockPos(this.pos);
        buffer.writeEnumValue(this.face);
        buffer.writeBoolean(this.isBlast);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.pos = buffer.readBlockPos();
        this.face = buffer.readEnumValue(Direction.class);
        this.isBlast = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleProjectileHitBlock(this));
        supplier.get().setPacketHandled(true);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Direction getFace() {
        return this.face;
    }

    public boolean isBlast() {
        return this.isBlast;
    }
}
