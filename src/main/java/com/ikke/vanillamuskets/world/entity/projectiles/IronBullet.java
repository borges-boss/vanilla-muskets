package com.ikke.vanillamuskets.world.entity.projectiles;

import com.ikke.vanillamuskets.VanillaMuskets;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class IronBullet extends AbstractArrow {
    private Item referenceItem;

    public IronBullet(EntityType<? extends IronBullet> type, Level level) {
        super(VanillaMuskets.IRON_BULLET.get(), level);
        this.referenceItem = VanillaMuskets.IRON_BULLET_ITEM.get();
    }

    public IronBullet(Level level,LivingEntity livingEntity) {
        super(VanillaMuskets.IRON_BULLET.get(),livingEntity, level);
        this.referenceItem = VanillaMuskets.IRON_BULLET_ITEM.get();
    }

    public IronBullet(EntityType<? extends IronBullet> type, LivingEntity shooter, Level level, Item referenceItem) {
        super(type, shooter, level);
        this.referenceItem = referenceItem;
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
