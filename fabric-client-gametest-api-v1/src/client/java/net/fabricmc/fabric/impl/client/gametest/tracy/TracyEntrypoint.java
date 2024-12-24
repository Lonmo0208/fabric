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

package net.fabricmc.fabric.impl.client.gametest.tracy;

import java.nio.file.Path;
import java.util.Arrays;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class TracyEntrypoint implements PreLaunchEntrypoint {
	private static final boolean ENABLED = System.getProperty("fabric.client.gametest") != null;

	private static TracyCapture tracyCapture;

	@Override
	public void onPreLaunch() {
		if (!ENABLED) {
			return;
		}

		if (!Arrays.asList(FabricLoader.getInstance().getLaunchArguments(true)).contains("--tracy")) {
			return;
		}

		Path output = FabricLoader.getInstance().getGameDir().resolve("profile.tracy");
		tracyCapture = new TracyCapture(output);
		tracyCapture.startCapture();
	}
}
