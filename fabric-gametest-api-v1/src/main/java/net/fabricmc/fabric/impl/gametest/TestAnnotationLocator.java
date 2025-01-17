package net.fabricmc.fabric.impl.gametest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.FunctionTestInstance;
import net.minecraft.test.TestContext;
import net.minecraft.test.TestData;
import net.minecraft.test.TestEnvironmentDefinition;
import net.minecraft.test.TestInstance;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

public class TestAnnotationLocator {
	private static final String ENTRYPOINT_KEY = "fabric-gametest";
	private static final Logger LOGGER = LoggerFactory.getLogger(TestAnnotationLocator.class);

	private final FabricLoader fabricLoader;

	private List<TestMethod> testMethods = null;

	public TestAnnotationLocator(FabricLoader fabricLoader) {
		this.fabricLoader = fabricLoader;
	}

	public List<TestMethod> getTestMethods() {
		if (testMethods != null) {
			return testMethods;
		}

		List<EntrypointContainer<Object>> entrypointContainers = fabricLoader
				.getEntrypointContainers(ENTRYPOINT_KEY, Object.class);

		return testMethods = entrypointContainers.stream()
				.flatMap(entrypoint -> findMagicMethods(entrypoint).stream())
				.toList();
	}

	private List<TestMethod> findMagicMethods(EntrypointContainer<Object> entrypoint) {
		Class<?> testClass = entrypoint.getEntrypoint().getClass();
		List<TestMethod> methods = new ArrayList<>();
		findMagicMethods(entrypoint, testClass, methods);

		if (methods.isEmpty()) {
			LOGGER.warn("No methods with the FabricGameTest annotation were found in {}", testClass.getName());
		}

		return methods;
	}

	// Recursively find all methods with the GameTestData annotation
	private void findMagicMethods(EntrypointContainer<Object> entrypoint, Class<?> testClass, List<TestMethod> methods) {
		for (Method method : testClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(FabricGameTest.class)) {
				validateMethod(method);
				methods.add(new TestMethod(method, method.getAnnotation(FabricGameTest.class), entrypoint));
			}
		}

		if (testClass.getSuperclass() != null) {
			findMagicMethods(entrypoint, testClass.getSuperclass(), methods);
		}
	}

	private void validateMethod(Method method) {
		if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != TestContext.class) {
			throw new UnsupportedOperationException("Method %s must have a single parameter of type TestContext".formatted(method.getName()));
		}
	}

	public record TestMethod(Method method, FabricGameTest fabricGameTest, EntrypointContainer<Object> entrypoint) {
		Identifier identifier() {
			String name = camelToSnake(entrypoint.getEntrypoint().getClass().getSimpleName() + "_" + method.getName());
			return Identifier.of(entrypoint.getProvider().getMetadata().getId(), name);
		}

		Consumer<TestContext> testFunction() {
			return context -> {
				try {
					method.invoke(entrypoint.getEntrypoint(), context);
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException("Failed to invoke test method", e);
				}
			};
		}

		TestData<RegistryEntry<TestEnvironmentDefinition>> testData(RegistryWrapper.WrapperLookup lookup) {
			RegistryEntryLookup<TestEnvironmentDefinition> testEnvironments = lookup.getOrThrow(RegistryKeys.TEST_ENVIRONMENT);
			RegistryEntry<TestEnvironmentDefinition> testEnvironment = testEnvironments.getOrThrow(RegistryKey.of(RegistryKeys.TEST_ENVIRONMENT, Identifier.of(fabricGameTest.environment())));

			return new TestData<>(
					testEnvironment,
					Identifier.of(fabricGameTest.structure()),
					fabricGameTest.maxTicks(),
					fabricGameTest.setupTicks(),
					fabricGameTest.required(),
					fabricGameTest.rotation(),
					fabricGameTest.manualOnly(),
					fabricGameTest.maxAttempts(),
					fabricGameTest.requiredSuccesses(),
					fabricGameTest.skyAccess()
			);
		}

		TestInstance testInstance(RegistryWrapper.WrapperLookup lookup) {
			return new FunctionTestInstance(
					Registries.TEST_FUNCTION.getEntry(identifier()).get(),
					testData(lookup)
			);
		}

		private static String camelToSnake(String input) {
			return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
		}
	}
}
