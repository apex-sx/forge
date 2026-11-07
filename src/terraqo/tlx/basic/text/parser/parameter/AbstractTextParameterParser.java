package pxf.toolkit.basic.text.parser.parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.util.Extract;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.basic.util.Split;

/**
 * 抽象文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
abstract class AbstractTextParameterParser<T> implements TextParameterParser<T> {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractTextParameterParser.class);
  /** 数组分割符 */
  private final char arraySplit;
  /** 默认值，与 {@code defaultValueSupplier}指定存在一个 */
  private final T defaultValue;
  /** 默认值提供器，与 {@code defaultValue}指定存在一个 */
  private final Supplier<T> defaultValueSupplier;

  protected AbstractTextParameterParser(char arraySplit, T defaultValue) {
    this.arraySplit = arraySplit;
    this.defaultValue = defaultValue;
    this.defaultValueSupplier = null;
  }

  protected AbstractTextParameterParser(char arraySplit, Supplier<T> defaultValueSupplier) {
    this.arraySplit = arraySplit;
    this.defaultValue = null;
    this.defaultValueSupplier = defaultValueSupplier;
  }

  /**
   * 获取支持结果类型
   *
   * @return 返回 {@code Class<?>}
   */
  @Nonnull
  @Override
  public Class<?> supportResultType() {
    return Extract.genericFromSuperclass(getClass(), 0);
  }

  /**
   * 是否是支持的结果类型
   *
   * @param value 测试值
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  @Override
  public boolean isSupportResultType(Object value) {
    return supportResultType().isInstance(value);
  }

  /**
   * 解析多个值
   *
   * @param input 输入字符串
   * @return 返回解析后的结果
   */
  @Override
  public T[] parseValues(String input) {
    if (Is.empty(input)) {
      return null;
    }
    String[] inputs = Split.simpleString(input, String.valueOf(arraySplit));
    if (Is.empty(inputs)) {
      return null;
    }
    List<T> result = new ArrayList<>(inputs.length);
    return Arrays.stream(inputs).map(this::parseValue).toArray(this::createArray);
  }
  /**
   * 解析单个值
   *
   * @param input 输入字符串
   * @return 返回解析后的结果
   */
  @Override
  public T parseValue(String input) {
    if (Is.empty(input)) {
      return getDefaultValue();
    }
    input = input.trim();
    try {
      return doParseValue(input);
    } catch (Throwable e) {
      if (LOG.isErrorEnabled()) {
        Class<?> generic = Extract.genericFromSuperclass(getClass(), 0);
        LOG.error("An exception occurred when parsing the string {} to {}", input, generic);
      }
      return getDefaultValue();
    }
  }
  /**
   * 解析单个值
   *
   * @param input 输入字符串
   * @return 返回解析后的结果
   */
  protected abstract T doParseValue(@Nonnull String input);

  /**
   * 创建数组
   *
   * @param length 数组长度
   * @return {@code T[]}
   */
  protected abstract T[] createArray(int length);

  /**
   * 获取默认值
   *
   * @return {@code T}
   */
  public final T getDefaultValue() {
    if (defaultValueSupplier != null) {
      return defaultValueSupplier.get();
    }
    if (defaultValue == null) {
      return defaultValue;
    }
    throw new RuntimeException(
        "The 'defaultValue' and 'defaultValueSupplier' at least one is not null");
  }

  public char getArraySplit() {
    return arraySplit;
  }
}
