package com.tac.guns.tileentity;

import com.tac.guns.common.container.UpgradeBenchContainer;
import com.tac.guns.init.ModTileEntities;
import com.tac.guns.tileentity.inventory.IStorageBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class UpgradeBenchTileEntity extends SyncedTileEntity implements IStorageBlock
{
    private NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

    public UpgradeBenchTileEntity()
    {
        super(ModTileEntities.WORKBENCH.get()/*UPGRADE_BENCH.get()*/);
    }

    @Override
    public NonNullList<ItemStack> getInventory()
    {
        return this.inventory;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        this.write(nbtTagCompound);
        return nbtTagCompound;
    }
    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT tag)
    {
        this.read(blockState, tag);
    }
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState blockState = world.getBlockState(pos);
        this.read(blockState, pkt.getNbtCompound());   // read from the nbt in the packet
    }
    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        this.write(nbtTagCompound);
        int tileEntityType = 42;  // arbitrary number; only used for vanilla TileEntities.
        return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);

        CompoundNBT weaponBt = new CompoundNBT();
        this.inventory.get(0).write(weaponBt);
        if(this.inventory.get(0).getTag() != null)
            compound.put("weapon", weaponBt);

        CompoundNBT modules = new CompoundNBT();
        this.inventory.get(1).write(modules);
        if(this.inventory.get(1).getOrCreateTag() != null)
            compound.put("modules", modules);

        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound)
    {
        super.read(state, compound);
        if(compound.contains("weapon"))
            this.inventory.set(0, ItemStack.read(compound.getCompound("weapon")));

        CompoundNBT itemStackNBT = compound.getCompound("modules");
        ItemStack readItemStack = ItemStack.read(itemStackNBT);
        this.inventory.set(1, readItemStack);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.tac.upgradeBench");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new UpgradeBenchContainer(windowId, playerInventory, this);
    }
}
