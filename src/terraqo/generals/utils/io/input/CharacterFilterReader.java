/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.io.Reader;
import java.util.function.IntPredicate;

/**
 * A filter reader that filters out a given character represented as an {@code int} code point, handy to remove
 * known junk characters from CSV files for example. This class is the most efficient way to filter out a single
 * character, as opposed to using a {@link CharacterSetFilterReader}. You can also nest {@link CharacterFilterReader}s.
 */
public class CharacterFilterReader extends AbstractCharacterFilterReader {

    /**
     * Constructs a new reader.
     *
     * @param reader
     *            the reader to filter.
     * @param skip
     *            the character to filter out.
     */
    public CharacterFilterReader(final Reader reader, final int skip) {
        super(reader, c -> c == skip);
    }

    /**
     * Constructs a new reader.
     *
     * @param reader the reader to filter.
     * @param skip Skip test.
     * @author potatoxf
     */
    public CharacterFilterReader(final Reader reader, final IntPredicate skip) {
        super(reader, skip);
    }

}
