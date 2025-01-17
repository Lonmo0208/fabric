package net.fabricmc.fabric.api.gametest.v1;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.util.BlockRotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface FabricGameTest {
	String environment() default "minecraft:default";

	String structure() default "minecraft:empty";

	int maxTicks() default 20;

	int setupTicks() default 0;

	boolean required() default true;

	BlockRotation rotation() default BlockRotation.NONE;

	boolean manualOnly() default false;

	int maxAttempts() default 1;

	int requiredSuccesses() default 1;

	boolean skyAccess() default false;
}
