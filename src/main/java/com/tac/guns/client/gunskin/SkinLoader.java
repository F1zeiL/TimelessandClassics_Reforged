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
    AA_12(ModItems.AA_12, BODY, BOLT, BOLT_HANDLE, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, MAG_DRUM,
            MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, RAIL_EXTENDED, RAIL_SCOPE,
            SIGHT, SIGHT_LIGHT),

    AI_AWP(ModItems.AI_AWP, BODY, BOLT, BOLT_EXTRA, BULLET_SHELL, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR,
            MUZZLE_SILENCER, SIGHT, SIGHT_LIGHT),

    AK47(ModItems.AK47, BODY, BOLT, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER, RAIL_SCOPE,
            STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    CZ75(ModItems.CZ75, BODY, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER, RAIL_SCOPE,
            SLIDE),

    CZ75_AUTO(ModItems.CZ75_AUTO, BODY, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER, SLIDE),

    DB_SHORT(ModItems.DB_SHORT, BODY, BARREL, BULLET1, BULLET2, HAMMER),

    DEAGLE_357(ModItems.DEAGLE_357, BODY, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER,
            SIGHT_LIGHT, SLIDE, SLIDE_LIGHT),

    DP28(ModItems.DP28, BODY, BOLT, MAG),

    FN_FAL(ModItems.FN_FAL, BODY, BARREL_EXTENDED, BARREL_STANDARD, BOLT, BULLET, GRIP_LIGHT, GRIP_TACTICAL, HAND_GUARD_EXTENDED,
            HAND_GUARD_STANDARD, HANDLE, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD,
            RAIL_SCOPE, SIGHT_LIGHT, STOCK_DEFAULT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    GLOCK_17(ModItems.GLOCK_17, BODY, BULLET, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER,
            SLIDE, SLIDE_LIGHT),

    GLOCK_18(ModItems.GLOCK_18, BODY, BULLET, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER,
            SLIDE, SLIDE_LIGHT),

    HK_G3(ModItems.HK_G3, BODY, BOLT, BULLET, GRIP_LIGHT, GRIP_TACTICAL, HANDLE, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED,
            MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, PULL, RAIL_DEFAULT, RAIL_EXTENDED,
            RAIL_SCOPE, SIGHT_LIGHT, STOCK_DEFAULT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    HK_MP5A5(ModItems.HK_MP5A5, BODY, BOLT, BOLT_HANDLE, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED,
            MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER, RAIL_DEFAULT, RAIL_EXTENDED, RAIL_SCOPE, SIGHT_LIGHT,
            STOCK_DEFAULT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    HK416_A5(ModItems.HK416_A5, BODY, BOLT, BULLET, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR,
            LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER,
            SIGHT, SIGHT_FOLDED, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    M1A1_SMG(ModItems.M1A1_SMG, BODY, BOLT, BULLET, MAG_DRUM, MAG_STANDARD),

    M4(ModItems.M4, BODY, BOLT, BOLT_HANDLE, CARRY, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR,
            LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER,
            RAIL_COVER_SIDE, RAIL_COVER_TOP, RAIL_DEFAULT, RAIL_EXTENDED, RAIL_EXTENDED_SIDE, RAIL_EXTENDED_TOP, SIGHT,
            SIGHT_LIGHT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    M16A4(ModItems.M16A4, BODY, BOLT, CARRY, GRIP_LIGHT, GRIP_TACTICAL, HANDLE, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR,
            LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER,
            RAIL_DEFAULT, RAIL_EXTENDED, SIGHT_LIGHT),

    M24(ModItems.M24, BODY, BOLT, BULLET_HEAD, BULLET_SHELL, MAG_EXTENDED, MAG_STANDARD, SIGHT, SIGHT_LIGHT),

    M60(ModItems.M60, BODY, BOLT, BULLET_CHAIN, CAP, HANDLE, MAG, SIGHT, SIGHT_FOLDED),

    M82A2(ModItems.M82A2, BODY, BARREL, BOLT, BULLET, HANDLE, MAG_EXTENDED, MAG_STANDARD, SIGHT, SIGHT_FOLDED, SIGHT_LIGHT),

    M92FS(ModItems.M92FS, BODY, BULLET, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER, SLIDE,
            SLIDE_LIGHT),

    M249(ModItems.M249, BODY, BOLT, BULLET_CHAIN, BULLET_CHAIN_COVER, CAP, GRIP_LIGHT, GRIP_TACTICAL, HANDLE, MAG, SIGHT_LIGHT),

    M870_CLASSIC(ModItems.M870_CLASSIC, BODY, BULLET, MAG_EXTENDED, PUMP),

    M1014(ModItems.M1014, BODY, BOLT, BULLET, BULLET_SHELL, SIGHT_LIGHT),

    M1911(ModItems.M1911, BODY, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER, SLIDE),

    MICRO_UZI(ModItems.MICRO_UZI, BODY, BOLT, BULLET, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER, SIGHT, STOCK_DEFAULT,
            STOCK_FOLDED),

    MK14(ModItems.MK14, BODY, BOLT, BOLT_HANDLE, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR,
            LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, RAIL_SCOPE, SIGHT_LIGHT),

    MK18_MOD1(ModItems.MK18_MOD1, BODY, BOLT, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR,
            LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER,
            SIGHT, SIGHT_FOLDED, SIGHT_FOLDED_LIGHT, SIGHT_LIGHT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    MK23(ModItems.MK23, BODY, BULLET, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER, SLIDE,
            SLIDE_LIGHT),

    MK47(ModItems.MK47, BODY, BOLT, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD,
            MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, PULL, SIGHT, SIGHT_FOLDED, SIGHT_FOLDED_LIGHT,
            SIGHT_LIGHT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    MP7(ModItems.MP7, BODY, BOLT, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD,
            MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, SIGHT, SIGHT_LIGHT),

    MP9(ModItems.MP9, BODY, BOLT, BULLET, HANDLE, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED, MAG_STANDARD, STOCK_DEFAULT,
            STOCK_FOLDED),

    MRAD(ModItems.MRAD, BODY, BARREL, BIPOD, BOLT, BOLT_EXTRA, BULLET, BULLET_SHELL, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC,
            LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, SIGHT, SIGHT_FOLDED, SIGHT_FOLDED_LIGHT,
            SIGHT_LIGHT),

    QBZ_95(ModItems.QBZ_95, BODY, BOLT, BULLET, MAG_DRUM, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT,
            MUZZLE_SILENCER, SIGHT_LIGHT),

    QBZ_191(ModItems.QBZ_191, BODY, BOLT, BULLET1, BULLET2, GRIP_LIGHT, GRIP_TACTICAL, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE,
            MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, RELEASE, SIGHT, SIGHT_FOLDED, SIGHT_LIGHT),

    RPG7(ModItems.RPG7, BODY, ROCKET),

    RPK(ModItems.RPK, BODY, BOLT, MAG_EXTENDED, MAG_STANDARD, RAIL_SCOPE, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    SCAR_H(ModItems.SCAR_H, BODY, BOLT, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE,
            MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, SIGHT, SIGHT_FOLDED,
            SIGHT_FOLDED_LIGHT, SIGHT_LIGHT),

    SCAR_L(ModItems.SCAR_L, BODY, BOLT, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE,
            MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, SIGHT, SIGHT_FOLDED,
            SIGHT_FOLDED_LIGHT, SIGHT_LIGHT),

    SCAR_MK20(ModItems.SCAR_MK20, BODY, BOLT, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE,
            MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, SIGHT, SIGHT_FOLDED,
            SIGHT_FOLDED_LIGHT, SIGHT_LIGHT),

    SIG_MCX_SPEAR(ModItems.SIG_MCX_SPEAR, BODY, BOLT, BULLET, GRIP_LIGHT, GRIP_TACTICAL, HANDLE, HANDLE_EXTRA, LASER_BASIC,
            LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE, MAG, SIGHT, SIGHT_LIGHT),

    SKS_TACTICAL(ModItems.SKS_TACTICAL, BODY, BOLT, GRIP_LIGHT, GRIP_TACTICAL, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR,
            LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, RAIL_SCOPE, SIGHT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    SPR_15(ModItems.SPR_15, BODY, BOLT, GRIP_LIGHT, GRIP_TACTICAL, HANDLE, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED,
            MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, RAIL_COVER_UNDER, SIGHT, SIGHT_FOLDED,
            SIGHT_FOLDED_LIGHT, SIGHT_LIGHT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    STI2011(ModItems.STI2011, BODY, BULLET1, BULLET2, HAMMER, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED, MAG_STANDARD,
            MUZZLE_SILENCER, SLIDE, SLIDE_LIGHT),

    TEC_9(ModItems.TEC_9, BODY, BOLT, BULLET, MAG_EXTENDED, MAG_STANDARD),

    TIMELESS_50(ModItems.TIMELESS_50, BODY, BARREL_EXTENDED, BARREL_STANDARD, BULLET1, BULLET2, CLUMSYYY, HAMMER, MAG_EXTENDED,
            MAG_STANDARD, MUZZLE_SILENCER, NEKOOO, SLIDE, SLIDE_EXTENDED, SLIDE_EXTENDED_LIGHT, SLIDE_LIGHT),

    TTI_G34(ModItems.TTI_G34, BODY, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER, SLIDE,
            SLIDE_LIGHT),

    TYPE81_X(ModItems.TYPE81_X, BODY, BOLT, MAG_EXTENDED, MAG_STANDARD, RAIL_SCOPE),

    UDP_9(ModItems.UDP_9, BODY, BOLT, BULLET, GRIP_LIGHT, GRIP_TACTICAL, HANDLE, LASER_BASIC, LASER_BASIC_DEVICE, MAG_EXTENDED,
            MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER, RAIL_COVER_SIDE, RAIL_COVER_UNDER,
            SIGHT, SIGHT_FOLDED, SIGHT_FOLDED_LIGHT, SIGHT_LIGHT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL),

    UZI(ModItems.UZI, BODY, BOLT, BULLET, HANDLE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_SILENCER, SIGHT_LIGHT, STOCK_DEFAULT,
        STOCK_FOLDED),

    VECTOR45(ModItems.VECTOR45, BODY, BODY_LIGHT, BOLT, GRIP_LIGHT, GRIP_TACTICAL, HANDLE, LASER_BASIC, LASER_BASIC_DEVICE,
            LASER_IR, LASER_IR_DEVICE, MAG_EXTENDED, MAG_STANDARD, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER, SIGHT,
            SIGHT_LIGHT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL)
    //, GRIP_LIGHT, GRIP_TACTICAL
    //, LASER_BASIC, LASER_BASIC_DEVICE, LASER_IR, LASER_IR_DEVICE
    //, MAG_EXTENDED, MAG_STANDARD
    //, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_DEFAULT, MUZZLE_SILENCER
    //, MUZZLE_BRAKE, MUZZLE_COMPENSATOR, MUZZLE_SILENCER
    //, STOCK_DEFAULT, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL
    //, STOCK_HEAVY, STOCK_LIGHT, STOCK_TACTICAL
    //, SIGHT, SIGHT_FOLDED, SIGHT_FOLDED_LIGHT, SIGHT_LIGHT
    ;

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
                ResourceLocation componentLoc = component.getModelLocation(skinName.getNamespace()+
                        ":gunskin/generated/"+this.name+"_"+skinName.getPath());
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
