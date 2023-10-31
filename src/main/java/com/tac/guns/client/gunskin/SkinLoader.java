package com.tac.guns.client.gunskin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.tac.guns.client.SpecialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinLoader {
    public final static Map<ResourceLocation, SkinLoader> skinLoaders = new HashMap<>();
    private DefaultSkin defaultSkin;
    public static IUnbakedModel missingModel;
    public static Map<ResourceLocation, IUnbakedModel> unbakedModels;
    public static Map<ResourceLocation, IUnbakedModel> topUnbakedModels;
    private final List<IModelComponent> components;
    private final ResourceLocation name;
    public SkinLoader(ResourceLocation name, IModelComponent... components) {
        this.components = Arrays.asList(components);
        this.name = name;
    }

    public SkinLoader(RegistryObject<?> item, IModelComponent... components) {
        this(item.getId(), components);
    }

    public static void register(ResourceLocation name, SkinLoader loader){
        skinLoaders.put(name,loader);
    }

    public List<IModelComponent> getComponents() {
        return components;
    }
    public static SkinLoader getSkinLoader(String name) {
        ResourceLocation rl = null;
        if(name.indexOf(':')>=0){
                        rl = ResourceLocation.tryCreate(name);
        }else{
            rl = ResourceLocation.tryCreate("tac:"+name);
        }
        if(rl==null)return null;
        return skinLoaders.get(rl);
    }

    public static SkinLoader getSkinLoader(ResourceLocation name) {
        return skinLoaders.get(name);
    }

    public ResourceLocation getGun() {
        return name;
    }

    public DefaultSkin loadDefaultSkin() {
        DefaultSkin skin = new DefaultSkin(this.name);
        String mainLoc = this.name.getNamespace()+ ":special/" + getGun().getPath();
        for (IModelComponent key : this.components) {
            tryLoadComponent(skin, mainLoc, key);
        }
        this.defaultSkin = skin;

        ResourceLocation iconLoc = ResourceLocation.tryCreate(this.name.getNamespace()+":textures/gui/icon/"+this.name.getPath()+".png");
        if(iconLoc!=null && Minecraft.getInstance().getResourceManager().hasResource(iconLoc)){
            skin.setIcon(iconLoc);
        }

        ResourceLocation miniIconLoc = ResourceLocation.tryCreate(this.name.getNamespace()+":textures/gui/icon/mini/"+this.name.getPath()+".png");
        if(iconLoc!=null && Minecraft.getInstance().getResourceManager().hasResource(iconLoc)){
            skin.setMiniIcon(miniIconLoc);
        }

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
            for (IModelComponent key : this.components) {
                tryLoadComponent(skin, main, key);
            }
        } else {
            for (IModelComponent key : this.components) {
                tryLoadComponent(skin, models, key);
            }
        }
        return skin;
    }

    public boolean loadSkinIcon(@Nonnull GunSkin skin, @Nonnull ResourceLocation iconLocation){
        if(Minecraft.getInstance().getResourceManager().hasResource(iconLocation)){
            skin.setIcon(iconLocation);
            return true;
        }else{
            ResourceLocation tl = ResourceLocation.tryCreate(iconLocation.getNamespace()+":textures/"+iconLocation.getPath()+".png");
            if(tl !=null && Minecraft.getInstance().getResourceManager().hasResource(tl)){
                skin.setIcon(tl);
                return true;
            }else {
                return false;
            }
        }
    }

    public boolean loadSkinMiniIcon(@Nonnull GunSkin skin, @Nonnull ResourceLocation iconLocation){
        if(Minecraft.getInstance().getResourceManager().hasResource(iconLocation)){
            skin.setIcon(iconLocation);
            return true;
        }else{
            ResourceLocation tl = ResourceLocation.tryCreate(iconLocation.getNamespace()+":textures/"+iconLocation.getPath()+".png");
            if(tl !=null && Minecraft.getInstance().getResourceManager().hasResource(tl)){
                skin.setMiniIcon(tl);
                return true;
            }else {
                return false;
            }
        }
    }

    private static void tryLoadComponent(GunSkin skin, Map<String, String> models, IModelComponent component) {
        if (models.containsKey(component.getKey())) {
            ResourceLocation loc = ResourceLocation.tryCreate(models.get(component.getKey()));
            if (loc != null) {
                SpecialModel mainModel = new SpecialModel(loc);
                ModelLoader.addSpecialModel(loc);
                skin.addComponent(component, mainModel);
            }
        }
    }

    private static void tryLoadComponent(GunSkin skin, String mainLocation, IModelComponent component) {
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
        for (IModelComponent component : this.components) {
            ResourceLocation parent = component.getModelLocation(this.name.getNamespace()+ ":special/" + this.name.getPath());
            TextureModel model = TextureModel.tryCreateCopy(parent);
            if (model != null) {
                model.applyTextures(textures);
                ResourceLocation componentLoc = component.getModelLocation(skinName.getNamespace()+
                        ":gunskin/generated/"+this.name.getNamespace()+this.name.getPath()+"_"+skinName.getPath());
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
            textures.forEach((p) -> applyTexture(p.getFirst(), p.getSecond()));
        }

        public void applyTexture(String key, ResourceLocation textureLocation) {
            this.unbaked.textures.put(key, Either.left(new RenderMaterial(atlasLocation, textureLocation)));
        }

        public BlockModel getModel() {
            return unbaked;
        }
    }
}
