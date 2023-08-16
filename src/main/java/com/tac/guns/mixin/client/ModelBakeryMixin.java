package com.tac.guns.mixin.client;

import com.tac.guns.client.gunskin.SkinLoader;
import com.tac.guns.client.gunskin.SkinManager;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin({ModelBakery.class})
public abstract class ModelBakeryMixin {
    @Shadow
    public abstract IUnbakedModel getUnbakedModel(ResourceLocation modelLocation);
    @Shadow
    @Final
    private Map<ResourceLocation, IUnbakedModel> unbakedModels;
    @Shadow
    @Final
    private Map<ResourceLocation, IUnbakedModel> topUnbakedModels;
    @Shadow @Final public static ModelResourceLocation MODEL_MISSING;
    @Unique
    private boolean timelessandClassics_Reforged$flag = true;

    @Inject(method = "processLoading",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/profiler/IProfiler;endStartSection(Ljava/lang/String;)V",
                    ordinal = 0
            ),
            remap = true)
    public void addSpecialModels(IProfiler profilerIn, int maxMipmapLevel, CallbackInfo ci) {
        SkinLoader.missingModel = getUnbakedModel(MODEL_MISSING);
        SkinLoader.unbakedModels = unbakedModels;
        SkinLoader.topUnbakedModels = topUnbakedModels;

        if (timelessandClassics_Reforged$flag) {
            SkinManager.loadDefaultSkins();
            timelessandClassics_Reforged$flag = false;
        }

        SkinManager.reload();

//        ResourceLocation raw = new ResourceLocation(Reference.MOD_ID,"special/ak47");
//
//        BlockModel r = (BlockModel) getUnbakedModel(raw);
//
//        List<BlockPart> list = Lists.newArrayList();
//        Map<String, Either<RenderMaterial, String>> map = Maps.newHashMap();
//
//        BlockModel model = new BlockModel(raw,list,map,true,null,r.getAllTransforms(),r.getOverrides());
//        ResourceLocation rl = new ResourceLocation(Reference.MOD_ID,"special/ak47/test");
//        model.name = rl.toString();
//        model.parent = r;
//
//        ResourceLocation al = new ResourceLocation("minecraft:textures/atlas/blocks.png");
//        ResourceLocation tl = new ResourceLocation("tac:gunskin/ak47/ak47_golden");
//
//        Either<RenderMaterial, String> t = Either.left(new RenderMaterial(al, tl));
//
    }
}