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
        public static final RegistryObject<RecipeType<BlockCorruptingRecipe>> BLOCK_CORRUPTING = register("block_corrupting");
        public static final RegistryObject<RecipeType<InfusingRecipe>> INFUSING = register("infusing");

        private Types() {}

        private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
            return ChaosRegistration.RECIPE_TYPES.register(name, () -> new RecipeType<T>() {
                @Override
                public String toString() {
                    return ChaosMod.MOD_ID + ":" + name;
                }
            });
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
