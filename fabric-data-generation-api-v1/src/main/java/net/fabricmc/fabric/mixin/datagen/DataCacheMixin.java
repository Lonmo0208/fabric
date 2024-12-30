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

package net.fabricmc.fabric.mixin.datagen;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.data.DataCache;

import net.fabricmc.fabric.impl.datagen.FabricDataCache;
import net.fabricmc.fabric.impl.datagen.FabricDataGeneratorImpl;

@Mixin(DataCache.class)
abstract class DataCacheMixin implements FabricDataCache {
	@Shadow
	@Final
	Set<Path> paths;

	@Shadow
	@Final
	private Map<String, DataCache.CachedData> cachedDatas;

	@Shadow
	@Final
	private Path root;

	@Shadow
	@Final
	@Mutable
	private int totalSize;

	@Shadow
	protected abstract Path getPath(String providerName);

	@Shadow
	private static DataCache.CachedData parseOrCreateCache(Path root, Path dataProviderPath) {
		throw new IllegalStateException();
	}

	// Lambda in write()V
	@Redirect(method = "method_46571", at = @At(value = "INVOKE", target = "Ljava/time/LocalDateTime;now()Ljava/time/LocalDateTime;"))
	private LocalDateTime constantTime() {
		// Write a constant time to the .cache file to ensure datagen output is reproducible
		return LocalDateTime.MIN;
	}

	@Override
	public void fabric_prepare(FabricDataGeneratorImpl dataGenerator) {
		Set<String> providerNames = dataGenerator.getProviderNames();

		for (String providerName : providerNames) {
			Path path = getPath(providerName);
			paths.add(path);
			cachedDatas.put(providerName, parseOrCreateCache(root, path));
		}

		totalSize += providerNames.size();
	}
}
