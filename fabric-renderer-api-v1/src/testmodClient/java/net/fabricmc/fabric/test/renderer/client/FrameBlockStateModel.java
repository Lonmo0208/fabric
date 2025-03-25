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

package net.fabricmc.fabric.test.renderer.client;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadTransform;

public class FrameBlockStateModel implements BlockStateModel {
	private final Mesh frameMesh;
	private final Sprite frameSprite;
	private final RenderMaterial translucentMaterial;
	private final RenderMaterial translucentEmissiveMaterial;

	public FrameBlockStateModel(Mesh frameMesh, Sprite frameSprite) {
		this.frameMesh = frameMesh;
		this.frameSprite = frameSprite;

		MaterialFinder finder = Renderer.get().materialFinder();
		this.translucentMaterial = finder.blendMode(BlendMode.TRANSLUCENT).find();
		finder.clear();
		this.translucentEmissiveMaterial = finder.blendMode(BlendMode.TRANSLUCENT).emissive(true).find();
	}

	@Override
	public void emitQuads(QuadEmitter emitter, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
		// Emit our frame mesh
		frameMesh.outputTo(emitter);

		// We should not access the block entity from here. We should instead use the immutable render data provided by the block entity.
		if (!(((FabricBlockView) blockView).getBlockEntityRenderData(pos) instanceof Block mimickedBlock)) {
			return; // No inner block to render, or data of wrong type
		}

		BlockState innerState = mimickedBlock.getDefaultState();
		BlockStateModel innerModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(innerState);

		// Now, we emit a transparent scaled-down version of the inner model
		// Try both emissive and non-emissive versions of the translucent material
		RenderMaterial material = pos.getX() % 2 == 0 ? translucentMaterial : translucentEmissiveMaterial;

		// Let's push a transform to scale the model down and make it transparent
		emitter.pushTransform(createInnerTransform(material));
		// Emit the inner block model
		innerModel.emitQuads(emitter, blockView, pos, state, random, cullTest);
		// Let's not forget to pop the transform!
		emitter.popTransform();
	}

	/**
	 * Create a transform to scale down the model, make it translucent, and assign the given material.
	 */
	private static QuadTransform createInnerTransform(RenderMaterial material) {
		return quad -> {
			// Scale model down
			for (int vertex = 0; vertex < 4; ++vertex) {
				float x = quad.x(vertex) * 0.8f + 0.1f;
				float y = quad.y(vertex) * 0.8f + 0.1f;
				float z = quad.z(vertex) * 0.8f + 0.1f;
				quad.pos(vertex, x, y, z);
			}

			// Make the quad partially transparent
			// Change material to translucent
			quad.material(material);

			// Change vertex colors to be partially transparent
			for (int vertex = 0; vertex < 4; ++vertex) {
				int color = quad.color(vertex);
				int alpha = (color >> 24) & 0xFF;
				alpha = alpha * 3 / 4;
				color = (color & 0xFFFFFF) | (alpha << 24);
				quad.color(vertex, color);
			}

			// Return true because we want the quad to be rendered
			return true;
		};
	}

	@Override
	@Nullable
	public Object createGeometryKey(BlockRenderView blockView, BlockPos pos, BlockState state, Random random) {
		// We should not access the block entity from here. We should instead use the immutable render data provided by the block entity.
		if (!(((FabricBlockView) blockView).getBlockEntityRenderData(pos) instanceof Block mimickedBlock)) {
			return this; // No inner block to render, or data of wrong type
		}

		BlockState innerState = mimickedBlock.getDefaultState();
		BlockStateModel innerModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(innerState);
		Object subkey = innerModel.createGeometryKey(blockView, pos, state, random);

		if (subkey == null) {
			return null;
		}

		record Key(Object subkey, boolean notEmissive) {
		}

		return new Key(subkey, pos.getX() % 2 == 0);
	}

	@Override
	public void addParts(Random random, List<BlockModelPart> parts) {
		// Renderer API makes this obsolete, so don't add any parts
	}

	@Override
	public Sprite particleSprite() {
		return frameSprite;
	}

	@Override
	public Sprite particleSprite(BlockRenderView blockView, BlockPos pos, BlockState state) {
		// We should not access the block entity from here. We should instead use the immutable render data provided by the block entity.
		if (!(((FabricBlockView) blockView).getBlockEntityRenderData(pos) instanceof Block mimickedBlock)) {
			return particleSprite(); // No inner block to render, or data of wrong type
		}

		BlockState innerState = mimickedBlock.getDefaultState();
		BlockStateModel innerModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(innerState);
		return innerModel.particleSprite(blockView, pos, state);
	}
}
