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

package net.fabricmc.fabric.impl.gametest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.test.TestInstance;
import net.minecraft.test.TestInstances;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricGameTestModInitializer implements ModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(FabricGameTestModInitializer.class);
	private static TestAnnotationLocator locator = new TestAnnotationLocator(FabricLoader.getInstance());

	@Override
	public void onInitialize() {
		if (!(FabricGameTestRunner.ENABLED || FabricLoader.getInstance().isDevelopmentEnvironment())) {
			// Don't try to load the tests if the game test runner is disabled or we are not in a development environment
			return;
		}

		for (TestAnnotationLocator.TestMethod testMethod : locator.getTestMethods()) {
			LOGGER.debug("Registering test method: {}", testMethod.identifier());
			Registry.register(Registries.TEST_FUNCTION, testMethod.identifier(), testMethod.testFunction());
		}

		DynamicRegistrySetupCallback.EVENT.register(registryView -> {
			// We ideally need an after loaded event, but this should hopefully work for now
			registryView.registerEntryAdded(RegistryKeys.TEST_INSTANCE, (rawId, id, object) -> {
				if (id.equals(TestInstances.ALWAYS_PASS.getValue())) {
					Registry<TestInstance> testInstances = registryView.getOptional(RegistryKeys.TEST_INSTANCE).get();

					for (TestAnnotationLocator.TestMethod testMethod : locator.getTestMethods()) {
						TestInstance testInstance = testMethod.testInstance(registryView.asDynamicRegistryManager());
						Registry.register(testInstances, testMethod.identifier(), testInstance);
					}
				}
			});
		});
	}
}
