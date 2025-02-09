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

package net.fabricmc.fabric.test.rendering.client;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

public class DimensionalRenderingTest implements ClientModInitializer {
	private static final Identifier SKY_TEXTURE = Identifier.ofVanilla("textures/block/dirt.png");
	private static final RenderLayer RENDER_LAYER = RenderLayer.of(
			"fabric-rendering-v1-testmod:void_sky",
			VertexFormats.POSITION_TEXTURE_COLOR,
			VertexFormat.DrawMode.QUADS,
			1536,
			false,
			false,
			RenderLayer.MultiPhaseParameters.builder()
					.program(RenderLayer.POSITION_TEXTURE_COLOR_PROGRAM)
					.texture(new RenderPhase.Texture(SKY_TEXTURE, TriState.FALSE, false))
					.transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
					.writeMaskState(RenderLayer.COLOR_MASK)
					.build(false)
	);

	private static void render(WorldRenderContext context) {
		VertexConsumer vertexConsumer = context.consumers().getBuffer(RENDER_LAYER);
		vertexConsumer.vertex(-100.0f, -100.0f, -100.0f).texture(0.0F, 0.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(-100.0f, -100.0f, 100.0f).texture(0.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, -100.0f, 100.0f).texture(1.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, -100.0f, -100.0f).texture(1.0F, 0.0F).color(255, 255, 255, 255);

		vertexConsumer.vertex(-100.0f, 100.0f, -100.0f).texture(0.0F, 0.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(-100.0f, -100.0f, -99.0f).texture(0.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, -100.0f, -99.0f).texture(1.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, 100.0f, -100.0f).texture(1.0F, 0.0F).color(255, 255, 255, 255);

		vertexConsumer.vertex(-100.0f, -100.0f, 100.0f).texture(0.0F, 0.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(-100.0f, 100.0f, 100.0f).texture(0.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, 100.0f, 100.0f).texture(1.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, -100.0f, 100.0f).texture(1.0F, 0.0F).color(255, 255, 255, 255);

		vertexConsumer.vertex(-100.0f, 100.0f, 101.0f).texture(0.0F, 0.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(-100.0f, 100.0f, -100.0f).texture(0.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, 100.0f, -100.0f).texture(1.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, 100.0f, 100.0f).texture(1.0F, 0.0F).color(255, 255, 255, 255);

		vertexConsumer.vertex(100.0f, -100.0f, -100.0f).texture(0.0F, 0.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, -100.0f, 100.0f).texture(0.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, 100.0f, 100.0f).texture(1.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(100.0f, 100.0f, -100.0f).texture(1.0F, 0.0F).color(255, 255, 255, 255);

		vertexConsumer.vertex(-100.0f, 100.0f, -100.0f).texture(0.0F, 0.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(-100.0f, 100.0f, 100.0f).texture(0.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(-100.0f, -100.0f, 100.0f).texture(1.0F, 1.0F).color(255, 255, 255, 255);
		vertexConsumer.vertex(-100.0f, -100.0f, -100.0f).texture(1.0F, 0.0F).color(255, 255, 255, 255);
	}

	@Override
	public void onInitializeClient() {
		DimensionRenderingRegistry.registerSkyRenderer(RegistryKey.of(RegistryKeys.WORLD, Identifier.of("fabric_dimension", "void")), DimensionalRenderingTest::render);
		DimensionRenderingRegistry.registerDimensionEffects(Identifier.of("fabric_dimension", "void"), new DimensionEffects.End());
	}
}
