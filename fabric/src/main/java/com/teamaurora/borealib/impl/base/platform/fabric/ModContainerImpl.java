package com.teamaurora.borealib.impl.base.platform.fabric;

import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import net.fabricmc.loader.api.metadata.Person;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ApiStatus.Internal
public class ModContainerImpl implements ModContainer {

    private final net.fabricmc.loader.api.ModContainer parent;

    public ModContainerImpl(net.fabricmc.loader.api.ModContainer parent) {
        this.parent = parent;
    }

    @Override
    public String getBrand() {
        return "Fabric";
    }

    @Override
    public List<Path> getRootPaths() {
        return this.parent.getRootPaths();
    }

    @Override
    public Optional<Path> resolvePath(String... path) {
        return this.parent.findPath(String.join("/", path));
    }

    @Override
    public String getName() {
        return this.parent.getMetadata().getName();
    }

    @Override
    public String getId() {
        return this.parent.getMetadata().getId();
    }

    @Override
    public String getVersion() {
        return this.parent.getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public Optional<String> getLogoFile(int size) {
        return this.parent.getMetadata().getIconPath(size);
    }

    @Override
    public String getDescription() {
        return this.parent.getMetadata().getDescription();
    }

    @Override
    public Stream<String> getAuthors() {
        return this.parent.getMetadata().getAuthors().stream().map(Person::getName);
    }

    @Override
    public @Nullable Collection<String> getLicense() {
        return this.parent.getMetadata().getLicense();
    }

    @Override
    public Optional<String> getHomepage() {
        return this.parent.getMetadata().getContact().get("homepage");
    }

    @Override
    public Optional<String> getIssueTracker() {
        return this.parent.getMetadata().getContact().get("issues");
    }
}
