package net.silentchaos512.gemschaos.setup;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.crafting.recipe.ApplyChaosRuneRecipe;

import java.util.function.Supplier;

public final class ChaosRecipes {
    public static final RegistryObject<SpecialRecipeSerializer<?>> APPLY_CHAOS_RUNE = registerSerializer("apply_chaos_rune", () ->
            new SpecialRecipeSerializer<>(ApplyChaosRuneRecipe::new));

    private ChaosRecipes() {}

    public static void register() {}

    private static <T extends IRecipeSerializer<? extends IRecipe<?>>> RegistryObject<T> registerSerializer(String name, Supplier<T> serializer) {
        return ChaosRegistration.RECIPE_SERIALIZERS.register(name, serializer);
    }

    private static void registerRecipeType(String name, IRecipeType<?> recipeType) {
        Registry.register(Registry.RECIPE_TYPE, ChaosMod.getId(name), recipeType);
    }
}
