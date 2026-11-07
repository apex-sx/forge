/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.user_agent;

/**
 * Enum constants classifying the different types of applications which are common in referrer strings
 *
 * @author potatoxf
 */
public enum ApplicationType {

    /**
     * Web mail service like Windows Live Hotmail and Gmail.
     */
    WEB_MAIL("Web mail client"),
    UNKNOWN("unknown");

    private final String name;

    ApplicationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
