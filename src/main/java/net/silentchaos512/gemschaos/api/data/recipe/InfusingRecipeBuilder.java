package net.silentchaos512.gemschaos.api.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class InfusingRecipeBuilder {
    private final Item result;
    private final int count;
    private final int chaosPerTick;
    private final int processTime;
    private final Ingredient ingredient;
    private final Ingredient catalyst;

    public InfusingRecipeBuilder(Ingredient ingredient, Ingredient catalyst, int chaosPerTick, int processTime, ItemLike result, int count) {
        this.result = result.asItem();
        this.count = count;
        this.ingredient = ingredient;
        this.catalyst = catalyst;
        this.chaosPerTick = chaosPerTick;
        this.processTime = processTime;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        ResourceLocation itemId = NameUtils.fromItem(this.result);
        build(consumer, new ResourceLocation(itemId.getNamespace(), "chaos_infusing/" + itemId.getPath()));
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final InfusingRecipeBuilder builder;

        public Result(ResourceLocation id, InfusingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("chaosPerTick", chaosPerTick);
            json.addProperty("processTime", processTime);
            json.add("ingredient", ingredient.toJson());
            json.add("catalyst", catalyst.toJson());
            json.addProperty("result", NameUtils.fromItem(result).toString());
            json.addProperty("count", count);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ChaosRecipes.INFUSING.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
