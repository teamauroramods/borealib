package dev.tesseract.api.platform.forge;

import dev.tesseract.api.platform.ModContainer;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModFileInfo;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ModContainerImpl implements ModContainer {

    private final net.minecraftforge.fml.ModContainer parent;

    public ModContainerImpl(net.minecraftforge.fml.ModContainer parent) {
        this.parent = parent;
    }

    @Override
    public String getBrand() {
        return "Forge";
    }

    @Override
    public List<Path> getRootPaths() {
        return List.of(this.parent.getModInfo().getOwningFile().getFile().getSecureJar().getRootPath());
    }

    @Override
    public Optional<Path> resolvePath(String... path) {
        return Optional.of(this.parent.getModInfo().getOwningFile().getFile().findResource(path)).filter(Files::exists);
    }

    @Override
    public String getName() {
        return this.parent.getModInfo().getDisplayName();
    }

    @Override
    public String getId() {
        return this.parent.getModId();
    }

    @Override
    public String getVersion() {
        return this.parent.getModInfo().getVersion().getQualifier();
    }

    @Override
    public Optional<String> getLogoFile(int size) {
        return this.parent.getModInfo().getLogoFile();
    }

    @Override
    public String getDescription() {
        return this.parent.getModInfo().getDescription();
    }

    @Override
    public Stream<String> getAuthors() {
        return this.parent.getModInfo().getConfig().getConfigElement("authors").map(String::valueOf).stream();
    }

    @Override
    public @Nullable Collection<String> getLicense() {
        return Collections.singleton(this.parent.getModInfo().getOwningFile().getLicense());
    }

    @Override
    public Optional<String> getHomepage() {
        return this.parent.getModInfo().getConfig().getConfigElement("displayURL").map(String::valueOf);
    }

    @Override
    public Optional<String> getIssueTracker() {
        IModFileInfo owningFile = this.parent.getModInfo().getOwningFile();
        if (owningFile instanceof ModFileInfo info) {
            return Optional.ofNullable(info.getIssueURL()).map(URL::toString);
        }
        return Optional.empty();
    }
}
