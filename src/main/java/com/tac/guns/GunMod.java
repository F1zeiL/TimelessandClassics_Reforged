package com.tac.guns;

import com.tac.guns.client.ClientHandler;
import com.tac.guns.client.CustomGunManager;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.render.pose.*;
import com.tac.guns.common.BoundingBoxManager;
import com.tac.guns.common.GripType;
import com.tac.guns.common.ProjectileManager;
import com.tac.guns.common.attachments.Perks;
import com.tac.guns.common.tooling.CommandsHandler;
import com.tac.guns.common.tooling.CommandsManager;
import com.tac.guns.datagen.*;
import com.tac.guns.enchantment.EnchantmentTypes;
import com.tac.guns.entity.MissileEntity;
import com.tac.guns.init.*;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.network.PacketHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Locale;

@Mod(Reference.MOD_ID)
public class GunMod {
    public static boolean controllableLoaded = false;
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModBlocks.WORKBENCH.get());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    }.setRelevantEnchantmentTypes(EnchantmentTypes.GUN, EnchantmentTypes.SEMI_AUTO_GUN);

    public static final ItemGroup PISTOL = new ItemGroup("Pistols") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.GLOCK_17.get());
            stack.getOrCreateTag().putInt("AmmoCount", ((TimelessGunItem) ModItems.GLOCK_17.get()).getGun().getReloads().getMaxAmmo());
            /*ItemStack stack = new ItemStack(ModItems.M1911.get());
            stack.getOrCreateTag().putInt("AmmoCount", ModItems.M1911.get().getGun().getReloads().getMaxAmmo());*/
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup SMG = new ItemGroup("SMGs") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.VECTOR45.get());
            stack.getOrCreateTag().putInt("AmmoCount", ModItems.VECTOR45.get().getGun().getReloads().getMaxAmmo());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup RIFLE = new ItemGroup("AssaultRifles") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.AK47.get());
            stack.getOrCreateTag().putInt("AmmoCount", ModItems.AK47.get().getGun().getReloads().getMaxAmmo());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup SNIPER = new ItemGroup("MarksmanRifles") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.AI_AWP.get());
            stack.getOrCreateTag().putInt("AmmoCount", ((TimelessGunItem) ModItems.AI_AWP.get()).getGun().getReloads().getMaxAmmo());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup SHOTGUN = new ItemGroup("Shotguns") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.M870_CLASSIC.get());
            stack.getOrCreateTag().putInt("AmmoCount", ((TimelessGunItem) ModItems.M870_CLASSIC.get()).getGun().getReloads().getMaxAmmo());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup HEAVY_MATERIAL = new ItemGroup("HeavyWeapons") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.M60.get());
            stack.getOrCreateTag().putInt("AmmoCount", ModItems.M60.get().getGun().getReloads().getMaxAmmo());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup AMMO = new ItemGroup("Ammo") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.BULLET_308.get());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup EXPLOSIVES = new ItemGroup("Explosives") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.BASEBALL_GRENADE.get());
            //stack.getOrCreateTag().putInt("AmmoCount", ModItems.BASEBALL_GRENADE.get().getGun().getReloads().getMaxAmmo());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public static final ItemGroup SKINS = new ItemGroup("Skins") {
        @Override
        public ItemStack createIcon() {
            ItemStack stack = new ItemStack(ModItems.BLANK_SKIN_MOD_LVL1.get());
            return stack;
        }

        @Override
        public void fill(NonNullList<ItemStack> items) {
            super.fill(items);
            CustomGunManager.fill(items);
        }
    };

    public GunMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Do so right away, I want to make sure I hit this during Curios load
        ModBlocks.REGISTER.register(bus);
        ModContainers.REGISTER.register(bus);
        ModEffects.REGISTER.register(bus);
        ModEnchantments.REGISTER.register(bus);
        ModEntities.REGISTER.register(bus);
        ModItems.REGISTER.register(bus);
        ModParticleTypes.REGISTER.register(bus);
        ModRecipeSerializers.REGISTER.register(bus);
        ModSounds.REGISTER.register(bus);
        ModTileEntities.REGISTER.register(bus);
        ModSyncedDataKeys.register();
        ModTags.init();
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
        bus.addListener(this::dataSetup);
        controllableLoaded = ModList.get().isLoaded("controllable");
        modInfo = ModLoadingContext.get().getActiveContainer().getModInfo();

        Perks.init();
    }

    public static IModInfo modInfo = null;

    private void onCommonSetup(FMLCommonSetupEvent event) {
        // Map projectile items to entities and renderer's
//        ProjectileManager.getInstance().registerFactory(ModItems.GRENADE.get(), (worldIn, entity, weapon, item, modifiedGun) -> new GrenadeEntity(ModEntities.GRENADE.get(), worldIn, entity, weapon, item, modifiedGun));
        //TODO: Reimplement rocket and grenade based weapons, weapon trails must be disabled for these as well, aka requiring reimplementation
        ProjectileManager.getInstance().registerFactory(ModItems.RPG7_MISSILE.get(), (worldIn, entity, weapon, item, modifiedGun, randP, randY) -> new MissileEntity(ModEntities.RPG7_MISSILE.get(), worldIn, entity, weapon, item, modifiedGun));
        //ProjectileManager.getInstance().registerFactory(ModItems.GRENADE_40MM.get(), (worldIn, entity, weapon, item, modifiedGun, randP, randY) -> new GrenadeEntity(ModEntities.GRENADE.get(), worldIn, entity, weapon, item, modifiedGun)); //, 1.5F

        // Register all custom networking
        PacketHandler.init();

        // Updated hitboxes for better serverside feel
        if (Config.COMMON.gameplay.improvedHitboxes.get()) {
            MinecraftForge.EVENT_BUS.register(new BoundingBoxManager());
        }

        GripType.registerType(new GripType(new ResourceLocation("tac", "one_handed_m1911"), new OneHandedPoseHighRes_m1911()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "one_handed_m1851"), new OneHandedPoseHighRes_m1851()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "two_handed_m1894"), new TwoHandedPoseHighRes_m1894()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "two_handed_m1928"), new TwoHandedPoseHighRes_m1928()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "two_handed_mosin"), new TwoHandedPoseHighRes_mosin()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "two_handed_ak47"), new TwoHandedPoseHighRes_ak47()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "two_handed_m60"), new TwoHandedPoseHighRes_m60()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "two_handed_vector"), new TwoHandedPoseHighRes_vector()));
        GripType.registerType(new GripType(new ResourceLocation("tac", "one_handed_m1873"), new OneHandedPoseHighRes_m1873()));

        // Custom commands handlers, called in common so any future server side command registry would be useable
        MinecraftForge.EVENT_BUS.register(CommandsManager.class);
        MinecraftForge.EVENT_BUS.register(CommandsHandler.class);
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        BlockTagGen blockTagGen = new BlockTagGen(dataGenerator, existingFileHelper);
        dataGenerator.addProvider(new RecipeGen(dataGenerator));
        dataGenerator.addProvider(new LootTableGen(dataGenerator));
        dataGenerator.addProvider(blockTagGen);
        dataGenerator.addProvider(new ItemTagGen(dataGenerator, blockTagGen, existingFileHelper));
        dataGenerator.addProvider(new LanguageGen(dataGenerator));
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        // Too much to keep in Gunmod file
        ClientHandler.setup(event.getMinecraftSupplier().get());


        // Auto register code animation files, such as firing, animation mapping is called in these files too
        for (Field field : ModItems.class.getDeclaredFields()) {
            RegistryObject<?> object;
            try {
                object = (RegistryObject<?>) field.get(null);
            } catch (ClassCastException | IllegalAccessException e) {
                continue;
            }
            if (TimelessGunItem.class.isAssignableFrom(object.get().getClass())) {
                try {
                    ModelOverrides.register(
                            (Item) object.get(),
                            (IOverrideModel) Class.forName("com.tac.guns.client.render.gun.model." + field.getName().toLowerCase(Locale.ENGLISH) + "_animation").newInstance()
                    );
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("Could not load animations for gun - " + field.getName());
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
