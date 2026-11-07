/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

import com.github.potatoxf.generals.Log;

/**
 * @author potatoxf
 */
public class FunctionException extends RuntimeException {

    public FunctionException(Throwable cause) {
        super("Function Exception Caused By Inner Exception", cause);
        Log.error("Function Exception", cause);
    }
}
