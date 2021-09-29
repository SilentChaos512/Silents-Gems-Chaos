package net.silentchaos512.gemschaos.crafting.recipe;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.silentchaos512.gemschaos.chaosbuff.IChaosBuff;
import net.silentchaos512.gemschaos.item.ChaosGemItem;
import net.silentchaos512.gemschaos.item.ChaosRuneItem;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;

import java.util.List;

public class ApplyChaosRuneRecipe extends CustomRecipe {
    public ApplyChaosRuneRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        int chaosGems = 0;
        int chaosRunes = 0;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ChaosGemItem) {
                    ++chaosGems;
                } else if (stack.getItem() instanceof ChaosRuneItem) {
                    ++chaosRunes;
                } else {
                    return false;
                }
            }
        }

        // One and only one chaos gem; one or more chaos runes
        return chaosGems == 1 && chaosRunes > 0;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack chaosGem = ItemStack.EMPTY;
        List<ItemStack> runes = NonNullList.create();

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ChaosRuneItem) {
                    // Any number of runes
                    runes.add(stack);
                } else {
                    // Only one chaos gem
                    if (!chaosGem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    chaosGem = stack;
                }
            }
        }

        if (chaosGem.isEmpty() || runes.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = chaosGem.copy();
        for (ItemStack rune : runes) {
            IChaosBuff buff = ChaosRuneItem.getBuff(rune);
            if (buff == null || !ChaosGemItem.addBuff(result, buff)) {
                return ItemStack.EMPTY;
            }
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height > 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ChaosRecipes.APPLY_CHAOS_RUNE.get();
    }
}

