package com.tac.guns.common;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Reference;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageGunSound;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ReloadTracker {
    private static final Map<PlayerEntity, ReloadTracker> RELOAD_TRACKER_MAP = new WeakHashMap<>();

    private final int startTick;
    private final int slot;
    private final ItemStack stack;
    private final Gun gun;

    private ReloadTracker(PlayerEntity player) {
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
    private boolean isSameWeapon(PlayerEntity player) {
        return !this.stack.isEmpty() && player.inventory.currentItem == this.slot && player.inventory.getCurrentItem() == this.stack;
    }

    /**
     * @return
     */
    private boolean isWeaponFull() {
        CompoundNBT tag = this.stack.getOrCreateTag();
        return tag.getInt("AmmoCount") >= GunModifierHelper.getAmmoCapacity(this.stack, this.gun);
    }

    /**
     * @return
     */
    private boolean isWeaponEmpty() {
        CompoundNBT tag = this.stack.getOrCreateTag();
        return tag.getInt("AmmoCount") == 0;
    }

    private boolean hasNoAmmo(PlayerEntity player) {
        return Gun.findAmmo(player, this.gun.getProjectile().getItem()).length == 0 || Gun.findAmmo(player, this.gun.getProjectile().getItem())[0].isEmpty();
    }

    private boolean canReload(PlayerEntity player) {
        boolean reload;
        Gun gun = ((GunItem) this.stack.getItem()).getGun();
        if (gun.getReloads().isNoMag())
            return false;

        if (gun.getReloads().isMagFed()) {
            if (this.isWeaponEmpty()) {
                int deltaTicks = player.ticksExisted - this.startTick;
                int interval = gun.getReloads().getReloadMagTimer() + gun.getReloads().getAdditionalReloadEmptyMagTimer() + this.gun.getReloads().getPreReloadPauseTicks();//GunEnchantmentHelper.getReloadInterval(this.stack);
                reload = deltaTicks > interval; // deltaTicks > 0 &&
            } else {
                int deltaTicks = player.ticksExisted - this.startTick;
                int interval = gun.getReloads().getReloadMagTimer() + this.gun.getReloads().getPreReloadPauseTicks();//GunEnchantmentHelper.getReloadInterval(this.stack);
                reload = deltaTicks > interval; // deltaTicks > 0 &&
            }
        } else if (gun.getReloads().isStripperClip()) {
            CompoundNBT tag = this.stack.getOrCreateTag();
            int toReload = GunModifierHelper.getAmmoCapacity(stack, gun) - tag.getInt("AmmoCount");
            if (toReload >= gun.getReloads().getStripperClipReloadAmount()) {
                int deltaTicks = player.ticksExisted - this.startTick;
                int interval = gun.getReloads().getAdditionalReloadEmptyMagTimer();
                reload = deltaTicks > interval;
            } else {
                int deltaTicks = player.ticksExisted - this.startTick;
                int interval = GunEnchantmentHelper.getReloadInterval(this.stack);
                reload = deltaTicks > 0 && deltaTicks % interval == 0;
            }
        } else {
            int deltaTicks = player.ticksExisted - this.startTick;
            int interval = GunEnchantmentHelper.getReloadInterval(this.stack);
            reload = deltaTicks > 0 && deltaTicks % interval == 0;
        }
        return reload;
    }

    private void increaseAmmo(PlayerEntity player) {
        ItemStack ammo = Gun.findAmmo(player, this.gun.getProjectile().getItem())[0];
        if (!ammo.isEmpty()) {
            CompoundNBT tag = this.stack.getTag();
            int amount = Math.min(ammo.getCount(), this.gun.getReloads().getReloadAmount());
            if (tag != null) {
                int maxAmmo = GunModifierHelper.getAmmoCapacity(this.stack, this.gun);
                amount = Math.min(amount, maxAmmo - tag.getInt("AmmoCount"));
                tag.putInt("AmmoCount", tag.getInt("AmmoCount") + amount);
            }
            //ammo.shrink(amount);
            shrinkFromAmmoPool(new ItemStack[]{ammo}, player, amount);
        }

        setReloadSound(player);
    }

    public static int calcMaxReserveAmmo(ItemStack[] ammoStacks) {
        int result = 0;
        for (ItemStack x : ammoStacks)
            result += x.getCount();
        return result;
    }

    public static void decreaseReserveAmmo(ItemStack[] ammoStacks, PlayerEntity player, int shrinkAmount) {
        shrinkFromAmmoPool(ammoStacks, player, shrinkAmount);
    }

    private static void shrinkFromAmmoPool(ItemStack[] ammoStacks, PlayerEntity player, int shrinkAmount) {
        int shrinkAmt = shrinkAmount;
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (ItemStack x : ammoStacks) {
            if (!x.isEmpty()) {
                int max = Math.min(shrinkAmt, x.getCount());
                x.shrink(max);
                shrinkAmt -= max;
            }
            if (shrinkAmt == 0)
                return;
        }
    }

    private void increaseAmmoFromStripperClip(PlayerEntity player) {
        ItemStack[] ammoStacks = Gun.findAmmo(player, this.gun.getProjectile().getItem());
        int stackItor = 0;
        if (ammoStacks.length > 0) {
            CompoundNBT tag = this.stack.getTag();
            int ammoAmount = this.gun.getReloads().getStripperClipReloadAmount();//Math.min(calcMaxReserveAmmo(ammoStacks), GunModifierHelper.getAmmoCapacity(this.stack, this.gun));
            if (tag != null) {
                int currentAmmo = tag.getInt("AmmoCount");
                int maxAmmo = GunModifierHelper.getAmmoCapacity(this.stack, this.gun);
                int amount = maxAmmo - currentAmmo;
                if (currentAmmo == 0) {
                    if (ammoAmount <= amount) {
                        tag.putInt("AmmoCount", currentAmmo + ammoAmount);
                        shrinkFromAmmoPool(ammoStacks, player, ammoAmount);
                    }
                }
            }
        }
        ResourceLocation reloadSound = this.gun.getSounds().getReloadEnd();
        sendReload(player, reloadSound);
    }

    private static void sendReload(PlayerEntity player, ResourceLocation reloadSound) {
        if (reloadSound != null) {
            MessageGunSound message = new MessageGunSound(reloadSound, SoundCategory.PLAYERS, (float) player.getPosX(), (float) player.getPosY() + 1.0F, (float) player.getPosZ(), 1.0F, 1.0F, player.getEntityId(), false, true);
            PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), (player.getPosY() + 1.0), player.getPosZ(), 16.0, player.world.getDimensionKey())), message);
        }
    }

    private void increaseMagAmmo(PlayerEntity player) {
        ItemStack[] ammoStacks = Gun.findAmmo(player, this.gun.getProjectile().getItem());
        int stackItor = 0;
        if (ammoStacks.length > 0) {
            CompoundNBT tag = this.stack.getTag();
            int ammoAmount = Math.min(calcMaxReserveAmmo(ammoStacks), GunModifierHelper.getAmmoCapacity(this.stack, this.gun));
            if (tag != null) {
                int currentAmmo = tag.getInt("AmmoCount");
                int maxAmmo = GunModifierHelper.getAmmoCapacity(this.stack, this.gun);
                int amount = maxAmmo - currentAmmo; //amount < maxAmmo ? maxAmmo - amount :
                if (currentAmmo == 0 && !this.gun.getReloads().isOpenBolt()) {
                    if (ammoAmount < amount) {
                        tag.putInt("AmmoCount", currentAmmo + ammoAmount);
                        shrinkFromAmmoPool(ammoStacks, player, ammoAmount);
                    } else {
                        tag.putInt("AmmoCount", maxAmmo - 1);
                        shrinkFromAmmoPool(ammoStacks, player, amount - 1);
                    }
                } else {
                    if (ammoAmount < amount) {
                        tag.putInt("AmmoCount", currentAmmo + ammoAmount);
                        shrinkFromAmmoPool(ammoStacks, player, ammoAmount);
                    } else {
                        tag.putInt("AmmoCount", maxAmmo);
                        shrinkFromAmmoPool(ammoStacks, player, amount);//ammoStacks.shrink(amount);
                    }
                }
            }
        }
        setReloadSound(player);
    }

    private void setReloadSound(PlayerEntity player) {
        ResourceLocation reloadSound = this.gun.getSounds().getReload();
        playReloadSound(player, reloadSound);
    }

    public static void playReloadSound(PlayerEntity player, ResourceLocation reloadSound) {
        sendReload(player, reloadSound);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.player.world.isRemote) {
            PlayerEntity player = event.player;
            if (SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING)) {
                if (!RELOAD_TRACKER_MAP.containsKey(player)) {
                    if (!(player.inventory.getCurrentItem().getItem() instanceof GunItem)) {
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, false);
                        return;
                    }
                    RELOAD_TRACKER_MAP.put(player, new ReloadTracker(player));
                }
                ReloadTracker tracker = RELOAD_TRACKER_MAP.get(player);
                if (!tracker.isSameWeapon(player) || tracker.isWeaponFull() || tracker.hasNoAmmo(player)) {
                    RELOAD_TRACKER_MAP.remove(player);
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, true);
                    return;
                }
                if (tracker.canReload(player)) {
                    final Gun gun = tracker.gun;
                    if (gun.getReloads().isMagFed()) {
                        tracker.increaseMagAmmo(player);
                        RELOAD_TRACKER_MAP.remove(player);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, false);
                    } else if (gun.getReloads().isStripperClip() && gun.getReloads().getMaxAmmo() - player.getHeldItemMainhand().getOrCreateTag().getInt("AmmoCount") >= gun.getReloads().getStripperClipReloadAmount()) {
                        tracker.increaseAmmoFromStripperClip(player);
                        RELOAD_TRACKER_MAP.remove(player);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                        SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, false);
                    } else {
                        tracker.increaseAmmo(player);
                        if (tracker.isWeaponFull() || tracker.hasNoAmmo(player) || gun.getReloads().isNoMag()) {
                            RELOAD_TRACKER_MAP.remove(player);
                            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.STOP_ANIMA, false);
                        }
                    }
                }
            } else RELOAD_TRACKER_MAP.remove(player);
        }
    }

    public static boolean isPlayerReload(PlayerEntity player) {
        return RELOAD_TRACKER_MAP.containsKey(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerEvent.PlayerLoggedOutEvent event) {
        MinecraftServer server = event.getPlayer().getServer();
        if (server != null) {
            server.execute(() -> RELOAD_TRACKER_MAP.remove(event.getPlayer()));
        }
    }
}
