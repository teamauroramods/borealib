package com.teamaurora.borealib.core.extensions.forge;

import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.Future;

@ApiStatus.Internal
public interface FMLHandshakeHandlerExtension {

    void borealib$addWait(Future<?> wait);
}