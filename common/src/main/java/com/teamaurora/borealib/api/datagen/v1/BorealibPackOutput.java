package com.teamaurora.borealib.api.datagen.v1;

import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link PackOutput} extension that also stores the current mod whose data is being generated.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class BorealibPackOutput extends PackOutput {

    private final ModContainer container;

    @ApiStatus.Internal
    public BorealibPackOutput(ModContainer container, PackOutput packOutput) {
        super(packOutput.getOutputFolder());
        this.container = container;
    }

    /**
     * @return The {@link ModContainer} for the data being generated
     */
    public ModContainer getModContainer() {
        return this.container;
    }

    /**
     * @return The id of the mod whose data is being generated
     */
    public String getModId() {
        return this.container.getId();
    }
}
