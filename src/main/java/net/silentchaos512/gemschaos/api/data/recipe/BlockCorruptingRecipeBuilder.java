package net.silentchaos512.gemschaos.api.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlockCorruptingRecipeBuilder {
    private final Item result;
    private final int chaosDissipated;
    private final Ingredient ingredient;

    public BlockCorruptingRecipeBuilder(Ingredient ingredient, IItemProvider result, int chaosDissipated) {
        this.result = result.asItem();
        this.ingredient = ingredient;
        this.chaosDissipated = chaosDissipated;
    }

    public static BlockCorruptingRecipeBuilder builder(IItemProvider block, IItemProvider result, int chaosDissipated) {
        return new BlockCorruptingRecipeBuilder(Ingredient.fromItems(block), result, chaosDissipated);
    }

    public static BlockCorruptingRecipeBuilder builder(ITag<Item> itemTag, IItemProvider result, int chaosDissipated) {
        return new BlockCorruptingRecipeBuilder(Ingredient.fromTag(itemTag), result, chaosDissipated);
    }

    public static BlockCorruptingRecipeBuilder builder(Ingredient ingredient, IItemProvider result, int chaosDissipated) {
        return new BlockCorruptingRecipeBuilder(ingredient, result, chaosDissipated);
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation itemId = NameUtils.from(this.result);
        build(consumer, new ResourceLocation(itemId.getNamespace(), "block_corrupting/" + itemId.getPath()));
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final BlockCorruptingRecipeBuilder builder;

        public Result(ResourceLocation id, BlockCorruptingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("chaosDissipated", chaosDissipated);
            json.add("ingredient", ingredient.serialize());
            json.addProperty("result", NameUtils.from(result).toString());
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ChaosRecipes.BLOCK_CORRUPTING.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
