package com.ikke.vanillamuskets.objects.item;

import com.google.common.collect.Lists;
import com.ikke.vanillamuskets.client.VanillaMusketsSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.ikke.vanillamuskets.VanillaMuskets.*;

public class Musket extends CrossbowItem {
    private RandomSource random = RandomSource.create();
    public static final Predicate<ItemStack> IRON_BULLET = (stack) -> stack.is(IRON_BULLET_ITEM.get());

    public static final Predicate<ItemStack> ALL_BULLETS = IRON_BULLET.or((p_43015_) -> {
        return p_43015_.is(COPPER_BULLET_ITEM.get()) || p_43015_.is(ENDER_BULLET_ITEM.get());
    });

    public static final Predicate<ItemStack> ALL_BULLETS2 = IRON_BULLET.or((p_43015_) -> {
        return p_43015_.is(COPPER_BULLET_ITEM.get());
    });
    public Musket(Properties p_43009_) {
        super(p_43009_);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ALL_BULLETS;
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return ALL_BULLETS;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 5;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    public static boolean isCharged(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null && compoundtag.getBoolean("Charged");
    }

    public static void setCharged(ItemStack stack, boolean p_40886_) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putBoolean("Charged", p_40886_);
    }

    private static void clearChargedProjectiles(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 9);
            listtag.clear();
            compoundtag.put("ChargedProjectiles", listtag);
        }

    }

    private static void onMusketShot(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity instanceof ServerPlayer serverplayer) {
            if (!level.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, stack);
            }

            serverplayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }

        clearChargedProjectiles(stack);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack stack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null && compoundtag.contains("ChargedProjectiles", 9)) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 10);
            if (listtag != null) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    list.add(ItemStack.of(compoundtag1));
                }
            }
        }

        return list;
    }

    private static float getRandomShotPitch(boolean p_220026_, RandomSource p_220027_) {
        float f = p_220026_ ? 0.63F : 0.43F;
        return 1.0F / (p_220027_.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static float[] getShotPitches(RandomSource p_220024_) {
        boolean flag = p_220024_.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, p_220024_), getRandomShotPitch(!flag, p_220024_)};
    }

    private static void addChargedProjectile(ItemStack p_40929_, ItemStack p_40930_) {
        CompoundTag compoundtag = p_40929_.getOrCreateTag();
        ListTag listtag;
        if (compoundtag.contains("ChargedProjectiles", 9)) {
            listtag = compoundtag.getList("ChargedProjectiles", 10);
        } else {
            listtag = new ListTag();
        }

        CompoundTag compoundtag1 = new CompoundTag();
        p_40930_.save(compoundtag1);
        listtag.add(compoundtag1);
        compoundtag.put("ChargedProjectiles", listtag);
    }

    private static boolean loadProjectile(LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack1, boolean p_40866_, boolean p_40867_) {
        if (itemStack1.isEmpty()) {
            return false;
        } else {
            boolean flag = p_40867_ && itemStack1.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !p_40867_ && !p_40866_) {
                itemstack = itemStack1.split(1);
                if (itemStack1.isEmpty() && livingEntity instanceof Player) {
                    ((Player)livingEntity).getInventory().removeItem(itemStack1);
                }
            } else {
                itemstack = itemStack1.copy();
            }

            addChargedProjectile(itemStack, itemstack);
            return true;
        }
    }

    @Override
    public int getUseDuration(ItemStack p_40938_) {
        return getChargeDuration(p_40938_) + 3;
    }

    public static int getChargeDuration(ItemStack p_40940_) {

        //Reloading time
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, p_40940_);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int p_40878_) {
        if (!isCharged(stack) && tryLoadProjectiles(livingEntity, stack)) {
            setCharged(stack, true);
            SoundSource soundsource = livingEntity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), VanillaMusketsSoundEvents.LOADING3.get(), soundsource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }


    private static boolean tryLoadProjectiles(LivingEntity livingEntity, ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
        int j = i == 0 ? 1 : 3;
        boolean flag = livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild;
        ItemStack itemstack = livingEntity.getProjectile(stack);
        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(IRON_BULLET_ITEM.get());
                itemstack1 = itemstack.copy();
            }

            if (!loadProjectile(livingEntity, stack, itemstack, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    private static AbstractArrow getIronBullet(Level p_40915_, LivingEntity p_40916_, ItemStack p_40917_, ItemStack p_40918_) {
        IronBulletItem ironBulletItem = (IronBulletItem)(p_40918_.getItem() instanceof IronBulletItem ? p_40918_.getItem() : IRON_BULLET_ITEM.get());
        AbstractArrow abstractarrow = ironBulletItem.createArrow(p_40915_, p_40918_, p_40916_);

        abstractarrow.setShotFromCrossbow(true);

        abstractarrow.setSoundEvent(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, p_40917_);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte)i);
        }

        return abstractarrow;
    }


    private static AbstractArrow getCopperBullet(Level p_40915_, LivingEntity p_40916_, ItemStack p_40917_, ItemStack p_40918_) {
        CopperBulletItem copperBulletItem = (CopperBulletItem)(p_40918_.getItem() instanceof CopperBulletItem ? p_40918_.getItem() : COPPER_BULLET_ITEM.get());
        AbstractArrow abstractarrow = copperBulletItem.createArrow(p_40915_, p_40918_, p_40916_);

        abstractarrow.setShotFromCrossbow(true);
        abstractarrow.setSoundEvent(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, p_40917_);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte)i);
        }

        return abstractarrow;
    }

    private static AbstractArrow getEnderBullet(Level p_40915_, LivingEntity p_40916_, ItemStack p_40917_, ItemStack p_40918_) {
        EnderBulletItem enderBulletItem = (EnderBulletItem)(p_40918_.getItem() instanceof EnderBulletItem ? p_40918_.getItem() : ENDER_BULLET_ITEM.get());
        AbstractArrow abstractarrow = enderBulletItem.createArrow(p_40915_, p_40918_, p_40916_);

        abstractarrow.setShotFromCrossbow(true);
        abstractarrow.setSoundEvent(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, p_40917_);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte)i);
        }

        return abstractarrow;
    }

    private static void shootProjectile(Level level, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack p_40898_, ItemStack stack, float p_40900_, boolean p_40901_, float p_40902_, float p_40903_, float p_40904_) {
        if (!level.isClientSide) {
            boolean flag = stack.is(IRON_BULLET_ITEM.get());
            AbstractArrow projectile;
           if(stack.is(IRON_BULLET_ITEM.get())) {
               projectile = getIronBullet(level,livingEntity,p_40898_,stack);
           }
           else if(stack.is(ENDER_BULLET_ITEM.get())) {
               projectile = getEnderBullet(level, livingEntity, p_40898_, stack);
           }
           else
               projectile = getCopperBullet(level, livingEntity, p_40898_, stack);

           //Disable the pickup
            projectile.pickup = AbstractArrow.Pickup.DISALLOWED;

            if (livingEntity instanceof CrossbowAttackMob crossbowattackmob) {
                crossbowattackmob.shootCrossbowProjectile(Objects.requireNonNull(crossbowattackmob.getTarget()), p_40898_, projectile, p_40904_);
            } else {
                Vec3 vec31 = livingEntity.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(p_40904_ * ((float)Math.PI / 180F)), vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = livingEntity.getViewVector(1.0F);
                Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), p_40902_, p_40903_);
            }

            p_40898_.hurtAndBreak(flag ? 3 : 1, livingEntity, (p_40858_) -> {
                p_40858_.broadcastBreakEvent(interactionHand);
            });
            level.addFreshEntity(projectile);

            level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), VanillaMusketsSoundEvents.SHOOT1.get(), SoundSource.PLAYERS, 1.0F, p_40900_);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemstack = context.getItemInHand();
        Player player = context.getPlayer();

        if(context.getLevel().isClientSide() && isCharged(itemstack) && player!=null){
            for(int i = 0; i < 5; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                player.getLevel().addParticle(ParticleTypes.FLAME, player.getRandomX(1.0D) - 1.0D, player.getRandomY(), player.getRandomZ(1.0D), d0, d1, d2);
            }
        }
        return super.useOn(context);
    }

    public static void performShooting(Level level, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack stack, float p_40892_, float p_40893_) {
        if (livingEntity instanceof Player player && net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, livingEntity.level, player, 1, true) < 0) return;
        List<ItemStack> list = getChargedProjectiles(stack);
        float[] afloat = getShotPitches(livingEntity.getRandom());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild;
            if (!itemstack.isEmpty()) {
                if (i == 0) {
                    shootProjectile(level, livingEntity, interactionHand, stack, itemstack, afloat[i], flag, p_40892_, p_40893_, 0.0F);
                } else if (i == 1) {
                    shootProjectile(level, livingEntity, interactionHand, stack, itemstack, afloat[i], flag, p_40892_, p_40893_, -10.0F);
                } else if (i == 2) {
                    shootProjectile(level, livingEntity, interactionHand, stack, itemstack, afloat[i], flag, p_40892_, p_40893_, 10.0F);
                }
            }
        }

        onMusketShot(level, livingEntity, stack);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (isCharged(itemstack)) {
            performShooting(level, player, interactionHand, itemstack, 3.15F, 1.0F);
            setCharged(itemstack, false);
            if(level.isClientSide()){
                for(int i = 0; i < 10; ++i) {
                    double d0 = random.nextGaussian() * 0.02D;
                    double d1 = random.nextGaussian() * 0.02D;
                    double d2 = random.nextGaussian() * 0.02D;
                    player.getLevel().addParticle(ParticleTypes.LARGE_SMOKE, player.getRandomX(1.0D) - 0.4d, player.getRandomY(), player.getRandomZ(1.0D), d0, d1, d2);
                    player.getLevel().addParticle(ParticleTypes.FLAME, player.getRandomX(1.0D) - 0.8d, player.getRandomY(), player.getRandomZ(1.0D), d0, d1, d2);

                }
            }

            return InteractionResultHolder.consume(itemstack);
        } else if (!player.getProjectile(itemstack).isEmpty()) {
            if (!isCharged(itemstack)) {
                player.startUsingItem(interactionHand);
            }

            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }
}
