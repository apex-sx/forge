/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

/**
 * @author potatoxf
 */
public interface GeneralUnit {

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    static String toHumanStorageUnit(double size) {
        double v = 0;
        double kb = 1024;
        if (size < kb) {
            return String.format("%f B", size);
        } else {
            double mb = kb * 1024;
            if (size < mb) {
                v = size / kb;
                return String.format(v > 100 ? "%.0f KB" : "%.1f KB", v);
            } else {
                double gb = mb * 1024;
                if (size < gb) {
                    v = size / mb;
                    return String.format(v > 100 ? "%.0f MB" : "%.1f MB", v);
                } else {
                    double tb = gb * 1024;
                    if (size < tb) {
                        v = size / gb;
                        return String.format(v > 100 ? "%.0f GB" : "%.1f GB", v);
                    } else {
                        double pb = tb * 1024;
                        if (size < pb) {
                            v = size / tb;
                            return String.format(v > 100 ? "%.0f TB" : "%.1f TB", v);
                        } else {
                            v = size / pb;
                            return String.format(v > 100 ? "%.0f PB" : "%.1f PB", v);
                        }
                    }
                }

            }
        }
    }
}
