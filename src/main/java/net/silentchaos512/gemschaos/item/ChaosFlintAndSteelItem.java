package net.silentchaos512.gemschaos.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gemschaos.api.ChaosApi;
import net.silentchaos512.gemschaos.crafting.recipe.BlockCorruptingRecipe;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;
import net.silentchaos512.lib.util.WorldUtils;

import java.util.Optional;

public class ChaosFlintAndSteelItem extends FlintAndSteelItem {
    public ChaosFlintAndSteelItem(Properties builder) {
        super(builder);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState blockstate = world.getBlockState(pos);

        Item blockItem = blockstate.getBlock().asItem();
        BlockCorruptingRecipe recipe = world.getRecipeManager().getRecipe(ChaosRecipes.Types.BLOCK_CORRUPTING, new Inventory(new ItemStack(blockItem)), world).orElse(null);

        if (recipe != null) {
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);

            // TODO: corrupt a spherical patch of blocks
            WorldUtils.getBlocksInSphere(world, pos, 2, (w, p1) -> {
                if (recipe.matches(w.getBlockState(p1))) {
                    return Optional.of(w.getBlockState(p1));
                }
                return Optional.empty();
            }).forEach((p, s) -> world.setBlockState(p, recipe.getResultBlock(s), 11));

            ChaosApi.Chaos.dissipate(player, recipe.getChaosDissipated());

            if (player instanceof ServerPlayerEntity) {
                ItemStack itemstack = context.getItem();
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, itemstack);
                itemstack.damageItem(1, player, (playerIn) -> {
                    playerIn.sendBreakAnimation(context.getHand());
                });
            }

            return ActionResultType.func_233537_a_(world.isRemote());
        } else {
            return ActionResultType.FAIL;
        }
    }
}
