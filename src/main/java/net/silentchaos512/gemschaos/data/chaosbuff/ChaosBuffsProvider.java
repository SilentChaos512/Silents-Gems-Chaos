package net.silentchaos512.gemschaos.data.chaosbuff;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gear.data.DataGenerators;
import net.silentchaos512.gemschaos.chaosbuff.CostConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class ChaosBuffsProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final DataGenerator generator;
    private final String modId;

    public ChaosBuffsProvider(DataGenerator generator, String modId) {
        this.generator = generator;
        this.modId = modId;
    }

    protected Collection<ChaosBuffBuilder> getBuilders() {
        Collection<ChaosBuffBuilder> ret = new ArrayList<>();

        ret.add(potion(MobEffects.DOLPHINS_GRACE, 1)
                .withSlots(5)
                .withCosts(5, 100)
                .withCostConditions(CostConditions.UNDERWATER)
        );
        ret.add(potion(MobEffects.FIRE_RESISTANCE, 1)
                .withSlots(8)
                .withCosts(5, 200)
                .withCostConditions(CostConditions.BURNING)
        );
        ret.add(potion(MobEffects.DIG_SPEED, 2)
                .withSlots(4, 6)
                .withCosts(3, 50, 60, 80, 100)
        );
        ret.add(potion(MobEffects.INVISIBILITY, 1)
                .withEffectDuration(410)
                .withSlots(6)
                .withCosts(0, 80)
        );
        ret.add(potion(MobEffects.JUMP, 4)
                .withSlots(4, 5, 6, 7)
                .withCosts(0, 30, 36, 48, 60)
                .withCostConditions(CostConditions.IN_AIR)
        );
        ret.add(potion(MobEffects.LEVITATION, 4)
                .withSlots(4, 5, 6, 7)
                .withCosts(0, 35, 45, 55, 65)
        );
        ret.add(potion(MobEffects.NIGHT_VISION, 1)
                .withEffectDuration(410)
                .withSlots(2)
                .withCosts(0, 30)
        );
        ret.add(potion(MobEffects.REGENERATION, 2)
                .withSlots(8, 12)
                .withCosts(25, 100, 200)
                .withCostConditions(CostConditions.HURT)
        );
        ret.add(potion(MobEffects.DAMAGE_RESISTANCE, 2)
                .withSlots(8, 12)
                .withCosts(10, 80, 150)
                .withCostConditions(CostConditions.HURT)
        );
        ret.add(potion(MobEffects.SLOW_FALLING, 1)
                .withSlots(5)
                .withCosts(0, 100)
                .withCostConditions(CostConditions.IN_AIR)
        );
        ret.add(potion(MobEffects.MOVEMENT_SPEED, 4)
                .withSlots(4, 5, 6, 7)
                .withCosts(0, 30, 40, 50, 60)
                .withCostConditions(CostConditions.MOVING)
        );
        ret.add(potion(MobEffects.DAMAGE_BOOST, 2)
                .withSlots(10, 12)
                .withCosts(2, 150, 250)
        );
        ret.add(potion(MobEffects.WATER_BREATHING, 1)
                .withSlots(6)
                .withCosts(0, 80)
                .withCostConditions(CostConditions.UNDERWATER)
        );

        return ret;
    }

    private PotionBuffBuilder potion(MobEffect effect, int maxLevel) {
        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        assert effectId != null;
        String path = effectId.getNamespace() + "." + effectId.getPath();
        return PotionBuffBuilder.builder(modId(path), maxLevel, effect);
    }

    @Override
    public String getName() {
        return "Chaos Buffs - " + modId;
    }

    protected ResourceLocation modId(String path) {
        return new ResourceLocation(this.modId, path);
    }

    @Override
    public void run(CachedOutput cache) {
        Path outputFolder = this.generator.getOutputFolder();
        Set<ResourceLocation> entries = Sets.newHashSet();

        //noinspection OverlyLongLambda
        getBuilders().forEach(builder -> {
            if (entries.contains(builder.buffId)) {
                throw new IllegalStateException("Duplicate buff: " + builder.buffId);
            }

            entries.add(builder.buffId);
            Path path = outputFolder.resolve(String.format("data/%s/silentgems_chaos_buffs/%s.json", builder.buffId.getNamespace(), builder.buffId.getPath()));
            trySaveStable(cache, builder, path);
        });
    }

    private static void trySaveStable(CachedOutput cache, ChaosBuffBuilder builder, Path path) {
        try {
            DataGenerators.saveStable(cache, builder.serialize(), path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
