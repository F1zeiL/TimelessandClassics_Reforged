package com.tac.guns.client;

import com.tac.guns.item.GunItem;
import com.tac.guns.item.IrDeviceItem;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.SideRailItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;

/**
 * @author Forked from MrCrayfish, continued by Timeless devs
 */
public enum GunConflictContext implements IKeyConflictContext
{
    IN_GAME_HOLDING_WEAPON
    {
        @Override
        public boolean isActive()
        {
            final Item item = Minecraft.getInstance().player.getHeldItemMainhand().getItem();
            return !KeyConflictContext.GUI.isActive() && Minecraft.getInstance().player != null && (item instanceof GunItem || item instanceof ScopeItem || item instanceof SideRailItem || item instanceof IrDeviceItem);
        }

        @Override
        public boolean conflicts(IKeyConflictContext other)
        {
            return this == other;
        }
    }
}
