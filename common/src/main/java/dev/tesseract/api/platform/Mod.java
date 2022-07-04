package dev.tesseract.api.platform;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * An interface for querying mod info shared across both platforms.
 *
 * @author ebo2022
 * @since 1.0.0
 */
public interface Mod {

    /**
     * @return The ID of the mod
     */
    String getId();

    /**
     * @return The version of the mod
     */
    String getVersion();

    /**
     * @return The brand of the mod, returns either "Forge" or "Fabric"
     */
    String getBrand();

    /**
     * @return The name of the mod
     */
    String getName();

    /**
     * @return The description of the mod
     */
    String getDescription();

    /**
     * @return The authors of the mod
     */
    Collection<String> getAuthors();

    /**
     * @return The license of the mod
     */
    @Nullable
    Collection<String> getLicense();

    /**
     * @return The homepage URL of the mod, if applicable
     */
    Optional<String> getHomepage();

    /**
     * @return The source URL of the mod, if applicable
     */
    Optional<String> getSources();

    /**
     * @return The issue tracker URL of the mod, if applicable
     */
    Optional<String> getIssueTracker();

    /**
     * Gets the file path for the mod logo.
     *
     * @param size The preferred size of the logo (<i>this is only used on Fabric</i>)
     * @return The logo path, relative to the file
     */
    Optional<String> getLogoFile(int size);

    /**
     * @return A list of all possible root paths for the mod
     */
    List<Path> getRootPaths();

    /**
     * Resolves a path inside the mod file.
     *
     * @param path The path to search for
     * @return The resource path if it exists, otherwise {@link Optional#empty()}
     */
    Optional<Path> resolvePath(String... path);

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
