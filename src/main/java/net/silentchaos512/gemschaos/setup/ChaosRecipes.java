package net.silentchaos512.gemschaos.setup;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.crafting.recipe.ApplyChaosRuneRecipe;
import net.silentchaos512.gemschaos.crafting.recipe.BlockCorruptingRecipe;
import net.silentchaos512.gemschaos.crafting.recipe.InfusingRecipe;

import java.util.function.Supplier;

public final class ChaosRecipes {
    public static final class Types {
        public static final RecipeType<BlockCorruptingRecipe> BLOCK_CORRUPTING = register("block_corrupting");
        public static final RecipeType<InfusingRecipe> INFUSING = register("infusing");

        private Types() {}

        private static <T extends Recipe<?>> RecipeType<T> register(String name) {
            return RecipeType.register(ChaosMod.getId(name).toString());
        }
    }

    public static final RegistryObject<SimpleRecipeSerializer<?>> APPLY_CHAOS_RUNE = register("apply_chaos_rune", () -> new SimpleRecipeSerializer<>(ApplyChaosRuneRecipe::new));
    public static final RegistryObject<RecipeSerializer<?>> BLOCK_CORRUPTING = register("block_corrupting", BlockCorruptingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> INFUSING = register("infusing", InfusingRecipe.Serializer::new);

    private static <T extends RecipeSerializer<? extends Recipe<?>>> RegistryObject<T> register(String name, Supplier<T> serializer) {
        return ChaosRegistration.RECIPE_SERIALIZERS.register(name, serializer);
    }

    private ChaosRecipes() {}

    public static void register() {}
}
