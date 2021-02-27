package net.silentchaos512.gemschaos.chaosbuff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gemschaos.ChaosMod;

import java.util.Objects;

public class PotionChaosBuff extends SimpleChaosBuff {
    private static final ResourceLocation SERIALIZER_ID = ChaosMod.getId("potion");
    public static final IChaosBuffSerializer<PotionChaosBuff> SERIALIZER = new Serializer<>(
            SERIALIZER_ID,
            PotionChaosBuff::new,
            (buff, json) -> {
                String str = JSONUtils.getString(json, "effect");
                buff.effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(str));
                buff.effectDuration = JSONUtils.getInt(json, "effectDuration", 50);
            },
            (buff, buffer) -> {
                buff.effect = ForgeRegistries.POTIONS.getValue(buffer.readResourceLocation());
                buff.effectDuration = buffer.readVarInt();
            },
            (buff, buffer) -> {
                buffer.writeResourceLocation(Objects.requireNonNull(buff.effect.getRegistryName()));
                buffer.writeVarInt(buff.effectDuration);
            }
    );

    private Effect effect;
    private int effectDuration;

    public PotionChaosBuff(ResourceLocation id) {
        super(id);
    }

    public Effect getEffect() {
        return effect;
    }

    @Override
    public void applyTo(PlayerEntity player, int level) {
        if (this.effect == Effects.NIGHT_VISION || player.getActivePotionEffect(this.effect) == null) {
            player.addPotionEffect(new EffectInstance(this.effect, this.effectDuration, level - 1, true, false));
        }
    }

    @Override
    public void removeFrom(PlayerEntity player) {
        player.removePotionEffect(this.effect);
    }

    @Override
    public int getRuneColor() {
        if (effect != null) return effect.getLiquidColor();
        return super.getRuneColor();
    }

    @Override
    public IChaosBuffSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
