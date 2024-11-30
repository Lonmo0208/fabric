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
import java.util.Comparator;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.HashCode;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.data.DataCache$CachedData")
public abstract class DataCacheCachedDataMixin {
	@WrapOperation(method = "write", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;entrySet()Lcom/google/common/collect/ImmutableSet;"))
	private ImmutableSet<Map.Entry<Path, HashCode>> sortPaths(ImmutableMap<Path, HashCode> instance, Operation<ImmutableSet<Map.Entry<Path, HashCode>>> original) {
		return original.call(instance).stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparing(k -> normalizePath(k.toString()))))
				.collect(ImmutableSet.toImmutableSet());
	}

	@WrapOperation(method = "write", at = @At(value = "INVOKE", target = "Ljava/nio/file/Path;toString()Ljava/lang/String;"))
	private String pathToString(Path instance, Operation<String> original) {
		return normalizePath(original.call(instance));
	}

	@Unique
	private static String normalizePath(String path) {
		return path.replace('\\', '/');
	}
}
