/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.io.Serial;

/**
 * @author Terraqo
 */
public class ObjectTuples extends ObjectTuple<Object> {
    @Serial
    private static final long serialVersionUID = 1L;

    public ObjectTuples(Object... values) {
        super(values);
    }
}
