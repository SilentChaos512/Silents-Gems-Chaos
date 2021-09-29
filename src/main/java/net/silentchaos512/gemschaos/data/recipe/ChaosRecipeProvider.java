package net.silentchaos512.gemschaos.data.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.api.data.recipe.BlockCorruptingRecipeBuilder;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;
import net.silentchaos512.gemschaos.setup.ChaosItems;
import net.silentchaos512.gemschaos.setup.ChaosTags;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;

import java.util.function.Consumer;

public class ChaosRecipeProvider extends LibRecipeProvider {
    public ChaosRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, ChaosMod.MOD_ID);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        registerOresAndCrystals(consumer);
        registerBlockCorrupting(consumer);
    }

    private void registerOresAndCrystals(Consumer<FinishedRecipe> consumer) {
        smeltingAndBlastingRecipes(consumer, "chaos_crystal", ChaosBlocks.CHAOS_ORE, ChaosItems.CHAOS_CRYSTAL, 1.0f);

        compressionRecipes(consumer, ChaosBlocks.CHAOS_CRYSTAL_BLOCK, ChaosItems.CHAOS_CRYSTAL, null);
    }

    private void registerBlockCorrupting(Consumer<FinishedRecipe> consumer) {
        BlockCorruptingRecipeBuilder.builder(Tags.Items.STONE, ChaosBlocks.CORRUPTED_STONE, 5_000).build(consumer);
        BlockCorruptingRecipeBuilder.builder(ChaosTags.Items.DIRT, ChaosBlocks.CORRUPTED_DIRT, 5_000).build(consumer);
    }
}
