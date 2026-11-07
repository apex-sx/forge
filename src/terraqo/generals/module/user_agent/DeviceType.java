/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.user_agent;

/**
 * Enum contact for the type of used device
 *
 * @author potatoxf
 */
public enum DeviceType {
    /**
     * Standard desktop or laptop computer
     */
    COMPUTER("Computer"),
    /**
     * Mobile phone or similar small mobile device
     */
    MOBILE("Mobile"),
    /**
     * Small tablet type computer.
     */
    TABLET("Tablet"),
    /**
     * Game console like the Wii or Playstation.
     */
    GAME_CONSOLE("Game console"),
    /**
     * Digital media receiver like the Google TV.
     */
    DMR("Digital media receiver"),
    /**
     * Miniature device like a smart watch or interactive glasses
     */
    WEARABLE("Wearable computer"),
    /**
     * Other or unknow type of device.
     */
    UNKNOWN("Unknown");

    private final String name;

    DeviceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
