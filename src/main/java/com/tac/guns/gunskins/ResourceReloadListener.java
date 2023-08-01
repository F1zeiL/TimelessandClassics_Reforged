package com.tac.guns.gunskins;

import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.render.gun.SkinAnimationModel;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.util.function.Predicate;

public class ResourceReloadListener implements ISelectiveResourceReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        SpecialModels.cleanCache();
        SkinAnimationModel.cleanAllCache();
        SkinManager.reload();
    }
}
