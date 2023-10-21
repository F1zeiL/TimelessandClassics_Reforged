package com.tac.guns.common.container;

import com.tac.guns.common.Gun;
import com.tac.guns.common.container.slot.AttachmentSlot;
import com.tac.guns.init.ModContainers;
import com.tac.guns.item.*;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.TransitionalTypes.TimelessOldRifleGunItem;
import com.tac.guns.item.TransitionalTypes.TimelessPistolGunItem;
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
public class AttachmentContainer extends Container {
    private ItemStack weapon;
    private IInventory playerInventory;
    private IInventory weaponInventory = new Inventory(IAttachment.Type.values().length) {
        @Override
        public void markDirty() {
            super.markDirty();
            AttachmentContainer.this.onCraftMatrixChanged(this);
        }
    };

    private boolean loaded = false;

    public static ItemStack[] getAttachments(ItemStack stack) {
        ItemStack[] attachments = new ItemStack[IAttachment.Type.values().length];
        if (stack.getItem() instanceof ScopeItem ||
                stack.getItem() instanceof SideRailItem ||
                stack.getItem() instanceof IrDeviceItem) {
            for (int i = 11; i < attachments.length; i++) {
                attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
        } else if (stack.getItem() instanceof TimelessOldRifleGunItem) {
            for (int i = 0; i < attachments.length - 7; i++) {
                if (i == 0)
                    attachments[i] = Gun.getAttachment(IAttachment.Type.OLD_SCOPE, stack);
                else
                    attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
        } else if (stack.getItem() instanceof TimelessPistolGunItem) {
            for (int i = 0; i < attachments.length - 7; i++) {
                if (i == 0)
                    attachments[i] = Gun.getAttachment(IAttachment.Type.PISTOL_SCOPE, stack);
                else if (i == 1)
                    attachments[i] = Gun.getAttachment(IAttachment.Type.PISTOL_BARREL, stack);
                else
                    attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
        } else if (stack.getItem() instanceof TimelessGunItem) {
            for (int i = 0; i < attachments.length - 7; i++) {
                /*if(i == 0)
                    attachments[i] = Gun.getAttachment(IAttachment.Type.SCOPE, stack);*/
                attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
        }
        return attachments;
    }

    public AttachmentContainer(int windowId, PlayerInventory playerInventory, ItemStack stack) // reads from attachments inv
    {
        this(windowId, playerInventory);
        ItemStack[] attachments = new ItemStack[IAttachment.Type.values().length];
        if (this.weapon.getItem() instanceof ScopeItem ||
                this.weapon.getItem() instanceof SideRailItem ||
                this.weapon.getItem() instanceof IrDeviceItem) {
            for (int i = 11; i < attachments.length; i++) {
                attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
            for (int i = 11; i < attachments.length; i++) {
                this.weaponInventory.setInventorySlotContents(i, attachments[i]);
            }
        } else if (this.weapon.getItem() instanceof TimelessOldRifleGunItem) {
            for (int i = 0; i < attachments.length - 7; i++) {
                if (i == 0)
                    attachments[0] = Gun.getAttachment(IAttachment.Type.OLD_SCOPE, stack);
                else
                    attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
            for (int i = 0; i < attachments.length - 7; i++) {
                this.weaponInventory.setInventorySlotContents(i, attachments[i]);
            }
        } else if (this.weapon.getItem() instanceof TimelessPistolGunItem) {
            for (int i = 0; i < attachments.length - 7; i++) {
                if (i == 0)
                    attachments[0] = Gun.getAttachment(IAttachment.Type.PISTOL_SCOPE, stack);
                else if (i == 1)
                    attachments[1] = Gun.getAttachment(IAttachment.Type.PISTOL_BARREL, stack);
                else
                    attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
            for (int i = 0; i < attachments.length - 7; i++) {
                this.weaponInventory.setInventorySlotContents(i, attachments[i]);
            }
        } else if (this.weapon.getItem() instanceof TimelessGunItem) {
            for (int i = 0; i < attachments.length - 7; i++) {
                if (Gun.getAttachment(IAttachment.Type.PISTOL_SCOPE, stack) != ItemStack.EMPTY && i == 0)
                    attachments[0] = Gun.getAttachment(IAttachment.Type.PISTOL_SCOPE, stack);
                else if (Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack) != ItemStack.EMPTY && i == 4)
                    attachments[4] = Gun.getAttachment(IAttachment.Type.IR_DEVICE, stack);
                else if (Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack) != ItemStack.EMPTY && i == 4)
                    attachments[4] = Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack);
                else
                    attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
            }
            for (int i = 0; i < attachments.length - 7; i++) {
                this.weaponInventory.setInventorySlotContents(i, attachments[i]);
            }
        }
        this.loaded = true;
    }

    public AttachmentContainer(int windowId, PlayerInventory playerInventory) {
        super(ModContainers.ATTACHMENTS.get(), windowId);
        this.weapon = playerInventory.getCurrentItem();
        this.playerInventory = playerInventory;

        if (this.weapon.getItem() instanceof ScopeItem ||
                this.weapon.getItem() instanceof SideRailItem ||
                this.weapon.getItem() instanceof IrDeviceItem) {
            // So this is pretty much me f'ing around with a single enum, likely should just adjust the properties of this enum in order to get the position in Index separately depending on the type of slot, instead of implementation requiring this
            for (int i = 11; i < IAttachment.Type.values().length; i++) {
                if (i == 11) {
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.SCOPE_RETICLE_COLOR,
                            playerInventory.player, i, 70, 50 + 18) {
                        @Override
                        public boolean canTakeStack(PlayerEntity playerIn) {
                            return true;
                        }
                    });
                }
                if (i == 12) {
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.SCOPE_BODY_COLOR,
                            playerInventory.player, i, 40, -1 + 18) {
                        @Override
                        public boolean canTakeStack(PlayerEntity playerIn) {
                            return true;
                        }
                    });
                }
                if (i == 13) {
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.SCOPE_GLASS_COLOR,
                            playerInventory.player, i, 10, 50 + 18) {
                        @Override
                        public boolean canTakeStack(PlayerEntity playerIn) {
                            return true;
                        }
                    });
                }
            }
        } else if (this.weapon.getItem() instanceof TimelessOldRifleGunItem) {
            for (int i = 0; i < IAttachment.Type.values().length - 7; i++) {
                if (i == 0)
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.OLD_SCOPE, playerInventory.player, 0, 5, 17));
                else if (i > 3)
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.values()[i], playerInventory.player, i, 155, 17 + (i - 4) * 18));
                else
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.values()[i], playerInventory.player, i, 5, 17 + i * 18));
            }
        } else if (this.weapon.getItem() instanceof TimelessPistolGunItem) {
            for (int i = 0; i < IAttachment.Type.values().length - 7; i++) {
                if (i == 0)
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.PISTOL_SCOPE, playerInventory.player, 0, 5, 17));
                else if (i == 1)
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.PISTOL_BARREL, playerInventory.player, 1, 5, 17 + 18));
                else if (i > 3)
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.values()[i], playerInventory.player, i, 155, 17 + (i - 4) * 18));
                else
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.values()[i], playerInventory.player, i, 5, 17 + i * 18));
            }
        } else if (this.weapon.getItem() instanceof TimelessGunItem) {
            for (int i = 0; i < IAttachment.Type.values().length - 7; i++) {
                if (i == 0 && ((TimelessGunItem) this.weapon.getItem()).getGun().canAttachType(IAttachment.Type.PISTOL_SCOPE))
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, new IAttachment.Type[]{IAttachment.Type.values()[i], IAttachment.Type.PISTOL_SCOPE}, playerInventory.player, 0, 5, 17));
                else if (IAttachment.Type.values()[i] == IAttachment.Type.SIDE_RAIL && ((TimelessGunItem) this.weapon.getItem()).getGun().canAttachType(IAttachment.Type.IR_DEVICE))
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, new IAttachment.Type[]{IAttachment.Type.values()[i], IAttachment.Type.IR_DEVICE}, playerInventory.player, 4, 155, 17));
                else if (IAttachment.Type.values()[i] == IAttachment.Type.IR_DEVICE && ((TimelessGunItem) this.weapon.getItem()).getGun().canAttachType(IAttachment.Type.SIDE_RAIL))
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, new IAttachment.Type[]{IAttachment.Type.values()[i], IAttachment.Type.SIDE_RAIL}, playerInventory.player, 4, 155, 17));
                else if (i > 3) {
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.values()[i], playerInventory.player, i, 155, 17 + (i - 4) * 18));
                } else
                    this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.values()[i], playerInventory.player, i, 5, 17 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            if (i == playerInventory.currentItem) {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160) {
                    @Override
                    public boolean canTakeStack(PlayerEntity playerIn) {
                        return true;
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

    public boolean hasExMag() {
        if (Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, this.weapon) != ItemStack.EMPTY)
            return true;
        else
            return false;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) // something with this...
    {
        //todo:maybe it needs to be optimized?
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
        } else if (this.weapon.getItem() instanceof TimelessOldRifleGunItem) {
            for (int i = 0; i < 7; i++) {
                ItemStack attachment = this.getSlot(i).getStack();
                if (i == 0) {
                    if (attachment.getItem() instanceof OldScopeItem) {
                        attachments.put(((IAttachment) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
                    }
                } else {
                    if (attachment.getItem() instanceof IAttachment) {
                        attachments.put(((IAttachment) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
                    }
                }
            }
        } else if (this.weapon.getItem() instanceof TimelessPistolGunItem) {
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    ItemStack attachment = this.getSlot(i).getStack();
                    if (attachment.getItem() instanceof PistolScopeItem) {
                        attachments.put(((IAttachment) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
                    }
                } else if (i == 1) {
                    ItemStack attachment = this.getSlot(i).getStack();
                    if (attachment.getItem() instanceof PistolBarrelItem) {
                        attachments.put(((IAttachment) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
                    }
                } else {
                    ItemStack attachment = this.getSlot(i).getStack();
                    if (attachment.getItem() instanceof IAttachment) {
                        attachments.put(((IAttachment) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
                    }
                }
            }
        } else if (this.weapon.getItem() instanceof TimelessGunItem) {
            for (int i = 0; i < 7; i++) {
                ItemStack attachment = this.getSlot(i).getStack();
                if (attachment.getItem() instanceof IAttachment){
                    checkAndWrite(attachment, attachments);
                }
            }
        }

        CompoundNBT tag = this.weapon.getOrCreateTag();
        tag.put("Attachments", attachments);
        super.detectAndSendChanges();
    }

    private void checkAndWrite(ItemStack attachment, CompoundNBT attachments) {
        if( Attachment.canApplyOn(attachment, (TimelessGunItem) this.weapon.getItem()) ){
            attachments.put(((IAttachment<?>) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (this.weapon.getItem() instanceof ScopeItem ||
                this.weapon.getItem() instanceof SideRailItem ||
                this.weapon.getItem() instanceof IrDeviceItem) {
            if (slot != null && slot.getHasStack()) {
                ItemStack slotStack = slot.getStack();
                copyStack = slotStack.copy();

                if (index == 0) {
                    if (!this.mergeItemStack(slotStack, 0, 36, true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (slotStack.getItem() instanceof DyeItem) {
                        if (!this.mergeItemStack(slotStack, 0, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index < 28) {
                        if (!this.mergeItemStack(slotStack, 28, 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index <= 36 && !this.mergeItemStack(slotStack, 0, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (slotStack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }

                if (slotStack.getCount() == copyStack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTake(playerIn, slotStack);
            }
        } else {
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
