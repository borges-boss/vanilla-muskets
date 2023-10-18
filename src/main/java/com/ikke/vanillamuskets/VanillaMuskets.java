package com.ikke.vanillamuskets;

import com.ikke.vanillamuskets.objects.item.CopperBulletItem;
import com.ikke.vanillamuskets.objects.item.EnderBulletItem;
import com.ikke.vanillamuskets.objects.item.IronBulletItem;
import com.ikke.vanillamuskets.objects.item.Musket;
import com.ikke.vanillamuskets.world.entity.projectiles.EnderBullet;
import com.ikke.vanillamuskets.world.entity.projectiles.IronBullet;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

//To build your mod, run gradlew build
//To run your mod, run gradlew runClient
//To run your mod in dedicated server, run gradlew runServer
// The value here should match an entry in the META-INF/mods.toml file
@Mod(VanillaMuskets.MODID)
public class VanillaMuskets
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "vanillamuskets";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,MODID);

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,MODID);

    public static final RegistryObject<SoundEvent> SHOOT1 = SOUND_EVENTS.register("shoot1",() -> new SoundEvent(new ResourceLocation(MODID,"shoot1")));

    public static final RegistryObject<SoundEvent> LOADING3 = SOUND_EVENTS.register("loading3",() -> new SoundEvent(new ResourceLocation(MODID,"loading3")));
    //Wooden Musket item
    public static final RegistryObject<Item> MUSKET_ITEM = ITEMS.register("musket",() -> new Musket(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).durability(3000)));
    public static final RegistryObject<Item> IRON_BULLET_ITEM = ITEMS.register("iron_bullet",() -> new IronBulletItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(64).setNoRepair()));
    public static final RegistryObject<Item> COPPER_BULLET_ITEM = ITEMS.register("copper_bullet",() -> new CopperBulletItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(64).setNoRepair()));
    public static final RegistryObject<Item> ENDER_BULLET_ITEM = ITEMS.register("ender_bullet",() -> new EnderBulletItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(64).setNoRepair()));

    public static final RegistryObject<EntityType<IronBullet>> IRON_BULLET = ENTITIES.register("iron_bullet",() -> EntityType.Builder.<IronBullet>of(IronBullet::new,MobCategory.MISC).sized(0.1F,0.1F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(MODID,"iron_bullet").toString()));
    public static final RegistryObject<EntityType<IronBullet>> COPPER_BULLET = ENTITIES.register("copper_bullet",() -> EntityType.Builder.<IronBullet>of(IronBullet::new,MobCategory.MISC).sized(0.1F,0.1F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(MODID,"copper_bullet").toString()));

    public static final RegistryObject<EntityType<EnderBullet>> ENDER_BULLET = ENTITIES.register("ender_bullet",() -> EntityType.Builder.<EnderBullet>of(EnderBullet::new,MobCategory.MISC).sized(0.1F,0.1F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(MODID,"ender_bullet").toString()));


    public VanillaMuskets()
    {
        //   public static final EntityType<Arrow> ARROW = register("arrow", EntityType.Builder.<Arrow>of(Arrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        SOUND_EVENTS.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        ENTITIES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
