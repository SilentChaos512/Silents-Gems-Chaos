package net.silentchaos512.gemschaos.chaosbuff;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Predicate;

public enum CostConditions implements ICostCondition {
    NO_CONDITION(p -> true),
    BURNING(Entity::isOnFire),
//    FREEZING(p -> p.getActivePotionEffect(GemsEffects.FREEZING.get()) != null),
//    SHOCKING(p -> p.getActivePotionEffect(GemsEffects.SHOCKING.get()) != null),
    FLYING(p -> p.getAbilities().flying),
    HURT(p -> p.getHealth() < p.getMaxHealth() - 0.5f),
    IN_AIR(p -> !p.isOnGround()),
    MOVING(CostConditions::hasMoved),
    UNDERWATER(Entity::isInWater);

    private final Predicate<Player> condition;

    CostConditions(Predicate<Player> condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(Player player) {
        return this.condition.test(player);
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
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

    private static boolean hasMoved(Player player) {
        // FIXME: does not work
//        double dx = player.prevPosX - player.posX;
//        double dz = player.prevPosZ - player.posZ;
//        return dx * dx + dz * dz > 0.01;
        return true;
    }
}
