package com.tac.guns.init;

import com.tac.guns.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds 
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);
	// Register specialty sounds that are used more-or-less universally
	public static final RegistryObject<SoundEvent> ITEM_GRENADE_PIN = register("item.grenade.pin");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_EXPLOSION = register("stun.explosion");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_RING = register("stun.ring");
	public static final RegistryObject<SoundEvent> UI_WEAPON_ATTACH = register("ui.weapon.attach");
	public static final RegistryObject<SoundEvent> BARREL_WHINE = register("item.barrel_whine");

	public static final RegistryObject<SoundEvent> M1_PING = register("item.m1ping");

	//public static final RegistryObject<SoundEvent> COCK = register("item.pistol.cock");
	public static final RegistryObject<SoundEvent> HEADSHOT_EXTENDED_PLAYFUL = register("item.headshot");

	private static RegistryObject<SoundEvent> register(String key)
	{
		return REGISTER.register(key, () -> new SoundEvent(new ResourceLocation(Reference.MOD_ID, key)));
	}
}
