package com.tac.guns.extra_events;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.event.LevelUpEvent;
import com.tac.guns.init.ModSounds;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.transition.M1GunItem;
import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.SGunLevelUp;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.Level;


/**
 * Author: ClumsyAlien
 */


@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TacEventListeners {

    /*
        A bit decent bit of extra code will be locked in external methods such as this, separating some of the standard and advanced
        Functions, especially in order to keep it all clean and allow easy backtracking, however both functions may receive changes
        For now as much of the work I can do will be kept externally such as with fire selection, and burst fire.
        (In short this serves as a temporary test bed to keep development on new functions on course)
    */

    private static boolean checked = true;
    private static boolean confirmed = false;
    private static VersionChecker.CheckResult status;

    @SubscribeEvent
    public static void InformPlayerOfUpdate(EntityJoinWorldEvent e) {
        try {
            if (!(e.getEntity() instanceof PlayerEntity))
                return;

            if (checked) {
                if (GunMod.modInfo != null) {
                    status = VersionChecker.getResult(GunMod.modInfo);
                    checked = false;
                }
            }
            if (!confirmed) {
                if (status.status == VersionChecker.Status.OUTDATED || status.status == VersionChecker.Status.BETA_OUTDATED) {
                    ((PlayerEntity) e.getEntity()).sendStatusMessage(new TranslationTextComponent("updateCheck.tac", status.target, status.url), false);
                    confirmed = true;
                }
            }
        } catch (Exception ev) {
            GunMod.LOGGER.log(Level.ERROR, ev.getMessage());
            return;
        }
        GunMod.LOGGER.log(Level.INFO, status.status);
    }

    @SubscribeEvent
    public static void onPartialLevel(LevelUpEvent.Post event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof TimelessGunItem) {
            int level = stack.getTag().getInt("level");
            player.getEntityWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.75F, 1.0F);
            PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SGunLevelUp(stack, level));
        }

    }

    // TODO: remaster method to play empty fire sound on most-all guns
    /* BTW this was by bomb787 as a Timeless Contributor */
    @SubscribeEvent
    public static void postShoot(GunFireEvent.Post event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!(heldItem.getItem() instanceof M1GunItem))
            return;
        CompoundNBT tag = heldItem.getTag();
        if (tag != null) {
            if (tag.getInt("AmmoCount") == 1)
                event.getPlayer().getEntityWorld().playSound(player, player.getPosition(), ModSounds.M1_PING.get()/*.GARAND_PING.get()*/, SoundCategory.MASTER, 3.0F, 1.0F);
        }
    }

    /*@SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void handleDeathWithArmor(LivingDeathEvent event)
    {
        if(event.getEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(WearableHelper.PlayerWornRig(player) != null)
            {
                GearSlotsHandler ammoItemHandler = (GearSlotsHandler) player.getCapability(ITEM_HANDLER_CAPABILITY).resolve().get();
                Block.spawnAsEntity(player.world, player.getPosition(), (ammoItemHandler.getStackInSlot(0)));
                Block.spawnAsEntity(player.world, player.getPosition(), (ammoItemHandler.getStackInSlot(1)));
            }
        }
        // TODO: Continue for dropping armor on a bot's death
    }*/

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity entity = event.player;
            boolean shooting = SyncedPlayerData.instance().get(entity, ModSyncedDataKeys.SHOOTING);
            ItemStack heldItem = entity.getHeldItemMainhand();

            if (!(heldItem.getItem() instanceof GunItem))
                return;

            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            if (gun.getReloads().isHeat()) {
                if (!Gun.hasAmmo(entity, heldItem) &&
                        ((!entity.isCreative() && !Config.SERVER.gameplay.commonUnlimitedCurrentAmmo.get()) ||
                                (entity.isCreative() && !Config.SERVER.gameplay.creativeUnlimitedCurrentAmmo.get()))) {
                    shooting = false;
                }

                if (shooting) {
                    if (heldItem.getTag() != null) {
                        if (gun.getReloads().isHeat() && heldItem.getTag().get("heatValue") != null) {
                            heldItem.getTag().putInt("heatValue", heldItem.getTag().getInt("heatValue") + 1);
                        }
                        if (heldItem.getTag().getInt("heatValue") >= gun.getReloads().getTickToHeat() && heldItem.getTag().get("overHeatLock") != null) {
                            if (!heldItem.getTag().getBoolean("overHeatLock")) {
                                heldItem.getTag().putInt("heatValue", heldItem.getTag().getInt("heatValue") + gun.getReloads().getTickOverHeat());
                                heldItem.getTag().putBoolean("overHeatLock", true);
                                entity.getEntityWorld().playSound(entity, entity.getPosition(), ModSounds.OVERHEAT.get(), SoundCategory.PLAYERS, (float) (Config.SERVER.barrelVolume.get() * 1F), 1.0F);
                            }
                        }
                    }
                } else {
                    if (heldItem.getItem() instanceof TimelessGunItem && heldItem.getTag() != null) {
                        if (gun.getReloads().isHeat() && heldItem.getTag().get("heatValue") != null) {
                            heldItem.getTag().putInt("heatValue", Math.max(heldItem.getTag().getInt("heatValue") - 1, 0));
                        }
                        if (heldItem.getTag().get("overHeatLock") != null) {
                            if (heldItem.getTag().getInt("heatValue") <= 0 && heldItem.getTag().getBoolean("overHeatLock"))
                                heldItem.getTag().putBoolean("overHeatLock", false);
                        }
                    }
                }

                if (heldItem.getTag() != null) {
                    if (overHeat(entity, heldItem))
                        if (heldItem.getTag().getInt("heatValue") >= gun.getReloads().getTickToHeat())
                            entity.sendStatusMessage(new TranslationTextComponent("info.tac.over_heat").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.RED), true);
                        else
                            entity.sendStatusMessage(new TranslationTextComponent(heldItem.getTag().getInt("heatValue") * 100 / gun.getReloads().getTickToHeat() + "% / 100%").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.RED), true);
                    else
                        entity.sendStatusMessage(new TranslationTextComponent("" + (heldItem.getTag().getInt("heatValue") * 100 / gun.getReloads().getTickToHeat()) + "% / 100%").mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.WHITE), true);
                }
            }
        }
    }

    private static boolean overHeat(PlayerEntity player, ItemStack heldItem) {
        if (heldItem.getItem() instanceof TimelessGunItem && !((TimelessGunItem) heldItem.getItem()).getGun().getReloads().isHeat())
            return false;

        return heldItem.getTag().getInt("heatValue") >= ((TimelessGunItem) heldItem.getItem()).getGun().getReloads().getTickToHeat() ||
                heldItem.getTag().getBoolean("overHeatLock");
    }
}
