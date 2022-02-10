/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.biome.modification;

import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.level.LevelProperties;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;

@ApiStatus.Internal
public class BiomeSelectionContextImpl implements BiomeSelectionContext {
	private final DynamicRegistryManager dynamicRegistries;
	private final LevelProperties levelProperties;
	private final RegistryKey<Biome> key;
	private final Biome biome;

	public BiomeSelectionContextImpl(DynamicRegistryManager dynamicRegistries, LevelProperties levelProperties, RegistryKey<Biome> key, Biome biome) {
		this.dynamicRegistries = dynamicRegistries;
		this.levelProperties = levelProperties;
		this.key = key;
		this.biome = biome;
	}

	@Override
	public RegistryKey<Biome> getBiomeKey() {
		return key;
	}

	@Override
	public Biome getBiome() {
		return biome;
	}

	@Override
	public Optional<RegistryKey<ConfiguredFeature<?, ?>>> getFeatureKey(ConfiguredFeature<?, ?> configuredFeature) {
		Registry<ConfiguredFeature<?, ?>> registry = dynamicRegistries.get(Registry.CONFIGURED_FEATURE_KEY);
		return registry.getKey(configuredFeature);
	}

	@Override
	public Optional<RegistryKey<PlacedFeature>> getPlacedFeatureKey(PlacedFeature placedFeature) {
		Registry<PlacedFeature> registry = dynamicRegistries.get(Registry.PLACED_FEATURE_KEY);
		return registry.getKey(placedFeature);
	}

	@Override
	public boolean hasStructure(RegistryKey<ConfiguredStructureFeature<?, ?>> key) {
		ConfiguredStructureFeature<?, ?> instance = dynamicRegistries.get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY).get(key);

		if (instance == null) {
			return false;
		}

		// Since the biome->structure mapping is now stored in the chunk generator configuration, we check every
		// chunk generator used by the current world-save.
		for (DimensionOptions dimension : levelProperties.getGeneratorOptions().getDimensions()) {
			StructuresConfig structuresConfig = dimension.getChunkGenerator().getStructuresConfig();

			if (structuresConfig.getConfiguredStructureFeature(instance.feature).get(key).contains(getBiomeKey())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Optional<RegistryKey<ConfiguredStructureFeature<?, ?>>> getStructureKey(ConfiguredStructureFeature<?, ?> configuredStructure) {
		Registry<ConfiguredStructureFeature<?, ?>> registry = dynamicRegistries.get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY);
		return registry.getKey(configuredStructure);
	}

	@Override
	public boolean canGenerateIn(RegistryKey<DimensionOptions> dimensionKey) {
		DimensionOptions dimension = levelProperties.getGeneratorOptions().getDimensions().get(dimensionKey);

		if (dimension == null) {
			return false;
		}

		return dimension.getChunkGenerator().getBiomeSource().getBiomes().anyMatch(entry -> entry.value() == biome);
	}
}
