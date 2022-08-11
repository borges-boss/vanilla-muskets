package com.ikke.vanillamuskets.client;

import com.ikke.vanillamuskets.client.renderer.entity.CopperBulletRenderer;
import com.ikke.vanillamuskets.client.renderer.entity.EnderBulletRenderer;
import com.ikke.vanillamuskets.client.renderer.entity.IronBulletRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.ikke.vanillamuskets.VanillaMuskets.*;


@Mod.EventBusSubscriber(modid = MODID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientModEventSubscriber {

    @SubscribeEvent
    public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
      event.registerEntityRenderer(IRON_BULLET.get(),IronBulletRenderer::new);
      event.registerEntityRenderer(COPPER_BULLET.get(), CopperBulletRenderer::new);
      event.registerEntityRenderer(ENDER_BULLET.get(), EnderBulletRenderer::new);
    }
}
