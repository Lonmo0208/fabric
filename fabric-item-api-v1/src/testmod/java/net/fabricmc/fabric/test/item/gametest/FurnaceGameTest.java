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

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

public class FurnaceGameTest {
	private static final int COOK_TIME = 200;
	private static final BlockPos POS = new BlockPos(0, 1, 0);

	/* TODO 1.21.5 tests
	@GameTest(templateName = EMPTY_STRUCTURE)
	public void basicSmelt(TestContext context) {
		context.setBlockState(POS, Blocks.FURNACE);
		FurnaceBlockEntity blockEntity = context.getBlockEntity(POS);

		setInputs(blockEntity, new ItemStack(Blocks.COBBLESTONE, 8), new ItemStack(Items.COAL, 2));

		cook(blockEntity, context, 1);
		assertInventory(blockEntity, "Testing vanilla smelting.",
				new ItemStack(Blocks.COBBLESTONE, 7),
				new ItemStack(Items.COAL, 1),
				new ItemStack(Blocks.STONE, 1));

		cook(blockEntity, context, 7);
		assertInventory(blockEntity, "Testing vanilla smelting.",
				ItemStack.EMPTY,
				new ItemStack(Items.COAL, 1),
				new ItemStack(Blocks.STONE, 8));

		context.complete();
	}

	@GameTest(templateName = EMPTY_STRUCTURE)
	public void vanillaRemainderTest(TestContext context) {
		context.setBlockState(POS, Blocks.FURNACE);
		FurnaceBlockEntity blockEntity = context.getBlockEntity(POS);

		setInputs(blockEntity, new ItemStack(Blocks.COBBLESTONE, 64), new ItemStack(Items.LAVA_BUCKET));

		cook(blockEntity, context, 64);
		assertInventory(blockEntity, "Testing vanilla smelting recipe remainder.",
				ItemStack.EMPTY,
				new ItemStack(Items.BUCKET),
				new ItemStack(Blocks.STONE, 64));

		context.complete();
	}

	@GameTest(templateName = EMPTY_STRUCTURE)
	public void fabricRemainderTest(TestContext context) {
		context.setBlockState(POS, Blocks.FURNACE);
		FurnaceBlockEntity blockEntity = context.getBlockEntity(POS);

		setInputs(blockEntity, new ItemStack(Blocks.COBBLESTONE, 32), new ItemStack(CustomDamageTest.WEIRD_PICK));

		cook(blockEntity, context, 1);
		assertInventory(blockEntity, "Testing fabric smelting recipe remainder.",
				new ItemStack(Blocks.COBBLESTONE, 31),
				RecipeGameTest.withDamage(new ItemStack(CustomDamageTest.WEIRD_PICK), 1),
				new ItemStack(Blocks.STONE, 1));

		cook(blockEntity, context, 30);
		assertInventory(blockEntity, "Testing fabric smelting recipe remainder.",
				new ItemStack(Blocks.COBBLESTONE, 1),
				RecipeGameTest.withDamage(new ItemStack(CustomDamageTest.WEIRD_PICK), 31),
				new ItemStack(Blocks.STONE, 31));

		cook(blockEntity, context, 1);
		assertInventory(blockEntity, "Testing fabric smelting recipe remainder.",
				ItemStack.EMPTY,
				ItemStack.EMPTY,
				new ItemStack(Blocks.STONE, 32));

		context.complete();
	}

	 */

	private void setInputs(FurnaceBlockEntity blockEntity, ItemStack ingredient, ItemStack fuel) {
		blockEntity.setStack(0, ingredient);
		blockEntity.setStack(1, fuel);
	}

	private void assertInventory(FurnaceBlockEntity blockEntity, String extraErrorInfo, ItemStack... stacks) {
		for (int i = 0; i < stacks.length; i++) {
			ItemStack currentStack = blockEntity.getStack(i);
			ItemStack expectedStack = stacks[i];

			throw new AssertionError("Not implemented yet");
			// RecipeGameTest.assertStacks(currentStack, expectedStack, extraErrorInfo);
		}
	}

	private void cook(FurnaceBlockEntity blockEntity, TestContext context, int items) {
		for (int i = 0; i < COOK_TIME * items; i++) {
			AbstractFurnaceBlockEntity.tick(context.getWorld(), POS, context.getBlockState(POS), blockEntity);
		}
	}
}
