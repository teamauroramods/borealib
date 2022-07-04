package dev.tesseract.api.platform.forge;

import dev.tesseract.api.platform.Mod;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApiStatus.Internal
public class ModImpl implements Mod {
    private final ModContainer container;

    public ModImpl(ModContainer container) {
        this.container = container;
    }

    @Override
    public String getId() {
        return this.container.getModInfo().getModId();
    }

    @Override
    public String getVersion() {
        return this.container.getModInfo().getVersion().getQualifier();
    }

    @Override
    public String getBrand() {
        return "Forge";
    }

    @Override
    public String getName() {
        return this.container.getModInfo().getDisplayName();
    }

    @Override
    public String getDescription() {
        return this.container.getModInfo().getDescription();
    }

    @Override
    public Collection<String> getAuthors() {
        Optional<String> optional = this.container.getModInfo().getConfig().getConfigElement("authors")
                .map(String::valueOf);
        return optional.isPresent() ? Collections.singleton(optional.get()) : Collections.emptyList();
    }

    @Nullable
    @Override
    public Collection<String> getLicense() {
        return Collections.singleton(this.container.getModInfo().getOwningFile().getLicense());
    }

    @Override
    public Optional<String> getHomepage() {
        return this.container.getModInfo().getConfig().getConfigElement("displayURL").map(String::valueOf);
    }

    @Override
    public Optional<String> getSources() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getIssueTracker() {
        if (this.container.getModInfo().getOwningFile() instanceof ModFileInfo info) {
            return Optional.ofNullable(info.getIssueURL()).map(URL::toString);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getLogoFile(int size) {
        return this.container.getModInfo().getLogoFile();
    }

    @Override
    public List<Path> getRootPaths() {
        return List.of(this.container.getModInfo().getOwningFile().getFile().getSecureJar().getRootPath());
    }

    @Override
    public Optional<Path> resolvePath(String... path) {
        return Optional.of(this.container.getModInfo().getOwningFile().getFile().findResource(path)).filter(Files::exists);
    }
}
