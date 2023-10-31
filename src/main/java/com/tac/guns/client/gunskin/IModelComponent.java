package com.tac.guns.client.gunskin;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static com.tac.guns.client.gunskin.ModelComponent.BODY;

public interface IModelComponent {
    /**
     * @return The default model location of the component according to the main component
     */
    @Nullable
    static ResourceLocation getModelLocation(IModelComponent component, String mainLocation) {
        return component.getModelLocation(mainLocation);
    }

    @Nullable
    static ResourceLocation getModelLocation(IModelComponent component, ResourceLocation mainLocation) {
        return component.getModelLocation(mainLocation);
    }

    @Nullable
    default ResourceLocation getModelLocation(String mainLocation){
        return ResourceLocation.tryCreate(mainLocation+(this==BODY ? "" : "_" + this.getKey()));
    }

    @Nullable
    default ResourceLocation getModelLocation(ResourceLocation mainLocation){
        return ResourceLocation.tryCreate(mainLocation+(this==BODY ? "" : "_" + this.getKey()));
    }

    String getKey();
}