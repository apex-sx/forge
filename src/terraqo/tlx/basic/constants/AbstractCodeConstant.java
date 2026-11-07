package pxf.toolkit.basic.constants;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统编码常量，并检查系统功能的常量是否重复在范围内
 *
 * @author potatoxf
 * @date 2021/5/3
 */
public abstract class AbstractCodeConstant<T extends AbstractCodeConstant<T>> extends
    AbstractFindableConstant<T> {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCodeConstant.class);
  /**
   * 存储编码常量
   */
  private static final Map<Integer, AbstractCodeConstant<?>> CODE_CONSTANT_MAP = new ConcurrentHashMap<>();
  /**
   * 存储编码范围
   */
  private static final Map<Class<? extends AbstractCodeConstant<?>>, int[]> CODE_CONSTANT_RANGE_MAP = new ConcurrentHashMap<>();

  protected boolean success;

  protected AbstractCodeConstant(int code, String msg) {
    this(code, msg, false);
  }

  protected AbstractCodeConstant(int code, String msg, boolean success) {
    super(code, msg);
    this.success = success;
    registerCodeConstant(this);
  }

  /**
   * 注册类型范围
   *
   * @param clazz {@code Class<? super CodeConstant<?>>}
   * @param start 起始值
   * @param end   结束值
   */
  protected static void registerRange(Class<? extends AbstractCodeConstant<?>> clazz, int start,
      int end) {
    if (start > end) {
      throw new IllegalArgumentException("<CodeConstant> start > end!");
    }

    if (CODE_CONSTANT_RANGE_MAP.containsKey(clazz)) {
      throw new IllegalArgumentException(
          String.format("<CodeConstant> Class:%s already exist!", clazz.getSimpleName()));
    }
    CODE_CONSTANT_RANGE_MAP.forEach((k, v) -> {
      if ((start >= v[0] && start <= v[1]) || (end >= v[0] && end <= v[1])) {
        throw new IllegalArgumentException(String
            .format("<CodeConstant> Class:%s 's id range[%d,%d] has " + "intersection with "
                    + "class:%s", clazz.getSimpleName(), start, end,
                k.getSimpleName()));
      }
    });

    CODE_CONSTANT_RANGE_MAP.put(clazz, new int[]{start, end});

    // 提前初始化static变量，进行范围检测
    Field[] fields = clazz.getFields();
    if (fields.length != 0) {
      try {
        fields[0].get(clazz);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        if (LOG.isErrorEnabled()) {
          LOG.error("", e);
        }
      }
    }
  }

  /**
   * 注册编码常量
   *
   * @param abstractCodeConstant {@code AbstractCodeConstant<?>}
   */
  protected static void registerCodeConstant(AbstractCodeConstant<?> abstractCodeConstant) {
    int[] idRange = CODE_CONSTANT_RANGE_MAP.get(abstractCodeConstant.getClass());
    if (idRange == null) {
      throw new IllegalArgumentException(String
          .format("<CodeConstant> Class:%s has not been registered!",
              abstractCodeConstant.getClass().getSimpleName()));
    }
    int code = abstractCodeConstant.identity();
    if (code < idRange[0] || code > idRange[1]) {
      throw new IllegalArgumentException(String
          .format("<CodeConstant> Id(%d) out of range[%d,%d], " + "class:%s", code, idRange[0],
              idRange[1], abstractCodeConstant.getClass().getSimpleName()));
    }
    if (CODE_CONSTANT_MAP.containsKey(code)) {
      if (LOG.isErrorEnabled()) {
        LOG.error(String
            .format("<CodeConstant> Id(%d) out of range[%d,%d], " + "class:%s  code is repeat!",
                code, idRange[0], idRange[1], abstractCodeConstant.getClass().getSimpleName()));
      }
      System.exit(0);
    }
    CODE_CONSTANT_MAP.put(code, abstractCodeConstant);
  }

  public int code() {
    return identity();
  }

  public String msg() {
    return comment();
  }

  public boolean isSuccess() {
    return success;
  }

}
