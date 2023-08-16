package com.tac.guns.client.gunskin;

import com.tac.guns.client.SpecialModels;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.util.function.Predicate;

public class ResourceReloadListener implements ISelectiveResourceReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        SpecialModels.cleanCache();
        SkinManager.cleanCache();
    }
}
