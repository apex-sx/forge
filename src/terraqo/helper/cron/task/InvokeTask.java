package potatoxf.helper.cron.task;

import potatoxf.helper.api.Whether;
import potatoxf.helper.cron.CronException;
import potatoxf.helper.exceptions.UtilException;
import potatoxf.helper.util.ClassLoaderUtil;
import potatoxf.helper.util.ClassUtil;
import potatoxf.helper.util.ReflectUtil;

import java.lang.reflect.Method;

/**
 * 反射执行任务<br>
 * 通过传入类名#方法名，通过反射执行相应的方法<br>
 * 如果是静态方法直接执行，如果是对象方法，需要类有默认的构造方法。
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class InvokeTask implements Task {

  private final Object obj;
  private final Method method;

  /**
   * 构造
   *
   * @param classNameWithMethodName 类名与方法名的字符串表示，方法名和类名使用#隔开或者.隔开
   */
  public InvokeTask(String classNameWithMethodName) {
    int splitIndex = classNameWithMethodName.lastIndexOf('#');
    if (splitIndex <= 0) {
      splitIndex = classNameWithMethodName.lastIndexOf('.');
    }
    if (splitIndex <= 0) {
      throw new UtilException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
    }

    // 类
    final String className = classNameWithMethodName.substring(0, splitIndex);
    if (Whether.blank(className)) {
      throw new IllegalArgumentException("Class name is blank !");
    }
    final Class<?> clazz = ClassLoaderUtil.loadClass(className);
    if (null == clazz) {
      throw new IllegalArgumentException("Load class with name of [" + className + "] fail !");
    }
    this.obj = ReflectUtil.newInstanceIfPossible(clazz);

    // 方法
    final String methodName = classNameWithMethodName.substring(splitIndex + 1);
    if (Whether.blank(methodName)) {
      throw new IllegalArgumentException("Method name is blank !");
    }
    this.method = ClassUtil.getPublicMethod(clazz, methodName);
    if (null == this.method) {
      throw new IllegalArgumentException("No method with name of [" + methodName + "] !");
    }
  }

  @Override
  public void execute() {
    try {
      ReflectUtil.invoke(this.obj, this.method);
    } catch (UtilException e) {
      throw new CronException(e.getCause());
    }
  }
}
