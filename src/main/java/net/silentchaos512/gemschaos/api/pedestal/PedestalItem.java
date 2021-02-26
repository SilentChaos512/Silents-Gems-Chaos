package net.silentchaos512.gemschaos.api.pedestal;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gemschaos.api.WorldPos;

public class PedestalItem implements IPedestalItem {
    @Override
    public boolean pedestalPowerChange(ItemStack stack, WorldPos pos, boolean powered) {
        return false;
    }

    @Override
    public void pedestalTick(ItemStack stack, WorldPos pos) {
        // No-op
    }
}
