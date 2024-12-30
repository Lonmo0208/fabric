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

package net.fabricmc.fabric.impl.datagen;

import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.data.DataCache;
import net.minecraft.registry.RegistryWrapper;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.ModContainer;

public class FabricDataGeneratorImpl extends FabricDataGenerator {
	private final DataCache dataCache;
	private final Set<String> providerNames;

	public FabricDataGeneratorImpl(Path output, ModContainer mod, boolean strictValidation, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, DataCache dataCache, Set<String> providerNames) {
		super(output, mod, strictValidation, registriesFuture);
		this.dataCache = dataCache;
		this.providerNames = providerNames;
	}

	public DataCache getDataCache() {
		return dataCache;
	}

	public Set<String> getProviderNames() {
		return providerNames;
	}
}
