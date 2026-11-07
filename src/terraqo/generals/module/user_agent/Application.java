/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.user_agent;

import com.github.potatoxf.generals.utils.GeneralIF;
import com.github.potatoxf.generals.utils.GeneralString;

/**
 * Enum constants for internet applications like web-application and rich
 * internet application.
 *
 * @author potatoxf
 */
public enum Application {
    HOTMAIL(Manufacturer.MICROSOFT, ApplicationType.WEB_MAIL, "Windows Live Hotmail", 1, "mail.live.com", "hotmail.msn"),
    GMAIL(Manufacturer.GOOGLE, ApplicationType.WEB_MAIL, "Gmail", 5, "mail.google.com"),
    YAHOO_MAIL(Manufacturer.YAHOO, ApplicationType.WEB_MAIL, "Yahoo Mail", 10, "mail.yahoo.com"),
    COMPUSERVE(Manufacturer.COMPUSERVE, ApplicationType.WEB_MAIL, "Compuserve", 20, "csmail.compuserve.com"),
    AOL_WEBMAIL(Manufacturer.AOL, ApplicationType.WEB_MAIL, "AOL webmail", 30, "webmail.aol.com"),
    /**
     * MobileMe webmail client by Apple. Previously known as .mac.
     */
    MOBILEME(Manufacturer.APPLE, ApplicationType.WEB_MAIL, "MobileMe", 40, "www.me.com"),
    /**
     * Mail.com
     * Mail.com provides consumers with web-based e-mail services
     */
    MAIL_COM(Manufacturer.MMC, ApplicationType.WEB_MAIL, "Mail.com", 50, ".mail.com"),
    /**
     * Popular open source webmail client. Often installed by providers or privately.
     */
    HORDE(Manufacturer.OTHER, ApplicationType.WEB_MAIL, "horde", 50, "horde"),
    OTHER_WEBMAIL(Manufacturer.OTHER, ApplicationType.WEB_MAIL, "Other webmail client", 60, "webmail", "webemail"),
    UNKNOWN(Manufacturer.OTHER, ApplicationType.UNKNOWN, "Unknown", 0);

    private final short id;
    private final String name;
    private final String[] aliases;
    private final ApplicationType applicationType;
    private final Manufacturer manufacturer;

    Application(Manufacturer manufacturer, ApplicationType applicationType, String name, int versionId, String... aliases) {
        this.id = (short) ((manufacturer.getId() << 8) + (byte) versionId);
        this.name = name;
        this.aliases = GeneralString.toLowerCase(aliases);
        this.applicationType = applicationType;
        this.manufacturer = manufacturer;
    }

    /*
     * Iterates over all Application to compare the signature with the referrer
     * string. If no match can be found Application.UNKNOWN will be returned.
     */
    public static Application parse(String string) {
        // skip the empty and "-" referrer
        if (string != null && string.length() > 1) {
            String referrerStringLowercase = string.toLowerCase();
            for (Application applicationInList : Application.values()) {
                if (applicationInList.isInReferrerString(referrerStringLowercase))
                    return applicationInList;
            }
        }
        return Application.UNKNOWN;
    }

    /**
     * Returns the enum constant of this type with the specified id. Throws
     * IllegalArgumentException if the value does not exist.
     *
     * @param id Id of the application
     * @return Application enum
     */
    public static Application valueOf(short id) {
        for (Application application : Application.values()) {
            if (application.getId() == id)
                return application;
        }
        // same behavior as standard valueOf(string) method
        throw new IllegalArgumentException("No enum const for id " + id);
    }

    /*
     * Checks if the given referrer string matches to the application. Only
     * checks for one specific application.
     */
    public boolean isInReferrerString(String referrerString) {
        return GeneralIF.isContainAny(referrerString, aliases);
    }

    public short getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }
}
