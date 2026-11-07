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

import java.io.Serial;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 支持国际化语言运行异常。
 * <p>
 * 这个异常类扩展了标准的Exception，提供了国际化消息支持，
 * 允许根据不同的语言环境显示不同的错误消息。
 *
 * <p>关键设计点：
 * <ul>
 *     <li>重写{@link #getLocalizedMessage()}返回国际化翻译后的文本；{@link #getMessage()}返回原始默认模板字符串</li>
 *     <li>可变参数args最后一位如果是Throwable，自动识别为异常cause，构建异常链，剩余参数用于MessageFormat格式化</li>
 *     <li>反射修改Throwable.detailMessage字段，让部分日志框架打印时直接展示默认本地化消息</li>
 *     <li>内部持有{@link InternationalMessage}对象，真正承担多语言解析、格式化逻辑</li>
 *     <li>默认资源baseName：META‑INF/i18n/exceptions</li>
 * </ul>
 *
 * @author Terraqo
 */
public class InternationalException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 缓存Throwable类的detailMessage反射Field。volatile保证多线程可见性；延迟初始化。
     * detailMessage是Throwable内部存储getMessage返回值的私有字段。
     */
    private static volatile Field DETAIL_MESSAGE_FIELD = null;

    /**
     * 存储国际化消息对象，用于提供不同语言环境下的错误消息。
     * 所有本地化文本、格式化参数、资源baseName、key都封装在此对象。
     */
    private final InternationalMessage internationalMessage;

    /**
     * 默认构造函数，创建一个没有消息参数的异常实例。
     * <p>
     * 此构造函数调用带参构造函数，传入null值，用于创建一个没有任何消息的异常实例。
     */
    public InternationalException() {
        this(null);
    }

    /**
     * 带消息和参数的构造函数。
     * <p>隐式使用默认baseName=META‑INF/i18n/exceptions，key=null。
     *
     * @param message 异常消息模板，用于定义错误消息的格式
     * @param args    消息格式化参数，最后一个参数可能是Throwable类型的异常原因
     *                参数说明：可变参数列表，其中可能包含用于消息格式化的参数以及异常原因
     */
    public InternationalException(String message, Object... args) {
        this(null, message, args);
    }

    /**
     * 带消息和参数的构造函数。
     * <p>隐式使用默认baseName=META‑INF/i18n/exceptions。
     *
     * @param key     国际化资源文件中消息key
     * @param message 异常消息模板，用于定义错误消息的格式
     * @param args    消息格式化参数，最后一个参数可能是Throwable类型的异常原因
     *                参数说明：可变参数列表，其中可能包含用于消息格式化的参数以及异常原因
     */
    public InternationalException(String key, String message, Object... args) {
        this("META-INF/i18n/exceptions", key, message, args);
    }

    /**
     * 完整参数构造函数。
     *
     * @param baseName 国际化资源包基础名称
     * @param key      资源文件中的消息key
     * @param message  异常消息模板（fallback默认文本）
     * @param args     格式化参数；args末尾如果是Throwable，自动剥离作为cause，其余用于消息格式化
     */
    public InternationalException(String baseName, String key, String message, Object... args) {
        // 调用父类构造函数设置基本消息，存储原始异常消息字符串
        super(message);
        // 初始化异常原因变量，用于存储可能的嵌套异常
        Throwable throwable = null;
        // 检查是否有参数，并且最后一个参数是否为Throwable类型
        if (args != null && args.length > 0) {
            if (args[args.length - 1] instanceof Throwable) {
                // 如果最后一个参数是Throwable，则将其作为异常原因
                // 这种设计允许在抛出异常时同时传递异常原因，符合Java异常链的标准做法
                throwable = (Throwable) args[args.length - 1];
                // 移除最后一个元素（异常原因），保留其他参数
                // 仅保留用于消息格式化的参数，不包括异常原因
                args = Arrays.copyOf(args, args.length - 1);
            }
        } else if (args == null) {
            // 如果参数为null，初始化为空数组
            // 确保args不会为null，避免后续使用时出现空指针异常
            args = new Object[0];
        }
        // 设置异常原因，建立异常链，允许上层捕获者追踪异常的根本原因
        this.initCause(throwable);
        // 创建国际化消息对象，基于当前异常实例和格式化参数生成本地化消息
        this.internationalMessage = InternationalMessage.of(baseName, key, message, args);
        // 使用异常辅助类初始化异常相关信息，执行额外的异常初始化逻辑
        // 传入当前异常实例、null(无特定方法信息)和默认语言的消息文本
        String defaultMessage = internationalMessage.getDefaultMessage();
        Field detailMessageField = getDetailMessageField();
        if (detailMessageField != null && defaultMessage != null) {
            try {
                // 反射修改Throwable私有detailMessage字段，把默认格式化消息塞进去；
                // 部分日志组件会直接读取该字段，而不调用getMessage()/getLocalizedMessage()
                detailMessageField.set(this, defaultMessage);
            } catch (Throwable ignored) {
                // 反射失败直接忽略，不影响异常本体运行
            }
        }
    }

    /**
     * 获取Throwable的detailMessage私有字段，做缓存；多线程安全。
     *
     * @return Field对象；获取失败返回null
     */
    public static Field getDetailMessageField() {
        if (DETAIL_MESSAGE_FIELD == null) {
            try {
                DETAIL_MESSAGE_FIELD = Throwable.class.getDeclaredField("detailMessage");
                // 打开反射访问权限
                DETAIL_MESSAGE_FIELD.setAccessible(true);
            } catch (NoSuchFieldException e) {
                return null;
            }
        }
        return DETAIL_MESSAGE_FIELD;
    }

    /**
     * 获取异常的基本消息文本。
     *
     * @return 返回原始异常消息，即构造函数中传入的message参数
     * 此方法覆盖父类方法，但直接返回父类中存储的消息，不做任何修改
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * 获取本地化的异常消息。
     *
     * @return 返回根据当前语言环境本地化的消息
     * 此方法提供国际化功能，根据系统语言环境返回相应的错误消息
     * 如果当前语言环境的资源包不存在，则返回默认语言的消息
     */
    @Override
    public String getLocalizedMessage() {
        return internationalMessage.getLocalizedMessage();
    }
}
