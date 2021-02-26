package net.silentchaos512.gemschaos.api.pedestal;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gemschaos.api.WorldPos;
import net.silentchaos512.lib.util.TriFunction;

import java.util.function.BiConsumer;

public class PedestalItemImpl extends PedestalItem {
    private final TriFunction<ItemStack, WorldPos, Boolean, Boolean> powerChangeHandler;
    private final BiConsumer<ItemStack, WorldPos> tickHandler;

    public PedestalItemImpl(TriFunction<ItemStack, WorldPos, Boolean, Boolean> powerChangeHandler, BiConsumer<ItemStack, WorldPos> tickHandler) {
        this.powerChangeHandler = powerChangeHandler;
        this.tickHandler = tickHandler;
    }

    @Override
    public boolean pedestalPowerChange(ItemStack stack, WorldPos pos, boolean powered) {
        return this.powerChangeHandler.apply(stack, pos, powered);
    }

    @Override
    public void pedestalTick(ItemStack stack, WorldPos pos) {
        this.tickHandler.accept(stack, pos);
    }
}
