package com.ikke.vanillamuskets.client.renderer.entity;

import com.ikke.vanillamuskets.VanillaMuskets;
import com.ikke.vanillamuskets.world.entity.projectiles.IronBullet;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class IronBulletRenderer extends ArrowRenderer<IronBullet> {

    public IronBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull IronBullet pEntity) {
        return new ResourceLocation(VanillaMuskets.MODID,"textures/entity/projectiles/iron_bullet.png");
    }
}
