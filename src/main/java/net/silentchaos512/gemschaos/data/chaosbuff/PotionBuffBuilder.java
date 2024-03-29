package net.silentchaos512.gemschaos.data.chaosbuff;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gemschaos.chaosbuff.PotionChaosBuff;

public class PotionBuffBuilder extends ChaosBuffBuilder {
    private final MobEffect effect;
    private int effectDuration = -1;

    public PotionBuffBuilder(ResourceLocation buffId, int maxLevel, MobEffect effect) {
        super(buffId, maxLevel, PotionChaosBuff.SERIALIZER);
        this.effect = effect;
        this.displayName = effect.getDisplayName();
    }

    public static PotionBuffBuilder builder(ResourceLocation buffId, int maxLevel, MobEffect effect) {
        return new PotionBuffBuilder(buffId, maxLevel, effect);
    }

    public PotionBuffBuilder withEffectDuration(int durationInTicks) {
        this.effectDuration = durationInTicks;
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = super.serialize();
        json.addProperty("effect", ForgeRegistries.MOB_EFFECTS.getKey(effect).toString());
        if (effectDuration > 0) {
            json.addProperty("effectDuration", effectDuration);
        }
        return json;
    }
}
