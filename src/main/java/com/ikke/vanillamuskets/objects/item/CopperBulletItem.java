package com.ikke.vanillamuskets.objects.item;

import com.ikke.vanillamuskets.world.entity.projectiles.CopperBullet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CopperBulletItem extends IronBulletItem {

    public CopperBulletItem(Properties properties) {
        super(properties);
    }


    @Override
    public @NotNull CopperBullet createArrow(Level level, ItemStack stack, LivingEntity livingEntity) {
        CopperBullet copperBullet = new CopperBullet(level, livingEntity);
        copperBullet.setBaseDamage(3.5D);
        copperBullet.setKnockback(2);
        return copperBullet;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.literal("Copper bullet");
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.world.entity.player.Player player) {
        int enchant = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS, bow);
        return enchant <= 0 ? false : this.getClass() == CopperBulletItem.class;
    }
}
