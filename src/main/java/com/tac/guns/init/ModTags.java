package com.tac.guns.init;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static final Tags.IOptionalNamedTag<Block> bullet_ignore = BlockTags.createOptional(new ResourceLocation("tac:bullet_ignore"));
    public static void init(){}
}
