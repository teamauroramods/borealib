package com.teamaurora.borealib.api.block.v1.set.variant;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService.ParallelDispatcher;
import com.teamaurora.borealib.api.block.v1.set.BlockSet;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A "template" to build items (not associated with a block) that belong to a {@link BlockSet}. Any previously added variants can be looked up during construction.
 *
 * @param <T> The block set type
 * @author ebo2022
 * @since 1.0
 */
public final class ItemVariant<T> {

    private final boolean automaticLang;
    private final String prefix;
    private final String suffix;
    private final Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit;
    private final Consumer<Item> onRegister;
    private final BlockSet.ComponentFactory<Item, T> factory;

    private ItemVariant(boolean automaticLang, String prefix, String suffix,
                        Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit,
                        Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit,
                        Consumer<Item> onRegister,
                        BlockSet.ComponentFactory<Item, T> factory) {
        this.automaticLang = automaticLang;
        this.prefix = prefix;
        this.suffix = suffix;
        this.clientInit = clientInit;
        this.clientPostInit = clientPostInit;
        this.onRegister = onRegister;
        this.factory = factory;
    }

    /**
     * Creates a new item variant builder.
     *
     * @param factory A factory to build the item
     * @param <T> The block set type
     * @return A new builder
     */
    public static <T> Builder<T> builder(BlockSet.ComponentFactory<Item, T> factory) {
        return new Builder<>(factory);
    }

    /**
     * Creates a prefixed & suffixed location for a template object of this variant.
     *
     * @param modid    The object namespace
     * @param baseName The root name to prefix and suffix
     * @return A prefixed and suffixed {@link ResourceLocation} to use
     */
    public ResourceLocation createName(String modid, String baseName) {
        String s = "";
        if(!this.prefix.isEmpty()) s += this.prefix + "_";
        s += baseName;
        if(!this.suffix.isEmpty()) s += "_" + this.suffix;
        return new ResourceLocation(modid, s);
    }

    /**
     * @return Whether data generators should auto-generate en_us language for this variant
     */
    public boolean shouldGenerateAutoLanguage() {
        return this.automaticLang;
    }

    /**
     * @return A prefix for this variant; it may be empty
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * @return A suffix for this variant; it may be empty
     */
    public String getSuffix() {
        return this.suffix;
    }

    /**
     * @return Code to run for this variant during client init
     */
    public Supplier<BiConsumer<T, RegistryReference<Item>>> getClientInit() {
        return this.clientInit;
    }

    /**
     * @return Code to run for this variant during client post-init
     */
    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> getClientPostInit() {
        return this.clientPostInit;
    }

    /**
     * @return Code to run immediately after an object of this variant is registered
     */
    public Consumer<Item> getOnRegister() {
        return this.onRegister;
    }

    /**
     * @return A factory to construct an object of this variant
     */
    public BlockSet.ComponentFactory<Item, T> getFactory() {
        return this.factory;
    }

    /**
     * Used to construct a new item variant template.
     *
     * @param <T> The block set type
     * @since 1.0
     */
    public static final class Builder<T> {

        private boolean automaticLang = true;
        private String prefix = "";
        private String suffix = "";
        private Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit;
        private Consumer<Item> onRegister;
        private final BlockSet.ComponentFactory<Item, T> factory;

        private Builder(BlockSet.ComponentFactory<Item, T> factory) {
            this.factory = factory;
        }

        /**
         * Disable data generators automatically generating en_us language for this variant.
         */
        public Builder<T> disableAutomaticLanguageGen() {
            this.automaticLang = false;
            return this;
        }

        /**
         * Adds a prefix for any objects of this variant.
         *
         * @param prefix The prefix to use
         */
        public Builder<T> prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Adds a suffix for any objects of this variant.
         *
         * @param suffix The prefix to use
         */
        public Builder<T> suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        /**
         * Adds code to run during client init for each member of this variant.
         *
         * @param clientInit The code to run
         */
        public Builder<T> clientInit(Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit) {
            this.clientInit = clientInit;
            return this;
        }

        /**
         * Adds code to run during client post-init for each member of this variant.
         *
         * @param clientPostInit The code to run
         */
        public Builder<T> clientPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit) {
            this.clientPostInit = clientPostInit;
            return this;
        }

        /**
         * Adds code to run when a member of this variant is guaranteed to be registered.
         *
         * @param onRegister The code to run
         */
        public Builder<T> onRegister(Consumer<Item> onRegister) {
            this.onRegister = onRegister;
            return this;
        }

        /**
         * Builds this variant.
         *
         * @return A new item variant
         */
        public ItemVariant<T> build() {
            return new ItemVariant<>(
                    this.automaticLang,
                    this.prefix,
                    this.suffix,
                    this.clientInit,
                    this.clientPostInit,
                    this.onRegister,
                    this.factory);
        }
    }
}
