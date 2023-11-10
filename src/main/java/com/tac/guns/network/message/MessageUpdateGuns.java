package com.tac.guns.network.message;

import com.google.common.collect.ImmutableMap;
import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.common.CustomGun;
import com.tac.guns.common.CustomGunLoader;
import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.attachments.CustomModifierData;
import com.tac.guns.common.attachments.NetworkModifierManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageUpdateGuns implements IMessage, NetworkGunManager.IGunProvider, NetworkModifierManager.ICustomModifiersProvider
{
    private ImmutableMap<ResourceLocation, Gun> registeredGuns;
    private ImmutableMap<ResourceLocation, CustomGun> customGuns;
    private ImmutableMap<ResourceLocation, CustomModifierData> customAttachments;

    public MessageUpdateGuns() {}

    @Override
    public void encode(PacketBuffer buffer)
    {
        Validate.notNull(NetworkGunManager.get());
        Validate.notNull(CustomGunLoader.get());
        NetworkGunManager.get().writeRegisteredGuns(buffer);
        CustomGunLoader.get().writeCustomGuns(buffer);
        NetworkModifierManager.getInstance().writeAttachments(buffer);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.registeredGuns = NetworkGunManager.readRegisteredGuns(buffer);
        this.customGuns = CustomGunLoader.readCustomGuns(buffer);
        this.customAttachments = NetworkModifierManager.readModifiers(buffer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleUpdateGuns(this));
        supplier.get().setPacketHandled(true);
    }

    @Override
    public ImmutableMap<ResourceLocation, Gun> getRegisteredGuns()
    {
        return this.registeredGuns;
    }

    public ImmutableMap<ResourceLocation, CustomGun> getCustomGuns()
    {
        return this.customGuns;
    }

    @Override
    @Nullable
    public ImmutableMap<ResourceLocation, CustomModifierData> getCustomModifiers() {
        return this.customAttachments;
    }
}
