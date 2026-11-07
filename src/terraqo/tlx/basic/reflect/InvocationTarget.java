package pxf.toolkit.basic.reflect;

import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import pxf.toolkit.basic.exception.ExpressionException;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.basic.util.Resolve;

/**
 * 调用目标信息
 *
 * <p>该类是集合调用目标方法信息，方便以多种方式调用目标方法
 *
 * @author potatoxf
 * @date 2021/5/7
 */
public final class InvocationTarget {

  /** 目标类型，如果是一个类则有该值，不是类则将名称写入 {@code typeName} */
  private Class<?> type;
  /** 目标类名称，如果是一个类则为 {@code null} */
  private String typeName;
  /** 方法名，如果没有则为 {@code null} */
  private String methodName;
  /** 方法字符串参数 */
  private String[] paramValues;
  /** 方法真实参数 */
  private Object[] realParamValues;

  private InvocationTarget() {}

  /**
   * 构造调用目标信息
   *
   * @param action 字符串
   * @return {@code InvocationTarget}
   */
  @Nonnull
  public static InvocationTarget of(@Nonnull String action) {
    InvocationTarget invocationTarget = new InvocationTarget();
    action = action.trim();
    int ps = action.indexOf("(");
    if (ps < 0) {
      ps = action.indexOf(" ");
    }
    String target = action;
    if (ps > 0) {
      int methodNameStartIndex = action.lastIndexOf(".", ps);
      if (methodNameStartIndex < 0) {
        throw new ExpressionException("Missing method call expression");
      }
      invocationTarget.methodName = action.substring(methodNameStartIndex + 1, ps).trim();
      invocationTarget.paramValues =
          Resolve.arrayExpression(action.substring(ps), ',', '\\', '(', ')');
      target = action.substring(0, methodNameStartIndex).trim();
    }
    try {
      invocationTarget.type = Class.forName(target, false, ClassLoader.getSystemClassLoader());
      if (invocationTarget.methodName != null) {
        Object[] param = invocationTarget.paramValues;
        invocationTarget.realParamValues =
            ReflectHelper.getMethodRealParams(
                invocationTarget.type, invocationTarget.methodName, param);
      }
    } catch (ClassNotFoundException e) {
      invocationTarget.type = null;
      invocationTarget.typeName = target;
    } catch (NoSuchMethodException e) {
      invocationTarget.methodName = null;
    }
    return invocationTarget;
  }

  public boolean isClass() {
    return type != null;
  }

  public boolean hasMethod() {
    return methodName != null;
  }

  public boolean isInstance(Class<?> clz) {
    if (isClass()) {
      return clz.isAssignableFrom(type);
    }
    return false;
  }

  public Class<?> getType() {
    return type;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getMethodName() {
    return methodName;
  }

  public String[] getParamValues() {
    return paramValues;
  }

  public Object[] getRealParamValues() {
    return realParamValues;
  }

  @Override
  public String toString() {
    if (type != null && methodName != null) {
      return type.getName()
          + "."
          + methodName
          + "("
          + (Is.empty(paramValues) ? "" : StringUtils.join(paramValues))
          + ")";
    } else if (type != null) {
      return type.getName();
    } else if (typeName != null && methodName != null) {
      return typeName
          + "."
          + methodName
          + "("
          + (Is.empty(paramValues) ? "" : StringUtils.join(paramValues))
          + ")";
    } else if (typeName != null) {
      return typeName;
    }
    return "";
  }
}
