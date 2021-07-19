package net.silentchaos512.gemschaos.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;

public class InfusingRecipe extends SingleItemRecipe {
    private final Ingredient catalyst;
    private int chaosPerTick = 5000;
    private int processTime = 200;

    public InfusingRecipe(ResourceLocation id, Ingredient ingredient, Ingredient catalyst, ItemStack result) {
        super(ChaosRecipes.Types.INFUSING, ChaosRecipes.INFUSING.get(), id, "", ingredient, result);
        this.catalyst = catalyst;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        ItemStack inputItem = inv.getStackInSlot(0);
        ItemStack catalystItem = inv.getStackInSlot(1);
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
    public boolean isDynamic() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusingRecipe> {
        @Override
        public InfusingRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            Ingredient catalyst = Ingredient.deserialize(json.get("catalyst"));

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, "result")));
            int count = JSONUtils.getInt(json, "count", 1);
            ItemStack resultStack = new ItemStack(item, count);

            InfusingRecipe recipe = new InfusingRecipe(recipeId, ingredient, catalyst, resultStack);
            recipe.chaosPerTick = JSONUtils.getInt(json, "chaosPerTick", recipe.chaosPerTick);
            recipe.processTime = JSONUtils.getInt(json, "processTime", recipe.processTime);
            return recipe;
        }

        @Override
        public InfusingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            Ingredient catalyst = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            InfusingRecipe recipe = new InfusingRecipe(recipeId, ingredient, catalyst, result);
            recipe.chaosPerTick = buffer.readVarInt();
            recipe.processTime = buffer.readVarInt();
            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, InfusingRecipe recipe) {
            recipe.ingredient.write(buffer);
            recipe.catalyst.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeVarInt(recipe.chaosPerTick);
            buffer.writeVarInt(recipe.processTime);
        }
    }
}