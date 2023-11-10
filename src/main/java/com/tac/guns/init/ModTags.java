package com.tac.guns.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static final Tags.IOptionalNamedTag<Block> bullet_ignore =
            BlockTags.createOptional(new ResourceLocation("tac:bullet_ignore"));

    public static final Tags.IOptionalNamedTag<Item> pistol =
            ItemTags.createOptional(new ResourceLocation("tac:pistol"));

    public static final Tags.IOptionalNamedTag<Item> assault_rifle =
            ItemTags.createOptional(new ResourceLocation("tac:assault_rifle"));

    public static final Tags.IOptionalNamedTag<Item> machine_gun =
            ItemTags.createOptional(new ResourceLocation("tac:machine_gun"));

    public static final Tags.IOptionalNamedTag<Item> shot_gun =
            ItemTags.createOptional(new ResourceLocation("tac:shot_gun"));

    public static final Tags.IOptionalNamedTag<Item> sub_machine_gun =
            ItemTags.createOptional(new ResourceLocation("tac:sub_machine_gun"));

    public static final Tags.IOptionalNamedTag<Item> sniper_rifle =
            ItemTags.createOptional(new ResourceLocation("tac:sniper_rifle"));

    public static final Tags.IOptionalNamedTag<Item> launcher =
            ItemTags.createOptional(new ResourceLocation("tac:launcher"));

    public static final Tags.IOptionalNamedTag<Item> battle_rifle =
            ItemTags.createOptional(new ResourceLocation("tac:battle_rifle"));

    public static final Tags.IOptionalNamedTag<Item> old_rifle =
            ItemTags.createOptional(new ResourceLocation("tac:old_rifle"));
    public static void init(){}
}
