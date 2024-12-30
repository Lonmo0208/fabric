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

import java.util.Set;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.data.DataGenerator;

import net.fabricmc.fabric.impl.datagen.FabricDataGeneratorImpl;

@Mixin(DataGenerator.Pack.class)
public class DataGeneratorPackMixin {
	@WrapOperation(method = "addProvider", at = @At(value = "FIELD", target = "Lnet/minecraft/data/DataGenerator;providerNames:Ljava/util/Set;"))
	private Set<String> addProvider(DataGenerator instance, Operation<Set<String>> original) {
		if ((Object)(instance) instanceof FabricDataGeneratorImpl fabricDataGenerator) {
			return fabricDataGenerator.getProviderNames();
		}

		return original.call(instance);
	}
}
