package com.teamaurora.borealib.impl.convention_tags;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ConventionTagSynchronizer {

    @ExpectPlatform
    public static void init() {
        Platform.expect();
    }
}
