package com.teamaurora.borealib.core.extensions;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface TagBuilderExtension {

    void borealib$setReplace(boolean replace);

    boolean borealib$replace();
}
