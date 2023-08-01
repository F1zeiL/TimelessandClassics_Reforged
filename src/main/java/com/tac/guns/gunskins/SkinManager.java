package com.tac.guns.gunskins;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkinManager {
    private static Map<String, Map<String,GunSkin>> skins = new HashMap<>();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerModels(ModelRegistryEvent event)
    {
        init();
    }

    public static void reload(){
        skins = new HashMap<>();
        init();
    }

    public static void init(){
        ResourceLocation loc = new ResourceLocation(Reference.MOD_ID,"models/gunskin/skin.json");
        try {
//            List<IResource> all = Minecraft.getInstance().getResourceManager().getAllResources(loc);
//            for (IResource resource :all) {
//                loadSkinList(resource);
//            }
            loadSkinList(Minecraft.getInstance().getResourceManager().getResource(loc));
        } catch (IOException e) {
            GunMod.LOGGER.warn("Skins load failed!");
        }
    }

    private static void loadSkinList(IResource resource) {
        JsonObject json;
        InputStream stream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        json = new JsonParser().parse(reader).getAsJsonObject();

        for (Map.Entry<String, JsonElement> e : json.entrySet()) {
            String gun = e.getKey();
            SkinLoader loader = SkinLoader.getSkinLoader(gun);
            if(loader==null)continue;
            JsonObject skins = e.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> skin : skins.entrySet()) {
                try{
                    String skinName = skin.getKey();
                    JsonObject modelPath = skin.getValue().getAsJsonObject();
                    Map<String,String> components = new HashMap<>();
                    for (Map.Entry<String, JsonElement> c : modelPath.entrySet()) {
                        components.put(c.getKey(),c.getValue().getAsString());
                    }
                    register(loader,skinName,components);
                    GunMod.LOGGER.info("Load gun skin of "+gun+" named "+skinName);
                }catch (IllegalStateException e2){
                    GunMod.LOGGER.warn("Failed to load skins of "+gun+"!");
                }
            }
        }
    }

    private static void register(SkinLoader loader, String skinName, Map<String, String> models){
        GunSkin skin = new GunSkin(skinName);

        loader.load(skin,models);

        skins.putIfAbsent(loader.getGun(),new HashMap<>());
        skins.get(loader.getGun()).put(skinName,skin);
    }

    public static GunSkin getSkin(String gun,String skinName){
        if(skins.containsKey(gun)){
            return skins.get(gun).get(skinName);
        }else return null;
    }
}
