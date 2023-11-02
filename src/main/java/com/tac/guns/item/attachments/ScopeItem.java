package com.tac.guns.item.attachments;

import com.tac.guns.common.attachments.AttachmentType;
import com.tac.guns.item.IColored;
import com.tac.guns.item.attachment.IScope;
import com.tac.guns.item.attachment.impl.Scope;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ScopeItem extends AttachmentItem implements IColored {
    private final boolean colored;

    public ScopeItem(Properties properties)
    {
        super(properties);
        this.colored = true;
    }

    @Override
    public AttachmentType getType() {
        return AttachmentType.SCOPE;
    }

    public ScopeItem(Properties properties, boolean colored)
    {
        super(properties);
        this.colored = colored;
    }

    @Override
    public boolean canColor(ItemStack stack) {
        return this.colored;
    }

    @Override
    public boolean hasEffect(ItemStack gunItem) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE || super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
