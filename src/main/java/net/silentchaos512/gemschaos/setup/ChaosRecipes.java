package net.silentchaos512.gemschaos.setup;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.crafting.recipe.ApplyChaosRuneRecipe;
import net.silentchaos512.gemschaos.crafting.recipe.BlockCorruptingRecipe;
import net.silentchaos512.gemschaos.crafting.recipe.InfusingRecipe;

import java.util.function.Supplier;

public final class ChaosRecipes {
    public static final class Types {
        public static final IRecipeType<BlockCorruptingRecipe> BLOCK_CORRUPTING = register("block_corrupting");
        public static final IRecipeType<InfusingRecipe> INFUSING = register("infusing");

        private Types() {}

        private static <T extends IRecipe<?>> IRecipeType<T> register(String name) {
            return IRecipeType.register(ChaosMod.getId(name).toString());
        }
    }

    public static final RegistryObject<SpecialRecipeSerializer<?>> APPLY_CHAOS_RUNE = register("apply_chaos_rune", () -> new SpecialRecipeSerializer<>(ApplyChaosRuneRecipe::new));
    public static final RegistryObject<IRecipeSerializer<?>> BLOCK_CORRUPTING = register("block_corrupting", BlockCorruptingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> INFUSING = register("infusing", InfusingRecipe.Serializer::new);

    private static <T extends IRecipeSerializer<? extends IRecipe<?>>> RegistryObject<T> register(String name, Supplier<T> serializer) {
        return ChaosRegistration.RECIPE_SERIALIZERS.register(name, serializer);
    }

    private ChaosRecipes() {}

    public static void register() {}
}
