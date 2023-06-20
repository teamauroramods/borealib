package com.teamaurora.borealib.api.base.v1.platform;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Allows some of Forge's <code>DistExecutor</code> methods to work on both platforms.
 *
 * @see <a href=https://github.com/MinecraftForge/MinecraftForge/blob/1.20.x/fmlcore/src/main/java/net/minecraftforge/fml/DistExecutor.java>DistExecutor</a>
 * @author ebo2022
 * @since 1.0
 */
public final class EnvExecutor {
    
    public static void runInEnv(Environment type, Supplier<Runnable> runnableSupplier) {
        if (Platform.getEnvironment() == type) {
            runnableSupplier.get().run();
        }
    }

    public static <T> T unsafeCallWhenOn(Environment environment, Supplier<Callable<T>> toRun) {
        if (environment == Platform.getEnvironment()) {
            try {
                return toRun.get().call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static void unsafeRunWhenOn(Environment environment, Supplier<Runnable> toRun) {
        if (environment == Platform.getEnvironment()) {
            toRun.get().run();
        }
    }
    
    public static <T> T getEnvSpecific(Supplier<Supplier<T>> client, Supplier<Supplier<T>> server) {
        if (Platform.isClient()) {
            return client.get().get();
        } else {
            return server.get().get();
        }
    }
    
    private EnvExecutor() {
    }
}