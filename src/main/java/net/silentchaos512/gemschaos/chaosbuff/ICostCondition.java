package net.silentchaos512.gemschaos.chaosbuff;

import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public interface ICostCondition extends Predicate<PlayerEntity> {
    String getName();
}
