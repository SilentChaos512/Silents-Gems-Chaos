package net.silentchaos512.gemschaos.chaosbuff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public enum CostConditions {
    NO_CONDITION(p -> true),
    BURNING(Entity::isBurning),
//    FREEZING(p -> p.getActivePotionEffect(GemsEffects.FREEZING.get()) != null),
//    SHOCKING(p -> p.getActivePotionEffect(GemsEffects.SHOCKING.get()) != null),
    FLYING(p -> p.abilities.isFlying),
    HURT(p -> p.getHealth() < p.getMaxHealth() - 0.5f),
    IN_AIR(p -> !p.isOnGround()),
    MOVING(CostConditions::hasMoved),
    UNDERWATER(Entity::isInWater);

    private final Predicate<PlayerEntity> condition;

    CostConditions(Predicate<PlayerEntity> condition) {
        this.condition = condition;
    }

    public boolean appliesTo(PlayerEntity player) {
        return this.condition.test(player);
    }

    @Nullable
    public static CostConditions from(String str) {
        for (CostConditions c : values()) {
            if (c.name().equalsIgnoreCase(str)) {
                return c;
            }
        }
        return null;
    }

    private static boolean hasMoved(PlayerEntity player) {
        // FIXME: does not work
//        double dx = player.prevPosX - player.posX;
//        double dz = player.prevPosZ - player.posZ;
//        return dx * dx + dz * dz > 0.01;
        return true;
    }
}
