package com.teamaurora.borealib.api.block.v1.set.variant;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService.ParallelDispatcher;
import com.teamaurora.borealib.api.block.v1.set.BlockSet;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a block in a {@link BlockSet}. It may or may not have a block-item associated with it.
 *
 * @param <T> The block set type
 */
public final class BlockVariant<T> {

    private final boolean hasBlockItem;
    private final boolean automaticLang;
    private final String prefix;
    private final String suffix;
    private final Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit;
    private final Consumer<Block> onRegister;
    private final BlockSet.ComponentFactory<Block, T> factory;

    private BlockVariant(boolean hasBlockItem, boolean automaticLang, String prefix, String suffix,
                         Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit,
                         Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit,
                         Consumer<Block> onRegister,
                         BlockSet.ComponentFactory<Block, T> factory) {
        this.hasBlockItem = hasBlockItem;
        this.automaticLang = automaticLang;
        this.prefix = prefix;
        this.suffix = suffix;
        this.clientInit = clientInit;
        this.clientPostInit = clientPostInit;
        this.onRegister = onRegister;
        this.factory = factory;
    }

    public static <T> Builder<T> builder(BlockSet.ComponentFactory<Block, T> factory) {
        return new Builder<>(factory);
    }

    public ResourceLocation createName(String modid, String baseName) {
        String s = "";
        if(!this.prefix.isEmpty()) s += this.prefix + "_";
        s += baseName;
        if(!this.suffix.isEmpty()) s += "_" + this.suffix;
        return new ResourceLocation(modid, s);
    }

    public boolean hasBlockItem() {
        return this.hasBlockItem;
    }

    public boolean shouldGenerateAutoLanguage() {
        return this.automaticLang;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Supplier<BiConsumer<T, RegistryReference<Block>>> getClientInit() {
        return this.clientInit;
    }

    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> getClientPostInit() {
        return this.clientPostInit;
    }

    public Consumer<Block> getOnRegister() {
        return this.onRegister;
    }

    public BlockSet.ComponentFactory<Block, T> getFactory() {
        return this.factory;
    }

    public static final class Builder<T> {

        private boolean hasBlockItem = true;
        private boolean automaticLang = true;
        private String prefix = "";
        private String suffix = "";
        private Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit;
        private Consumer<Block> onRegister;
        private final BlockSet.ComponentFactory<Block, T> factory;

        private Builder(BlockSet.ComponentFactory<Block, T> factory) {
            this.factory = factory;
        }

        public Builder<T> noBlockItem() {
            this.hasBlockItem = false;
            return this;
        }

        public Builder<T> disableAutomaticLanguageGen() {
            this.automaticLang = false;
            return this;
        }

        public Builder<T> prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder<T> suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder<T> clientInit(Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit) {
            this.clientInit = clientInit;
            return this;
        }

        public Builder<T> clientPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit) {
            this.clientPostInit = clientPostInit;
            return this;
        }

        public Builder<T> onRegister(Consumer<Block> onRegister) {
            this.onRegister = onRegister;
            return this;
        }

        public BlockVariant<T> build() {
            return new BlockVariant<>(this.hasBlockItem,
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
