package net.silentchaos512.gemschaos.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gemschaos.chaosbuff.IChaosBuff;
import net.silentchaos512.gemschaos.item.ChaosGemItem;
import net.silentchaos512.gemschaos.item.ChaosRuneItem;
import net.silentchaos512.gemschaos.setup.ChaosRecipes;

import java.util.List;

public class ApplyChaosRuneRecipe extends SpecialRecipe {
    public ApplyChaosRuneRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @SuppressWarnings("ChainOfInstanceofChecks")
    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        int chaosGems = 0;
        int chaosRunes = 0;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
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
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack chaosGem = ItemStack.EMPTY;
        List<ItemStack> runes = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
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
    public boolean canFit(int width, int height) {
        return width * height > 1;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ChaosRecipes.APPLY_CHAOS_RUNE.get();
    }
}

