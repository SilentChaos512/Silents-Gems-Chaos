package net.silentchaos512.gemschaos.crafting.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;
import net.silentchaos512.lib.util.NameUtils;

public class BlockCorruptingRecipe extends SingleItemRecipe {
    private int chaosDissipated = 1000;

    public BlockCorruptingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result) {
        super(ChaosRecipes.Types.BLOCK_CORRUPTING.get(), ChaosRecipes.BLOCK_CORRUPTING.get(), id, "", ingredient, result);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.ingredient);
        return list;
    }

    public boolean matches(BlockState state) {
        return this.ingredient.test(new ItemStack(state.getBlock().asItem()));
    }

    public BlockState getResultBlock(BlockState state) {
        BlockState result = ((BlockItem) this.result.getItem()).getBlock().defaultBlockState();
        for (Property<?> property : result.getProperties()) {
            if (state.hasProperty(property)) {
                result = copyProperty(result, state, property);
            }
        }
        return result;
    }

    private static BlockState copyProperty(BlockState copy, BlockState original, Property<?> property) {
        //noinspection ChainOfInstanceofChecks
        if (property instanceof DirectionProperty) {
            DirectionProperty prop = (DirectionProperty) property;
            return copy.setValue(prop, original.getValue(prop));
        } else if (property instanceof BooleanProperty) {
            BooleanProperty prop = (BooleanProperty) property;
            return copy.setValue(prop, original.getValue(prop));
        }
        return copy;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getChaosDissipated() {
        return chaosDissipated;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BlockCorruptingRecipe> {
        @Override
        public BlockCorruptingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));

            ResourceLocation itemId = new ResourceLocation(GsonHelper.getAsString(json, "result"));
            Item item = ForgeRegistries.ITEMS.getValue(itemId);
            if (item == null) {
                throw new JsonParseException("Unknown item: " + itemId);
            } else if (!(item instanceof BlockItem)) {
                throw new JsonParseException("Item '" + NameUtils.from(item) + "' is not a block item!");
            }
            ItemStack resultStack = new ItemStack(item);

            BlockCorruptingRecipe recipe = new BlockCorruptingRecipe(recipeId, ingredient, resultStack);
            recipe.chaosDissipated = GsonHelper.getAsInt(json, "chaosDissipated", recipe.chaosDissipated);
            return recipe;
        }

        @Override
        public BlockCorruptingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            BlockCorruptingRecipe recipe = new BlockCorruptingRecipe(recipeId, ingredient, result);
            recipe.chaosDissipated = buffer.readVarInt();
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BlockCorruptingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.chaosDissipated);
        }
    }
}
