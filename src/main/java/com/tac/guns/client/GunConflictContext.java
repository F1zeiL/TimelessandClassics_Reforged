package com.tac.guns.client;

import com.tac.guns.item.GunItem;
import com.tac.guns.item.IrDeviceItem;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.SideRailItem;
import net.minecraft.client.Minecraft;
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
            return !KeyConflictContext.GUI.isActive() && Minecraft.getInstance().player != null &&
                    (Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof GunItem ||
                            Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ScopeItem ||
                            Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof SideRailItem ||
                            Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof IrDeviceItem);
        }

        @Override
        public boolean conflicts(IKeyConflictContext other)
        {
            return this == other;
        }
    }
}
