package net.silentchaos512.gemschaos.data.chaosbuff;

import com.google.gson.JsonObject;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gemschaos.chaosbuff.PotionChaosBuff;
import net.silentchaos512.lib.util.NameUtils;

public class PotionBuffBuilder extends ChaosBuffBuilder {
    private final Effect effect;
    private int effectDuration = -1;

    public PotionBuffBuilder(ResourceLocation buffId, int maxLevel, Effect effect) {
        super(buffId, maxLevel, PotionChaosBuff.SERIALIZER);
        this.effect = effect;
        this.displayName = effect.getDisplayName();
    }

    public static PotionBuffBuilder builder(ResourceLocation buffId, int maxLevel, Effect effect) {
        return new PotionBuffBuilder(buffId, maxLevel, effect);
    }

    public PotionBuffBuilder withEffectDuration(int durationInTicks) {
        this.effectDuration = durationInTicks;
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = super.serialize();
        json.addProperty("effect", NameUtils.from(effect).toString());
        if (effectDuration > 0) {
            json.addProperty("effectDuration", effectDuration);
        }
        return json;
    }
}
