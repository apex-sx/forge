/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author potatoxf
 */
public interface AdapterHttpSessionListener extends HttpSessionListener {
    /**
     * Receives notification that a session has been created.
     *
     * @param event the HttpSessionEvent containing the session
     */
    @Override
    default void sessionCreated(HttpSessionEvent event) {
        event.getSession().setAttribute("com.github.potatoxf.generals.servlet.AdapterHttpSessionListener", new Mutex());
    }

    /**
     * Receives notification that a session is about to be invalidated.
     *
     * @param event the HttpSessionEvent containing the session
     */
    @Override
    default void sessionDestroyed(HttpSessionEvent event) {
        event.getSession().removeAttribute("com.github.potatoxf.generals.utils.servlet.AdapterHttpSessionListener");
    }

    final class Mutex {
        private Mutex() {
        }
    }
}
