/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.symbolic.DataModel;

import java.util.Collection;

/**
 * Sql提供器
 *
 * @author potatoxf
 */
public final class SqlSupplier implements DataModel {
    private static final long serialVersionUID = 1L;
    private final String sql;

    private final Object[] args;

    public SqlSupplier(String sql, Collection<Object> args) {
        this(sql, args.toArray());
    }

    public SqlSupplier(String sql, Object[] args) {
        this.sql = sql;
        this.args = args;
    }

    public String sql() {
        return sql;
    }

    public Object[] args() {
        return args;
    }
}
