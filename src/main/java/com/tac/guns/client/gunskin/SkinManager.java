package com.tac.guns.client.gunskin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.util.GunModifierHelper;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class SkinManager {
    private static Map<String, Map<ResourceLocation, GunSkin>> skins = new HashMap<>();
    private static final Map<String, DefaultSkin> defaultSkins = new HashMap<>();

    public static void reload() {
        skins = new HashMap<>();
        init();
    }

    public static void cleanCache() {
        for (GunSkin skin : defaultSkins.values()) {
            skin.cleanCache();
        }
        for (Map<ResourceLocation, GunSkin> map : skins.values()) {
            for (GunSkin skin : map.values()) {
                skin.cleanCache();
            }
        }
    }

    private static void init() {
        //get skin configs from all namespace
        Set<String> nameSpaces = Minecraft.getInstance().getResourceManager().getResourceNamespaces();
        for (String nameSpace : nameSpaces) {
            ResourceLocation loc = new ResourceLocation(nameSpace, "models/gunskin/skin.json");
            try {
                List<IResource> all = Minecraft.getInstance().getResourceManager().getAllResources(loc);
                for (IResource resource : all) {
                    loadSkinList(resource);
                }
            } catch (IOException e) {
                GunMod.LOGGER.warn("Failed to load skins from {} {}", loc, e);
            }
        }
    }

    private static void loadSkinList(IResource resource) throws IOException {
        JsonObject json;
        InputStream stream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        json = new JsonParser().parse(reader).getAsJsonObject();
        String nameSpace = resource.getLocation().getNamespace();

        for (Map.Entry<String, JsonElement> e : json.entrySet()) {
            //gun
            String gun = e.getKey();
            SkinLoader loader = SkinLoader.getSkinLoader(gun);
            if (loader == null) continue;
            JsonObject skins = e.getValue().getAsJsonObject();

            for (Map.Entry<String, JsonElement> s : skins.entrySet()) {
                // skin
                try {
                    String skinName = s.getKey();
                    JsonObject skinObject = s.getValue().getAsJsonObject();

                    String skinType = skinObject.get("type").getAsString();
                    ResourceLocation skinLoc = ResourceLocation.tryCreate(nameSpace + ":" + skinName);

                    if (skinLoc == null) {
                        GunMod.LOGGER.warn("Failed to load skins of {} named {}: invalid name.", gun, skinName);
                        continue;
                    } else if (!defaultSkins.containsKey(gun)) {
                        GunMod.LOGGER.warn("Failed to load skins of {} named {}: default skin no loaded.", gun, skinName);
                        continue;
                    }

                    if ("custom".equals(skinType)) {
                        JsonObject modelObject = skinObject.get("models").getAsJsonObject();

                        Map<String, String> components = new HashMap<>();

                        for (Map.Entry<String, JsonElement> c : modelObject.entrySet()) {
                            components.put(c.getKey(), c.getValue().getAsString());
                        }

                        if (registerCustomSkin(loader, skinLoc, components)) {
                            GunMod.LOGGER.info("Loaded custom gun skin of {} named {}", gun, skinName);
                        }

                    } else if ("texture".equals(skinType)) {
                        JsonObject modelObject = skinObject.get("textures").getAsJsonObject();

                        List<Pair<String, ResourceLocation>> textures = new ArrayList<>();

                        for (Map.Entry<String, JsonElement> c : modelObject.entrySet()) {
                            ResourceLocation tl = ResourceLocation.tryCreate(c.getValue().getAsString());
                            if (tl != null) {
                                textures.add(new Pair<>(c.getKey(), tl));
                            }
                        }

                        if (registerTextureOnlySkin(loader, skinLoc, textures)) {
                            GunMod.LOGGER.info("Loaded texture-only gun skin of {} named {}", gun, skinName);
                        }
                    }else if("common_texture".equals(skinType)){
                        JsonObject modelObject = skinObject.get("textures").getAsJsonObject();

                        List<Pair<String, ResourceLocation>> textures = new ArrayList<>();

                        for (Map.Entry<String, JsonElement> c : modelObject.entrySet()) {
                            ResourceLocation tl = ResourceLocation.tryCreate(c.getValue().getAsString());
                            if (tl != null) {
                                textures.add(new Pair<>(c.getKey(), tl));
                            }
                        }
                        ResourceLocation icon = null;
                        if(skinObject.get("icon")!=null){
                            icon = ResourceLocation.tryCreate(skinObject.get("icon").getAsString());
                        }
                        ResourceLocation miniIcon = null;
                        if(skinObject.get("mini_icon")!=null){
                            miniIcon = ResourceLocation.tryCreate(skinObject.get("mini_icon").getAsString());
                        }

                        int cnt = registerCommonSkins(loader,textures,icon,miniIcon);
                        GunMod.LOGGER.info("Loaded common gun skins of {}, total: {}", gun, cnt);
                        continue;
                    }else {
                        GunMod.LOGGER.warn("Failed to load skins of {} named {}: unknown type.", gun, skinName);
                        continue;
                    }

                    if(skinObject.get("icon")!=null){
                        ResourceLocation rl = ResourceLocation.tryCreate(skinObject.get("icon").getAsString());
                        GunSkin skin = getSkin(gun,skinLoc);
                        if(skin!=null && rl!=null){
                            loader.loadSkinIcon(skin,rl);
                        }
                    }

                    if(skinObject.get("mini_icon")!=null){
                        ResourceLocation rl = ResourceLocation.tryCreate(skinObject.get("mini_icon").getAsString());
                        GunSkin skin = getSkin(gun,skinLoc);
                        if(skin!=null && rl!=null){
                            loader.loadSkinMiniIcon(skin,rl);
                        }
                    }

                } catch (Exception e2) {
                    GunMod.LOGGER.warn("Failed to load skins from {} {}.", resource.getLocation(), e2);
                }
            }
        }
        reader.close();
        stream.close();
    }

    public static void loadDefaultSkins() {
        for (SkinLoader loader : SkinLoader.values()) {
            DefaultSkin skin = loader.loadDefaultSkin();
            defaultSkins.put(loader.getGun(), skin);
        }
    }

    /**
     * Try to load preset dyed skins for a gun.
     */
    private static int registerCommonSkins(SkinLoader loader, List<Pair<String, ResourceLocation>> textures,
                                           ResourceLocation icon,ResourceLocation mini_icon){
        String[] skinList = {
                "black", "blue", "brown", "dark_blue", "dark_green",
                "gray", "green", "jade", "light_gray", "magenta",
                "orange", "pink", "purple", "red", "sand", "white"
        };
        int cnt = 0;
        for(String color : skinList){
            ResourceLocation rl = new ResourceLocation("tac:"+color);
            List<Pair<String, ResourceLocation>> skinTextures =
                    textures.stream().map(
                            (p)-> new Pair<>(p.getKey(),ResourceLocation.tryCreate(p.getValue()+"_"+color))
                    ).collect(Collectors.toList());
            if(registerTextureOnlySkin(loader,rl,skinTextures)){
                cnt++;
                GunSkin gunSkin = getSkin(loader.getGun(),rl);
                if(gunSkin!=null){
                    if(icon!=null){
                        loader.loadSkinIcon(gunSkin,icon);
                    }
                    if(mini_icon!=null){
                        loader.loadSkinMiniIcon(gunSkin,mini_icon);
                    }
                }

            }
        }
        return cnt;
    }

    private static boolean registerCustomSkin(SkinLoader loader, ResourceLocation skinLocation, Map<String, String> models) {
        GunSkin skin = loader.loadCustomSkin(skinLocation, models);

        if (skin != null) {
            skins.putIfAbsent(loader.getGun(), new HashMap<>());
            skins.get(loader.getGun()).put(skinLocation, skin);
            return true;
        } else return false;
    }

    private static boolean registerTextureOnlySkin(SkinLoader loader, ResourceLocation skinLocation, List<Pair<String, ResourceLocation>> textures) {
        for(Pair<String, ResourceLocation> p : textures){
            ResourceLocation tl = ResourceLocation.tryCreate(p.getValue().getNamespace()+":textures/"+p.getValue().getPath()+".png");
            if(tl == null || !Minecraft.getInstance().getResourceManager().hasResource(tl)) {
                return false;
            }
        }
        GunSkin skin = loader.loadTextureOnlySkin(skinLocation, textures);

        if (skin != null) {
            skins.putIfAbsent(loader.getGun(), new HashMap<>());
            skins.get(loader.getGun()).put(skinLocation, skin);
            return true;
        } else return false;
    }

    public static GunSkin getSkin(String gun, ResourceLocation skinLocation) {
        if (skinLocation != null && skins.containsKey(gun)) return skins.get(gun).get(skinLocation);
        else return null;
    }

    private static GunSkin getAttachedSkin(ItemStack weapon) {
        if (weapon.getItem() instanceof TimelessGunItem) {
            String gun = weapon.getItem().toString();
            String skin = GunModifierHelper.getAdditionalSkin(weapon).toLowerCase();
//            String[] skinList = {
//                    "ak47.GOLDEN",
//                    "ak47.SILVER",
//
//                    "all.BLACK",
//                    "all.BLUE",
//                    "all.BROWN",
//                    "all.DARK_BLUE",
//                    "all.DARK_GREEN",
//                    "all.GRAY",
//                    "all.GREEN",
//                    "all.JADE",
//                    "all.LIGHT_GRAY",
//                    "all.MAGENTA",
//                    "all.ORANGE",
//                    "all.PINK",
//                    "all.PURPLE",
//                    "all.RED",
//                    "all.SAND",
//                    "all.WHITE"
//            };
//
//            for (String s : skinList) {
//                String[] currentSkin = s.split("\\.");
//                if (currentSkin.length < 2) return null;
//                String resourceName = "tac:" + gun + "_" + currentSkin[1].toLowerCase();
//                if (currentSkin[0].equals("all")) {
//                    if (currentSkin[1].equals(GunModifierHelper.getAdditionalSkin(weapon)))
//                        return getSkin(gun, new ResourceLocation(resourceName));
//                } else {
//                    String skinName = gun + "_" + currentSkin[1];
//                    if (skinName.equals(GunModifierHelper.getAdditionalSkin(weapon)) && gun.equals(currentSkin[0]))
//                        return getSkin(gun, new ResourceLocation(resourceName));
//                }
//            }
            if(!"NONE".equals(skin)){
                ResourceLocation rl = new ResourceLocation("tac:"+skin);
                return getSkin(gun,rl);
            }
        }
        return null;
    }

    public static GunSkin getSkin(ItemStack stack) {
        GunSkin skin = null;
        String gun = stack.getItem().toString();
        if (stack.getTag() != null) {
            if (stack.getTag().contains("Skin", Constants.NBT.TAG_STRING)) {
                String skinLoc = stack.getTag().getString("Skin");
                ResourceLocation loc;
                if (skinLoc.contains(":")) {
                    loc = ResourceLocation.tryCreate(skinLoc);
                } else {
                    loc = new ResourceLocation(Reference.MOD_ID, skinLoc);
                }
                skin = getSkin(gun, loc);
            }
        }
        if (skin == null) {
            skin = getAttachedSkin(stack);
        }
        if (skin == null && defaultSkins.containsKey(gun)) {
            return defaultSkins.get(gun);
        }
        return skin;
    }

    public static DefaultSkin getDefaultSkin(String gun) {
        return defaultSkins.get(gun);
    }
}
