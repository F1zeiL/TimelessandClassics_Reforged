package com.tac.guns.common;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Reference;
import com.tac.guns.common.network.ServerPlayHandler;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.*;
import com.tac.guns.util.GunEnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import com.tac.guns.util.GunModifierHelper;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ReloadTracker
{
    private static final Map<PlayerEntity, ReloadTracker> RELOAD_TRACKER_MAP = new WeakHashMap<>();

    private final int startTick;
    private final int slot;
    private final ItemStack stack;
    private final Gun gun;

    private ReloadTracker(PlayerEntity player)
    {
        this.startTick = player.ticksExisted;
        this.slot = player.inventory.currentItem;
        this.stack = player.inventory.getCurrentItem();
        this.gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
    }

    /**
     * Tests if the current item the player is holding is the same as the one being reloaded
     *
     * @param player the player to check
     * @return True if it's the same weapon and slot
     */
    private boolean isSameWeapon(PlayerEntity player)
    {
        return !this.stack.isEmpty() && player.inventory.currentItem == this.slot && player.inventory.getCurrentItem() == this.stack;
    }

    /**
     * @return
     */
    private boolean isWeaponFull()
    {
        CompoundNBT tag = this.stack.getOrCreateTag();
        return tag.getInt("AmmoCount") >= GunModifierHelper.getAmmoCapacity(this.stack, this.gun);
    }
    /**
     * @return
     */
    private boolean isWeaponEmpty()
    {
        CompoundNBT tag = this.stack.getOrCreateTag();
        return tag.getInt("AmmoCount") == 0;
    }

    private boolean hasNoAmmo(PlayerEntity player)
    {
        return Gun.findAmmo(player, this.gun.getProjectile().getItem()).length == 0 ? true : Gun.findAmmo(player, this.gun.getProjectile().getItem())[0].isEmpty();
    }

    private boolean canReload(PlayerEntity player)
    {
        boolean reload;
        Gun gun = ((GunItem)this.stack.getItem()).getGun();
        if(gun.getReloads().isMagFed())
        {
            if(this.isWeaponEmpty())
            {
                int deltaTicks = player.ticksExisted - this.startTick;
                int interval = gun.getReloads().getReloadMagTimer()+gun.getReloads().getAdditionalReloadEmptyMagTimer()+this.gun.getReloads().getPreReloadPauseTicks();//GunEnchantmentHelper.getReloadInterval(this.stack);
                reload = deltaTicks > interval; // deltaTicks > 0 &&
            }
            else
            {
                int deltaTicks = player.ticksExisted - this.startTick;
                int interval = gun.getReloads().getReloadMagTimer()+this.gun.getReloads().getPreReloadPauseTicks();//GunEnchantmentHelper.getReloadInterval(this.stack);
                reload = deltaTicks > interval; // deltaTicks > 0 &&
            }
        }
        else
        {
            int deltaTicks = player.ticksExisted - this.startTick;
            int interval = GunEnchantmentHelper.getReloadInterval(this.stack);
            reload = deltaTicks > 0 && deltaTicks % interval == 0;
        }
        return reload;
    }

    private void increaseAmmo(PlayerEntity player)
    {
        ItemStack ammo = Gun.findAmmo(player, this.gun.getProjectile().getItem())[0];
        if(!ammo.isEmpty())
        {
            CompoundNBT tag = this.stack.getTag();
            int amount = Math.min(ammo.getCount(), this.gun.getReloads().getReloadAmount());
            if (tag != null) {
                int maxAmmo = GunModifierHelper.getAmmoCapacity(this.stack, this.gun);
                amount = Math.min(amount, maxAmmo - tag.getInt("AmmoCount"));
                tag.putInt("AmmoCount", tag.getInt("AmmoCount") + amount);
            }
            //ammo.shrink(amount);
            this.shrinkFromAmmoPool(new ItemStack[]{ammo}, player, amount);
        }

        ResourceLocation reloadSound = this.gun.getSounds().getReload();
        if(reloadSound != null)
        {
            MessageGunSound message = new MessageGunSound(reloadSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) player.getPosY() + 1.0F, (float) player.getPosZ(), 1.0F, 1.0F, player.getEntityId(), false, true);
            PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), (player.getPosY() + 1.0), player.getPosZ(), 16.0, player.world.getDimensionKey())), message);
        }
    }

    public static int calcMaxReserveAmmo(ItemStack[] ammoStacks)
    {
        int result = 0;
        for (ItemStack x: ammoStacks)
            result+=x.getCount();
        return result;
    }

    private void shrinkFromAmmoPool(ItemStack[] ammoStacks, PlayerEntity player, int shrinkAmount)
    {
        int shrinkAmt = shrinkAmount;
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (ItemStack x: ammoStacks)
        {
            if(!x.isEmpty())
            {
                int max = Math.min(shrinkAmt, x.getCount());
                x.shrink(max);
                shrinkAmt-=max;
            }
            if(shrinkAmt==0)
                return;
        }
    }

    private void increaseMagAmmo(PlayerEntity player)
    {
        /*ItemStack rig = WearableHelper.PlayerWornRig(player);
        if(rig != null) {
            RigSlotsHandler itemHandler = (RigSlotsHandler) rig.getCapability(ArmorRigCapabilityProvider.capability).resolve().get();
            for (ItemStack x : itemHandler.getStacks()) {
                if(Gun.isAmmo(x, this.gun.getProjectile().getItem()))
                    ammoStacks.add(x);
            }
        }*/
        ItemStack[] ammoStacks = Gun.findAmmo(player, this.gun.getProjectile().getItem());
        int stackItor = 0;
        //ItemStack ammo = Gun.findAmmo(player, this.gun.getProjectile().getItem());
        if(ammoStacks.length > 0)
        {
            CompoundNBT tag = this.stack.getTag();
            int ammoAmount = Math.min(calcMaxReserveAmmo(ammoStacks), GunModifierHelper.getAmmoCapacity(this.stack, this.gun));
            if (tag != null) {
                int currentAmmo = tag.getInt("AmmoCount");
                int maxAmmo = GunModifierHelper.getAmmoCapacity(this.stack, this.gun);
                int amount = maxAmmo - currentAmmo; //amount < maxAmmo ? maxAmmo - amount :
                if(currentAmmo == 0 && !this.gun.getReloads().isOpenBolt())
                {
                    if(ammoAmount < amount) {
                        tag.putInt("AmmoCount", currentAmmo+ammoAmount);
                        this.shrinkFromAmmoPool(ammoStacks, player, ammoAmount);
                    }
                    else {
                        tag.putInt("AmmoCount", maxAmmo - 1);
                        this.shrinkFromAmmoPool(ammoStacks, player,amount-1);
                    }
                }
                else {
                    if(ammoAmount < amount) {
                        tag.putInt("AmmoCount", currentAmmo+ammoAmount);
                        this.shrinkFromAmmoPool(ammoStacks,player, ammoAmount);
                    }
                    else {
                        tag.putInt("AmmoCount", maxAmmo);
                        this.shrinkFromAmmoPool(ammoStacks,player, amount);//ammoStacks.shrink(amount);
                    }
                }
            }
        }

        ResourceLocation reloadSound = this.gun.getSounds().getReload();
        if(reloadSound != null)
        {
            MessageGunSound message = new MessageGunSound(reloadSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) player.getPosY() + 1.0F, (float) player.getPosZ(), 1.0F, 1.0F, player.getEntityId(), false, true);
            PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), (player.getPosY() + 1.0), player.getPosZ(), 16.0, player.world.getDimensionKey())), message);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && !event.player.world.isRemote)
        {
            PlayerEntity player = event.player;
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
            {
                if(!RELOAD_TRACKER_MAP.containsKey(player))
                {
                    if(!(player.inventory.getCurrentItem().getItem() instanceof GunItem))
                    {
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, false);
                        return;
                    }
                    RELOAD_TRACKER_MAP.put(player, new ReloadTracker(player));
                }
                ReloadTracker tracker = RELOAD_TRACKER_MAP.get(player);
                if(!tracker.isSameWeapon(player) || tracker.isWeaponFull() || tracker.hasNoAmmo(player))
                {
                    RELOAD_TRACKER_MAP.remove(player);
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, true);
                    return;
                }
                if(tracker.canReload(player))
                {
                    final PlayerEntity finalPlayer = player;
                    final Gun gun = tracker.gun;
                    if(gun.getReloads().isMagFed())
                    {
                        tracker.increaseMagAmmo(player);
                        RELOAD_TRACKER_MAP.remove(player);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, false);
                        /*DelayedTask.runAfter(2, () ->
                        {
                            ResourceLocation cockSound = gun.getSounds().getCock();
                            if (cockSound != null && finalPlayer.isAlive()) {
                                MessageGunSound messageSound = new MessageGunSound(cockSound, SoundCategory.PLAYERS, (float) finalPlayer.getPosX(), (float) (finalPlayer.getPosY() + 1.0), (float) finalPlayer.getPosZ(), 1.0F, 1.0F, finalPlayer.getEntityId(), false, true);
                                PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(finalPlayer.getPosX(), (finalPlayer.getPosY() + 1.0), finalPlayer.getPosZ(), 16.0, finalPlayer.world.getDimensionKey())), messageSound);
                            }
                        });*/
                    }
                    else {
                        tracker.increaseAmmo(player);
                        if (tracker.isWeaponFull() || tracker.hasNoAmmo(player)) {
                            RELOAD_TRACKER_MAP.remove(player);
                            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, false);
                            /*DelayedTask.runAfter(4, () ->
                            {
                                ResourceLocation cockSound = gun.getSounds().getCock();
                                if (cockSound != null && finalPlayer.isAlive()) {
                                    MessageGunSound messageSound = new MessageGunSound(cockSound, SoundCategory.PLAYERS, (float) finalPlayer.getPosX(), (float) (finalPlayer.getPosY() + 1.0), (float) finalPlayer.getPosZ(), 1.0F, 1.0F, finalPlayer.getEntityId(), false, true);
                                    PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(finalPlayer.getPosX(), (finalPlayer.getPosY() + 1.0), finalPlayer.getPosZ(), 16.0, finalPlayer.world.getDimensionKey())), messageSound);
                                }
                            });*/
                        }
                    }
                }
            }
            else if(RELOAD_TRACKER_MAP.containsKey(player))
            {
                RELOAD_TRACKER_MAP.remove(player);
            }
        }
    }

    public static boolean isPlayerReload(PlayerEntity player){
        return RELOAD_TRACKER_MAP.containsKey(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerEvent.PlayerLoggedOutEvent event)
    {
        MinecraftServer server = event.getPlayer().getServer();
        if(server != null)
        {
            server.execute(() -> RELOAD_TRACKER_MAP.remove(event.getPlayer()));
        }
    }
}
