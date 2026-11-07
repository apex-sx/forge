/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.spi;

import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Helps to work with {@link FileSystemProvider}.
 *
 * @author potatoxf
 */
public class FileSystemProviders { // NOPMD Class will be final in 3.0.

    private static final String SCHEME_FILE = "file";
    private static final FileSystemProviders INSTALLED = new FileSystemProviders(FileSystemProvider.installedProviders());

    /**
     * Gets the {@link FileSystemProvider} for the given Path.
     *
     * @param path The Path to query
     * @return the {@link FileSystemProvider} for the given Path.
     */
    @SuppressWarnings("resource") // FileSystem is not allocated here.
    public static FileSystemProvider getFileSystemProvider(final Path path) {
        return Objects.requireNonNull(path, "path").getFileSystem().provider();
    }

    /**
     * Returns the instance for the installed providers.
     *
     * @return the instance for the installed providers.
     * @see FileSystemProvider#installedProviders()
     */
    public static FileSystemProviders installed() {
        return INSTALLED;
    }

    private final List<FileSystemProvider> providers;

    /*
     * Might make public later.
     */
    private FileSystemProviders(final List<FileSystemProvider> providers) {
        this.providers = providers != null ? providers : Collections.emptyList();
    }

    /**
     * Gets the {@link FileSystemProvider} for the given scheme.
     *
     * @param scheme The scheme to query.
     * @return the {@link FileSystemProvider} for the given URI or null.
     */
    @SuppressWarnings("resource") // FileSystems.getDefault() returns a constant.
    public FileSystemProvider getFileSystemProvider(final String scheme) {
        Objects.requireNonNull(scheme, "scheme");
        // Check default provider first to avoid loading of installed providers.
        if (scheme.equalsIgnoreCase(SCHEME_FILE)) {
            return FileSystems.getDefault().provider();
        }
        // Find provider.
        return providers.stream().filter(provider -> provider.getScheme().equalsIgnoreCase(scheme)).findFirst().orElse(null);
    }

    /**
     * Gets the {@link FileSystemProvider} for the given URI.
     *
     * @param uri The URI to query
     * @return the {@link FileSystemProvider} for the given URI or null.
     */
    public FileSystemProvider getFileSystemProvider(final URI uri) {
        return getFileSystemProvider(Objects.requireNonNull(uri, "uri").getScheme());
    }

    /**
     * Gets the {@link FileSystemProvider} for the given URL.
     *
     * @param url The URL to query
     * @return the {@link FileSystemProvider} for the given URI or null.
     */
    public FileSystemProvider getFileSystemProvider(final URL url) {
        return getFileSystemProvider(Objects.requireNonNull(url, "url").getProtocol());
    }

}
