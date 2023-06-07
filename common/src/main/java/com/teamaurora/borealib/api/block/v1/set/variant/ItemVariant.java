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
 * Represents a standalone item that is related to a {@link BlockSet}. It is registered after all blocks and block-items.
 *
 * @param <T> The block set type
 */
public final class ItemVariant<T> {

    private final boolean automaticLang;
    private final String prefix;
    private final String suffix;
    private final Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit;
    private final Supplier<BiConsumer<T, RegistryReference<Item>>> serverInit;
    private final TriConsumer<ParallelDispatcher, T, RegistryReference<Item>> commonPostInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit;
    private final Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> serverPostInit;
    private final Consumer<Item> onRegister;
    private final BlockSet.ComponentFactory<Item, T> factory;

    private ItemVariant(boolean automaticLang, String prefix, String suffix,
                        Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit,
                        Supplier<BiConsumer<T, RegistryReference<Item>>> serverInit,
                        TriConsumer<ParallelDispatcher, T, RegistryReference<Item>> commonPostInit,
                        Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit,
                        Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> serverPostInit,
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

    public Supplier<BiConsumer<T, RegistryReference<Item>>> getClientInit() {
        return this.clientInit;
    }

    public Supplier<BiConsumer<T, RegistryReference<Item>>> getServerInit() {
        return this.serverInit;
    }

    public TriConsumer<ParallelDispatcher, T, RegistryReference<Item>> getCommonPostInit() {
        return this.commonPostInit;
    }

    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> getClientPostInit() {
        return this.clientPostInit;
    }

    public Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> getServerPostInit() {
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
        private Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit;
        private Supplier<BiConsumer<T, RegistryReference<Item>>> serverInit;
        private TriConsumer<ParallelDispatcher, T, RegistryReference<Item>> commonPostInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit;
        private Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> serverPostInit;
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

        public Builder<T> clientInit(Supplier<BiConsumer<T, RegistryReference<Item>>> clientInit) {
            this.clientInit = clientInit;
            return this;
        }

        public Builder<T> serverInit(Supplier<BiConsumer<T, RegistryReference<Item>>> serverInit) {
            this.serverInit = serverInit;
            return this;
        }

        public Builder<T> commonPostInit(TriConsumer<ParallelDispatcher, T, RegistryReference<Item>> commonPostInit) {
            this.commonPostInit = commonPostInit;
            return this;
        }

        public Builder<T> clientPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> clientPostInit) {
            this.clientPostInit = clientPostInit;
            return this;
        }

        public Builder<T> serverPostInit(Supplier<TriConsumer<ParallelDispatcher, T, RegistryReference<Item>>> serverPostInit) {
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
