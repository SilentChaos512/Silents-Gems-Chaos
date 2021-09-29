package net.silentchaos512.gemschaos.chaosbuff;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public interface IChaosBuff {
    ResourceLocation getId();

    void applyTo(Player player, int level);

    void removeFrom(Player player);

    int getChaosGenerated(@Nullable Player player, int level);

    default int getActiveChaosGenerated(int level) {
        return getChaosGenerated(null, level);
    }

    int getMaxLevel();

    int getSlotsForLevel(int level);

    boolean isActive(Player player);

    Component getDisplayName(int level);

    int getRuneColor();

    IChaosBuffSerializer<?> getSerializer();
}
