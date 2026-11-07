/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

/**
 * A filter reader that removes a given set of characters represented as {@code int} code points, handy to remove known
 * junk characters from CSV files for example.
 * <p>
 * This class must convert each {@code int} read to an {@link Integer}. You can increase the Integer cache with a system
 * property, see {@link Integer}.
 * </p>
 */
public class CharacterSetFilterReader extends AbstractCharacterFilterReader {

    private static IntPredicate toIntPredicate(final Set<Integer> skip) {
        if (skip == null) {
            return SKIP_NONE;
        }
        final Set<Integer> unmodifiableSet = Collections.unmodifiableSet(skip);
        return c -> unmodifiableSet.contains(Integer.valueOf(c));
    }

    /**
     * Constructs a new reader.
     *
     * @param reader the reader to filter.
     * @param skip the set of characters to filter out.
     * @author potatoxf
     */
    public CharacterSetFilterReader(final Reader reader, final Integer... skip) {
        this(reader, new HashSet<>(Arrays.asList(skip)));
    }

    /**
     * Constructs a new reader.
     *
     * @param reader the reader to filter.
     * @param skip the set of characters to filter out.
     */
    public CharacterSetFilterReader(final Reader reader, final Set<Integer> skip) {
        super(reader, toIntPredicate(skip));
    }

}
