package net.silentchaos512.gemschaos.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.config.OreConfig;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.config.ChaosConfig;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID)
public final class ChaosWorldFeatures {
    private static boolean configuredFeaturesRegistered = false;

    private ChaosWorldFeatures() {}

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent biome) {
        registerConfiguredFeatures();

        addOreFeature(biome, ChaosConfig.Common.chaosOres);
    }

    private static void registerConfiguredFeatures() {
        if (configuredFeaturesRegistered) return;
        configuredFeaturesRegistered = true;

        registerConfiguredFeature("chaos", ChaosConfig.Common.chaosOres.createConfiguredFeature(config -> {
            ImmutableList<OreConfiguration.TargetBlockState> targetList = ImmutableList.of(
                    OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, ChaosBlocks.CHAOS_ORE.get().defaultBlockState()),
                    OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, ChaosBlocks.DEEPSLATE_CHAOS_ORE.get().defaultBlockState()));
            return Feature.ORE
                    .configured(new OreConfiguration(targetList, config.getSize()))
                    .rangeUniform(VerticalAnchor.aboveBottom(config.getMinHeight()), VerticalAnchor.absolute(config.getMaxHeight()))
                    .decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(config.getRarity())))
                    .squared()
                    .count(config.getCount());
        }));
    }

    private static void registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        ChaosMod.LOGGER.debug("register configured feature '{}'", name);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ChaosMod.getId(name), configuredFeature);
    }

    private static void addOreFeature(BiomeLoadingEvent biome, OreConfig config) {
        if (config.isEnabled()) {
            biome.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, config.getConfiguredFeature());
        }
    }
}
