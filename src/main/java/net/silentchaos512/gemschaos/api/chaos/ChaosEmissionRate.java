package net.silentchaos512.gemschaos.api.chaos;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.silentchaos512.gemschaos.ChaosMod;

import java.util.Locale;

public enum ChaosEmissionRate {
    NONE(0),
    MINIMAL(8),
    SMALL(64),
    MODERATE(512),
    HIGH(4096),
    VERY_HIGH(32_768),
    EXTREME(262_144);

    private final int maxValue;

    ChaosEmissionRate(int maxValue) {
        this.maxValue = maxValue;
    }

    public static ChaosEmissionRate fromAmount(int amount) {
        for (ChaosEmissionRate e : values()) {
            if (e.maxValue >= amount) {
                return e;
            }
        }
        return EXTREME;
    }

    public MutableComponent getDisplayName() {
        String name = name().toLowerCase(Locale.ROOT);
        return translate("emissionRate." + name);
    }

    public MutableComponent getDisplayName(int chaos) {
        if (this == NONE || this == MINIMAL) {
            return getDisplayName();
        }

        ChaosEmissionRate previous = values()[this.ordinal() - 1];
        int diff = this.maxValue - previous.maxValue;

        if (chaos > (previous.maxValue + 2 * diff / 3)) {
            Component text = translate("emissionRate.plus2");
            return getDisplayName().append(text);
        }
        if (chaos > (previous.maxValue + diff / 3)) {
            Component text = translate("emissionRate.plus1");
            return getDisplayName().append(text);
        }

        return getDisplayName();
    }

    public Component getEmissionText() {
        return translate("emission", getDisplayName());
    }

    public Component getEmissionText(int chaos) {
        return translate("emission", getDisplayName(chaos));
    }

    private static MutableComponent translate(String key, Object... params) {
        return ChaosMod.TEXT.translate("chaos", key, params);
    }
}
