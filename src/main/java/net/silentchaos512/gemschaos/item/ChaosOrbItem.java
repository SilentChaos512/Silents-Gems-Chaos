package net.silentchaos512.gemschaos.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.silentchaos512.gemschaos.ChaosMod;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class ChaosOrbItem extends Item {
    public static final ResourceLocation CRACK_STAGE = ChaosMod.getId("crack_stage");

    private final int maxAbsorb;
    private final int crackStages;
    private final float leakage;

    public ChaosOrbItem(int crackStages, int maxAbsorb, float leakage) {
        super(new Properties()
                .tab(ChaosMod.ITEM_GROUP)
                .stacksTo(1)
                .defaultDurability(maxAbsorb)
                .setNoRepair()
        );

        this.maxAbsorb = maxAbsorb;
        this.crackStages = crackStages;
        this.leakage = leakage;
    }

    public static int absorbChaos(LivingEntity entity, ItemStack stack, int amount) {
        if (!(stack.getItem() instanceof ChaosOrbItem)) return 0;

        ChaosOrbItem item = (ChaosOrbItem) stack.getItem();
        int toAbsorb = getChaosToAbsorb(amount, item);
        int crackStage = getCrackStage(stack);

        // Absorb chaos amount minus leakage, destroy if durability depleted
        if (stack.hurt(toAbsorb, entity.getRandom(), entity instanceof ServerPlayer ? (ServerPlayer) entity : null)) {
            destroyOrb(entity, stack);
        }

        // If not destroyed, check crack stage and notify player
        if (!stack.isEmpty()) {
            int newCrackStage = getCrackStage(stack);
            if (newCrackStage != crackStage) {
                notifyOrbCracked(entity, stack);
            }
        }

        return amount - toAbsorb;
    }

    public static int absorbChaos(LevelAccessor world, BlockPos pos, ItemStack stack, int amount) {
        if (!(stack.getItem() instanceof ChaosOrbItem)) return 0;

        ChaosOrbItem item = (ChaosOrbItem) stack.getItem();
        int toAbsorb = getChaosToAbsorb(amount, item);
        int crackStage = getCrackStage(stack);

        // Absorb chaos amount minus leakage, destroy if durability depleted
        if (stack.hurt(toAbsorb, ChaosMod.RANDOM, null)) {
            destroyOrb(world, pos, stack);
        }

        // If not destroyed, check crack stage and play sound
        if (!stack.isEmpty()) {
            int newCrackStage = getCrackStage(stack);
            if (newCrackStage != crackStage) {
                playBreakSound(world, pos);
            }
        }

        return amount - toAbsorb;
    }

    private static int getChaosToAbsorb(int amount, ChaosOrbItem item) {
        return (int) (amount * (1 - item.leakage));
    }

    private static void notifyOrbCracked(LivingEntity entity, ItemStack stack) {
        entity.sendMessage(ChaosMod.TEXT.translate("item", "chaos_orb.crack", stack.getHoverName()), Util.NIL_UUID);
        playCrackSound(entity.level, entity.blockPosition());
    }

    private static void destroyOrb(LivingEntity entity, ItemStack stack) {
        // Display name will be Air after we shrink, so get it now
        Component displayName = stack.getHoverName();
//        entity.renderBrokenItemStack(stack);
        entity.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_BREAK, entity.getSoundSource(), 0.8F, 0.8F + entity.level.random.nextFloat() * 0.4F, false);
        if (entity instanceof Player) {
            ((Player) entity).awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
        }
        destroyOrb(entity.level, entity.blockPosition(), stack);

        int pieceCount = entity.getRandom().nextInt(99000) + 1000;
        String piecesFormatted = String.format("%,d", pieceCount);
        entity.sendMessage(ChaosMod.TEXT.translate("item", "chaos_orb.break", displayName, piecesFormatted), Util.NIL_UUID);
    }

    private static void destroyOrb(LevelAccessor world, BlockPos pos, ItemStack stack) {
        stack.shrink(1);
        stack.setDamageValue(0);
        playBreakSound(world, pos);
    }

    private static void playCrackSound(LevelAccessor world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.AMBIENT, 0.6f, 1.5f);
    }

    private static void playBreakSound(LevelAccessor world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.AMBIENT, 0.7f, -2.5f);
    }

    public static int getChaosAbsorbed(ItemStack stack) {
        return stack.getDamageValue();
    }

    public static int getCrackStage(ItemStack stack) {
        if (!(stack.getItem() instanceof ChaosOrbItem)) return 0;

        ChaosOrbItem item = (ChaosOrbItem) stack.getItem();
        float ratio = (float) getChaosAbsorbed(stack) / item.maxAbsorb;
        return Mth.clamp((int) (ratio * (item.crackStages + 1)), 0, item.crackStages);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(ChaosMod.TEXT.translate("item", "chaos_orb.leakage", (int) (100 * this.leakage)));
    }
}
