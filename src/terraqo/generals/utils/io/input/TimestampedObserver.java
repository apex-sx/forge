/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.input;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import com.github.potatoxf.generals.utils.io.input.ObservableInputStream.Observer;

/**
 * An observer with timestamps.
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * final TimestampedObserver timestampedObserver = new TimestampedObserver();
 * try (ObservableInputStream inputStream = new ObservableInputStream(...),
 *     timestampedObserver)) {
 *     ...
 * }
 * System.out.printf("IO duration: %s%n", timestampedObserver.getOpenToCloseDuration());
 * </pre>
 *
 * @author potatoxf
 */
public class TimestampedObserver extends Observer {

    private volatile Instant closeInstant;
    private final Instant openInstant = Instant.now();

    /**
     * Constructs a new instance.
     */
    public TimestampedObserver() {
        // empty
    }

    @Override
    public void closed() throws IOException {
        closeInstant = Instant.now();
    }

    /**
     * Gets the instant for when this instance was closed.
     *
     * @return the instant for when closed was called.
     */
    public Instant getCloseInstant() {
        return closeInstant;
    }

    /**
     * Gets the instant for when this instance was created.
     *
     * @return the instant for when this instance was created.
     */
    public Instant getOpenInstant() {
        return openInstant;
    }

    /**
     * Gets the Duration between creation and close.
     *
     * @return the Duration between creation and close.
     */
    public Duration getOpenToCloseDuration() {
        return Duration.between(openInstant, closeInstant);
    }

    /**
     * Gets the Duration between creation and now.
     *
     * @return the Duration between creation and now.
     */
    public Duration getOpenToNowDuration() {
        return Duration.between(openInstant, Instant.now());
    }

    /**
     * Tests whether {@link #closed()} has been called.
     *
     * @return whether {@link #closed()} has been called.
     * @author potatoxf
     */
    public boolean isClosed() {
        return closeInstant != null;
    }

    @Override
    public String toString() {
        return "TimestampedObserver [openInstant=" + openInstant + ", closeInstant=" + closeInstant + "]";
    }

}
