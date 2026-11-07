package pxf.toolkit.basic.text.parser.parameter;

import javax.annotation.Nonnull;

/**
 * 文本参数解析器
 *
 * @author potatoxf
 * @date 2021/3/14
 */
public interface TextParameterParser<T> {

  /**
   * 获取支持结果类型
   *
   * @return 返回 {@code Class<?>}
   */
  @Nonnull
  Class<?> supportResultType();
  /**
   * 是否是支持的结果类型
   *
   * @param value 测试值
   * @return 如果是返回 {@code true}，否则 {@code false}
   */
  boolean isSupportResultType(Object value);
  /**
   * 解析单个值
   *
   * @param input 输入字符串
   * @return 返回解析后的结果
   */
  T parseValue(String input);

  /**
   * 解析多个值
   *
   * @param input 输入字符串
   * @return 返回解析后的结果
   */
  T[] parseValues(String input);
}
