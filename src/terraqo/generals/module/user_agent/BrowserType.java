/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.user_agent;

/**
 * Enum constants classifying the different types of browsers which are common in user-agent strings
 *
 * @author potatoxf
 */
public enum BrowserType {

    /**
     * Standard web-browser
     */
    WEB_BROWSER("Browser"),
    /**
     * Special web-browser for mobile devices
     */
    MOBILE_BROWSER("Browser (mobile)"),
    /**
     * Text only browser like the good old Lynx
     */
    TEXT_BROWSER("Browser (text only)"),
    /**
     * Email client like Thunderbird
     */
    EMAIL_CLIENT("Email Client"),
    /**
     * Search robot, spider, crawler,...
     */
    ROBOT("Robot"),
    /**
     * Downloading tools
     */
    TOOL("Downloading tool"),
    /**
     * Application
     */
    APP("Application"),
    UNKNOWN("unknown");

    private final String name;

    BrowserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
