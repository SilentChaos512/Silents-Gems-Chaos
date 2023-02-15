package net.silentchaos512.gemschaos.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;

public class InfusingRecipe extends SingleItemRecipe {
    private final Ingredient catalyst;
    private int chaosPerTick = 5000;
    private int processTime = 200;

    public InfusingRecipe(ResourceLocation id, Ingredient ingredient, Ingredient catalyst, ItemStack result) {
        super(ChaosRecipes.INFUSING_TYPE.get(), ChaosRecipes.INFUSING.get(), id, "", ingredient, result);
        this.catalyst = catalyst;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        ItemStack inputItem = inv.getItem(0);
        ItemStack catalystItem = inv.getItem(1);
        return this.ingredient.test(inputItem) && this.catalyst.test(catalystItem);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.ingredient);
        list.add(this.catalyst);
        return list;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    public int getChaosPerTick() {
        return chaosPerTick;
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<InfusingRecipe> {
        @Override
        public InfusingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            Ingredient catalyst = Ingredient.fromJson(json.get("catalyst"));

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "result")));
            int count = GsonHelper.getAsInt(json, "count", 1);
            ItemStack resultStack = new ItemStack(item, count);

            InfusingRecipe recipe = new InfusingRecipe(recipeId, ingredient, catalyst, resultStack);
            recipe.chaosPerTick = GsonHelper.getAsInt(json, "chaosPerTick", recipe.chaosPerTick);
            recipe.processTime = GsonHelper.getAsInt(json, "processTime", recipe.processTime);
            return recipe;
        }

        @Override
        public InfusingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient catalyst = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            InfusingRecipe recipe = new InfusingRecipe(recipeId, ingredient, catalyst, result);
            recipe.chaosPerTick = buffer.readVarInt();
            recipe.processTime = buffer.readVarInt();
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, InfusingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.catalyst.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.chaosPerTick);
            buffer.writeVarInt(recipe.processTime);
        }
    }
}
