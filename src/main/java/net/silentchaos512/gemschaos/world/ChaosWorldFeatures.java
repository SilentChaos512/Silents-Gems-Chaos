package net.silentchaos512.gemschaos.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.config.OreConfig;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.config.ChaosConfig;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;

import java.util.List;

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

        registerConfiguredFeature("chaos", ChaosConfig.Common.chaosOres.createConfiguredFeature(
                config -> {
                    ImmutableList<OreConfiguration.TargetBlockState> targetList = ImmutableList.of(
                            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ChaosBlocks.CHAOS_ORE.get().defaultBlockState()),
                            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ChaosBlocks.DEEPSLATE_CHAOS_ORE.get().defaultBlockState()));
                    return FeatureUtils.register(ChaosMod.MOD_ID + ":chaos_ore", Feature.ORE, new OreConfiguration(targetList, config.getSize(), config.getDiscardChanceOnAirExposure()));
                },
                (config, feature) -> {
                    return PlacementUtils.register(ChaosMod.MOD_ID + ":chaos_ore", feature, List.of(
                            CountPlacement.of(config.getCount()),
                            RarityFilter.onAverageOnceEvery(config.getRarity()),
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.getMinHeight()), VerticalAnchor.absolute(config.getMaxHeight())),
                            InSquarePlacement.spread(),
                            BiomeFilter.biome()
                    ));
                }
        ));
    }

    private static <FC extends FeatureConfiguration> void registerConfiguredFeature(String name, Holder<ConfiguredFeature<FC, ?>> configuredFeature) {
        ChaosMod.LOGGER.debug("register configured feature '{}'", name);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ChaosMod.getId(name), configuredFeature.value());
    }

    private static void addOreFeature(BiomeLoadingEvent biome, OreConfig config) {
        if (config.isEnabled()) {
            biome.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, config.getPlacedFeature());
        }
    }
}
