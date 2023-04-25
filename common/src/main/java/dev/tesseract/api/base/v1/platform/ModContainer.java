package dev.tesseract.api.base.v1.platform;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A wrapper for platform-specific mod containers to query common info.
 *
 * @author ebo2022
 * @since 1.0.0
 */
public interface ModContainer {

    /**
     * @return The brand of the mod (Forge or Fabric)
     */
    String getBrand();

    /**
     * @return A list of all possible root paths for the mod
     */
    List<Path> getRootPaths();

    /**
     * Resolves a path inside the mod file.
     *
     * @param path The path to search for
     * @return The path if it exists, otherwise returns {@link Optional#empty()}
     */
    Optional<Path> resolvePath(String... path);

    /**
     * @return The name of the mod
     */
    String getName();

    /**
     * @return The mod id
     */
    String getId();

    /**
     * @return The version of the mod
     */
    String getVersion();

    /**
     * Finds the logo file for the mod if one is present.
     *
     * @param size The preferred size for the logo file (only used on Fabric)
     * @return The logo file
     */
    Optional<String> getLogoFile(int size);

    /**
     * @return The description of the mod
     */
    String getDescription();

    /**
     * @return The authors of the mod
     */
    Stream<String> getAuthors();

    /**
     * @return The license of the mod, if any is present
     */
    @Nullable
    Collection<String> getLicense();

    /**
     * @return The homepage of the mod if it is present
     */
    Optional<String> getHomepage();

    /**
     * @return The issue URL for the mod if it has one
     */
    Optional<String> getIssueTracker();

    /**
     * @return The visible display name of the mod
     */
    default String getDisplayName() {
        if (this.getName() != null) {
            return this.getName();
        } else {
            return this.getBrand() + " Mod \"" + this.getId() + "\"";
        }
    }
}