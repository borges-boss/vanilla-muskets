package com.ikke.vanillamuskets.world.entity.projectiles;

import com.ikke.vanillamuskets.VanillaMuskets;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class CopperBullet extends IronBullet {

    private Item referenceItem;

    public CopperBullet(EntityType<? extends CopperBullet> type, Level level) {
        super(VanillaMuskets.COPPER_BULLET.get(), level);
        this.referenceItem = VanillaMuskets.COPPER_BULLET_ITEM.get();
    }

    public CopperBullet(Level level, LivingEntity livingEntity) {
        super(level, livingEntity);
        this.referenceItem = VanillaMuskets.COPPER_BULLET_ITEM.get();
    }

    public CopperBullet(EntityType<? extends IronBullet> type, LivingEntity shooter, Level level, Item referenceItem) {
        super(VanillaMuskets.COPPER_BULLET.get(), shooter, level, referenceItem);
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
