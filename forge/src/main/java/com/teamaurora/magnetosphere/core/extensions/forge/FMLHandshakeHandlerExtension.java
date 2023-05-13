package com.teamaurora.magnetosphere.core.extensions.forge;

import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.Future;

@ApiStatus.Internal
public interface FMLHandshakeHandlerExtension {

    void magnetosphere$addWait(Future<?> wait);
}