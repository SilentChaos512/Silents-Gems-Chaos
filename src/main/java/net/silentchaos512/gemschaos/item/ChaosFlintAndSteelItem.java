package net.silentchaos512.gemschaos.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(pos);

        Item blockItem = blockstate.getBlock().asItem();
        BlockCorruptingRecipe recipe = world.getRecipeManager().getRecipeFor(ChaosRecipes.Types.BLOCK_CORRUPTING, new SimpleContainer(new ItemStack(blockItem)), world).orElse(null);

        if (recipe != null) {
            world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);

            // TODO: corrupt a spherical patch of blocks
            WorldUtils.getBlocksInSphere(world, pos, 2, (w, p1) -> {
                if (recipe.matches(w.getBlockState(p1))) {
                    return Optional.of(w.getBlockState(p1));
                }
                return Optional.empty();
            }).forEach((p, s) -> world.setBlock(p, recipe.getResultBlock(s), 11));

            ChaosApi.Chaos.dissipate(player, recipe.getChaosDissipated());

            if (player instanceof ServerPlayer) {
                ItemStack itemstack = context.getItemInHand();
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, itemstack);
                itemstack.hurtAndBreak(1, player, (playerIn) -> {
                    playerIn.broadcastBreakEvent(context.getHand());
                });
            }

            return InteractionResult.sidedSuccess(world.isClientSide());
        } else {
            return InteractionResult.FAIL;
        }
    }
}
