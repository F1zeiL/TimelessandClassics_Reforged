package com.tac.guns.common.container.slot;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.common.Gun;
import com.tac.guns.common.container.AttachmentContainer;
import com.tac.guns.init.ModSounds;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.item.attachment.impl.Attachment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class AttachmentSlot extends Slot {
    private AttachmentContainer container;
    private ItemStack weapon;
    private SlotType type;
    private PlayerEntity player;
    private int index;

    public AttachmentSlot(AttachmentContainer container, IInventory weaponInventory, ItemStack weapon, SlotType type, PlayerEntity player, int index, int x, int y) {
        super(weaponInventory, index, x, y);
        this.container = container;
        this.weapon = weapon;
        this.player = player;
        this.index = index;
        this.type = type;
    }

    @Override
    public boolean isEnabled() {
        this.weapon.inventoryTick(player.world, player, index, true);
        if ((this.type == SlotType.EXTENDED_MAG && this.weapon.getOrCreateTag().getInt("AmmoCount") > ((TimelessGunItem) this.weapon.getItem()).getGun().getReloads().getMaxAmmo())
                || SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING) || EnchantmentHelper.hasBindingCurse(this.container.getWeaponInventory().getStackInSlot(this.index))) {
            return false;
        }
        if ((this.type == SlotType.AMMO && this.weapon.getOrCreateTag().getInt("AmmoCount") > 0)
                || SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING) || EnchantmentHelper.hasBindingCurse(this.container.getWeaponInventory().getStackInSlot(this.index))) {
            return false;
        }
        if (this.weapon.getItem() instanceof GunItem) {
            GunItem item = (GunItem) this.weapon.getItem();
            Gun modifiedGun = item.getModifiedGun(this.weapon);
            return modifiedGun.hasSlot(this.type);
        }
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        //todo: logic of this part has been reorganized, need to confirm if there are any side effects or bugs.
        //gun attachments
        if(this.weapon.getItem() instanceof TimelessGunItem){
            if(!(stack.getItem() instanceof IAttachment<?>))return false;

            TimelessGunItem weapon = (TimelessGunItem) this.weapon.getItem();
            int maxAmmo = weapon.getGun().getReloads().getMaxAmmo();
            if(this.type == SlotType.EXTENDED_MAG && Gun.getAmmo(this.weapon) > maxAmmo){
                return false;
            }
            if(this.type == SlotType.AMMO && Gun.getAmmo(this.weapon) > 0){
                return false;
            }
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING)){
                return false;
            }

            //check extra limit from nbt tags
            Gun modifiedGun = weapon.getModifiedGun(this.weapon);
            boolean flag = ((PlayerInventory)this.container.getPlayerInventory()).player.world.isRemote();
            if(!Attachment.canApplyOn(stack,weapon)){
                return false;
            }
            SlotType stackType = ((IAttachment<?>) stack.getItem()).getSlot();
            IAttachment.Type attachmentType = ((IAttachment<?>) stack.getItem()).getType();

            if(this.type!=null){
                return stackType == this.type && modifiedGun.canAttachType(attachmentType);
            }

        }
        return false;
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