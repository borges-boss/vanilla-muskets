package com.ikke.vanillamuskets.world.entity.projectiles;

import com.ikke.vanillamuskets.VanillaMuskets;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnderBullet extends AbstractArrow {

    private Item referenceItem;
    private final Level level;
    private LivingEntity player;

    private static final List<EnderMan> enderMen = new ArrayList<>();

    public EnderBullet(EntityType<? extends EnderBullet> type, Level level) {
        super(VanillaMuskets.ENDER_BULLET.get(), level);
        this.level = level;
        this.referenceItem = VanillaMuskets.ENDER_BULLET_ITEM.get();
    }

    public EnderBullet(Level level,LivingEntity livingEntity) {
        super(VanillaMuskets.ENDER_BULLET.get(),livingEntity, level);
        this.level = level;
        this.referenceItem = VanillaMuskets.ENDER_BULLET_ITEM.get();
        this.player = livingEntity;
    }

    public EnderBullet(EntityType<? extends IronBullet> type, LivingEntity shooter, Level level, Item referenceItem) {
        super(type, shooter, level);
        this.level = level;
        this.referenceItem = referenceItem;
        this.player = shooter;
    }



    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        boolean flag = entityHitResult.getEntity().getType() == EntityType.ENDERMAN;

        boolean isEnderDragon = entityHitResult.getEntity().getType() == EntityType.ENDER_DRAGON;

        if(flag) {
            double x = entityHitResult.getLocation().x;
            double y = entityHitResult.getLocation().y;
            double z = entityHitResult.getLocation().z;

            EnderMan enderMan = (EnderMan) entityHitResult.getEntity();
            if(enderMan.getHealth() > 0 && !enderMen.contains(enderMan)) {
                enderMen.add(enderMan);

            if(enderMan.isUsingItem()) {
                enderMan.stopUsingItem();
            }

            enderMan.hurt((DamageSource) new DamageSources(this.getCommandSenderWorld().registryAccess()).fall(),1.0F);


            //Freezes the EnderMan in place
            enderMan.teleportTo(x,y,z);
            enderMan.playSound(SoundEvents.ENDERMAN_TELEPORT);
            enderMan.getNavigation().stop();
            enderMan.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,4000,200));
            enderMan.setBeingStaredAt();
            enderMan.startPersistentAngerTimer();

            new Thread(() -> {
                for(int i = 0;i < 4;i++) {
                    enderMan.getNavigation().stop();
                    enderMan.teleportTo(x, y, z);
                    enderMan.playSound(SoundEvents.ENDERMAN_TELEPORT);
                    enderMan.playSound(SoundEvents.ENDERMAN_SCREAM);


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                enderMan.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE);
                enderMan.kill();
                enderMen.remove(enderMan);
                Thread.currentThread().interrupt();
            }).start();

            }
        }
        else if(isEnderDragon) {
            EnderDragon dragon = (EnderDragon) entityHitResult.getEntity();
            dragon.hurt((DamageSource) new DamageSources(this.getCommandSenderWorld().registryAccess()).magic(),50.0F);
            dragon.playSound(SoundEvents.CREEPER_PRIMED);
        }
    }


    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        this.discard();
    }


    @Override
    public @NotNull ItemStack getPickupItem() {
        return new ItemStack(this.referenceItem);
    }


}
