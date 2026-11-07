/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */
package apexsx.forge.core;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Supplier;

/**
 * 国际化消息记录类，整合了 LocalizedContent 和 InternationalMessage 的功能，
 * 用于支持多语言功能并提供统一的国际化内容接口。
 *
 * <p>主要功能包括：
 * <ul>
 *     <li>支持基于资源包基础名称和消息键获取国际化消息</li>
 *     <li>提供默认内容供应器作为备选方案</li>
 *     <li>支持消息格式化参数</li>
 *     <li>支持从异常、表达式、枚举等对象中提取国际化消息</li>
 *     <li>使用 International 管理 baseName</li>
 * </ul>
 *
 * <p>执行逻辑：
 * <ol>
 *     <li>调用getLocalizedMessage(locale)：使用international管理器加载对应locale的ResourceBundle</li>
 *     <li>尝试读取bundle中key对应的字符串；资源缺失捕获MissingResourceException打警告日志</li>
 *     <li>资源读取失败则回退执行fallback供应器获取默认文本</li>
 *     <li>拿到原始文本后，使用MessageFormat配合args数组做占位符格式化</li>
 *     <li>结果为null返回空字符串""</li>
 * </ol>
 *
 * @author Terraqo
 */
public record InternationalMessage(International international, String key, Supplier<String> fallback, Object... args) {
    /**
     * 空的国际化消息实例，用于表示没有实际内容的国际化消息对象。
     * 该实例的bundleManager、key均为null，默认内容为空字符串。
     */
    public static final InternationalMessage EMPTY_LOCALIZED_MESSAGE = new InternationalMessage(null, null, null);

    /**
     * record规范的紧凑构造器；这里没有额外逻辑，仅保留。
     *
     * @param international 国际化管理器实例，可以为null（只使用fallback）
     * @param key           资源properties中的消息key，可以为null
     * @param fallback      默认文本供应器，资源找不到时使用；可以为null
     * @param args          MessageFormat格式化参数数组
     */
    public InternationalMessage {
    }

    /**
     * 创建 InternationalMessage 实例的静态工厂方法。
     * <p>baseName空白会自动替换默认值"META‑INF/i18n/messages"。
     * 分支逻辑：
     * <ul>
     *     <li>fallback为空，key为空 → 返回EMPTY_LOCALIZED_MESSAGE</li>
     *     <li>fallback为空，key不为空 → 只有资源key，无降级文本</li>
     *     <li>fallback不为空，key为空 → 只使用fallback文本，不读取资源包</li>
     *     <li>fallback不为空，key不为空 → 优先读取资源key，失败回退fallback</li>
     * </ul>
     *
     * @param baseName 资源包基础名称
     * @param key      资源文件中的键
     * @param fallback 当找不到国际化内容时的默认内容
     * @param args     格式化参数
     * @return InternationalMessage 实例
     */
    public static InternationalMessage of(String baseName, String key, String fallback, Object... args) {
        if (baseName == null || baseName.isBlank()) {
            baseName = "META-INF/i18n/messages";
        }
        International international = International.of(baseName);
        if (fallback == null || fallback.isBlank()) {
            if (key == null || key.isBlank()) {
                return EMPTY_LOCALIZED_MESSAGE;
            } else {
                return new InternationalMessage(international, key, null, args);
            }
        } else {
            if (key == null || key.isBlank()) {
                return new InternationalMessage(null, null, () -> fallback, args);
            } else {
                return new InternationalMessage(international, key, () -> fallback, args);
            }
        }
    }

    /**
     * 从 TagKey 对象创建 InternationalMessage 实例。
     * 使用tagKey的moduleResourcePath作为baseName，type.name()作为资源key。
     *
     * @param tagKey   TagKey对象，从中获取模块资源路径和类型名称
     * @param fallback 当找不到国际化内容时的默认内容
     * @param args     格式化参数
     * @return InternationalMessage 实例
     */
    public static InternationalMessage of(TagKey tagKey, String fallback, Object... args) {
        // 使用 Category 对象的模块资源路径和类型名称构建 InternationalMessage
        return InternationalMessage.of(tagKey.getModuleResourcePath(), tagKey.type().name(), fallback, args);
    }

    /**
     * 从枚举对象中构造国际化消息。
     * <p>baseName使用枚举类简单类名；资源key使用枚举常量name()。
     *
     * @param enumeration 枚举对象，用于提取枚举类名和枚举常量名称作为国际化键
     * @param fallback    默认的消息内容，当无法找到对应的国际化文本时使用此默认值
     * @param args        消息格式化参数，用于替换国际化消息中的占位符
     * @return 返回一个包含国际化消息的 {@link InternationalMessage} 对象
     */
    public static InternationalMessage ofEnumeration(Enum<?> enumeration, String fallback, Object... args) {
        // 使用枚举类的简单名称和枚举常量名称构建国际化键，并创建LocalizedMessage对象
        return InternationalMessage.of(enumeration.getClass().getSimpleName(), enumeration.name(), fallback, args);
    }

    /**
     * 获取默认消息内容（不进行本地化，直接使用fallback文本做格式化）。
     * <p>注意：不会读取properties资源文件，只执行fallback供应器+MessageFormat格式化。
     *
     * @return 默认消息内容；fallback返回null会得到null，上层调用注意判空
     */
    public String getDefaultMessage() {
        // 从 fallbackSupplier 获取默认消息
        String result = fallback.get();
        // 如果存在格式化参数，则应用这些参数
        if (result != null && args.length > 0) {
            result = MessageFormat.format(result, args);
        }
        return result;
    }

    /**
     * 获取默认本地化的消息内容，使用JVM全局默认Locale。
     *
     * @return 根据默认本地设置获取的消息内容
     */
    public String getLocalizedMessage() {
        // 使用系统默认的 Locale 设置获取本地化消息
        return getLocalizedMessage(Locale.getDefault());
    }

    /**
     * 根据指定的本地设置获取本地化消息。核心业务方法。
     * <p>流程：
     * 1. international不为null，则调用findResourceBundle加载对应locale资源包
     * 2. bundle和key有效，尝试getString(key)；捕获MissingResourceException打印警告
     * 3. 资源读取结果为null，执行fallback供应器拿默认文本
     * 4. 拿到文本后执行MessageFormat格式化；最终null返回空字符串
     *
     * @param locale 本地化配置，如果为null则使用默认本地
     * @return 根据本地设置获取的消息内容，如果找不到则返回默认内容，不会返回null
     */
    public String getLocalizedMessage(Locale locale) {
        // 获取与指定区域设置对应的语言包
        ResourceBundle resourceBundle = null;
        if (international != null) {
            resourceBundle = international.findResourceBundle(locale);
        }
        String result = null;
        if (resourceBundle != null) {
            if (key != null) {
                try {
                    // 尝试从语言包中获取指定键的消息
                    result = resourceBundle.getString(key);
                } catch (MissingResourceException e) {
                    // 如果找不到对应的资源键，记录警告日志
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Error getting string from ResourceBundle with key: " + key, e);
                    }
                }
            }
        }
        // 如果没有从资源包中获取到消息，则使用备用供应商提供的消息
        if (result == null && fallback != null) {
            result = fallback.get();
        }
        // 应用格式化参数
        if (result != null && args.length > 0) {
            result = MessageFormat.format(result, args);
        }
        // 如果最终结果仍为 null，返回空字符串
        return result == null ? "" : result;
    }

    /**
     * 获取格式化参数。返回数组副本，防止外部代码修改内部args数组，保护内部状态。
     *
     * @return 格式化参数数组副本
     */
    @Override
    public Object[] args() {
        // 返回 args 数组的副本，防止外部修改影响内部状态
        return args.clone();
    }

    /**
     * 判断两个 InternationalMessage 对象是否相等。
     * <p>
     * 比较逻辑：
     * <ul>
     *     <li>检查对象是否为 InternationalMessage 类型</li>
     *     <li>比较 args 参数数组的内容是否相同（deepEquals支持数组）</li>
     *     <li>比较 key 是否相同</li>
     *     <li>比较 international 是否相同</li>
     * </ul>
     * <p>注意：fallback是Supplier，这里equals没有比较fallback对象；record原生equals会比较全部组件，本处重写自定义逻辑。
     *
     * @param o 待比较的对象
     * @return 如果两个对象相等则返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InternationalMessage that)) return false;
        // 使用 deepEquals 比较参数数组的内容，使用 equals 比较其他字段
        return Objects.deepEquals(args, that.args)
                && Objects.equals(key, that.key)
                && Objects.equals(international, that.international);
    }

    /**
     * 计算 InternationalMessage 对象的哈希码。
     * <p>
     * 哈希码基于以下属性计算：
     * <ul>
     *     <li>bundleManager - 国际化资源管理器</li>
     *     <li>key - 资源文件中的键</li>
     *     <li>args - 格式化参数数组的哈希码</li>
     * </ul>
     *
     * @return 对象的哈希码值
     */
    @Override
    public int hashCode() {
        // 使用 bundleManager、key 和 args 的哈希码组合计算整体哈希码
        return Objects.hash(international, key, Arrays.hashCode(args));
    }

    /**
     * 返回 InternationalMessage 对象的字符串表示形式，用于调试打印。
     * <p>
     * 字符串格式为：[key]<bundleManager>
     * 其中 key 是资源文件中的键，bundleManager 是国际化资源管理器
     *
     * @return InternationalMessage 对象的字符串表示
     */
    @Override
    public String toString() {
        // 返回格式化的字符串表示，便于调试和日志记录
        return "[" + key + "]<" + international + ">";
    }
}
