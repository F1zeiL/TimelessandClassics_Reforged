package com.tac.guns.common.container;

import com.tac.guns.common.Gun;
import com.tac.guns.common.container.slot.DyeSlot;
import com.tac.guns.common.container.slot.SlotType;
import com.tac.guns.init.ModContainers;
import com.tac.guns.item.IrDeviceItem;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.SideRailItem;
import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.item.attachment.impl.Attachment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class DyeContainer extends Container {
    private ItemStack weapon;
    private IInventory playerInventory;
    private IInventory weaponInventory = new Inventory(3) {
        @Override
        public void markDirty() {
            super.markDirty();
            DyeContainer.this.onCraftMatrixChanged(this);
        }
    };

    private boolean loaded = false;

    public DyeContainer(int windowId, PlayerInventory playerInventory, ItemStack stack) // reads from attachments inv
    {
        this(windowId, playerInventory);
        ItemStack[] dyes = new ItemStack[3];
        if (this.weapon.getItem() instanceof ScopeItem ||
            this.weapon.getItem() instanceof IrDeviceItem ||
            this.weapon.getItem()instanceof SideRailItem) {
            dyes[0] = Gun.getAttachment(SlotType.SCOPE_RETICLE_COLOR, stack);
            dyes[1] = Gun.getAttachment(SlotType.SCOPE_BODY_COLOR, stack);
            dyes[2] = Gun.getAttachment(SlotType.SCOPE_GLASS_COLOR, stack);

            for (int i = 0; i < 3; i++) {
                this.weaponInventory.setInventorySlotContents(i, dyes[i]);
            }
        }
        this.loaded = true;
    }

    public DyeContainer(int windowId, PlayerInventory playerInventory) {
        super(ModContainers.DYES.get(), windowId);
        this.weapon = playerInventory.getCurrentItem();
        this.playerInventory = playerInventory;
        // dyes

        this.addSlot(new DyeSlot(this, this.weaponInventory, this.weapon, SlotType.SCOPE_RETICLE_COLOR,
                playerInventory.player, 0, 70, 50 + 18));


        this.addSlot(new DyeSlot(this, this.weaponInventory, this.weapon, SlotType.SCOPE_BODY_COLOR,
                playerInventory.player, 1, 40, -1 + 18));

        this.addSlot(new DyeSlot(this, this.weaponInventory, this.weapon, SlotType.SCOPE_GLASS_COLOR,
                playerInventory.player, 2, 10, 50 + 18));

        // inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }
        // hot bars
        for (int i = 0; i < 9; i++) {
            if (i == playerInventory.currentItem) {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160) {
                    @Override
                    public boolean canTakeStack(PlayerEntity playerIn) {
                        return false;
                    }
                });
            } else {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160) {
                    @Override
                    public boolean canTakeStack(PlayerEntity playerIn) {
                        return true;
                    }
                });
            }
        }
    }


    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        CompoundNBT attachments = new CompoundNBT();

        if (this.weapon.getItem() instanceof ScopeItem ||
                this.weapon.getItem() instanceof SideRailItem ||
                this.weapon.getItem() instanceof IrDeviceItem) {
            for (int i = 0; i < this.getWeaponInventory().getSizeInventory(); i++) {
                ItemStack attachment = this.getSlot(i).getStack();
                if (attachment.getItem() instanceof DyeItem) {
                    if (i == 0)
                        attachments.put(IAttachment.Type.SCOPE_RETICLE_COLOR.getTagKey(), attachment.write(new CompoundNBT()));
                    if (i == 1)
                        attachments.put(IAttachment.Type.SCOPE_BODY_COLOR.getTagKey(), attachment.write(new CompoundNBT()));
                    if (i == 2)
                        attachments.put(IAttachment.Type.SCOPE_GLASS_COLOR.getTagKey(), attachment.write(new CompoundNBT()));
                }
            }
        }

        CompoundNBT tag = this.weapon.getOrCreateTag();
        tag.put("Attachments", attachments);
        super.detectAndSendChanges();
    }

    private void checkAndWrite(ItemStack attachment, CompoundNBT attachments) {
        if(playerInventory instanceof PlayerInventory) {
            boolean isLocal = ((PlayerInventory) playerInventory).player.world.isRemote();
            if (Attachment.canApplyOn(attachment, (TimelessGunItem) this.weapon.getItem())) {
//            attachments.put(( (IAttachment<?>) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
                attachments.put(((IAttachment<?>) attachment.getItem()).getSlot().getTagKey(), attachment.write(new CompoundNBT()));
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            copyStack = slotStack.copy();

            if (index < this.weaponInventory.getSizeInventory()) {
                if (!this.mergeItemStack(slotStack, this.weaponInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 0, this.weaponInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return copyStack;
    }

    public IInventory getPlayerInventory() {
        return this.playerInventory;
    }

    public IInventory getWeaponInventory() {
        return this.weaponInventory;
    }
}
