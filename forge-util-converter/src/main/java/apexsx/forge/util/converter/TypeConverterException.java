package apexsx.forge.util.converter;

/**
 * TypeConverterException 是类型转换过程中发生的异常。
 * 当类型转换失败或遇到不支持的类型转换时，将抛出此异常。
 *
 * @author zhitron
 */
public class TypeConverterException extends RuntimeException {
    /**
     * 构造一个没有详细消息的新 TypeConverterException 实例。
     */
    public TypeConverterException() {
    }

    /**
     * 构造一个带有指定详细消息的新 TypeConverterException 实例。
     *
     * @param message 异常的详细信息
     */
    public TypeConverterException(String message) {
        super(message);
    }

    /**
     * 构造一个带有指定详细消息和原因的新 TypeConverterException 实例。
     *
     * @param message 异常的详细信息
     * @param cause   异常的原因
     */
    public TypeConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个带有指定原因的新 TypeConverterException 实例。
     *
     * @param cause 异常的原因
     */
    public TypeConverterException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个带有指定详细消息、原因、抑制启用标志和可写堆栈跟踪标志的新 TypeConverterException 实例。
     *
     * @param message            异常的详细信息
     * @param cause              异常的原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     */
    public TypeConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
