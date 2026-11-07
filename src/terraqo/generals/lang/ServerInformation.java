/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.utils.GeneralRemote;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * 服务器相关信息
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public class ServerInformation {
    /**
     * 操作系统
     */
    public final String osName;
    /**
     * 系统架构
     */
    public final String osArch;
    /**
     * 项目路径
     */
    public final String userDir;
    /**
     * 服务器名称
     */
    public final String serverName;
    /**
     * 服务器Ip
     */
    public final String serverIp;
    /**
     * jdk名称
     */
    public final String jvmName;
    /**
     * JDK路径
     */
    public final String jvmHome;
    /**
     * JDK版本
     */
    public final String jvmVersion;
    /**
     * JVM启动时间
     */
    public final long jvmStartTime;
    /**
     * 当前JVM占用的内存总数(M)
     */
    public final double jvmTotal;
    /**
     * JVM最大可用内存总数(M)
     */
    public final double jvmMax;
    /**
     * JVM空闲内存(M)
     */
    public final double jvmFree;
    /**
     * CPU相关信息
     */
    public final CPU cpu = new CPU();
    /**
     * 內存相关信息
     */
    public final MEM mem = new MEM();
    /**
     * 磁盘相关信息
     */
    public final List<DISK> disks = new LinkedList<>();

    public ServerInformation() {
        Properties props = System.getProperties();
        this.serverName = GeneralRemote.getLocalHostName();
        this.serverIp = GeneralRemote.getLocalHostIp();
        this.osName = props.getProperty("os.name");
        this.osArch = props.getProperty("os.arch");
        this.userDir = props.getProperty("user.dir");
        this.jvmName = ManagementFactory.getRuntimeMXBean().getVmName();
        this.jvmHome = props.getProperty("java.home");
        this.jvmVersion = props.getProperty("java.version");
        this.jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        this.jvmTotal = Runtime.getRuntime().totalMemory();
        this.jvmMax = Runtime.getRuntime().maxMemory();
        this.jvmFree = Runtime.getRuntime().freeMemory();
    }

    public ServerInformation addDisk(Consumer<DISK> diskSetting) {
        DISK disk = new DISK();
        diskSetting.accept(disk);
        disks.add(disk);
        return this;
    }

    /**
     * CPU相关信息
     */
    public static final class CPU {
        /**
         * 核心数
         */
        public int cores;
        /**
         * CPU总的使用率
         */
        public double total;
        /**
         * CPU系统使用率
         */
        public double system;
        /**
         * CPU用户使用率
         */
        public double user;
        /**
         * CPU当前等待率
         */
        public double wait;
        /**
         * CPU当前空闲率
         */
        public double free;

        private CPU() {
        }
    }

    /**
     * 內存相关信息
     *
     * @author potatoxf
     */
    public static final class MEM {
        /**
         * 内存总量
         */
        public double total;
        /**
         * 已用内存
         */
        public double used;
        /**
         * 剩余内存
         */
        public double free;

        private MEM() {
        }
    }

    /**
     * 系统文件相关信息
     *
     * @author potatoxf
     */
    public static final class DISK {
        /**
         * 盘符路径
         */
        public String driver;
        /**
         * 盘符类型
         */
        public String type;
        /**
         * 文件类型
         */
        public String name;
        /**
         * 总大小
         */
        public double total;
        /**
         * 剩余大小
         */
        public double free;
        /**
         * 已经使用量
         */
        public double used;
        /**
         * 资源的使用率
         */
        public double usage;

        private DISK() {
        }
    }
}
