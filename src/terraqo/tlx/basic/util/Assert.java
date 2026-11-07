package pxf.toolkit.basic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 断言类
 *
 * @author potatoxf
 * @date 2021/4/21
 */
public final class Assert {

  private static final Logger LOG = LoggerFactory.getLogger(Assert.class);

  // --------------------------------------------------------------------------- 类型

  private static final String NUMBER = "number";
  private static final String ITERATOR = "iterator";
  private static final String ARRAY = "array";
  private static final String CONDITION = "condition";
  private static final String OBJECT = "object";
  private static final String COLLECTION = "collection";
  private static final String MAP = "map";
  private static final String STRING = "string";
  /** 模板前缀符号 */
  private static final String TEMPLATE_START_SYMBOL = "]";
  /** 主体模板 */
  private static final String HOW_SUBJECT_TEMPLATE = "The '%s' must be %s";
  /** 类初始化状态 */
  private static final AtomicBoolean INIT_STATUS_LOCK = new AtomicBoolean(false);
  /** 运行时检查异常类型 */
  private static volatile Class<?> runtimeExceptionType = IllegalArgumentException.class;
  /** 默认构造器 */
  private static volatile Constructor<?> defaultConstructor;
  /** 无参构造器 */
  private static volatile Constructor<?> hasParameterConstructor;

  private Assert() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static void noNull(Object object, Object... args) {
    if (object == null) {
      failSubject(OBJECT, "no null", args);
    }
  }

  public static void beTrue(boolean b, Object... args) {
    if (!b) {
      failSubject(CONDITION, "true", args);
    }
  }

  public static void beFalse(boolean b, Object... args) {
    if (b) {
      failSubject(CONDITION, "false", args);
    }
  }

  public static <K> void contains(Map<? extends K, ?> map, K key, Object... args) {
    if (!map.containsKey(key)) {
      failSubject(MAP, "contains key with" + key, args);
    }
  }

  public static void noEmpty(CharSequence cs, Object... args) {
    if (Is.empty(cs)) {
      failSubject(STRING, "no empty", args);
    }
  }

  public static void noEmpty(Collection<?> coll, Object... args) {
    if (Is.empty(coll)) {
      failSubject(COLLECTION, "no empty", args);
    }
  }

  public static void noEmpty(boolean[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(char[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(byte[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(short[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(int[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(long[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(float[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(double[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(Object[] arr, Object... args) {
    if (Is.empty(arr)) {
      failSubject(ARRAY, "no empty", args);
    }
  }

  public static void noEmpty(Map<?, ?> map, Object... args) {
    if (Is.empty(map)) {
      failSubject(MAP, "no empty", args);
    }
  }

  public static void noEmpty(Iterator<?> iterator, Object... args) {
    if (Is.empty(iterator)) {
      failSubject(ITERATOR, "no empty", args);
    }
  }

  public static void noEmpty(Iterable<?> iterable, Object... args) {
    if (Is.empty(iterable)) {
      failSubject(ITERATOR, "no empty", args);
    }
  }

  public static void positive(int number, Object... args) {
    if (number <= 0) {
      failSubject(NUMBER, "positive", args);
    }
  }

  public static void noPositive(int number, Object... args) {
    if (number > 0) {
      failSubject(NUMBER, "no positive", args);
    }
  }

  public static void negative(int number, Object... args) {
    if (number >= 0) {
      failSubject(NUMBER, "negative", args);
    }
  }

  public static void noNegative(int number, Object... args) {
    if (number < 0) {
      failSubject(NUMBER, "no negative", args);
    }
  }

  public static void positive(long number, Object... args) {
    if (number <= 0) {
      failSubject(NUMBER, "positive", args);
    }
  }

  public static void noPositive(long number, Object... args) {
    if (number > 0) {
      failSubject(NUMBER, "no positive", args);
    }
  }

  public static void negative(long number, Object... args) {
    if (number >= 0) {
      failSubject(NUMBER, "negative", args);
    }
  }

  public static void noNegative(long number, Object... args) {
    if (number < 0) {
      failSubject(NUMBER, "no negative", args);
    }
  }

  public static void positive(double number, Object... args) {
    if (number <= 0) {
      failSubject(NUMBER, "positive", args);
    }
  }

  public static void noPositive(double number, Object... args) {
    if (number > 0) {
      failSubject(NUMBER, "no positive", args);
    }
  }

  public static void negative(double number, Object... args) {
    if (number >= 0) {
      failSubject(NUMBER, "negative", args);
    }
  }

  public static void noNegative(double number, Object... args) {
    if (number < 0) {
      failSubject(NUMBER, "no negative", args);
    }
  }

  public static void positiveNumber(Number number, Object... args) {
    if (number.doubleValue() <= 0) {
      failSubject(NUMBER, "positive", args);
    }
  }

  public static void noPositiveNumber(Number number, Object... args) {
    if (number.doubleValue() > 0) {
      failSubject(NUMBER, "no positive", args);
    }
  }

  public static void negativeNumber(Number number, Object... args) {
    if (number.doubleValue() >= 0) {
      failSubject(NUMBER, "negative", args);
    }
  }

  public static void noNegativeNumber(Number number, Object... args) {
    if (number.doubleValue() < 0) {
      failSubject(NUMBER, "no negative", args);
    }
  }

  public static void correctLocale(String locale, Object... args) {
    Assert.noNull(locale, args);
    for (int i = 0; i < locale.length(); i++) {
      char ch = locale.charAt(i);
      if (ch != ' ' && ch != '_' && ch != '-' && ch != '#' && !Character.isLetterOrDigit(ch)) {
        failSubject("locale", "' ','_','-','#',letter or digit", args);
      }
    }
  }

  /**
   * 失败调用，如果调用该方法则一定会抛出异常
   *
   * @param subjectName 主体名
   * @param howAboutIt 怎么样
   * @param args 参数
   */
  private static void failSubject(
      final String subjectName, final String howAboutIt, Object... args) {
    fail(
        () -> String.format(HOW_SUBJECT_TEMPLATE, subjectName, howAboutIt),
        () -> String.format(HOW_SUBJECT_TEMPLATE, "%s", howAboutIt),
        args);
  }

  /**
   * 失败调用，如果调用该方法则一定会抛出异常
   *
   * @param defaultSupplier 默认信息提供器，当模板信息为空，参数为null，则使用它
   * @param templateSupplier 模板信息提供器，如果参数第一个包含模板前缀，则使用它
   * @param args 参数，第一个参数是消息参数默认字符串，如果不是字符串则使用{@code toString()}转成字符粗， 其余参数都是{@code
   *     String.format}的参数
   */
  private static void fail(
      StringSupplier defaultSupplier, StringSupplier templateSupplier, Object... args) {
    if (args == null) {
      throw throwRuntimeException(defaultSupplier.get());
    } else {
      int len = args.length;
      if (len == 0) {
        throw throwRuntimeException(defaultSupplier.get());
      } else {
        Object arg1 = args[0];
        String message;
        if (arg1 instanceof String) {
          message = (String) arg1;
        } else {
          message = arg1.toString();
        }
        if (Is.empty(message)) {
          throw throwRuntimeException(defaultSupplier.get());
        }
        if (message.startsWith(TEMPLATE_START_SYMBOL)) {
          String templateMessage = templateSupplier.get();
          if (Is.empty(templateMessage)) {
            throw throwRuntimeException(defaultSupplier.get());
          }
          throw throwRuntimeException(
              String.format(templateMessage, message.substring(TEMPLATE_START_SYMBOL.length())));
        }
        Object[] tempArgs;
        if (len == 1) {
          tempArgs = new Object[0];
        } else if (len == 2) {
          tempArgs = new Object[] {args[1]};
        } else {
          int newLen = len - 1;
          tempArgs = new Object[newLen];
          System.arraycopy(args, 1, tempArgs, 0, newLen);
        }
        throw throwRuntimeException(String.format(message, tempArgs));
      }
    }
  }

  /**
   * 抛出运行时异常
   *
   * @return {@code RuntimeException}
   */
  @Nonnull
  private static RuntimeException throwRuntimeException() {
    return throwRuntimeException(null);
  }

  /**
   * 抛出运行时异常
   *
   * @param message 消息
   * @return {@code RuntimeException}
   */
  @Nonnull
  private static RuntimeException throwRuntimeException(String message) {
    Class<?> runtimeExceptionType = getRuntimeExceptionType();
    try {
      Object runtimeException;
      if (message == null) {
        runtimeException = getDefaultConstructor().newInstance();
      } else {
        runtimeException = getHasParameterConstructor().newInstance(message);
      }
      return (RuntimeException) runtimeException;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(
          "The runtime exception class must have a public constructor with default or one cn.cilisi.series.infrastructure.string parameter ",
          e);
    } catch (InstantiationException e) {
      throw new RuntimeException(
          "The runtime exception class must have a public class instantiable", e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取无参构造器
   *
   * @return {@code Constructor<?>}
   */
  private static Constructor<?> getDefaultConstructor() {
    if (defaultConstructor == null) {
      synchronized (Assert.class) {
        try {
          defaultConstructor = runtimeExceptionType.getConstructor();
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(
              "The runtime exception class must have a default constructor", e);
        }
      }
    }
    return defaultConstructor;
  }

  /**
   * 获取有参构造器{@code String}或{@code Object}
   *
   * @return {@code Constructor<?>}
   */
  private static Constructor<?> getHasParameterConstructor() {
    if (hasParameterConstructor == null) {
      synchronized (Assert.class) {
        if (hasParameterConstructor == null) {
          try {
            hasParameterConstructor = runtimeExceptionType.getConstructor(String.class);
          } catch (NoSuchMethodException e) {
            try {
              hasParameterConstructor = runtimeExceptionType.getConstructor(Object.class);
            } catch (NoSuchMethodException e2) {
              throw new RuntimeException(
                  "The runtime exception class must have a constructor with one cn.cilisi.series.infrastructure.string or object parameter");
            }
          }
        }
      }
    }
    return hasParameterConstructor;
  }

  /**
   * 获取运行时异常类型
   *
   * @return {@code Class<?>}
   */
  private static Class<?> getRuntimeExceptionType() {
    if (runtimeExceptionType == null || !INIT_STATUS_LOCK.get()) {
      synchronized (Assert.class) {
        // 状态还没变
        if (runtimeExceptionType == null || !INIT_STATUS_LOCK.get()) {
          try {
            Enumeration<URL> systemResources =
                ClassLoader.getSystemResources(Assert.class.getName());
            boolean b = systemResources.hasMoreElements();
            // 有第一个
            if (b) {
              InputStream inputStream = systemResources.nextElement().openStream();
              LineNumberReader lineNumberReader =
                  new LineNumberReader(new InputStreamReader(inputStream));
              String line = lineNumberReader.readLine();
              if (line != null && !line.isEmpty()) {
                try {
                  Class<?> clz = Class.forName(line);
                  if (RuntimeException.class.isAssignableFrom(clz)) {
                    // 修改类
                    runtimeExceptionType = clz;
                  }
                } catch (ClassNotFoundException e) {
                  if (LOG.isInfoEnabled()) {
                    LOG.info(String.format("No corresponding class found [%s]", line), e);
                  }
                }
              }
            }
            if (systemResources.hasMoreElements()) {
              if (LOG.isDebugEnabled()) {
                String ls = "\n";
                StringBuilder sb = new StringBuilder("Find redundant classes:").append(ls);
                sb.append(systemResources.nextElement()).append(ls);
                while (systemResources.hasMoreElements()) {
                  sb.append(systemResources.nextElement()).append(ls);
                }
                sb.delete(sb.length() - ls.length(), Integer.MAX_VALUE);
                LOG.debug(sb.toString());
              }
            }
          } catch (IOException e) {
            if (LOG.isInfoEnabled()) {
              LOG.info(
                  String.format(
                      "Unable to get file under the classpath for [%s]", Assert.class.getName()),
                  e);
            }
          }
        }
        INIT_STATUS_LOCK.compareAndSet(false, true);
      }
      if (LOG.isInfoEnabled()) {
        LOG.info(
            String.format(
                "The Assert class be initialized by use class for [%s]", runtimeExceptionType));
      }
    }
    return runtimeExceptionType;
  }

  private interface StringSupplier {

    String get();
  }
}
