package com.teamaurora.borealib.api.block.v1.set.variant;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService.ParallelDispatcher;
import com.teamaurora.borealib.api.block.v1.set.BlockSet;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A "template" to build blocks that belong to a {@link BlockSet}. Any previously added variants can be looked up during construction.
 *
 * @param <T> The block set type
 * @author ebo2022
 * @since 1.0
 */
public final class BlockVariant<T> {

    private final BiFunction<T, Block, ? extends BlockItem> blockItemFactory;
    private final boolean automaticLang;
    private final String prefix;
    private final String suffix;
    private final Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit;
    private final Supplier<BiConsumer<T, RegistryReference<Item>>> itemClientInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> itemClientPostInit;
    private final Consumer<Block> onRegister;
    private final BlockSet.ComponentFactory<Block, T> factory;

    private BlockVariant(BiFunction<T, Block, ? extends BlockItem> blockItemFactory, boolean automaticLang, String prefix, String suffix,
                         Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit,
                         Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit,
                         Supplier<BiConsumer<T, RegistryReference<Item>>> itemClientInit,
                         Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> itemClientPostInit,
                         Consumer<Block> onRegister,
                         BlockSet.ComponentFactory<Block, T> factory) {
        this.blockItemFactory = blockItemFactory;
        this.automaticLang = automaticLang;
        this.prefix = prefix;
        this.suffix = suffix;
        this.clientInit = clientInit;
        this.clientPostInit = clientPostInit;
        this.itemClientInit = itemClientInit;
        this.itemClientPostInit = itemClientPostInit;
        this.onRegister = onRegister;
        this.factory = factory;
    }

    /**
     * Creates a new block variant builder.
     *
     * @param factory A factory to build the block
     * @param <T> The block set type
     * @return A new builder
     */
    public static <T> Builder<T> builder(BlockSet.ComponentFactory<Block, T> factory) {
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
     * @return Whether this variant has a corresponding block item
     */
    public boolean hasBlockItem() {
        return this.blockItemFactory != null;
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
    public Supplier<BiConsumer<T, RegistryReference<Block>>> getClientInit() {
        return this.clientInit;
    }

    /**
     * @return Code to run for this variant during client post-init
     */
    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> getClientPostInit() {
        return this.clientPostInit;
    }

    /**
     * @return Code to run for this variant's block item during client init
     */
    public Supplier<BiConsumer<T, RegistryReference<Item>>> getItemClientInit() {
        return this.itemClientInit;
    }

    /**
     * @return Code to run for this variant's block item during client post-init
     */
    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> getItemClientPostInit() {
        return this.itemClientPostInit;
    }

    /**
     * @return A factory to generate this variant's block item
     */
    public BiFunction<T, Block, ? extends BlockItem> getBlockItemFactory() {
        return this.blockItemFactory;
    }

    /**
     * @return Code to run immediately after an object of this variant is registered
     */
    public Consumer<Block> getOnRegister() {
        return this.onRegister;
    }

    /**
     * @return A factory to construct an object of this variant
     */
    public BlockSet.ComponentFactory<Block, T> getFactory() {
        return this.factory;
    }

    /**
     * Used to construct a new block variant template.
     *
     * @param <T> The block set type
     * @since 1.0
     */
    public static final class Builder<T> {

        private boolean automaticLang = true;
        private String prefix = "";
        private String suffix = "";
        private Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit;
        private Supplier<BiConsumer<T, RegistryReference<Item>>> itemClientInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> itemClientPostInit;
        private Consumer<Block> onRegister;
        private final BlockSet.ComponentFactory<Block, T> factory;
        private BiFunction<T, Block, ? extends BlockItem> blockItemFactory = (set, block) -> new BlockItem(block, new Item.Properties());

        private Builder(BlockSet.ComponentFactory<Block, T> factory) {
            this.factory = factory;
        }

        /**
         * Specify that no block item should be generated for this variant.
         */
        public Builder<T> noBlockItem() {
            this.blockItemFactory = null;
            return this;
        }

        /**
         * Sets the factory to generate the block item for this variant.
         *
         * @param blockItemFactory The block item factory
         */
        public Builder<T> blockItemFactory(BiFunction<T, Block, ? extends BlockItem> blockItemFactory) {
            this.blockItemFactory = blockItemFactory;
            return this;
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
         * @param suffix The suffix to use
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
        public Builder<T> clientInit(Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit) {
            this.clientInit = clientInit;
            return this;
        }

        /**
         * Adds code to run during client post-init for each member of this variant.
         *
         * @param clientPostInit The code to run
         */
        public Builder<T> clientPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit) {
            this.clientPostInit = clientPostInit;
            return this;
        }

        /**
         * Adds code to run during client init for each block item.
         *
         * @param clientInit The code to run
         */
        public Builder<T> itemClientInit(Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit) {
            this.itemClientInit = clientInit;
            return this;
        }

        /**
         * Adds code to run during client post-init for each block item.
         *
         * @param clientPostInit The code to run
         */
        public Builder<T> itemClientPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit) {
            this.itemClientPostInit = clientPostInit;
            return this;
        }

        /**
         * Adds code to run when a member of this variant is guaranteed to be registered.
         *
         * @param onRegister The code to run
         */
        public Builder<T> onRegister(Consumer<Block> onRegister) {
            this.onRegister = onRegister;
            return this;
        }

        /**
         * Builds the variant.
         *
         * @return A new block variant
         */
        public BlockVariant<T> build() {
            return new BlockVariant<>(this.blockItemFactory,
                    this.automaticLang,
                    this.prefix,
                    this.suffix,
                    this.clientInit,
                    this.clientPostInit,
                    this.itemClientInit,
                    this.itemClientPostInit,
                    this.onRegister,
                    this.factory);
        }
    }
}
