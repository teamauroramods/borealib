package com.teamaurora.borealib.api.base.v1.platform;

import net.fabricmc.api.EnvType;

/**
 * Represents the current game environment.
 *
 * @author ebo2022
 * @since 1.0
 */
public enum Environment {

    CLIENT,
    SERVER;

    public static Environment fromPlatform(Object type) {
        return type == EnvType.CLIENT ? CLIENT : type == EnvType.SERVER ? SERVER : null;
    }

    public EnvType toPlatform() {
        return this == CLIENT ? EnvType.CLIENT : EnvType.SERVER;
    }
}