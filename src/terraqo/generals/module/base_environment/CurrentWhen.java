/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.base_environment;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 当前时间
 *
 * @author potatoxf
 */
public interface CurrentWhen {
    AtomicReference<CurrentWhen> GLOBAL = new AtomicReference<>();

    static CurrentWhen getCurrentWhen() {
        CurrentWhen currentUser = GLOBAL.get();
        if (currentUser == null) {
            throw new RuntimeException("No set global '" + CurrentWhen.class.getSimpleName() + "' instance");
        }
        return currentUser;
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    static Date getsDate() {
        return getCurrentWhen().getDate();
    }

    /**
     * 获取当前日历
     *
     * @return 返回当前日历
     */
    static Calendar getsCalendar() {
        return getCurrentWhen().getCalendar();
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    Date getDate();

    /**
     * 获取当前日历
     *
     * @return 返回当前日历
     */
    default Calendar getCalendar() {
        return Calendar.getInstance();
    }
}
