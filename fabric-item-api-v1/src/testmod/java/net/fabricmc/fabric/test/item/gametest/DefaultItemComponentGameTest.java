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

public class DefaultItemComponentGameTest {
	/* TODO 1.21.5 tests
	@GameTest(templateName = EMPTY_STRUCTURE)
	public void modify(TestContext context) {
		Consumer<Text> checkText = text -> {
			if (text == null) {
				throw new GameTestException("Item name component not found on gold ingot");
			}

			if (!"Fool's Gold".equals(text.getString())) {
				throw new GameTestException("Item name component on gold ingot is not set");
			}
		};

		Text text = Items.GOLD_INGOT.getComponents().get(DataComponentTypes.ITEM_NAME);
		checkText.accept(text);

		text = new ItemStack(Items.GOLD_INGOT).getComponents().get(DataComponentTypes.ITEM_NAME);
		checkText.accept(text);

		boolean isBeefFood = Items.BEEF.getComponents().contains(DataComponentTypes.FOOD);

		if (isBeefFood) {
			throw new GameTestException("Food component not removed from beef");
		}

		context.complete();
	}

	@GameTest(templateName = EMPTY_STRUCTURE)
	public void afterModify(TestContext context) {
		FireworksComponent fireworksComponent = Items.GOLD_NUGGET.getComponents().get(DataComponentTypes.FIREWORKS);

		if (fireworksComponent == null) {
			throw new GameTestException("Fireworks component not found on gold nugget");
		}

		Boolean enchantGlint = Items.GOLD_NUGGET.getComponents().get(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);

		if (enchantGlint != Boolean.TRUE) {
			throw new GameTestException("Enchantment glint override not set on gold nugget");
		}

		context.complete();
	}

	@GameTest(templateName = EMPTY_STRUCTURE)
	public void diamondPickaxeIsRenamed(TestContext context) {
		Item testItem = Items.DIAMOND_PICKAXE;
		ItemStack stack = testItem.getDefaultStack();

		String itemName = stack.getName().getString();
		String expectedName = "Modified Diamond Pickaxe";

		String errorMessage = "Expected '%s' to be contained in '%s', but it was not!";

		// if they contain each other, then they are equal
		context.assertTrue(itemName.contains(expectedName), errorMessage.formatted(expectedName, itemName));
		context.assertTrue(expectedName.contains(itemName), errorMessage.formatted(itemName, expectedName));
		context.complete();
	}

	 */
}
