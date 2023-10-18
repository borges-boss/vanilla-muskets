package com.ikke.vanillamuskets.objects.item;

import com.ikke.vanillamuskets.world.entity.projectiles.EnderBullet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class EnderBulletItem extends ArrowItem {


    public EnderBulletItem(Properties p_40512_) {
        super(p_40512_);
    }

    @Override
    public @NotNull EnderBullet createArrow(Level level, ItemStack stack, LivingEntity livingEntity) {
        EnderBullet enderBullet = new EnderBullet(level, livingEntity);
        enderBullet.setBaseDamage(1.0D);
        enderBullet.setKnockback(0);
        return enderBullet;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> componentList, TooltipFlag p_41424_) {
        componentList.add(Component.literal("Effective against the Endermen").withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC));
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.world.entity.player.Player player) {
        int enchant = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS, bow);
        return enchant <= 0 ? false : this.getClass() == EnderBulletItem.class;
    }
}
