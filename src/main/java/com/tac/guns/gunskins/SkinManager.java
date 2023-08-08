package com.tac.guns.gunskins;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkinManager {
    private static Map<String, Map<ResourceLocation, GunSkin>> skins = new HashMap<>();
    private static final Map<String, DefaultSkin> defaultSkins = new HashMap<>();

    public static void reload(){
        skins = new HashMap<>();
        init();
    }

    public static void cleanCache(){
        for(GunSkin skin : defaultSkins.values()){
            skin.cleanCache();
        }
        for(Map<ResourceLocation, GunSkin> map : skins.values()){
            for(GunSkin skin : map.values()){
                skin.cleanCache();
            }
        }
    }

    private static void init(){
        Set<String> nameSpaces = Minecraft.getInstance().getResourceManager().getResourceNamespaces();
        for(String nameSpace : nameSpaces){
            ResourceLocation loc = new ResourceLocation(nameSpace,"models/gunskin/skin.json");
            try {
                List<IResource> all = Minecraft.getInstance().getResourceManager().getAllResources(loc);
                for (IResource resource :all) {
                    loadSkinList(resource);
                }
//            loadSkinList(Minecraft.getInstance().getResourceManager().getResource(loc));
            } catch (IOException e) {
                GunMod.LOGGER.warn("Failed to load skins from {} {}",loc,e);
            }
        }
    }

    private static void loadSkinList(IResource resource) {
        JsonObject json;
        InputStream stream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        json = new JsonParser().parse(reader).getAsJsonObject();
        String nameSpace = resource.getLocation().getNamespace();
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

                    ResourceLocation location = ResourceLocation.tryCreate(nameSpace+":"+skinName);
                    if(location==null){
                        GunMod.LOGGER.warn("Failed to load skins of {} named {}: invalid name.",gun,skinName);
                    }else if(!defaultSkins.containsKey(gun)){
                        GunMod.LOGGER.warn("Failed to load skins of {} named {}: default skin no loaded.",gun,skinName);
                    } else {
                        DefaultSkin defaultSkin = defaultSkins.get(gun);
                        registerSkin(loader,location,components,defaultSkin);
                        GunMod.LOGGER.info("Loaded gun skin of {} named {}",gun,skinName);
                    }
                }catch (IllegalStateException e2){
                    GunMod.LOGGER.warn("Failed to load skins from {} {}.",resource.getLocation(),e2);
                }
            }
        }
    }

    private static void registerSkin(SkinLoader loader, ResourceLocation skinLocation, Map<String, String> models,DefaultSkin defaultSkin){
        GunSkin skin = new GunSkin(skinLocation, loader.getGun(),defaultSkin);

        loader.load(skin,models);

        skins.putIfAbsent(loader.getGun(),new HashMap<>());
        skins.get(loader.getGun()).put(skinLocation,skin);
    }

    public static void loadDefaultSkins(){
        for(SkinLoader loader : SkinLoader.values()){
            DefaultSkin skin = new DefaultSkin(loader.getGun());
            skin.loadDefault(loader);
            defaultSkins.put(loader.getGun(), skin);
        }
    }

    public static GunSkin getSkin(String gun, ResourceLocation skinLocation){
        if(skinLocation!=null && skins.containsKey(gun)){
            return skins.get(gun).get(skinLocation);
        }else return null;
    }

    public static GunSkin getSkin(ItemStack stack, String gun) {
        GunSkin skin = null;
        if (stack.getTag() != null) {
            if (stack.getTag().contains("Skin", Constants.NBT.TAG_STRING)) {
                String skinLoc = stack.getTag().getString("Skin");
                ResourceLocation loc;
                if(skinLoc.contains(":")){
                    loc = ResourceLocation.tryCreate(skinLoc);
                }else {
                    loc = new ResourceLocation(Reference.MOD_ID,skinLoc);
                }
                skin = getSkin(gun,loc);
            }
        }
        if(skin==null && defaultSkins.containsKey(gun)){
            return defaultSkins.get(gun);
        }
        return skin;
    }

//    public static GunSkin getSkin(ItemStack stack) {
//        GunSkin skin = null;
//        if (stack.getTag() != null) {
//            if (stack.getTag().contains("Skin", Constants.NBT.TAG_STRING)) {
//                String skinLoc = stack.getTag().getString("Skin");
//                ResourceLocation loc;
//                if(skinLoc.contains(":")){
//                    loc = ResourceLocation.tryCreate(skinLoc);
//                }else {
//                    loc = new ResourceLocation(Reference.MOD_ID,skinLoc);
//                }
//                skin = getSkin(gun,loc);
//            }
//        }
//        if(skin==null && defaultSkins.containsKey(gun)){
//            return defaultSkins.get(gun);
//        }
//        return skin;
//    }

    public static DefaultSkin getDefaultSkin(String gun){
        return defaultSkins.get(gun);
    }
}
