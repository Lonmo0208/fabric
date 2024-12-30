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
import java.util.Collection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.GameVersion;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;

import net.fabricmc.fabric.impl.datagen.FabricDataGeneratorImpl;

@Mixin(DataGenerator.class)
public class DataGeneratorMixin {
	@WrapOperation(method = "run", at = @At(value = "NEW", target = "(Ljava/nio/file/Path;Ljava/util/Collection;Lnet/minecraft/GameVersion;)Lnet/minecraft/data/DataCache;"))
	private DataCache newDataCache(Path root, Collection<String> providerNames, GameVersion gameVersion, Operation<DataCache> original) {
		if ((Object) (this) instanceof FabricDataGeneratorImpl fabricDataGenerator) {
			return fabricDataGenerator.getDataCache();
		}

		return original.call(root, providerNames, gameVersion);
	}

	@WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/DataCache;write()V"))
	private void dataCacheWrite(DataCache instance, Operation<Void> original) {
		if ((Object) (this) instanceof FabricDataGeneratorImpl) {
			// Skip this for now, we will run it for all data generators in FabricDataGenHelper
			return;
		}

		original.call(instance);
	}
}
