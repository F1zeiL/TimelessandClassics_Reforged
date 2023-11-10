package com.tac.guns.item;

import com.tac.guns.common.container.slot.SlotType;
import com.tac.guns.item.attachment.IirDevice;
import com.tac.guns.item.attachment.impl.IrDevice;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * A basic under barrel attachment item implementation with color support
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class IrDeviceItem extends Item implements IirDevice, IColored
{
    private final IrDevice sideRail;
    private final boolean colored;

    public IrDeviceItem(IrDevice underBarrel, Properties properties)
    {
        super(properties);
        this.sideRail = underBarrel;
        this.colored = true;
    }

    public IrDeviceItem(IrDevice underBarrel, Properties properties, boolean colored)
    {
        super(properties);
        this.sideRail = underBarrel;
        this.colored = colored;
    }

    @Override
    public IrDevice getProperties()
    {
        return this.sideRail;
    }

    @Override
    public SlotType getSlot() {
        return SlotType.SIDE_RAIL;
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
