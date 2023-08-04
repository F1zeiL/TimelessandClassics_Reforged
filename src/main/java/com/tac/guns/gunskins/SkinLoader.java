package com.tac.guns.gunskins;

import com.tac.guns.client.SpecialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tac.guns.gunskins.ModelComponent.*;

public enum SkinLoader {
    AK47("ak47",BODY,MOUNT,BOLT,STOCK_LIGHT,STOCK_TACTICAL,
                STOCK_HEAVY,MUZZLE_SILENCER,MUZZLE_BRAKE,MAG_EXTENDED,MAG_STANDARD),
    QBZ95("qbz95",BODY,BOLT,MUZZLE_BRAKE,MUZZLE_COMPENSATOR,MUZZLE_SILENCER,
            MUZZLE_DEFAULT,MAG_STANDARD,MAG_DRUM),
    DEAGLE357("deagle357",BODY,SLIDE,MUZZLE_BRAKE,MUZZLE_COMPENSATOR,MUZZLE_SILENCER,
            MAG_STANDARD,MAG_EXTENDED),
    AIAWP("ai_awp",BODY,BOLT,BOLT_EXTRA,SIGHT,MUZZLE_SILENCER,MUZZLE_COMPENSATOR,
            MUZZLE_BRAKE, MAG_EXTENDED,MAG_STANDARD,BULLET_SHELL),
    HK416("hk416",BODY,BOLT,BULLET,SIGHT,SIGHT_FOLDED,LASER_BASIC,LASER_BASIC_DEVICE,
            LASER_IR,LASER_IR_DEVICE,STOCK_LIGHT,STOCK_TACTICAL,STOCK_HEAVY,
            MUZZLE_DEFAULT,MUZZLE_BRAKE,MUZZLE_COMPENSATOR,MUZZLE_SILENCER,
            MAG_STANDARD,MAG_EXTENDED,GRIP_LIGHT,GRIP_TACTICAL),
    MP9("mp9",BODY,LASER_BASIC,LASER_BASIC_DEVICE,STOCK_DEFAULT,STOCK_ANY,
            MAG_EXTENDED,MAG_STANDARD,HANDLE,BOLT,BULLET),
    SCARL("scar_l",BODY,BOLT,SIGHT,SIGHT_FOLDED,GRIP_LIGHT,GRIP_TACTICAL,
            LASER_BASIC,LASER_BASIC_DEVICE,LASER_IR,LASER_IR_DEVICE,
            MUZZLE_DEFAULT,MUZZLE_BRAKE,MUZZLE_COMPENSATOR,MUZZLE_SILENCER,
            MAG_STANDARD,MAG_EXTENDED)
    ;
    private final static Map<String,SkinLoader> byName = new HashMap<>();
    private final List<ModelComponent> components;
    private final String name;

    static {
        for (SkinLoader skinLoader : SkinLoader.values()) {
            byName.put(skinLoader.name, skinLoader);
        }
    }

    SkinLoader(String name,ModelComponent... components){
        this.components = Arrays.asList(components);
        this.name=name;
    }

    public static SkinLoader getSkinLoader(String name){
        return byName.get(name);
    }

    public String getGun() {
        return name;
    }

    public void load(GunSkin skin, Map<String,String> models){
        if(models.containsKey("auto")){
            String main = models.get("auto");
            for (ModelComponent key : this.components) {
                tryLoadComponent(skin,main,key);
            }
        }else{
            for (ModelComponent key : this.components) {
                tryLoadComponent(skin,models,key);
            }
        }
        if(models.containsKey("icon")){
            ResourceLocation loc = ResourceLocation.tryCreate(models.get("icon"));
            skin.setIcon(loc);
        }
        if(models.containsKey("mini_icon")){
            ResourceLocation loc = ResourceLocation.tryCreate(models.get("mini_icon"));
            skin.setMiniIcon(loc);
        }
    }

    private static void tryLoadComponent(GunSkin skin, Map<String,String> models, ModelComponent component){
        if(models.containsKey(component.key)){
            ResourceLocation loc = ResourceLocation.tryCreate(models.get(component.key));
            if(loc!=null){
                SpecialModel mainModel = new SpecialModel(loc);
                ModelLoader.addSpecialModel(loc);
                skin.addComponent(component,mainModel);
            }
        }
    }

    private static void tryLoadComponent(GunSkin skin, String mainLocation, ModelComponent component){
        ResourceLocation loc = ResourceLocation.tryCreate(mainLocation+
                (component==BODY ? "" : "_" + component.key));
        if(loc!=null){
            ResourceLocation test = new ResourceLocation(loc.getNamespace(),"models/"+loc.getPath()+".json");
            if(Minecraft.getInstance().getResourceManager().hasResource(test)){
                SpecialModel mainModel = new SpecialModel(loc);
                ModelLoader.addSpecialModel(loc);
                skin.addComponent(component,mainModel);
            }
        }
    }
}
