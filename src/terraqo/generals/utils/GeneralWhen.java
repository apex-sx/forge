/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;


import com.github.potatoxf.generals.Log;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * @author potatoxf
 */
public interface GeneralWhen {

    /**
     * Returns the number of nanoseconds of the given duration without throwing or overflowing.
     * <p>
     * Instead of throwing {@link ArithmeticException}, this method silently saturates to either
     * {@link Long#MAX_VALUE} or {@link Long#MIN_VALUE}. This behavior can be useful when decomposing
     * a duration in order to call a legacy API which requires a {@code long, TimeUnit} pair.
     */
    static long getNanos(boolean isThrow, Duration duration) {
        // Using a try/catch seems lazy, but the catch block will rarely get invoked (except for
        // durations longer than approximately +/- 292 years).
        try {
            return duration.toNanos();
        } catch (Throwable e) {
            Log.warnOrThrowError(isThrow, e, "Error to nanos");
            return duration.isNegative() ? Long.MIN_VALUE : Long.MAX_VALUE;
        }
    }

    static Charset parseCharset(Object input) {
        if (input == null) return Charset.defaultCharset();
        else if (input instanceof Charset) return (Charset) input;
        else if (input instanceof CharSequence) {
            try {
                return Charset.forName(input.toString());
            } catch (Throwable e) {
                throw new IllegalArgumentException("Error to parse charset", e);
            }
        }

        throw new IllegalArgumentException("The input parameters do not meet the requirements");
    }

    static Locale parseLocale(Object input) {
        if (input == null) return Locale.getDefault();
        else if (input instanceof Locale) return (Locale) input;
        else if (input instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) input;

            /*
             * 将给定的{@code String}表示形式解析为{@link Locale}，格式如下：
             * <ul>
             *   <li>en
             *   <li>en_US
             *   <li>zh CN
             * </ul>
             */
            String localeName = GeneralAssertion.checkMatch(charSequence, v -> v == '_' || v == ' ' || Character.isLetterOrDigit(v)).toString();
            if (!GeneralIF.isNoEmpty(localeName)) return Locale.getDefault();
            try {
                StringTokenizer st = new StringTokenizer(localeName, "_ ");
                String first = st.nextToken();
                String second = st.hasMoreTokens() ? st.nextToken() : null;
                if (second == null) {
                    Locale resolved = Locale.forLanguageTag(first);
                    if (resolved != null) {
                        String language = resolved.getLanguage();
                        if (language != null && !language.isEmpty()) return resolved;
                    }
                }
                String country = second == null ? "" : second;
                String variant = "";
                if (st.hasMoreTokens()) {
                    // There is definitely a variant, and it is everything after the country
                    // code sans the separator between the country code and the variant.
                    int endIndexOfCountryCode = localeName.indexOf(country) + country.length();
                    // Strip off any leading '_' and whitespace, what's left is the variant.
                    variant = localeName.substring(endIndexOfCountryCode).trim();
                    int index = 0;
                    for (int i = 0; i < variant.length(); i++) {
                        if (variant.charAt(i) == '_') index++;
                        else break;
                    }
                    variant = variant.substring(index);
                }
                if (variant.isEmpty() && country.startsWith("#")) {
                    variant = country;
                    country = "";
                }
                return new Locale(first, country, variant);
            } catch (Throwable e) {
                throw new IllegalArgumentException("Error to parse locale", e);
            }
        }
        throw new IllegalArgumentException("The input parameters do not meet the requirements");
    }

    static ZoneId parseZoneId(Object input) {
        try {
            if (input == null) return ZoneId.systemDefault();
            else if (input instanceof ZoneId) return (ZoneId) input;
            else if (input instanceof CharSequence) return ZoneId.of(input.toString());
            else if (input instanceof TemporalAccessor) return ZoneId.from((TemporalAccessor) input);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Error to parse zone", e);
        }
        throw new IllegalArgumentException("The input parameters do not meet the requirements");
    }

    /**
     * Returns a parsed duration value.
     */
    static Duration parseDuration(String value, Duration defaultValue) {
        return GeneralWhen.parseDuration(value, defaultValue, "Input Duration");
    }

    /**
     * Returns a parsed duration value.
     */
    static Duration parseDuration(String value, Duration defaultValue, String main) {
        if (value == null) {
            if (defaultValue == null) {
                throw new IllegalArgumentException("The value of '" + main + "' must be not null");
            } else {
                return defaultValue;
            }
        }
        int length = value.length();
        if (length == 0) {
            if (defaultValue == null) {
                throw new IllegalArgumentException("The value of '" + main + "' must be not empty");
            } else {
                return defaultValue;
            }
        }
        boolean isIsoFormat = value.contains("p") || value.contains("P");
        if (isIsoFormat) {
            Duration duration = Duration.parse(value);
            if (duration.isNegative()) {
                throw new IllegalArgumentException(String.format(
                        "main %s invalid format; was %s, but the duration cannot be negative", main, value));
            }
            return duration;
        }
        TimeUnit timeUnit = GeneralWhen.parseTimeUnit(value, null, main);

        int i = value.length() - 2;
        if (GeneralIF.isNumberChar(value.charAt(i), false)) {
            i++;
        }
        long duration;
        try {
            duration = Long.parseLong(value.substring(0, i));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("The '%s' of '%s' invalid number format.", value, main), e);
        }
        long nanos = timeUnit.toNanos(duration);
        return Duration.ofNanos(nanos);
    }

    /**
     * Returns a parsed {@link TimeUnit} value.
     */
    static TimeUnit parseTimeUnit(String value, TimeUnit defaultValue) {
        return GeneralWhen.parseTimeUnit(value, defaultValue, "Input TimeUnit");
    }

    /**
     * Returns a parsed {@link TimeUnit} value.
     */
    static TimeUnit parseTimeUnit(String value, TimeUnit defaultValue, String main) {
        if (value == null) {
            if (defaultValue == null) {
                throw new IllegalArgumentException("The value of '" + main + "' must be not null");
            } else {
                return defaultValue;
            }
        }
        int length = value.length();
        if (length == 0) {
            if (defaultValue == null) {
                throw new IllegalArgumentException("The value of '" + main + "' must be not empty");
            } else {
                return defaultValue;
            }
        }
        char c = Character.toLowerCase(value.charAt(length - 1));
        switch (c) {
            case 'd':
                return TimeUnit.DAYS;
            case 'h':
                return TimeUnit.HOURS;
            case 'm':
                return TimeUnit.MINUTES;
            case 's': {
                if (length > 2) {
                    c = Character.toLowerCase(value.charAt(length - 2));
                    if (c == 'm') return TimeUnit.MILLISECONDS;
                }
                return TimeUnit.SECONDS;
            }
        }
        throw new IllegalArgumentException(String.format(
                "The '%s' invalid format; was %s, must end with one of [dDhHmMsS]", main, value));
    }
}
