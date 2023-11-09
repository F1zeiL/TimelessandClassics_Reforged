package com.tac.guns.common.attachments;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.annotation.Validator;
import com.tac.guns.common.JsonDeserializers;
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
public class NetworkModifierManager extends ReloadListener<Map<ResourceLocation, CustomModifierData>> {
    private static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResourceLocation.class, JsonDeserializers.RESOURCE_LOCATION);
        return builder.create();
    });

    private static NetworkModifierManager instance;

    public static NetworkModifierManager getInstance() {
        return instance;
    }

    private static Map<ResourceLocation, CustomModifierData> infoMap = new HashMap<>();

    public static CustomModifierData getCustomModifier(ResourceLocation location){
        if(infoMap!=null){
            return infoMap.get(location);
        }
        return null;
    }

    public static Map<ResourceLocation, CustomModifierData> getCustomModifiers(){
        if(infoMap!=null){
            return infoMap;
        }
        return null;
    }

    @Override
    protected Map<ResourceLocation, CustomModifierData> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ResourceLocation, CustomModifierData> map = new HashMap<>();
        resourceManagerIn.getAllResourceLocations("modifiers/",(s)-> s.endsWith(".json"))
                .forEach((resourceLocation)->{
                    try {
                        resourceManagerIn.getAllResources(resourceLocation).forEach((resource)->{
                            try(InputStream is = resource.getInputStream();
                                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
                            {
                                CustomModifierData skin = JSONUtils.fromJson(GSON_INSTANCE,reader, CustomModifierData.class);
                                if (skin != null && Validator.isValidObject(skin)) {
                                    skin.init();
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
    protected void apply(Map<ResourceLocation, CustomModifierData> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        infoMap = objectIn;
    }

    public void writeAttachments(PacketBuffer buffer){
        buffer.writeVarInt(infoMap.size());
        infoMap.forEach((id,skinInfo)->{
            buffer.writeResourceLocation(id);
            buffer.writeCompoundTag(skinInfo.serializeNBT());
        });
    }

    public static ImmutableMap<ResourceLocation, CustomModifierData> readModifiers(PacketBuffer buffer)
    {
        int size = buffer.readVarInt();
        if(size > 0)
        {
            ImmutableMap.Builder<ResourceLocation, CustomModifierData> builder = ImmutableMap.builder();
            for(int i = 0; i < size; i++)
            {
                ResourceLocation id = buffer.readResourceLocation();
                CustomModifierData info = new CustomModifierData();
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

    public interface ICustomModifiersProvider {
        ImmutableMap<ResourceLocation, CustomModifierData> getCustomModifiers();
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean updateCustomAttachments(ICustomModifiersProvider message) {
        infoMap.clear();
        message.getCustomModifiers().forEach((k, v)->{
            //todo: maybe need to do some check here?
            infoMap.put(k,v);
        });
        return true;
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        instance = new NetworkModifierManager();
        event.addListener(instance);
    }
}
