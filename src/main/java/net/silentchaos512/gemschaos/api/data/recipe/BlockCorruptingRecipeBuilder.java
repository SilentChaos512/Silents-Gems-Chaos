package net.silentchaos512.gemschaos.api.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlockCorruptingRecipeBuilder {
    private final Item result;
    private final int chaosDissipated;
    private final Ingredient ingredient;

    public BlockCorruptingRecipeBuilder(Ingredient ingredient, ItemLike result, int chaosDissipated) {
        this.result = result.asItem();
        this.ingredient = ingredient;
        this.chaosDissipated = chaosDissipated;
    }

    public static BlockCorruptingRecipeBuilder builder(ItemLike block, ItemLike result, int chaosDissipated) {
        return new BlockCorruptingRecipeBuilder(Ingredient.of(block), result, chaosDissipated);
    }

    public static BlockCorruptingRecipeBuilder builder(TagKey<Item> itemTag, ItemLike result, int chaosDissipated) {
        return new BlockCorruptingRecipeBuilder(Ingredient.of(itemTag), result, chaosDissipated);
    }

    public static BlockCorruptingRecipeBuilder builder(Ingredient ingredient, ItemLike result, int chaosDissipated) {
        return new BlockCorruptingRecipeBuilder(ingredient, result, chaosDissipated);
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        ResourceLocation itemId = NameUtils.from(this.result);
        build(consumer, new ResourceLocation(itemId.getNamespace(), "block_corrupting/" + itemId.getPath()));
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final BlockCorruptingRecipeBuilder builder;

        public Result(ResourceLocation id, BlockCorruptingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("chaosDissipated", chaosDissipated);
            json.add("ingredient", ingredient.toJson());
            json.addProperty("result", NameUtils.from(result).toString());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ChaosRecipes.BLOCK_CORRUPTING.get();
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
