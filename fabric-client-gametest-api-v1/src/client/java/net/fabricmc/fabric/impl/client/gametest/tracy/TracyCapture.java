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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracyCapture {
	private static final Logger LOGGER = LoggerFactory.getLogger(TracyCapture.class);
	private final Path output;

	@Nullable
	private Process process = null;

	public TracyCapture(Path output) {
		this.output = output;
	}

	public void startCapture() {
		try {
			Files.createDirectories(output.getParent());
			LOGGER.info("Starting tracy-capture");
			process = createProcess().start();
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to start tracy-capture", e);
		}

		// Send ctrl+c on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (process != null) {
				LOGGER.info("Stopping tracy-capture");

				try {
					// Send ctrl+c
					long pid = process.pid(); // Requires Java 9+
					ProcessBuilder killProcess = new ProcessBuilder("kill", "-SIGINT", String.valueOf(pid));
					killProcess.inheritIO().start();

					process.waitFor();
					LOGGER.info("tracy-capture finished with exit code {}", process.exitValue());
					LOGGER.info(new String(process.getInputStream().readAllBytes()));
				} catch (InterruptedException e) {
					LOGGER.error("tracy-capture was interrupted", e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}));
	}

	private ProcessBuilder createProcess() {
		return new ProcessBuilder()
		.command(
			"/home/linuxbrew/.linuxbrew/Cellar/tracy/0.11.1/tracy-capture",
			"-o", output.toAbsolutePath().toString(),
			"-a", "127.0.0.1",
			"-f"
		);
	}
}
