package net.silentchaos512.gemschaos.api.pedestal;

import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gemschaos.api.WorldPos;

/**
 * An item which can be ticked when placed on a pedestal (example: chaos gems). This can either be
 * implemented on the item (only if Gems is a hard dependency) or attached to {@link ItemStack}s as
 * a capability.
 * <p>
 * Also see: {@link PedestalItemCapability}
 */
public interface IPedestalItem {
    /**
     * Called when the redstone power level changes. Any signal strength is considered powered.
     *
     * @param stack   The item on the pedestal
     * @param pos     The pedestal's position
     * @param powered True if receiving redstone power (any strength)
     * @return True if the pedestal should send an update to clients
     */
    boolean pedestalPowerChange(ItemStack stack, WorldPos pos, boolean powered);

    /**
     * Called each tick when the item is on a pedestal
     *  @param stack The item on the pedestal
     * @param pos   The pedestal's position
     */
    void pedestalTick(ItemStack stack, WorldPos pos);
}
