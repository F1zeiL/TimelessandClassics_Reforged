package com.tac.guns.client.gunskin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.util.GunModifierHelper;
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
    private static Map<ResourceLocation, Map<ResourceLocation, GunSkin>> skins = new HashMap<>();
    private static final Map<ResourceLocation, DefaultSkin> defaultSkins = new HashMap<>();

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
                GunMod.LOGGER.debug("Failed to load skins from {} {}", loc, e);
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
                    }

                    if (!defaultSkins.containsKey(loader.getGun())) {
                        GunMod.LOGGER.warn("Failed to load skins of {} named {}: default skin no loaded.", gun, skinName);
                        continue;
                    }

                    if(skinType == null){
                        GunMod.LOGGER.warn("Failed to load skins of {} named {}: unknown type.", gun, skinName);
                        continue;
                    }

                    switch (skinType) {
                        case "custom":
                            parseCustom(skinObject, loader, skinLoc, gun, skinName);
                            break;
                        case "texture":
                            parseTextureOnly(skinObject, loader, skinLoc, gun, skinName);
                            break;
                        case "dye":
                            parseDye(skinObject, loader);
                            continue;
                        default:
                            GunMod.LOGGER.warn("Failed to load skins of {} named {}: unknown type.", gun, skinName);
                            continue;
                    }

                    if(skinObject.get("icon")!=null){
                        ResourceLocation rl = ResourceLocation.tryCreate(skinObject.get("icon").getAsString());
                        GunSkin skin = getSkin(loader.getGun(),skinLoc);
                        if(skin!=null && rl!=null){
                            loader.loadSkinIcon(skin,rl);
                        }
                    }

                    if(skinObject.get("mini_icon")!=null){
                        ResourceLocation rl = ResourceLocation.tryCreate(skinObject.get("mini_icon").getAsString());
                        GunSkin skin = getSkin(loader.getGun(),skinLoc);
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

    private static void parseDye(JsonObject skinObject, SkinLoader loader) {
        JsonObject colors = skinObject.get("colors").getAsJsonObject();

        int cnt = 0;

        for(DyeSkin.PresetType preset : DyeSkin.PresetType.values()){
            JsonElement color = colors.get(preset.name());

            if(color == null)continue;

            if(color.isJsonArray()){
                JsonArray array = color.getAsJsonArray();
                int l = array.size();
                int[] c = new int[l];
                for (int i = 0; i < l; i++) {
                    c[i] = array.get(i).getAsInt();
                }

                DyeSkin skin = loader.loadDyeSkin(preset, c);

                if (skin != null) {
                    cnt += 1;
                    skins.putIfAbsent(loader.getGun(), new HashMap<>());
                    skins.get(loader.getGun()).put(preset.getSkinLocation(), skin);
                }
            }
        }

        GunMod.LOGGER.info("Loaded {} dye gun skins of {}",cnt, loader.getGun());

    }


    private static void parseTextureOnly(JsonObject skinObject, SkinLoader loader, ResourceLocation skinLoc, String gun, String skinName) {
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
    }

    private static void parseCustom(JsonObject skinObject, SkinLoader loader, ResourceLocation skinLoc, String gun, String skinName) {
        JsonObject modelObject = skinObject.get("models").getAsJsonObject();

        Map<String, String> components = new HashMap<>();

        for (Map.Entry<String, JsonElement> c : modelObject.entrySet()) {
            components.put(c.getKey(), c.getValue().getAsString());
        }

        if (registerCustomSkin(loader, skinLoc, components)) {
            GunMod.LOGGER.info("Loaded custom gun skin of {} named {}", gun, skinName);
        }
    }

    public static void loadDefaultSkins() {
        SkinLoaders.init();
        for (SkinLoader loader : SkinLoader.skinLoaders.values()) {
            loadDefaultSkin(loader.getGun(),loader.loadDefaultSkin());
        }
    }

    public static void loadDefaultSkin(ResourceLocation gun, DefaultSkin skin){
        defaultSkins.put(gun,skin);
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
            ResourceLocation tl = ResourceLocation.tryCreate(p.getSecond().getNamespace()+":textures/"+p.getSecond().getPath()+".png");
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
        ResourceLocation rl = ResourceLocation.tryCreate("tac:"+gun);
        if (skinLocation != null && skins.containsKey(rl)) return skins.get(rl).get(skinLocation);
        else return null;
    }

    public static GunSkin getSkin(ResourceLocation gun, ResourceLocation skinLocation) {
        if (skinLocation != null && skins.containsKey(gun)) return skins.get(gun).get(skinLocation);
        else return null;
    }

    private static GunSkin getAttachedSkin(ItemStack weapon) {
        if (weapon.getItem() instanceof TimelessGunItem) {
            ResourceLocation gun = weapon.getItem().getRegistryName();
            ResourceLocation skin = GunModifierHelper.getAdditionalSkin(weapon);
            return getSkin(gun,skin);
        }
        return null;
    }

    public static GunSkin getSkin(ItemStack stack) {
        GunSkin skin = null;
        ResourceLocation gun = stack.getItem().getRegistryName();
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
        ResourceLocation rl = ResourceLocation.tryCreate("tac:"+gun);
        if(rl==null)return null;
        return defaultSkins.get(rl);
    }

    public static DefaultSkin getDefaultSkin(ResourceLocation gun) {
        if(gun==null)return null;
        return defaultSkins.get(gun);
    }
}
