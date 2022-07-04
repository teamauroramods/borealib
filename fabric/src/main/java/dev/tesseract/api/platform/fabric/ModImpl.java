package dev.tesseract.api.platform.fabric;

import dev.tesseract.api.platform.Mod;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.Person;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class ModImpl implements Mod {

    private final ModContainer container;

    public ModImpl(ModContainer container) {
        this.container = container;
    }

    @Override
    public String getId() {
        return this.container.getMetadata().getId();
    }

    @Override
    public String getVersion() {
        return this.container.getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public String getBrand() {
        return "Fabric";
    }

    @Override
    public String getName() {
        return this.container.getMetadata().getName();
    }

    @Override
    public String getDescription() {
        return this.container.getMetadata().getDescription();
    }

    @Override
    public Collection<String> getAuthors() {
        return this.container.getMetadata().getAuthors().stream().map(Person::getName).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Collection<String> getLicense() {
        return this.container.getMetadata().getLicense();
    }

    @Override
    public Optional<String> getHomepage() {
        return this.container.getMetadata().getContact().get("homepage");
    }

    @Override
    public Optional<String> getSources() {
        return this.container.getMetadata().getContact().get("sources");
    }

    @Override
    public Optional<String> getIssueTracker() {
        return this.container.getMetadata().getContact().get("issues");
    }

    @Override
    public Optional<String> getLogoFile(int size) {
        return this.container.getMetadata().getIconPath(size);
    }

    @Override
    public List<Path> getRootPaths() {
        return this.container.getRootPaths();
    }

    @Override
    public Optional<Path> resolvePath(String... path) {
        return this.container.findPath(String.join("/", path));
    }
}
