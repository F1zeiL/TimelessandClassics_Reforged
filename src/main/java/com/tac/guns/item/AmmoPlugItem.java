package com.tac.guns.item;

import com.tac.guns.common.container.slot.SlotType;
import com.tac.guns.item.attachment.IAmmoPlug;
import com.tac.guns.item.attachment.impl.AmmoPlug;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AmmoPlugItem extends Item implements IAmmoPlug, IColored {
    private final AmmoPlug ammoPlug;
    private final boolean colored;

    public AmmoPlugItem(AmmoPlug ammoPlug, Properties properties)
    {
        super(properties);
        this.ammoPlug = ammoPlug;
        this.colored = true;
    }

    public AmmoPlugItem(AmmoPlug ammoPlug, Properties properties, boolean colored)
    {
        super(properties);
        this.ammoPlug = ammoPlug;
        this.colored = colored;
    }

    @Override
    public AmmoPlug getProperties()
    {
        return this.ammoPlug;
    }

    @Override
    public SlotType getSlot() {
        return SlotType.AMMO;
    }

    @Override
    public boolean canColor(ItemStack stack)
    {
        return this.colored;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment == Enchantments.BINDING_CURSE || super.canApplyAtEnchantingTable(stack, enchantment);
    }
}