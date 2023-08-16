package com.tac.guns.client.gunskin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.tac.guns.client.SpecialModel;
import com.tac.guns.init.ModItems;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tac.guns.client.gunskin.ModelComponent.*;

public enum SkinLoader {
    AI_AWP(ModItems.AI_AWP, BODY, BOLT, BOLT_EXTRA, SIGHT, SIGHT_LIGHT, MUZZLE_SILENCER, MUZZLE_COMPENSATOR, MUZZLE_BRAKE,
            MAG_EXTENDED, MAG_STANDARD, BULLET_SHELL),
    AK47(ModItems.AK47, BODY, MOUNT, BOLT, STOCK_LIGHT, STOCK_TACTICAL, STOCK_HEAVY, MUZZLE_SILENCER, MUZZLE_BRAKE,
            MAG_EXTENDED, MAG_STANDARD),
    DEAGLE_357(ModItems.DEAGLE_357, BODY, SLIDE, SLIDE_LIGHT, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER,
            MAG_STANDARD, MAG_EXTENDED, SIGHT_LIGHT),
    SCAR_L(ModItems.SCAR_L, BODY, BOLT, SIGHT, SIGHT_LIGHT, SIGHT_FOLDED, SIGHT_FOLDED_LIGHT, GRIP_LIGHT, GRIP_TACTICAL,
            LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE, MUZZLE_DEFAULT, MUZZLE_BRAKE, MUZZLE_COMPENSATOR,
            MUZZLE_SILENCER, MAG_STANDARD, MAG_EXTENDED),
    UDP_9(ModItems.UDP_9, BODY, BOLT, SIGHT, SIGHT_LIGHT, SIGHT_FOLDED, SIGHT_FOLDED_LIGHT, GRIP_LIGHT, GRIP_TACTICAL,
            LASER_BASIC, LASER_BASIC_DEVICE, MUZZLE_DEFAULT, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER, MAG_STANDARD,
            MAG_EXTENDED, BARREL_EXTENDED, HANDLE, STOCK_LIGHT, STOCK_TACTICAL, STOCK_HEAVY, RAIL_COVER, BULLET);
//    QBZ95(ModItems.QBZ_95, BODY, BOLT, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER,
//            MUZZLE_DEFAULT, MAG_STANDARD, MAG_DRUM),
//    HK416(ModItems.HK416_A5, BODY, BOLT, BULLET, SIGHT, SIGHT_FOLDED, LASER_BASIC, LASER_BASIC_DEVICE,
//            LASER_IR, LASER_IR_DEVICE, STOCK_LIGHT, STOCK_TACTICAL, STOCK_HEAVY,
//            MUZZLE_DEFAULT, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER,
//            MAG_STANDARD, MAG_EXTENDED, GRIP_LIGHT, GRIP_TACTICAL),
//    MP9(ModItems.MP9, BODY, LASER_BASIC, LASER_BASIC_DEVICE, STOCK_DEFAULT, STOCK_ANY,
//            MAG_EXTENDED, MAG_STANDARD, HANDLE, BOLT, BULLET),

    private final static Map<String, SkinLoader> byName = new HashMap<>();
    private DefaultSkin defaultSkin;
    public static IUnbakedModel missingModel;
    public static Map<ResourceLocation, IUnbakedModel> unbakedModels;
    public static Map<ResourceLocation, IUnbakedModel> topUnbakedModels;

    public List<ModelComponent> getComponents() {
        return components;
    }

    private final List<ModelComponent> components;
    private final String name;

    static {
        for (SkinLoader skinLoader : SkinLoader.values()) {
            byName.put(skinLoader.name, skinLoader);
        }
    }

    SkinLoader(String name, ModelComponent... components) {
        this.components = Arrays.asList(components);
        this.name = name;
    }

    SkinLoader(RegistryObject<?> item, ModelComponent... components) {
        this(item.getId().getPath(), components);
    }

    public static SkinLoader getSkinLoader(String name) {
        return byName.get(name);
    }

    public String getGun() {
        return name;
    }

    public DefaultSkin loadDefaultSkin() {
        DefaultSkin skin = new DefaultSkin(this.name);
        String mainLoc = "tac:special/" + getGun();
        for (ModelComponent key : this.components) {
            tryLoadComponent(skin, mainLoc, key);
        }
        this.defaultSkin = skin;
        return skin;
    }

    /**
     * Should be called during model loading.<br>
     * Try to load a gun skin with unique models, then add them into bake queue.<br>
     * If there exist the key 'auto', it will attempt to load all model components that comply with the default naming format.
     *
     * @return the skin, or return null if the default skin is null.
     */
    public GunSkin loadCustomSkin(ResourceLocation skinName, Map<String, String> models) {
        if (defaultSkin == null) return null;

        GunSkin skin = new GunSkin(skinName, getGun());
        skin.setDefaultSkin(this.defaultSkin);

        if (models.containsKey("auto")) {
            String main = models.get("auto");
            for (ModelComponent key : this.components) {
                tryLoadComponent(skin, main, key);
            }
        } else {
            for (ModelComponent key : this.components) {
                tryLoadComponent(skin, models, key);
            }
        }
        return skin;
    }

    private static void tryLoadComponent(GunSkin skin, Map<String, String> models, ModelComponent component) {
        if (models.containsKey(component.key)) {
            ResourceLocation loc = ResourceLocation.tryCreate(models.get(component.key));
            if (loc != null) {
                SpecialModel mainModel = new SpecialModel(loc);
                ModelLoader.addSpecialModel(loc);
                skin.addComponent(component, mainModel);
            }
        }
    }

    private static void tryLoadComponent(GunSkin skin, String mainLocation, ModelComponent component) {
        ResourceLocation loc = component.getModelLocation(mainLocation);
        if (loc != null) {
            ResourceLocation test = new ResourceLocation(loc.getNamespace(), "models/" + loc.getPath() + ".json");
            if (Minecraft.getInstance().getResourceManager().hasResource(test)) {
                SpecialModel mainModel = new SpecialModel(loc);
                ModelLoader.addSpecialModel(loc);
                skin.addComponent(component, mainModel);
            }
        }
    }

    /**
     * Should be called during model loading.<br>
     * Try to load a gun skin without unique models from given textures, then add them into bake queue.
     *
     * @return the skin, or return null if the default skin is null.
     * @see net.minecraft.client.renderer.model.ModelBakery
     */
    public GunSkin loadTextureOnlySkin(ResourceLocation skinName, List<Pair<String, ResourceLocation>> textures) {
        if (defaultSkin == null) return null;

        GunSkin skin = new GunSkin(skinName, this.getGun());
        skin.setDefaultSkin(this.defaultSkin);
        //create unbaked models for every component of this gun.
        for (ModelComponent component : this.components) {
            ResourceLocation parent = component.getModelLocation("tac:special/" + this.name);
            TextureModel model = TextureModel.tryCreateCopy(parent);
            if (model != null) {
                model.applyTextures(textures);
                ResourceLocation componentLoc = component.getModelLocation(skinName);
                skin.addComponent(component, new SpecialModel(componentLoc));

                unbakedModels.put(componentLoc, model.getModel());
                topUnbakedModels.put(componentLoc, model.getModel());
            }
        }
        return skin;
    }

    public static class TextureModel {
        public static final ResourceLocation atlasLocation = new ResourceLocation("minecraft:textures/atlas/blocks.png");
        private final BlockModel unbaked;

        private TextureModel(BlockModel model) {
            this.unbaked = model;
        }

        public static TextureModel tryCreateCopy(ResourceLocation parentLocation) {
            TextureModel textureModel = null;
            if (ModelLoader.instance() != null) {
                BlockModel parent = (BlockModel) ModelLoader.instance().getUnbakedModel(parentLocation);

                if (parent == missingModel) return null;

                List<BlockPart> list = Lists.newArrayList();
                Map<String, Either<RenderMaterial, String>> map = Maps.newHashMap();

                BlockModel model = new BlockModel(parentLocation, list, map,
                        true, null, parent.getAllTransforms(), parent.getOverrides());

                textureModel = new TextureModel(model);
            }
            return textureModel;
        }

        public void applyTextures(List<Pair<String, ResourceLocation>> textures) {
            textures.forEach((p) -> applyTexture(p.getKey(), p.getValue()));
        }

        public void applyTexture(String key, ResourceLocation textureLocation) {
            this.unbaked.textures.put(key, Either.left(new RenderMaterial(atlasLocation, textureLocation)));
        }

        public BlockModel getModel() {
            return unbaked;
        }
    }
}
