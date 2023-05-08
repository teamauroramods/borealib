package com.teamaurora.magnetosphere.api.block.set.variant;

import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService.ParallelDispatcher;
import com.teamaurora.magnetosphere.api.block.set.BlockSet;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a standalone item that is related to a {@link BlockSet}. It is registered after all blocks and block-items.
 *
 * @param <T> The block set type
 */
public final class ItemVariant<T> {

    private final boolean automaticLang;
    private final String prefix;
    private final String suffix;
    private final Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit;
    private final Supplier<BiConsumer<T, RegistryReference<Block>>> serverInit;
    private final TriConsumer<ParallelDispatcher, T, RegistryReference<Block>> commonPostInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> serverPostInit;
    private final Consumer<Item> onRegister;
    private final BlockSet.ComponentFactory<Item, T> factory;

    private ItemVariant(boolean automaticLang, String prefix, String suffix,
                        Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit,
                        Supplier<BiConsumer<T, RegistryReference<Block>>> serverInit,
                        TriConsumer<ParallelDispatcher, T, RegistryReference<Block>> commonPostInit,
                        Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit,
                        Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> serverPostInit,
                        Consumer<Item> onRegister,
                        BlockSet.ComponentFactory<Item, T> factory) {
        this.automaticLang = automaticLang;
        this.prefix = prefix;
        this.suffix = suffix;
        this.clientInit = clientInit;
        this.serverInit = serverInit;
        this.commonPostInit = commonPostInit;
        this.clientPostInit = clientPostInit;
        this.serverPostInit = serverPostInit;
        this.onRegister = onRegister;
        this.factory = factory;
    }

    public static <T> Builder<T> builder(BlockSet.ComponentFactory<Item, T> factory) {
        return new Builder<>(factory);
    }

    public ResourceLocation createName(String modid, String baseName) {
        String s = "";
        if(!this.prefix.isEmpty()) s += this.prefix + "_";
        s += baseName;
        if(!this.suffix.isEmpty()) s += "_" + this.suffix;
        return new ResourceLocation(modid, s);
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

    public Supplier<BiConsumer<T, RegistryReference<Block>>> getServerInit() {
        return this.serverInit;
    }

    public TriConsumer<ParallelDispatcher, T, RegistryReference<Block>> getCommonPostInit() {
        return this.commonPostInit;
    }

    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> getClientPostInit() {
        return this.clientPostInit;
    }

    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> getServerPostInit() {
        return this.serverPostInit;
    }

    public Consumer<Item> getOnRegister() {
        return this.onRegister;
    }

    public BlockSet.ComponentFactory<Item, T> getFactory() {
        return this.factory;
    }

    public static final class Builder<T> {

        private boolean automaticLang = true;
        private String prefix = "";
        private String suffix = "";
        private Supplier<BiConsumer<T, RegistryReference<Block>>> clientInit;
        private Supplier<BiConsumer<T, RegistryReference<Block>>> serverInit;
        private TriConsumer<ParallelDispatcher, T, RegistryReference<Block>> commonPostInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> serverPostInit;
        private Consumer<Item> onRegister;
        private final BlockSet.ComponentFactory<Item, T> factory;

        private Builder(BlockSet.ComponentFactory<Item, T> factory) {
            this.factory = factory;
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

        public Builder<T> serverInit(Supplier<BiConsumer<T, RegistryReference<Block>>> serverInit) {
            this.serverInit = serverInit;
            return this;
        }

        public Builder<T> commonPostInit(TriConsumer<ParallelDispatcher, T, RegistryReference<Block>> commonPostInit) {
            this.commonPostInit = commonPostInit;
            return this;
        }

        public Builder<T> clientPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> clientPostInit) {
            this.clientPostInit = clientPostInit;
            return this;
        }

        public Builder<T> serverPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Block>>> serverPostInit) {
            this.serverPostInit = serverPostInit;
            return this;
        }

        public Builder<T> onRegister(Consumer<Item> onRegister) {
            this.onRegister = onRegister;
            return this;
        }

        public ItemVariant<T> build() {
            return new ItemVariant<>(
                    this.automaticLang,
                    this.prefix,
                    this.suffix,
                    this.clientInit,
                    this.serverInit,
                    this.commonPostInit,
                    this.clientPostInit,
                    this.serverPostInit,
                    this.onRegister,
                    this.factory);
        }

    }
}
