package net.silentchaos512.gemschaos.chaosbuff;

import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public interface ICostCondition extends Predicate<Player> {
    String getName();
}
