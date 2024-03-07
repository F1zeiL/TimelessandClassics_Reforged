package com.tac.guns.client;

import com.tac.guns.Reference;
import com.tac.guns.client.gunskin.ResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum SpecialModels {
    BULLET_SHELL("bullet_shell"), // Simply for testing fall back, FOR REMOVAL
    FLAME("flame"),

    //Everything from this point on is all scope additions

    MINI_DOT_BASE("optics/mini_dot_base"),
    MICRO_HOLO_BASE("optics/micro_holo_base"),
    LPVO_1_6_FRONT("optics/lpvo_1_6_front"),
    LPVO_1_6("optics/lpvo_1_6"),
    Sx8_FRONT("optics/8x_scope_front"),
    Sx8_BODY("optics/8x_scope");

    /**
     * The location of an item model in the [MOD_ID]/models/special/[NAME] folder
     */
    private ResourceLocation modelLocation;

    /**
     * Determines if the model should be loaded as a special model.
     */
    private boolean specialModel;

    /**
     * Cached model
     */
    @OnlyIn(Dist.CLIENT)
    private IBakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModels(String modelName) {
        this(new ResourceLocation(Reference.MOD_ID, "special/" + modelName), true);
    }

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModels(SpecialModel modelName) {
        this(new ResourceLocation(Reference.MOD_ID, "special/" + modelName), true);
    }

    /**
     * Sets the model's location
     *
     * @param resource     name of the model file
     * @param specialModel if the model is a special model
     */
    SpecialModels(ResourceLocation resource, boolean specialModel) {
        this.modelLocation = resource;
        this.specialModel = specialModel;
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    @OnlyIn(Dist.CLIENT)
    public IBakedModel getModel() {
        if (this.cachedModel == null) {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
            if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
                return model;
            }
            this.cachedModel = model;
        }
        return this.cachedModel;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void register(ModelRegistryEvent event) {
        for (SpecialModels model : values()) {
            if (model.specialModel) {
                ModelLoader.addSpecialModel(model.modelLocation);
            }
        }
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        ((SimpleReloadableResourceManager) manager).addReloadListener(new ResourceReloadListener());
    }

    public static void cleanCache() {
        for (SpecialModels model : values()) {
            model.cachedModel = null;
        }
    }
}
