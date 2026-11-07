package pxf.toolkit.basic.text.parser.parameter;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * {@code String}文本参数解析器
 *
 * <p>该类线程安全
 *
 * @author potatoxf
 * @date 2021/3/14
 */
@ThreadSafe
public class ClassTextParameterParser extends AbstractTextParameterParser<Class<?>> {

  public ClassTextParameterParser(char arraySplit, Class<?> defaultValue) {
    super(arraySplit, defaultValue);
  }

  public ClassTextParameterParser(char arraySplit, Supplier<Class<?>> defaultValueSupplier) {
    super(arraySplit, defaultValueSupplier);
  }

  @Override
  public boolean isSupportResultType(Object value) {
    return value instanceof Class;
  }

  @Override
  protected Class<?> doParseValue(@Nonnull String input) {
    try {
      return Class.forName(input, false, ClassLoader.getSystemClassLoader());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected Class<?>[] createArray(int length) {
    return new Class<?>[length];
  }
}
