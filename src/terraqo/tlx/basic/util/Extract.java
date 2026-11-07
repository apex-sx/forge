package pxf.toolkit.basic.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提取操作
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class Extract {

  /** 8位 */
  private static final int BIT_8 = 8;
  /** 16位 */
  private static final int BIT_16 = 16;
  /** 32位 */
  private static final int BIT_32 = 32;
  /** 64位 */
  private static final int BIT_64 = 64;

  private Extract() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 提取异常信息
   *
   * @param throwable 异常
   * @return 返回异常信息
   */
  public static String exceptionInformation(Throwable throwable) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter, true);
    throwable.printStackTrace(printWriter);
    printWriter.flush();
    stringWriter.flush();
    return stringWriter.toString();
  }

  /**
   * 提取文件基础名
   *
   * @param filePath 文件路径
   * @return 返回扩展，不存在返回 {@code ""}
   */
  public static String fileBaseNameFormPath(String filePath) {
    return fileBaseNameFormPath(filePath, "");
  }

  /**
   * 提取文件路径的基础名
   *
   * @param filePath 文件路径
   * @param defaultBaseName 默认基础名
   * @return 返回扩展，不存在返回 {@code defaultBaseName}
   */
  public static String fileBaseNameFormPath(String filePath, String defaultBaseName) {
    int[] location = Find.fileBaseNameLocation(filePath);
    if (location == null) {
      return defaultBaseName;
    }
    return filePath.substring(location[0], location[1]);
  }

  /**
   * 提取文件路径的扩展名
   *
   * @param filePath 文件路径
   * @return 返回扩展，不存在返回 {@code ""}
   */
  public static String fileExtensionFormPath(String filePath) {
    return fileExtensionFormPath(filePath, "");
  }

  /**
   * 提取文件路径的扩展名
   *
   * @param filePath 文件路径
   * @param defaultExtension 默认扩展
   * @return 返回扩展，不存在返回 {@code defaultExtension}
   */
  public static String fileExtensionFormPath(String filePath, String defaultExtension) {
    int i = Find.fileExtensionLocation(filePath) + 1;
    if (i <= 0 || i == filePath.length()) {
      return defaultExtension;
    }
    return filePath.substring(i);
  }

  /**
   * 提取文件名
   *
   * @param filePath 文件路径
   * @return 返回扩展，不存在返回 {@code ""}
   */
  public static String fileNameFormPath(String filePath) {
    return fileNameFormPath(filePath, "");
  }

  /**
   * 提取文件路径的扩展名
   *
   * @param filePath 文件路径
   * @param defaultName 默认扩展
   * @return 返回扩展，不存在返回 {@code defaultName}
   */
  public static String fileNameFormPath(String filePath, String defaultName) {
    if (filePath == null) {
      return defaultName;
    }
    int location = Find.lastPathSeparator(filePath);
    if (location == -1) {
      return filePath;
    }
    return filePath.substring(location);
  }

  /**
   * 提取URL地址中的基本文件名
   *
   * @param urlAddress URL地址
   * @return 返回扩展，不存在返回 {@code ""}
   */
  public static String fileBaseNameFromUrl(String urlAddress) {
    return fileBaseNameFromUrl(urlAddress, "");
  }

  /**
   * 提取URL地址中的基本文件名
   *
   * @param urlAddress URL地址
   * @param defaultBaseName 默认基础名
   * @return 返回扩展，不存在返回 {@code defaultBaseName}
   */
  public static String fileBaseNameFromUrl(String urlAddress, String defaultBaseName) {
    char[] chars = urlAddress.toCharArray();
    int[] results = Extract.fileNameFromUrl(chars);
    if (results[2] == 1) {
      if (results[0] == 0 && results[1] == chars.length) {
        return defaultBaseName;
      } else {
        for (int i = results[1] - 1; i >= results[0]; i--) {
          if (chars[i] == '.') {
            results[1] = i;
          }
        }
        return urlAddress.substring(results[0], results[1]);
      }
    }
    return defaultBaseName;
  }

  /**
   * 提取URL地址中的文件扩展名
   *
   * @param urlAddress URL地址
   * @return 返回扩展，不存在返回 {@code ""}
   */
  public static String fileExtensionFromUrl(String urlAddress) {
    return fileExtensionFromUrl(urlAddress, "");
  }

  /**
   * 提取URL地址中的文件扩展名
   *
   * @param urlAddress URL地址
   * @param defaultExtension 默认扩展
   * @return 返回扩展，不存在返回 {@code defaultExtension}
   */
  public static String fileExtensionFromUrl(String urlAddress, String defaultExtension) {
    char[] chars = urlAddress.toCharArray();
    int[] results = Extract.fileNameFromUrl(chars);
    if (results[2] == 1) {
      if (results[0] == 0 && results[1] == chars.length) {
        return defaultExtension;
      } else {
        int dotI = -1;
        for (int i = results[1] - 1; i >= results[0]; i--) {
          if (chars[i] == '.') {
            dotI = i + 1;
          }
        }
        if (dotI == -1) {
          return defaultExtension;
        }
        return urlAddress.substring(dotI, results[1]);
      }
    }
    return defaultExtension;
  }

  /**
   * 提取URL地址中的文件名
   *
   * @param urlAddress URL地址
   * @return 返回名称，不存在返回 {@code ""}
   */
  public static String fileNameFromUrl(String urlAddress) {
    return fileNameFromUrl(urlAddress, "");
  }

  /**
   * 提取URL地址中的文件名
   *
   * @param urlAddress URL地址
   * @param defaultName 默认扩展
   * @return 返回名称，不存在返回 {@code defaultName}
   */
  public static String fileNameFromUrl(String urlAddress, String defaultName) {
    char[] chars = urlAddress.toCharArray();
    int[] results = Extract.fileNameFromUrl(chars);
    if (results[2] == 1) {
      if (results[0] == 0 && results[1] == chars.length) {
        return defaultName;
      } else {
        return urlAddress.substring(results[0], results[1]);
      }
    }
    return defaultName;
  }

  /**
   * 从url解析文件名
   *
   * @param chars 字符数组
   * @return 返回三个元素的 {@code int[]}数组，第一个元素起始索引，第二个元素结束索引，第三个元素状态值
   *     <p>状态为 {@code 0}则协议部分没有解析过去，状态为 {@code 1}则解析到路径部分
   */
  private static int[] fileNameFromUrl(char[] chars) {
    // 解析协议
    int si = 0, ei = chars.length, sign = 0;
    for (int i = 0; i < chars.length; i++) {
      if (sign == 0) {
        if (chars[i] == ':'
            && i + 3 < chars.length
            && Is.pathDelimiter(chars[i + 1], chars[i + 2])) {
          i += 2;
          sign++;
        }
      } else if (sign == 1) {
        if ((chars[i] == '/' || chars[i] == '\\')) {
          si = i + 1;
        } else if (chars[i] == '?') {
          ei = i;
        }
      }
    }
    return new int[] {si, ei, sign};
  }

  /**
   * 提取URL地址中的参数
   *
   * <p>该方法主要用于只需要提取一个参数时使用，如果需要提取多个请使用 {@link #parameterFromUrl(String)}
   *
   * @param urlAddress URL地址
   * @param key 参数键
   * @return 返回{@code String}返回字符串值
   */
  public static String parameterValueFromUrl(String urlAddress, String key) {
    return parameterValueFromUrl(urlAddress, key, "");
  }

  /**
   * 提取URL地址中的参数
   *
   * <p>该方法主要用于只需要提取一个参数时使用，如果需要提取多个请使用 {@link #parameterFromUrl(String)}
   *
   * @param urlAddress URL地址
   * @param key 参数键
   * @param defaultValue 默认值
   * @return 返回{@code String}返回字符串值
   */
  public static String parameterValueFromUrl(String urlAddress, String key, String defaultValue) {
    return parameterValueFromUrl(urlAddress, key, defaultValue, true);
  }

  /**
   * 提取URL地址中的参数
   *
   * <p>该方法主要用于只需要提取一个参数时使用，如果需要提取多个请使用 {@link #parameterFromUrl(String)}
   *
   * @param urlAddress URL地址
   * @param key 参数键
   * @param defaultValue 默认值
   * @param isIgnoreCase 是否忽略大小写
   * @return 返回{@code String}返回字符串值
   */
  public static String parameterValueFromUrl(
      String urlAddress, String key, String defaultValue, boolean isIgnoreCase) {
    char[] chars = urlAddress.toCharArray();
    int[] results = Extract.parameterFromUrl(chars);
    int kl = key.length();
    if (results[2] == 2) {
      if (results[0] == 0 && results[1] == chars.length) {
        return defaultValue;
      } else {
        int ksi = results[0], kei = 0, vsi = 0, vei = 0, sign = 0;
        for (int i = results[0]; i < results[1]; i++) {
          if (sign == 0) {
            if (chars[i] == '=') {
              kei = i;
              vsi = kei + 1;
              sign++;
            }
          } else if (sign == 1) {
            if (chars[i] == '&') {
              vei = i;
              sign++;
            }
          } else if (sign == 2) {
            if (ksi != kei) {
              if (Is.equalCharsWithString(chars, ksi, kei, key, kl, isIgnoreCase)) {
                return vsi == vei ? "" : new String(chars, vsi, vei - vsi);
              }
            }
            ksi = i;
            sign = 0;
          }
        }
        vei = results[1];
        if (sign == 1) {
          if (Is.equalCharsWithString(chars, ksi, kei, key, kl, isIgnoreCase)) {
            return vsi == vei ? "" : new String(chars, vsi, vei - vsi);
          }
        }
      }
    }
    return defaultValue;
  }

  /**
   * 提取URL地址中的参数
   *
   * @param urlAddress URL地址
   * @return 返回{@code Prop<String, String>}
   */
  public static Map<String, String> parameterFromUrl(String urlAddress) {
    char[] chars = urlAddress.toCharArray();
    int[] results = Extract.parameterFromUrl(chars);
    if (results[2] == 2) {
      if (results[0] == 0 && results[1] == chars.length) {
        return Collections.emptyMap();
      } else {
        int count = 0;
        for (int i = results[0]; i < results[1]; i++) {
          if (chars[i] == '&') {
            count++;
          }
        }
        Map<String, String> map = new HashMap<>(count + 1, 1);
        int ksi = results[0], kei = 0, vsi = 0, vei = 0, sign = 0;
        for (int i = results[0]; i < results[1]; i++) {
          if (sign == 0) {
            if (chars[i] == '=') {
              kei = i;
              vsi = kei + 1;
              sign++;
            }
          } else if (sign == 1) {
            if (chars[i] == '&') {
              vei = i;
              sign++;
            }
          } else if (sign == 2) {
            if (ksi != kei) {
              map.put(
                  new String(chars, ksi, kei - ksi),
                  vsi == vei ? "" : new String(chars, vsi, vei - vsi));
            }
            ksi = i;
            sign = 0;
          }
        }
        vei = results[1];
        if (sign == 1) {
          map.put(
              new String(chars, ksi, kei - ksi),
              vsi != vei ? "" : new String(chars, vsi, vei - vsi));
        }
        return map;
      }
    }
    return Collections.emptyMap();
  }

  /**
   * 从url解析参数
   *
   * @param chars 字符数组
   * @return 返回三个元素的 {@code int[]}数组，第一个元素起始索引，第二个元素结束索引，第三个元素状态值
   *     <p>状态为 {@code 0}则协议部分没有解析过去，状态为 {@code 1}则解析到路径部分，状态为 {@code 2}解析到参数部分
   */
  private static int[] parameterFromUrl(char[] chars) {
    // 解析协议
    int si = 0, ei = chars.length, sign = 0;
    for (int i = 0; i < chars.length; i++) {
      if (sign == 0) {
        if (chars[i] == ':'
            && i + 3 < chars.length
            && Is.pathDelimiter(chars[i + 1], chars[i + 2])) {
          i += 2;
          sign++;
        }
      } else if (sign == 1) {
        if (chars[i] == '?') {
          si = i + 1;
          sign++;
        }
      } else if (sign == 2) {
        if (chars[i] == '#') {
          ei = i;
        }
      }
    }
    return new int[] {si, ei, sign};
  }

  /**
   * 提取对象类必须继承指定类型并且等于指定泛型
   *
   * @param object 被提取对象
   * @param type 指定类型
   * @param genericLength 泛型长度
   * @param indexGeneric 要比较的泛型索引
   * @return 如果符合要求返回 {@code object}的 {@code Class}，否则返回 {@code null}
   */
  public static Class<?> classForInheritTypeAndEqualGeneric(
      Object object, Class<?> type, int genericLength, int indexGeneric) {
    if (indexGeneric >= genericLength) {
      throw new IllegalArgumentException(
          "Specify the generic position must be less than the generic length");
    }
    Class<?> objectClass = object.getClass();
    if (!type.isAssignableFrom(objectClass)) {
      return null;
    }
    Type[] genericInterfaces = objectClass.getGenericInterfaces();
    for (Type genericInterface : genericInterfaces) {
      if (!(genericInterface instanceof ParameterizedType)) {
        continue;
      }
      ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
      Type rawType = parameterizedType.getRawType();
      if (rawType == type) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length != genericLength) {
          throw new IllegalArgumentException(
              "The specified generic length must be equal to the target type generic length");
        }
        if (actualTypeArguments[indexGeneric] == objectClass) {
          return objectClass;
        }
      }
    }
    return null;
  }

  /**
   * 获取父类泛型类型
   *
   * @param fromType 从哪个类型获取
   * @param index 获取第几个
   * @return {@code Class<?>}
   * @throws IllegalArgumentException 如果索引超出泛型个数
   */
  public static Class<?> genericFromSuperclass(Class<?> fromType, int index) {
    List<Class<?>> list = new ArrayList<>();
    for (Class<?> from = fromType; from != null; from = from.getSuperclass()) {
      list.add(from);
      Type superclass = from.getGenericSuperclass();
      Type[] actualTypeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
      if (actualTypeArguments.length <= index) {
        continue;
      }
      try {
        return Class.forName(((Class<?>) actualTypeArguments[index]).getName());
      } catch (ClassNotFoundException ignored) {
      }
    }
    throw new IllegalArgumentException(
        String.format("No generic class found in [%d] on the parent class of %s", index, list));
  }

  /**
   * 提取位值
   *
   * @param value 值
   * @param whichBit 哪位数据
   * @return 返回那一位的真值
   */
  public static boolean bitVal(byte value, int whichBit) {
    if (whichBit < 1 || whichBit > BIT_8) {
      throw new IllegalArgumentException("The specified number of bits must be between 1 and 8");
    }
    return Extract.bit(value, whichBit);
  }

  /**
   * 提取位值
   *
   * @param value 值
   * @param whichBit 哪位数据
   * @return 返回那一位的真值
   */
  public static boolean bitVal(short value, int whichBit) {
    if (whichBit < 1 || whichBit > BIT_16) {
      throw new IllegalArgumentException("The specified number of bits must be between 1 and 16");
    }
    return Extract.bit(value, whichBit);
  }

  /**
   * 提取位值
   *
   * @param value 值
   * @param whichBit 哪位数据
   * @return 返回那一位的真值
   */
  public static boolean bitVal(int value, int whichBit) {
    if (whichBit < 1 || whichBit > BIT_32) {
      throw new IllegalArgumentException("The specified number of bits must be between 1 and 32");
    }
    return Extract.bit(value, whichBit);
  }

  /**
   * 提取位值
   *
   * @param value 值
   * @param whichBit 哪位数据
   * @return 返回那一位的真值
   */
  public static boolean bitVal(long value, int whichBit) {
    if (whichBit < 1 || whichBit > BIT_64) {
      throw new IllegalArgumentException("The specified number of bits must be between 1 and 64");
    }
    return Extract.bit(value, whichBit);
  }

  /**
   * 提取8位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @return 返回8位的值
   */
  public static int bitValFor4(int value, int whichGroup) {
    return Extract.bitValForN(value, whichGroup, 2, 8, 0xF);
  }

  /**
   * 提取4位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @return 返回4位的值
   */
  public static int bitValFor4(long value, int whichGroup) {
    return Extract.bitValForN(value, whichGroup, 2, 16, 0xF);
  }

  /**
   * 提取8位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @return 返回8位的值
   */
  public static int bitValFor8(int value, int whichGroup) {
    return Extract.bitValForN(value, whichGroup, 3, 4, 0xFF);
  }

  /**
   * 提取8位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @return 返回8位的值
   */
  public static int bitValFor8(long value, int whichGroup) {
    return Extract.bitValForN(value, whichGroup, 3, 8, 0xFF);
  }

  /**
   * 提取16位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @return 返回16位的值
   */
  public static int bitValFor16(int value, int whichGroup) {
    return Extract.bitValForN(value, whichGroup, 4, 2, 0xFFFF);
  }

  /**
   * 提取16位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @return 返回16位的值
   */
  public static int bitValFor16(long value, int whichGroup) {
    return Extract.bitValForN(value, whichGroup, 4, 4, 0xFFFF);
  }

  /**
   * 提取32位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @return 返回32位的值
   */
  public static int bitValFor32(long value, int whichGroup) {
    return Extract.bitValForN(value, whichGroup, 5, 2, 0xFFFF_FFFF);
  }

  /**
   * 提取位值
   *
   * @param value 值
   * @param whichBit 哪位数据
   * @return 返回那一位的真值
   */
  private static boolean bit(long value, int whichBit) {
    int bitOffset = whichBit - 1;
    long i = (value & (1L << bitOffset)) >>> bitOffset;
    return value == 1;
  }

  /**
   * 提取N位值
   *
   * @param value 值
   * @param whichGroup 数据哪组
   * @param bitPower 几次幂
   * @param totalGroup 总计多少组
   * @param mark 掩码
   * @return 返回N位的值
   */
  private static int bitValForN(
      long value, int whichGroup, int bitPower, int totalGroup, int mark) {
    if (whichGroup < 1 || whichGroup > totalGroup) {
      throw new IllegalArgumentException("The whichGroup must be in range from 1 to " + totalGroup);
    }
    return (int) ((value >> ((whichGroup - 1) << bitPower)) & mark);
  }
}
