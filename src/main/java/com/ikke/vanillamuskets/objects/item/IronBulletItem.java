package com.ikke.vanillamuskets.objects.item;

import com.ikke.vanillamuskets.world.entity.projectiles.IronBullet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IronBulletItem extends ArrowItem {

    public IronBulletItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull IronBullet createArrow(Level level, ItemStack stack, LivingEntity livingEntity) {
        IronBullet ironBullet = new IronBullet(level, livingEntity);
        ironBullet.setBaseDamage(6.5D);
        ironBullet.setKnockback(3);
        return ironBullet;
    }


    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.world.entity.player.Player player) {
        int enchant = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS, bow);
        return enchant <= 0 ? false : this.getClass() == IronBulletItem.class;
    }
}
