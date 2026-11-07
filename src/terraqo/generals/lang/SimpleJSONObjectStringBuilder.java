/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.utils.GeneralString;

import java.util.Date;

/**
 * @author potatoxf
 */
public final class SimpleJSONObjectStringBuilder {
    private final StringBuilder sb = new StringBuilder();

    public SimpleJSONObjectStringBuilder add(String key, Object value) {
        if (value instanceof Number) {
            return this.add(key, (Number) value);
        } else if (value instanceof String) {
            return this.add(key, (String) value);
        } else if (value instanceof Date) {
            return this.add(key, (Date) value);
        } else if (value instanceof Boolean) {
            return this.add(key, value);
        } else {
            return this.addPair(key, "null");
        }
    }

    public SimpleJSONObjectStringBuilder add(String key, boolean value) {
        return this.addPair(key, String.valueOf(value));
    }

    public SimpleJSONObjectStringBuilder add(String key, Number value) {
        return this.addPair(key, value == null ? "null" : value.toString());
    }

    public SimpleJSONObjectStringBuilder add(String key, Date value) {
        return this.addPair(key, value == null ? "null" : '"' + value.toString() + '"');
    }

    public SimpleJSONObjectStringBuilder add(String key, String value) {
        value = value == null ? "" : GeneralString.replace(value, "\"", "\\\"", "\t", "\\t", "\r", "\\r", "\n", "\\n");
        return this.addPair(key, '"' + value + '"');
    }

    public SimpleJSONObjectStringBuilder append(SimpleJSONObjectStringBuilder value) {
        sb.append(value.sb);
        return this;
    }

    public SimpleJSONObjectStringBuilder append(String value) {
        if (value.startsWith("{") && value.endsWith("\n}    ")) {
            sb.append(value, 1, value.length() - 6);
        }
        return this;
    }

    private SimpleJSONObjectStringBuilder addPair(String key, String value) {
        sb.append("\n\t").append('"').append(key).append('"').append(":").append(value).append(",");
        return this;
    }

    @Override
    public String toString() {
        return "{" + sb.substring(0, sb.length() - 1) + "\n}    ";
    }
}
