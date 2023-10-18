package com.ikke.vanillamuskets.client;


import com.ikke.vanillamuskets.VanillaMuskets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VanillaMusketsSoundEvents  {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,VanillaMuskets.MODID);
    public static final RegistryObject<SoundEvent>  LOADING3 = registerSoundEvent("loading3");
    public static final RegistryObject<SoundEvent>  SHOOT1 = registerSoundEvent("shoot1");


    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
    return SOUND_EVENTS.register(name,() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(VanillaMuskets.MODID,name)));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
