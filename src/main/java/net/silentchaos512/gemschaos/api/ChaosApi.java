package net.silentchaos512.gemschaos.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gemschaos.api.chaos.ChaosSourceCapability;
import net.silentchaos512.gemschaos.api.chaos.IChaosSource;
import net.silentchaos512.gemschaos.item.ChaosOrbItem;
import net.silentchaos512.lib.util.TimeUtils;

import javax.annotation.Nonnegative;

public class ChaosApi {
    public static final class Chaos {
        // Rate of natural dissipation (per player)
        private static final int DISSIPATION_SCALE = 20;
        // Chaos will balance out to certain levels, which will vary slightly over time
        private static final int EQUILIBRIUM_BASE = 150_000;
        private static final int EQUILIBRIUM_VARIATION = 100_000;
        private static final int EQUILIBRIUM_CYCLE_LENGTH = TimeUtils.ticksFromHours(4);
        private static final double EQUILIBRIUM_CYCLE_CONSTANT = 2 * Math.PI / EQUILIBRIUM_CYCLE_LENGTH;

        private Chaos() {throw new IllegalAccessError("Utility class");}

        /**
         * Adds to the chaos of the player.
         *
         * @param player    The player
         * @param amount    The amount of chaos to add
         * @param allowOrbs If true, chaos will first be sent to one chaos orb in the player's
         *                  inventory, with leakage spilling over to the player
         */
        public static void generate(Player player, int amount, boolean allowOrbs) {
            if (amount == 0) return;

            int amountLeft = amount;
            if (allowOrbs) {
                // Chaos orbs absorb chaos, but put it all into the first we find
                for (ItemStack stack : player.getInventory().items) {
                    if (!stack.isEmpty() && stack.getItem() instanceof ChaosOrbItem) {
                        amountLeft = ChaosOrbItem.absorbChaos(player, stack, amountLeft);
                        break;
                    }
                }
            }

            generate(player, amountLeft, player.blockPosition());
        }

        /**
         * Adds to the chaos of the object.
         *
         * @param obj    The chaos source
         * @param amount The amount of chaos to add
         * @param pos    The position of whatever generated the chaos
         */
        public static void generate(ICapabilityProvider obj, @Nonnegative int amount, BlockPos pos) {
            if (amount > 0) {
                obj.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source -> source.addChaos(amount));
            }
        }

        @SuppressWarnings("TypeMayBeWeakened")
        public static void dissipate(Player player, int amount) {
            if (amount <= 0) {
                return;
            }
            IChaosSource source = player.getCapability(ChaosSourceCapability.INSTANCE).orElseThrow(IllegalStateException::new);
            source.dissipateChaos(amount);
        }

        /**
         * Gets the chaos dissipation rate.
         *
         * @param world The world
         * @return The dissipation rate
         */
        public static int getDissipationRate(Level world) {
            return DISSIPATION_SCALE;
        }

        public static int getEquilibriumPoint(Level world) {
            long time = world.getGameTime();
            return (int) (EQUILIBRIUM_BASE + EQUILIBRIUM_VARIATION * Math.cos(EQUILIBRIUM_CYCLE_CONSTANT * time));
        }

        public static int getChaos(ICapabilityProvider provider) {
            LazyOptional<IChaosSource> optional = provider.getCapability(ChaosSourceCapability.INSTANCE);
            return optional.isPresent() ? optional.orElseGet(ChaosSourceCapability::new).getChaos() : 0;
        }
    }
}
