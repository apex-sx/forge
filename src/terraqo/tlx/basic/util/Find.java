package pxf.toolkit.basic.util;

/**
 * 查找操作
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class Find {

  private Find() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 查找最后一个路径分割符的位置
   *
   * @param fileDirectoryPath 文件目录路径
   * @return 返回索引位置，不存在返回-1
   */
  public static int lastPathSeparator(String fileDirectoryPath) {
    if (fileDirectoryPath == null) {
      return -1;
    }
    int lastUnixPos = fileDirectoryPath.lastIndexOf('/');
    int lastWindowsPos = fileDirectoryPath.lastIndexOf('\\');
    return Math.max(lastUnixPos, lastWindowsPos);
  }

  /**
   * 查找扩展位置
   *
   * @param filePath 文件路径
   * @return 返回索引位置，不存在返回-1
   */
  public static int fileExtensionLocation(String filePath) {
    if (filePath == null) {
      return -1;
    }
    int extensionPos = filePath.lastIndexOf('.');
    int lastSeparator = lastPathSeparator(filePath);
    return (lastSeparator > extensionPos ? -1 : extensionPos);
  }

  /**
   * 查找扩展位置
   *
   * @param filePath 文件路径
   * @return 返回前后索引位置，{@code int{start,end}}
   */
  public static int[] fileBaseNameLocation(String filePath) {
    if (filePath == null) {
      return null;
    }
    int start = lastPathSeparator(filePath) + 1;
    int end = filePath.lastIndexOf('.');
    if (start <= 0 && end == -1) {
      start = 0;
      end = filePath.length();
    } else if (end < 0 || start > end) {
      end = filePath.length();
    } else if (start <= 0) {
      start = 0;
    }
    return new int[] {start, end};
  }

  /**
   * 取最小值
   *
   * @param <T> 元素类型
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static <T extends Comparable<? super T>> T min(T[] numberArray) {
    T min = numberArray[0];
    for (T t : numberArray) {
      if (Compare.objectOrComparableForPriorityNull(min, t) > 0) {
        min = t;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static long min(long... numberArray) {
    long min = numberArray[0];
    for (long l : numberArray) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static int min(int... numberArray) {
    int min = numberArray[0];
    for (int value : numberArray) {
      if (min > value) {
        min = value;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static short min(short... numberArray) {
    short min = numberArray[0];
    for (short value : numberArray) {
      if (min > value) {
        min = value;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static char min(char... numberArray) {
    char min = numberArray[0];
    for (char c : numberArray) {
      if (min > c) {
        min = c;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static byte min(byte... numberArray) {
    byte min = numberArray[0];
    for (byte b : numberArray) {
      if (min > b) {
        min = b;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static double min(double... numberArray) {
    double min = numberArray[0];
    for (double v : numberArray) {
      if (min > v) {
        min = v;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param numberArray 数字数组
   * @return 最小值
   */
  public static float min(float... numberArray) {
    float min = numberArray[0];
    for (float v : numberArray) {
      if (min > v) {
        min = v;
      }
    }
    return min;
  }

  /**
   * 取最大值
   *
   * @param <T> 元素类型
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static <T extends Comparable<? super T>> T max(T[] numberArray) {
    T max = numberArray[0];
    for (T t : numberArray) {
      if (Compare.objectOrComparableForPriorityNull(max, t) < 0) {
        max = t;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static long max(long... numberArray) {

    long max = numberArray[0];
    for (long l : numberArray) {
      if (max < l) {
        max = l;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static int max(int... numberArray) {
    int max = numberArray[0];
    for (int j : numberArray) {
      if (max < j) {
        max = j;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static short max(short... numberArray) {
    short max = numberArray[0];
    for (short value : numberArray) {
      if (max < value) {
        max = value;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static char max(char... numberArray) {
    char max = numberArray[0];
    for (char c : numberArray) {
      if (max < c) {
        max = c;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static byte max(byte... numberArray) {
    byte max = numberArray[0];
    for (byte b : numberArray) {
      if (max < b) {
        max = b;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static double max(double... numberArray) {

    double max = numberArray[0];
    for (double v : numberArray) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param numberArray 数字数组
   * @return 最大值
   */
  public static float max(float... numberArray) {
    float max = numberArray[0];
    for (float v : numberArray) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }
}
