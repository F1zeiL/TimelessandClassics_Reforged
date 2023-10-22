package com.tac.guns.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.annotation.Validator;
import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * Synchronize custom modifier defined by data pack.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class AttachmentManager extends ReloadListener<Map<ResourceLocation, CustomModifier>> {
    private static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResourceLocation.class, JsonDeserializers.RESOURCE_LOCATION);
        return builder.create();
    });

    private static AttachmentManager instance;

    public static AttachmentManager getInstance() {
        return instance;
    }

    public IGunModifier CUSTOM = new IGunModifier() {
        @Override
        public String additionalSkin() {
            return IGunModifier.super.additionalSkin();
        }
    };

    private static Map<ResourceLocation, CustomModifier> infoMap = new HashMap<>();

    public static CustomModifier getCustomModifier(ResourceLocation location){
        if(infoMap!=null){
            return infoMap.get(location);
        }
        return null;
    }

    public static Map<ResourceLocation, CustomModifier> getCustomModifiers(){
        if(infoMap!=null){
            return infoMap;
        }
        return null;
    }

    @Override
    protected Map<ResourceLocation, CustomModifier> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ResourceLocation, CustomModifier> map = new HashMap<>();
        resourceManagerIn.getAllResourceLocations("attachments/skin",(s)-> s.endsWith(".json"))
                .forEach((resourceLocation)->{
                    try {
                        resourceManagerIn.getAllResources(resourceLocation).forEach((resource)->{
                            try(InputStream is = resource.getInputStream();
                                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
                            {
                                CustomModifier skin = JSONUtils.fromJson(GSON_INSTANCE,reader, CustomModifier.class);
                                if (skin != null && Validator.isValidObject(skin)) {
                                    map.put(skin.getId(),skin);
                                }
                            } catch(InvalidObjectException e) {
                                GunMod.LOGGER.error("Missing required properties for {}", resourceLocation);
                            } catch(IOException e) {
                                GunMod.LOGGER.error("Couldn't parse data file {}", resourceLocation);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (IOException e) {
                        GunMod.LOGGER.error("Couldn't parse data file {}", resourceLocation);
                    }
                });
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, CustomModifier> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        infoMap = objectIn;
    }

    public void writeAttachments(PacketBuffer buffer){
        buffer.writeVarInt(infoMap.size());
        infoMap.forEach((id,skinInfo)->{
            buffer.writeResourceLocation(id);
            buffer.writeCompoundTag(skinInfo.serializeNBT());
        });
    }

    public static ImmutableMap<ResourceLocation, CustomModifier> readAttachments(PacketBuffer buffer)
    {
        int size = buffer.readVarInt();
        if(size > 0)
        {
            ImmutableMap.Builder<ResourceLocation, CustomModifier> builder = ImmutableMap.builder();
            for(int i = 0; i < size; i++)
            {
                ResourceLocation id = buffer.readResourceLocation();
                CustomModifier info = new CustomModifier();
                CompoundNBT nbt = buffer.readCompoundTag();
                if(nbt!=null){
                    info.deserializeNBT(nbt);
                    builder.put(id, info);
                }
            }
            return builder.build();
        }
        return ImmutableMap.of();
    }

    public interface IAttachmentsProvider{
        ImmutableMap<ResourceLocation, CustomModifier> getAttachments();
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean updateCustomAttachments(IAttachmentsProvider message) {
        infoMap.clear();
        message.getAttachments().forEach((k,v)->{
            //todo: maybe need to do some check here?
            infoMap.put(k,v);
        });
        return true;
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        instance = new AttachmentManager();
        event.addListener(instance);
    }
}
