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

package net.fabricmc.fabric.test.item.gametest;

public class CustomEnchantmentEffectsGameTest {
	/* TODO 1.21.5 tests
	@GameTest(templateName = "fabric-item-api-v1-testmod:bedrock_platform")
	public void weirdImpalingSetsFireToTargets(TestContext context) {
		BlockPos pos = new BlockPos(3, 3, 3);
		CreeperEntity creeper = context.spawnEntity(EntityType.CREEPER, pos);
		PlayerEntity player = context.createMockPlayer(GameMode.CREATIVE);

		ItemStack trident = Items.TRIDENT.getDefaultStack();
		Optional<RegistryEntry.Reference<Enchantment>> impaling = getEnchantmentRegistry(context)
				.getOptional(CustomEnchantmentEffectsTest.WEIRD_IMPALING);
		if (impaling.isEmpty()) {
			throw new GameTestException("Weird Impaling enchantment is not present");
		}

		trident.addEnchantment(impaling.get(), 1);

		player.setStackInHand(Hand.MAIN_HAND, trident);

		context.expectEntityWithData(pos, EntityType.CREEPER, Entity::isOnFire, false);
		player.attack(creeper);
		context.expectEntityWithDataEnd(pos, EntityType.CREEPER, Entity::isOnFire, true);
	}

	@GameTest(templateName = EMPTY_STRUCTURE)
	public void weirdImpalingHasTwoDamageEffects(TestContext context) {
		Enchantment impaling = getEnchantmentRegistry(context).get(CustomEnchantmentEffectsTest.WEIRD_IMPALING);

		if (impaling == null) {
			throw new GameTestException("Weird Impaling enchantment is not present");
		}

		List<EnchantmentEffectEntry<EnchantmentValueEffect>> damageEffects = impaling
				.getEffect(EnchantmentEffectComponentTypes.DAMAGE);

		context.assertTrue(
				damageEffects.size() == 2,
				String.format("Weird Impaling has %d damage effect(s), not the expected 2", damageEffects.size())
		);
		context.complete();
	}

	private static Registry<Enchantment> getEnchantmentRegistry(TestContext context) {
		DynamicRegistryManager registryManager = context.getWorld().getRegistryManager();
		return registryManager.getOrThrow(RegistryKeys.ENCHANTMENT);
	}

	 */
}
