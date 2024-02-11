package com.tac.guns.client.gunskin;

import com.tac.guns.Reference;
import com.tac.guns.client.SpecialModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class DyeSkin extends GunSkin {
    public enum PresetType {
        black, blue, brown, dark_blue,
        dark_green, gray, green, jade,
        light_gray, magenta, orange, pink,
        purple, red, sand, white;
        PresetType() {
            this.skinLocation = new ResourceLocation(Reference.MOD_ID, this.name());
        }
        private final ResourceLocation skinLocation;
        public ResourceLocation getSkinLocation() {
            return skinLocation;
        }
    }
    private final PresetType type;
    private int[] colors;
    private BasicSkin base;
    protected DyeSkin(ResourceLocation registerName, ResourceLocation gun, PresetType type) {
        super(registerName, gun);
        this.type = type;
    }

    public int[] getColors() {
        return colors;
    }

    public DyeSkin setColors(int[] colors) {
        this.colors = colors;
        return this;
    }

    public PresetType getType() {
        return type;
    }

    public BasicSkin getBase() {
        return base;
    }

    public DyeSkin setBase(BasicSkin base) {
        this.base = base;
        return this;
    }

    @Nullable
    @Override
    public SpecialModel getModel(IModelComponent component) {
        return base.getModel(component);
    }
}
