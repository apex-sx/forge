/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用版本解析与比较工具类，支持数字和非数字版本段。
 * <p>
 * 版本格式：{@code major.minor.patch[-preRelease]}，每段可以是数字或非数字字符串。
 * <p>
 * 比较规则：
 * <ul>
 *   <li>数字段按数值比较（如 2 &lt; 10）</li>
 *   <li>非数字段按字典序比较</li>
 *   <li>数字段 &lt; 非数字段</li>
 *   <li>段数不同时，缺少段视为小于多余段（如 1.0 &lt; 1.0.1）</li>
 *   <li>段全部相同时，无预发布后缀 &gt; 有预发布后缀（如 1.0.0 &gt; 1.0.0-alpha）</li>
 * </ul>
 *
 * @author potatoxf
 */
public final class Version implements Comparable<Version> {

    /**
     * 原始版本字符串
     */
    private final String version;

    /**
     * 解析后的版本段数组（不包含预发布后缀）
     */
    private final Segment[] segments;

    /**
     * 预发布后缀字符串（如 "alpha", "beta-1"），如果没有则为 null
     */
    private final String preRelease;

    /**
     * 私有构造方法，通过静态工厂方法 {@link #of(String)} 创建实例
     *
     * @param version    原始版本字符串
     * @param segments   解析后的版本段数组
     * @param preRelease 预发布后缀，可为 null
     */
    private Version(String version, Segment[] segments, String preRelease) {
        this.version = version;
        this.segments = segments;
        this.preRelease = preRelease;
    }

    /**
     * 根据给定的版本字符串解析并创建 {@link Version} 实例
     *
     * @param version 版本字符串，不能为 null 或空白
     * @return 解析后的 {@link Version} 实例
     * @throws IllegalArgumentException 如果版本字符串为 null 或空白
     */
    public static Version of(String version) {
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version string must not be null or blank");
        }

        version = version.trim();

        // 分离主版本部分和预发布后缀
        String mainPart = version, preRelease = null;
        int dashIndex = version.indexOf('-');
        if (dashIndex >= 0) {
            mainPart = version.substring(0, dashIndex);
            preRelease = version.substring(dashIndex + 1);
        }

        // 按 "." 分割主版本部分并转换为 Segment 数组
        String[] tokens = mainPart.split("\\.");
        Segment[] segments = new Segment[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            segments[i] = new Segment(tokens[i]);
        }
        return new Version(version, segments, preRelease);
    }

    /**
     * 创建一个带有默认并发缓存的 {@link Fetcher}，用于从字符串中提取并缓存版本对象
     *
     * @param version 初始版本字符串（用于非空校验，实际提取由 Fetcher 的 fetch 方法处理）
     * @return 带有缓存功能的 {@link Fetcher} 实例
     * @throws IllegalArgumentException 如果版本字符串为 null 或空白
     */
    public static Fetcher ofCache(String version) {
        return Version.ofCache(version, new ConcurrentHashMap<>());
    }

    /**
     * 创建一个带有自定义缓存 Map 的 {@link Fetcher}，用于从字符串中提取并缓存版本对象
     *
     * @param version 初始版本字符串（用于非空校验）
     * @param cache   用于缓存版本对象的 Map，建议使用线程安全的 Map（如 ConcurrentHashMap）
     * @return 带有缓存功能的 {@link Fetcher} 实例
     * @throws IllegalArgumentException 如果版本字符串为 null 或空白
     */
    public static Fetcher ofCache(String version, Map<String, Version> cache) {
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version string must not be null or blank");
        }

        return input -> cache.computeIfAbsent(input, Version::of);
    }

    /**
     * 获取原始版本字符串
     *
     * @return 原始版本字符串
     */
    public String version() {
        return version;
    }

    /**
     * 获取主版本号（major version）
     *
     * @return 主版本号字符串，如果不存在则返回 "0"
     */
    public String majorVersion() {
        return segments.length == 0 ? "0" : segments[0].raw();
    }

    /**
     * 获取次版本号（minor version）
     *
     * @return 次版本号字符串，如果不存在则返回 "0"
     */
    public String minorVersion() {
        return segments.length < 2 ? "0" : segments[1].raw();
    }

    /**
     * 获取修订版本号（patch version）
     *
     * @return 修订版本号字符串，如果不存在则返回 "0"
     */
    public String patchVersion() {
        return segments.length < 3 ? "0" : segments[2].raw();
    }

    /**
     * 获取预发布后缀
     *
     * @return 预发布后缀字符串，如果没有则返回 null
     */
    public String preRelease() {
        return preRelease;
    }

    /**
     * 判断当前版本是否为预发布版本
     *
     * @return 如果包含预发布后缀返回 true，否则返回 false
     */
    public boolean isPreRelease() {
        return preRelease != null;
    }

    /**
     * 获取所有的版本段列表
     *
     * @return 包含所有 {@link Segment} 的不可变列表
     */
    public List<Segment> segments() {
        return List.of(segments);
    }

    /**
     * 判断当前版本是否小于指定的最小版本
     *
     * @param version 要比较的版本字符串
     * @return 如果当前版本小于指定版本返回 true，否则返回 false
     */
    public boolean isMinimumVersion(String version) {
        return this.compareTo(Version.of(version)) < 0;
    }

    /**
     * 判断当前版本是否小于指定的最小版本
     *
     * @param version 要比较的 {@link Version} 对象
     * @return 如果当前版本小于指定版本返回 true，否则返回 false
     */
    public boolean isMinimumVersion(Version version) {
        return this.compareTo(version) < 0;
    }

    /**
     * 判断当前版本是否大于指定的最大版本
     *
     * @param version 要比较的版本字符串
     * @return 如果当前版本大于指定版本返回 true，否则返回 false
     */
    public boolean isMaximumVersion(String version) {
        return this.compareTo(Version.of(version)) > 0;
    }

    /**
     * 判断当前版本是否大于指定的最大版本
     *
     * @param version 要比较的 {@link Version} 对象
     * @return 如果当前版本大于指定版本返回 true，否则返回 false
     */
    public boolean isMaximumVersion(Version version) {
        return this.compareTo(version) > 0;
    }

    /**
     * 判断当前版本是否在指定的版本范围内（包含边界）
     *
     * @param minimumVersion 最小版本字符串
     * @param maximumVersion 最大版本字符串
     * @return 如果当前版本在 [minimumVersion, maximumVersion] 范围内返回 true，否则返回 false
     */
    public boolean isRange(String minimumVersion, String maximumVersion) {
        return isRange(Version.of(minimumVersion), Version.of(maximumVersion));
    }

    /**
     * 判断当前版本是否在指定的版本范围内（包含边界）
     *
     * @param minimumVersion 最小 {@link Version} 对象
     * @param maximumVersion 最大 {@link Version} 对象
     * @return 如果当前版本在 [minimumVersion, maximumVersion] 范围内返回 true，否则返回 false
     */
    public boolean isRange(Version minimumVersion, Version maximumVersion) {
        return this.compareTo(minimumVersion) >= 0 && this.compareTo(maximumVersion) <= 0;
    }

    /**
     * 将此版本与另一个版本进行比较。
     * <p>
     * 比较规则：
     * 1. 依次比较每个版本段（Segment）。
     * 2. 如果段数不同，较短的版本在缺失段时视为较小。
     * 3. 如果所有段都相同，则比较预发布后缀：无后缀 > 有后缀。
     * 4. 如果都有后缀，则按字典序比较后缀。
     *
     * @param other 要比较的另一个 {@link Version} 对象
     * @return 负整数、零或正整数，表示当前版本小于、等于或大于指定版本
     */
    @Override
    public int compareTo(Version other) {
        if (other == null) return 1;
        if (this == other) return 0;

        // 比较主版本段
        int size = Math.max(this.segments.length, other.segments.length);
        for (int i = 0; i < size; i++) {
            if (i >= this.segments.length) return -1;
            if (i >= other.segments.length) return 1;
            int cmp = this.segments[i].compareTo(other.segments[i]);
            if (cmp != 0) return cmp;
        }

        // 比较预发布后缀
        if (this.preRelease == null && other.preRelease == null) return 0;
        if (this.preRelease == null) return 1; // 无后缀大于有后缀
        if (other.preRelease == null) return -1;
        return this.preRelease.compareTo(other.preRelease);
    }

    /**
     * 计算版本对象的哈希码
     *
     * @return 哈希码值
     */
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(segments);
        result = 31 * result + (preRelease != null ? preRelease.hashCode() : 0);
        return result;
    }

    /**
     * 判断当前版本是否与另一个对象相等
     *
     * @param obj 要比较的对象
     * @return 如果相等返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Version other)) return false;
        return compareTo(other) == 0;
    }

    /**
     * 返回原始版本字符串的字符串表示形式
     *
     * @return 原始版本字符串
     */
    @Override
    public String toString() {
        return version;
    }

    /**
     * 版本获取器接口，用于从输入字符串中提取并返回 {@link Version} 对象。
     * 通常结合缓存使用，避免重复解析相同的版本字符串。
     *
     * @author potatoxf
     */
    public interface Fetcher {
        /**
         * 从输入字符串中提取版本对象
         *
         * @param input 输入字符串
         * @return 解析后的 {@link Version} 对象
         */
        Version fetch(String input);
    }

    /**
     * 版本段，表示版本号中由 "." 分隔的每一部分。
     * 自动识别并区分数字类型和非数字类型，以便进行正确的比较。
     */
    public static final class Segment implements Comparable<Segment> {

        /**
         * 原始字符串表示
         */
        private final String raw;

        /**
         * 解析后的数值。如果是有效数字则为该数字的绝对值，否则为 -1
         */
        private final int numeric;

        /**
         * 私有构造方法，解析原始字符串
         *
         * @param raw 版本段的原始字符串，不能为 null 或空白
         * @throws IllegalArgumentException 如果字符串为 null 或空白
         */
        private Segment(String raw) {
            if (raw == null || raw.isBlank()) {
                throw new IllegalArgumentException("Segment string must not be null or blank");
            }
            this.raw = raw;
            int numeric = -1;
            try {
                // 尝试解析为整数，如果成功则记录其绝对值
                numeric = Math.abs(Integer.parseInt(raw));
            } catch (NumberFormatException ignored) {
                // 解析失败，保持 numeric 为 -1，表示非数字段
            }
            this.numeric = numeric;
        }

        /**
         * 获取原始字符串
         *
         * @return 原始字符串
         */
        public String raw() {
            return this.raw;
        }

        /**
         * 获取解析后的数值
         *
         * @return 如果是数字段则返回其绝对值，否则返回 -1
         */
        public int numeric() {
            return this.numeric;
        }

        /**
         * 将此版本段与另一个版本段进行比较。
         * <p>
         * 比较规则：
         * 1. 如果都是数字段，按数值大小比较。
         * 2. 如果都是非数字段，按字典序比较。
         * 3. 如果一个是数字段，另一个是非数字段，数字段小于非数字段。
         *
         * @param other 要比较的另一个 {@link Segment} 对象
         * @return 负整数、零或正整数，表示当前段小于、等于或大于指定段
         */
        @Override
        public int compareTo(Segment other) {
            // 两者都是数字段
            if (this.numeric >= 0 && other.numeric >= 0) {
                return Integer.compare(this.numeric, other.numeric);
            }
            // 两者都是非数字段
            if (this.numeric < 0 && other.numeric < 0) {
                return this.raw.compareTo(other.raw);
            }
            // 一个是数字，一个是非数字，数字小于非数字
            return this.numeric < other.numeric ? -1 : 1;
        }

        /**
         * 计算版本段的哈希码
         *
         * @return 哈希码值
         */
        @Override
        public int hashCode() {
            return this.raw.hashCode();
        }

        /**
         * 判断当前版本段是否与另一个对象相等
         *
         * @param object 要比较的对象
         * @return 如果相等返回 true，否则返回 false
         */
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Segment other)) return false;
            return compareTo(other) == 0;
        }

        /**
         * 返回版本段的原始字符串表示形式
         *
         * @return 原始字符串
         */
        @Override
        public String toString() {
            return this.raw;
        }
    }
}
