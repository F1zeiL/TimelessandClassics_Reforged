package com.tac.guns.common.container.slot;

import com.tac.guns.common.container.DyeContainer;
import com.tac.guns.init.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class DyeSlot extends Slot {
    private DyeContainer container;
    private ItemStack weapon;
    private SlotType type;
    private PlayerEntity player;
    private int index;

    public DyeSlot(DyeContainer container, IInventory weaponInventory, ItemStack weapon, SlotType type, PlayerEntity player, int index, int x, int y) {
        super(weaponInventory, index, x, y);
        this.container = container;
        this.weapon = weapon;
        this.player = player;
        this.index = index;
        this.type = type;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof DyeItem;
    }

    @Override
    public void onSlotChanged() {
        if (this.container.isLoaded()) {
            this.player.world.playSound(null, this.player.getPosX(), this.player.getPosY() + 1.0, this.player.getPosZ(), ModSounds.UI_WEAPON_ATTACH.get(), SoundCategory.PLAYERS, 0.5F, this.getHasStack() ? 1.0F : 0.75F);
        }
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        return true;
    }
}