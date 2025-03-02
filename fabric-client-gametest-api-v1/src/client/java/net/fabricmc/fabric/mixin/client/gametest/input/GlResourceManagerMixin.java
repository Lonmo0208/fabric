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

package net.fabricmc.fabric.mixin.client.gametest.input;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlResourceManager;
import net.minecraft.client.texture.DrawableTexture;

import net.fabricmc.fabric.impl.client.gametest.util.WindowHooks;

@Mixin(GlResourceManager.class)
public class GlResourceManagerMixin {
	@WrapOperation(method = "method_68357", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glBlitFrameBuffer(IIIIIIIIII)V"))
	private void blitFrameBuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter, Operation<Void> original, @Local(argsOnly = true) DrawableTexture drawableTexture) {
		if (drawableTexture == MinecraftClient.getInstance().getFramebuffer().getColorAttachment()) {
			WindowHooks window = ((WindowHooks) (Object) MinecraftClient.getInstance().getWindow());
			dstX1 = window.fabric_getRealFramebufferWidth();
			dstY1 = window.fabric_getRealFramebufferHeight();
		}

		original.call(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
	}
}
