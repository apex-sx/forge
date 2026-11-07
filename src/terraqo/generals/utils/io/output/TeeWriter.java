/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.output;

import java.io.Writer;
import java.util.Collection;

/**
 * Classic splitter of {@link Writer}. Named after the UNIX 'tee' command. It allows a stream to be branched off so
 * there are now two streams.
 * <p>
 * This currently a only convenience class with the proper name "TeeWriter".
 * </p>
 *
 * @author potatoxf
 */
public class TeeWriter extends ProxyCollectionWriter {

    /**
     * Constructs a new filtered collection writer.
     *
     * @param writers Writers to provide the underlying targets.
     */
    public TeeWriter(final Collection<Writer> writers) {
        super(writers);
    }

    /**
     * Constructs a new filtered collection writer.
     *
     * @param writers Writers to provide the underlying targets.
     */
    public TeeWriter(final Writer... writers) {
        super(writers);
    }
}
