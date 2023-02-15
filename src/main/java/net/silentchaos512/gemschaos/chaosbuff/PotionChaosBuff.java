package net.silentchaos512.gemschaos.chaosbuff;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gemschaos.ChaosMod;

import java.util.Objects;

public class PotionChaosBuff extends SimpleChaosBuff {
    private static final ResourceLocation SERIALIZER_ID = ChaosMod.getId("potion");
    public static final IChaosBuffSerializer<PotionChaosBuff> SERIALIZER = new Serializer<>(
            SERIALIZER_ID,
            PotionChaosBuff::new,
            (buff, json) -> {
                String str = GsonHelper.getAsString(json, "effect");
                buff.effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(str));
                buff.effectDuration = GsonHelper.getAsInt(json, "effectDuration", 50);
            },
            (buff, buffer) -> {
                buff.effect = ForgeRegistries.MOB_EFFECTS.getValue(buffer.readResourceLocation());
                buff.effectDuration = buffer.readVarInt();
            },
            (buff, buffer) -> {
                buffer.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(buff.effect)));
                buffer.writeVarInt(buff.effectDuration);
            }
    );

    private MobEffect effect;
    private int effectDuration;

    public PotionChaosBuff(ResourceLocation id) {
        super(id);
    }

    public MobEffect getEffect() {
        return effect;
    }

    @Override
    public void applyTo(Player player, int level) {
        if (this.effect == MobEffects.NIGHT_VISION || player.getEffect(this.effect) == null) {
            player.addEffect(new MobEffectInstance(this.effect, this.effectDuration, level - 1, true, false));
        }
    }

    @Override
    public void removeFrom(Player player) {
        player.removeEffect(this.effect);
    }

    @Override
    public int getRuneColor() {
        if (effect != null) return effect.getColor();
        return super.getRuneColor();
    }

    @Override
    public IChaosBuffSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
